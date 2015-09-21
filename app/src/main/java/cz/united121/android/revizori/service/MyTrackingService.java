package cz.united121.android.revizori.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.helper.LocationHelper;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/5/2015}
 **/
public class MyTrackingService extends Service implements LocationHelper.LocationHelperInterface {
	public static final String TAG = MyTrackingService.class.getName();
	public static final String SP_RUNNING = "shared_pref_is_running";
	public static final String SERVICE_START = "cz.united121.android.revizori.service.START";
	public static final String SERVICE_STOP = "cz.united121.android.revizori.service.STOP";


	private static final int ID = 211;
	private static int PERIOD = 5000; // repeat period in ms
	/**
	 * If it is true then we call server and checking inspector
	 */
	private static boolean shouldBeRunning = false;

	private LocationHelper mLocationManager;
	private Location mLastKnownPosition;

	/**
	 * We call check position for one position only once ( if there is no location change = no calling )
	 */
	private boolean mValid;
	private Timer mTimer;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand");
		if (intent.getAction().equals(SERVICE_START)) {
			shouldBeRunning = true;
			//To make close button
			Intent closeIntent = new Intent(this, MyTrackingService.class);
			closeIntent.setAction(MyTrackingService.SERVICE_STOP);
			PendingIntent pCloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

			//Main intent - what happen after click on notification
			Intent maintIntent = new Intent(this, MapActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

			PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, maintIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			Notification notification = new NotificationCompat.Builder(this)
					.setContentTitle("Prohledavani okoli proti revizorum")
					.setTicker("Skenovani okoli a hledani rizik vyskytu revizora")
					.setContentText("Revizori")
					.setSmallIcon(R.drawable.a_letter)
					.setLargeIcon(
							BitmapFactory.decodeResource(getResources(), R.drawable.b_letter))
					.setContentIntent(resultPendingIntent)
					.setPriority(NotificationCompat.PRIORITY_MAX)
					.setOngoing(true)
					.addAction(R.drawable.c_letter, "Vypnout", pCloseIntent)
					.build();

			startForeground(ID, notification);

		} else if (intent.getAction().equals(SERVICE_STOP)) {
			shouldBeRunning = false;
			// true means that notification disappear
			stopForeground(true);
			stopSelf();
		}
		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mLocationManager = LocationHelper.getInstance(this);
		mLocationManager.registerListener(this);

		final Context context = this;

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Log.d(TAG, "run == " + (mValid && shouldBeRunning));
				if (mValid && shouldBeRunning) {
					new ControlPosition(context).execute(mLastKnownPosition);
					mValid = false;
				}
			}
		};
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(timerTask, 0, PERIOD);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mTimer.cancel();
		mTimer.purge();
		mLocationManager.removeListener(this);
	}

	@Override
	public void OnLocationChanged(Location location) {
		Log.d(TAG, "OnLocationChanged");
		mLastKnownPosition = location;
		mValid = true;
	}

	@Override
	public void OnLocationGetFailed() {
		Log.d(TAG, "OnLocationGetFailed");
		mValid = false;
	}

	@Override
	public void OnConnectionFailed() {
		Log.d(TAG, "OnConnectionFailed");
		mValid = false;
	}

	public class ControlPosition extends AsyncTask<Location, Void, List<ReportInspector>> {

		public final String TAG = ControlPosition.class.getName();
		private final String RESPONSE_KEY = "RESULT";
		private final String PARAMS_LOCATION = "point";
		private final String PARAMS_DISTANCE = "distance";
		private final String RESPONSE_DATA_KEY = "DATA";

		private final float mDistance = 0.5f;

		private Context mContext;

		public ControlPosition(Context context) {
			mContext = context;
		}

		@Override
		protected List<ReportInspector> doInBackground(Location... params) {
			Log.d(TAG, "doInBackground");
			if (params.length <= 0 || params[0] == null) {
				return new ArrayList<ReportInspector>();
			}
			Map<String, Object> paramsToCloud = new HashMap<>();
			ParseGeoPoint parseGeoPoint = new ParseGeoPoint(params[0].getLatitude(), params[0].getLongitude());
			paramsToCloud.put(PARAMS_LOCATION, parseGeoPoint);
			paramsToCloud.put(PARAMS_DISTANCE, mDistance);
			try {
				HashMap<String, Object> response = ParseCloud.callFunction("downloadRelevantData", paramsToCloud);
				Boolean responseResult = (Boolean) response.get(RESPONSE_KEY);
				List<ReportInspector> inspectorList = (ArrayList<ReportInspector>) response.get(RESPONSE_DATA_KEY);
				return (inspectorList == null || !responseResult) ? (new ArrayList<ReportInspector>()) : (inspectorList);
			} catch (ParseException e) {
				Log.d(TAG, "ParseException exception");
				e.printStackTrace();
			} catch (ClassCastException e) {
				Log.d(TAG, "ClassCastException exception");
				Log.d(TAG, e.getMessage());
				e.printStackTrace();

			}
			return new ArrayList<ReportInspector>();
		}

		@Override
		protected void onPostExecute(List<ReportInspector> nearestInspector) {
			super.onPostExecute(nearestInspector);
			Log.d(TAG, "onPostExecute: nearestInspector.size = " + nearestInspector.size());
			if (nearestInspector.size() != 0) {
				LocationGetter.addReports(nearestInspector);
				Util.makeNotification(mContext, "REVIZOR JE BLIZKO", "V okoli Vasi pozice byl pred nedavnou dobou spatren revizor", "Radeji davejte bacha", MapActivity.class);
			}
		}
	}
}

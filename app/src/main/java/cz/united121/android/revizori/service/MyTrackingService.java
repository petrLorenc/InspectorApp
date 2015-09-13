package cz.united121.android.revizori.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.helper.LocationHelper;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/5/2015}
 **/
public class MyTrackingService extends Service implements LocationHelper.LocationHelperInterface {
	public static final String TAG = MyTrackingService.class.getName();

	private static int PERIOD = 5000; // repeat period in ms

	private LocationHelper mLocationManager;
	private Location mLastKnownPosition;

	private boolean mValid;
	private Timer mTimer = new Timer();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand");
		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		mLocationManager = LocationHelper.getInstance(this);
		mLocationManager.registerListener(this);

		final Context context = this;

		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Log.d(TAG, "run");
				if (mValid) {
					new ControlPosition(context).execute(mLastKnownPosition);
					mValid = false;
				}
			}
		}, 0, 5000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
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

	}

	@Override
	public void OnConnectionFailed() {
		Log.d(TAG, "OnConnectionFailed");

	}

	public class ControlPosition extends AsyncTask<Location, Void, Boolean> {

		public final String TAG = ControlPosition.class.getName();
		public final String RESPONSE_KEY = "RESULT";
		public final String PARAMS_KEY = "point";

		private Context mContext;

		public ControlPosition(Context context) {
			mContext = context;
		}

		@Override
		protected Boolean doInBackground(Location... params) {
			Log.d(TAG, "doInBackground");
			if (params.length <= 0 || params[0] == null) {
				return false;
			}
			Map<String, ParseGeoPoint> paramsToCloud = new HashMap<>();
			ParseGeoPoint parseGeoPoint = new ParseGeoPoint(params[0].getLatitude(), params[0].getLongitude());
			//ParseGeoPoint parseGeoPoint = new ParseGeoPoint(50,77);
			paramsToCloud.put(PARAMS_KEY, parseGeoPoint);
			try {
				HashMap<String, Object> response = ParseCloud.callFunction("isInspectorNearMyPosition", paramsToCloud);
				Boolean responseResult = (Boolean) response.get(RESPONSE_KEY);
				Log.d(TAG, "doInBackground response" + responseResult.toString());
				return responseResult;
			} catch (ParseException e) {
				Log.d(TAG, "doInBackground exception");
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean isInpectorNear) {
			super.onPostExecute(isInpectorNear);
			Log.d(TAG, "onPostExecute" + (isInpectorNear != null ? isInpectorNear.toString() : "It was null"));
			if (isInpectorNear == true) {
				NotificationCompat.Builder inspectorIsNearNotification = new NotificationCompat.Builder(mContext)
						.setSmallIcon(R.drawable.reporting_inspector)
						.setContentTitle("POZOR revizor je blizko")
						.setContentText("nejaky popisny text")
						.setTicker("Pozor revizor byl spatren nedaleko")
						.setAutoCancel(true)
						.setStyle(new NotificationCompat.InboxStyle())
						.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

				Intent intent = new Intent(mContext, MapActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				inspectorIsNearNotification.setContentIntent(resultPendingIntent);
				NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(1, inspectorIsNearNotification.build());
			}
		}
	}
}

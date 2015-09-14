package cz.united121.android.revizori.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

	public class ControlPosition extends AsyncTask<Location, Void, List<ReportInspector>> {

		public final String TAG = ControlPosition.class.getName();
		public final String RESPONSE_KEY = "RESULT";
		public final String PARAMS_KEY = "point";
		public final String RESPONSE_DATA_KEY = "DATA";

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
			Map<String, ParseGeoPoint> paramsToCloud = new HashMap<>();
			ParseGeoPoint parseGeoPoint = new ParseGeoPoint(params[0].getLatitude(), params[0].getLongitude());
			paramsToCloud.put(PARAMS_KEY, parseGeoPoint);
			try {
				HashMap<String, Object> response = ParseCloud.callFunction("isInspectorNearMyPosition", paramsToCloud);
				Boolean responseResult = (Boolean) response.get(RESPONSE_KEY);
				List<ReportInspector> inspectorList = (ArrayList<ReportInspector>) response.get(RESPONSE_DATA_KEY);
				Log.d(TAG, "doInBackground inspectorList" + inspectorList.get(0).getTypeOfVehicle());
				return inspectorList;
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
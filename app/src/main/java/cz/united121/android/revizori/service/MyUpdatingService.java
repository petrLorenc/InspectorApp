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

import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.fragment.FullMapFragment;
import cz.united121.android.revizori.helper.LocationHelper;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/20/2015}
 **/
public class MyUpdatingService extends Service implements LocationHelper.LocationHelperInterface {
	public static final String TAG = MyUpdatingService.class.getName();

	public static final String SERVICE_START = "cz.united121.android.revizori,service.MyUpdatingService.START";
	/**
	 * For forcing service to check for new report in database
	 */
	public static final String SERVICE_FORCE = "cz.united121.android.revizori,service.MyUpdatingService.FORCE_START";
	public static final String SERVICE_STOP = "cz.united121.android.revizori.service.MyUpdatingService.STOP";
	private static Timer mUpdatingTimer = new Timer();
	private LocationHelper mLocationManager;
	private Location mLastKnownPosition;
	/**
	 * To avoid spam the server we provide one report per minute
	 */
	//private int PERIOD_BETWEEN_DELETING = 600 * 1000; // ms
	private int PERIOD_BETWEEN_UPDATING = 10 * 1000; // ms
	//private boolean TIME_APROVAL = false; // ms
	private boolean LOCATION_APROVAL = false; // ms
	private TimerTask mTimeAproving = new TimerTask() {
		@Override
		public void run() {
			Log.d(TAG, "mTimeAproving is running");
			if (!LOCATION_APROVAL) {
				return;
			}
			if (mLastKnownPosition != null) {
				LOCATION_APROVAL = false;
				Log.d(TAG, "mTimeAproving");
				new UpdatingMapAsyncTask(MyUpdatingService.this).execute(mLastKnownPosition);
			}
		}
	};


	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreate");
		}
		mLocationManager = LocationHelper.getInstance(this);
		mLocationManager.registerListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDestroy");
		}
		mLocationManager.removeListener(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand" + intent.getAction());
		if (intent.getAction().equals(SERVICE_FORCE)) {
			LOCATION_APROVAL = true;
			if (mLastKnownPosition != null) {
				mTimeAproving.run();
			}
		} else if (intent.getAction().equals(SERVICE_START)) {
			mUpdatingTimer.scheduleAtFixedRate(mTimeAproving, 0, PERIOD_BETWEEN_UPDATING);
		} else if (intent.getAction().equals(SERVICE_STOP)) {
			LOCATION_APROVAL = false;
			mLastKnownPosition = null;
			mUpdatingTimer.cancel();
			mUpdatingTimer.purge();
			stopSelf();
		}
		return START_NOT_STICKY;
	}

	@Override
	public void OnLocationChanged(Location location) {
		Log.d(TAG, "OnLocationChanged");
		if (location != null && !location.equals(mLastKnownPosition)) {
			mLastKnownPosition = location;
			LOCATION_APROVAL = true;
		}
	}

	@Override
	public void OnLocationGetFailed() {
		Log.d(TAG, "OnLocationGetFailed");
		LOCATION_APROVAL = false;
	}

	@Override
	public void OnConnectionFailed() {
		Log.d(TAG, "OnConnectionFailed");
		LOCATION_APROVAL = false;
	}

	public class UpdatingMapAsyncTask extends AsyncTask<Location, Void, List<ReportInspector>> {

		public final String TAG = UpdatingMapAsyncTask.class.getName();
		private final String RESPONSE_KEY = "RESULT";
		private final String RESPONSE_DATA_KEY = "DATA";
		private final String PARAMS_LOCATION = "point";
		private final String PARAMS_DISTANCE = "distance";

		private final float mDistanceInKm = 20.0f;

		private Context mContext;

		public UpdatingMapAsyncTask(Context context) {
			mContext = context;
		}

		//Class cast exception will be catch
		@SuppressWarnings("unchecked")
		@Override
		protected List<ReportInspector> doInBackground(Location... params) {
			Log.d(TAG, "doInBackground");
			if (params.length <= 0 || params[0] == null) {
				return new ArrayList<>();
			}
			Map<String, Object> paramsToCloud = new HashMap<>();
			ParseGeoPoint parseGeoPoint = new ParseGeoPoint(params[0].getLatitude(), params[0].getLongitude());
			paramsToCloud.put(PARAMS_LOCATION, parseGeoPoint);
			paramsToCloud.put(PARAMS_DISTANCE, mDistanceInKm);
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
			return new ArrayList<>();
		}

		@Override
		protected void onPostExecute(List<ReportInspector> nearestInspector) {
			super.onPostExecute(nearestInspector);
			Log.d(TAG, "onPostExecute: nearestInspector.size = " + nearestInspector.size());
			//LocationGetter.deleteRedundantReports(nearestInspector);
			LocationGetter.refreshReportsTo(nearestInspector);
			mContext.sendBroadcast(new Intent(FullMapFragment.BROADCAST_TO_REFRESH_MAP));
		}
	}

}

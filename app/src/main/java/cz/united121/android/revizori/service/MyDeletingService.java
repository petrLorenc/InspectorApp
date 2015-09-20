package cz.united121.android.revizori.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/20/2015}
 **/
public class MyDeletingService extends Service {
	public static final String TAG = MyDeletingService.class.getName();

	public static final String SERVICE_START = "cz.united121.android.revizori,service.MyDeletingService.START";
	public static final String SERVICE_STOP = "cz.united121.android.revizori.service.MyDeletingService.STOP";

	/**
	 * To avoid spam the server we provide one report per minute
	 */
	//private int PERIOD_BETWEEN_DELETING = 600 * 1000; // ms
	private int PERIOD_BETWEEN_DELETING = 30 * 1000; // ms
	private Handler deletingHandler;
	private Runnable deletingTask = new Runnable() {
		@Override
		public void run() {


			deletingHandler.postDelayed(deletingTask, PERIOD_BETWEEN_DELETING);
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
		deletingHandler = new Handler();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onDestroy");
		}
		deletingHandler.removeCallbacks(deletingTask);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (intent.getAction().equals(SERVICE_START)) {
			deletingHandler.postDelayed(deletingTask, PERIOD_BETWEEN_DELETING);
		} else if (intent.getAction().equals(SERVICE_STOP)) {

			deletingHandler.removeCallbacksAndMessages(null);
			stopSelf();
		}
		return START_STICKY;
	}

	public class ControlPosition extends AsyncTask<Void, Void, List<ReportInspector>> {

		public final String TAG = ControlPosition.class.getName();
		public final String RESPONSE_KEY = "RESULT";
		public final String PARAMS_KEY = "point";
		public final String RESPONSE_DATA_KEY = "DATA";

		@Override
		protected List<ReportInspector> doInBackground(Void... params) {
			try {
				HashMap<String, Object> response = ParseCloud.callFunction("toRemoveFromMapInspector", null);
				Boolean responseResult = (Boolean) response.get(RESPONSE_KEY);
				List<ReportInspector> inspectorList = (ArrayList<ReportInspector>) response.get(RESPONSE_DATA_KEY);
				return (inspectorList == null) ? (new ArrayList<ReportInspector>()) : (inspectorList);
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
				LocationGetter.deleteReports(nearestInspector);
			}
		}
	}

}

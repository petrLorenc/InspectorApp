package cz.united121.android.revizori.listeners;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.Map;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/5/2015}
 **/
public class MyServiceLocationListener implements LocationListener {
	public static final String TAG = MyServiceLocationListener.class.getName();

	public static boolean isLocationEnabled = false;

	private Context mContext;
	private Location mLastKnownPosition = null;

	public MyServiceLocationListener(Context context) {
		mContext = context;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged" + isLocationEnabled);
		mLastKnownPosition = location;
		if(isLocationEnabled){
			new ControlPosition(mContext).execute(mLastKnownPosition);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, "onStatusChanged");

	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled");
		isLocationEnabled = true;
		new ControlPosition(mContext).execute(mLastKnownPosition);
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "onProviderDisabled");
		isLocationEnabled = false;
	}

	public class ControlPosition extends AsyncTask<Location,Void,Boolean> {

		public final String TAG = ControlPosition.class.getName();
		public final String RESPONSE_KEY = "RESULT";
		public final String PARAMS_KEY = "point";

		private Context mContext;

		public ControlPosition(Context context){
			mContext = context;
		}

		@Override
		protected Boolean doInBackground(Location... params) {
			Log.d(TAG, "doInBackground");
			if(params.length <= 0 || params[0] == null){
				return false;
			}
			Map<String,ParseGeoPoint> paramsToCloud = new HashMap<>();
			ParseGeoPoint parseGeoPoint = new ParseGeoPoint(params[0].getLatitude(),params[0].getLongitude());
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
			if(isInpectorNear == true){
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
				PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
				inspectorIsNearNotification.setContentIntent(resultPendingIntent);
				NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(1,inspectorIsNearNotification.build());
			}
		}
	}

}

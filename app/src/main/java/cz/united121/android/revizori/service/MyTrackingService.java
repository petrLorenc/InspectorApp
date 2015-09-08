package cz.united121.android.revizori.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cz.united121.android.revizori.listeners.MyServiceLocationListener;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/5/2015}
 **/
public class MyTrackingService extends Service {
	public static final String TAG = MyTrackingService.class.getName();

	private LocationManager mLocationManager;
	private MyServiceLocationListener mMyServiceLocationListener;

	private static final int MIN_INTERVAL = 2000; // in miliseconds
	private static final float MIN_DISTANCE = 5f; // in metres

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
		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		}
		mMyServiceLocationListener = new MyServiceLocationListener(this);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_INTERVAL, MIN_DISTANCE,mMyServiceLocationListener);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		if(mLocationManager != null){
			mLocationManager.removeUpdates(mMyServiceLocationListener);
		}
	}

}

/*
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

*/
/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/5/2015}
 **//*

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

}
*/

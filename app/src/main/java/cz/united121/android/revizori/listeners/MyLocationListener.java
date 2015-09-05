package cz.united121.android.revizori.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/5/2015}
 **/
public class MyLocationListener implements LocationListener {
	public static final String TAG = MyLocationListener.class.getName();

	private BaseActivity mBaseActivity;

	private Location mLocationBaseOnTime;
	private long timeLastLocationUpdate;
	private long timePenumtimateLocationUpdate;
	private final long TIME_TOLERANCE_IN_SECOND = 10;

	public MyLocationListener(BaseActivity baseActivity) {
		timeLastLocationUpdate = SystemClock.elapsedRealtime();
		timePenumtimateLocationUpdate = 0;
		mBaseActivity = baseActivity;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		timePenumtimateLocationUpdate = timeLastLocationUpdate;
		timeLastLocationUpdate = SystemClock.elapsedRealtime();
		mLocationBaseOnTime = location;

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d(TAG, "onStatusChanged");

	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "onProviderEnabled");

	}

	// Provider is disabled -> call even when start aplication and after that disable Provider
	// (here GPS)
	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "onProviderDisabled");
		Util.makeAlertDialogGPS(mBaseActivity, mBaseActivity.getString(R.string
				.full_map_requesting_enabling_GPS_start));
	}

	/**
	 *
	 * @return null if time between two measurement are too small
	 */
	public Location getValidLocation(){
		if(((timeLastLocationUpdate - timePenumtimateLocationUpdate)/1000) < TIME_TOLERANCE_IN_SECOND){
			return mLocationBaseOnTime;
		}else {
			return null;
		}
	}

	/**
	 *
	 * @return null if time between two measurement are too small
	 */
	public Location getValidLocation(GoogleApiClient googleApiClient){
		Location location = null;
		if(googleApiClient != null
				&
				LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient)
						.isLocationAvailable()
				&
				(location = LocationServices.FusedLocationApi
						.getLastLocation (googleApiClient)) != null){
			return location;
		}
		return null;
	}
}

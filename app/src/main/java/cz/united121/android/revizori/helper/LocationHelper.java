package cz.united121.android.revizori.helper;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/12/2015}
 **/
public class LocationHelper implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static final String TAG = LocationHelper.class.getName();

	private static LocationHelper mLocationHelper;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;

	private List<LocationHelperInterface> mLocationHelperInterfaceList = new ArrayList<>();

	private LocationHelper(Context context) {
		buildGoogleApiClient(context);

		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(5000);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(1000);
		// Set minimal distance between two measurement
		mLocationRequest.setSmallestDisplacement(50);
	}

	public static LocationHelper getInstance(Context context){
		if(mLocationHelper == null){
			mLocationHelper = new LocationHelper(context.getApplicationContext());
		}
		return mLocationHelper;
	}

	private void buildGoogleApiClient(Context context) {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

	public void requestUpdate(){
		if (mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		} else {
			mGoogleApiClient.reconnect();
		}
	}

	public void registerListener(LocationHelperInterface mLocationHelperInterface){
		if (mLocationHelperInterfaceList.size() <= 0) { // add request only when there is some listener - have to manage add and removing
			requestUpdate();
		}
		mLocationHelperInterfaceList.add(mLocationHelperInterface);
	}

	public void removeListener(LocationHelperInterface mLocationHelperInterface){
		mLocationHelperInterfaceList.remove(mLocationHelperInterface);
		if (mLocationHelperInterfaceList.size() <= 0) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			mLocationHelper = null;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");
		for (LocationHelperInterface helperInterface : mLocationHelperInterfaceList) {
			helperInterface.OnLocationChanged(location);
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.d(TAG, "onConnected");
		requestUpdate();
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d(TAG, "onConnectionSuspended");
		for (LocationHelperInterface helperInterface : mLocationHelperInterfaceList) {
			helperInterface.OnConnectionFailed();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed");
		for (LocationHelperInterface helperInterface : mLocationHelperInterfaceList) {
			helperInterface.OnConnectionFailed();
		}
	}

	public interface LocationHelperInterface{
		void OnLocationChanged(Location location);
		void OnLocationGetFailed();

		void OnConnectionFailed();
	}
}

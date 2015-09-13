package cz.united121.android.revizori.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import cz.united121.android.revizori.App;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/12/2015}
 **/
public class LocationHelper implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	public static final String TAG = LocationHelper.class.getName();

	private static LocationHelper mLocationHelper;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private LocationManager locationManager;

	private List<LocationHelperInterface> mLocationHelperInterfaceList = new ArrayList<>();

	public static LocationHelper getInstance(Context context){
		if(mLocationHelper == null){
			mLocationHelper = new LocationHelper(context);
		}
		return mLocationHelper;
	}

	private LocationHelper(Context context){
		buildGoogleApiClient(context);

		mLocationRequest =  LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(5000);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(1000);
	}

	private void buildGoogleApiClient(Context context) {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	public void requestUpdate(){
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
	}

	public void registerListener(LocationHelperInterface mLocationHelperInterface){
		mLocationHelperInterfaceList.add(mLocationHelperInterface);
	}

	public void removeListener(LocationHelperInterface mLocationHelperInterface){
		mLocationHelperInterfaceList.remove(mLocationHelperInterface);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");

	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.d(TAG, "onConnected");

	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d(TAG, "onConnectionSuspended");

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed");

	}

	public interface LocationHelperInterface{
		void OnLocationChanged();
		void OnLocationGetFailed();
	}
}

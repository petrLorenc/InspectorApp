package cz.united121.android.revizori.listeners;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.List;

import cz.united121.android.revizori.model.ReportInspector;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/6/2015}
 **/
public class MyCameraChangedListener implements GoogleMap.OnCameraChangeListener {
	public static final String TAG = MyCameraChangedListener.class.getName();

	private GoogleMap mGoogleMap;
	private MyLocationListener mMyLocationListener;

	public MyCameraChangedListener(GoogleMap googleMap, MyLocationListener myLocationListener) {
		mGoogleMap = googleMap;
		mMyLocationListener = myLocationListener;
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		Log.d(TAG, "onCameraChange");
		doMapUpdate();
	}

	private void doMapUpdate() {
		ParseQuery<ReportInspector> queryOnInspectorPosition = ReportInspector.getQuery();
		Location location = mMyLocationListener.getValidLocation();
		if(location == null){
			return;
		}
		ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
		queryOnInspectorPosition.whereWithinKilometers("Location", parseGeoPoint, 0.5);
		queryOnInspectorPosition.findInBackground(new FindCallback<ReportInspector>() {
			@Override
			public void done(List<ReportInspector> list, ParseException e) {
				Log.d(TAG, "done");
				if(e != null){
					Log.d(TAG,"error in findInBackground");
					return;
				}
				for(int i = 0 ; i < list.size() ; i++){
					mGoogleMap.addMarker(new MarkerOptions()
							.position(new LatLng(list.get(i).getLocation().getLatitude(), list.get(i).getLocation().getLongitude()))
							.title("Hello world"));
				}
			}
		});
	}
}

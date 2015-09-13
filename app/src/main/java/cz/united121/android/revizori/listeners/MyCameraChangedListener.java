package cz.united121.android.revizori.listeners;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.List;

import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/6/2015}
 **/
public class MyCameraChangedListener implements GoogleMap.OnCameraChangeListener {
	public static final String TAG = MyCameraChangedListener.class.getName();

	private ClusterManager mClusterManager;
	private ParseQuery<ReportInspector> mLastQuery;


	public MyCameraChangedListener(ClusterManager clusterManager) {
		mClusterManager = clusterManager;
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		Log.d(TAG, "onCameraChange");
		doMapUpdate(cameraPosition.target,cameraPosition.zoom);
	}

	private void doMapUpdate(LatLng location, double zoomLevel) {
		ParseQuery<ReportInspector> queryOnInspectorPosition = ReportInspector.getQuery();
		if(location == null){
			return;
		}
		ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.latitude,location.longitude);
		//TODO hardCoded screenWidth
		queryOnInspectorPosition.whereWithinKilometers("Location", parseGeoPoint, getRadiusToGivenZoom(720, zoomLevel));
		if(mLastQuery != null){
			queryOnInspectorPosition.whereDoesNotMatchQuery("objectId",mLastQuery);
		}
		queryOnInspectorPosition.findInBackground(new FindCallback<ReportInspector>() {
			@Override
			public void done(List<ReportInspector> list, ParseException e) {
				Log.d(TAG, "done");
				if (e != null) {
					Log.d(TAG, "error in findInBackground");
					e.printStackTrace();
					return;
				}
				LocationGetter.addReports(list);
				mClusterManager.addItems(LocationGetter.getReports());
			}
		});
		mLastQuery = queryOnInspectorPosition;
	}

	private double getRadiusToGivenZoom(double screenWidthInPixel, double zoomLevel){
		double equatorLength = 40075004; // in meters
		double widthInPixels = screenWidthInPixel;
		double metersPerPixel = equatorLength / 256;
		int zoomLevelDef = 1;
		while (zoomLevelDef < zoomLevel) {
			metersPerPixel /= 2;
			zoomLevelDef++;
		}
		Log.d(TAG,"(metersPerPixel * widthInPixels)" + ((metersPerPixel * widthInPixels)/1000)/2);
		return ((metersPerPixel * widthInPixels)/1000)/2;
	}
}

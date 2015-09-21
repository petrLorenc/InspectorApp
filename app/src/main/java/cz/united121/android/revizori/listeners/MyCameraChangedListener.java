/*
package cz.united121.android.revizori.listeners;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;

*/
/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/6/2015}
 **//*

public class MyCameraChangedListener implements GoogleMap.OnCameraChangeListener {
	public static final String TAG = MyCameraChangedListener.class.getName();

	private ClusterManager mClusterManager;

	public final String RESPONSE_KEY = "RESULT";
	public final String RESPONSE_DATA_KEY = "DATA";
	public final String PARAMS_PIINT = "point";
	public final String PARAMS_RADIUS = "radius";

	public MyCameraChangedListener(ClusterManager clusterManager) {
		mClusterManager = clusterManager;
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		Log.d(TAG, "onCameraChange");
		doMapUpdate(cameraPosition.target, cameraPosition.zoom);
	}

	private void doMapUpdate(LatLng location, double zoomLevel) {
		if(location == null){
			return;
		}
		//ParseQuery<ReportInspector> queryOnInspectorPosition = ReportInspector.getQuery();
		ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.latitude,location.longitude);
		//TODO hardCoded screenWidth
		//queryOnInspectorPosition.whereWithinKilometers("Location", parseGeoPoint, getRadiusToGivenZoom(720, zoomLevel) + 0.5);
		Map<String, Object> paramsToCloud = new HashMap<>();
		paramsToCloud.put(PARAMS_PIINT,parseGeoPoint);
		paramsToCloud.put(PARAMS_RADIUS, getRadiusToGivenZoom(720, zoomLevel) + 0.5);

		ParseCloud.callFunctionInBackground("downloadRelevantData", paramsToCloud, new FunctionCallback<HashMap<String, Object>>() {
			@Override
			public void done(HashMap<String, Object> hashResponse, ParseException e) {
				Log.d(TAG, "done");
				if(e != null ) {
					Log.d(TAG, "ParseException in downloadRelevantData");
					Log.d(TAG, e.getMessage());
				}
				if( (Boolean) hashResponse.get(RESPONSE_KEY) == false){
					Log.d(TAG, "downloadRelevantData return false");
					return;
				}
				LocationGetter.addReports((List<ReportInspector>) hashResponse.get(RESPONSE_DATA_KEY));
				mClusterManager.clearItems();
				mClusterManager.addItems(LocationGetter.getReports());
			}
		});
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
*/

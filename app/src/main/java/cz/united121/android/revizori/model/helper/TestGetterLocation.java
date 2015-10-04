package cz.united121.android.revizori.model.helper;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {10/3/2015}
 **/
public class TestGetterLocation {
	public static final String TAG = TestGetterLocation.class.getName();

	private static List<LatLng> markersLocation = new ArrayList<>();
	private static CameraPosition sCameraPosition;

	public static void addLocationMarker(LatLng location) {
		LatLng latLng = new LatLng(location.latitude, location.longitude + Math.random());
		//location.longitude = location.longitude + Math.random();
		markersLocation.add(location);
	}

	public static List<LatLng> getMarkersLocation() {
		return markersLocation;
	}

	public static CameraPosition getCameraPosition() {
		return sCameraPosition;
	}

	public static void setCameraPosition(CameraPosition cameraPosition) {
		sCameraPosition = cameraPosition;
	}
}

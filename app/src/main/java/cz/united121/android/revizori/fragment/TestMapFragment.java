package cz.united121.android.revizori.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;
import cz.united121.android.revizori.model.helper.TestGetterLocation;
import cz.united121.android.revizori.model.helper.TypeOfVehicle;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {10/3/2015}
 **/
public class TestMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraChangeListener {
	public static final String TAG = TestMapFragment.class.getName();

	public static final String KEY_POINTS = "points";
	private final static String BUNDLE_KEY_MAP_STATE = "mapData";
	private static int PERIOD_BETWEEN_REPORTING = 10 * 1000; // ms
	private static boolean isTimeValid = true;
	private static MyCountingThread mThread;
	@Bind(R.id.map)
	MapView mMapView;
	GoogleMap mMap;

	public static TestMapFragment newInstance() {
		TestMapFragment fragment = new TestMapFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View mapLayout = inflater.inflate(R.layout.fragment_map, container, false);
		ButterKnife.bind(this, mapLayout);
		return mapLayout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");
		// Save the map state to it's own bundle
		Bundle mapState = new Bundle();
		mMapView.onSaveInstanceState(mapState);
		// Put the map bundle in the main outState
		outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
		super.onSaveInstanceState(outState);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated");
		Bundle mapState = null;
		if (savedInstanceState != null) {
			// Load the map state bundle from the main savedInstanceState
			mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
			savedInstanceState.remove(BUNDLE_KEY_MAP_STATE);
		}
		mMapView.onCreate(mapState);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		mMapView.onPause();
		super.onPause();
		//mMap = null;
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		setUpMapIfNeeded();
		mMapView.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Log.d(TAG, "onDestroyView");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onStop");
		}
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		mMapView.onDestroy();
		mMap = null;
		ButterKnife.unbind(this);
		super.onDestroy();
	}

	public void onLowMemory() {
		Log.d(TAG, "onLowMemory");
		super.onLowMemory();
		mMapView.onLowMemory();
	}

	;

	private void setUpMapIfNeeded() {
		Log.d(TAG, "setUpMapIfNeeded");
		if (mMap == null) {
			mMapView.getMapAsync(this);
		}
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		Log.d(TAG, "onMapReady");
		mMap = googleMap;

		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);

		mMap.setOnMapClickListener(this);
		mMap.setOnCameraChangeListener(this);

		refreshMap();

		CameraPosition lastCameraPosition = TestGetterLocation.getCameraPosition();
		if (lastCameraPosition != null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCameraPosition.target, lastCameraPosition.zoom));
		}
	}

	@Override
	public void onMapClick(LatLng latLng) {
		Log.d(TAG, "onMapClick");
		//mMap.addMarker(new MarkerOptions().position(latLng));
		//TestGetterLocation.addLocationMarker(latLng);

		LocationGetter.addReport(new ReportInspector(ParseUser.getCurrentUser(), new ParseGeoPoint(latLng.latitude, latLng.longitude), TypeOfVehicle.BUS.name()));
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		Log.d(TAG, "onCameraChange");
		TestGetterLocation.setCameraPosition(cameraPosition);
	}

	@OnClick(R.id.reporting_insperctor)
	public void OnReportinInspectorClick(View view) {
		refreshMap();
	}

	private void refreshMap() {
		LocationGetter.getReports().findInBackground(new FindCallback<ReportInspector>() {
			@Override
			public void done(List<ReportInspector> list, ParseException e) {
				Log.d(TAG, "done");
				if (e == null && mMap != null) {
					mMap.clear();
					for (ReportInspector report : list) {
						mMap.addMarker(new MarkerOptions()
								.position(report.getLocation())
								.icon(BitmapDescriptorFactory.fromResource(report
										.getTypeOfVehicle()
										.getMarkerImageResource())));
					}
				}

			}
		});


	}

	//NOT LIFECYCLE OF FRAGMENT/////////////////

	private void startCountingPeriod() {
		mThread = new MyCountingThread();
		mThread.start();
	}

	private void stopCountingPeriod() {
		mThread.close();
	}

	/**
	 * Static inner classes don't hold implicit references to their
	 * enclosing class, so the Activity instance won't be leaked across
	 * configuration changes. - > to avoid memory leaks
	 */
	private static class MyCountingThread extends Thread {
		private boolean mRunning = false;

		@Override
		public void run() {
			mRunning = true;
			while (mRunning) {
				SystemClock.sleep(PERIOD_BETWEEN_REPORTING);
				isTimeValid = true;
			}
		}

		public void close() {
			mRunning = false;
		}
	}
}

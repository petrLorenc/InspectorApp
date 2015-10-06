package cz.united121.android.revizori.fragment;

import android.app.Fragment;
import android.location.Location;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.dialog.ChooseTransportDialogFragment;
import cz.united121.android.revizori.helper.LocationHelper;
import cz.united121.android.revizori.model.MapPoint;
import cz.united121.android.revizori.model.helper.LocationGetter;
import cz.united121.android.revizori.model.helper.TestGetterLocation;
import cz.united121.android.revizori.model.helper.TypeOfVehicle;
import io.realm.RealmResults;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {10/3/2015}
 **/
public class TestMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraChangeListener, LocationHelper.LocationHelperInterface {
	public static final String TAG = TestMapFragment.class.getName();

	private final static String BUNDLE_KEY_MAP_STATE = "mapData";
	private static int PERIOD_BETWEEN_REPORTING = 10 * 1000; // ms
	private static boolean isTimeValid = true;
	private static MyCountingThread mThread;

	private static boolean isLocationValid = true;
	private static Location mLastKnownLocation;

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
	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		setUpMapAddMarkersIfNeeded();
		mMapView.onResume();
		LocationHelper.getInstance(getActivity()).registerListener(this);
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause");
		mMapView.onPause();
		super.onPause();
		//mMap = null;
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
		mMap.setMyLocationEnabled(false);
		mMap = null;
		mMapView.onDestroy();
		ButterKnife.unbind(this);
		super.onDestroy();
	}

	public void onLowMemory() {
		Log.d(TAG, "onLowMemory");
		super.onLowMemory();
		mMapView.onLowMemory();
	}

	;

	private void setUpMapAddMarkersIfNeeded() {
		Log.d(TAG, "setUpMapAddMarkersIfNeeded");
		if (mMap == null) {
			mMapView.getMapAsync(this);
		} else {
			refreshMapWithDatabase();
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

		refreshMapWithDatabase();

		CameraPosition lastCameraPosition = TestGetterLocation.getCameraPosition();
		if (lastCameraPosition != null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCameraPosition.target, lastCameraPosition.zoom));
		}
	}

	@Override
	public void onMapClick(LatLng latLng) {
		Log.d(TAG, "onMapClick");
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		Log.d(TAG, "onCameraChange");
		TestGetterLocation.setCameraPosition(cameraPosition);
	}

	@OnClick(R.id.reporting_insperctor)
	public void OnReportinInspectorClick(View view) {
		ChooseTransportDialogFragment chooseTransportDialogFragment =
				ChooseTransportDialogFragment.newInstance(new ChooseTransportDialogFragment.ChooseTransportInterface() {
					@Override
					public void OnChoosingMetro(ChooseTransportDialogFragment fragment) {
						Log.d(TAG, "OnChoosingMetro");
						if (requirementsChecked()) {
							isTimeValid = false;

						}
					}

					@Override
					public void OnChoosingTram(ChooseTransportDialogFragment fragment) {
						Log.d(TAG, "OnChoosingTram");
						if (requirementsChecked()) {
							isTimeValid = false;
							changeFragmentToSummary(mLastKnownLocation, TypeOfVehicle.TRAM.name());
						}
					}

					@Override
					public void OnChoosingBus(ChooseTransportDialogFragment fragment) {
						Log.d(TAG, "OnChoosingBus");
						if (requirementsChecked()) {
							isTimeValid = false;
							changeFragmentToSummary(mLastKnownLocation, TypeOfVehicle.BUS.name());
						}
					}
				});
		chooseTransportDialogFragment.show(getFragmentManager(), "alertDialog");
	}

	private void changeFragmentToSummary(Location mLastKnownLocation, String nameTypeOfVehicle) {
		startThreadForWaitingPeriodAndSet();

		Bundle bundle = new Bundle();

		bundle.putDouble(SummaryFragment.BUNDLE_LATITUDE, mLastKnownLocation.getLatitude());
		bundle.putDouble(SummaryFragment.BUNDLE_LONGITUDE, mLastKnownLocation.getLongitude());
		bundle.putString(SummaryFragment.BUNDLE_TYPE_OF_VEHICLE, nameTypeOfVehicle);

		((BaseActivity) getActivity()).changeFragment(SummaryFragment.class.getName(), bundle);
	}

	private boolean requirementsChecked() {
		return isTimeValid && isLocationValid;
	}

	private void refreshMapWithDatabase() {
		RealmResults<MapPoint> dataOnMap = LocationGetter.getReportsWithUpdate(new LocationGetter.OnDownloadDataFromNet() {
			@Override
			public void dataWasSavedToRealmDatabase() {
				clearAndAddAllMarkers(LocationGetter.getReportsWithoutUpdate());
			}
		});

		clearAndAddAllMarkers(dataOnMap);
	}

	private void clearAndAddAllMarkers(RealmResults<MapPoint> dataOnMap) {
		mMap.clear();
		for (MapPoint report : dataOnMap) {
			mMap.addMarker(new MarkerOptions()
					.position(new LatLng(report.getLatitude(), report.getLongitude()))
					.icon(BitmapDescriptorFactory.fromResource(TypeOfVehicle.valueOf(report.getTranporttype()).getMarkerImageResource()))
					.title(report.getAuthor()));
		}
	}

	//NOT LIFECYCLE OF FRAGMENT/////////////////

	private void startThreadForWaitingPeriodAndSet() {
		mThread = new MyCountingThread();
		mThread.start();
	}

	//location services //////////////////////////////////

	@Override
	public void OnLocationChanged(Location location) {
		Log.d(TAG, "OnLocationChanged");
		mLastKnownLocation = location;
		isLocationValid = true;
	}

	@Override
	public void OnLocationGetFailed() {
		Log.d(TAG, "OnLocationGetFailed");
		isLocationValid = false;
	}

	@Override
	public void OnConnectionFailed() {
		Log.d(TAG, "OnConnectionFailed");
		isLocationValid = false;
	}

	//Location services //////////////////////////////////

	/**
	 * Static inner classes don't hold implicit references to their
	 * enclosing class, so the Activity instance won't be leaked across
	 * configuration changes. - > to avoid memory leaks
	 */
	private static class MyCountingThread extends Thread {

		@Override
		public void run() {
			if (!isTimeValid) {
				SystemClock.sleep(PERIOD_BETWEEN_REPORTING);
				isTimeValid = true;
			}
		}
	}
}

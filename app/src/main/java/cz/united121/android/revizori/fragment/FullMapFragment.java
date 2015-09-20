package cz.united121.android.revizori.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.fragment.dialog.AlertDialogFragment;
import cz.united121.android.revizori.helper.LocationHelper;
import cz.united121.android.revizori.listeners.MyCameraChangedListener;
import cz.united121.android.revizori.listeners.helper.MyMultipleCameraChangeListener;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.service.MyDeletingService;
import cz.united121.android.revizori.service.MyTrackingService;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {13.8.2015}
 */
public class FullMapFragment extends BaseFragment implements OnMapReadyCallback,
		AlertDialogFragment.OnClickListener, LocationHelper.LocationHelperInterface {
	public static final String TAG = FullMapFragment.class.getName();
	private static View cachedView;

	@Bind(R.id.reporting_insperctor)
	public ImageView mReportingGeneral;
	@Bind(R.id.reporting_insperctor_bus)
	public ImageView mReportingBus;
	@Bind(R.id.reporting_insperctor_tram)
	public ImageView mReportingTram;
	@Bind(R.id.reporting_insperctor_metro)
	public ImageView mReportingMetro;

	private List<ReportInspector> listPIncpectorObj = new ArrayList<>();
	private MapFragment mMapFragment;
	private GoogleMap mGoogleMap;
	private ClusterManager<ReportInspector> mClusterManager;
	private MyMultipleCameraChangeListener mMyMultipleCameraChangeListener;
	private LocationHelper mLocationHelper;


	/**
	 * To avoid spam the server we provide one report per minute
	 */
	private int PERIOD_BETWEEN_REPORTING = 60 * 1000; // ms
	private boolean isTimeValid = true;
	private Handler oneMinuteHandler = new Handler();
	private Runnable oneMinuteTask = new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "oneMinuteTask-run");
			isTimeValid = true;
		}
	};

	private boolean isLocationValid = false;
	private Location mLastKnownLocation;

	@Override
	public int getLayout() {
		return R.layout.fragment_map;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onCreate");
		mLocationHelper = LocationHelper.getInstance(getActivity());

		Intent startIntent = new Intent(getActivity(), MyDeletingService.class);
		startIntent.setAction(MyDeletingService.SERVICE_START);
		getActivity().startService(startIntent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "cachedView is null == " + (cachedView == null));
		if (cachedView == null) {
			cachedView = super.onCreateView(inflater, container, savedInstanceState);

			AlertDialogFragment alertDialogFragment =
					AlertDialogFragment.newInstance(this,
							"Služba hlásení revizorů",
							"Chcete zapnout službu pro hlášení revizorů ve Vašem okolí?\nTuto volbu můžete změnit v nastavení.",
							"Zapnout",
							"Později");
			alertDialogFragment.show(getFragmentManager(), "alertDialog");
		}
		Log.d(TAG, "onCreateView");
		ButterKnife.bind(this, cachedView);

		mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mMapFragment.getMapAsync(this);

		mLocationHelper.registerListener(this);

		return cachedView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		mMapFragment.onResume();
		if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Util.makeAlertDialogGPS(getActivity(), getString(R.string.full_map_requesting_enabling_GPS_start));
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		mLocationHelper.removeListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		cachedView = null;
		mMapFragment.onDestroy();

		Intent stopIntent = new Intent(getActivity(), MyDeletingService.class);
		stopIntent.setAction(MyDeletingService.SERVICE_STOP);
		getActivity().startService(stopIntent);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		Log.d(TAG, "onMapReady");
		this.mGoogleMap = googleMap;

		mClusterManager = new ClusterManager<ReportInspector>(getActivity(), mGoogleMap);

		this.mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				Log.d(TAG, "onMapClick");
				Log.d("arg0", latLng.latitude + "-" + latLng.longitude);

				mReportingGeneral.setVisibility(View.VISIBLE);
				mReportingBus.setVisibility(View.GONE);
				mReportingTram.setVisibility(View.GONE);
				mReportingMetro.setVisibility(View.GONE);
			}
		});
		this.mGoogleMap.setMyLocationEnabled(true);

		mMyMultipleCameraChangeListener = new MyMultipleCameraChangeListener();
		mMyMultipleCameraChangeListener.addListener(mClusterManager);
		mMyMultipleCameraChangeListener.addListener(new MyCameraChangedListener(mClusterManager));

		this.mGoogleMap.setOnCameraChangeListener(mMyMultipleCameraChangeListener);

	}

	@OnClick(R.id.reporting_insperctor)
	public void reportingInspector(View view) {
		Log.d(TAG, "reportingInspector");
		//TODO TEST
		//doMapUpdate();
		mReportingGeneral.setVisibility(View.GONE);
		mReportingBus.setVisibility(View.VISIBLE);
		mReportingTram.setVisibility(View.VISIBLE);
		mReportingMetro.setVisibility(View.VISIBLE);

	}

	@OnClick(R.id.reporting_insperctor_bus)
	public void reportingInspectorBus(View view) {
		Log.d(TAG, "reportingInspectorBus");
		if (!checkingStatus()) {
			return;
		}
//		//check if we can use last known position
		if (mLastKnownLocation != null && isLocationValid) {
			changeFragmentToSummary(mLastKnownLocation, ReportInspector.TypeOfVehicle.BUS);
		} else {
			Util.makeAlertDialogOnlyOK(getActivity(), getString(R.string.full_map_problem_with_position));
			return;
		}
	}

	@OnClick(R.id.reporting_insperctor_tram)
	public void reportingInspectorTram(View view) {
		Log.d(TAG, "reportingInspectorTram");
		if (!checkingStatus()) {
			return;
		}
		//check if we can use last known position
		if (mLastKnownLocation != null && isLocationValid) {
			changeFragmentToSummary(mLastKnownLocation, ReportInspector.TypeOfVehicle.TRAM);
		} else {
			Util.makeAlertDialogOnlyOK(getActivity(), getString(R.string.full_map_problem_with_position));
			return;
		}
	}

	private boolean checkingStatus() {
		if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Util.makeAlertDialogGPS(getActivity(), getString(R.string.full_map_requesting_enabling_GPS_report));
			return false;
		}
		if (!isTimeValid) {
			Util.makeAlertDialogOnlyOK(getActivity(), "Nemuzete hlasit revizory takto rychle za sebou");
			return false;
		}
		return true;
	}

	@OnClick(R.id.reporting_insperctor_metro)
	public void reportingInspectorMetro(View view) {
		Log.d(TAG, "reportingInspectorMetro");
		((MapActivity) getActivity()).changeFragment(MetroStationsFragment.class.getName());
	}

	private void changeFragmentToSummary(Location location, String typeOfVehicle) {
		oneMinuteHandler.postDelayed(oneMinuteTask, PERIOD_BETWEEN_REPORTING);
		isTimeValid = false;
		isLocationValid = false;

		Bundle bundle = new Bundle();
		bundle.putDouble(SummaryFragment.BUNDLE_LATITUDE, location.getLatitude());
		bundle.putDouble(SummaryFragment.BUNDLE_LONGITUDE, location.getLongitude());
		bundle.putString(SummaryFragment.BUNDLE_TYPEOFVEHICLE, typeOfVehicle);
		//bundle.putString(SummaryFragment.BUNDLE_NAMEOFSTATION, mStation.getName());
		((BaseActivity) getActivity()).changeFragment(SummaryFragment.class.getName(), bundle);
	}

	@Override
	public void onPositiveClick(AlertDialogFragment dialogFragment) {
		Log.d(TAG, "onPositiveClick - AlertDialogFragment");
		Intent startIntent = new Intent(getActivity(), MyTrackingService.class);
		startIntent.setAction(MyTrackingService.SERVICE_START);
		getActivity().startService(startIntent);
		dialogFragment.dismiss();
	}

	@Override
	public void onNegativeClick(AlertDialogFragment dialogFragment) {
		Log.d(TAG, "onNegativeClick - AlertDialogFragment");
		dialogFragment.dismiss();
	}

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
}

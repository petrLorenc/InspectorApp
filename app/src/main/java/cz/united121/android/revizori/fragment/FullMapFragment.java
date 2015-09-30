package cz.united121.android.revizori.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

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
import cz.united121.android.revizori.fragment.dialog.ChooseTransportDialogFragment;
import cz.united121.android.revizori.helper.LocationHelper;
import cz.united121.android.revizori.listeners.helper.MyMultipleCameraChangeListener;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.LocationGetter;
import cz.united121.android.revizori.service.MyTrackingService;
import cz.united121.android.revizori.service.MyUpdatingService;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {13.8.2015}
 */
public class FullMapFragment extends BaseFragment implements OnMapReadyCallback,
		AlertDialogFragment.AlertOnClickListener, LocationHelper.LocationHelperInterface, ChooseTransportDialogFragment.ChooseTransportInterface {
	public static final String TAG = FullMapFragment.class.getName();
	public static final String BROADCAST_TO_REFRESH_MAP = "cz.united121.android.revizori.fragment.refresh_map";
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
	private SupportMapFragment mMapFragment;
	private GoogleMap mGoogleMap;
	private AppCompatActivity mContainingActivity;

	private BroadcastReceiver myRefrestMapBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "onReceive");
			Log.d(TAG, "LocationGetter.getReports().size == " + LocationGetter.getReports().size());
			for(ReportInspector reportInspector : LocationGetter.getReports()){
				reportInspector.setMarker(mGoogleMap);
			}
		}
	};

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
	public void onAttach(Activity activity) {
		mContainingActivity = (AppCompatActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onCreate");
		mLocationHelper = LocationHelper.getInstance(getActivity());

		Intent startIntent = new Intent(getActivity(), MyUpdatingService.class);
		startIntent.setAction(MyUpdatingService.SERVICE_START);
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
			mMapFragment = (SupportMapFragment) mContainingActivity.getSupportFragmentManager().findFragmentById(R.id.map);
			mMapFragment.getMapAsync(this);
		}
		Log.d(TAG, "onCreateView");
		ButterKnife.bind(this, cachedView);

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
		getActivity().registerReceiver(this.myRefrestMapBroadcastReceiver, new IntentFilter(BROADCAST_TO_REFRESH_MAP));

		Intent startIntent = new Intent(getActivity(), MyUpdatingService.class);
		startIntent.setAction(MyUpdatingService.SERVICE_FORCE);
		getActivity().startService(startIntent);
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapFragment.onPause();
		getActivity().unregisterReceiver(this.myRefrestMapBroadcastReceiver);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		mLocationHelper.removeListener(this);
		ButterKnife.unbind(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");

		Intent stopIntent = new Intent(getActivity(), MyUpdatingService.class);
		stopIntent.setAction(MyUpdatingService.SERVICE_STOP);
		getActivity().startService(stopIntent);
	}

	@Override
	public void onDetach() {
		cachedView = null;
		super.onDetach();
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		Log.d(TAG, "onMapReady");
		this.mGoogleMap = googleMap;

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

	}

	@OnClick(R.id.reporting_insperctor)
	public void reportingInspector(View view) {
		Log.d(TAG, "reportingInspector");
		ChooseTransportDialogFragment chooseTransportDialogFragment =
				ChooseTransportDialogFragment.newInstance(this);
		chooseTransportDialogFragment.show(getFragmentManager(), "alertDialog");
	}

	private boolean ableToReport() {
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

	private void changeFragmentToSummary(Location location, String typeOfVehicle) {
		//oneMinuteHandler.postDelayed(oneMinuteTask, PERIOD_BETWEEN_REPORTING);
		isTimeValid = false;
		isLocationValid = false;

		Bundle bundle = new Bundle();
		bundle.putDouble(SummaryFragment.BUNDLE_LATITUDE, location.getLatitude());
		bundle.putDouble(SummaryFragment.BUNDLE_LONGITUDE, location.getLongitude());
		bundle.putString(SummaryFragment.BUNDLE_TYPE_OF_VEHICLE, typeOfVehicle);
		//bundle.putString(SummaryFragment.BUNDLE_NAME_OF_STATION, mStation.getName());
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

	@Override
	public void OnChoosingMetro(ChooseTransportDialogFragment fragment) {
		Log.d(TAG, "OnChoosingMetro");

		fragment.getDialog().dismiss();
		fragment.dismiss();

		((MapActivity) getActivity()).changeFragment(MetroStationsFragment.class.getName());
	}

	@Override
	public void OnChoosingTram(ChooseTransportDialogFragment fragment) {
		Log.d(TAG, "OnChoosingTram");

		fragment.getDialog().dismiss();
		fragment.dismiss();

		if (!ableToReport()) {
			return;
		}
		//check if we can use last known position
		if (mLastKnownLocation != null && isLocationValid) {
			changeFragmentToSummary(mLastKnownLocation, ReportInspector.TypeOfVehicle.TRAM.toString());
		} else {
			Util.makeAlertDialogOnlyOK(getActivity(), getString(R.string.full_map_problem_with_position));
		}
	}

	@Override
	public void OnChoosingBus(ChooseTransportDialogFragment fragment) {
		Log.d(TAG, "OnChoosingBus");

		fragment.getDialog().dismiss();
		fragment.dismiss();

		if (!ableToReport()) {
			return;
		}
//		//check if we can use last known position
		if (mLastKnownLocation != null && isLocationValid) {
			changeFragmentToSummary(mLastKnownLocation, ReportInspector.TypeOfVehicle.BUS.toString());
		} else {
			Util.makeAlertDialogOnlyOK(getActivity(), getString(R.string.full_map_problem_with_position));
		}
	}
}
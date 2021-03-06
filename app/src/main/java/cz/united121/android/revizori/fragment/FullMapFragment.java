package cz.united121.android.revizori.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.fragment.dialog.AlertDialogFragment;
import cz.united121.android.revizori.listeners.MyCameraChangedListener;
import cz.united121.android.revizori.listeners.MyLocationListener;
import cz.united121.android.revizori.listeners.helper.MyMultipleListener;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {13.8.2015}
 */
public class FullMapFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
		AlertDialogFragment.OnClickListener{
    public static final String TAG = FullMapFragment.class.getName();

	private List<ReportInspector> listPIncpectorObj = new ArrayList<>();

	private boolean mStatus = false;

    private GoogleApiClient mGoogleApiClient;
    private MapFragment mMapFragment;
    private GoogleMap mGoogleMap;
	private ClusterManager<ReportInspector> mClusterManager;

	private MyMultipleListener mMyMultipleListener;

	private static View cachedView;

	@Bind(R.id.reporting_insperctor)
	public ImageView mReportingGeneral;

	@Bind(R.id.reporting_insperctor_bus)
	public ImageView mReportingBus;

	@Bind(R.id.reporting_insperctor_tram)
	public ImageView mReportingTram;

	@Bind(R.id.reporting_insperctor_metro)
	public ImageView mReportingMetro;

    @Override
    public int getLayout() {
        return R.layout.fragment_map;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "onActivityCreated");
		mMapFragment.onCreate(savedInstanceState);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "cachedView is null == " + (cachedView == null));
		if (cachedView == null) {
			cachedView = super.onCreateView(inflater, container, savedInstanceState);
		}
		Log.d(TAG, "onCreateView");
		ButterKnife.bind(this, cachedView);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        return cachedView;
    }

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		mMapFragment.onResume();
		if(((LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE ))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Util.makeAlertDialogGPS(getActivity(), getString(R.string.full_map_requesting_enabling_GPS_start));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");
		mMapFragment.onSaveInstanceState(outState);
	}

	@Override
    public void onDestroyView() {
		Log.d(TAG, "onDestroyView");
		super.onDestroyView();
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		cachedView = null;
	}

	/**
	 * Google map API client
	 * @param bundle
	 */
	@Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        // Everything is OK
        mStatus = true;
    }

	/**
	 * Google map API client
	 * @param i
	 */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"onConnectionSuspended");
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
		mStatus = false;
    }

	/**
	 * Google map API client
	 * @param connectionResult
	 */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
		mStatus = false;
		Toast.makeText(getActivity(), "Connection to Google Play failed. Please restart " +
						"application.", Toast.LENGTH_SHORT).show();
	}

    @Override
    public void onMapReady(GoogleMap googleMap) {
		Log.d(TAG, "onMapReady");
        this.mGoogleMap = googleMap;

		mClusterManager = new ClusterManager<ReportInspector>(getActivity(),mGoogleMap);

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

		mMyMultipleListener = new MyMultipleListener();
		mMyMultipleListener.addListener(mClusterManager);
		mMyMultipleListener.addListener(new MyCameraChangedListener(mClusterManager));

		this.mGoogleMap.setOnCameraChangeListener(mMyMultipleListener);

        mGoogleApiClient.connect();
    }

	@OnClick(R.id.reporting_insperctor)
	public void reportingInspector(View view) {
		Log.d(TAG,"reportingInspector");
		//TODO TEST
		//doMapUpdate();
		mReportingGeneral.setVisibility(View.GONE);
		mReportingBus.setVisibility(View.VISIBLE);
		mReportingTram.setVisibility(View.VISIBLE);
		mReportingMetro.setVisibility(View.VISIBLE);

		//AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(this,"POZOR","Zapnout wifi","Jdeme dale", "UPS");
		//alertDialogFragment.show(getFragmentManager(),"alertDialog");

	}

	@OnClick(R.id.reporting_insperctor_bus)
	public void reportingInspectorBus(View view) {
		Log.d(TAG,"reportingInspectorBus");
		//check enabling GPS
		if(((LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE ))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Util.makeAlertDialogGPS(getActivity(), getString(R.string.full_map_requesting_enabling_GPS_report));
		}
//		//check if we can use last known position
//		Location location = null;
//		//measurement of location was before 10 second
//		 if((location = mMyLocationListener.getValidLocation()) != null){}
//		else{
//			Util.makeAlertDialogOnlyOK(getActivity(),getString(R.string.full_map_problem_with_position));
//			return;
//		}
//
//		changeFragmentToSummary(location, ReportInspector.TypeOfVehicle.BUS);
	}

	@OnClick(R.id.reporting_insperctor_tram)
	public void reportingInspectorTram(View view) {
		Log.d(TAG,"reportingInspectorTram");
		if(((LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE ))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Util.makeAlertDialogGPS(getActivity(), getString(R.string.full_map_requesting_enabling_GPS_report));
		}
		//check if we can use last known position
//		Location location = null;
//		if((location = mMyLocationListener.getValidLocation()) != null){}
//		else{
//			Util.makeAlertDialogOnlyOK(getActivity(),getString(R.string.full_map_problem_with_position));
//			return;
//		}
//
//		changeFragmentToSummary(location, ReportInspector.TypeOfVehicle.TRAM);
	}

	@OnClick(R.id.reporting_insperctor_metro)
	public void reportingInspectorMetro(View view) {
		Log.d(TAG, "reportingInspectorMetro");
		((MapActivity) getActivity()).changeFragment(MetroStationsFragment.class.getName());
	}

	private void changeFragmentToSummary(Location location, String typeOfVehicle){
		Bundle bundle = new Bundle();
		bundle.putDouble(SummaryFragment.BUNDLE_LATITUDE,location.getLatitude());
		bundle.putDouble(SummaryFragment.BUNDLE_LONGITUDE,location.getLongitude());
		bundle.putString(SummaryFragment.BUNDLE_TYPEOFVEHICLE, typeOfVehicle);
		//bundle.putString(SummaryFragment.BUNDLE_NAMEOFSTATION, mStation.getName());
		((BaseActivity) getActivity()).changeFragment(SummaryFragment.class.getName(), bundle);
	}

	@Override
	public void onPositiveClick() {
		Log.d(TAG, "onPositiveClick");

	}

	@Override
	public void onNegativeClick() {
		Log.d(TAG, "onNegativeClick");

	}
}

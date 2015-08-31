package cz.united121.android.revizori.fragment;

import android.database.Cursor;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.model.MyDatabase;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {13.8.2015}
 */
public class FullMapFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback{
    public static final String TAG = FullMapFragment.class.getName();

	private boolean mStatus = false;

    private GoogleApiClient mGoogleApiClient;
    private MapFragment mMapFragment;
    private GoogleMap googleMap;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
        if(cachedView == null) {
			cachedView = super.onCreateView(inflater, container, savedInstanceState);
		}
		ButterKnife.bind(this,cachedView);

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

	@Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        // Everything is OK
        mStatus = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"onConnectionSuspended");
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.

		mStatus = false;
    }

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
        this.googleMap = googleMap;
		this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
        googleMap.setMyLocationEnabled(true);
        mGoogleApiClient.connect();
    }



	@OnClick(R.id.reporting_insperctor)
	public void reportingInspector(View view) {
		Log.d(TAG,"reportingInspector");
		mReportingGeneral.setVisibility(View.GONE);
		mReportingBus.setVisibility(View.VISIBLE);
		mReportingTram.setVisibility(View.VISIBLE);
		mReportingMetro.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.reporting_insperctor_bus)
	public void reportingInspectorBus(View view) {
		Log.d(TAG,"reportingInspectorBus");
	}

	@OnClick(R.id.reporting_insperctor_tram)
	public void reportingInspectorTram(View view) {
		Log.d(TAG,"reportingInspectorTram");
	}

	@OnClick(R.id.reporting_insperctor_metro)
	public void reportingInspectorMetro(View view) {
		Log.d(TAG,"reportingInspectorMetro");
		((MapActivity) getActivity()).changeFragment(MetroStationsFragment.class.getName());
	}

}

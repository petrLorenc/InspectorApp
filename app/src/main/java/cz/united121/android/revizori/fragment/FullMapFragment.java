package cz.united121.android.revizori.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.oguzdev.circularfloatingactionmenu.library.animation.MenuAnimationHandler;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.ui.AfterSnackBarBehavior;

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
	private static ArrayList<View> unmovedView;

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

		setMenu();

        return cachedView;
    }

    @Override
    public void onDestroyView() {
		Log.d(TAG, "onDestroyView");
		super.onDestroyView();
		//AfterSnackBarBehavior.mStaticViewToMove.get(0).setVisibility(View.GONE);
		//AfterSnackBarBehavior.mStaticViewToMove.remove(0);
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
        googleMap.setMyLocationEnabled(true);
        mGoogleApiClient.connect();
    }

	@OnClick(R.id.fab_reporting_insperctor)
	public void reportingInspector(View view) {
		Log.d(TAG,"reportingInspector");
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if(location != null){
			Log.d(TAG,location.getLatitude() + "");
			Log.d(TAG,location.getLongitude() + "");

			new ReportInspector(new ParseGeoPoint(location.getLatitude(),location.getLongitude())
					,null);

			if(googleMap != null){
				googleMap.addMarker(new MarkerOptions().title("My location")
						.position(new LatLng(location.getLatitude
								(),location.getLongitude())));
			}
		}
	}

	private void setMenu(){
		// in Activity Context
		FloatingActionButton actionButton = (FloatingActionButton) cachedView.findViewById(R.id
				.fab_reporting_insperctor);

		SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());



		// Creating items
		ImageView itemIcon = new ImageView(getActivity());
		//itemIcon.pare
//		ImageView itemIcon2 = new ImageView(getActivity());
//		ImageView itemIcon3 = new ImageView(getActivity());

		CoordinatorLayout coordinatorLayout = (CoordinatorLayout) cachedView.findViewById
				(R.id.fragment_map_coordinator_layout);
		coordinatorLayout.addView(itemIcon);

		itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.menu_map));
		itemIcon.setLayoutParams(new ViewGroup.LayoutParams(75, 75));
//		itemIcon2.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.menu_about_me));
//		itemIcon3.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.menu_logout));

		//SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
//		SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
//		SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

//		button3.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.d(TAG, "onClick");
//				Toast.makeText(getActivity(), "BUM", Toast.LENGTH_SHORT).show();
//			}
//		});

		//AfterSnackBarBehavior.mStaticViewToMove.add(itemIcon);

//		cachedView.findViewById(R.id.image_image).remove
		FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
//				.addSubActionView(cachedView.findViewById(R.id.image_image))
				//.addSubActionView(button2)
				//.addSubActionView(button3)
				.addSubActionView(itemIcon)
				.attachTo(actionButton)
				.build();
	}

	private class MyFloatingActionMenuBuilder extends FloatingActionMenu.Builder{

		public MyFloatingActionMenuBuilder(Activity activity) {
			super(activity);
		}

		@Override
		public FloatingActionMenu.Builder addSubActionView(View subActionView, int width, int height) {
			Log.d(TAG, "addSubActionView");
			return super.addSubActionView(subActionView, width, height);
		}

		@Override
		public FloatingActionMenu.Builder addSubActionView(View subActionView) {
			Log.d(TAG, "addSubActionView");
			return super.addSubActionView(subActionView);
		}

		@Override
		public FloatingActionMenu.Builder addSubActionView(int resId, Context context) {
			Log.d(TAG, "addSubActionView");
			return super.addSubActionView(resId, context);
		}

	}
}

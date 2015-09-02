package cz.united121.android.revizori.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.SummaryFragment;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.Station;
import cz.united121.android.revizori.ui.helper.CursorViewAdapter;
import cz.united121.android.revizori.ui.helper.FilterableViewAdapter;

/**
 * Cares about starting summary framgnet when we choose metro
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/30/2015}
 **/
public class StationsViewAdapter extends FilterableViewAdapter<StationsViewAdapter.ViewHolder> {
	public static final String TAG = StationsViewAdapter.class.getName();

	public StationsViewAdapter(Activity activity, Cursor cursor) {
		super(activity, cursor);
		Log.d(TAG, "StationsViewAdapter");
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		Log.d(TAG, "onBindViewHolder");
		Station station = mVisibleStations.get(position);
		viewHolder.mNameOfStation.setText(station.getName());
		viewHolder.mStationLogo.setImageResource(station.getPicture());
		viewHolder.mStation = station;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		Log.d(TAG, "onCreateViewHolder");
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
				.recycler_view_row_stations,viewGroup,false);
		ViewHolder viewHolder = new ViewHolder(view, mActivity);
		return viewHolder;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		@Bind(R.id.rv_row_logo)
		public ImageView mStationLogo;

		@Bind(R.id.rv_row_name)
		public TextView mNameOfStation;

		Activity mActivity;
		public Station mStation;

		public ViewHolder(View itemView, Activity activity) {
			super(itemView);
			Log.d(TAG, "ViewHolder constuctor");
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
			mActivity = activity;
		}

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onClick");
			Log.d(TAG, "v" + getAdapterPosition() + " name: " + mNameOfStation.getText());
			if(mActivity instanceof BaseActivity){
				Bundle bundle = new Bundle();
				bundle.putDouble(SummaryFragment.BUNDLE_LATITUDE,mStation.getLatitude());
				bundle.putDouble(SummaryFragment.BUNDLE_LONGITUDE,mStation.getLongtitude());
				bundle.putString(SummaryFragment.BUNDLE_TYPEOFVEHICLE, ReportInspector
						.TypeOfVehicle.METRO);
				bundle.putString(SummaryFragment.BUNDLE_NAMEOFSTATION, mStation.getName());
				((BaseActivity) mActivity).changeFragment(SummaryFragment.class.getName(),bundle);
			}
		}
	}
}

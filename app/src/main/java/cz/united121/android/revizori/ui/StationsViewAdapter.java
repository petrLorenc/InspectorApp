package cz.united121.android.revizori.ui;

import android.content.Context;
import android.database.Cursor;
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
import cz.united121.android.revizori.model.Station;
import cz.united121.android.revizori.ui.helper.CursorViewAdapter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/30/2015}
 **/
public class StationsViewAdapter extends CursorViewAdapter<StationsViewAdapter.ViewHolder> {
	public static final String TAG = StationsViewAdapter.class.getName();

	public StationsViewAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		Log.d(TAG, "StationsViewAdapter");
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
		Log.d(TAG, "onBindViewHolder");
		Station station = Station.constructFromCursor(cursor);
		viewHolder.mNameOfStation.setText(station.getName());
		viewHolder.mStationLogo.setImageResource(station.getPicture());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		Log.d(TAG, "onCreateViewHolder");
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
				.recycler_view_row_stations,viewGroup,false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		@Bind(R.id.rv_row_logo)
		public ImageView mStationLogo;

		@Bind(R.id.rv_row_name)
		public TextView mNameOfStation;

		public ViewHolder(View itemView) {
			super(itemView);
			Log.d(TAG, "ViewHolder constuctor");
			ButterKnife.bind(this,itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onClick");
			Log.d(TAG, "v" + getAdapterPosition() + " name: " + mNameOfStation.getText());
		}
	}
}

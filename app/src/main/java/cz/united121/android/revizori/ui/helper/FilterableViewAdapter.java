package cz.united121.android.revizori.ui.helper;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import cz.united121.android.revizori.model.Station;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/2/2015}
 **/
public abstract class FilterableViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView
		.Adapter<VH> implements Filterable {
	public static final String TAG = FilterableViewAdapter.class.getName();

	protected Activity mActivity;
	protected MyFilter mMyFilter;
	protected List<Station> mAllStations;
	protected List<Station> mVisibleStations;


	public FilterableViewAdapter(Activity activity, Cursor cursor) {
		super();
		Log.d(TAG, "CursorViewAdapter constuctor");
		mActivity = activity;
		if(cursor != null){
			mAllStations = getListFromCursor(cursor);
			mVisibleStations = new ArrayList<>(mAllStations);
		}
	}

	private List<Station> getListFromCursor(Cursor cursor){
		List<Station> stationList = new ArrayList<>(cursor.getCount());
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			// The Cursor is now set to the right position
			stationList.add(Station.constructFromCursor(cursor));
		}
		return stationList;
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount");
		int count = 0;
		if(mVisibleStations != null){
			count =  mVisibleStations.size();
		}
		return count;
	}

	@Override
	public Filter getFilter() {
		Log.d(TAG, "getFilter");
		if(mMyFilter == null){
			mMyFilter = new MyFilter();
		}
		return mMyFilter;
	}

	/**
	 * will be signleton
	 */
	public class MyFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			Log.d(TAG, "performFiltering");
			Log.d(TAG, "constraint" + constraint.toString());
			FilterResults filterResults = new FilterResults();
			if (constraint == null){
				mVisibleStations.addAll(mAllStations);
				return filterResults;
			}else{
				mVisibleStations.clear();
				for(Station station : mAllStations){
					if(station.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
						mVisibleStations.add(station);
					}
				}
			}
			return null;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			Log.d(TAG, "publishResults");
			notifyDataSetChanged();
		}
	}
}

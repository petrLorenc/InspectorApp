package cz.united121.android.revizori.ui.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/30/2015}
 **/
public abstract class CursorViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView
		.Adapter<VH> {
	public static final String TAG = CursorViewAdapter.class.getName();

	private Context mContext;
	private Cursor mCursor;

	public CursorViewAdapter(Context context, Cursor cursor) {
		super();
		Log.d(TAG, "CursorViewAdapter constuctor");
		mContext = context;
		mCursor = cursor;

	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount");
		int count = 0;
		if(mCursor != null){
			count =  mCursor.getCount();
		}
		return count;
	}

	public abstract void onBindViewHolder(VH vh, Cursor cursor);

	@Override
	public void onBindViewHolder(VH vh, int position) {
		Log.d(TAG, "onBindViewHolder");
		if(!mCursor.moveToPosition(position)){
			throw new IllegalStateException("No valid data at given position " + position);
		}
		onBindViewHolder(vh,mCursor);
	}
}

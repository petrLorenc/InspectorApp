package cz.united121.android.revizori.ui.helper;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/30/2015}
 **/
public abstract class RankingListViewAdapter extends BaseAdapter {
	public static final String TAG = RankingListViewAdapter.class.getName();

	List<Pair<String, String>> mParseUsers;
	Context mActivityContext;


	public RankingListViewAdapter(Context activityContext) {
		mParseUsers = getUsersFromParse();
		mActivityContext = activityContext;
	}

	protected abstract List<Pair<String, String>> getUsersFromParse();

	@Override
	public int getCount() {
		Log.d(TAG, "getCount");
		return mParseUsers.size();
	}

	@Override
	public Object getItem(int position) {
		Log.d(TAG, "getItem");
		return mParseUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG, "getItemId");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView");
		View actualView = convertView;
		if (actualView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			actualView = layoutInflater.inflate(R.layout.widget_row_ranking_user, parent, false);
		}
		TextView username = (TextView) actualView.findViewById(R.id.row_ranking_username);
		username.setText(mParseUsers.get(position).first);
		TextView points = (TextView) actualView.findViewById(R.id.row_ranking_points);
		points.setText(mParseUsers.get(position).second);

		return actualView;
	}
}

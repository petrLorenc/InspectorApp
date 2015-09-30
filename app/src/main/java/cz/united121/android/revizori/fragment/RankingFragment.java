package cz.united121.android.revizori.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.ui.helper.RankingListViewAdapter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/30/2015}
 **/
public class RankingFragment extends BaseFragment {
	public static final String TAG = RankingFragment.class.getName();

	@Bind(R.id.ranking_fragment_list_view)
	ListView mRankingListView;

	@Override
	public int getLayout() {
		Log.d(TAG, "getLayout");
		return R.layout.fragment_ranking;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		ButterKnife.bind(this, view);

		RankingListViewAdapter rankingListViewAdapter = new RankingListViewAdapter(getActivity()) {
			@Override
			protected List<Pair<String, String>> getUsersFromParse() {
				Log.d(TAG, "getUsersFromParse");

				List<Pair<String, String>> pairList = new ArrayList<>();
				pairList.add(new Pair<>("Uni", "20"));
				pairList.add(new Pair<>("United", "203"));
				pairList.add(new Pair<>("Uniss", "2033"));
				return pairList;
			}
		};

		mRankingListView.setAdapter(rankingListViewAdapter);

		return view;
	}
}

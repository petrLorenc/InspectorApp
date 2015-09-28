package cz.united121.android.revizori.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.model.MyDatabase;
import cz.united121.android.revizori.ui.StationsViewAdapter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/30/2015}
 **/
public class MetroStationsFragment extends BaseFragment implements SearchView.OnQueryTextListener {
	public static final String TAG = MetroStationsFragment.class.getName();

	@Bind(R.id.recycler_view_metros)
	public RecyclerView mRecyclerView;

	@Bind(R.id.choosing_metro_search_view)
	protected SearchView mSearchView;

	private LinearLayoutManager mLayoutManager;
	private StationsViewAdapter mAdapter;

	@Override
	public int getLayout() {
		return R.layout.fragment_recycler_view_metro;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		View view = super.onCreateView(inflater, container, savedInstanceState);

		ButterKnife.bind(this, view);

		mLayoutManager = new LinearLayoutManager(getActivity());
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setHasFixedSize(true);

		mAdapter = new StationsViewAdapter(getActivity(), MyDatabase.getStationsAll(getActivity()));
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		mSearchView.requestFocus();
		mSearchView.setOnQueryTextListener(this);

		return view;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.d(TAG, "onQueryTextSubmit");
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		mAdapter.getFilter().filter(mSearchView.getQuery());
		return false;
	}
}

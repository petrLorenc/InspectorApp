package cz.united121.android.revizori.activity;

import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.TestMapFragment;
import cz.united121.android.revizori.model.helper.LocationGetter;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {13.8.2015}
 */
public class MapActivity extends BaseActivity {
    public static final String TAG = MapActivity.class.getName();

    @Override
    protected String getFragmentName() {
		return TestMapFragment.class.getName();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocationGetter.deleteAllReports();
	}
}

package cz.united121.android.revizori.fragment;

import android.util.Log;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.base.BaseFragment;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/23/2015}
 **/
public class SettingFragment extends BaseFragment {
	public static final String TAG = SettingFragment.class.getName();

	@Override
	public int getLayout() {
		Log.d(TAG, "getLayout");
		return R.layout.fragment_setting;
	}
}

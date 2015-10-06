package cz.united121.android.revizori.fragment.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getName();

	//Because of duplicate (double initialize of fragment - likely MapFragment)
	//private static View rootView;

    public abstract int getLayout();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "onCreateView");
		View view = inflater.inflate(getLayout(), container, false);
		return view;
    }
}

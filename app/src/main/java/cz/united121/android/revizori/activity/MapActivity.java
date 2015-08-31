package cz.united121.android.revizori.activity;

import android.os.Bundle;

import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.FullMapFragment;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {13.8.2015}
 */
public class MapActivity extends BaseActivity {
    public static final String TAG = MapActivity.class.getName();

    @Override
    protected String getFragmentName() {
        return FullMapFragment.class.getName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}

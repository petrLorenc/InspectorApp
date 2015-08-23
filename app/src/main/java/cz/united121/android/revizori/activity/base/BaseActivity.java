package cz.united121.android.revizori.activity.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.LoginFragment;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getName();

    /**
     * returns the name of the fragment to be instantiated
     *
     * @return
     */
    protected abstract String getFragmentName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        String fragmentName = getFragmentName();
        if (fragmentName == null) {
            finish();
            return;
        }

        Fragment fragment = getFragmentManager().findFragmentByTag(fragmentName);
        if(fragment == null){
            fragment = Fragment.instantiate(this,fragmentName);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_place, fragment, fragment.getClass().getName())
                    .commit();
        }else{
			getFragmentManager()
					.beginTransaction()
					.add(R.id.fragment_place, fragment, fragment.getClass().getName())
					.commit();
		}

    }

    public void changeFragment(Fragment toFragment){
		Util.hideSoftKeyboard(this);
        Fragment fragment = getFragmentManager().findFragmentByTag(toFragment.getClass().getName());
        if(fragment == null){
			fragment = Fragment.instantiate(this,toFragment.getClass().getName());
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_place,toFragment)
                    .addToBackStack(null)
                    .commit();
        }else{
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_place,toFragment)
					.addToBackStack(null)
					.commit();
		}
    }

	@Override
    protected void onStop() {
        super.onStop();
    }
}

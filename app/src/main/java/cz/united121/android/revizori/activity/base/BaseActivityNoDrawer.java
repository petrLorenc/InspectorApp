package cz.united121.android.revizori.activity.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
public abstract class BaseActivityNoDrawer extends BaseActivity {
	public static final String TAG = BaseActivityNoDrawer.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed(); // after that I cant finnish this clicking back button
		Log.d(TAG, "onBackPressed");
		Util.hideSoftKeyboard(this);
		getFragmentManager().popBackStack();
	}

	/**
	 * Login screen
	 * @param toFragment
	 */
	@Override
	public void changeFragment(String toFragment){
		Fragment mCurrentFragment = getFragmentManager().findFragmentById(R.id
				.fragment_place);
		if(mCurrentFragment.getClass().getName() == toFragment){
			return;
		}
		Util.hideSoftKeyboard(this);
		Fragment fragment = getFragmentManager().findFragmentByTag(toFragment);
		if(fragment == null){
			fragment = Fragment.instantiate(this,toFragment);
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.card_flip_right_in, R.animator.card_flip_right_out,
							R.animator.card_flip_left_in, R.animator.card_flip_left_out)
					.replace(R.id.fragment_place, fragment)
					.addToBackStack(null)
					.commit();
		}else{
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.card_flip_right_in, R.animator.card_flip_right_out,
							R.animator.card_flip_left_in, R.animator.card_flip_left_out)
					.replace(R.id.fragment_place,fragment)
					.addToBackStack(null)
					.commit();
		}
	}
}

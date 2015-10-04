package cz.united121.android.revizori.activity.base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.LoginActivity;
import cz.united121.android.revizori.fragment.RankingFragment;
import cz.united121.android.revizori.fragment.SettingFragment;
import cz.united121.android.revizori.fragment.TestMapFragment;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = BaseActivity.class.getName();

	private static final String SAVE_FRAGMENT = "fragment_save_in_bundle";

	protected DrawerLayout mDrawerLayout;
	protected NavigationView mNavigationView;
    /**
     * returns the name of the fragment to be instantiated
     *
     * @return
     */
    protected abstract String getFragmentName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.base_layout);


        String fragmentName = getFragmentName();
        if (fragmentName == null) {
            finish();
            return;
        }

		getFragmentManager().enableDebugLogging(true);
		if (savedInstanceState == null) {
			changeFragment(fragmentName);
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
		mNavigationView.setNavigationItemSelectedListener(this);

		if(ParseUser.getCurrentUser() != null) {
			((TextView) findViewById(R.id.navigation_drawer_username)).setText(ParseUser
					.getCurrentUser().getUsername());
			((TextView) findViewById(R.id.navigation_drawer_email)).setText(ParseUser
					.getCurrentUser().getEmail());
		}
    }

	public void changeFragment(String toFragment){
		changeFragment(toFragment,null);
	}

    public void changeFragment(String toFragment, Bundle args){
		Log.d(TAG, "changeFragment");
		Util.hideSoftKeyboard(this);
		Fragment fragment = null;
		String backStateName = toFragment;

		Fragment mCurrentFragment = getFragmentManager().findFragmentById(R.id
				.fragment_place);

		if (mCurrentFragment == null) {
			fragment = instantiateFragment(toFragment, args);
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.fragment_slide_in, R.animator.fragment_slide_out, 0, 0)
					.add(R.id.fragment_place, fragment, backStateName)
					.addToBackStack(backStateName)
					.commit();
			return;
		}

		if (mCurrentFragment.getClass().getName().equals(toFragment)) {
			return;
		}

		boolean fragmentPopped = getFragmentManager().popBackStackImmediate(backStateName, 0);

		if (!fragmentPopped && getFragmentManager().findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
			fragment = instantiateFragment(toFragment, args);
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.fragment_slide_in, R.animator.fragment_slide_out, 0, 0)
					.add(R.id.fragment_place, fragment, backStateName)
					.addToBackStack(backStateName)
					.commit();
		}
    }

	private Fragment instantiateFragment(String toFragmentName, Bundle args) {
		Fragment fragment = Fragment.instantiate(this, toFragmentName);
		if (args != null) {
			fragment.setArguments(args);
		}
		return fragment;
	}

	/**
	 * Because of implements NavigationView.OnNavigationItemSelectedListener
	 * Better styling of code
	 * @param menuItem
	 * @return
	 */
	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		Log.d(TAG, "onNavigationItemSelected");

		switch (menuItem.getItemId()){
			case R.id.drawer_menu_item_home:
				changeFragment(TestMapFragment.class.getName());
				break;

			case R.id.drawer_menu_item_setting:
				changeFragment(SettingFragment.class.getName());
				break;

			case R.id.drawer_menu_item_about_me:
				changeFragment(RankingFragment.class.getName());
				break;

			case R.id.drawer_menu_item_log_out:
				startActivity(new Intent(this, LoginActivity.class));
				ParseUser.logOutInBackground();
				this.finish();
				break;
		}
		menuItem.setChecked(true);
		mDrawerLayout.closeDrawers();
		return true;
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed(); // after that I cant finnish this clicking back button
		Log.d(TAG, "onBackPressed");
		Util.hideSoftKeyboard(this);
		if (getFragmentManager().popBackStackImmediate()) {

		} else {
			Snackbar.make(findViewById(R.id.main_content_frame), getString(R.string.snacbar_back_button_message), Snackbar.LENGTH_SHORT)
					.setAction("Ano", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.d(TAG, "onClick");
							finish();
						}
					})
					.setActionTextColor(getResources().getColor(R.color.colorDanger))
					.show();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onRestart");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onSaveInstanceState");
		}
		//outState.putString(SAVE_FRAGMENT,getFragmentManager().findFragmentById(R.id.map));
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onRestoreInstanceState");
		}
	}
}

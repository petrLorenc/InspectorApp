package cz.united121.android.revizori.activity.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.FullMapFragment;
import cz.united121.android.revizori.fragment.LoginFragment;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = BaseActivity.class.getName();

	protected DrawerLayout mDrawerLayout;
	protected NavigationView mNavigationView;
	protected Fragment mCurrentFragment;
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
			mCurrentFragment = fragment;
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
		mCurrentFragment = fragment;
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
		mNavigationView.setNavigationItemSelectedListener(this);

    }

    public void changeFragment(String toFragment){
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
		mCurrentFragment = fragment;
    }

	@Override
    protected void onStop() {
        super.onStop();
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
				changeFragment(FullMapFragment.class.getName());
				break;

			case R.id.drawer_menu_item_setting:
				Toast.makeText(BaseActivity.this, "Make a Toast", Toast.LENGTH_SHORT).show();
				break;

			case R.id.drawer_menu_item_about_me:
				Toast.makeText(BaseActivity.this, "Make a Toast", Toast.LENGTH_SHORT).show();
				break;

			case R.id.drawer_menu_item_log_out:
				Toast.makeText(BaseActivity.this, "Logout", Toast.LENGTH_SHORT).show();
				break;
		}
		menuItem.setChecked(true);
		mDrawerLayout.closeDrawers();
		return true;
	}
}

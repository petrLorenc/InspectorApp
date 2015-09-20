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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.LoginActivity;
import cz.united121.android.revizori.fragment.FullMapFragment;
import cz.united121.android.revizori.fragment.SettingFragment;
import cz.united121.android.revizori.model.Images;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String TAG = BaseActivity.class.getName();

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
			// checking savedInstanceState - after rotate it wont be null -> no adding same fragment second time
			if(savedInstanceState == null ) {
				getFragmentManager()
						.beginTransaction()
						.add(R.id.fragment_place, fragment, fragment.getClass().getName())
						.commit();
			}
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
		mNavigationView.setNavigationItemSelectedListener(this);

		if(ParseUser.getCurrentUser() != null) {
			((TextView) findViewById(R.id.navigation_drawer_username)).setText(ParseUser
					.getCurrentUser().getUsername());
			((TextView) findViewById(R.id.navigation_drawer_email)).setText(ParseUser
					.getCurrentUser().getEmail());
			final ImageView icon = ((ImageView) findViewById(R.id.navigation_drawer_image));
			ParseQuery<Images> imagesParseQuery = ParseQuery.getQuery(Images.PARSE_TAG);
			imagesParseQuery.whereEqualTo(Images.USER, ParseUser.getCurrentUser());
			imagesParseQuery.getFirstInBackground(new GetCallback<Images>() {
				@Override
				public void done(Images image, ParseException e) {
					Log.d(TAG, "done");
					Picasso.with(getApplicationContext()).load(image.getUrlImage()).into(icon);
				}
			});
		}
    }

	public void changeFragment(String toFragment){
		changeFragment(toFragment,null);
	}

    public void changeFragment(String toFragment, Bundle args){
		Log.d(TAG, "onNavigationItemSelected");
		Fragment mCurrentFragment = getFragmentManager().findFragmentById(R.id
				.fragment_place);
		if(mCurrentFragment.getClass().getName() == toFragment){
			return;
		}
		Util.hideSoftKeyboard(this);
        Fragment fragment = getFragmentManager().findFragmentByTag(toFragment);
        if(fragment == null){
			fragment = Fragment.instantiate(this,toFragment);
			if(args != null){
				fragment.setArguments(args);
			}
			getFragmentManager().beginTransaction()
                    .setCustomAnimations(
							R.animator.fragment_slide_in, R.animator.fragment_slide_out,0,0)
					.replace(R.id.fragment_place,fragment)
					.addToBackStack(null)
					.commit();
		}else{
			getFragmentManager().beginTransaction()
					.setCustomAnimations(
							R.animator.fragment_slide_in, R.animator.fragment_slide_out,0,0)
					.replace(R.id.fragment_place,fragment)
					.addToBackStack(null)
					.commit();
		}
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
				changeFragment(SettingFragment.class.getName());
				break;

			case R.id.drawer_menu_item_about_me:
				Toast.makeText(BaseActivity.this, "Make a Toast", Toast.LENGTH_SHORT).show();
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
		Snackbar.make(findViewById(R.id.main_content_frame),getString(R.string.snacbar_back_button_message),Snackbar.LENGTH_SHORT)
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

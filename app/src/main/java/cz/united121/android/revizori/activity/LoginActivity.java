package cz.united121.android.revizori.activity;

import android.content.Intent;

import com.parse.ParseUser;

import cz.united121.android.revizori.activity.base.BaseActivityNoDrawer;
import cz.united121.android.revizori.fragment.LoginFragment;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public class LoginActivity extends BaseActivityNoDrawer {
    public static final String TAG = LoginActivity.class.getName();

    @Override
    protected String getFragmentName() {
        if(ParseUser.getCurrentUser() == null){
			return LoginFragment.class.getName();
		}else{
			startActivity(new Intent(LoginActivity.this, MapActivity.class));
			return null; // this finish actual activity
		}
    }
}

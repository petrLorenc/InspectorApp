package cz.united121.android.revizori.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
@ParseClassName("User")
public class User extends ParseUser {
	public static final String TAG = User.class.getName();

	public User() {
	}

	public User(String name, String password, String email){
		this.setUsername(name);
		this.setPassword(password);
		this.setEmail(email);
	}

	public String getUsername(){
		Log.d(TAG, "getUsername");
		return super.getUsername();
	}

	public String getEmail(){
		Log.d(TAG,"getEmail");
		return super.getEmail();
	}
}

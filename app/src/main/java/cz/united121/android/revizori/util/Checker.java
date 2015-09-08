package cz.united121.android.revizori.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
public class Checker {
	public static final String TAG = Checker.class.getName();

	public static boolean checkUsername(String username){
		//TODO
		return true;
	}

	public static boolean checkPassword(String password){
		//TODO
		return true;
	}

	public static boolean checkEmail(String email){
		//TODO
		return true;
	}

	public static boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}

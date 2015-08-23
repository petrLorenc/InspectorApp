package cz.united121.android.revizori.util;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * TODO add class description TEST
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
public class Util {
	public static final String TAG = Util.class.getName();

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		try {
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}catch (NullPointerException e){
			Log.d(TAG, "hideSoftKeyboard - NullPointerException");
			e.printStackTrace();
		}
	}
}

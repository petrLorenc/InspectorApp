package cz.united121.android.revizori.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
public class Util {
	public static final String TAG = Util.class.getName();

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}

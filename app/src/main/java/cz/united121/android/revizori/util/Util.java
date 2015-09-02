package cz.united121.android.revizori.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
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

	public static void makeAlertDialogGPS(final Activity actualActivity, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(actualActivity);
		final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

		builder.setMessage(message)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								actualActivity.startActivity(new Intent(action));
								d.dismiss();
							}
						})
				.setNegativeButton("Ignorovat",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								d.cancel();
							}
						});
		builder.create().show();
	}

	public static void makeAlertDialogOnlyOK(final Activity actualActivity, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(actualActivity);
		builder.setMessage(message)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								d.dismiss();
							}
						});
		builder.create().show();
	}
}

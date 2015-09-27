package cz.united121.android.revizori.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import cz.united121.android.revizori.R;

/**
 * TODO add class description TEST
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
public class Util {
	public static final String TAG = Util.class.getName();
	public static boolean shovingDiaglog = false;

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		try {
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		} catch (NullPointerException e) {
			Log.d(TAG, "hideSoftKeyboard - NullPointerException");
			e.printStackTrace();
		}
	}

	public static void makeAlertDialogGPS(final Activity actualActivity, String message) {
		if (shovingDiaglog == false) {
			shovingDiaglog = true;
		} else {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(actualActivity);
		final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;

		builder.setMessage(message)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								actualActivity.startActivityForResult(new Intent(action), 200);
								shovingDiaglog = false;
								d.dismiss();
							}
						})
				.setNegativeButton("Ignorovat",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								shovingDiaglog = false;
								d.cancel();
							}
						});
		builder.create().show();
	}

	public static void makeAlertDialogOnlyOK(final Activity actualActivity, String message) {
		if (shovingDiaglog == false) {
			shovingDiaglog = true;
		} else {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(actualActivity);
		builder.setMessage(message)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int id) {
								shovingDiaglog = false;
								d.dismiss();
							}
						});
		builder.create().show();
	}

	public static void makeNotification(Context mContext, String title, String contentText, String ticker, Class toClass) {
		NotificationCompat.Builder inspectorIsNearNotification = new NotificationCompat.Builder(mContext)
				.setSmallIcon(R.drawable.ic_stat_alert_error)
				.setContentTitle(title)
				.setContentText(contentText)
				.setTicker(ticker)
				.setAutoCancel(true)
				.setStyle(new NotificationCompat.InboxStyle())
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		Intent intent = new Intent(mContext, toClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		inspectorIsNearNotification.setContentIntent(resultPendingIntent);
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, inspectorIsNearNotification.build());
	}

	public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}

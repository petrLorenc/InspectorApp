package cz.united121.android.revizori;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseObject;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cz.united121.android.revizori.model.Images;
import cz.united121.android.revizori.model.ReportInspector;
import io.fabric.sdk.android.Fabric;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public class App extends Application {
    public static final String TAG = App.class.getName();

	private static Context context;

    String mApplicationID = "3usYnV6BWJig2jUImcueR6o7pyRoZQaqtjeKgzsT";
    String mClientKey = "rZJpDl2jVAQym82wpjvy6tJqbOnMWk0FhCumqUbi";
	private RefWatcher refWatcher;

	public static RefWatcher getRefWatcher(Context context) {
		App application = (App) context.getApplicationContext();
		return application.refWatcher;
	}

	public static Context getAppContext() {
		return App.context.getApplicationContext();
	}

    @Override
    public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		Log.d(TAG, "onCreate");

		LeakCanary.install(this);

		Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(ReportInspector.class);
		ParseObject.registerSubclass(Images.class);
		Parse.initialize(this, mApplicationID, mClientKey);

		App.context = getApplicationContext();
	}


//	public static Display getDisplay(){
//		WindowManager wm = (WindowManager) sApp.getSystemService(Context.WINDOW_SERVICE);
//		return wm.getDefaultDisplay();
//	}
}

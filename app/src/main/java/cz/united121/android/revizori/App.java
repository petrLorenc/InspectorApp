package cz.united121.android.revizori;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;

import cz.united121.android.revizori.model.ReportInspector;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public class App extends Application {
    public static final String TAG = App.class.getName();

    String mApplicationID = "3usYnV6BWJig2jUImcueR6o7pyRoZQaqtjeKgzsT";
    String mClientKey = "rZJpDl2jVAQym82wpjvy6tJqbOnMWk0FhCumqUbi";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(ReportInspector.class);
        Parse.initialize(this, mApplicationID, mClientKey);
    }
}

package cz.united121.android.revizori.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Locale;
import java.util.TimeZone;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/21/2015}
 **/
@ParseClassName("ReportInspector")
public class ReportInspector extends ParseObject{
	public static final String TAG = ReportInspector.class.getName();

	private static final String stringLocation = "Location";
	private static final String stringTimeZone = "TimeZone";

	private ParseGeoPoint mLocation;
	private TimeZone mTimeZone; // for further extension

	public ReportInspector() {
	}

	public ReportInspector(ParseGeoPoint location, TimeZone timeZone) {
		put(stringLocation,location);
		put(stringTimeZone,TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT, Locale
				.getDefault()));
		this.saveInBackground();
	}

	public ParseGeoPoint getLocation(){
		return getParseGeoPoint(stringLocation);
	}
}

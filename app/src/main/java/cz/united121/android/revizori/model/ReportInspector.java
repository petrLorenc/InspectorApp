package cz.united121.android.revizori.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Locale;
import java.util.TimeZone;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/21/2015}
 **/
@ParseClassName("ReportInspector")
public class ReportInspector extends ParseObject {
	public static final String TAG = ReportInspector.class.getName();

	private static final String LOCATION = "Location";
	private static final String TIMEZONE = "TimeZone";
	private static final String USER = "User";
	private static final String COMMENT = "Comment";
	private static final String TYPEOFVEHICLE = "TypeOfVehicle";

	private ParseUser mUser;
	private ParseGeoPoint mLocation;
	private TimeZone mTimeZone; // for further extension
	private TypeOfVehicle mTypeOfVehicle;
	private String mComment;

	public ReportInspector() {
	}

	public ReportInspector(ParseUser user, ParseGeoPoint location, String typeOfVehicle) {
		put(USER, user);
		put(LOCATION, location);
		put(TIMEZONE, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT, Locale
				.getDefault()));
		put(TYPEOFVEHICLE, typeOfVehicle);
		this.saveInBackground();
	}

	public void setComment(String comment) {
		put(COMMENT, comment);
		this.saveInBackground();
	}

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint(LOCATION);
	}

	public interface TypeOfVehicle {

		String METRO = "Metro";
		String BUS = "Bus";
		String TRAM = "Tram";
	}
}

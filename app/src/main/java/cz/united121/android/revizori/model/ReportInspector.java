package cz.united121.android.revizori.model;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Locale;
import java.util.TimeZone;

import cz.united121.android.revizori.model.helper.TypeOfVehicle;

/**
 * After set have to call saving function
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
	private static final String NAME_OF_STATION = "NameOfStation";

	private LatLng mPosition;

	public ReportInspector() {
	}

	public ReportInspector(ParseUser user, ParseGeoPoint location, String typeOfVehicle) {
		put(USER, user);
		put(LOCATION, location);
		put(TIMEZONE, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT, Locale
				.getDefault()));
		put(TYPEOFVEHICLE, typeOfVehicle);
	}

	public ReportInspector(ParseUser user, ParseGeoPoint location, TypeOfVehicle typeOfVehicle,
						   String commentIfAvailable, String nameOfStationIfAvailable) {
		put(USER, user);
		put(LOCATION, location);
		put(TIMEZONE, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT, Locale
				.getDefault()));
		put(TYPEOFVEHICLE, typeOfVehicle.name());
		put(COMMENT, commentIfAvailable);
		put(NAME_OF_STATION, nameOfStationIfAvailable);
	}

	public static ParseQuery<ReportInspector> getQuery() {
		return ParseQuery.getQuery(ReportInspector.class);
	}

	public String getUserName() {
		return getParseUser(USER).getUsername();
	}

	public String getComment() {
		return getString(COMMENT);
	}

	public void setComment(String comment) {
		put(COMMENT, comment);
	}

	public String getStringTypeOfVehicle() {
		//return TypeOfVehicle.valueOf(getString(TYPEOFVEHICLE));
		return getString(TYPEOFVEHICLE);
	}

	public void setNameOfStation(String nameOfStation) {
		put(NAME_OF_STATION, nameOfStation);
	}

	public LatLng getLocation() {
		if (mPosition == null) {
			ParseGeoPoint parseGeoPoint = getParseGeoPoint(LOCATION);
			mPosition = new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
		}
		return mPosition;
	}
}

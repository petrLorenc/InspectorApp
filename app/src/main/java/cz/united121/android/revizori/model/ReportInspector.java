package cz.united121.android.revizori.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Locale;
import java.util.TimeZone;

/**
 * After set have to call saving function
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/21/2015}
 **/
@ParseClassName("ReportInspector")
public class ReportInspector extends ParseObject implements ClusterItem {
	public static final String TAG = ReportInspector.class.getName();

	private static final String LOCATION = "Location";
	private static final String TIMEZONE = "TimeZone";
	private static final String USER = "User";
	private static final String COMMENT = "Comment";
	private static final String TYPEOFVEHICLE = "TypeOfVehicle";
	private static final String NAME_OF_STATION = "NameOfStation";

//	private ParseUser mUser;
//	private ParseGeoPoint mLocation;
//	private TimeZone mTimeZone; // for further extension
//	private TypeOfVehicle mTypeOfVehicle;
//	private String mComment;
//	private String mNameOfStation;

	private LatLng mPosition;
	private Marker mMarker;

	public ReportInspector() {
	}

	public ReportInspector(ParseUser user, ParseGeoPoint location, String typeOfVehicle) {
		put(USER, user);
		put(LOCATION, location);
		put(TIMEZONE, TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT, Locale
				.getDefault()));
		put(TYPEOFVEHICLE, typeOfVehicle);
	}

	public void setComment(String comment) {
		put(COMMENT, comment);
	}
	public void setNameOfStation(String nameOfStation) {
		put(NAME_OF_STATION, nameOfStation);
	}
	public String getTypeOfVehicle(){
		return getString(TYPEOFVEHICLE);
	}

	public LatLng getLocation() {
		ParseGeoPoint parseGeoPoint = getParseGeoPoint(LOCATION);
		mPosition = new LatLng(parseGeoPoint.getLatitude(),parseGeoPoint.getLongitude());
		return mPosition;
	}

	public Marker getMarker(GoogleMap map){
		if(mMarker == null){
			mMarker = map.addMarker(new MarkerOptions().
					position(mPosition != null ? mPosition : getLocation()).
					title(getTypeOfVehicle()));
		}
		return mMarker;
	}

	public static ParseQuery<ReportInspector> getQuery(){
		return ParseQuery.getQuery(ReportInspector.class);
	}

	/**
	 * Cluster item override - to make cluster point on map
	 * @return
	 */
	@Override
	public LatLng getPosition() {
		if(mPosition != null){
			return mPosition;
		}
		return getLocation();
	}

	public interface TypeOfVehicle {

		String METRO = "Metro";
		String BUS = "Bus";
		String TRAM = "Tram";
	}
}

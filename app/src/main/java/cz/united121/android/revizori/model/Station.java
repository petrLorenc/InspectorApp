package cz.united121.android.revizori.model;

import android.database.Cursor;
import android.util.Log;

import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/30/2015}
 **/
public class Station {
	public static final String TAG = Station.class.getName();

	private String mLine;
	private String mName;
	private double mLatitude;
	private double mLongtitude;
	public Station(String name, double latitude, double longtitude, String line) {
		mName = name;
		mLatitude = latitude;
		mLongtitude = longtitude;
		mLine = line;
	}

	public String getName() {
		return mName;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public double getLongtitude() {
		return mLongtitude;
	}

	public int getPicture() {
		if (mLine.equals("A")) {
			return R.drawable.a_letter;
		} else if (mLine.equals("B")) {
			return R.drawable.b_letter;
		} else if (mLine.equals("C")) {
			return R.drawable.c_letter;
		}
		return 0;
	}

	public static Station constructFromCursor(Cursor cursor) {
		// String[] sqlSelect = {DAT_LINE,DAT_NAME, DAT_LATITUDE, DAT_LONGTITUDE}; from MyDatabase
		Log.d(TAG, "constructFromCursor");
		return new Station(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3),cursor
				.getString(0) );
	}
}

package cz.united121.android.revizori.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Loading data from database (assets/databases/...{DATABASE_NAME}
 * Provide basic Cursor
 * USING: For static data - like pocition of Metro stations
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/25/2015}
 **/
public class MyDatabase extends SQLiteAssetHelper {
	public static final String TAG = MyDatabase.class.getName();

	private static final String DATABASE_NAME = "database_stations.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME = "PositionOfStation";

	private static final String DAT_LINE = "LINE";
	private static final String DAT_NAME = "NAME";
	private static final String DAT_LATITUDE = "LAT";
	private static final String DAT_LONGTITUDE = "LON";

	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static Cursor getStationsAll(Context context) {
		return new MyDatabase(context).getStations("");
	}

	public static Cursor getStationsA(Context context) {
		return new MyDatabase(context).getStations("A");
	}

	public static Cursor getStationsB(Context context) {
		return new MyDatabase(context).getStations("B");
	}

	public static Cursor getStationsC(Context context) {
		return new MyDatabase(context).getStations("C");
	}

	/**
	 *
	 * @param line If line equal nothing "" then return all stations
	 * @return
	 */
	private Cursor getStations(String line) {
		Log.d(TAG, "getStations" + line);
		SQLiteDatabase db = getReadableDatabase(); // non static
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String[] sqlSelect = {DAT_LINE, DAT_NAME, DAT_LATITUDE, DAT_LONGTITUDE};
		String sqlWhere = DAT_LINE + " = ?";
		String[] sqlArg = {line};

		qb.setTables(TABLE_NAME);
		Cursor c = null;
		if (!line.equals("")) {
			c = qb.query(db, sqlSelect, sqlWhere, sqlArg,
					null, null, null);
		} else {
			c = qb.query(db, sqlSelect, null, null,
					null, null, null);
		}
		c.moveToFirst();
		return c;
	}
}

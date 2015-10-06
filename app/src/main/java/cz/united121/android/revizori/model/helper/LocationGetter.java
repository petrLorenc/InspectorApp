package cz.united121.android.revizori.model.helper;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import cz.united121.android.revizori.App;
import cz.united121.android.revizori.model.MapPoint;
import cz.united121.android.revizori.model.ReportInspector;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/13/2015}
 **/
public class LocationGetter {
	public static final String TAG = LocationGetter.class.getName();

	private static List<ReportInspector> mReportList = new ArrayList<>();

	private static Context applicationContext;

	public static void addReport(ReportInspector reportInspector) {
		mReportList.add(reportInspector);
		reportInspector.saveInBackground();
	}

	public static void refreshReportsTo(List<ReportInspector> toPreserve) {
		deleteRedundantReports(toPreserve);
		for (ReportInspector reportInspector : toPreserve) {
			if (!mReportList.contains(reportInspector)) {
				mReportList.add(reportInspector);
			}
		}
	}

	public static void deleteReportsByList(List<ReportInspector> list) {
		mReportList.removeAll(list);
	}

	public static void deleteAllReports() {
		mReportList.clear();
	}

	public static void deleteRedundantReports(List<ReportInspector> toPreserve) {
		List<ReportInspector> toDelete = new ArrayList<>();
		for (ReportInspector reportInspectorToPreserve : toPreserve) {
			if (!mReportList.contains(reportInspectorToPreserve)) {
				toDelete.add(reportInspectorToPreserve);
			}
		}
		deleteReportsByList(toDelete);
	}

	public static RealmResults<MapPoint> getReportsWithUpdate(final OnDownloadDataFromNet onDownloadDataFromNet) {
		Realm realm = Realm.getInstance(App.getAppContext());
		RealmQuery<MapPoint> mapPointRealmQuery = realm.where(MapPoint.class);
		RealmResults<MapPoint> results = mapPointRealmQuery.findAll();

		ParseQuery<ReportInspector> internetQuery = new ParseQuery<ReportInspector>("ReportInspector");
		internetQuery.findInBackground(new FindCallback<ReportInspector>() {
			@Override
			public void done(List<ReportInspector> list, ParseException e) {
				if (e != null) {
					return;
				}
				Realm realm = Realm.getInstance(App.getAppContext());
				realm.beginTransaction();
				realm.clear(MapPoint.class);
				for (ReportInspector reportInspector : list) {
					MapPoint mapPoint = realm.createObject(MapPoint.class);

					mapPoint.setAuthor(reportInspector.getUserName());
					if (reportInspector.getComment() != null) {
						mapPoint.setComment(reportInspector.getComment());
						mapPoint.setIscommentpresent(true);
					} else {
						mapPoint.setIscommentpresent(false);
					}
					mapPoint.setTranporttype(reportInspector.getStringTypeOfVehicle());
					mapPoint.setLatitude(reportInspector.getLocation().latitude);
					mapPoint.setLongitude(reportInspector.getLocation().longitude);
				}
				realm.commitTransaction();
				onDownloadDataFromNet.dataWasSavedToRealmDatabase();
			}
		});

		return results;
	}

	public static RealmResults<MapPoint> getReportsWithoutUpdate() {
		Realm realm = Realm.getInstance(App.getAppContext());
		RealmQuery<MapPoint> mapPointRealmQuery = realm.where(MapPoint.class);
		RealmResults<MapPoint> results = mapPointRealmQuery.findAll();
		return results;
	}


	public interface OnDownloadDataFromNet {
		void dataWasSavedToRealmDatabase();
	}

}

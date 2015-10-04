package cz.united121.android.revizori.model.helper;

import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import cz.united121.android.revizori.model.ReportInspector;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/13/2015}
 **/
public class LocationGetter {
	public static final String TAG = LocationGetter.class.getName();

	private static List<ReportInspector> mReportList = new ArrayList<>();

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

	public static ParseQuery<ReportInspector> getReports() {
		return new ParseQuery<ReportInspector>("ReportInspector");
	}
}

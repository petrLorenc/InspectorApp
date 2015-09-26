package cz.united121.android.revizori.model.helper;

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

	public static void refreshReportsTo(List<ReportInspector> toPreserve) {
		deleteAllReports();
		mReportList.addAll(toPreserve);
//		for(ReportInspector reportInspector : toPreserve){
//			if(!mReportList.contains(reportInspector)){
//				mReportList.add(reportInspector);
//			}
//		}
		//deleteRedundantReports(toPreserve);
	}

	public static void deleteReports(List<ReportInspector> list){
		for (ReportInspector reportInspector : list) {
			if (mReportList.contains(reportInspector)) {
				reportInspector.removeMarker();
			}
		}
		mReportList.removeAll(list);
	}

	public static void deleteAllReports() {
		for (ReportInspector reportInspector : mReportList) {
			reportInspector.removeMarker();
		}
		mReportList.clear();
	}

	public static void deleteRedundantReports(List<ReportInspector> toPreserve) {
		List<ReportInspector> toDelete = new ArrayList<>();
		for (ReportInspector reportInspectorToPreserve : toPreserve) {
			if (!mReportList.contains(reportInspectorToPreserve)) {
				toDelete.add(reportInspectorToPreserve);
			}
		}
		deleteReports(toDelete);
	}

	public static List<ReportInspector> getReports(){
		return mReportList;
	}
}

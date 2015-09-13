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

	public static List<ReportInspector> mReportList = new ArrayList<>();

	public static void addReports(List<ReportInspector> list){
		for(ReportInspector reportInspector : list){
			if(!mReportList.contains(reportInspector)){
				mReportList.add(reportInspector);
			}
		}
	}

	public static void deleteReports(List<ReportInspector> list){
		mReportList.removeAll(list);
	}

	public static List<ReportInspector> getReports(){
		return mReportList;
	}
}

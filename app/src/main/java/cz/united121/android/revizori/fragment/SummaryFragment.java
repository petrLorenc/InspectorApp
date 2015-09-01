package cz.united121.android.revizori.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.model.ReportInspector;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/1/2015}
 **/
public class SummaryFragment extends BaseFragment {
	public static final String TAG = SummaryFragment.class.getName();

	public static final String BUNDLE_LATITUDE = "sum_latitude";
	public static final String BUNDLE_LONGITUDE = "sum_longitude";
	public static final String BUNDLE_TYPEOFVEHICLE = "sum_typeOfVehicle";
	public static final String BUNDLE_NAMEOFSTATION = "sum_nameOfStation";

	@Bind(R.id.summary_text_view)
	TextView mSummary;

	private ReportInspector mReportInspector;

	@Override
	public int getLayout() {
		Log.d(TAG, "getLayout");
		return R.layout.fragment_summary;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =  super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "onCreateView");

		ButterKnife.bind(this,view);
		Bundle passArg = getArguments();
		if(passArg == null){
			mSummary.setText("Nic");
			return view;
		}
		passArg.getDouble(BUNDLE_LATITUDE, 0.0);
		passArg.getDouble(BUNDLE_LONGITUDE, 0.0);
		passArg.getString(BUNDLE_TYPEOFVEHICLE, ReportInspector.TypeOfVehicle.BUS); // default is BUS

		mReportInspector = new ReportInspector(
				ParseUser.getCurrentUser(),
				new ParseGeoPoint(passArg.getDouble(BUNDLE_LATITUDE,0.0),
						          passArg.getDouble(BUNDLE_LONGITUDE, 0.0)),
				passArg.getString(BUNDLE_TYPEOFVEHICLE,ReportInspector.TypeOfVehicle.BUS));
				// default is BUS);

		mSummary.setText(passArg.getString(BUNDLE_NAMEOFSTATION,"Neznámá"));

		return view;
	}
}

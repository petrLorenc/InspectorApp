package cz.united121.android.revizori.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.model.ReportInspector;
import cz.united121.android.revizori.model.helper.TypeOfVehicle;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/1/2015}
 **/
public class SummaryFragment extends BaseFragment {
	public static final String TAG = SummaryFragment.class.getName();

	public static final String BUNDLE_LATITUDE = "sum_latitude";
	public static final String BUNDLE_LONGITUDE = "sum_longitude";
	public static final String BUNDLE_TYPE_OF_VEHICLE = "sum_typeOfVehicle";
	public static final String BUNDLE_NAME_OF_STATION = "sum_nameOfStation";

	@Bind(R.id.summary_text_view)
	EditText mSummary;
	@Bind(R.id.summary_text_view_thanks)
	TextView mThanksText;
	Bundle passArg;
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

		ButterKnife.bind(this, view);

		mThanksText.setText(Html.fromHtml(getString(R.string.summary_thanks_and_request)));

		passArg = getArguments();

		return view;
	}

	@OnClick(R.id.summary_to_map_button)
	public void onSaveAndReturnToMap(View view) {
		savingReportFromPassArg();
		((BaseActivity) getActivity()).changeFragment(FullMapFragment.class.getName());
	}

	private void savingReportFromPassArg() {
		if (passArg == null) {
			Log.e(TAG, "passArg == null so we cant save report inspector");
			return;
		}
		mReportInspector = new ReportInspector(
				ParseUser.getCurrentUser(),
				new ParseGeoPoint(passArg.getDouble(BUNDLE_LATITUDE, 0.0),
						passArg.getDouble(BUNDLE_LONGITUDE, 0.0)),
				TypeOfVehicle.valueOf(passArg.getString(BUNDLE_TYPE_OF_VEHICLE)),
				mSummary.getText().toString(),
				passArg.getString(BUNDLE_NAME_OF_STATION, "Neznámá"));

		mReportInspector.saveInBackground();
		ParseUser.getCurrentUser().increment("score");
		ParseUser.getCurrentUser().saveInBackground();
	}
}

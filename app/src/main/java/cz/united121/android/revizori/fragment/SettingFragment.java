package cz.united121.android.revizori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.service.MyTrackingService;
import cz.united121.android.revizori.util.Util;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/23/2015}
 **/
public class SettingFragment extends BaseFragment {
	public static final String TAG = SettingFragment.class.getName();

	@Bind(R.id.setting_toggle_button_start_service)
	ToggleButton mToggleButton;

	@Override
	public int getLayout() {
		Log.d(TAG, "getLayout");
		return R.layout.fragment_setting;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "onCreateView");
		}
		ButterKnife.bind(this, view);

		mToggleButton.setChecked(Util.isMyServiceRunning(getActivity(), MyTrackingService.class));

		mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Intent startIntent = new Intent(getActivity(), MyTrackingService.class);
					startIntent.setAction(MyTrackingService.SERVICE_START);
					getActivity().startService(startIntent);
				} else {
					Intent startIntent = new Intent(getActivity(), MyTrackingService.class);
					startIntent.setAction(MyTrackingService.SERVICE_STOP);
					getActivity().startService(startIntent);
				}
			}
		});

		return view;
	}


	@OnClick(R.id.setting_button_reset_password)
	public void resetPassword(View view){
		ParseUser currentUser = ParseUser.getCurrentUser();
		ParseUser.requestPasswordResetInBackground(currentUser.getEmail());
		Util.makeAlertDialogOnlyOK(getActivity(),"Nové heslo bude zasláno na Váš email (" + currentUser.getEmail() + ")." );
	}
}

package cz.united121.android.revizori.fragment.dialog;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/11/2015}
 **/
public class AlertDialogFragment extends DialogFragment {
	public static final String TAG = AlertDialogFragment.class.getName();

	public static final String ARG_TITLE = "ARG_TITLE";
	public static final String ARG_DESC = "ARG_DESC";
	public static final String ARG_YES = "ARG_YES";
	public static final String ARG_NO = "ARG_NO";

	@Bind(R.id.alert_dialog_title)
	TextView titleView;
	@Bind(R.id.alert_dialog_description)
	TextView descriptionView;
	@Bind(R.id.alert_dialog_yes_button)
	Button yestButton;
	@Bind(R.id.alert_dialog_no_button)
	Button noButton;

	AlertOnClickListener mAlertOnClickListener;

	public static AlertDialogFragment newInstance(Fragment targetFragment,
												  String title,
												  String desc,
												  String text_yes,
												  String text_no){
		AlertDialogFragment fragment = new AlertDialogFragment();

		fragment.setTargetFragment(targetFragment, 200);

		Bundle bundle = new Bundle();
		bundle.putString(ARG_TITLE,title);
		bundle.putString(ARG_DESC, desc);
		bundle.putString(ARG_YES, text_yes);
		bundle.putString(ARG_NO, text_no);

		fragment.setArguments(bundle);

		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.dialog_fragment_alert,container,false);
		ButterKnife.bind(this, view);

		try {
			mAlertOnClickListener = (AlertOnClickListener) getTargetFragment();
		}catch (ClassCastException e){
			throw new ClassCastException("Target fragment have to implement AlertOnClickListener");
		}

		titleView.setText(getArguments().getString(ARG_TITLE));
		descriptionView.setText(getArguments().getString(ARG_DESC));
		yestButton.setText(getArguments().getString(ARG_YES));
		noButton.setText(getArguments().getString(ARG_NO));

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.d(TAG, "onViewCreated");
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		ButterKnife.unbind(this);
	}

	@OnClick(R.id.alert_dialog_yes_button)
	public void onYesClick(View view){
		mAlertOnClickListener.onPositiveClick(this);
	}
	@OnClick(R.id.alert_dialog_no_button)
	public void onNoClick(View view){
		mAlertOnClickListener.onNegativeClick(this);
	}

	public interface AlertOnClickListener {
		void onPositiveClick(AlertDialogFragment dialogFragment);

		void onNegativeClick(AlertDialogFragment dialogFragment);
	}
}

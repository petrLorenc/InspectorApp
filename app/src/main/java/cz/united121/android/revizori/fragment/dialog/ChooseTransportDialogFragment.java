package cz.united121.android.revizori.fragment.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/26/2015}
 **/
public class ChooseTransportDialogFragment extends DialogFragment {
	public static final String TAG = ChooseTransportDialogFragment.class.getName();

	private static int optionalRequestCode = 201;

	private static ChooseTransportInterface mChooseTransportInterface;

	public static ChooseTransportDialogFragment newInstance(ChooseTransportInterface chooseTransportInterface) {
		ChooseTransportDialogFragment chooseTransportDialogFragment = new ChooseTransportDialogFragment();

		chooseTransportDialogFragment.setInterfaceForCommunication(chooseTransportInterface);

		return chooseTransportDialogFragment;
	}

	private void setInterfaceForCommunication(ChooseTransportInterface chooseTransportInterface) {
		mChooseTransportInterface = chooseTransportInterface;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.dialog_fragment_choose_type_transport, container, false);
		ButterKnife.bind(this, view);

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
		ButterKnife.unbind(this);
		//mChooseTransportInterface = null;
		Log.d(TAG, "onDestroyView");
		super.onDestroyView();
	}

	@OnClick(R.id.dialog_choose_type_metro)
	public void onMetroClick(View view) {
		mChooseTransportInterface.OnChoosingMetro(this);
		getDialog().dismiss();
		dismiss();
	}

	@OnClick(R.id.dialog_choose_type_tram)
	public void onTramClick(View view) {
		mChooseTransportInterface.OnChoosingTram(this);
		getDialog().dismiss();
		dismiss();
	}

	@OnClick(R.id.dialog_choose_type_bus)
	public void onBusClick(View view) {
		mChooseTransportInterface.OnChoosingBus(this);
		getDialog().dismiss();
		dismiss();
	}

	public interface ChooseTransportInterface {
		void OnChoosingMetro(ChooseTransportDialogFragment fragment);

		void OnChoosingTram(ChooseTransportDialogFragment fragment);

		void OnChoosingBus(ChooseTransportDialogFragment fragment);
	}
}
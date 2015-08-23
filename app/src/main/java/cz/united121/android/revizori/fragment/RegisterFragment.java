package cz.united121.android.revizori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.LoginActivity;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.model.User;
import cz.united121.android.revizori.util.Checker;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/22/2015}
 **/
public class RegisterFragment extends BaseFragment {
	public static final String TAG = RegisterFragment.class.getName();

	@Bind(R.id.register_fragment_username)
	TextView txt_username;

	@Bind(R.id.register_fragment_password)
	TextView txt_password;

	@Bind(R.id.register_fragment_email)
	TextView txt_email;

	@Bind(R.id.register_content_layout)
	LinearLayout layout_content;

	@Bind(R.id.register_progressBar)
	ProgressBar progress_bar;

	@Override
	public int getLayout() {
		return R.layout.fragment_register;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =  super.onCreateView(inflater, container, savedInstanceState);
		ButterKnife.bind(this, view);

		if(BuildConfig.DEBUG){
			txt_username.setText("United121");
			txt_password.setText("12345");
			txt_email.setText("lorenc55Petr@seznam.cz");
		}

		return view;
	}

	@OnClick(R.id.register_fragment_button_register)
	public void onClickRegister(View view) {
		String username = txt_username.getText().toString();
		String password = txt_password.getText().toString();
		String email = txt_email.getText().toString();

		if(Checker.checkUsername(username)
				&& Checker.checkEmail(email)
				&& Checker.checkPassword(password)){
			layout_content.setVisibility(View.GONE);
			progress_bar.setVisibility(View.VISIBLE);
			User user = new User(username,password,email);
			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(ParseException e) {
					if(e == null){
						startActivity(new Intent(getActivity(), MapActivity.class));
						getActivity().finish();
					}else{
						showError();
					}
				}
			});
		}else{
			showError();
		}
	}

	@OnClick(R.id.register_fragment_button_login)
	public void onClickLogin(View view) {
		Log.d(TAG,"onClickLogin");
		((LoginActivity) getActivity()).changeFragment(LoginFragment.class.getName());
	}

	private void showError(){
		progress_bar.setVisibility(View.GONE);
		layout_content.setVisibility(View.VISIBLE);
		Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
	}
}

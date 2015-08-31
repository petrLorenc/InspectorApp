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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.united121.android.revizori.BuildConfig;
import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.activity.base.BaseActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;
import cz.united121.android.revizori.util.Checker;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public class LoginFragment extends BaseFragment {
    public static final String TAG = LoginFragment.class.getName();

	@Bind(R.id.login_textview_username)
	TextView txt_username;

	@Bind(R.id.login_textview_password)
	TextView txt_password;

	@Bind(R.id.login_progressBar)
	ProgressBar progress_bar_login;

	@Bind(R.id.login_content)
	LinearLayout linear_content;



	@Override
    public int getLayout() {
        return R.layout.fragment_login;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG,"onCreateView");
		ButterKnife.bind(this, view);

		if(BuildConfig.DEBUG){
			txt_username.setText("United121");
			txt_password.setText("12345");
		}

        return view;
    }

	@OnClick(R.id.login_fragment_button_login)
	public void onClickLogin(View view) {
		String username = txt_username.getText().toString();
		String password = txt_password.getText().toString();

		if(!Checker.checkUsername(username) && !Checker.checkPassword(password)){
			return;
		}

		linear_content.setVisibility(View.GONE);
		progress_bar_login.setVisibility(View.VISIBLE);

		ParseUser.logInInBackground(username, password, new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					// Logged in
					startActivity(new Intent(getActivity(), MapActivity.class));
					getActivity().finish();
				} else {
					// Signup failed.
					Toast.makeText(getActivity(), R.string.login_login_error,Toast.LENGTH_SHORT).show();
					progress_bar_login.setVisibility(View.GONE);
					linear_content.setVisibility(View.VISIBLE);
					Log.d(TAG, e.getMessage());
				}
			}
		});
	}

	@OnClick(R.id.login_fragment_button_already_have_account)
	public void onClickHaveAccount(View view) {
		Log.d(TAG, "onClickLogin");
		((BaseActivity) getActivity()).changeFragment(RegisterFragment.class.getName());
	}
}

package cz.united121.android.revizori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cz.united121.android.revizori.R;
import cz.united121.android.revizori.activity.MapActivity;
import cz.united121.android.revizori.fragment.base.BaseFragment;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {6.8.2015}
 */
public class LoginFragment extends BaseFragment {
    public static final String TAG = LoginFragment.class.getName();

    @Override
    public int getLayout() {
        return R.layout.fragment_login;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        Button loginButton = (Button) view.findViewById(R.id.login_fragment_button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MapActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}

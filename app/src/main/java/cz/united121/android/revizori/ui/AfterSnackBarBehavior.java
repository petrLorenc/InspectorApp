package cz.united121.android.revizori.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.parse.ParseClassName;

import java.util.ArrayList;

import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/24/2015}
 **/
public class AfterSnackBarBehavior extends CoordinatorLayout.Behavior<CoordinatorLayout> {
	public static final String TAG = AfterSnackBarBehavior.class.getName();

	/**
	 * Arraylist of views which are not append to CoordationView and dont move with this animation
	 * you have to removing and adding elems by yourself
	 */
	public static ArrayList<View> mStaticViewToMove = new ArrayList<>();

	public AfterSnackBarBehavior(Context context, AttributeSet attrs) {}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, CoordinatorLayout child, View dependency) {
		return dependency instanceof Snackbar.SnackbarLayout;
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent, CoordinatorLayout child, View dependency) {
		float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
		MyFAB fab = (MyFAB) parent.findViewById(R.id
				.fab_reporting_insperctor);
		if(fab != null && fab.isClicked()){
			fab.callOnClick();
		}
		child.setTranslationY(translationY);

		for (View view : mStaticViewToMove){
			if(view != null){
				view.setTranslationY(translationY);
			}
		}
		return true;
	}
}

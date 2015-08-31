package cz.united121.android.revizori.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/24/2015}
 **/
public class AfterSnackBarBehavior extends CoordinatorLayout.Behavior<FrameLayout> {
	public static final String TAG = AfterSnackBarBehavior.class.getName();

	public AfterSnackBarBehavior(Context context, AttributeSet attrs) {}

	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, FrameLayout child, View dependency) {
		return dependency instanceof Snackbar.SnackbarLayout;
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent, FrameLayout child, View dependency) {
		float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
		child.setTranslationY(translationY);

		return true;
	}
}

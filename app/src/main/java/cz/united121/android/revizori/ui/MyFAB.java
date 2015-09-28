package cz.united121.android.revizori.ui;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {8/24/2015}
 **/
public class MyFAB extends FloatingActionButton {
	public static final String TAG = MyFAB.class.getName();

	private boolean wasClicked = false;

	public MyFAB(Context context) {
		super(context);
	}

	public MyFAB(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyFAB(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean performClick() {
		Log.d(TAG, "performClick");
		wasClicked = !wasClicked;
		return super.performClick();
	}

	@Override
	public boolean callOnClick() {
		Log.d(TAG, "callOnClick");
		wasClicked = !wasClicked;
		return super.callOnClick();
	}
}

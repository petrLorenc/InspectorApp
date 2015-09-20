package cz.united121.android.revizori.listeners.helper;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/13/2015}
 **/
public class MyMultipleCameraChangeListener implements GoogleMap.OnCameraChangeListener {
	public static final String TAG = MyMultipleCameraChangeListener.class.getName();

	List<GoogleMap.OnCameraChangeListener> mListeners = new ArrayList<>();

	public void addListener(GoogleMap.OnCameraChangeListener cameraChangeListener){
		if(!mListeners.contains(cameraChangeListener)){
			mListeners.add(cameraChangeListener);
		}
	}

	public void removeListener(GoogleMap.OnCameraChangeListener onCameraChangeListener){
		mListeners.remove(onCameraChangeListener);
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		Log.d(TAG, "onCameraChange");
		for(GoogleMap.OnCameraChangeListener cameraChangeListener : mListeners){
			cameraChangeListener.onCameraChange(cameraPosition);
		}
	}
}

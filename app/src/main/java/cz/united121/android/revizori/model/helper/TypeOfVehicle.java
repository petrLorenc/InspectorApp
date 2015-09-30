package cz.united121.android.revizori.model.helper;

import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/30/2015}
 **/
public enum TypeOfVehicle {

	METRO("Metro", R.drawable.marker_metro_smaller),
	BUS("Bus", R.drawable.marker_metro_smaller),
	TRAM("Tram", R.drawable.marker_metro_smaller);

	private String name;
	private int markerImageResource;

	TypeOfVehicle(String name, int markerImageResource) {
		this.name = name;
		this.markerImageResource = markerImageResource;
	}

	public int getMarkerImageResource() {
		return markerImageResource;
	}

	@Override
	public String toString() {
		return name;
	}
}

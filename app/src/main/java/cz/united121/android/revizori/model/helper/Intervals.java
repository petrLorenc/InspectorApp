package cz.united121.android.revizori.model.helper;

/**
 * Intervals in minutes. Time to call alert for inspector
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/20/2015}
 **/
public enum Intervals {
	//in minutes
	SHORT("Kratky", 10f),
	MEDIUM("Stredni", 22.5f),
	LONG("Dlouhy", 35f);

	private String stringValue;
	private float mMinutes;

	private Intervals(String name, float minutes) {
		stringValue = name;
		this.mMinutes = minutes;
	}

	@Override
	public String toString() {
		return stringValue;
	}
}

package cz.united121.android.revizori.model.helper;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/20/2015}
 **/
public enum Distances {
	SHORT("Blizko", 0.5f),
	MEDIUM("Stredne", 1.0f),
	LONG("Daleko", 1.5f);

	private String stringValue;
	private float intValue;

	Distances(String name, float value) {
		stringValue = name;
		intValue = value;
	}

	@Override
	public String toString() {
		return stringValue;
	}
}

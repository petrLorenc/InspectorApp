package cz.united121.android.revizori.model;

import io.realm.RealmObject;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {10/5/2015}
 **/
public class MapPoint extends RealmObject {

	private String author;
	private double latitude;
	private double longitude;

	private boolean iscommentpresent;
	private String comment;

	private String tranporttype;

	public String getTranporttype() {
		return tranporttype;
	}

	public void setTranporttype(String tranporttype) {
		this.tranporttype = tranporttype;
	}

	public boolean iscommentpresent() {
		return iscommentpresent;
	}

	public void setIscommentpresent(boolean iscommentpresent) {
		this.iscommentpresent = iscommentpresent;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}

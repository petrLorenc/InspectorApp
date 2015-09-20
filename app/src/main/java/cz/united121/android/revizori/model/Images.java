package cz.united121.android.revizori.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {9/19/2015}
 **/
@ParseClassName("Images")
public class Images extends ParseObject {
	public static final String TAG = Images.class.getName();
	public static final String PARSE_TAG = "Images";

	public static final String USER = "User";
	public static final String IMAGE = "Image";

	public String getUrlImage() {
		return getParseFile(IMAGE).getUrl();
	}
}

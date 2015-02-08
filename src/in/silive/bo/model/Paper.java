package in.silive.bo.model;

import in.silive.bo.utilities.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class Paper {

	private JSONObject subjectJSON;

	public Paper(JSONObject obj) {
		this.subjectJSON = obj;
	}

	public String getTitle() throws JSONException {
		return subjectJSON.getString(Config.SUBJECT_TITLE);
	}

	public String getType() throws JSONException {
		return subjectJSON.getString(Config.SUBJECT_EXAM_CATEGORY) + " "
				+ subjectJSON.getString(Config.SUBJECT_PAPER_CATEGORY);
	}
	
	public String getUrl() throws JSONException {
		return subjectJSON.getString(Config.SUBJECT_URL);
	}
	
	public String getSize() throws JSONException {
		return subjectJSON.getString(Config.SUBJECT_SIZE);
	}
 }

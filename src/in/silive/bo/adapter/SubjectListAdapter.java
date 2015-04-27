package in.silive.bo.adapter;

import in.silive.bo.R;
import in.silive.bo.model.Paper;
import in.silive.bo.utilities.Config;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubjectListAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<Paper> paperList;
	private static final String TYPE_PUT = "PUT";
	private static final String TYPE_UT = "UT";
	private static final String TYPE_ST1 = "ST 1";
	private static final String TYPE_ST2 = "ST 2";
	private static final String TYPE_ST = "ST ";
	private static final String TYPE_SOLUTION = "SOLUTION";
	private static final String TYPE_PAPER = "QUESTION PAPER";
	private static final Pattern pattern = Pattern.compile("-?\\d+");

	public SubjectListAdapter(Activity activity, ArrayList<Paper> paperList) {
		this.mActivity = activity;
		this.paperList = paperList;
	}

	@Override
	public int getCount() {
		return paperList.size();
	}

	@Override
	public Object getItem(int position) {
		return paperList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity
					.getLayoutInflater();
			convertView = inflater.inflate(R.layout.rl_subject, parent, false);
		}
		Paper currentPaper = (Paper) getItem(position);

		ImageView pImgView = (ImageView) convertView
				.findViewById(R.id.paper_img);
		TextView pTitleTxtView = (TextView) convertView
				.findViewById(R.id.paper_title);
		TextView pSizeTxtView = (TextView) convertView
				.findViewById(R.id.paper_size);
		TextView pTypeTxtView = (TextView) convertView
				.findViewById(R.id.paper_type);
		try {
			String fullTitle = currentPaper.getTitle();
			String title = fullTitle.substring(0, fullTitle.length() - 4);
			String extension = fullTitle.substring(fullTitle.length() - 3);
			String type = currentPaper.getType().toUpperCase();
			int pDrawable = (extension.equalsIgnoreCase(Config.PAPER_TYPE_PDF)) ? R.drawable.pdf
					: R.drawable.word;
			String refinedType = "";

			if (type.indexOf(TYPE_ST1) != -1) {
				refinedType += TYPE_ST1;
			} else if (type.indexOf(TYPE_ST2) != -1) {
				refinedType += TYPE_ST2;
			} else if (type.indexOf(TYPE_ST) != -1) {
				refinedType += TYPE_ST;
			} else if (type.indexOf(TYPE_PUT) != -1) {
				refinedType += TYPE_PUT;
			} else {
				refinedType += TYPE_UT;
			}

			refinedType += " ";
			Matcher years = pattern.matcher(type);
			while (years.find()) {
				if (years.group().length() >= 4) {
					refinedType += years.group() + " ";
				} 
			}

			if (type.indexOf(TYPE_SOLUTION) != -1) {
				refinedType += TYPE_SOLUTION;
			} else {
				refinedType += TYPE_PAPER;
			}

			pImgView.setImageResource(pDrawable);
			pTitleTxtView.setText(title);
			pSizeTxtView.setText(currentPaper.getSize());
			pTypeTxtView.setText(refinedType);
			convertView.setTag(currentPaper.getUrl());
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

}

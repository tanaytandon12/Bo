package in.silive.bo.adapter;

import in.silive.bo.R;
import in.silive.bo.model.Paper;
import in.silive.bo.model.ViewHolder;
import in.silive.bo.utilities.Utilities;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import android.app.Activity;
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
		ViewHolder mViewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity
					.getLayoutInflater();
			convertView = inflater.inflate(R.layout.rl_subject, parent, false);
			mViewHolder = new ViewHolder();
			mViewHolder.pImgView = (ImageView) convertView
					.findViewById(R.id.paper_img);
			mViewHolder.pTitleTxtView = (TextView) convertView
					.findViewById(R.id.paper_title);
			mViewHolder.pSizeTxtView = (TextView) convertView
					.findViewById(R.id.paper_size);
			mViewHolder.pTypeTxtView = (TextView) convertView
					.findViewById(R.id.paper_type);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		Paper currentPaper = (Paper) getItem(position);

		try {
			String fullTitle = currentPaper.getTitle();
			int indexOfLastDot = fullTitle.lastIndexOf(".");
			String title = fullTitle.substring(0, indexOfLastDot);
			String extension = fullTitle.substring(indexOfLastDot + 1);
			String type = currentPaper.getType().toUpperCase(
					mActivity.getResources().getConfiguration().locale);
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

			mViewHolder.pImgView.setImageDrawable(Utilities.getIconDrawable(
					extension, mActivity));
			mViewHolder.pTitleTxtView.setText(title);
			mViewHolder.pSizeTxtView.setText(currentPaper.getSize());
			mViewHolder.pTypeTxtView.setText(refinedType);
			mViewHolder.data = currentPaper.getUrl();

		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return convertView;
	}

}

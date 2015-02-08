package in.silive.bo.adapter;

import in.silive.bo.R;
import in.silive.bo.model.Paper;
import in.silive.bo.utilities.Config;

import java.util.ArrayList;

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
			int pDrawable = (extension.equalsIgnoreCase(Config.PAPER_TYPE_PDF)) ? R.drawable.pdf
					: R.drawable.word;
			pImgView.setImageResource(pDrawable);
			pTitleTxtView.setText(title);
			pSizeTxtView.setText(currentPaper.getSize());
			pTypeTxtView.setText(currentPaper.getType());
			convertView.setTag(currentPaper.getUrl());
		} catch (JSONException ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

}

package in.silive.bo;

import in.silive.bo.adapter.SubjectListAdapter;
import in.silive.bo.listener.RequestListener;
import in.silive.bo.model.Paper;
import in.silive.bo.utilities.Config;
import in.silive.bo.utilities.Controller;
import in.silive.bo.utilities.MyUtilities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements RequestListener,
		OnClickListener {

	private ImageView searchImgView, siImgView;
	private AutoCompleteTextView searchTxtView;
	private ListView subjectListView;
	private ProgressBar mProgressBar;
	private Button retryBtn;
	private TextView noResultTxtView, noInternetTxtView;
	private View searchView;
	private SubjectListAdapter subListAdapter;
	private ArrayList<Paper> papers = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize the views
		searchImgView = (ImageView) findViewById(R.id.search_img);
		siImgView = (ImageView) findViewById(R.id.si_logo);
		searchTxtView = (AutoCompleteTextView) findViewById(R.id.search_txt);
		subjectListView = (ListView) findViewById(R.id.subject_list);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		retryBtn = (Button) findViewById(R.id.retry_btn);
		noResultTxtView = (TextView) findViewById(R.id.no_result_txt);
		noInternetTxtView = (TextView) findViewById(R.id.no_internet_txt);
		searchView = findViewById(R.id.search_container);

		searchImgView.setOnClickListener(this);
		siImgView.setOnClickListener(this);
		retryBtn.setOnClickListener(this);
		
		doInternetConnBasedAction();
		
		subListAdapter = new SubjectListAdapter(this, papers);
		subjectListView.setAdapter(subListAdapter);
		Controller.setRequestListener(this);
	}

	@Override
	public void preRequest() {
		hideListView();
		showProgressBar();
	}

	@Override
	public void postRequest(String result) {
		hideProgressBar();
		showListView();
		papers.clear();
			try {
			JSONArray jsonArr = new JSONArray(result);
			if (jsonArr.length() == 0) {
				showNoResultView();
				return;
			}
			Log.d("Response", "length : " + jsonArr.length());
			for (int i = 0; i < jsonArr.length(); i = i + 1) {
				papers.add(new Paper(jsonArr.getJSONObject(i)));
			}
			subListAdapter.notifyDataSetChanged();
		}catch(JSONException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.search_img :
			String queryStr = searchTxtView.getText().toString();
			if (queryStr.length() == 0) {
				Toast.makeText(this, "Please enter a valid query", Toast.LENGTH_SHORT).show();
			} else {
				hideNoResultView();
				showProgressBar();
				(new Controller()).execute(Config.PAPERS_URL + queryStr);
		
			}
			break;
		case R.id.retry_btn : 
			hideNoInternetView();
			doInternetConnBasedAction();
			break;
		case R.id.si_logo : 
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.SI_CONTACT_URL));
			startActivity(browserIntent);
			break;
		}
	}

	private void doInternetConnBasedAction() {
		if (!MyUtilities.isInternetConnected(this)) {
			showNoInternetView();
		} else {
			showSearchView();
		}
	}
	
	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
	}

	private void hideSearchView() {
		searchView.setVisibility(View.GONE);
	}

	private void showProgressBar() {
		mProgressBar.setVisibility(View.VISIBLE);
	}

	private void hideProgressBar() {
		mProgressBar.setVisibility(View.GONE);
	}

	private void showNoResultView() {
		noResultTxtView.setVisibility(View.VISIBLE);
	}

	private void hideNoResultView() {
		noResultTxtView.setVisibility(View.GONE);
	}

	private void showListView() {
		subjectListView.setVisibility(View.VISIBLE);
	}

	private void hideListView() {
		subjectListView.setVisibility(View.GONE);
	}

	private void showNoInternetView() {
		noInternetTxtView.setVisibility(View.VISIBLE);
		retryBtn.setVisibility(View.VISIBLE);
	}

	private void hideNoInternetView() {
		noInternetTxtView.setVisibility(View.GONE);
		retryBtn.setVisibility(View.GONE);
	}
}

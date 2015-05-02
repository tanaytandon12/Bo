package in.silive.bo;

import in.silive.bo.adapter.SubjectListAdapter;
import in.silive.bo.listener.RequestListener;
import in.silive.bo.model.Paper;
import in.silive.bo.model.ViewHolder;
import in.silive.bo.utilities.Config;
import in.silive.bo.utilities.Controller;
import in.silive.bo.utilities.Utilities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements RequestListener,
		OnClickListener, TextWatcher, OnItemClickListener {

	private ImageView crossImgView;
	private EditText searchEdtView;
	private ListView subjectListView;
	private ProgressBar mProgressBar;
	private Button retryBtn;
	private TextView noResultTxtView, noInternetTxtView;
	private View searchView;
	private InputMethodManager imm;
	private SubjectListAdapter subListAdapter;
	private ArrayList<Paper> papers = new ArrayList<>();
	private boolean mLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// initialize the views
		crossImgView = (ImageView) findViewById(R.id.cross_img);
		searchEdtView = (EditText) findViewById(R.id.search_txt);
		subjectListView = (ListView) findViewById(R.id.subject_list);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		retryBtn = (Button) findViewById(R.id.retry_btn);
		noResultTxtView = (TextView) findViewById(R.id.no_result_txt);
		noInternetTxtView = (TextView) findViewById(R.id.no_internet_txt);
		searchView = findViewById(R.id.search_container);

		searchEdtView.addTextChangedListener(this);

		crossImgView.setOnClickListener(this);
		retryBtn.setOnClickListener(this);

		// ENTER on Keyboard means SEARCH
		searchEdtView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					onClick(v);
					return true;
				default:
					break;
				}
				return false;
			}
		});

		imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

		doInternetConnBasedAction();

		subListAdapter = new SubjectListAdapter(this, papers);
		subjectListView.setAdapter(subListAdapter);
		subjectListView.setOnItemClickListener(this);
		Controller.setRequestListener(this);

		IntentFilter openFileIntent = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, openFileIntent);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(downloadReceiver);
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
			subjectListView.setSelectionAfterHeaderView();
		} catch (JSONException ex) {
			showNoInternetView();
			ex.printStackTrace();
		}
		mLoading = false;
		findViewById(R.id.search_container).requestFocus();
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.retry_btn:
			hideNoInternetView();
			doInternetConnBasedAction();
			break;
		case R.id.search_txt:
			String queryStr = searchEdtView.getText().toString()
					.replace(" ", "%20");
			if (queryStr.length() == 0) {
				Toast.makeText(this, "Please enter a valid query",
						Toast.LENGTH_SHORT).show();
			} else {
				hideNoResultView();
				showProgressBar();
				if (!mLoading) {
					(new Controller()).execute(Config.PAPERS_URL + queryStr);
					mLoading = true;
				}
			}
			break;
		case R.id.cross_img:
			searchEdtView.setText("");
			searchEdtView.requestFocus();
			mLoading = false;
			break;
		}
	}

	private void doInternetConnBasedAction() {
		if (!Utilities.isInternetConnected(this)) {
			showNoInternetView();
		} else {
			showSearchView();
		}
	}

	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0)
			crossImgView.setVisibility(View.VISIBLE);
		else
			crossImgView.setVisibility(View.GONE);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String data = ((ViewHolder) view.getTag()).data;
		String extension = data.substring(data.lastIndexOf(".") + 1);
		if (Utilities.canOpenFile(extension)) {
			(new ConfirmationDialog(view)).show(getSupportFragmentManager(),
					"Confirmation Dilaog");
		} else {
			(new AlertDialog.Builder(MainActivity.this)
			// set the title
					.setTitle("Warning")
					.setMessage(Html
							.fromHtml("The file you are atempting to download is a <b>"
									+ extension
									+ "</b> file, and it seems you do not have any application that will be able to open it. We suggest you install an application to open <b>"
									+ extension + "</b> files."))).show();
		}

	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("TAG", "Fuck yeah" + intent.getDataString());
		}
	};
}

package in.silive.bo;

import in.silive.bo.listener.RequestListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements RequestListener, OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void preRequest() {
		
	}

	@Override
	public void postRequest(String result) {
		
	}

	@Override
	public void onClick(View v) {
		
	}

}

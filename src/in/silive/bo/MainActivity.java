package in.silive.bo;

import in.silive.bo.listener.RequestListener;
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements RequestListener {

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

}

package in.silive.bo.utilities;

import in.silive.bo.listener.RequestListener;
import android.os.AsyncTask;

public class Controller extends AsyncTask<String, String, String> {

	private RequestListener mRequestListener;
	
	@Override
	protected void onPreExecute() {
		mRequestListener.preRequest();
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		mRequestListener.postRequest(result);
	}
	
	public void setRequestListener(RequestListener mListener) {
		this.mRequestListener = mListener;
	}
}

package in.silive.bo.utilities;

import in.silive.bo.listener.RequestListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class Controller extends AsyncTask<String, String, String> {

	private static RequestListener mRequestListener;

	@Override
	protected void onPreExecute() {
		mRequestListener.preRequest();
	}

	@Override
	protected String doInBackground(String... params) {
		String queryStr = params[0];
		String res = null;
		BufferedReader buffReader = null;
		HttpURLConnection httpUrlConn = null;
		String url = Config.BASE_URL + queryStr;
		try {
			URL httpURL = new URL(url);
			httpUrlConn = (HttpURLConnection) httpURL.openConnection();
			httpUrlConn.connect();
			httpUrlConn.setConnectTimeout(5000);
			InputStream ipStream = httpUrlConn.getInputStream();
			buffReader = new BufferedReader(new InputStreamReader(ipStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = buffReader.readLine()) != null)
				sb.append(line);
			res = sb.toString();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (buffReader != null) {
					buffReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			httpUrlConn.disconnect();
		}
		return res;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result == null)
			result = "[]";
		mRequestListener.postRequest(result);
	}

	public static void setRequestListener(RequestListener mListener) {
		mRequestListener = mListener;
	}
}

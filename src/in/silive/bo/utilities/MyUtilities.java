package in.silive.bo.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyUtilities {

	public static boolean isInternetConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = connManager.getAllNetworkInfo();
		for(NetworkInfo ni : netInfo) {
			if (ni.isConnected())
				return true;
		}
		return false;
	}
}

package in.silive.bo.utilities;

import in.silive.bo.R;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class Utilities {

	private static HashMap<String, Drawable> mExt2IconMap = new HashMap<>();

	public static boolean isInternetConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = connManager.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.isConnected())
				return true;
		}
		return false;
	}

	public static Drawable getIconDrawable(String extension, Context context) {
		if (mExt2IconMap.containsKey(extension)) {
			return mExt2IconMap.get(extension);
		}
		File file = new File(Environment.DIRECTORY_DOWNLOADS, "TestFile."
				+ extension);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
		intent.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				extension));
		List<ResolveInfo> launchables = context.getPackageManager()
				.queryIntentActivities(intent, 0);
		if (launchables.size() >= 1) {
			Log.d("TAG", "number of launchables : " + launchables.size());
			for (int i = 0; i < launchables.size(); i++) {
				Log.d("TAG",
						launchables.get(i)
								.loadLabel(context.getPackageManager())
								.toString());
			}
			mExt2IconMap.put(extension,
					launchables.get(0).loadIcon(context.getPackageManager()));
			return mExt2IconMap.get(extension);
		} else {
			return context.getResources().getDrawable(R.drawable.question_mark);
		}
	}

	public static boolean canOpenFile(String extension) {
		return mExt2IconMap.containsKey(extension);
	}

}

package in.silive.bo;

import in.silive.bo.model.ViewHolder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

public class ConfirmationDialog extends DialogFragment {

	private View view;

	public ConfirmationDialog(View view) {
		super();
		this.view = view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = "Do you want to download "
				+ ((TextView) view.findViewById(R.id.paper_title)).getText()
				+ " "
				+ ((TextView) view.findViewById(R.id.paper_type)).getText()
				+ "?";
		return new AlertDialog.Builder(getActivity())
		// Set Dialog Title
				.setTitle("Are you sure?")
				// Set Dialog Message
				.setMessage(message)
				// Positive button
				.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								
								String mPaperUrl = ((ViewHolder) view.getTag()).data;
								String downloadUrl = mPaperUrl.replace(" ",
										"%20");
								String documentName = mPaperUrl
										.substring(mPaperUrl.lastIndexOf("/") + 1);
								
								DownloadManager.Request dmRequest = new DownloadManager.Request(
										Uri.parse(downloadUrl));
								
								dmRequest.setTitle(((TextView) view
										.findViewById(R.id.paper_title))
										.getText());
								dmRequest.setDescription(((TextView) view
										.findViewById(R.id.paper_title))
										.getText()
										+ " "
										+ ((TextView) view
												.findViewById(R.id.paper_type))
												.getText()
										+ " is being downloaded");
								dmRequest.setDestinationInExternalPublicDir(
										Environment.DIRECTORY_DOWNLOADS,
										documentName);
								dmRequest.allowScanningByMediaScanner();
								dmRequest
										.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
								
								DownloadManager manager = (DownloadManager) getActivity()
										.getSystemService(
												Context.DOWNLOAD_SERVICE);

								manager.enqueue(dmRequest);
							}
						})

				// Negative Button
				.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Do something else
					}
				}).create();
	}

}

package in.silive.bo;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ListPopupWindow;

/**
 * Created by Kartikay on 27-Apr-15.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import in.silive.bo.R;
public class DownloadFile extends DialogFragment {
    private View view;
    private Context ctxt;
    static NotificationCompat.Builder builder;
    public static NotificationManager notificationManager;
    static ProgressBar pBar;
    public DownloadFile(View view) {
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
        Log.d("CD", view.getTag().toString());
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
                                String tag = view.getTag().toString();
                                String downloadUrl = tag.replace(" ", "%20");
                                new DownloadFileTask().execute(downloadUrl);
                            }
                        })
                        // Negative Button
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                }).create();
    }
    public class DownloadFileTask extends AsyncTask<String,String,String>{
        ProgressDialog pDialog=new ProgressDialog(getActivity());
   NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
        @Override
        protected void onPreExecute() {
            mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getActivity());
            mBuilder.setContentTitle("Download")
                    .setContentText("Download in progress")
                    .setSmallIcon(R.drawable.bytepad);
pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... aurl) {
            int count;
            try{
                URL url = new URL(aurl[0]);
                String path = url.getPath();
                String filename = new File(path).getName();
                Environment.getExternalStorageDirectory();
                File tm = new File("/sdcard/Transform Mii/");
                tm.mkdirs();
                File out = new File(tm, filename);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion   .getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(out);
                byte data[] = new byte[4194208];
                long total = 0;
                Log.d("Input Stream","Input"+input.toString());
                while ((count = input.read(data)) != -1) {
                    total += count;
            Log.d("Total",""+total);
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {}
            return null;
        }
        protected void onProgressUpdate(String... progress) {
            /*pBar.setProgress(R.id.pBar, 100, , false);
            */
        //    pBar.setProgress(Integer.parseInt(progress[0]));
         //   notificationManager.notify(0,builder.build());
         /*
pBar.setProgress(Integer.parseInt(progress[0]));
         */
            mBuilder.setProgress(100,Integer.parseInt(progress[0]),false);
            mNotifyManager.notify(0, mBuilder.build());
            this.pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
        pDialog.dismiss();
            mBuilder.setContentText("Download complete");
            // Removes the progress bar
            mBuilder.setProgress(0, 0, false);
    /*        Intent notificationIntent = new Intent( getActivity(),DownloadManagerBroadcastReceiver.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0,
                    notificationIntent, 0);
            mBuilder.setContentIntent(contentIntent);
    */        mNotifyManager.notify(0, mBuilder.build());
            super.onPostExecute(s);
        }
    }
}
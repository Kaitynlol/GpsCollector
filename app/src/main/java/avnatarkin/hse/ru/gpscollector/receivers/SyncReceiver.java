package avnatarkin.hse.ru.gpscollector.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import avnatarkin.hse.ru.gpscollector.R;
import avnatarkin.hse.ru.gpscollector.constants.Constants;


public class SyncReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public SyncReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
        new HttpAsyncTask(context,1).execute();
    }
    public class HttpAsyncTask extends
            AsyncTask<Void, Void, Boolean> {

        String urlString = Constants.URL;

        private final String TAG = "post json example";
        private Context context;

        public HttpAsyncTask(Context context, int advertisementId) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "1 - RequestVoteTask is about to start...");

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean status = false;

            String response = "";
            Log.e(TAG, "2 - pre Request to response...");

            try {
                response = performPostCall(urlString, new HashMap<String, String>() {

                    private static final long serialVersionUID = 1L;

                    {
                        put("Accept", "application/json");
                        put("Content-Type", "application/json");
                    }
                });
                Log.e(TAG, "3 - give Response...");
                Log.e(TAG, "4 " + response.toString());
            } catch (Exception e) {
                // displayLoding(false);

                Log.e(TAG, "Error ...");
            }
            Log.e(TAG, "5 - after Response...");

            if (!response.equalsIgnoreCase("")) {
                try {
                    Log.e(TAG, "6 - response !empty...");
                    //
                    JSONObject jRoot = new JSONObject(response);
                    JSONObject d = jRoot.getJSONObject("d");

                    int ResultType = d.getInt("ResultType");
                    Log.e("ResultType", ResultType + "");

                    if (ResultType == 1) {

                        status = true;

                    }

                } catch (JSONException e) {
                    // displayLoding(false);
                    // e.printStackTrace();
                    Log.e(TAG, "Error " + e.getMessage());
                } finally {

                }
            } else {
                Log.e(TAG, "6 - response is empty...");

                status = false;
            }

            return status;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            //
            Log.e(TAG, "7 - onPostExecute ...");

            if (result) {
                Log.e(TAG, "8 - Update UI ...");

                // setUpdateUI(adv);
            } else {
                Log.e(TAG, "8 - Finish ...");

                // displayLoding(false);
                // finish();
            }

        }

        public String performPostCall(String requestURL,
                                      HashMap<String, String> postDataParams) {

            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(context.getResources().getInteger(
                        R.integer.maximum_timeout_to_server));
                conn.setConnectTimeout(context.getResources().getInteger(
                        R.integer.maximum_timeout_to_server));
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");

                Log.e(TAG, "11 - url : " + requestURL);

            /*
             * JSON
             */

                JSONObject root = new JSONObject();
                root.put("id",66);
                root.put("name","android");
                root.put("createdDate",148121643);
                //
                // String token = Static.getPrefsToken(context);

                // root.put("securityInfo", Static.getSecurityInfo(context));
                // root.put("advertisementId", advertisementId);

                Log.e(TAG, "12 - root : " + root.toString());

                String str = root.toString();
                byte[] outputBytes = str.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputBytes);

                int responseCode = conn.getResponseCode();

                Log.e(TAG, "13 - responseCode : " + responseCode);

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.e(TAG, "14 - HTTP_OK");

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    Log.e(TAG, "14 - False - HTTP_OK");
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}

package thack.ac.tabledash;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BaseActivity extends Activity {
    private Activity self = this;
    public final String TAG = ((Object) this).getClass().getSimpleName();

    public NfcAdapter mNfcAdapter;
    public BroadcastReceiver receiver;
    public IntentFilter filter;

    //Service Handler
    ServiceHandler sh = new ServiceHandler();

    Handler handler=new Handler();
    Runnable r;
    Runnable r2;

//    Android ID
    private String android_id;
    String clientToken2 = "eyJ2ZXJzaW9uIjoxLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI2ZjBjMGQ4YmU1NjBlNjhiMjAwYjI4MGMxZTBjNDU0NTk0ZGU4ZjQ3MWVhOTBjZjczNTRmMzc3NmU0ZDFhYjY0fGNyZWF0ZWRfYXQ9MjAxNC0wOS0xNFQwMDoyMzoyMi45MDQ3NTE5NTArMDAwMFx1MDAyNm1lcmNoYW50X2lkPWRjcHNweTJicndkanIzcW5cdTAwMjZwdWJsaWNfa2V5PTl3d3J6cWszdnIzdDRuYzgiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvZGNwc3B5MmJyd2RqcjNxbi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbImN2diJdLCJwYXltZW50QXBwcyI6W10sImNsaWVudEFwaVVybCI6Imh0dHBzOi8vYXBpLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb206NDQzL21lcmNoYW50cy9kY3BzcHkyYnJ3ZGpyM3FuL2NsaWVudF9hcGkiLCJhc3NldHNVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImF1dGhVcmwiOiJodHRwczovL2F1dGgudmVubW8uc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFuYWx5dGljcyI6eyJ1cmwiOiJodHRwczovL2NsaWVudC1hbmFseXRpY3Muc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbSJ9LCJ0aHJlZURTZWN1cmVFbmFibGVkIjpmYWxzZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJtZXJjaGFudEFjY291bnRJZCI6IjV6dzVtbSIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9fQ==";
    String clientToken = "MIIBCgKCAQEAyrJIFzpFzGHClLRcnEkMJOOhD9ReWBxFp46AfUc+iIhvrayotkayf575H4Z76AZvvesKoAApIdk9mczVigCEMGaw275yFQ/L33BjygVpOZed02kSdpv4ADYygwXesO70V7p37zmzb7hzIaXi/CYLCv5Rnmg9G4c7pHH+jvDgLiyF5Qn7BAhbzI8DtZjuoa9CL6MbIaF3MNdzy3z7midBLli/Bs1xqSpQgKX5pYZtDJlI/fYe+utw8RwBf4jApy6a7a/wNe9dEIvlk0fQoOYvNtwfgjhAxnttv0ChByGyQBbiy6xwpveN553udcgzcL/o0rt4VCryvjD2cY2e5+HCLQIDAQAB";
    int REQUEST_CODE = 100;

    /**
     * Urls for server communications
     */
//    static String PREFIX_URL = "http://tabledash.ml/";
    static String PREFIX_URL = "http://192.168.50.224/tabledashserver/";
    static String CHECK_IN_URL = "checkin.php";
    static String CHECK_OUT_URL = "checkout.php";
    static String CHECK_VAC_URL = "check_vacancy.php";

//    Threshold
    public final double PERCENTAGE = 0.8;

//    Pref storage and variable to track if the user's estimated ending time && table tag ID
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public Date notification_time;
    public Date ending_time;
    public String current_table_ID;
    public final String TAG_ENDING_TIME = "TAG_ENDING_TIME";
    public final String TAG_NOTIFICATION_TIME = "TAG_NOTIFICATION_TIME";
    public final String TAG_TABLE_ID = "TAG_TABLE_ID";


//    Button for checkout if present
    public View checkOutButton;
    public TextView mStatusView;
    public AnimationDrawable tapAnimation;

//    String for JSON
    public String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

//        Get Android ID
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_base);
//        Check if user is still eating
//        Open pref storage
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        Check the pref storage ending time to see if still eating
        if(preferences.contains(TAG_ENDING_TIME)){
            String date_string = preferences.getString(TAG_ENDING_TIME, null);
            String notification_string = preferences.getString(TAG_NOTIFICATION_TIME, null);
            if(date_string!=null){
                ending_time = Helper.parseDateFromString(date_string);
                notification_time = Helper.parseDateFromString(notification_string);
                current_table_ID = preferences.getString(TAG_TABLE_ID, null);
                Log.e(TAG, "end: " + ending_time.getTime() + "noti: " + notification_time.getTime() + "eating? " + isEating());
                Log.e(TAG, "now: " + new Date().getTime());

            }
        }
        if(isEating()){
            //        Log out user after time passed in case the activity does not gets closed
            final int delay = (int)(ending_time.getTime() - new Date().getTime());
            final int notification_delay = (int)(notification_time.getTime() - Calendar.getInstance().getTime().getTime());
            Log.e(TAG, "delay: " + delay + "noti delay: " + notification_delay);
            r = new Runnable() {
                public void run() {
                    Log.e(TAG, "Runnable 1!");
                    checkOut(false);
                }
            };
            r2 = new Runnable() {
                public void run() {
                    Log.e(TAG, "Runnable 2!");
                    notifyEndingSoon();
                }
            };
            handler.postDelayed(r, delay);
            handler.postDelayed(r2, notification_delay);
        }
    }

    protected String handleIntent(Intent intent) {
        String serialId = null;
        String action = intent.getAction();
//        Log.e(TAG, "in handleIntent, action: " + action);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String tech_mfc = MifareClassic.class.getName();
            String tech_NfcA = NfcA.class.getName();
            String tech_IsoDep = IsoDep.class.getName();
            String tech_NfcB = NfcB.class.getName();
            String tech_Ndef = Ndef.class.getName();
//            Log.d(TAG, "tags: " + tag);
            // Get the ID directly
            try {
                byte[] tagId = tag.getId();
                serialId = Helper.getHexString(tagId, tagId.length);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            }
        }
//        Log.d(TAG, "Direct Read - Serial Number: " + serialId);
        return serialId;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        final String tag_ID = handleIntent(intent);
//        Log.d(TAG, "ID:" + tag_ID);
        if(tag_ID != null){
            Log.e(TAG, "Time: " + ending_time + " current TAG ID:" + current_table_ID);

//            Toast.makeText(this, "Tag ID: " + tag_ID, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(self);

            LayoutInflater inflater;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            Use different durations for new check in at new places and renewed check in
            LinearLayout ll;
            String buttonConfirmText = "Check in!";
            String buttonCancelText = "Cancel";
            if(isEating() && tag_ID.equals(current_table_ID)){
                if(almostEnd()){
                    buttonConfirmText = "Extend";
                    buttonCancelText = "No need";
                    alert.setTitle(getResources().getString(R.string.dialog_title_re_check_in));
                    ll = (LinearLayout) inflater.inflate(R.layout.re_check_in_dialog ,null);
                    alert.setView(ll);
                    alert.setMessage(R.string.dialog_message_re_check_in);
                }else{
                    buttonConfirmText = "Modify";
                    buttonCancelText = "Cancel";
                    alert.setTitle(getResources().getString(R.string.dialog_title_modify));
                    ll = (LinearLayout) inflater.inflate(R.layout.check_in_dialog ,null);
                    alert.setView(ll);
                    alert.setMessage(R.string.dialog_message_modify);
                }

            }else{
                alert.setTitle(getResources().getString(R.string.dialog_title_check_in));
                ll = (LinearLayout) inflater.inflate(R.layout.check_in_dialog ,null);
                alert.setView(ll);
                alert.setMessage(R.string.dialog_message_check_in);
            }

            final RadioGroup rg1=(RadioGroup)ll.findViewById(R.id.durations_rg);

            alert.setPositiveButton(buttonConfirmText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if(rg1.getCheckedRadioButtonId()!=-1){
                        String selection = getSelectionFromRadioGroup(rg1);
                        int minutes = Integer.parseInt(selection);
                        //Add nameValuePair for http request
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        addToNameValuePairs(selection, nameValuePairs, tag_ID);
//                        Update the variables
                        int notification_seconds = (int)(minutes*60*PERCENTAGE);
                        int total_seconds = minutes*60;
                        updateNotificationAndEndingTime(notification_seconds, total_seconds);
                        updateTagID(tag_ID);
                        notifyEating();
//                        Set up scheduled notification
                        createScheduledNotification(minutes);
//                        Log out user after time passed in case the activity does not gets closed
                        final int delay = 1000 * total_seconds;
                        final int notification_delay = 1000 * notification_seconds;
                        Log.e(TAG, "delay: " + delay + "noti delay: " + notification_delay);
                        r = new Runnable() {
                            public void run() {
                                Log.e(TAG, "Runnable 22!");
                                checkOut(false);
                            }
                        };
                        r2 = new Runnable() {
                            public void run() {
                                Log.e(TAG, "Runnable 11!");
                                notifyEndingSoon();
                            }
                        };
                        handler.postDelayed(r, delay);
                        handler.postDelayed(r2, notification_delay);
                        Log.e(TAG, "Time: " + ending_time + " current TAG ID:" + current_table_ID + "after new check in");
                        new checkInAsync().execute(nameValuePairs);
                    }
                }
            });

            alert.setNegativeButton(buttonCancelText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }
    }

    public void checkOut(Boolean confirmation){
        if(confirmation){
            AlertDialog.Builder alert = new AlertDialog.Builder(self);
            alert.setTitle(getResources().getString(R.string.dialog_title_check_out));
            alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Add nameValuePair for http request
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    addToNameValuePairs(nameValuePairs);
                    notifyCheckedOut(self);
                    new checkOutAsync().execute(nameValuePairs);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }else{
            Log.e(TAG, "checkout before Async: ");
            //Add nameValuePair for http request
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            addToNameValuePairs(nameValuePairs);
            notifyCheckedOut(self);
            new checkOutAsync().execute(nameValuePairs);
        }

    }

    /**
     * NameValuePair for Check in
     * @param selection         Selected duration
     * @param nameValuePairs    NameValuePair
     * @param tag_ID            tag_ID
     */
    private void addToNameValuePairs(String selection, List<NameValuePair> nameValuePairs, String tag_ID) {
        String param_name;
        String param_value;
        param_name= "user_id";
        param_value= android_id;
        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
        param_name= "tag_id";
        param_value= tag_ID;
        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
        param_name= "duration";
        param_value= selection;
        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
    }

    /**
     * NameValuePair for Check out
     * @param nameValuePairs    NameValuePair
     */
    private void addToNameValuePairs(List<NameValuePair> nameValuePairs) {
        String param_name;
        String param_value;
        param_name= "user_id";
        param_value= android_id;
        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
    }

    /**
     * NameValuePair for Check out
     * @param nameValuePairs    NameValuePair
     */
    public void addToNameValuePairsCheck(List<NameValuePair> nameValuePairs, String canteen) {
        String param_name;
        String param_value;
        param_name= "canteen_id";
        param_value= canteen;
        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
    }

    public String getSelectionFromRadioGroup(RadioGroup rg1) {
        int id= rg1.getCheckedRadioButtonId();
        View radioButton = rg1.findViewById(id);
        int radioId = rg1.indexOfChild(radioButton);
        RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
        String selection = (String) btn.getText();
//        Toast.makeText(self, "You selected " + selection, Toast.LENGTH_SHORT).show();
        return selection;
    }

    /**
     * AsyncTask for checking in
     */
    private class checkInAsync extends AsyncTask<List<NameValuePair>, Void, Void>{
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(self);
            this.dialog.setMessage("Checking in");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(List<NameValuePair>... lists) {
            List<NameValuePair> nameValuePairs = lists[0];
            // Creating service handler class instance
            sh = new ServiceHandler();
            String json = sh.makeServiceCall(PREFIX_URL + CHECK_IN_URL, ServiceHandler.POST, nameValuePairs);
            Log.e(TAG, "Response: " + json);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
//            Make the check out button visible
            checkOutButton = findViewById(R.id.check_out_button);
            if(checkOutButton != null){
                checkOutButton.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * AsyncTask for checking out
     */
    private class checkOutAsync extends AsyncTask<List<NameValuePair>, Void, Void>{
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(self);
            this.dialog.setMessage("Checking out...");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(List<NameValuePair>... lists) {
            List<NameValuePair> nameValuePairs = lists[0];
            // Creating service handler class instance
            sh = new ServiceHandler();
            String json = sh.makeServiceCall(PREFIX_URL + CHECK_OUT_URL, ServiceHandler.POST, nameValuePairs);
            Log.e(TAG, "Response: " + json);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
//            Make the check out button invisible
            checkOutButton = findViewById(R.id.check_out_button);
            if(checkOutButton != null){
                checkOutButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * AsyncTask for checking vacancy
     */
    public class checkVacancyAsync extends AsyncTask<List<NameValuePair>, Void, String>{
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(self);
            this.dialog.setMessage("Checking vacancy...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(List<NameValuePair>... lists) {
            List<NameValuePair> nameValuePairs = lists[0];
            // Creating service handler class instance
            sh = new ServiceHandler();
            json = sh.makeServiceCall(PREFIX_URL + CHECK_VAC_URL, ServiceHandler.POST, nameValuePairs);
            Log.e(TAG, "JSON -2 : " + json);


            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.e(TAG, "JSON -1 : " + json);
            Intent newintent = new Intent(self, StatusActivity.class);
            newintent.putExtra("json", json);
            startActivity(newintent);
        }
    }


    private void createScheduledNotification(int minutes)
    {
//        Schedule a notification at 80% time
        int scheduled_seconds = (int)(minutes * 60 * PERCENTAGE);
        // Get new calendar object and set the date to now
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Add defined amount of minutes to the date
        calendar.add(Calendar.SECOND, scheduled_seconds);

        // Retrieve alarm manager from the system
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getBaseContext().ALARM_SERVICE);
        // Every scheduled intent needs a different ID, else it is just executed once
        int id = (int) System.currentTimeMillis();

        // Prepare the intent which should be launched at the date
        Intent intent = new Intent(this, TimeAlarm.class);

        // Prepare the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void updateNotificationAndEndingTime(int notification_seconds, int total_seconds) {
        int remaining_seconds = total_seconds - notification_seconds;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, notification_seconds);
        notification_time = cal.getTime();
        cal.add(Calendar.SECOND, remaining_seconds);
        ending_time = cal.getTime();
        editor = preferences.edit();
        editor.putString(TAG_ENDING_TIME, Helper.parseDateToString(ending_time));
        editor.putString(TAG_NOTIFICATION_TIME, Helper.parseDateToString(notification_time));
        editor.commit();
    }



    private void updateTagID(String tag_ID) {
        current_table_ID = tag_ID;
        editor = preferences.edit();
        editor.putString(TAG_TABLE_ID, tag_ID);
        editor.commit();
    }

    private void clearPref() {
        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public Boolean isEating(){
        if(ending_time == null){
            return false;
        }
        return !Helper.checkIfFinished(ending_time);
    }

    public Boolean almostEnd(){
        if(ending_time == null || !isEating()){
            return false;
        }
        return Helper.checkIfAlmostEnd(notification_time);
    }

    public Boolean alreadyFinished(){
        if(ending_time == null){
            return false;
        }
        return Helper.checkIfFinished(ending_time);
    }

    public void notifyEndingSoon() {
        Log.e(TAG, "status: " + mStatusView);
        if(mStatusView != null){
            mStatusView.setText("Haven't finished yet? Tap again to extend.");
        }else{
            Toast.makeText(this, "Haven't finished yet? Tap again to extend.", Toast.LENGTH_LONG).show();
        }
    }

    public void notifyEating() {
        if(mStatusView != null) {
            mStatusView.setText("Enjoy your food!");
        }
        // Finds the image view, starts the animation
        ImageView scanImage = (ImageView) findViewById(R.id.iv_main_image);
        if(scanImage != null){
            tapAnimation = (AnimationDrawable) scanImage.getBackground();
            tapAnimation.stop();
            scanImage.setBackgroundResource(R.drawable.enjoy_your_meal);
        }
    }

    /**
     * @param activity The corresponding {@link MainActivity} requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    protected static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    protected static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{
                new String[]{Ndef.class.getName()},
                new String[]{MifareClassic.class.getName(), NfcA.class.getName(), NdefFormatable.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{IsoDep.class.getName()}
        };

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
//        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
//        try {
//            filters[0].addDataType(MIME_TEXT_PLAIN);
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            throw new RuntimeException("Check your mime type.");
//        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
        if(alreadyFinished()){
            notifyCheckedOut(self);
        }

    }

    private void notifyCheckedOut(Activity activity) {
        Toast.makeText(this, "You have been checked out.", Toast.LENGTH_LONG).show();
        clearPref();
        activity.recreate();
        // Finds the image view, starts the animation
        ImageView scanImage = (ImageView) findViewById(R.id.iv_main_image);
        if(scanImage!=null){
            scanImage.setBackgroundResource(R.drawable.animation);
            tapAnimation = (AnimationDrawable) scanImage.getBackground();
            tapAnimation.start();
        }
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Remove previous handlers
        Log.e(TAG, "onDestroy");
        handler.removeCallbacks(r);
        handler.removeCallbacks(r2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch(item.getItemId())
        {
            case R.id.action_donate:
                onBraintreeSubmit(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBraintreeSubmit(Context context) {
        Intent intent = new Intent(context, BraintreePaymentActivity.class);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken2);
        Customization customization = new Customization.CustomizationBuilder()
                .primaryDescription("Donate")
                .submitButtonText("Donate")
                .build();
        intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);

        // REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == BraintreePaymentActivity.RESULT_OK) {
            String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
            postNonceToServer(paymentMethodNonce);
        }
    }

    void postNonceToServer(String nonce) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("nonce", nonce);
        client.post(PREFIX_URL + "payment-method-nonce", params,
                new AsyncHttpResponseHandler() {
                    // Your implementation here
                });
    }
}

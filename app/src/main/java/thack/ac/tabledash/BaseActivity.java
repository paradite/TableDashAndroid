package thack.ac.tabledash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BaseActivity extends Activity {
    private Activity self = this;
    public final String TAG = ((Object) this).getClass().getSimpleName();

    public NfcAdapter mNfcAdapter;
    public BroadcastReceiver receiver;
    public IntentFilter filter;

    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    //Service Handler
    ServiceHandler sh = new ServiceHandler();

    /**
     * Urls for server communications
     */
    static String CHECK_IN_URL = "http://tabledash.ml/checkin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_base);
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
            Log.d(TAG, "tags: " + tag);
            // Get the ID directly
            try {
                byte[] tagId = tag.getId();
                serialId = Helper.getHexString(tagId, tagId.length);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            }
        }
        Log.d(TAG, "Direct Read - Serial Number: " + serialId);
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
        String tag_ID = handleIntent(intent);
        Log.d(TAG, "ID:" + tag_ID);
        if(tag_ID != null){
            Toast.makeText(this, "Tag ID: " + tag_ID, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alert = new AlertDialog.Builder(self);
            alert.setTitle(getResources().getString(R.string.dialog_title_check_in));

            LayoutInflater inflater;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.check_in_dialog ,null);

            alert.setView(ll);
            final RadioGroup rg1=(RadioGroup)ll.findViewById(R.id.durations_rg);

            alert.setPositiveButton("Check in!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//                Log.d(TAG, "activity: " + activity.getClass());
//                Log.d(TAG, "rg: " + rg1);
                    if(rg1.getCheckedRadioButtonId()!=-1){
                        int id= rg1.getCheckedRadioButtonId();
                        View radioButton = rg1.findViewById(id);
                        int radioId = rg1.indexOfChild(radioButton);
                        RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
                        String selection = (String) btn.getText();
                        Toast.makeText(self, "You selected " + selection + " minutes.", Toast.LENGTH_SHORT).show();

                        //Add nameValuePair for http request
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        String param_name;
                        String param_value;

                        param_name= "user_id";
                        param_value= "zhuliang";
                        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
                        param_name= "tag_id";
                        param_value= "tag_ID";
                        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));
                        param_name= "duration";
                        param_value= selection;
                        nameValuePairs.add(new BasicNameValuePair(param_name, param_value));


                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }
    }

    protected class SentCount extends AsyncTask<List<NameValuePair>, Void, Void>{

        @Override
        protected Void doInBackground(List<NameValuePair>... lists) {
            // Creating service handler class instance
            sh = new ServiceHandler();
            String json = sh.makeServiceCall(CHECK_IN_URL, ServiceHandler.GET, nameValuePairs);
            return null;
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
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package thack.ac.tabledash;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Arrays;


public class BaseActivity extends Activity {
    private Activity self = this;
    public final String TAG = ((Object) this).getClass().getSimpleName();

    public NfcAdapter mNfcAdapter;
    public BroadcastReceiver receiver;
    public IntentFilter filter;

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
                Log.d(TAG, "Direct Read - Serial Number: " + serialId);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                serialId = "ERROR";
            }
        }
        return serialId;
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    private static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
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

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        registerReceiver(receiver, filter);
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);
        unregisterReceiver(receiver);
        super.onPause();
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
        handleIntent(intent);
    }

    /**
     * @param activity The corresponding {@link MainActivity} requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    private static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
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

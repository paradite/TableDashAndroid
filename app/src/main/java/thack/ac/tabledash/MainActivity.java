package thack.ac.tabledash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusView = (TextView) findViewById(R.id.tv_main_checkIn);
        checkOutButton = findViewById(R.id.check_out_button);

        // Finds the image view, starts the animation
        ImageView scanImage = (ImageView) findViewById(R.id.iv_main_image);
        scanImage.setBackgroundResource(R.drawable.animation);
        tapAnimation = (AnimationDrawable) scanImage.getBackground();
        tapAnimation.start();

        //Set the default texts
        if (!mNfcAdapter.isEnabled()) {
            mStatusView.setText("NFC is disabled. Please enable it in the settings to check in.");
        }


        if(isEating()){
//                    Still eating, make the check out button visible and record the tag ID
            checkOutButton.setVisibility(View.VISIBLE);
        }

        Log.e(TAG, "Time: " + ending_time + " current TAG ID:" + current_table_ID);


        // Implement onClickListeners for clickable views
        findViewById(R.id.tv_main_checkStatus).setOnClickListener(this);


        // Handle changes in NFC state
        filter = new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                            NfcAdapter.STATE_OFF);
                    switch (state) {
                        case NfcAdapter.STATE_OFF:
                            Toast.makeText(MainActivity.this, "NFC disabled.", Toast.LENGTH_SHORT).show();
                            mStatusView.setText("NFC is disabled. Please enable it in the settings.");
                            break;
                        case NfcAdapter.STATE_TURNING_OFF:
//                            Toast.makeText(MainActivity.this, "NFC enabled.", Toast.LENGTH_SHORT).show();
                            break;
                        case NfcAdapter.STATE_ON:
                            Toast.makeText(MainActivity.this, "NFC enabled.", Toast.LENGTH_SHORT).show();
                            mStatusView.setText(R.string.tap_card);
                            break;
                        case NfcAdapter.STATE_TURNING_ON:
//                            Toast.makeText(MainActivity.this, "NFC enabled.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }
        };
        registerReceiver(receiver, filter);

        // Handling of intent
        onNewIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            // Handle onClick for check status button
            case R.id.tv_main_checkStatus:
                Toast.makeText(getApplicationContext(), "Going to check canteen status..", Toast.LENGTH_SHORT).show();

                // Launch main activity
                startActivity(new Intent(this, PickerActivity.class));
                break;
            // Handle onClick for check out
            case R.id.check_out_button:
                checkOut(true);
                break;
        }
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
        if(isEating()){
            notifyEating();
        }
        if(isEating() && almostEnd() && current_table_ID!= null && !current_table_ID.equals("")){
            notifyEndingSoon();
        }

        // Starts animation
        tapAnimation.start();
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);
        unregisterReceiver(receiver);
        super.onPause();

        // Stops animation
        tapAnimation.stop();
    }
}

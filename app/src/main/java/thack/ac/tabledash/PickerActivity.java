package thack.ac.tabledash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class PickerActivity extends BaseActivity implements OnClickListener {

    String canteenID;
    RadioGroup rg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        rg1=(RadioGroup)findViewById(R.id.radioGroup_Location);
        // Implement onClickListeners for clickable views
        findViewById(R.id.radio_btn_Canteen_A).setOnClickListener(this);
        findViewById(R.id.radio_btn_Canteen_B).setOnClickListener(this);
        findViewById(R.id.radio_btn_Canteen_C).setOnClickListener(this);

        findViewById(R.id.btn_picker_check_status).setOnClickListener(this);
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
        if (id == R.id.action_donate) {
            onBraintreeSubmit(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.radio_btn_Canteen_A:
//                Toast.makeText(getApplicationContext(), "Canteen A", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_btn_Canteen_B:
//                Toast.makeText(getApplicationContext(), "Canteen B", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_btn_Canteen_C:
//                Toast.makeText(getApplicationContext(), "Canteen C", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_picker_check_status:
                if(rg1.getCheckedRadioButtonId()!=-1){
                    String selection = getSelectionFromRadioGroup(rg1);
                    selection = selection.toLowerCase().replace(" ", "_");
                    Log.e(TAG, "Table selected: " + selection);
                    //Add nameValuePair for http request
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    addToNameValuePairsCheck(nameValuePairs, selection);
                    new checkVacancyAsync().execute(nameValuePairs);
                }

        }
    }
}

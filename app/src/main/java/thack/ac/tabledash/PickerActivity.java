package thack.ac.tabledash;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class PickerActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        // Implement onClickListeners for clickable views
        findViewById(R.id.radio_btn_Canteen_A).setOnClickListener(this);
        findViewById(R.id.radio_btn_Canteen_B).setOnClickListener(this);
        findViewById(R.id.radio_btn_Canteen_C).setOnClickListener(this);
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
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.radio_btn_Canteen_A:
                Toast.makeText(getApplicationContext(), "Canteen A", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_btn_Canteen_B:
                Toast.makeText(getApplicationContext(), "Canteen B", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_btn_Canteen_C:
                Toast.makeText(getApplicationContext(), "Canteen C", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

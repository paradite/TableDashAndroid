package thack.ac.tabledash;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatusActivity extends BaseActivity {
    public static ArrayList<Table> tables = null;
    StatusActivity self = this;
    TabListener<StatisticsFragment> statsTabListener;

    // Needed to populate statistics page
    static public TextView tv_totalTables_value;
    static public TextView tv_vacantTables_value;
    static public TextView tv_avgWaitingTime;

    public String table_ID="canteen_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        statsTabListener = new TabListener<StatisticsFragment>(
                this, "statistics", StatisticsFragment.class);

        TabListener<GraphicalFragment> graphTabListener = new TabListener<GraphicalFragment>(
                this, "graphical", GraphicalFragment.class);

                Tab tab = actionBar.newTab()
                .setText(R.string.status_tab_statistics)
                .setTabListener(statsTabListener);
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.status_tab_graphical)
                .setTabListener(graphTabListener);

        actionBar.addTab(tab);





        // new Table(context, String id, int durationLeft, int locationX, int locationY)

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.status_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch(item.getItemId())
        {
            case R.id.action_update:
                // Update statistics & graphical layout fragments
                //Add nameValuePair for http request
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                addToNameValuePairsCheck(nameValuePairs, table_ID);
                new checkVacancyAsync().execute(nameValuePairs);
                break;

            case R.id.action_donate:
                onBraintreeSubmit(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        public Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

	    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

    /**
     * A statistics fragment
     *
     */
    public static class StatisticsFragment extends Fragment {

        public StatisticsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_statistics,
                    container, false);
            return rootView;
        }

        public void setViews(String total, String vacant, String ave){
            // Needed to populate statistics page

        }

    }

    /**
     * A graphical fragment
     *
     */
    public static class GraphicalFragment extends Fragment {

        public static GraphicalLayout graphicalLayout;

        public GraphicalFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            graphicalLayout = new GraphicalLayout(getActivity());
            return graphicalLayout;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_totalTables_value = (TextView) statsTabListener.mFragment.getView().findViewById(R.id.tv_totalTables_value);
        tv_vacantTables_value = (TextView) statsTabListener.mFragment.getView().findViewById(R.id.tv_vacantTables_value);
        tv_avgWaitingTime = (TextView) statsTabListener.mFragment.getView().findViewById(R.id.tv_averageWaitingTime_value);

        Log.e(TAG, tv_totalTables_value.toString() + tv_vacantTables_value.toString() + tv_avgWaitingTime.toString());
        String json = getIntent().getExtras().getString("json");
        //Format the JSON for tables
        //Wrapper JSONArray
        JSONArray tablewrapperJSON = null;
        JSONArray outerwrapperJSON = null;
        Log.e(TAG, "JSON 0: " + json);
        tables= new ArrayList<Table>();
        if (json != null) {
            try {
                outerwrapperJSON = new JSONArray(json);
                tablewrapperJSON = outerwrapperJSON.getJSONArray(0);
                Log.e(TAG, "JSON 1: " + outerwrapperJSON.getJSONArray(1));
                Log.e(TAG, "JSON 2: " + outerwrapperJSON.getJSONArray(1).getJSONArray(0));
                Log.e(TAG, "JSON 3: " + outerwrapperJSON.getJSONArray(1).getJSONArray(0).getInt(0));
                int occupy = outerwrapperJSON.getJSONArray(1).getJSONArray(0).getInt(0);
                int total = outerwrapperJSON.getJSONArray(2).getJSONArray(0).getInt(0);
                int vacant = total - occupy;
                int total_duration = 0;
                Log.e(TAG, total + " " + vacant + " " + total_duration);
                tv_totalTables_value.setText(String.valueOf(total));
                tv_vacantTables_value.setText(String.valueOf(vacant));
                int x[] = new int[]{80, 600, 80, 600, 80, 600, 80, 600};
                int y[] = new int[]{80, 80, 600, 600};

                //Add new experiments into database
                int length = 0;
                if (tablewrapperJSON.length() > 0) {
                    length = tablewrapperJSON.length();
                    for (int i = 0; i < 4; i++) {
                        JSONArray table = tablewrapperJSON.getJSONArray(i);
                        String tag_ID = table.getString(0);
                        String table_ID = table.getString(1);
                        String ending_time = table.getString(2);
                        int duration;
                        if(ending_time.equals("0")){
                            duration = Integer.parseInt(ending_time);
                        }else{
                            Date ending_date = Helper.parseDateFromString(ending_time);
                            duration = (int) (ending_date.getTime() - Calendar.getInstance().getTime().getTime()) / 1000/1000/60;
                            if(duration < 0 ){
                                duration = 0;
                            }
                        }
//                        int duration = 20;
                        total_duration += duration;
                        tables.add(new Table(this, table_ID, duration, x[i], y[i]));
                    }
                    tv_avgWaitingTime.setText(String.valueOf(total_duration / 4));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(tables.isEmpty()){
            tables.add(new Table(this, "Table 0", 30, 80, 80));
            tables.add(new Table(this, "Table 1", 30, 600, 80));
            tables.add(new Table(this, "Table 2", 30, 80, 600));
            tables.add(new Table(this, "Table 3", 30, 600, 600));
        }
    }

    /**
     * AsyncTask for checking vacancy
     */
    public class checkVacancyAsync extends AsyncTask<List<NameValuePair>, Void, String> {
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
            //Format the JSON for tables
            //Wrapper JSONArray
            JSONArray tablewrapperJSON = null;
            JSONArray outerwrapperJSON = null;
            Log.e(TAG, "JSON 0: " + json);
            tables= new ArrayList<Table>();
            if (json != null) {
                try {
                    outerwrapperJSON = new JSONArray(json);
                    tablewrapperJSON = outerwrapperJSON.getJSONArray(0);
                    Log.e(TAG, "JSON 1: " + outerwrapperJSON.getJSONArray(1));
                    Log.e(TAG, "JSON 2: " + outerwrapperJSON.getJSONArray(1).getJSONArray(0));
                    Log.e(TAG, "JSON 3: " + outerwrapperJSON.getJSONArray(1).getJSONArray(0).getInt(0));
                    int occupy = outerwrapperJSON.getJSONArray(1).getJSONArray(0).getInt(0);
                    int total = outerwrapperJSON.getJSONArray(2).getJSONArray(0).getInt(0);
                    int vacant = total - occupy;
                    int total_duration = 0;
                    Log.e(TAG, total + " " + vacant + " " + total_duration);
                    tv_totalTables_value.setText(String.valueOf(total));
                    tv_vacantTables_value.setText(String.valueOf(vacant));
                    int x[] = new int[]{80, 600, 80, 600, 80, 600, 80, 600};
                    int y[] = new int[]{80, 80, 600, 600};

                    //Add new experiments into database
                    int length = 0;
                    if (tablewrapperJSON.length() > 0) {
                        length = tablewrapperJSON.length();
                        for (int i = 0; i < 4; i++) {
                            JSONArray table = tablewrapperJSON.getJSONArray(i);
                            String tag_ID = table.getString(0);
                            String table_ID = table.getString(1);
                            String ending_time = table.getString(2);
                            int duration;
                            if (ending_time.equals("0")) {
                                duration = Integer.parseInt(ending_time);
                            } else {
                                Date ending_date = Helper.parseDateFromString(ending_time);
                                duration = (int) (ending_date.getTime() - Calendar.getInstance().getTime().getTime()) / 1000/1000/60;
                                if (duration < 0) {
                                    duration = 0;
                                }
                            }
//                        int duration = 20;
                            total_duration += duration;
                            tables.add(new Table(self, table_ID, duration, x[i], y[i]));
                        }
                        tv_avgWaitingTime.setText(String.valueOf((total_duration/4)));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}

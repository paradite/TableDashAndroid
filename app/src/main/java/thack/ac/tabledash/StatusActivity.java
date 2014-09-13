package thack.ac.tabledash;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class StatusActivity extends BaseActivity {

    public static ArrayList<Table> tables = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create tables
        tables = new ArrayList<Table>();

        // Setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab = actionBar.newTab()
                .setText(R.string.status_tab_statistics)
                .setTabListener(new TabListener<StatisticsFragment>(
                        this, "statistics", StatisticsFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.status_tab_graphical)
                .setTabListener(new TabListener<GraphicalFragment>(
                        this, "graphical", GraphicalFragment.class));

        actionBar.addTab(tab);

        // new Table(context, String id, int durationLeft, int locationX, int locationY)
        tables.add(new Table(this, "Guest 0", 30, 80, 80));
        tables.add(new Table(this, "Guest 1", 30, 600, 80));
        tables.add(new Table(this, "Guest 2", 30, 80, 600));
        tables.add(new Table(this, "Guest 3", 30, 600, 600));
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
                for(Table table : tables)
                {
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
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
}

package csd.jt.capsmobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BaseActivity extends Activity {

    // Navigation Drawer variables
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // create navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mActivityTitle = getTitle().toString();

        mDrawerList = (ListView) findViewById(R.id.navList);

        //setupDrawer();
        addDrawerItems();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);


    }

    // build navigation drawer and add listener
    private void addDrawerItems() {
        String[] navArray = {"Home", "Volunteer", "Organization", "Event", "Search", "Login", "Sign up", "Sign out"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                if (position == 1) {
                    Intent intent = new Intent(BaseActivity.this, VolunteerActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(BaseActivity.this, OrganizationActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(BaseActivity.this, EventActivity.class);
                    startActivity(intent);
                }
                if (position == 4) {
                    Intent intent = new Intent(BaseActivity.this, SearchForm.class);
                    startActivity(intent);
                }
                if (position == 5) {
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                if (position == 6) {
                    Intent intent = new Intent(BaseActivity.this, RegisterVolActivity.class);
                    startActivity(intent);
                }
                if (position == 7) {
                    SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.clear();
                    editor.commit();
                }
            }
        });
    }

//    private void setupDrawer() {
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                R.string.drawer_open, R.string.drawer_close) {
//
//            /** Called when a drawer has settled in a completely open state. */
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//
//            /** Called when a drawer has settled in a completely closed state. */
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(mActivityTitle);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };
//
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}

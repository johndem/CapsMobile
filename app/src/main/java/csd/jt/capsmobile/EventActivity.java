package csd.jt.capsmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class EventActivity extends BaseActivity {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    Activity act = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new EventPagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

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

        return super.onOptionsItemSelected(item);
    }

    class EventPagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "General";
            else return "Details";
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources

            View view;

            if (position == 0) {
                view = act.getLayoutInflater().inflate(R.layout.fragment_event_general,
                        container, false);

                Bundle extras = act.getIntent().getExtras();
                String title = null, category = null, area = null, date = null, desc = null;
                if (extras != null) {
                    title = extras.getString("title");
                    category = extras.getString("category");
                    area = extras.getString("area");
                    date = extras.getString("day");
                    desc = extras.getString("sdesc");
                }
                TextView titleTv = (TextView) view.findViewById(R.id.eventTitleTv);
                titleTv.setText(title);
                TextView catTv = (TextView) view.findViewById(R.id.eventCatTv);
                catTv.setText(category);
                TextView areaTv = (TextView) view.findViewById(R.id.eventAreaTv);
                areaTv.setText(area);
                TextView dateTv = (TextView) view.findViewById(R.id.eventDateTv);
                dateTv.setText(date);
                TextView descTv = (TextView) view.findViewById(R.id.eventSdescTv);
                descTv.setText(desc);

                SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                String id = myPrefs.getString("userId", null);
                String role = myPrefs.getString("userRole",null);

                if (role.equals("vol")) {
//                    TextView applyTv = (TextView) view.findViewById(R.id.applyTv);
//                    applyTv.setVisibility(view.VISIBLE);
//                    Spinner applySpin = (Spinner) findViewById(R.id.applySpin);
//                    applySpin.setVisibility(view.VISIBLE);
//                    Button applyBtn = (Button) findViewById(R.id.applyBtn);
//                    applyBtn.setVisibility(view.VISIBLE);
                }
                else if (role.equals("org")) {
                    TextView applicantsTv = (TextView) view.findViewById(R.id.applicantTv);
                    applicantsTv.setVisibility(view.VISIBLE);
                    ListView applicantLv = (ListView) view.findViewById(R.id.applicantLv);
                    applicantLv.setVisibility(view.VISIBLE);


                    GetApplicantData app = new GetApplicantData(view);
                    app.execute();

                }



            }
            else {
                view  = act.getLayoutInflater().inflate(R.layout.fragment_event_details,
                        container, false);

                Bundle extras = act.getIntent().getExtras();
                String address = null, area = null, str = null, zip = null, date = null, time = null, agegroup = null, skills = null, body = null;
                if (extras != null) {
                    address = extras.getString("address");
                    area = extras.getString("area");
                    str = extras.getString("street");
                    zip = extras.getString("zipcode");
                    date = extras.getString("day");
                    time = extras.getString("time");
                    agegroup = extras.getString("agegroup");
                    skills = extras.getString("skills");
                    body = extras.getString("ddesc");
                }
                TextView addressTv = (TextView) view.findViewById(R.id.eventAddressTv);
                addressTv.setText(address + " " + str + ", " + area + ", " + zip);
                TextView timeTv = (TextView) view.findViewById(R.id.eventTimeTv);
                timeTv.setText(date + ", " + time);
                TextView ageTv = (TextView) view.findViewById(R.id.eventAgeTv);
                ageTv.setText(agegroup);
                TextView skillsTv = (TextView) view.findViewById(R.id.eventSkillsTv);
                skillsTv.setText(skills);
                TextView bodyTv = (TextView) view.findViewById(R.id.eventBodyTv);
                bodyTv.setText(body);

                final String mapAddress = address, mapStreet = str, mapZipcode = zip, mapArea = area;

                Button mapBtn;
                mapBtn = (Button) view.findViewById(R.id.mapBtn);
                mapBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        Intent intent = new Intent(act, MapsActivity.class);
                        intent.putExtra("address", mapAddress);
                        intent.putExtra("street", mapStreet);
                        intent.putExtra("zipcode", mapZipcode);
                        intent.putExtra("area", mapArea);
                        startActivity(intent);
                    }
                });
            }
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }



        private class GetApplicantData extends AsyncTask<Void, Void, Void> {

            // JSON Node names
            private static final String TAG_APPLICANTS = "applicants";
            private static final String TAG_ID = "id";
            private static final String TAG_USERNAME = "username";
            private static final String TAG_FIRSTNAME = "firstname";
            private static final String TAG_LASTNAME = "lastname";
            private static final String TAG_SKILL = "skill";
            private static final String TAG_SELECTED = "selected";

            ArrayList<HashMap<String, String>> dataList;
            private String url = "http://10.0.2.2/CAPS/android/find-applicants.php";

            View v;
            JSONArray applicants = null;

            public GetApplicantData(View view) {
                dataList = new ArrayList<HashMap<String, String>>();
                v = view;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();

                // Building Parameters
                HashMap<String, String> params = new HashMap<>();
                SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
                String org_id = myPrefs.getString("userId", null);
                params.put("id", org_id);
                Bundle extras = act.getIntent().getExtras();
                String event_id = null;
                if (extras != null) {
                    event_id = extras.getString("id");
                    params.put("event-id", event_id);
                }

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url, params);

                Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        applicants = jsonObj.getJSONArray(TAG_APPLICANTS);

                        // looping through All Contacts
                        for (int i = 0; i < applicants.length(); i++) {
                            JSONObject c = applicants.getJSONObject(i);

                            String id = c.getString(TAG_ID);
                            String username = c.getString(TAG_USERNAME);
                            String firstname = c.getString(TAG_FIRSTNAME);
                            String lastname = c.getString(TAG_LASTNAME);
                            String skill = c.getString(TAG_SKILL);
                            String selected = c.getString(TAG_SELECTED);

                            String apply = firstname + " " + lastname + " has applied for " + skill;


                            // tmp hashmap for single contact
                            HashMap<String, String> row = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            row.put(TAG_ID, id);
                            row.put(TAG_USERNAME, username);
                            row.put(TAG_FIRSTNAME, firstname);
                            row.put(TAG_LASTNAME, lastname);
                            row.put(TAG_SKILL, skill);
                            row.put(TAG_SELECTED, selected);
                            row.put("apply", apply);

                            // adding contact to contact list
                            dataList.add(row);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();

                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                        act, dataList,
                        R.layout.list_item_4, new String[]{"apply"}, new int[]{R.id.appVolTv});

                ListView list = (ListView) v.findViewById(R.id.applicantLv);

                list.setAdapter(adapter);


            }

        }

    }


}


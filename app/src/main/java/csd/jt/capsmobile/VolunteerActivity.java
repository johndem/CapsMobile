package csd.jt.capsmobile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class VolunteerActivity extends BaseActivity {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    Activity act = this;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new VolPagerAdapter());
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
        getMenuInflater().inflate(R.menu.menu_volunteer, menu);
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

    class VolPagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
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
            if (position == 0) return "Profile";
            else if (position == 1) return "Active events";
            else return "Completed Events";
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
                view = act.getLayoutInflater().inflate(R.layout.fragment_vol_profile,
                        container, false);


                GetProfileData pr = new GetProfileData(view);
                pr.execute();
            }
            else if (position == 1) {
                view = act.getLayoutInflater().inflate(R.layout.fragment_vol_active_events,
                        container, false);

                GetActivetData active = new GetActivetData(view);
                active.execute();
            }
            else {
                view = act.getLayoutInflater().inflate(R.layout.fragment_vol_completed_events,
                        container, false);


                GetCompletedData com = new GetCompletedData(view);

                com.execute();
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

    }

    private class GetProfileData extends AsyncTask<Void, Void, Void> {

        // JSON Node names
        private static final String TAG_VOLUNTEER = "volunteer";
        private static final String TAG_FIRSTNAME = "firstname";
        private static final String TAG_LASTNAME = "lastname";
        private static final String TAG_USERNAME = "username";
        private static final String TAG_EMAIL = "email";
        private static final String TAG_PHONE = "phone";
        private static final String TAG_ADDRESS = "address";
        private static final String TAG_STREET = "street";
        private static final String TAG_ZIPCODE = "zipcode";
        private static final String TAG_CITY = "city";
        private static final String TAG_DOB = "dob";
        private static final String TAG_IMAGE = "image";
        private static final String TAG_POINTS = "points";

        // URL to get contacts JSON
        private String url = "http://10.0.2.2/android/find-vol.php";

        HashMap<String, String> dataList;
        // contacts JSONArray
        JSONArray completed = null;
        View v;
        public GetProfileData(View view) {
            v= view;
            dataList = new HashMap<String, String>();
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
            params.put("id", "1");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    completed = jsonObj.getJSONArray(TAG_VOLUNTEER);

                    // looping through All Contacts
                    for (int i = 0; i < completed.length(); i++) {
                        JSONObject c = completed.getJSONObject(i);

                        String firstname = c.getString(TAG_FIRSTNAME);
                        String lastname = c.getString(TAG_LASTNAME);
                        String username = c.getString(TAG_USERNAME);
                        String email = c.getString(TAG_EMAIL);
                        String phone = c.getString(TAG_PHONE);
                        String address = c.getString(TAG_ADDRESS);
                        String street = c.getString(TAG_STREET);
                        String zipcode = c.getString(TAG_ZIPCODE);
                        String city = c.getString(TAG_CITY);
                        String dob = c.getString(TAG_DOB);
                        String image = c.getString(TAG_IMAGE);
                        String points = c.getString(TAG_POINTS);

                        // adding each child node to HashMap key => value
                        dataList.put(TAG_FIRSTNAME, firstname);
                        dataList.put(TAG_LASTNAME, lastname);
                        dataList.put(TAG_USERNAME, username);
                        dataList.put(TAG_EMAIL, email);
                        dataList.put(TAG_PHONE, phone);
                        dataList.put(TAG_ADDRESS, address);
                        dataList.put(TAG_STREET, street);
                        dataList.put(TAG_ZIPCODE, zipcode);
                        dataList.put(TAG_CITY, city);
                        dataList.put(TAG_DOB, dob);
                        dataList.put(TAG_IMAGE, image);
                        dataList.put(TAG_POINTS, points);

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

            //ImageView img;
            TextView nameTv, emailTv, dobTv, pointsTv;

           // img = (ImageView) v.findViewById(R.id.volProfileImg);
            nameTv = (TextView) v.findViewById(R.id.nameTv);
            emailTv = (TextView) v.findViewById(R.id.emailTv);
            dobTv = (TextView) v.findViewById(R.id.dobTv);
            pointsTv = (TextView) v.findViewById(R.id.pointsTv);

           // img.setImageURI(Uri.parse("http://10.0.2.2/" + dataList.get(TAG_IMAGE)));
            String name = dataList.get(TAG_FIRSTNAME) + " " + dataList.get(TAG_LASTNAME);
            nameTv.setText(nameTv.getText() + name);
            String email = dataList.get(TAG_EMAIL);
            emailTv.setText(emailTv.getText() + email);
            String dob = dataList.get(TAG_DOB);
            dobTv.setText(dobTv.getText() + dob);
            String points = dataList.get(TAG_POINTS);
            pointsTv.setText(pointsTv.getText() + points);

        }

    }

    private class GetActivetData extends AsyncTask<Void, Void, Void> {

        private String url = "http://10.0.2.2/android/find-vol-active.php";

        private static final String TAG_ACTIVE = "active";
        private static final String TAG_TITLE = "title";
        private static final String TAG_CATEGORY = "category";
        private static final String TAG_ADDRESS = "address";
        private static final String TAG_STREET = "street";
        private static final String TAG_ZIPCODE = "zipcode";
        private static final String TAG_AREA = "area";
        private static final String TAG_DAY = "day";
        private static final String TAG_TIME = "time";
        private static final String TAG_AGEGROUP = "agegroup";
        private static final String TAG_SKILLS = "skills";
        private static final String TAG_SDESC = "sdesc";
        private static final String TAG_DDESC = "ddesc";
        private static final String TAG_IMAGE1 = "image1";
        private static final String TAG_IMAGE2 = "image2";
        private static final String TAG_IMAGE3 = "image3";

        ArrayList<HashMap<String, String>> dataList;
        View v;
        JSONArray active = null;

        public GetActivetData(View view) {
            v= view;
            dataList = new ArrayList<HashMap<String, String>>();

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
            params.put("id", "1");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    active = jsonObj.getJSONArray(TAG_ACTIVE);

                    // looping through All Contacts
                    for (int i = 0; i < active.length(); i++) {
                        JSONObject c = active.getJSONObject(i);

                        String title = c.getString(TAG_TITLE);
                        String category = c.getString(TAG_CATEGORY);
                        String address = c.getString(TAG_ADDRESS);
                        String street = c.getString(TAG_STREET);
                        String zipcode = c.getString(TAG_ZIPCODE);
                        String area = c.getString(TAG_AREA);
                        String day = c.getString(TAG_DAY);
                        String time = c.getString(TAG_TIME);
                        String agegroup = c.getString(TAG_AGEGROUP);
                        String skills = c.getString(TAG_SKILLS);
                        String sdesc = c.getString(TAG_SDESC);
                        String ddesc = c.getString(TAG_DDESC);
                        String image1 = c.getString(TAG_IMAGE1);
                        String image2 = c.getString(TAG_IMAGE2);
                        String image3 = c.getString(TAG_IMAGE3);


                        // tmp hashmap for single contact
                        HashMap<String, String> row = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        row.put(TAG_TITLE, title);
                        row.put(TAG_CATEGORY, category);
                        row.put(TAG_ADDRESS, address);
                        row.put(TAG_STREET, street);
                        row.put(TAG_ZIPCODE, zipcode);
                        row.put(TAG_AREA, area);
                        row.put(TAG_DAY, day);
                        row.put(TAG_TIME, time);
                        row.put(TAG_AGEGROUP, agegroup);
                        row.put(TAG_SKILLS, skills);
                        row.put(TAG_SDESC, sdesc);
                        row.put(TAG_DDESC, ddesc);
                        row.put(TAG_IMAGE1, image1);
                        row.put(TAG_IMAGE2, image2);
                        row.put(TAG_IMAGE3, image3);


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

            ListAdapter adapter = new SimpleAdapter(
                    act, dataList,
                    R.layout.list_item_2, new String[]{TAG_TITLE, TAG_CATEGORY, TAG_AREA, TAG_DAY}, new int[]{R.id.title, R.id.category, R.id.at, R.id.when});

            ListView list = (ListView) v.findViewById(R.id.activelist);

            list.setAdapter(adapter);



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(act, EventActivity.class);

                    TextView tv = (TextView) view.findViewById(R.id.title);
                    String title = tv.getText().toString();

                    Bundle params = getParams(title);
                    intent.putExtras(params);

                    startActivity(intent);
                }
            });
        }

        private Bundle getParams(String title) {
            Bundle bundle = new Bundle();

            for (HashMap<String, String> listItem : dataList) {
                if (listItem.get(TAG_TITLE).equals(title)) {
                    bundle.putString(TAG_TITLE, title);
                    bundle.putString(TAG_CATEGORY, listItem.get(TAG_CATEGORY));
                    bundle.putString(TAG_ADDRESS, listItem.get(TAG_ADDRESS));
                    bundle.putString(TAG_STREET, listItem.get(TAG_STREET));
                    bundle.putString(TAG_ZIPCODE, listItem.get(TAG_ZIPCODE));
                    bundle.putString(TAG_AREA, listItem.get(TAG_AREA));
                    bundle.putString(TAG_DAY, listItem.get(TAG_DAY));
                    bundle.putString(TAG_TIME, listItem.get(TAG_TIME));
                    bundle.putString(TAG_AGEGROUP, listItem.get(TAG_AGEGROUP));
                    bundle.putString(TAG_SKILLS, listItem.get(TAG_SKILLS));
                    bundle.putString(TAG_SDESC, listItem.get(TAG_SDESC));
                    bundle.putString(TAG_DDESC, listItem.get(TAG_DDESC));
                    bundle.putString(TAG_IMAGE1, listItem.get(TAG_IMAGE1));
                    bundle.putString(TAG_IMAGE2, listItem.get(TAG_IMAGE2));
                    bundle.putString(TAG_IMAGE3, listItem.get(TAG_IMAGE3));
                }
            }

            return  bundle;
        }



    }

    private class GetCompletedData extends AsyncTask<Void, Void, Void> {

        // JSON Node names
        private static final String TAG_COMPLETED = "completed";
        private static final String TAG_TITLE = "title";
        private static final String TAG_CATEGORY = "category";
        private static final String TAG_ADDRESS = "address";
        private static final String TAG_STREET = "street";
        private static final String TAG_ZIPCODE = "zipcode";
        private static final String TAG_AREA = "area";
        private static final String TAG_DAY = "day";
        private static final String TAG_TIME = "time";
        private static final String TAG_AGEGROUP = "agegroup";
        private static final String TAG_SKILLS = "skills";
        private static final String TAG_SDESC = "sdesc";
        private static final String TAG_DDESC = "ddesc";
        private static final String TAG_IMAGE1 = "image1";
        private static final String TAG_IMAGE2 = "image2";
        private static final String TAG_IMAGE3 = "image3";

        ArrayList<HashMap<String, String>> dataList;
        private String url = "http://10.0.2.2/android/find-vol-completed.php";
        View v;
        JSONArray completed = null;
        public GetCompletedData(View view) {
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
            params.put("id", "1");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    completed = jsonObj.getJSONArray(TAG_COMPLETED);

                    // looping through All Contacts
                    for (int i = 0; i < completed.length(); i++) {
                        JSONObject c = completed.getJSONObject(i);

                        String title = c.getString(TAG_TITLE);
                        String category = c.getString(TAG_CATEGORY);
                        String address = c.getString(TAG_ADDRESS);
                        String street = c.getString(TAG_STREET);
                        String zipcode = c.getString(TAG_ZIPCODE);
                        String area = c.getString(TAG_AREA);
                        String day = c.getString(TAG_DAY);
                        String time = c.getString(TAG_TIME);
                        String agegroup = c.getString(TAG_AGEGROUP);
                        String skills = c.getString(TAG_SKILLS);
                        String sdesc = c.getString(TAG_SDESC);
                        String ddesc = c.getString(TAG_DDESC);
                        String image1 = c.getString(TAG_IMAGE1);
                        String image2 = c.getString(TAG_IMAGE2);
                        String image3 = c.getString(TAG_IMAGE3);


                        // tmp hashmap for single contact
                        HashMap<String, String> row = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        row.put(TAG_TITLE, title);
                        row.put(TAG_CATEGORY, category);
                        row.put(TAG_ADDRESS, address);
                        row.put(TAG_STREET, street);
                        row.put(TAG_ZIPCODE, zipcode);
                        row.put(TAG_AREA, area);
                        row.put(TAG_DAY, day);
                        row.put(TAG_TIME, time);
                        row.put(TAG_AGEGROUP, agegroup);
                        row.put(TAG_SKILLS, skills);
                        row.put(TAG_SDESC, sdesc);
                        row.put(TAG_DDESC, ddesc);
                        row.put(TAG_IMAGE1, image1);
                        row.put(TAG_IMAGE2, image2);
                        row.put(TAG_IMAGE3, image3);


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
                    R.layout.list_item_2, new String[]{TAG_TITLE, TAG_CATEGORY, TAG_AREA, TAG_DAY}, new int[]{R.id.title, R.id.category, R.id.at, R.id.when});

            ListView list = (ListView) v.findViewById(R.id.completelist);

            list.setAdapter(adapter);



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(act, EventActivity.class);

                    TextView tv = (TextView) view.findViewById(R.id.title);
                    String title = tv.getText().toString();

                    Bundle params = getParams(title);
                    intent.putExtras(params);

                    startActivity(intent);
                }
            });

        }

        private Bundle getParams(String title) {
            Bundle bundle = new Bundle();

            for (HashMap<String, String> listItem : dataList) {
                if (listItem.get(TAG_TITLE).equals(title)) {
                    bundle.putString(TAG_TITLE, title);
                    bundle.putString(TAG_CATEGORY, listItem.get(TAG_CATEGORY));
                    bundle.putString(TAG_ADDRESS, listItem.get(TAG_ADDRESS));
                    bundle.putString(TAG_STREET, listItem.get(TAG_STREET));
                    bundle.putString(TAG_ZIPCODE, listItem.get(TAG_ZIPCODE));
                    bundle.putString(TAG_AREA, listItem.get(TAG_AREA));
                    bundle.putString(TAG_DAY, listItem.get(TAG_DAY));
                    bundle.putString(TAG_TIME, listItem.get(TAG_TIME));
                    bundle.putString(TAG_AGEGROUP, listItem.get(TAG_AGEGROUP));
                    bundle.putString(TAG_SKILLS, listItem.get(TAG_SKILLS));
                    bundle.putString(TAG_SDESC, listItem.get(TAG_SDESC));
                    bundle.putString(TAG_DDESC, listItem.get(TAG_DDESC));
                    bundle.putString(TAG_IMAGE1, listItem.get(TAG_IMAGE1));
                    bundle.putString(TAG_IMAGE2, listItem.get(TAG_IMAGE2));
                    bundle.putString(TAG_IMAGE3, listItem.get(TAG_IMAGE3));
                }
            }

            return  bundle;
        }

    }
}

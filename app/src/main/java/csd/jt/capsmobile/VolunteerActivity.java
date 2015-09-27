package csd.jt.capsmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private String uri = "http://idematis.webpages.auth.gr";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Παρακαλούμε περιμένετε...");
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
            if (position == 0) return "ΠΡΟΦΙΛ";
            else if (position == 1) return "ΙΣΤΟΡΙΚΟ";
            else return "ΕΙΔΟΠΟΙΗΣΕΙΣ";
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
                view = act.getLayoutInflater().inflate(R.layout.fragment_vol_history,
                        container, false);

                GetHistoryData hist = new GetHistoryData(view);
                hist.execute();
            }
            else {
                view = act.getLayoutInflater().inflate(R.layout.fragment_vol_notifications,
                        container, false);


                GetNotificationData not = new GetNotificationData(view);

                not.execute();
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
        private String url = uri + "/CAPS/android/find-vol.php";

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
            SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String id = myPrefs.getString("userId",null);
            params.put("id", id);

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

    private class GetHistoryData extends AsyncTask<Void, Void, Void> {

        private String urlActive = uri + "/CAPS/android/find-vol-active.php";
        private String urlCompleted = uri + "/CAPS/android/find-vol-completed.php";

        private static final String TAG_ACTIVE = "active";
        private static final String TAG_aID = "id";
        private static final String TAG_aTITLE = "title";
        private static final String TAG_aCATEGORY = "category";
        private static final String TAG_aADDRESS = "address";
        private static final String TAG_aSTREET = "street";
        private static final String TAG_aZIPCODE = "zipcode";
        private static final String TAG_aAREA = "area";
        private static final String TAG_aDAY = "day";
        private static final String TAG_aTIME = "time";
        private static final String TAG_aAGEGROUP = "agegroup";
        private static final String TAG_aSKILLS = "skills";
        private static final String TAG_aSDESC = "sdesc";
        private static final String TAG_aDDESC = "ddesc";
        private static final String TAG_aIMAGE1 = "image1";
        private static final String TAG_aIMAGE2 = "image2";
        private static final String TAG_aIMAGE3 = "image3";

        private static final String TAG_COMPLETED = "completed";
        private static final String TAG_cID = "id";
        private static final String TAG_cTITLE = "title";
        private static final String TAG_cCATEGORY = "category";
        private static final String TAG_cADDRESS = "address";
        private static final String TAG_cSTREET = "street";
        private static final String TAG_cZIPCODE = "zipcode";
        private static final String TAG_cAREA = "area";
        private static final String TAG_cDAY = "day";
        private static final String TAG_cTIME = "time";
        private static final String TAG_cAGEGROUP = "agegroup";
        private static final String TAG_cSKILLS = "skills";
        private static final String TAG_cSDESC = "sdesc";
        private static final String TAG_cDDESC = "ddesc";
        private static final String TAG_cIMAGE1 = "image1";
        private static final String TAG_cIMAGE2 = "image2";
        private static final String TAG_cIMAGE3 = "image3";

        ArrayList<HashMap<String, String>> dataListA;
        ArrayList<HashMap<String, String>> dataListC;

        View v;
        JSONArray active = null, completed = null;

        public GetHistoryData(View view) {
            v= view;
            dataListA = new ArrayList<HashMap<String, String>>();
            dataListC = new ArrayList<HashMap<String, String>>();
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
            String id = myPrefs.getString("userId",null);
            params.put("id", id);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlActive, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    active = jsonObj.getJSONArray(TAG_ACTIVE);

                    // looping through All Contacts
                    for (int i = 0; i < active.length(); i++) {
                        JSONObject c = active.getJSONObject(i);

                        String event_id = c.getString(TAG_aID);
                        String title = c.getString(TAG_aTITLE);
                        String category = c.getString(TAG_aCATEGORY);
                        String address = c.getString(TAG_aADDRESS);
                        String street = c.getString(TAG_aSTREET);
                        String zipcode = c.getString(TAG_aZIPCODE);
                        String area = c.getString(TAG_aAREA);
                        String day = c.getString(TAG_aDAY);
                        String time = c.getString(TAG_aTIME);
                        String agegroup = c.getString(TAG_aAGEGROUP);
                        String skills = c.getString(TAG_aSKILLS);
                        String sdesc = c.getString(TAG_aSDESC);
                        String ddesc = c.getString(TAG_aDDESC);
                        String image1 = c.getString(TAG_aIMAGE1);
                        String image2 = c.getString(TAG_aIMAGE2);
                        String image3 = c.getString(TAG_aIMAGE3);


                        // tmp hashmap for single contact
                        HashMap<String, String> row = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        row.put(TAG_aID, event_id);
                        row.put(TAG_aTITLE, title);
                        row.put(TAG_aCATEGORY, category);
                        row.put(TAG_aADDRESS, address);
                        row.put(TAG_aSTREET, street);
                        row.put(TAG_aZIPCODE, zipcode);
                        row.put(TAG_aAREA, area);
                        row.put(TAG_aDAY, day);
                        row.put(TAG_aTIME, time);
                        row.put(TAG_aAGEGROUP, agegroup);
                        row.put(TAG_aSKILLS, skills);
                        row.put(TAG_aSDESC, sdesc);
                        row.put(TAG_aDDESC, ddesc);
                        row.put(TAG_aIMAGE1, image1);
                        row.put(TAG_aIMAGE2, image2);
                        row.put(TAG_aIMAGE3, image3);


                        // adding contact to contact list
                        dataListA.add(row);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(urlCompleted, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    active = jsonObj.getJSONArray(TAG_COMPLETED);

                    // looping through All Contacts
                    for (int i = 0; i < active.length(); i++) {
                        JSONObject c = active.getJSONObject(i);

                        String event_id = c.getString(TAG_cID);
                        String title = c.getString(TAG_cTITLE);
                        String category = c.getString(TAG_cCATEGORY);
                        String address = c.getString(TAG_cADDRESS);
                        String street = c.getString(TAG_cSTREET);
                        String zipcode = c.getString(TAG_cZIPCODE);
                        String area = c.getString(TAG_cAREA);
                        String day = c.getString(TAG_cDAY);
                        String time = c.getString(TAG_cTIME);
                        String agegroup = c.getString(TAG_cAGEGROUP);
                        String skills = c.getString(TAG_cSKILLS);
                        String sdesc = c.getString(TAG_cSDESC);
                        String ddesc = c.getString(TAG_cDDESC);
                        String image1 = c.getString(TAG_cIMAGE1);
                        String image2 = c.getString(TAG_cIMAGE2);
                        String image3 = c.getString(TAG_cIMAGE3);


                        // tmp hashmap for single contact
                        HashMap<String, String> row = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        row.put(TAG_cID, event_id);
                        row.put(TAG_cTITLE, title);
                        row.put(TAG_cCATEGORY, category);
                        row.put(TAG_cADDRESS, address);
                        row.put(TAG_cSTREET, street);
                        row.put(TAG_cZIPCODE, zipcode);
                        row.put(TAG_cAREA, area);
                        row.put(TAG_cDAY, day);
                        row.put(TAG_cTIME, time);
                        row.put(TAG_cAGEGROUP, agegroup);
                        row.put(TAG_cSKILLS, skills);
                        row.put(TAG_cSDESC, sdesc);
                        row.put(TAG_cDDESC, ddesc);
                        row.put(TAG_cIMAGE1, image1);
                        row.put(TAG_cIMAGE2, image2);
                        row.put(TAG_cIMAGE3, image3);


                        // adding contact to contact list
                        dataListC.add(row);
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

            ListAdapter adapterA = new SimpleAdapter(
                    act, dataListA,
                    R.layout.list_item_2, new String[]{TAG_aTITLE, TAG_aCATEGORY, TAG_aAREA, TAG_aDAY}, new int[]{R.id.title, R.id.category, R.id.at, R.id.when});

            ListView listActive = (ListView) v.findViewById(R.id.activelist);

            listActive.setAdapter(adapterA);

            listActive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(act, EventActivity.class);

                    TextView tv = (TextView) view.findViewById(R.id.title);
                    String title = tv.getText().toString();

                    Bundle params = getParams(title, 0);
                    intent.putExtras(params);

                    startActivity(intent);
                }
            });

            ListAdapter adapterC = new SimpleAdapter(
                    act, dataListC,
                    R.layout.list_item_2, new String[]{TAG_cTITLE, TAG_cCATEGORY, TAG_cAREA, TAG_cDAY}, new int[]{R.id.title, R.id.category, R.id.at, R.id.when});

            ListView listCompleted = (ListView) v.findViewById(R.id.completedlist);

            listCompleted.setAdapter(adapterC);

            listCompleted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(act, EventActivity.class);

                    TextView tv = (TextView) view.findViewById(R.id.title);
                    String title = tv.getText().toString();

                    Bundle params = getParams(title, 1);
                    intent.putExtras(params);

                    startActivity(intent);
                }
            });
        }

        private Bundle getParams(String title, int list) {
            Bundle bundle = new Bundle();

            if (list == 0) {
                for (HashMap<String, String> listItem : dataListA) {
                    if (listItem.get(TAG_aTITLE).equals(title)) {
                        bundle.putString(TAG_aID, listItem.get(TAG_aID));
                        bundle.putString(TAG_aTITLE, title);
                        bundle.putString(TAG_aCATEGORY, listItem.get(TAG_aCATEGORY));
                        bundle.putString(TAG_aADDRESS, listItem.get(TAG_aADDRESS));
                        bundle.putString(TAG_aSTREET, listItem.get(TAG_aSTREET));
                        bundle.putString(TAG_aZIPCODE, listItem.get(TAG_aZIPCODE));
                        bundle.putString(TAG_aAREA, listItem.get(TAG_aAREA));
                        bundle.putString(TAG_aDAY, listItem.get(TAG_aDAY));
                        bundle.putString(TAG_aTIME, listItem.get(TAG_aTIME));
                        bundle.putString(TAG_aAGEGROUP, listItem.get(TAG_aAGEGROUP));
                        bundle.putString(TAG_aSKILLS, listItem.get(TAG_aSKILLS));
                        bundle.putString(TAG_aSDESC, listItem.get(TAG_aSDESC));
                        bundle.putString(TAG_aDDESC, listItem.get(TAG_aDDESC));
                        bundle.putString(TAG_aIMAGE1, listItem.get(TAG_aIMAGE1));
                        bundle.putString(TAG_aIMAGE2, listItem.get(TAG_aIMAGE2));
                        bundle.putString(TAG_aIMAGE3, listItem.get(TAG_aIMAGE3));
                    }
                }
            }
            else if (list == 1) {
                for (HashMap<String, String> listItem : dataListC) {
                    if (listItem.get(TAG_cTITLE).equals(title)) {
                        bundle.putString(TAG_cID, listItem.get(TAG_cID));
                        bundle.putString(TAG_cTITLE, title);
                        bundle.putString(TAG_cCATEGORY, listItem.get(TAG_cCATEGORY));
                        bundle.putString(TAG_cADDRESS, listItem.get(TAG_cADDRESS));
                        bundle.putString(TAG_cSTREET, listItem.get(TAG_cSTREET));
                        bundle.putString(TAG_cZIPCODE, listItem.get(TAG_cZIPCODE));
                        bundle.putString(TAG_cAREA, listItem.get(TAG_cAREA));
                        bundle.putString(TAG_cDAY, listItem.get(TAG_cDAY));
                        bundle.putString(TAG_cTIME, listItem.get(TAG_cTIME));
                        bundle.putString(TAG_cAGEGROUP, listItem.get(TAG_cAGEGROUP));
                        bundle.putString(TAG_cSKILLS, listItem.get(TAG_cSKILLS));
                        bundle.putString(TAG_cSDESC, listItem.get(TAG_cSDESC));
                        bundle.putString(TAG_cDDESC, listItem.get(TAG_cDDESC));
                        bundle.putString(TAG_cIMAGE1, listItem.get(TAG_cIMAGE1));
                        bundle.putString(TAG_cIMAGE2, listItem.get(TAG_cIMAGE2));
                        bundle.putString(TAG_cIMAGE3, listItem.get(TAG_cIMAGE3));
                    }
                }
            }


            return  bundle;
        }



    }

    private class GetNotificationData extends AsyncTask<Void, Void, Void> {

        // JSON Node names
        private static final String TAG_NOTIFICATIONS = "notifications";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_DATE = "date";
        private static final String TAG_TITLE = "title";
        private static final String TAG_ID = "id";
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
        private String url = uri + "/CAPS/android/get-notifications.php";

        View v;
        JSONArray notifications = null;

        public GetNotificationData(View view) {
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
            String id = myPrefs.getString("userId", null);
            String role = myPrefs.getString("userRole",null);
            params.put("id", id);
            if (role.equals("vol"))
                params.put("role", "0");
            else if (role.equals("org"))
                params.put("role", "1");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    notifications = jsonObj.getJSONArray(TAG_NOTIFICATIONS);

                    // looping through All Contacts
                    for (int i = 0; i < notifications.length(); i++) {
                        JSONObject c = notifications.getJSONObject(i);

                        String message = c.getString(TAG_MESSAGE);
                        String title = c.getString(TAG_TITLE);
                        String date = c.getString(TAG_DATE);
                        String event_id = c.getString(TAG_ID);
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
                        row.put(TAG_MESSAGE, message);
                        row.put(TAG_TITLE, title);
                        row.put(TAG_DATE, date);
                        row.put(TAG_ID, event_id);
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
                    R.layout.list_item_3, new String[]{TAG_MESSAGE, TAG_TITLE, TAG_DATE}, new int[]{R.id.message, R.id.event, R.id.date});

            ListView list = (ListView) v.findViewById(R.id.messageList);

            list.setAdapter(adapter);



            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(act, EventActivity.class);

                    TextView tv = (TextView) view.findViewById(R.id.event);
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
                    bundle.putString(TAG_ID, listItem.get(TAG_ID));
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

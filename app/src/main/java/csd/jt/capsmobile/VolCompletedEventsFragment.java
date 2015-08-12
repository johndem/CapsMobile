package csd.jt.capsmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
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


public class VolCompletedEventsFragment extends ListFragment {

    private ListView lv;
    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://10.0.2.2/android/find-vol-completed.php";

    // JSON Node names
    private static final String TAG_COMPLETED = "completed";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CREATOR = "creator";
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


    // contacts JSONArray
    JSONArray completed = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_vol_completed_events, container, false);

        dataList = new ArrayList<HashMap<String, String>>();

        // Calling async task to get json
        new GetData().execute();

        return rootView;
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

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
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
                        String creator = c.getString(TAG_CREATOR);
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
                        row.put(TAG_CREATOR, creator);
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
                    getActivity(), dataList,
                    R.layout.list_item, new String[]{TAG_TITLE, TAG_CATEGORY, TAG_SDESC}, new int[]{R.id.title, R.id.category, R.id.sdesc});

            setListAdapter(adapter);

            lv = getListView();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), EventActivity.class);

                    TextView tv = (TextView) view.findViewById(R.id.title);
                    String title = tv.getText().toString();

                    Bundle params = getParams(title);
                    intent.putExtras(params);

                    startActivity(intent);
                }
            });
        }

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
}

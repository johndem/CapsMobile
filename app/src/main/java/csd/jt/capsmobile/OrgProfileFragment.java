package csd.jt.capsmobile;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrgProfileFragment extends Fragment {

    private ProgressDialog pDialog;
    ImageView img;
    TextView nameTv, emailTv, descTv;

    // URL to get contacts JSON
    private static String url = "http://10.0.2.2/android/find-org.php";

    // JSON Node names
    private static final String TAG_ORGANIZATION = "organization";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_NAME = "name";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_FACEBOOK = "facebook";
    private static final String TAG_TWITTER = "twitter";
    private static final String TAG_OTHER = "other";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_IMAGE = "image";


    // contacts JSONArray
    JSONArray completed = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dataList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_org_profile, container, false);

        img = (ImageView) rootView.findViewById(R.id.orgProfileImg);
        nameTv = (TextView) rootView.findViewById(R.id.orgTitleTv);
        emailTv = (TextView) rootView.findViewById(R.id.orgEmailTv);
        descTv = (TextView) rootView.findViewById(R.id.orgDescTv);

        dataList = new ArrayList<HashMap<String, String>>();

        // Calling async task to get json
        new GetData().execute();

        return rootView;
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
                    completed = jsonObj.getJSONArray(TAG_ORGANIZATION);

                    // looping through All Contacts
                    for (int i = 0; i < completed.length(); i++) {
                        JSONObject c = completed.getJSONObject(i);

                        String username = c.getString(TAG_USERNAME);
                        String email = c.getString(TAG_EMAIL);
                        String name = c.getString(TAG_NAME);
                        String website = c.getString(TAG_WEBSITE);
                        String facebook = c.getString(TAG_FACEBOOK);
                        String twitter = c.getString(TAG_TWITTER);
                        String other = c.getString(TAG_OTHER);
                        String description = c.getString(TAG_DESCRIPTION);
                        String image = c.getString(TAG_IMAGE);

                        // tmp hashmap for single contact
                        HashMap<String, String> row = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        row.put(TAG_USERNAME, username);
                        row.put(TAG_EMAIL, email);
                        row.put(TAG_NAME, name);
                        row.put(TAG_WEBSITE, website);
                        row.put(TAG_FACEBOOK, facebook);
                        row.put(TAG_TWITTER, twitter);
                        row.put(TAG_OTHER, other);
                        row.put(TAG_DESCRIPTION, description);
                        row.put(TAG_IMAGE, image);

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
            img.setImageURI(Uri.parse("http://10.0.2.2/" + dataList.get(0).get(TAG_IMAGE)));
            String title = dataList.get(0).get(TAG_NAME);
            nameTv.setText(nameTv.getText() + title);
            String email = dataList.get(0).get(TAG_EMAIL);
            emailTv.setText(emailTv.getText() + email);
            String desc = dataList.get(0).get(TAG_DESCRIPTION);
            descTv.setText(desc);

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

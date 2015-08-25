package csd.jt.capsmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends Activity {

    private ProgressDialog pDialog;
    EditText username, password;
    Button logBtn;
    ServiceHandler sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sh = new ServiceHandler();

        username = (EditText) findViewById(R.id.usernameEt);
        password = (EditText) findViewById(R.id.passEt);
        logBtn = (Button) findViewById(R.id.loginBtn);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                // Calling async task to get json
                new GetData().execute(user, pass);
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            String url = "http://10.0.2.2/android/verify-user.php";
            HashMap<String, String> params = new HashMap<>();
            String user = arg[0];
            String pass = arg[1];
            params.put("username", user);
            params.put("password", pass);
            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
//                //Toast.makeText(LoginActivity.this, jsonStr, Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    active = jsonObj.getJSONArray(TAG_ACTIVE);
//
//                    // looping through All Contacts
//                    for (int i = 0; i < active.length(); i++) {
//                        JSONObject c = active.getJSONObject(i);
//
//                        String title = c.getString(TAG_TITLE);
//                        String category = c.getString(TAG_CATEGORY);
//                        String address = c.getString(TAG_ADDRESS);
//                        String street = c.getString(TAG_STREET);
//                        String zipcode = c.getString(TAG_ZIPCODE);
//                        String area = c.getString(TAG_AREA);
//                        String day = c.getString(TAG_DAY);
//                        String time = c.getString(TAG_TIME);
//                        String agegroup = c.getString(TAG_AGEGROUP);
//                        String skills = c.getString(TAG_SKILLS);
//                        String sdesc = c.getString(TAG_SDESC);
//                        String ddesc = c.getString(TAG_DDESC);
//                        String image1 = c.getString(TAG_IMAGE1);
//                        String image2 = c.getString(TAG_IMAGE2);
//                        String image3 = c.getString(TAG_IMAGE3);
//
//
//                        // tmp hashmap for single contact
//                        HashMap<String, String> row = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        row.put(TAG_TITLE, title);
//                        row.put(TAG_CATEGORY, category);
//                        row.put(TAG_ADDRESS, address);
//                        row.put(TAG_STREET, street);
//                        row.put(TAG_ZIPCODE, zipcode);
//                        row.put(TAG_AREA, area);
//                        row.put(TAG_DAY, day);
//                        row.put(TAG_TIME, time);
//                        row.put(TAG_AGEGROUP, agegroup);
//                        row.put(TAG_SKILLS, skills);
//                        row.put(TAG_SDESC, sdesc);
//                        row.put(TAG_DDESC, ddesc);
//                        row.put(TAG_IMAGE1, image1);
//                        row.put(TAG_IMAGE2, image2);
//                        row.put(TAG_IMAGE3, image3);
//
//
//                        // adding contact to contact list
//                        dataList.add(row);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}

package csd.jt.capsmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private EditText username, password;
    private Button logBtn;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameEt);
        password = (EditText) findViewById(R.id.passEt);
        logBtn = (Button) findViewById(R.id.loginBtn);

        sharedpreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Please fill both fields!", Toast.LENGTH_SHORT).show();
                else {
                    // Calling async task to get json
                    new GetData().execute(user, pass);
                }
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<String, Void, String> {

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
        protected String doInBackground(String... arg) {
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
            String response = null;

            if (jsonStr.equals("\"0\"")) {
                response = "Your credentials don't match!";
            }
            else {
                response = jsonStr;
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("userId", jsonStr.replaceAll("^\"|\"$", ""));
                editor.commit();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            // result is the user's id
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
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

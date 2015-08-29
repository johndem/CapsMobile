package csd.jt.capsmobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterVolActivity extends Activity {

    private ProgressDialog pDialog;
    private EditText firstname, lastname, username, password, cpassword, email, birthday;
    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vol);

        firstname = (EditText) findViewById(R.id.firstnameEt);
        lastname = (EditText) findViewById(R.id.lastnameEt);
        username = (EditText) findViewById(R.id.usernameEt);
        password = (EditText) findViewById(R.id.passwordEt);
        cpassword = (EditText) findViewById(R.id.cPasswordEt);
        email = (EditText) findViewById(R.id.emailEt);
        birthday = (EditText) findViewById(R.id.birthdayEt);
        regBtn = (Button) findViewById(R.id.registerBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String cpass = cpassword.getText().toString();
                String e = email.getText().toString();
                String b = birthday.getText().toString();

                if (fname.equals("") || lname.equals("") || user.equals("") || pass.equals("") || cpass.equals("") || e.equals("") || b.equals(""))
                    Toast.makeText(RegisterVolActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                else if (pass.length() < 10) {
                    Toast.makeText(RegisterVolActivity.this, "Password must be at least 10 characters long!", Toast.LENGTH_SHORT).show();
                }
                else if (!pass.equals(cpass)) {
                    Log.d("Password: ", "> " + pass);
                    Log.d("C Password: ", "> " + cpass);
                    Toast.makeText(RegisterVolActivity.this, "Password fields don't match!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Calling async task to get json
                    new GetData().execute(fname, lname, user, pass, cpass, e, b);
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
            pDialog = new ProgressDialog(RegisterVolActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... arg) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            String url = "http://10.0.2.2/android/register-vol.php";
            HashMap<String, String> params = new HashMap<>();
            String fname = arg[0];
            String lname = arg[1];
            String user = arg[2];
            String pass = arg[3];
            String cpass = arg[4];
            String e = arg[5];
            String b = arg[6];
            params.put("firstname", fname);
            params.put("lastname", lname);
            params.put("username", user);
            params.put("password", pass);
            params.put("cpassword", cpass);
            params.put("email", e);
            params.put("birthday", b);

            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);
            String response = null;

            if (jsonStr.equals("\"0\"")) {
                response = "There was a problem signing up!";
            }
            else if (jsonStr.equals("\"-1\"")) {
                response = "Username already in use!";
            }
            else if (jsonStr.equals("\"-2\"")) {
                response = "Email already in use!";
            }
            else {
                response = "Account successfully created!";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            // result is the user's id
            Toast.makeText(RegisterVolActivity.this, result, Toast.LENGTH_SHORT).show();
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

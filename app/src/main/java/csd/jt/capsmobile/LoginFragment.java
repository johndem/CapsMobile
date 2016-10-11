package csd.jt.capsmobile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private ProgressDialog pDialog;
    private EditText username, password;
    private Button logBtn;
    SharedPreferences sharedpreferences;

    private String uri = "http://idematis.webpages.auth.gr";


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);


        username = (EditText) rootView.findViewById(R.id.usernameEt);
        password = (EditText) rootView.findViewById(R.id.passEt);
        logBtn = (Button) rootView.findViewById(R.id.loginBtn);

        sharedpreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.equals("") || pass.equals(""))
                    Toast.makeText(getActivity(), "Παρακαλούμε συμπληρώστε και τα δύο πεδία!", Toast.LENGTH_SHORT).show();
                else {
                    // Calling async task to get json
                    new LoginFragment.GetData().execute(user, pass);
                }
            }
        });


        return rootView;
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Παρακαλούμε περιμένετε...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... arg) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            String url = uri + "/CAPS/android/verify-user.php";
            HashMap<String, String> params = new HashMap<>();
            String user = arg[0];
            String pass = arg[1];
            params.put("username", user);
            params.put("password", pass);

            Log.d("Response: ", "> " + user + " " + pass);

            String jsonStr = sh.makeServiceCall(url, params);

            Log.d("Response: ", "> " + jsonStr);
            String response = null;
            String userId = null, userRole = null;

            try {
                JSONObject jObj = new JSONObject(jsonStr);
                JSONArray jArray = jObj.getJSONArray("log-state");
                userId = jArray.getString(0);
                userRole = jArray.getString(1);
                Log.d("Id is: ", "> " + userId + " and Role is: " + userRole);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (userId.equals("0")) {
                response = "Τα στοιχεία σας δεν είναι σωστά!";
            }
            else {
                response = "Ok";

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("userId", userId);
                editor.putString("userRole", userRole);
                editor.commit();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.equals("Ok")) {
//                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
            else
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }

}

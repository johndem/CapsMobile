package csd.jt.capsmobile;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private ProgressDialog pDialog;
    private EditText firstname, lastname, username, password, cpassword, email, birthday;
    private Button regBtn;

    private String uri = "http://idematis.webpages.auth.gr";


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);


        firstname = (EditText) rootView.findViewById(R.id.firstnameEt);
        lastname = (EditText) rootView.findViewById(R.id.lastnameEt);
        username = (EditText) rootView.findViewById(R.id.usernameEt);
        password = (EditText) rootView.findViewById(R.id.passwordEt);
        cpassword = (EditText) rootView.findViewById(R.id.cPasswordEt);
        email = (EditText) rootView.findViewById(R.id.emailEt);
        birthday = (EditText) rootView.findViewById(R.id.birthdayEt);
        regBtn = (Button) rootView.findViewById(R.id.registerBtn);

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
                    Toast.makeText(getActivity(), "Παρακαλούμε συμπληρώστε όλα τα πεδία!", Toast.LENGTH_SHORT).show();
                else if (pass.length() < 10) {
                    Toast.makeText(getActivity(), "Ο κωδικός πρέπει να είναι τουλάχιστον 10 χαρακτήρες!", Toast.LENGTH_SHORT).show();
                }
                else if (!pass.equals(cpass)) {
                    Log.d("Password: ", "> " + pass);
                    Log.d("C Password: ", "> " + cpass);
                    Toast.makeText(getActivity(), "Τα πεδία password δεν ταιριάζουν!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Calling async task to get json
                    new RegisterFragment.GetData().execute(fname, lname, user, pass, cpass, e, b);
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

            String url = uri + "/CAPS/android/register-vol.php";
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
                response = "Υπήρξε πρόβλημα κατά την εγγραφή!";
            }
            else if (jsonStr.equals("\"-1\"")) {
                response = "Αυτο username δεν είναι διαθέσιμο!";
            }
            else if (jsonStr.equals("\"-2\"")) {
                response = "Αυτό το email είναι ήδη σε χρήση!";
            }
            else {
                response = "Ο λογαριασμός σας δημιουργήθηκε επιτυχώς!";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            // result is the user's id
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }


}

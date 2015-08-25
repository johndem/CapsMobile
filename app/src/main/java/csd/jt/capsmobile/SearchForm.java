package csd.jt.capsmobile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SearchForm extends Activity {

    private static final String TAG_TITLE = "title";
    private static final String TAG_RESULTS = "results";


    public Activity act = this;
    JSONArray active = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);

        new GetData("http://10.0.3.2/CAPS/android/get-categories.php",(Spinner) findViewById(R.id.spCategory)).execute();
        new GetData("http://10.0.3.2/CAPS/android/get-areas.php", (Spinner) findViewById(R.id.spArea)).execute();
        new GetData("http://10.0.3.2/CAPS/android/get-agegroups.php",(Spinner) findViewById(R.id.spAgegroup)).execute();
        new GetData("http://10.0.3.2/CAPS/android/get-skills.php", (Spinner) findViewById(R.id.spSkills)).execute();




    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date = day + "/"+ (month+1) + "/" + year;
            TextView tv = (TextView) getActivity().findViewById(R.id.tvDateShow);
            tv.setText(date);
        }
    }


    private class GetData extends AsyncTask<Void, Void, Void> {
        ArrayList<String> dataList;
        private ProgressDialog pDialog;
        String url = "";
        Spinner spinner;

        public GetData(String url, Spinner spinner)
        {
            this.url = url;
            this.spinner = spinner;
            dataList = new ArrayList<>();
        }




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("tag1", "preEx");

            //Showing progress dialog
            pDialog = new ProgressDialog(act);
            pDialog.setMessage("Fetching data from database");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            Log.d("tag1", "Service");
            HashMap<String, String> params = new HashMap<>();
            params.put("category", "ha");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url,params);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    active = jsonObj.getJSONArray(TAG_RESULTS);

                    // looping through All Contacts
                    for (int i = 0; i < active.length(); i++) {
                        JSONObject c = active.getJSONObject(i);

                        String title = c.getString(TAG_TITLE);
                        Log.d("title", title);

                        // adding contact to contact list
                        dataList.add(title);
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
//            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(act,
                    android.R.layout.simple_spinner_item, dataList);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);



        }

    }

}
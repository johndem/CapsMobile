package csd.jt.capsmobile;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG_TITLE = "title";
    private static final String TAG_RESULTS = "results";
    private String uri = "http://idematis.webpages.auth.gr";
    public Activity act = getActivity();
    JSONArray active = null;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);


        new SearchFragment.GetData(uri + "/CAPS/android/get-categories.php",(Spinner) rootView.findViewById(R.id.spCategory)).execute();
        new SearchFragment.GetData(uri + "/CAPS/android/get-areas.php", (Spinner) rootView.findViewById(R.id.spArea)).execute();
        new SearchFragment.GetData(uri + "/CAPS/android/get-agegroups.php",(Spinner) rootView.findViewById(R.id.spAgegroup)).execute();
        new SearchFragment.GetData(uri + "/CAPS/android/get-skills.php", (Spinner) rootView.findViewById(R.id.spSkills)).execute();

        Button btnDate = (Button) rootView.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        Button btnSearch = (Button) rootView.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);

                Spinner spCategory = (Spinner) rootView.findViewById(R.id.spCategory);
                if (spCategory.getSelectedItemPosition() > 0) { intent.putExtra("category", (String) spCategory.getSelectedItem()); flag = true; }

                Spinner spArea = (Spinner) rootView.findViewById(R.id.spArea);
                if (spArea.getSelectedItemPosition() > 0) { intent.putExtra("area", (String) spArea.getSelectedItem()); flag = true; }

                Spinner spAgegroup = (Spinner) rootView.findViewById(R.id.spAgegroup);
                if (spAgegroup.getSelectedItemPosition() > 0) { intent.putExtra("agegroup", (String) spAgegroup.getSelectedItem()); flag = true; }

                Spinner spSkill = (Spinner) rootView.findViewById(R.id.spSkills);
                if (spSkill.getSelectedItemPosition() > 0) {  intent.putExtra("skill", (String) spSkill.getSelectedItem()); flag = true; }

                TextView tvDate = (TextView) rootView.findViewById(R.id.tvDateShow);
                if (!tvDate.getText().toString().isEmpty()) { intent.putExtra("date", tvDate.getText().toString()); flag =true; }

                if (flag == false) {
                    Toast.makeText(getActivity(), "Πρέπει να επιλέξετε τουλάχιστον ένα φίλτρο!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getActivity(), "Would start activity/10", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });


        return rootView;
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
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

        public GetData(String url, Spinner spinner) {
            this.url = url;
            this.spinner = spinner;
            dataList = new ArrayList<>();
            dataList.add("Επιλέξτε");
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d("tag1", "preEx");

            //Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching data from database");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            //Log.d("tag1", "Service");
            HashMap<String, String> params = new HashMap<>();
            params.put("category", "ha");

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, params);

            //Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    active = jsonObj.getJSONArray(TAG_RESULTS);

                    // looping through All Contacts
                    for (int i = 0; i < active.length(); i++) {
                        JSONObject c = active.getJSONObject(i);

                        String title = c.getString(TAG_TITLE);
                        //Log.d("title", title);

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

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, dataList);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);


        }

    }

}

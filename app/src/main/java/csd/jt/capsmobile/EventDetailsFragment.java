package csd.jt.capsmobile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EventDetailsFragment extends Fragment {

    private Button mapBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        String address = null, area = null, str = null, zip = null, date = null, time = null, agegroup = null, skills = null, body = null;
        if (extras != null) {
            address = extras.getString("address");
            area = extras.getString("area");
            str = extras.getString("street");
            zip = extras.getString("zipcode");
            date = extras.getString("day");
            time = extras.getString("time");
            agegroup = extras.getString("agegroup");
            skills = extras.getString("skills");
            body = extras.getString("ddesc");
        }
        TextView addressTv = (TextView) rootView.findViewById(R.id.eventAddressTv);
        addressTv.setText("Address: " + address + " " + str + ", " + area + ", " + zip);
        TextView timeTv = (TextView) rootView.findViewById(R.id.eventTimeTv);
        timeTv.setText("When: " + date + ", " + time);
        TextView ageTv = (TextView) rootView.findViewById(R.id.eventAgeTv);
        ageTv.setText("Agegroup: " + agegroup);
        TextView skillsTv = (TextView) rootView.findViewById(R.id.eventSkillsTv);
        skillsTv.setText("Skills: " + skills);
        TextView bodyTv = (TextView) rootView.findViewById(R.id.eventBodyTv);
        bodyTv.setText(body);

        final String mapAddress = address, mapStreet = str, mapZipcode = zip, mapArea = area;

        mapBtn = (Button) rootView.findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("address", mapAddress);
                intent.putExtra("street", mapStreet);
                intent.putExtra("zipcode", mapZipcode);
                intent.putExtra("area", mapArea);
                startActivity(intent);
            }
        });

        return rootView;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_event_details, menu);
//        return true;
//    }

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

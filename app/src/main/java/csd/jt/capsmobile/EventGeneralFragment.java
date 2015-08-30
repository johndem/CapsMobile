package csd.jt.capsmobile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class EventGeneralFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_general, container, false);


        Bundle extras = getActivity().getIntent().getExtras();
        String title = null, category = null, area = null, date = null, desc = null;
        if (extras != null) {
            title = extras.getString("title");
            category = extras.getString("category");
            area = extras.getString("area");
            date = extras.getString("day");
            desc = extras.getString("sdesc");
        }
        TextView titleTv = (TextView) rootView.findViewById(R.id.eventTitleTv);
        titleTv.setText(title);
        TextView catTv = (TextView) rootView.findViewById(R.id.eventCatTv);
        catTv.setText(category);
        TextView areaTv = (TextView) rootView.findViewById(R.id.eventAreaTv);
        areaTv.setText(area);
        TextView dateTv = (TextView) rootView.findViewById(R.id.eventDateTv);
        dateTv.setText(date);
        TextView descTv = (TextView) rootView.findViewById(R.id.eventSdescTv);
        descTv.setText(desc);

        return rootView;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_event_general, menu);
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

package csd.jt.capsmobile;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class EventActivity extends FragmentActivity {

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    Activity act = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
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

    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "General Information";
            else return "Details";
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources

            View view;

            if (position == 0) {
                view = act.getLayoutInflater().inflate(R.layout.fragment_event_general,
                        container, false);

                Bundle extras = act.getIntent().getExtras();
                String title = null, category = null, area = null, date = null, desc = null;
                if (extras != null) {
                    title = extras.getString("title");
                    category = extras.getString("category");
                    area = extras.getString("area");
                    date = extras.getString("day");
                    desc = extras.getString("sdesc");
                }
                TextView titleTv = (TextView) view.findViewById(R.id.eventTitleTv);
                titleTv.setText(title);
                TextView catTv = (TextView) view.findViewById(R.id.eventCatTv);
                catTv.setText(category);
                TextView areaTv = (TextView) view.findViewById(R.id.eventAreaTv);
                areaTv.setText(area);
                TextView dateTv = (TextView) view.findViewById(R.id.eventDateTv);
                dateTv.setText(date);
                TextView descTv = (TextView) view.findViewById(R.id.eventSdescTv);
                descTv.setText(desc);
            }
            else {
                view  = act.getLayoutInflater().inflate(R.layout.fragment_event_details,
                        container, false);

                Bundle extras = act.getIntent().getExtras();
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
                TextView addressTv = (TextView) view.findViewById(R.id.eventAddressTv);
                addressTv.setText(address + " " + str + ", " + area + ", " + zip);
                TextView timeTv = (TextView) view.findViewById(R.id.eventTimeTv);
                timeTv.setText(date + ", " + time);
                TextView ageTv = (TextView) view.findViewById(R.id.eventAgeTv);
                ageTv.setText(agegroup);
                TextView skillsTv = (TextView) view.findViewById(R.id.eventSkillsTv);
                skillsTv.setText(skills);
                TextView bodyTv = (TextView) view.findViewById(R.id.eventBodyTv);
                bodyTv.setText(body);

                final String mapAddress = address, mapStreet = str, mapZipcode = zip, mapArea = area;

                Button mapBtn;
                mapBtn = (Button) view.findViewById(R.id.mapBtn);
                mapBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                        Intent intent = new Intent(act, MapsActivity.class);
                        intent.putExtra("address", mapAddress);
                        intent.putExtra("street", mapStreet);
                        intent.putExtra("zipcode", mapZipcode);
                        intent.putExtra("area", mapArea);
                        startActivity(intent);
                    }
                });
            }
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}


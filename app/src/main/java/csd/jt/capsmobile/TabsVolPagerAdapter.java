package csd.jt.capsmobile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by John on 20/7/2015.
 */
public class TabsVolPagerAdapter extends FragmentPagerAdapter {

    public TabsVolPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new VolProfileFragment();
            case 1:
                // Games fragment activity
                return new VolActiveEventsFragment();
            case 2:
                // Games fragment activity
                return new VolCompletedEventsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}

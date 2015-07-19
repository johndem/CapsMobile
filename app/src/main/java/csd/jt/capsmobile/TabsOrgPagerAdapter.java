package csd.jt.capsmobile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by John on 19/7/2015.
 */
public class TabsOrgPagerAdapter extends FragmentPagerAdapter {

    public TabsOrgPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ActiveEventsFragment();
            case 1:
                // Games fragment activity
                return new CompletedEventsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}

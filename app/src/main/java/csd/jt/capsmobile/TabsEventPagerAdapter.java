package csd.jt.capsmobile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by John on 23/7/2015.
 */
public class TabsEventPagerAdapter extends FragmentPagerAdapter {

    public TabsEventPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new EventGeneralFragment();
            case 1:
                // Games fragment activity
                return new EventDetailsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}

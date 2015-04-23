package me.echeung.cdflabs.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.LabsFragment;
import me.echeung.cdflabs.fragments.LocationsFragment;
import me.echeung.cdflabs.fragments.PrintersFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static LabsFragment labsFragment;
    private static PrintersFragment printersFragment;
    private static LocationsFragment locationsFragment;

    private String[] TITLES;

    public ViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.TITLES = new String[]{
                mContext.getString(R.string.title_labs),
                mContext.getString(R.string.title_printers),
                mContext.getString(R.string.title_locations)
        };
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LabsFragment.newInstance(1);
            case 1:
                return PrintersFragment.newInstance(2);
            case 2:
                return LocationsFragment.newInstance(3);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        // Save fragment references depending on position
        switch (position) {
            case 0:
                labsFragment = (LabsFragment) createdFragment;
                break;
            case 1:
                printersFragment = (PrintersFragment) createdFragment;
                break;
            case 2:
                locationsFragment = (LocationsFragment) createdFragment;
                break;
        }
        return createdFragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    public static LabsFragment getLabsFragment() {
        return labsFragment;
    }

    public static PrintersFragment getPrintersFragment() {
        return printersFragment;
    }

    public static LocationsFragment getLocationsFragment() {
        return locationsFragment;
    }
}

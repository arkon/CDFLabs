package me.echeung.cdflabs.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.ui.fragments.LabsFragment;
import me.echeung.cdflabs.ui.fragments.LocationsFragment;
import me.echeung.cdflabs.ui.fragments.PrintersFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static LabsFragment labsFragment;
    private static PrintersFragment printersFragment;
    private static LocationsFragment locationsFragment;

    private String[] TITLES;

    final private int[] ICONS = new int[]{
            R.drawable.ic_desktop,
            R.drawable.ic_print,
            R.drawable.ic_explore
    };

    public ViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);

        this.TITLES = new String[]{
                mContext.getString(R.string.title_labs),
                mContext.getString(R.string.title_printers),
                mContext.getString(R.string.title_locations)
        };
    }

    public static LabsFragment getLabsFragment() {
        return labsFragment;
    }

    public static PrintersFragment getPrintersFragment() {
        return printersFragment;
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
        final Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

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
        return null;
    }

    /**
     * Gets the tab's title.
     *
     * @param position The tab index.
     * @return The tab's title.
     */
    public CharSequence getTitle(int position) {
        return TITLES[position];
    }

    /**
     * Gets the tab's icon.
     *
     * @param position The tab index.
     * @return The resource ID for the tab's icon.
     */
    public int getIcon(int position) {
        return ICONS[position];
    }
}

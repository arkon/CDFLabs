package me.echeung.cdflabs.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TabFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of the fragment for the given section number.
     */
    public static Fragment newInstance(int sectionNumber, Fragment fragment) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}

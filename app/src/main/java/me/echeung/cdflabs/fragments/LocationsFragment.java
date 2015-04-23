package me.echeung.cdflabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.base.TabFragment;

public class LocationsFragment extends Fragment {

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new LocationsFragment());
    }

    public LocationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locations, container, false);
    }
}

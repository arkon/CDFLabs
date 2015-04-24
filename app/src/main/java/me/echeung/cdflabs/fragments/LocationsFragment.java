package me.echeung.cdflabs.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.base.TabFragment;

public class LocationsFragment extends Fragment {

    private static final String BAHEN_MAP_ADDRESS =
            "https://www.google.com/maps/place/Bahen+Centre+for+Information+Technology/@43.659489,-79.397613,17z/data=!3m1!4b1!4m2!3m1!1s0x882b34c75165c957:0x6459384147b4b67b?hl=en";

    private static final String NX_ADDRESS =
            "http://www.cdf.utoronto.ca/using_cdf/remote_access_server.html";

    public LocationsFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new LocationsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        // Google Maps for Bahen address
        final LinearLayout bahenAddress = (LinearLayout) rootView.findViewById(R.id.bahen_address);
        bahenAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(BAHEN_MAP_ADDRESS);
            }
        });

        // Webpage for NX
        final Button nxMore = (Button) rootView.findViewById(R.id.more_nx);
        nxMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLink(NX_ADDRESS);
            }
        });

        return rootView;
    }

    private void openLink(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        startActivity(intent);
    }
}

package me.echeung.cdflabs.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import me.echeung.cdflabs.R;

public class LocBahenFragment extends Fragment {

    private LinearLayout address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loc_bahen, container, false);

        // Google Maps for address
        address = (LinearLayout) rootView.findViewById(R.id.bahen_address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.bahen_web)));
                startActivity(intent);
            }
        });

        return rootView;
    }
}

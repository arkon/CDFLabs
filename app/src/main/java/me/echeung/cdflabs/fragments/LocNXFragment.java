package me.echeung.cdflabs.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.echeung.cdflabs.R;

public class LocNXFragment extends Fragment {

    private Button more;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loc_nx, container, false);

        more = (Button) rootView.findViewById(R.id.more_nx);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getString(R.string.nx_web)));
                startActivity(intent);
            }
        });

        return rootView;
    }
}

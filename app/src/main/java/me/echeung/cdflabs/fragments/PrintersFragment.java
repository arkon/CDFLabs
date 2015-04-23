package me.echeung.cdflabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.base.TabFragment;

public class PrintersFragment extends TabFragment {

    private View rootView;

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new PrintersFragment());
    }

    public PrintersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_printers, container, false);

        return rootView;
    }

    public void updateText(String text) {
        ((TextView) rootView.findViewById(R.id.printers_text)).setText(text);
    }
}

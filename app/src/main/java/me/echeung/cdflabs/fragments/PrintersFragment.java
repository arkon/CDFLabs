package me.echeung.cdflabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.base.TabFragment;
import me.echeung.cdflabs.printers.Printer;

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

    public void updateText(Map<String, Printer> printers) {
        ((TextView) rootView.findViewById(R.id.printer_2210a_text)).setText(printers.get("2210a").toString());
        ((TextView) rootView.findViewById(R.id.printer_2210b_text)).setText(printers.get("2210b").toString());
        ((TextView) rootView.findViewById(R.id.printer_3185_text)).setText(printers.get("3185a").toString());
    }
}

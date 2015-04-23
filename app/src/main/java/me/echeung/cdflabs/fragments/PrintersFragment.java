package me.echeung.cdflabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.base.TabFragment;
import me.echeung.cdflabs.printers.Printer;
import me.echeung.cdflabs.utils.LabDataScraper;
import me.echeung.cdflabs.utils.NetworkUtils;
import me.echeung.cdflabs.utils.PrinterDataScraper;

public class PrintersFragment extends TabFragment {

    private View rootView;

    private SwipeRefreshLayout mPullToRefresh;
    private ProgressBar mProgress;
    private LinearLayout mEmpty;

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new PrintersFragment());
    }

    public PrintersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_printers, container, false);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress);
        mEmpty = (LinearLayout) rootView.findViewById(R.id.no_connection);

        // Pull to refresh
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.printers_container);
        mPullToRefresh.setColorSchemeResources(R.color.colorPrimary);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        fetchData();

        return rootView;
    }

    public void fetchData() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            // No network connection: show retry button
            mEmpty.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
            mPullToRefresh.setRefreshing(false);
        } else {
            mEmpty.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);

            new PrinterDataScraper().execute();
        }
    }

    public void updateLists(Map<String, Printer> printers) {
        // Hide progress spinner, and show the list
        mProgress.setVisibility(View.GONE);

        // Complete pull to refresh
        mPullToRefresh.setRefreshing(false);

        ((TextView) rootView.findViewById(R.id.printer_2210a_text)).setText(printers.get("2210a").toString());
        ((TextView) rootView.findViewById(R.id.printer_2210b_text)).setText(printers.get("2210b").toString());
        ((TextView) rootView.findViewById(R.id.printer_3185_text)).setText(printers.get("3185a").toString());

        ((TextView) rootView.findViewById(R.id.timestamp)).setText(printers.get("2210a").getTimestamp());
    }
}

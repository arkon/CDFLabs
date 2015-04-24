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
import me.echeung.cdflabs.utils.NetworkUtils;
import me.echeung.cdflabs.utils.PrinterDataScraper;

public class PrintersFragment extends TabFragment {

    private View rootView;

    private SwipeRefreshLayout mPullToRefresh;
    private ProgressBar mProgress;
    private LinearLayout mEmpty;

    private TextView p2210a;
    private TextView p2210b;
    private TextView p3185;
    private TextView mTimestamp;

    public PrintersFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new PrintersFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_printers, container, false);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress);
        mEmpty = (LinearLayout) rootView.findViewById(R.id.no_connection);

        // Pull to refresh
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.printers_container);
        mPullToRefresh.setColorSchemeResources(R.color.colorAccent);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // TextViews
        p2210a = (TextView) rootView.findViewById(R.id.printer_2210a_text);
        p2210b = (TextView) rootView.findViewById(R.id.printer_2210b_text);
        p3185 = (TextView) rootView.findViewById(R.id.printer_3185_text);
        mTimestamp = (TextView) rootView.findViewById(R.id.timestamp);

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

        p2210a.setText(printers.get("2210a").toString());
        p2210b.setText(printers.get("2210b").toString());
        p3185.setText(printers.get("3185a").toString());

        mTimestamp.setText(String.format(getString(R.string.timestamp), printers.get("2210a").getTimestamp()));
    }
}

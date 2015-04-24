package me.echeung.cdflabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.fragments.base.TabFragment;
import me.echeung.cdflabs.printers.Printer;
import me.echeung.cdflabs.utils.NetworkUtils;
import me.echeung.cdflabs.utils.PrinterDataScraper;

public class PrintersFragment extends TabFragment {

    private SwipeRefreshLayout mPullToRefresh;
    private ProgressBar mProgress;
    private LinearLayout mEmpty;
    private RelativeLayout mPrintersView;
    private ScrollView mPrintersScroller;
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
        final View rootView = inflater.inflate(R.layout.fragment_printers, container, false);

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

        // Some references
        mPrintersView = (RelativeLayout) rootView.findViewById(R.id.printers_list);
        mPrintersScroller = (ScrollView) rootView.findViewById(R.id.printers_scroller);
        p2210a = (TextView) rootView.findViewById(R.id.printer_2210a_text);
        p2210b = (TextView) rootView.findViewById(R.id.printer_2210b_text);
        p3185 = (TextView) rootView.findViewById(R.id.printer_3185_text);
        mTimestamp = (TextView) rootView.findViewById(R.id.timestamp);

        // Disable pull to refresh unless at the top
        final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
                ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        mPullToRefresh.setEnabled(mPrintersScroller.getScrollY() == 0);
                    }
                };

        mPrintersScroller.setOnTouchListener(new View.OnTouchListener() {
            private ViewTreeObserver observer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (observer == null) {
                    observer = mPrintersScroller.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                } else if (!observer.isAlive()) {
                    observer.removeOnScrollChangedListener(onScrollChangedListener);
                    observer = mPrintersScroller.getViewTreeObserver();
                    observer.addOnScrollChangedListener(onScrollChangedListener);
                }

                return false;
            }
        });

        // No connection retry button
        Button mRetry = (Button) rootView.findViewById(R.id.btn_retry);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
                ViewPagerAdapter.getLabsFragment().fetchData();
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
            mPrintersView.setVisibility(View.GONE);
            mPullToRefresh.setRefreshing(false);
            mPullToRefresh.setEnabled(false);
        } else {
            mEmpty.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            mPullToRefresh.setEnabled(true);

            new PrinterDataScraper().execute();
        }
    }

    public void updateLists(Map<String, Printer> printers) {
        // Hide progress spinner, and show the list
        mProgress.setVisibility(View.GONE);
        mPrintersView.setVisibility(View.VISIBLE);

        // Complete pull to refresh
        mPullToRefresh.setRefreshing(false);

        p2210a.setText(printers.get("2210a").toString());
        p2210b.setText(printers.get("2210b").toString());
        p3185.setText(printers.get("3185a").toString());

        mTimestamp.setText(String.format(getString(R.string.timestamp), printers.get("2210a").getTimestamp()));
    }
}

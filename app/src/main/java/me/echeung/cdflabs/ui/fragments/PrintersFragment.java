package me.echeung.cdflabs.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.PrintersListAdapter;
import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.ui.fragments.base.TabFragment;
import me.echeung.cdflabs.utils.NetworkUtils;
import me.echeung.cdflabs.utils.PrinterDataScraper;

public class PrintersFragment extends TabFragment {

    private PrintersListAdapter adapter;

    public PrintersFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new PrintersFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_printers, container, false);

        // Pull to refresh
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.printers_container);

        // Some references
        mContent = (RelativeLayout) rootView.findViewById(R.id.printers_list);

        // List/adapter
        mList = (RecyclerView) rootView.findViewById(R.id.printers);
        adapter = new PrintersListAdapter(getActivity());
        mList.setAdapter(adapter);

        // No connection retry button
        Button mRetry = (Button) rootView.findViewById(R.id.btn_retry);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
                ViewPagerAdapter.getLabsFragment().fetchData();
            }
        });

        return initializeView(rootView);
    }

    @Override
    protected View initializeView(View rootView) {
        super.initializeView(rootView);
        fetchData();

        return rootView;
    }

    @Override
    public void fetchData() {
        super.fetchData();

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new PrinterDataScraper().execute();
        }
    }

    public void updateQueue(PrintQueue queue) {
        super.updateContent();

        adapter.setPrintQueue(queue);
    }
}

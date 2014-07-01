package me.echeung.cdflabs.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.DataScraper;
import me.echeung.cdflabs.adapters.LabsListAdapter;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.labs.LabsByAvail;
import me.echeung.cdflabs.labs.LabsByBuilding;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class LabsFragment extends Fragment {

    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView listLabs;
    private LabsListAdapter adapter;
    private RelativeLayout labsView;
    private ProgressBar progress;
    private LinearLayout empty;
    private Spinner sort;
    private TextView timestamp;

    private List<Lab> labs;

    private Boolean sortAvail = false;

    public LabsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_labs, container, false);

        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        empty    = (LinearLayout) rootView.findViewById(R.id.empty_list);

        if (!isNetworkAvailable()) {
            // No network connection: show retry button
            empty.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);

            return rootView;
        } else {
            // Get data from website
            DataScraper dataTask = new DataScraper(getActivity(), this);
            dataTask.execute();
        }

        labsView  = (RelativeLayout) rootView.findViewById(R.id.labs_list);
        listLabs  = (ListView) rootView.findViewById(R.id.labs);
        sort      = (Spinner) rootView.findViewById(R.id.sort);
        timestamp = (TextView) rootView.findViewById(R.id.timestamp);

        // Pull to refresh on labs list
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        if (!isNetworkAvailable()) {
                            // No network connection: show retry
                            empty.setVisibility(View.VISIBLE);
                            labsView.setVisibility(View.GONE);
                            mPullToRefreshLayout.setRefreshComplete();
                        } else {
                            // Get data from website
                            DataScraper dataTask = new DataScraper(getActivity(), LabsFragment.this);
                            dataTask.execute();
                        }
                    }
                })
                .setup(mPullToRefreshLayout);

        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {
                if (labs != null) {
                    if (position == 0) {
                        // Labs by building
                        Collections.sort(labs, new LabsByBuilding());
                        sortAvail = false;
                    } else {
                        // Labs by availability
                        Collections.sort(labs, new LabsByAvail());
                        sortAvail = true;
                    }
                    updateAdapter(labs);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        return rootView;
    }

    /** Update the adapter with the new list of labs. */
    public void updateAdapter(List<Lab> labs) {
        // Hide progress spinner, and show the list
        progress.setVisibility(View.GONE);
        labsView.setVisibility(View.VISIBLE);

        // Complete pull to refresh
        mPullToRefreshLayout.setRefreshComplete();

        // Sort according to what the user has selected
        if (sortAvail)
            Collections.sort(labs, new LabsByAvail());
        else
            Collections.sort(labs, new LabsByBuilding());

        // Set the list
        this.labs = labs;

        if (adapter == null) {
            // Initialize adapter for the list of labs
            adapter = new LabsListAdapter(getActivity(), labs);
            listLabs.setAdapter(adapter);
        } else {
            // Update with new list
            adapter.setLabs(labs);
            adapter.notifyDataSetChanged();
        }

        timestamp.setText(String.format(getString(R.string.timestamp), labs.get(0).getTimestamp()));
    }

    /** Checks if there's an Internet connection and returns true iff there is. */
    private boolean isNetworkAvailable() {
        if (getActivity() != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
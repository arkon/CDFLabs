package me.echeung.cdflabs.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.LabsListAdapter;
import me.echeung.cdflabs.fragments.base.TabFragment;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.labs.LabsByAvail;
import me.echeung.cdflabs.labs.LabsByBuilding;
import me.echeung.cdflabs.utils.DataScraper;
import me.echeung.cdflabs.utils.NetworkUtils;

public class LabsFragment extends TabFragment {

    private View rootView;

    private SwipeRefreshLayout mPullToRefresh;
    private RecyclerView mListLabs;
    private LabsListAdapter adapter;
    private RelativeLayout mLabsView;
    private ProgressBar mProgress;
    private LinearLayout mEmpty;
    private Spinner mSort;
    private TextView mTimestamp;

    private List<Lab> labs;

    private Boolean sortAvail = true;

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new LabsFragment());
    }

    public LabsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_labs, container, false);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress);
        mEmpty = (LinearLayout) rootView.findViewById(R.id.no_connection);

        // Pull to refresh
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.labs_container);
        mPullToRefresh.setColorSchemeResources(R.color.colorPrimary);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // Some references
        mLabsView = (RelativeLayout) rootView.findViewById(R.id.labs_list);
        mTimestamp = (TextView) rootView.findViewById(R.id.timestamp);
        mSort = (Spinner) rootView.findViewById(R.id.sort);

        // List
        mListLabs = (RecyclerView) rootView.findViewById(R.id.labs);
        mListLabs.setLayoutManager(new LinearLayoutManager(getActivity()));

        // No connection retry button
        Button mRetry = (Button) rootView.findViewById(R.id.btn_retry);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeView(rootView);
            }
        });

        return initializeView(rootView);
    }

    private void fetchData() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            // No network connection: show retry button
            mEmpty.setVisibility(View.VISIBLE);
            mLabsView.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mPullToRefresh.setRefreshing(false);
        } else {
            mEmpty.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);

            // Get lab/printer data
            new DataScraper().execute();
        }
    }

    private View initializeView(View rootView) {
        fetchData();

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            // The list of labs
            mListLabs.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int topRowVerticalPosition =
                            (mListLabs == null || mListLabs.getChildCount() == 0) ?
                                    0 : mListLabs.getChildAt(0).getTop();
                    mPullToRefresh.setEnabled(topRowVerticalPosition >= 0);
                }
            });

            mSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id) {
                    if (labs != null) {
                        if (position == 0) {
                            // Labs by availability
                            Collections.sort(labs, new LabsByAvail());
                            sortAvail = true;
                        } else {
                            // Labs by building
                            Collections.sort(labs, new LabsByBuilding());
                            sortAvail = false;
                        }
                        updateAdapter(labs);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }

        return rootView;
    }

    /**
     * Update the adapter with the new list of labs.
     *
     * @param labs The list of labs to display.
     */
    public void updateAdapter(List<Lab> labs) {
        // Hide progress spinner, and show the list
        mProgress.setVisibility(View.GONE);
        mLabsView.setVisibility(View.VISIBLE);

        // Complete pull to refresh
        mPullToRefresh.setRefreshing(false);

        // Sort according to what the user has selected
        if (sortAvail)
            Collections.sort(labs, new LabsByAvail());
        else
            Collections.sort(labs, new LabsByBuilding());

        // Set the list
        this.labs = labs;

        if (adapter == null) {
            // Initialize adapter for the list of labs
            adapter = new LabsListAdapter(getActivity());
            adapter.setLabs(labs);
            mListLabs.setAdapter(adapter);
        } else {
            // Update with new list
            adapter.setLabs(labs);
            adapter.notifyDataSetChanged();
        }

        mTimestamp.setText(String.format(getString(R.string.timestamp), labs.get(0).getTimestamp()));
    }
}

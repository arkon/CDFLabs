package me.echeung.cdflabs.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.Collections;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.LabsListAdapter;
import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.labs.LabsByAvail;
import me.echeung.cdflabs.labs.LabsByBuilding;
import me.echeung.cdflabs.ui.fragments.base.TabFragment;
import me.echeung.cdflabs.utils.LabDataScraper;
import me.echeung.cdflabs.utils.NetworkUtils;

public class LabsFragment extends TabFragment {

    private Spinner mSort;
    private LabsListAdapter adapter;
    private List<Lab> labs;
    private Boolean sortAvail = true;

    public LabsFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new LabsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_labs, container, false);

        // Some references
        mContent = (RelativeLayout) rootView.findViewById(R.id.labs_list);
        mSort = (Spinner) rootView.findViewById(R.id.sort);

        // List/adapter
        mList = (RecyclerView) rootView.findViewById(R.id.labs);
        adapter = new LabsListAdapter(getActivity());
        mList.setAdapter(adapter);

        // Pull to refresh
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.labs_container);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // No connection retry button
        Button mRetry = (Button) rootView.findViewById(R.id.btn_retry);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeView(rootView);
                ViewPagerAdapter.getPrintersFragment().fetchData();
            }
        });

        return initializeView(rootView);
    }

    @Override
    protected View initializeView(View rootView) {
        super.initializeView(rootView);
        fetchData();

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

        return rootView;
    }

    @Override
    public void fetchData() {
        super.fetchData();

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new LabDataScraper().execute();
        }
    }

    /**
     * Update the adapter with the new list of labs.
     *
     * @param labs The list of labs to display.
     */
    public void updateAdapter(List<Lab> labs) {
        super.updateContent();

        // Sort according to what the user has selected
        Collections.sort(labs, sortAvail ? new LabsByAvail() : new LabsByBuilding());

        // Set the list
        this.labs = labs;

        adapter.setLabs(labs);
        adapter.notifyDataSetChanged();
    }
}

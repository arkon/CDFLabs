package me.echeung.cdflabs.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.LabsListAdapter;
import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.ui.AppState;
import me.echeung.cdflabs.ui.fragments.base.TabFragment;
import me.echeung.cdflabs.utils.LabDataScraper;
import me.echeung.cdflabs.enums.LabSortEnum;
import me.echeung.cdflabs.utils.NetworkUtils;

public class LabsFragment extends TabFragment {

    private LabsListAdapter adapter;

    public LabsFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new LabsFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_labs, menu);

        switch (AppState.getLabSort()) {
            case LabSortEnum.NAME:
                menu.findItem(R.id.sortName).setChecked(true);
                break;

            case LabSortEnum.AVAIL:
            default:
                menu.findItem(R.id.sortAvail).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortAvail:
                setSortMode(LabSortEnum.AVAIL);
                return true;

            case R.id.sortName:
                setSortMode(LabSortEnum.NAME);
                return true;

            default:
                break;
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_labs, container, false);

        // Some references
        mContent = (RelativeLayout) rootView.findViewById(R.id.labs_list);

        // List/adapter
        mList = (RecyclerView) rootView.findViewById(R.id.labs);
        adapter = new LabsListAdapter(getActivity());
        mList.setAdapter(adapter);

        // Pull to refresh
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.labs_container);

        // No connection retry button
        final Button mRetry = (Button) rootView.findViewById(R.id.btn_retry);
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

        adapter.setLabs(labs);
    }

    private void setSortMode(int type) {
        AppState.setLabSort(type);
        adapter.updateSortingCriteria();
        getActivity().invalidateOptionsMenu();
    }
}

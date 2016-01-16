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
import android.widget.RelativeLayout;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.LabsListAdapter;
import me.echeung.cdflabs.enums.SortEnum;
import me.echeung.cdflabs.labs.Labs;
import me.echeung.cdflabs.ui.AppState;
import me.echeung.cdflabs.ui.fragments.base.TabFragment;
import me.echeung.cdflabs.utils.LabDataScraper;
import me.echeung.cdflabs.utils.NetworkUtils;

public class LabsFragment extends TabFragment {

    private LabsListAdapter adapter;

    public LabsFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        return TabFragment.newInstance(sectionNumber, new LabsFragment());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_labs, menu);

        switch (AppState.getLabSort()) {
            case SortEnum.AVAIL:
                menu.findItem(R.id.labsSortAvail).setChecked(true);
                break;

            case SortEnum.NAME:
                menu.findItem(R.id.labsSortName).setChecked(true);
                break;
        }

        menu.findItem(R.id.labsShowNX).setChecked(AppState.isNXVisible());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.labsSortAvail:
                setSortMode(SortEnum.AVAIL);
                break;

            case R.id.labsSortName:
                setSortMode(SortEnum.NAME);
                break;

            case R.id.labsShowNX:
                setNXVisibility(!item.isChecked());
                break;
        }

        getActivity().invalidateOptionsMenu();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_labs, container, false);

        // Some references
        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.labs_container);
        mContent = (RelativeLayout) rootView.findViewById(R.id.labs_list);

        // List/adapter
        mList = (RecyclerView) rootView.findViewById(R.id.labs);
        adapter = new LabsListAdapter(getActivity());
        mList.setAdapter(adapter);

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
     * @param labs The labs data to display.
     */
    public void updateAdapter(Labs labs) {
        super.updateContent();

        adapter.setLabs(labs);
    }

    /**
     * Sets the sorting mode, updating the options menu and list.
     *
     * @param type The sorting mode, which is from SortEnum.
     */
    private void setSortMode(int type) {
        AppState.setLabSort(type);

        adapter.updateSortingCriteria();
    }

    private void setNXVisibility(boolean visible) {
        AppState.setNXVisibility(visible);

        adapter.updateList();
    }
}

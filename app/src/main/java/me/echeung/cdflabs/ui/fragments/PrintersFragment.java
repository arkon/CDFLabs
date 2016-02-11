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

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.PrintersListAdapter;
import me.echeung.cdflabs.enums.SortEnum;
import me.echeung.cdflabs.printers.Printers;
import me.echeung.cdflabs.ui.AppState;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_printers, menu);

        switch (AppState.getPrinterSort()) {
            case SortEnum.AVAIL:
                menu.findItem(R.id.printersSortAvail).setChecked(true);
                break;

            case SortEnum.NAME:
                menu.findItem(R.id.printersSortName).setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.printersSortAvail:
                setSortMode(SortEnum.AVAIL);
                break;

            case R.id.printersSortName:
                setSortMode(SortEnum.NAME);
                break;
        }

        getActivity().invalidateOptionsMenu();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_printers, container, false);

        mPullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.printers_container);

        // List/adapter
        mList = (RecyclerView) rootView.findViewById(R.id.printers);
        adapter = new PrintersListAdapter(getActivity());
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
            new PrinterDataScraper().execute();
        }
    }

    /**
     * Updates the printer queue list.
     *
     * @param queue The queue information.
     */
    public void updateQueue(Printers queue) {
        super.updateContent();

        adapter.setPrintQueue(queue);
    }

    /**
     * Sets the sorting mode, updating the options menu and list.
     *
     * @param type The sorting mode, which is from SortEnum.
     */
    private void setSortMode(int type) {
        AppState.setPrinterSort(type);

        adapter.updateSortingCriteria();
    }
}

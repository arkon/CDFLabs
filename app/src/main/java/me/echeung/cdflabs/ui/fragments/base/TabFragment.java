package me.echeung.cdflabs.ui.fragments.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.utils.DividerItemDecoration;
import me.echeung.cdflabs.utils.NetworkUtils;

public abstract class TabFragment extends Fragment implements ITabFragment {

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private boolean loadedData;

    protected LinearLayout mError;
    protected TextView mErrorMsg;
    protected SwipeRefreshLayout mPullToRefresh;
    protected RelativeLayout mContent;
    protected RecyclerView mList;

    /**
     * Returns a new instance of the fragment for the given section number.
     *
     * @param sectionNumber The section number (corresponding to the tab).
     * @param fragment      The fragment.
     * @return The fragment with the section number in its arguments.
     */
    public static Fragment newInstance(int sectionNumber, Fragment fragment) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes common view references and prevent pull to refresh from
     * triggering if not at the top of the list.
     *
     * @param rootView The view.
     * @return The view.
     */
    protected View initializeView(View rootView) {
        mError = (LinearLayout) rootView.findViewById(R.id.error);
        mErrorMsg = (TextView) rootView.findViewById(R.id.error_msg);

        mPullToRefresh.setColorSchemeResources(R.color.colorAccent);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        mList.addItemDecoration(new DividerItemDecoration(getContext()));
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mList == null || mList.getChildCount() == 0) ?
                                0 : mList.getChildAt(0).getTop();
                mPullToRefresh.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        // No connection retry button
        final Button mRetry = (Button) rootView.findViewById(R.id.btn_retry);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAllData();
            }
        });

        loadedData = false;

        return rootView;
    }

    /**
     * Hides views appropriately and handles pull to refresh functionality
     * when attempting to fetch data. Displays retry message/button if
     * there's no network connection.
     */
    public void fetchData() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            if (loadedData) {
                showConnectionErrorSnackbar();
            } else {
                showConnectionError();
            }
        } else {
            mError.setVisibility(View.GONE);

            mPullToRefresh.setEnabled(true);
            mPullToRefresh.setRefreshing(true);
        }
    }

    /**
     * Fetches data in both the labs and printers tabs.
     */
    public void fetchAllData() {
        ViewPagerAdapter.getLabsFragment().fetchData();
        ViewPagerAdapter.getPrintersFragment().fetchData();
    }

    /**
     * Toggles visibility of views and handles pull to refresh when updating
     * content data.
     */
    public void updateContent() {
        // Note that we've gotten data successfully once
        loadedData = true;

        // Show the content
        mContent.setVisibility(View.VISIBLE);

        // Complete pull to refresh
        mPullToRefresh.setRefreshing(false);

        // Scroll list back to top
        mList.smoothScrollToPosition(0);
    }

    /**
     * Displays a connection error message in a snackbar. Used when data has already been
     * loaded before, instead of hiding that.
     */
    private void showConnectionErrorSnackbar() {
        mPullToRefresh.setRefreshing(false);

        Snackbar.make(mList, getString(R.string.error_connection_short), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchData();
                    }
                })
                .show();
    }

    /**
     * Displays the error message view and hides the content.
     * Also disables pull to refresh.
     */
    private void showError() {
        mError.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.GONE);

        mPullToRefresh.setRefreshing(false);
        mPullToRefresh.setEnabled(false);
    }

    /**
     * Shows a network connection error.
     */
    public void showConnectionError() {
        showError();

        if (isAdded()) {
            mErrorMsg.setText(getString(R.string.error_connection));
        }
    }

    /**
     * Shows a data fetching error.
     */
    public void showFetchError() {
        showError();

        if (isAdded()) {
            mErrorMsg.setText(getString(R.string.error_fetch));
        }
    }
}

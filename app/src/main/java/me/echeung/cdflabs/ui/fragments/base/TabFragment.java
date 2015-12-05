package me.echeung.cdflabs.ui.fragments.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.utils.NetworkUtils;

public abstract class TabFragment extends Fragment implements ITabFragment {

    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    protected LinearLayout mEmpty;
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
        mEmpty = (LinearLayout) rootView.findViewById(R.id.no_connection);

        mPullToRefresh.setColorSchemeResources(R.color.colorAccent);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

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

        return rootView;
    }

    /**
     * Hides views appropriately and handles pull to refresh functionality
     * when attempting to fetch data. Displays retry message/button if
     * there's no network connection.
     */
    public void fetchData() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
            mEmpty.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);

            mPullToRefresh.setRefreshing(false);
            mPullToRefresh.setEnabled(false);
        } else {
            mEmpty.setVisibility(View.GONE);

            mPullToRefresh.setRefreshing(true);
            mPullToRefresh.setEnabled(true);
        }
    }

    /**
     * Toggles visibility of views and handles pull to refresh when updating
     * content data.
     */
    public void updateContent() {
        // Show the content
        mContent.setVisibility(View.VISIBLE);

        // Complete pull to refresh
        mPullToRefresh.setRefreshing(false);

        // Scroll list back to top
        mList.smoothScrollToPosition(0);
    }
}

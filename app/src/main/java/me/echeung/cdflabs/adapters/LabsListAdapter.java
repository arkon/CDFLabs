package me.echeung.cdflabs.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.comparators.LabsByAvail;
import me.echeung.cdflabs.comparators.LabsByBuilding;
import me.echeung.cdflabs.holders.LabHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.ui.AppState;
import me.echeung.cdflabs.enums.LabSortEnum;

public class LabsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Some constants
    private static final int LAB = 0;
    private static final int TIMESTAMP = 1;

    private Activity mContext;
    private List<Lab> mLabs;
    private Comparator<Lab> mComparator;

    public LabsListAdapter(Activity context) {
        this.mContext = context;
        this.mLabs = new ArrayList<>();

        updateSortingCriteria();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == TIMESTAMP) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timestamp_item, parent, false);
            return new TimestampHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_list_item, parent, false);
            return new LabHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TIMESTAMP : LAB;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {
        if (index == getItemCount() - 1) {
            TimestampHolder timestampHolder = (TimestampHolder) holder;

            if (mLabs.size() > 0) {
                timestampHolder.timestampView.setText(String.format(mContext.getString(R.string.timestamp),
                        mLabs.get(0).getTimestamp()));
            } else {
                timestampHolder.timestampView.setText(mContext.getString(R.string.no_data));
            }
        } else {
            LabHolder labHolder = (LabHolder) holder;

            final Lab lab = mLabs.get(index);

            // Show available machines and set the square's background colour accordingly
            int avail = lab.getAvail();
            labHolder.freeView.setText(String.valueOf(avail));

            if (avail == 0)
                labHolder.compsView.setBackgroundColor(getColor(R.color.free_red));
            else if (avail <= 5)
                labHolder.compsView.setBackgroundColor(getColor(R.color.free_orange));
            else
                labHolder.compsView.setBackgroundColor(getColor(R.color.free_green));

            // Show lab name and stats
            labHolder.labView.setText(lab.getLab());
            labHolder.statsView.setText(String.format(mContext.getString(R.string.stats),
                    lab.getTotal(), lab.getPercent()));

        }
    }

    @Override
    public int getItemCount() {
        // Add 1 for the timestamp "footer"
        return mLabs.size() + 1;
    }

    public void setLabs(List<Lab> labs) {
        mLabs.clear();
        if (labs != null)
            mLabs.addAll(labs);

        sortLabs(labs);
    }

    public void updateSortingCriteria() {
        switch (AppState.getLabSort()) {
            case LabSortEnum.NAME:
                this.mComparator = new LabsByBuilding();
                break;
            case LabSortEnum.AVAIL:
            default:
                this.mComparator = new LabsByAvail();
                break;
        }

        sortLabs(mLabs);
    }

    private void sortLabs(List<Lab> labs) {
        Collections.sort(labs, this.mComparator);
        this.mLabs = labs;
        notifyDataSetChanged();
    }

    private int getColor(int name) {
        return ContextCompat.getColor(mContext, name);
    }
}

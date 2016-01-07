package me.echeung.cdflabs.adapters;

import android.content.Context;
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
import me.echeung.cdflabs.enums.LabSortEnum;
import me.echeung.cdflabs.enums.ListEnum;
import me.echeung.cdflabs.holders.LabHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.labs.Labs;
import me.echeung.cdflabs.ui.AppState;
import me.echeung.cdflabs.utils.ListItem;

public class LabsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Labs mLabsData;
    private List<ListItem> mLabs;
    private Comparator<Lab> mComparator;

    public LabsListAdapter(Context context) {
        this.mContext = context;
        this.mLabs = new ArrayList<>();

        updateSortingCriteria();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case ListEnum.ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.lab_item, parent, false);
                return new LabHolder(v);

            case ListEnum.TIMESTAMP:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.timestamp_item, parent, false);
                return new TimestampHolder(v);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mLabs.get(position).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {
        switch (mLabs.get(index).getType()) {
            case ListEnum.ITEM:
                final LabHolder labHolder = (LabHolder) holder;
                final Lab lab = (Lab) mLabs.get(index).getItem();

                // Show available machines and set the square's background colour accordingly
                final int avail = lab.getAvailable();
                labHolder.freeView.setText(String.valueOf(avail));

                if (avail == 0)
                    labHolder.compsView.setBackgroundColor(getColor(R.color.free_red));
                else if (avail <= 5)
                    labHolder.compsView.setBackgroundColor(getColor(R.color.free_orange));
                else
                    labHolder.compsView.setBackgroundColor(getColor(R.color.free_green));

                // Show lab name and stats
                labHolder.labView.setText(lab.getName());
                labHolder.statsView.setText(String.format(mContext.getString(R.string.stats),
                        lab.getTotal(), lab.getPercent()));
                break;

            case ListEnum.TIMESTAMP:
                final TimestampHolder timestampHolder = (TimestampHolder) holder;
                final String timestamp = (String) mLabs.get(index).getItem();

                timestampHolder.timestampView.setText(
                        String.format(mContext.getString(R.string.timestamp), timestamp));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mLabs.size();
    }

    public void setLabs(Labs labs) {
        if (labs == null) return;

        this.mLabsData = labs;

        sortLabs();
    }

    public void updateSortingCriteria() {
        switch (AppState.getLabSort()) {
            case LabSortEnum.NAME:
                this.mComparator = new LabsByBuilding();
                break;

            case LabSortEnum.AVAIL:
                this.mComparator = new LabsByAvail();
                break;
        }

        sortLabs();
    }

    private void sortLabs() {
        if (this.mLabsData == null) return;

        this.mLabs.clear();

        // Sort labs
        List<Lab> sortedLabs = this.mLabsData.getLabs();
        Collections.sort(sortedLabs, this.mComparator);

        for (final Lab lab : sortedLabs) {
            this.mLabs.add(new ListItem(ListEnum.ITEM, lab));
        }

        // Timestamp
        this.mLabs.add(new ListItem(ListEnum.TIMESTAMP, this.mLabsData.getTimestamp()));

        notifyDataSetChanged();
    }

    private int getColor(int name) {
        return ContextCompat.getColor(mContext, name);
    }
}

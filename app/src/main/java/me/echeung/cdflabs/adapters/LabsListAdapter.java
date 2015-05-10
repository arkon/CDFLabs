package me.echeung.cdflabs.adapters;

import android.app.Activity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Comparator;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.holders.LabHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.labs.LabsByAvail;
import me.echeung.cdflabs.labs.LabsByBuilding;
import me.echeung.cdflabs.utils.LabSortEnum;

public class LabsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Some constants
    private static final int LAB = 0;
    private static final int TIMESTAMP = 1;

    private Activity mContext;
    private SortedList<Lab> mLabs;
    private Comparator<Lab> mComparator;

    public LabsListAdapter(Activity context) {
        this.mContext = context;
        this.mComparator = new LabsByAvail();
        this.mLabs = new SortedList<>(Lab.class, new SortedListAdapterCallback<Lab>(this) {
            @Override
            public int compare(Lab t0, Lab t1) {
                return mComparator.compare(t0, t1);
            }

            @Override
            public boolean areContentsTheSame(Lab oldItem, Lab newItem) {
                return oldItem.getLab().equals(newItem.getLab());
            }

            @Override
            public boolean areItemsTheSame(Lab item1, Lab item2) {
                return item1 == item2;
            }
        });
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

            timestampHolder.timestampView.setText(String.format(mContext.getString(R.string.timestamp),
                    mLabs.get(0).getTimestamp()));
        } else {
            LabHolder labHolder = (LabHolder) holder;

            final Lab lab = mLabs.get(index);

            // Show available machines and set the square's background colour accordingly
            int avail = lab.getAvail();
            labHolder.freeView.setText(String.valueOf(avail));

            if (avail == 0)
                labHolder.compsView.setBackgroundColor(mContext.getResources().getColor(R.color.free_red));
            else if (avail <= 5)
                labHolder.compsView.setBackgroundColor(mContext.getResources().getColor(R.color.free_orange));
            else
                labHolder.compsView.setBackgroundColor(mContext.getResources().getColor(R.color.free_green));

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
        clearList();

        mLabs.beginBatchedUpdates();

        for (Lab lab : labs) {
            mLabs.add(lab);
        }

        mLabs.endBatchedUpdates();

        this.notifyDataSetChanged();
    }

    public void setSortingCriteria(int type) {
        switch (type) {
            case LabSortEnum.BUILDING:
                this.mComparator = new LabsByBuilding();
            case LabSortEnum.AVAIL:
            default:
                this.mComparator = new LabsByAvail();
        }

        this.notifyDataSetChanged();
    }

    private void clearList() {
        mLabs.beginBatchedUpdates();

        while (mLabs.size() > 0) {
            mLabs.remove(mLabs.get(0));
        }

        mLabs.endBatchedUpdates();
    }
}

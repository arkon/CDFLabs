package me.echeung.cdflabs.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.labs.Lab;

public class LabsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private List<Lab> mLabs;

    public LabsListAdapter(Activity context) {
        mContext = context;
        mLabs = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timestamp_item, parent, false);
            return new TimestampHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_list_item, parent, false);
            return new LabViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {
        if (index == getItemCount() - 1) {
            TimestampHolder timestampHolder = (TimestampHolder) holder;

            timestampHolder.timestampView.setText(String.format(mContext.getString(R.string.timestamp),
                    mLabs.get(0).getTimestamp()));
        } else {
            LabViewHolder labHolder = (LabViewHolder) holder;

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
        return mLabs.size() + 1;
    }

    public void setLabs(List<Lab> labs) {
        this.mLabs = labs;
    }

    public static class LabViewHolder extends RecyclerView.ViewHolder {
        View view;
        LinearLayout compsView;
        TextView freeView;
        TextView labView;
        TextView statsView;

        public LabViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            compsView = (LinearLayout) itemView.findViewById(R.id.comps);
            freeView = (TextView) itemView.findViewById(R.id.free);
            labView = (TextView) itemView.findViewById(R.id.lab);
            statsView = (TextView) itemView.findViewById(R.id.stats);
        }
    }

    public static class TimestampHolder extends RecyclerView.ViewHolder {
        View view;
        TextView timestampView;

        public TimestampHolder(View itemView) {
            super(itemView);
            view = itemView;
            timestampView = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }
}

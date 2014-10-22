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

public class LabsListAdapter extends RecyclerView.Adapter<LabsListAdapter.LabViewHolder> {

    private Activity mContext;
    private List<Lab> mLabs;

    public LabsListAdapter(Activity context) {
        mContext = context;
        mLabs = new ArrayList<Lab>();
    }

    @Override
    public LabViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_list_item, parent, false);
        return new LabViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LabViewHolder holder, int index) {
        final Lab lab = mLabs.get(index);

        // Show available machines and set the square's background colour accordingly
        int avail =lab.getAvail();
        holder.freeView.setText(String.valueOf(avail));

        if (avail == 0)
            holder.compsView.setBackgroundColor(mContext.getResources().getColor(R.color.free_red));
        else if (avail <= 5)
            holder.compsView.setBackgroundColor(mContext.getResources().getColor(R.color.free_orange));
        else
            holder.compsView.setBackgroundColor(mContext.getResources().getColor(R.color.free_green));

        // Show lab name and stats
        holder.labView.setText(lab.getLab());
        holder.statsView.setText(String.format(mContext.getString(R.string.stats),
                lab.getTotal(), lab.getPercent()));
    }

    @Override
    public int getItemCount() {
        return mLabs.size();
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
            freeView  = (TextView) itemView.findViewById(R.id.free);
            labView   = (TextView) itemView.findViewById(R.id.lab);
            statsView = (TextView) itemView.findViewById(R.id.stats);
        }
    }

    public void setLabs(List<Lab> labs) {
        this.mLabs = labs;
    }
}

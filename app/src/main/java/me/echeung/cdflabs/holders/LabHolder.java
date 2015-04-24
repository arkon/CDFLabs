package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class LabHolder extends RecyclerView.ViewHolder {
    public LinearLayout compsView;
    public TextView freeView;
    public TextView labView;
    public TextView statsView;

    public LabHolder(View itemView) {
        super(itemView);

        compsView = (LinearLayout) itemView.findViewById(R.id.comps);
        freeView = (TextView) itemView.findViewById(R.id.free);
        labView = (TextView) itemView.findViewById(R.id.lab);
        statsView = (TextView) itemView.findViewById(R.id.stats);
    }
}
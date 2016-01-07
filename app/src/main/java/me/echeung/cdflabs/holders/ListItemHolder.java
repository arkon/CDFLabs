package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class ListItemHolder extends RecyclerView.ViewHolder {

    public LinearLayout statusView;
    public TextView statusNumView;
    public TextView statusAdjView;
    public TextView titleView;
    public TextView subtitleView;

    public ListItemHolder(View itemView) {
        super(itemView);

        statusView = (LinearLayout) itemView.findViewById(R.id.status);
        statusNumView = (TextView) itemView.findViewById(R.id.status_number);
        statusAdjView = (TextView) itemView.findViewById(R.id.status_adj);
        titleView = (TextView) itemView.findViewById(R.id.item_title);
        subtitleView = (TextView) itemView.findViewById(R.id.item_subtitle);
    }
}
package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class TimestampHolder extends RecyclerView.ViewHolder {
    public TextView timestampView;

    public TimestampHolder(View itemView) {
        super(itemView);

        timestampView = (TextView) itemView.findViewById(R.id.timestamp);
    }
}
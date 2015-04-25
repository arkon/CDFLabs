package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class PrinterHeadingHolder extends RecyclerView.ViewHolder {

    public TextView headingView;

    public PrinterHeadingHolder(View itemView) {
        super(itemView);

        headingView = (TextView) itemView.findViewById(R.id.printer_heading);
    }
}
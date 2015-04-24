package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class PrinterHolder extends RecyclerView.ViewHolder {
    public TextView queueView;

    public PrinterHolder(View itemView) {
        super(itemView);

        queueView = (TextView) itemView.findViewById(R.id.printer_text);
    }
}
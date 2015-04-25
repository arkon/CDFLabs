package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class PrinterJobHolder extends RecyclerView.ViewHolder {

    public TextView ownerView;
    public TextView filesView;
    public TextView infoView;

    public PrinterJobHolder(View itemView) {
        super(itemView);

        ownerView = (TextView) itemView.findViewById(R.id.print_job_owner);
        filesView = (TextView) itemView.findViewById(R.id.print_job_files);
        infoView = (TextView) itemView.findViewById(R.id.print_job_info);
    }
}
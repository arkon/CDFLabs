package me.echeung.cdflabs.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.echeung.cdflabs.R;

public class PrinterHolder extends RecyclerView.ViewHolder {

    public TextView ownerView;
    public TextView sizeView;
    public TextView filesView;
    public TextView numberView;
    public TextView rankView;
    public TextView timeView;

    public PrinterHolder(View itemView) {
        super(itemView);

        ownerView = (TextView) itemView.findViewById(R.id.print_job_owner);
        sizeView = (TextView) itemView.findViewById(R.id.print_job_size);
        filesView = (TextView) itemView.findViewById(R.id.print_job_files);
        numberView = (TextView) itemView.findViewById(R.id.print_job_number);
        rankView = (TextView) itemView.findViewById(R.id.print_job_rank);
        timeView = (TextView) itemView.findViewById(R.id.print_job_time);
    }
}
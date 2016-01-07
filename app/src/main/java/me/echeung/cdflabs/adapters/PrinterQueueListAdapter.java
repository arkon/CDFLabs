package me.echeung.cdflabs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.holders.PrintJobHolder;
import me.echeung.cdflabs.printers.PrintJob;

public class PrinterQueueListAdapter extends ArrayAdapter<PrintJob> {

    public PrinterQueueListAdapter(Context context, int resId) {
        super(context, resId);
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        if (itemView == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            itemView = vi.inflate(R.layout.printer_list_item, null);
        }

        final PrintJobHolder holder = new PrintJobHolder(itemView);
        final PrintJob job = getItem(position);

//        v = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.printer_empty_item, parent, false);

        holder.ownerView.setText(job.getOwner());

        if (job.getRaw().contains("ERROR")) {
            holder.filesView.setText(job.getRaw());
        } else {
            holder.filesView.setText(
                    String.format(getContext().getString(R.string.print_files),
                            job.getFiles(), job.getSize()));
            holder.infoView.setText(
                    String.format(getContext().getString(R.string.print_info),
                            job.getRank(), job.getJob(), job.getTime()));
        }

        return itemView;
    }
}

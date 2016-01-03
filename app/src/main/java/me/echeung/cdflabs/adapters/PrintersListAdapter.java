package me.echeung.cdflabs.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.enums.PrintersListEnum;
import me.echeung.cdflabs.holders.PrinterEmptyHolder;
import me.echeung.cdflabs.holders.PrinterHeadingHolder;
import me.echeung.cdflabs.holders.PrinterJobHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.printers.PrintJob;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;
import me.echeung.cdflabs.printers.PrintersListHeading;
import me.echeung.cdflabs.printers.PrintersListItem;

public class PrintersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private List<PrintersListItem> mQueue;

    public PrintersListAdapter(Activity context) {
        this.mContext = context;
        this.mQueue = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case PrintersListEnum.HEADING:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.printer_heading_item, parent, false);
                return new PrinterHeadingHolder(v);

            case PrintersListEnum.JOB:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.printer_list_item, parent, false);
                return new PrinterJobHolder(v);

            case PrintersListEnum.EMPTY:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.printer_empty_item, parent, false);
                return new PrinterEmptyHolder(v);

            case PrintersListEnum.TIMESTAMP:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.timestamp_item, parent, false);
                return new TimestampHolder(v);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mQueue.get(position).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {
        switch (mQueue.get(index).getType()) {
            case PrintersListEnum.HEADING:
                final PrinterHeadingHolder headingHolder = (PrinterHeadingHolder) holder;
                final PrintersListHeading heading = (PrintersListHeading) mQueue.get(index).getItem();

                headingHolder.headingView.setText(heading.getName());
                headingHolder.descriptionView.setText(heading.getDescription());
                break;

            case PrintersListEnum.JOB:
                final PrinterJobHolder jobHolder = (PrinterJobHolder) holder;
                final PrintJob job = (PrintJob) mQueue.get(index).getItem();

                jobHolder.ownerView.setText(job.getOwner());

                if (job.getRaw().contains("ERROR")) {
                    jobHolder.filesView.setText(job.getRaw());
                } else {
                    jobHolder.filesView.setText(
                            String.format(mContext.getString(R.string.print_files),
                                    job.getFiles(), job.getSize()));
                    jobHolder.infoView.setText(
                            String.format(mContext.getString(R.string.print_info),
                                    job.getRank(), job.getJob(), job.getTime()));
                }
                break;

            case PrintersListEnum.EMPTY:
                break;

            case PrintersListEnum.TIMESTAMP:
                final TimestampHolder timestampHolder = (TimestampHolder) holder;
                final String timestamp = (String) mQueue.get(index).getItem();

                timestampHolder.timestampView.setText(
                        String.format(mContext.getString(R.string.timestamp), timestamp));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mQueue.size();
    }

    public void setPrintQueue(PrintQueue queue) {
        if (queue == null) return;

        final List<String> printerNames = queue.getSortedKeys();
        final Map<String, Printer> printers = queue.getPrinters();

        this.mQueue.clear();

        for (final String key : printerNames) {
            final Printer printer = printers.get(key);

            // Printer heading
            this.mQueue.add(new PrintersListItem(PrintersListEnum.HEADING,
                    new PrintersListHeading(key, printer.getDescription())));

            if (printer.getLength() == 0) {
                // Empty job queue
                this.mQueue.add(new PrintersListItem(PrintersListEnum.EMPTY, null));
            } else {
                // Printer job queue
                for (PrintJob job : printer.getJobs()) {
                    this.mQueue.add(new PrintersListItem(PrintersListEnum.JOB, job));
                }
            }
        }

        // Timestamp
        this.mQueue.add(new PrintersListItem(PrintersListEnum.TIMESTAMP, queue.getTimestamp()));

        notifyDataSetChanged();
    }
}

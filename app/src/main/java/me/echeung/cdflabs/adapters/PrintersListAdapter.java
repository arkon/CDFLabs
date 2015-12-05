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
import me.echeung.cdflabs.holders.PrinterHeadingHolder;
import me.echeung.cdflabs.holders.PrinterJobHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.printers.PrintJob;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;

public class PrintersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Some constants
    private static final int JOB = 0;
    private static final int HEADING = 1;
    private static final int TIMESTAMP = 2;

    private Activity mContext;
    private String mTimestamp;
    private List<String> mPrinterNames;
    private List<String> mPrinterDescriptions;
    private List<PrintJob> mQueue;

    public PrintersListAdapter(Activity context) {
        this.mContext = context;
        this.mTimestamp = "";
        this.mPrinterNames = new ArrayList<>();
        this.mPrinterDescriptions = new ArrayList<>();
        this.mQueue = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case TIMESTAMP:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.timestamp_item, parent, false);
                return new TimestampHolder(v);
            case HEADING:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.printer_heading_item, parent, false);
                return new PrinterHeadingHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.printer_list_item, parent, false);
                return new PrinterJobHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TIMESTAMP;

        if (mQueue.get(position) == null)
            return HEADING;

        return JOB;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int index) {
        if (index == getItemCount() - 1) {
            TimestampHolder timestampHolder = (TimestampHolder) holder;

            timestampHolder.timestampView.setText(
                    String.format(mContext.getString(R.string.timestamp), mTimestamp));
        } else if (mQueue.get(index) == null) {
            PrinterHeadingHolder headingHolder = (PrinterHeadingHolder) holder;

            int headerIndex = getSectionHeaderIndex(index);

            headingHolder.headingView.setText(mPrinterNames.get(headerIndex));
            headingHolder.descriptionView.setText(mPrinterDescriptions.get(headerIndex));
        } else {
            PrinterJobHolder jobHolder = (PrinterJobHolder) holder;

            final PrintJob job = mQueue.get(index);

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
        }
    }

    @Override
    public int getItemCount() {
        // Add 1 for the timestamp "footer"
        return mQueue.size() + 1;
    }

    public void setPrintQueue(PrintQueue queue) {
        this.mTimestamp = queue.getTimestamp();
        this.mPrinterNames = queue.getSortedKeys();

        this.mQueue.clear();

        Map<String, Printer> printers = queue.getPrinters();

        for (final String key : this.mPrinterNames) {
            Printer printer = printers.get(key);

            this.mQueue.add(null);  // For the heading
            this.mPrinterDescriptions.add(printer.getDescription());
            this.mQueue.addAll(printer.getJobs());
        }

        notifyDataSetChanged();
    }

    private int getSectionHeaderIndex(int queueItemPosition) {
        int headerIndex = 0;

        for (int i = 0; i < queueItemPosition; i++) {
            if (this.mQueue.get(i) == null) {
               headerIndex++;
            }
        }

        return headerIndex;
    }
}

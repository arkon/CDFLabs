package me.echeung.cdflabs.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.holders.PrinterHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;

public class PrintersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Some constants
    private static final int JOB = 0;
    private static final int HEADING = 1;
    private static final int TIMESTAMP = 2;

    private Activity mContext;
    private List<Printer> mPrinters;
    private List<PrintQueue> mQueue;
    private int headingCount;

    public PrintersListAdapter(Activity context) {
        this.mContext = context;
        this.mPrinters = new ArrayList<>();
        this.mQueue = new ArrayList<>();
        this.headingCount = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case TIMESTAMP:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timestamp_item, parent, false);
                return new TimestampHolder(v);
            case HEADING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_heading_item, parent, false);
                return new PrinterHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_list_item, parent, false);
                return new PrinterHolder(v);
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

            timestampHolder.timestampView.setText(String.format(mContext.getString(R.string.timestamp),
                    mPrinters.get(0).getTimestamp()));
        } else if (mQueue.get(index) == null) {
            PrinterHolder printerHolder = (PrinterHolder) holder;

            printerHolder.queueView.setText(
                    String.format(mContext.getString(R.string.bahen_room),
                            mPrinters.get(headingCount++).getName()));
        } else {
            PrinterHolder printerHolder = (PrinterHolder) holder;

            final PrintQueue job = mQueue.get(index);
            printerHolder.queueView.setText(job.toString());
        }
    }

    @Override
    public int getItemCount() {
        // Add 1 for the timestamp "footer"
        return mQueue.size() + 1;
    }

    public void setPrinters(List<Printer> printers) {
        this.mPrinters = printers;

        this.mQueue.clear();
        for (Printer p : printers) {
            this.mQueue.add(null);  // For the heading
            this.mQueue.addAll(p.getPrintQueue());
        }

        this.headingCount = 0;
    }
}

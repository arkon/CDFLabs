package me.echeung.cdflabs.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.comparators.PrintersByAvail;
import me.echeung.cdflabs.comparators.PrintersByName;
import me.echeung.cdflabs.enums.ListEnum;
import me.echeung.cdflabs.enums.SortEnum;
import me.echeung.cdflabs.holders.ListItemHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.printers.Printer;
import me.echeung.cdflabs.printers.Printers;
import me.echeung.cdflabs.printers.PrintersListItem;
import me.echeung.cdflabs.ui.App;
import me.echeung.cdflabs.utils.ListItem;

public class PrintersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Printers mPrintersData;
    private List<ListItem> mQueue;
    private Comparator<Printer> mComparator;

    private AlertDialog mQueueDialog;
    private PrinterQueueListAdapter mQueueAdapter;
    private ListView mQueueList;

    public PrintersListAdapter(Context context) {
        this.mContext = context;
        this.mQueue = new ArrayList<>();

        updateSortingCriteria();

        // Set up job queue dialog
        this.mQueueDialog = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                .setPositiveButton(context.getString(R.string.close), null)
                .create();

        // Show list in dialog
        final View dialogView = LayoutInflater.from(context).inflate(
                R.layout.fragment_queue_dialog, null);
        this.mQueueDialog.setView(dialogView);

        // Set dialog list adapter
        this.mQueueAdapter = new PrinterQueueListAdapter(context, R.layout.queue_job_item);
        this.mQueueList = (ListView) dialogView.findViewById(R.id.printer_queue);
        this.mQueueList.setEmptyView(dialogView.findViewById(R.id.printer_queue_empty));
        this.mQueueList.setAdapter(mQueueAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case ListEnum.ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.list_info_item, parent, false);
                return new ListItemHolder(v);

            case ListEnum.TIMESTAMP:
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
            case ListEnum.ITEM:
                final ListItemHolder printerHolder = (ListItemHolder) holder;
                final PrintersListItem printer = (PrintersListItem) mQueue.get(index).getItem();

                // Show number of queued jobs and set the square's background colour accordingly
                final int queued = printer.getQueued();
                printerHolder.statusNumView.setText(String.valueOf(queued));
                printerHolder.statusAdjView.setText(mContext.getString(R.string.printer_queued));

                if (queued == 0)
                    printerHolder.statusView.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.status_green));
                else if (queued >= 5)
                    printerHolder.statusView.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.status_red));
                else
                    printerHolder.statusView.setBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.status_orange));

                // Show printer name and description
                printerHolder.titleView.setText(printer.getName());
                printerHolder.subtitleView.setText(printer.getDescription());

                // Show overflow icon
                printerHolder.moreView.setVisibility(View.VISIBLE);

                // Show queue in dialog on click
                printerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mQueueList.setAdapter(null);

                        if (printer.getQueue().size() > 0) {
                            mQueueAdapter.clear();
                            mQueueList.setAdapter(mQueueAdapter);
                            mQueueAdapter.addAll(printer.getQueue());
                        }

                        mQueueDialog.setTitle(String.format("%s: %s",
                                mContext.getString(R.string.queue),
                                printer.getName()));
                        mQueueDialog.show();
                    }
                });
                break;

            case ListEnum.TIMESTAMP:
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

    public void setPrintQueue(Printers queue) {
        if (queue == null) return;

        this.mPrintersData = queue;

        updateList();
    }

    public void updateSortingCriteria() {
        switch (App.getPrinterSort()) {
            case SortEnum.NAME:
                this.mComparator = new PrintersByName();
                break;

            case SortEnum.AVAIL:
                this.mComparator = new PrintersByAvail();
                break;
        }

        updateList();
    }

    public void updateList() {
        if (this.mPrintersData == null) return;

        this.mQueue.clear();

        // Sort printers
        List<Printer> sortedPrinters = this.mPrintersData.getPrinters();
        Collections.sort(sortedPrinters, this.mComparator);

        for (final Printer printer : sortedPrinters) {
            this.mQueue.add(new ListItem<>(ListEnum.ITEM,
                    new PrintersListItem(printer.getName(), printer.getDescription(),
                            printer.getJobs(), printer.getQueued())));
        }

        // Timestamp
        this.mQueue.add(new ListItem<>(ListEnum.TIMESTAMP, this.mPrintersData.getTimestamp()));

        notifyDataSetChanged();
    }
}

package me.echeung.cdflabs.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.enums.ListEnum;
import me.echeung.cdflabs.holders.PrinterHolder;
import me.echeung.cdflabs.holders.TimestampHolder;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;
import me.echeung.cdflabs.printers.PrintersListItem;
import me.echeung.cdflabs.utils.ListItem;

public class PrintersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ListItem> mQueue;

    private AlertDialog mQueueDialog;
    private PrinterQueueListAdapter mQueueAdapter;

    public PrintersListAdapter(Context context) {
        this.mContext = context;
        this.mQueue = new ArrayList<>();

        // Set up job queue dialog
        this.mQueueDialog = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                .setTitle(context.getString(R.string.print_queue))
                .setPositiveButton(context.getString(R.string.close), null)
                .create();

        // Show list in dialog
        final View dialogView = LayoutInflater.from(context).inflate(
                R.layout.fragment_queue_dialog, null);
        this.mQueueDialog.setView(dialogView);

        // Set dialog list adapter
        this.mQueueAdapter = new PrinterQueueListAdapter(context, R.layout.queue_job_item);
        final ListView listView = (ListView) dialogView.findViewById(R.id.printer_queue);
        listView.setAdapter(mQueueAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case ListEnum.ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.printer_item, parent, false);
                return new PrinterHolder(v);

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
                final PrinterHolder printerHolder = (PrinterHolder) holder;
                final PrintersListItem printer = (PrintersListItem) mQueue.get(index).getItem();

                printerHolder.headingView.setText(printer.getName());
                printerHolder.descriptionView.setText(printer.getDescription());
                printerHolder.queuedView.setText(
                        String.format(mContext.getString(R.string.printer_queued),
                                printer.getQueued()));

                // Show queue in dialog on click
                printerHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mQueueAdapter.clear();
                        mQueueAdapter.addAll(printer.getQueue());
                        mQueueAdapter.notifyDataSetChanged();

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

    public void setPrintQueue(PrintQueue queue) {
        if (queue == null) return;

        this.mQueue.clear();

        final List<Printer> printers = queue.getPrinters();
        for (final Printer printer : printers) {
            this.mQueue.add(new ListItem(ListEnum.ITEM,
                    new PrintersListItem(printer.getName(), printer.getDescription(),
                            printer.getJobs(), printer.getQueued())));
        }

        // Timestamp
        this.mQueue.add(new ListItem(ListEnum.TIMESTAMP, queue.getTimestamp()));

        notifyDataSetChanged();
    }
}

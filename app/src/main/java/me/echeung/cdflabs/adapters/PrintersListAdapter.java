package me.echeung.cdflabs.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.holders.PrinterHolder;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;

public class PrintersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;

    private List<Printer> mPrinters;

    public PrintersListAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_list_item, parent, false);
        return new PrinterHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PrinterHolder printerHolder = (PrinterHolder) holder;

        final PrintQueue job = mPrinters.get(0).getPrintQueue().get(0);

        printerHolder.queueView.setText(job.toString());
    }

    @Override
    public int getItemCount() {

        return mPrinters.size();
    }

    public void setPrinters(List<Printer> printers) {
        this.mPrinters = printers;
    }
}

package me.echeung.cdflabs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.labs.Lab;

public class LabsListAdapter extends ArrayAdapter<Lab> {

    private final Context context;
    private List<Lab> labs;

    private LinearLayout compsView;
    private TextView freeView, labView, statsView;

    private Lab lab;

    public LabsListAdapter(Context context, List<Lab> labs) {
        super(context, R.layout.lab_list_item, labs);
        this.context = context;
        this.labs = labs;
    }

    public void setLabs(List<Lab> labs) {
        this.labs = labs;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.lab_list_item, parent, false);

        lab = labs.get(position);

        compsView = (LinearLayout) rowView.findViewById(R.id.comps);
        freeView  = (TextView) rowView.findViewById(R.id.free);
        labView   = (TextView) rowView.findViewById(R.id.lab);
        statsView = (TextView) rowView.findViewById(R.id.stats);

        // Show available machines and set the square's background colour accordingly
        int avail = lab.getAvail();
        freeView.setText(String.valueOf(avail));

        if (avail == 0)
            compsView.setBackgroundColor(context.getResources().getColor(R.color.free_red));
        else if (avail <= 5)
            compsView.setBackgroundColor(context.getResources().getColor(R.color.free_orange));
        else
            compsView.setBackgroundColor(context.getResources().getColor(R.color.free_green));

        // Show lab name and stats
        labView.setText(lab.getLab());
        statsView.setText(String.format(context.getString(R.string.stats), lab.getTotal(), lab.getPercent()));

        return rowView;
    }
}

package me.echeung.cdflabs.utils;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.fragments.LabsFragment;
import me.echeung.cdflabs.labs.Lab;

public class DataScraper extends AsyncTask<Void, Void, Document> {

    private Context context;
    private Document doc;
    private LabsFragment fragment;
    private List<Lab> labs;

    public DataScraper(Context context, LabsFragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.labs = new ArrayList<>();
    }

    @Override
    protected Document doInBackground(Void... params) {
        try {
            doc = Jsoup.connect(context.getString(R.string.website_data)).get();
            return doc;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Document items) {
        if (doc != null) {
            labs = retrieveData();
            fragment.updateAdapter(labs);
        }
    }

    /** Scrape web page and instantiate the Labs with the data. */
    private List<Lab> retrieveData() {
        Elements links = doc.select("td");

        int i = 0;
        Lab lab = null;

        // Each lab has 6 elements: name, avail, busy, total, % busy, timestamp
        for (Element link : links) {
            String text = link.text();
            switch (i) {
                case 0:
                    lab = new Lab();

                    if (!text.equals("NX"))
                        text = "BA " + text;

                    lab.setLab(text);
                    break;
                case 1:
                    lab.setAvail(Integer.parseInt(text));
                    break;
                case 2:
                    lab.setBusy(Integer.parseInt(text));
                    break;
                case 3:
                    lab.setTotal(Integer.parseInt(text));
                    break;
                case 4:
                    lab.setPercent(Double.parseDouble(text));
                    break;
                case 5:
                    lab.setTimestamp(text);
                    labs.add(lab);
                    i = -1;
                    break;
            }
            i++;
        }

        return labs;
    }
}

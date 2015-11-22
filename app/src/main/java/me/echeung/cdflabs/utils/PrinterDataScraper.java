package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.ui.fragments.PrintersFragment;

public class PrinterDataScraper extends AsyncTask<Void, Void, Void> {

    private static final String PRINT_QUEUE_URL =
            "http://www.cdf.toronto.edu/~g3cheunh/cdfprinters.json";

    private PrintQueue queue;
    private String response;

    public PrinterDataScraper() {
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            response = Jsoup.connect(PRINT_QUEUE_URL).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void items) {
        if (response != null) {
            Gson gson = new Gson();
            queue = gson.fromJson(response, PrintQueue.class);

            PrintersFragment printersFragment = ViewPagerAdapter.getPrintersFragment();

            if (printersFragment != null) {
                printersFragment.updateQueue(queue);
            }
        }
    }
}

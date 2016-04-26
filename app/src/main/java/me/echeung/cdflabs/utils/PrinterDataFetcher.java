package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import java.util.Collections;

import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.comparators.PrintersByName;
import me.echeung.cdflabs.printers.Printers;
import me.echeung.cdflabs.ui.fragments.PrintersFragment;

public class PrinterDataFetcher extends AsyncTask<Void, Void, Void> {

    private static final String PRINT_QUEUE_URL =
            "http://www.cdf.toronto.edu/~g3cheunh/cdfprinters.json";

    private String response;

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
        final PrintersFragment printersFragment = ViewPagerAdapter.getPrintersFragment();

        if (printersFragment != null) {
            if (response != null) {
                Gson gson = new Gson();
                final Printers queue = gson.fromJson(response, Printers.class);

                if (queue != null) {
                    // Sort the printers by name
                    Collections.sort(queue.getPrinters(), new PrintersByName());

                    printersFragment.updateQueue(queue);
                    return;
                }
            }

            printersFragment.showFetchError();
        }
    }
}

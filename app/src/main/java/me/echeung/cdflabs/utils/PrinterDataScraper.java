package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.jsoup.Jsoup;

import java.lang.reflect.Type;
import java.util.Collections;

import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.comparators.PrintersByName;
import me.echeung.cdflabs.printers.Printers;
import me.echeung.cdflabs.ui.fragments.PrintersFragment;

public class PrinterDataScraper extends AsyncTask<Void, Void, Void> {

    private static final String PRINT_QUEUE_URL =
            "http://www.cdf.toronto.edu/~g3cheunh/cdfprinters.json";

    private Printers queue;
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
        final PrintersFragment printersFragment = ViewPagerAdapter.getPrintersFragment();

        if (response != null) {
            Gson gson = new Gson();
            queue = gson.fromJson(response, Printers.class);

            // Sort the printers by name
            Collections.sort(queue.getPrinters(), new PrintersByName());

            if (printersFragment != null) {
                printersFragment.updateQueue(queue);
            }
        } else {
            printersFragment.showFetchError();
        }
    }
}
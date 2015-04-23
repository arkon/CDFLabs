package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.fragments.PrintersFragment;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;

public class PrinterDataScraper extends AsyncTask<Void, Void, Void> {

    private static final String PRINT_QUEUE_URL =
            "http://www.cdf.toronto.edu/~g3cheunh/printdata.json";

    private static final String[] PRINTERS =
            new String[]{"2210a", "2210b", "3185a"};

    private Map<String, Printer> printers;
    private String printData;

    public PrinterDataScraper() {
        this.printers = new HashMap<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            printData = Jsoup.connect(PRINT_QUEUE_URL).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void items) {
        if (printData != null) {
            printers = parsePrintQueueData(printData);

            PrintersFragment printersFragment = ViewPagerAdapter.getPrintersFragment();

            if (printersFragment != null) {
                printersFragment.updateLists(printers);
            }
        }
    }

    /**
     * Parse the JSON data of print queue data and return it as a map of objects.
     * @param printData The JSON data in string format.
     * @return A map of Printer objects.
     */
    private Map<String, Printer> parsePrintQueueData(String printData) {
        Map<String, Printer> printers = null;

        try {
            JSONObject jsonObj = new JSONObject(printData);

            printers = new HashMap<>();

            String timestamp = jsonObj.getString("timestamp");

            for (String printer : PRINTERS) {
                JSONArray jsonArr = jsonObj.getJSONArray(printer);
                printers.put(printer, parseQueue(printer, timestamp, jsonArr));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return printers;
    }

    /**
     * Parse the JSON array of print queue data and return it as a Printer object.
     * @param name The name of the printer.
     * @param timestamp The timestamp of the data.
     * @param jsonArr The JSON array of data.
     * @return A Printer object of the data.
     */
    private Printer parseQueue(String name, String timestamp, JSONArray jsonArr) {
        Printer printer = new Printer(name, timestamp);

        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);

                printer.addToQueue(new PrintQueue(
                        jsonObj.getString("rank"),
                        jsonObj.getString("size"),
                        jsonObj.getString("class"),
                        jsonObj.getString("files"),
                        jsonObj.getString("job"),
                        jsonObj.getString("time"),
                        jsonObj.getString("owner")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return printer;
    }
}

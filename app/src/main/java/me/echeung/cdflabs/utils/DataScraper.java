package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.fragments.LabsFragment;
import me.echeung.cdflabs.fragments.PrintersFragment;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.printers.PrintQueue;
import me.echeung.cdflabs.printers.Printer;

public class DataScraper extends AsyncTask<Void, Void, Void> {

    private static final String USAGE_URL =
            "http://www.cdf.toronto.edu/usage/usage.html";
    private static final String PRINT_QUEUE_URL =
            "http://www.cdf.toronto.edu/~g3cheunh/printdata.json";

    private static final String[] PRINTERS =
            new String[]{"2210a", "2210b", "3185a"};

    private Document doc;

    private List<Lab> labs;
    private Map<String, Printer> printers;

    private String printData;

    public DataScraper() {
        this.labs = new ArrayList<>();
        this.printers = new HashMap<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Lab machine usage
            doc = Jsoup.connect(USAGE_URL).get();

            // Print queue
            printData = Jsoup.connect(PRINT_QUEUE_URL).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void items) {
        if (doc != null) {
            labs = parseLabData();

            LabsFragment labsFragment = ViewPagerAdapter.getLabsFragment();

            if (labsFragment != null) {
                labsFragment.updateAdapter(labs);
            }
        }

        if (printData != null) {
            printers = parsePrintQueueData(printData);

            PrintersFragment printersFragment = ViewPagerAdapter.getPrintersFragment();

            if (printersFragment != null) {
                printersFragment.updateText(printers);
            }
        }
    }

    /**
     * Parse the web page document and return it as a list of objects.
     * @return A list of Lab objects.
     */
    private List<Lab> parseLabData() {
        Elements links = doc.select("td");

        int i = 0;
        Lab lab = null;

        // Each lab has 6 elements:
        // name, avail, busy, total, % busy, timestamp
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

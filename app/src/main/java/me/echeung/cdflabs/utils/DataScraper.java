package me.echeung.cdflabs.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.echeung.cdflabs.R;
import me.echeung.cdflabs.activities.MainActivity;
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
    private List<Printer> printers;

    private String printData;

    public DataScraper() {
        this.labs = new ArrayList<>();
        this.printers = new ArrayList<>();
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
                printersFragment.updateText(printers.toArray().toString());
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
     * Parse the JSON data of print queue data and return it as a list of objects.
     * @param printData The JSON data in string format.
     * @return A list of Printer objects.
     */
    private List<Printer> parsePrintQueueData(String printData) {
        List<Printer> printers = null;

        try {
            JSONObject jsonObj = new JSONObject(printData);

            printers = new ArrayList<>();

            String timestamp = jsonObj.getString("timestamp");

            for (String printer : PRINTERS) {
                JSONArray jsonArr = jsonObj.getJSONArray(printer);
                printers.add(parseQueue(printer, timestamp, jsonArr));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return printers;
    }

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

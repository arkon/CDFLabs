package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import org.json.JSONArray;
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

import me.echeung.cdflabs.fragments.LabsFragment;
import me.echeung.cdflabs.labs.Lab;
import me.echeung.cdflabs.printers.Printer;

public class DataScraper extends AsyncTask<Void, Void, Void> {

    private static final String USAGE_URL =
            "http://www.cdf.toronto.edu/usage/usage.html";
    private static final String PRINT_QUEUE_URL =
            "http://www.cdf.toronto.edu/~g3cheunh/printdata.json";

    private Document doc;
    private LabsFragment fragment;
    private List<Lab> labs;
    private List<Printer> printers;

    private List<String> printData;

    public DataScraper(LabsFragment fragment) {
        this.fragment = fragment;
        this.labs = new ArrayList<>();
        this.printers = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            // Usage
            doc = Jsoup.connect(USAGE_URL).get();

            // Print queue
            printData = new ArrayList<>();

            URL url = new URL(PRINT_QUEUE_URL);
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

            String next;
            while ((next = bufferedReader.readLine()) != null){
                JSONArray ja = new JSONArray(next);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    printData.add(jo.getString("time"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void items) {
        if (doc != null) {
            labs = parseLabData();
            fragment.updateAdapter(labs);
        }

        if (printData != null) {

        }
    }

    /**
     * Scrape web page and instantiate the Labs with the data.
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
}

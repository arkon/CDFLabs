package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.echeung.cdflabs.fragments.LabsFragment;
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
    private LabsFragment fragment;

    private List<Lab> labs;
    private List<Printer> printers;

    private String printData;

    public DataScraper(LabsFragment fragment) {
        this.fragment = fragment;
        this.labs = new ArrayList<>();
        this.printers = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        InputStreamReader in = null;
        BufferedReader reader = null;

        try {
            // Usage
            doc = Jsoup.connect(USAGE_URL).get();

            // Print queue
            URL url = new URL(PRINT_QUEUE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            in = new InputStreamReader(urlConnection.getInputStream());
            reader = new BufferedReader(in);

            StringBuilder sBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            printData = sBuilder.toString();

            if (reader != null)
                reader.close();

            if (in != null)
                in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

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
            printers = parsePrintQueueData(printData);
        }
    }

    /**
     * Scrape web page and instantiate the Labs with the data.
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
     *
     * @param printData The JSON data in string format.
     * @return A list of Printer objects.
     */
    private List<Printer> parsePrintQueueData(String printData) {
        List<Printer> printers = null;

        try {
            JSONObject jsonObj = new JSONObject(printData);

            printers = new ArrayList<>();

            String timestamp = jsonObj.getString("timestamp");

            // 2210a
            for (int i = 0; i < PRINTERS.length; i++) {
                JSONArray jsonArr = jsonObj.getJSONArray(PRINTERS[i]);
                printers.add(parseQueue(PRINTERS[i], timestamp, jsonArr));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return printer;
    }

}

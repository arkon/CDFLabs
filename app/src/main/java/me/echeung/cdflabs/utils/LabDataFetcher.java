package me.echeung.cdflabs.utils;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.jsoup.Jsoup;

import me.echeung.cdflabs.adapters.ViewPagerAdapter;
import me.echeung.cdflabs.labs.Labs;
import me.echeung.cdflabs.ui.fragments.LabsFragment;

public class LabDataFetcher extends AsyncTask<Void, Void, Void> {

    private static final String USAGE_URL =
            "http://www.teach.cs.toronto.edu/~cheun550/cdflabs.json";

    private String response;

    @Override
    protected Void doInBackground(Void... params) {
        try {
            response = Jsoup.connect(USAGE_URL).ignoreContentType(true).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void items) {
        final LabsFragment labsFragment = ViewPagerAdapter.getLabsFragment();

        if (labsFragment != null) {
            if (response != null) {
                Gson gson = new Gson();
                final Labs labs = gson.fromJson(response, Labs.class);

                if (labs != null) {
                    labsFragment.updateAdapter(labs);
                    return;
                }
            }

            labsFragment.showFetchError();
        }
    }
}

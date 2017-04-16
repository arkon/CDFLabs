package me.echeung.cdflabs.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.echeung.cdflabs.enums.PreferencesEnum;
import me.echeung.cdflabs.enums.SortEnum;

public class App extends Application {

    private static SharedPreferences preferences;

    private static int labSort;
    private static boolean labShowNX;

    private static int printerSort;

    public static int getLabSort() {
        return labSort;
    }

    public static void setLabSort(int value) {
        App.labSort = value;

        SharedPreferences.Editor editor = App.preferences.edit();
        editor.putInt(PreferencesEnum.LAB_SORT, value);
        editor.apply();
    }

    public static boolean isNXVisible() {
        return labShowNX;
    }

    public static void setNXVisibility(boolean value) {
        App.labShowNX = value;

        SharedPreferences.Editor editor = App.preferences.edit();
        editor.putBoolean(PreferencesEnum.LAB_NX, value);
        editor.apply();
    }

    public static int getPrinterSort() {
        return printerSort;
    }

    public static void setPrinterSort(int value) {
        App.printerSort = value;

        SharedPreferences.Editor editor = App.preferences.edit();
        editor.putInt(PreferencesEnum.PRINTER_SORT, value);
        editor.apply();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        App.preferences = PreferenceManager.getDefaultSharedPreferences(this);

        App.labSort = App.preferences.getInt(PreferencesEnum.LAB_SORT, SortEnum.AVAIL);
        App.labShowNX = App.preferences.getBoolean(PreferencesEnum.LAB_NX, true);

        App.printerSort = App.preferences.getInt(PreferencesEnum.PRINTER_SORT, SortEnum.NAME);
    }
}

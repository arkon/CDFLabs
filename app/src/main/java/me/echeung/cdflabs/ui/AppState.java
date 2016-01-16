package me.echeung.cdflabs.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.echeung.cdflabs.enums.PreferencesEnum;
import me.echeung.cdflabs.enums.SortEnum;

public class AppState extends Application {

    private static SharedPreferences preferences;

    private static int labSort;
    private static boolean labShowNX;

    private static int printerSort;

    @Override
    public void onCreate() {
        super.onCreate();

        AppState.preferences = PreferenceManager.getDefaultSharedPreferences(this);

        AppState.labSort = AppState.preferences.getInt(PreferencesEnum.LAB_SORT, SortEnum.AVAIL);
        AppState.labShowNX = AppState.preferences.getBoolean(PreferencesEnum.LAB_NX, true);

        AppState.printerSort = AppState.preferences.getInt(PreferencesEnum.PRINTER_SORT, SortEnum.NAME);
    }

    public static int getLabSort() {
        return labSort;
    }

    public static void setLabSort(int value) {
        AppState.labSort = value;

        SharedPreferences.Editor editor = AppState.preferences.edit();
        editor.putInt(PreferencesEnum.LAB_SORT, value);
        editor.apply();
    }

    public static boolean isNXVisible() {
        return labShowNX;
    }

    public static void setNXVisibility(boolean value) {
        AppState.labShowNX = value;

        SharedPreferences.Editor editor = AppState.preferences.edit();
        editor.putBoolean(PreferencesEnum.LAB_NX, value);
        editor.apply();
    }

    public static int getPrinterSort() {
        return printerSort;
    }

    public static void setPrinterSort(int value) {
        AppState.printerSort = value;

        SharedPreferences.Editor editor = AppState.preferences.edit();
        editor.putInt(PreferencesEnum.PRINTER_SORT, value);
        editor.apply();
    }
}

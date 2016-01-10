package me.echeung.cdflabs.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.echeung.cdflabs.enums.LabSortEnum;
import me.echeung.cdflabs.enums.PreferencesEnum;

public class AppState extends Application {

    private static SharedPreferences preferences;

    private static int labSort;
    private static boolean showNX;

    @Override
    public void onCreate() {
        super.onCreate();

        AppState.preferences = PreferenceManager.getDefaultSharedPreferences(this);

        AppState.labSort = AppState.preferences.getInt(PreferencesEnum.PREF_SORT, LabSortEnum.AVAIL);
        AppState.showNX = AppState.preferences.getBoolean(PreferencesEnum.PREF_NX, true);
    }

    public static int getLabSort() {
        return labSort;
    }

    public static void setLabSort(int value) {
        AppState.labSort = value;

        SharedPreferences.Editor editor = AppState.preferences.edit();
        editor.putInt(PreferencesEnum.PREF_SORT, value);
        editor.apply();
    }

    public static boolean isNXVisible() {
        return showNX;
    }

    public static void setNXVisibility(boolean value) {
        AppState.showNX = value;

        SharedPreferences.Editor editor = AppState.preferences.edit();
        editor.putBoolean(PreferencesEnum.PREF_NX, value);
        editor.apply();
    }
}

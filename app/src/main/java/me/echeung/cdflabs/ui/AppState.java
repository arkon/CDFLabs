package me.echeung.cdflabs.ui;

import android.app.Application;

import me.echeung.cdflabs.enums.LabSortEnum;

public class AppState extends Application {

    private static int labSort;
    private static boolean showNX;

    @Override
    public void onCreate() {
        super.onCreate();

        AppState.labSort = LabSortEnum.AVAIL;
        AppState.showNX = true;
    }

    public static int getLabSort() {
        return labSort;
    }

    public static void setLabSort(int labSort) {
        AppState.labSort = labSort;
    }

    public static boolean isNXVisible() {
        return showNX;
    }

    public static void setNXVisibility(boolean showNX) {
        AppState.showNX = showNX;
    }
}

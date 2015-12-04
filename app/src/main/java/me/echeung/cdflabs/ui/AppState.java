package me.echeung.cdflabs.ui;

import android.app.Application;

import me.echeung.cdflabs.utils.LabSortEnum;

public class AppState extends Application {

    private static int labSort;

    @Override
    public void onCreate() {
        super.onCreate();

        AppState.labSort = LabSortEnum.AVAIL;
    }

    public static int getLabSort() {
        return labSort;
    }

    public static void setLabSort(int labSort) {
        AppState.labSort = labSort;
    }
}

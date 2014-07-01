package me.echeung.cdflabs.labs;

import java.util.Comparator;

public class LabsByBuilding implements Comparator<Lab> {

    @Override
    public int compare(Lab x, Lab y) {
        return x.getLab().compareTo(y.getLab());
    }
}

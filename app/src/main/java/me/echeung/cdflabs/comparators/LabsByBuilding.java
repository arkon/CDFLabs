package me.echeung.cdflabs.comparators;

import java.util.Comparator;

import me.echeung.cdflabs.labs.Lab;

public class LabsByBuilding implements Comparator<Lab> {

    @Override
    public int compare(Lab x, Lab y) {
        return x.getLab().compareTo(y.getLab());
    }
}

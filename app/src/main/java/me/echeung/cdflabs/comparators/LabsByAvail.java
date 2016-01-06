package me.echeung.cdflabs.comparators;

import java.util.Comparator;

import me.echeung.cdflabs.labs.Lab;

public class LabsByAvail implements Comparator<Lab> {

    @Override
    public int compare(Lab x, Lab y) {
        return (x.getAvailable() == y.getAvailable()) ? 0 : (x.getAvailable() < y.getAvailable()) ? 1 : -1;
    }
}

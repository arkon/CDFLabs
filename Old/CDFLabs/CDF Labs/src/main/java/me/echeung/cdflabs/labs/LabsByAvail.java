package me.echeung.cdflabs.labs;

import java.util.Comparator;

public class LabsByAvail implements Comparator<Lab> {

    @Override
    public int compare(Lab x, Lab y) {
        return (x.getAvail() == y.getAvail()) ? 0 : (x.getAvail() < y.getAvail()) ? 1 : -1;
    }
}

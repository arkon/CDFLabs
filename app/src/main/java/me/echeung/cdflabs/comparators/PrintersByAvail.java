package me.echeung.cdflabs.comparators;

import java.util.Comparator;

import me.echeung.cdflabs.printers.Printer;

public class PrintersByAvail implements Comparator<Printer> {
    @Override
    public int compare(Printer x, Printer y) {
        return (x.getQueued() == y.getQueued()) ? 0 : (x.getQueued() > y.getQueued()) ? 1 : -1;
    }
}

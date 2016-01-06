package me.echeung.cdflabs.comparators;

import java.util.Comparator;

import me.echeung.cdflabs.printers.Printer;

public class PrintersByName implements Comparator<Printer> {
    @Override
    public int compare(Printer x, Printer y) {
        return x.getName().compareTo(y.getName());
    }
}

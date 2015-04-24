package me.echeung.cdflabs.printers;

import java.util.Comparator;

public class PrintersByName implements Comparator<Printer> {

    @Override
    public int compare(Printer x, Printer y) {
        return x.getName().compareTo(y.getName());
    }
}

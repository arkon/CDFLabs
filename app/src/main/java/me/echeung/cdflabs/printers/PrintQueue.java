package me.echeung.cdflabs.printers;

import java.util.Collections;
import java.util.List;

import me.echeung.cdflabs.comparators.PrintersByName;

public class PrintQueue {

    private List<Printer> printers;
    private String timestamp;

    public PrintQueue(List<Printer> printers, String timestamp) {
        Collections.sort(printers, new PrintersByName());
        this.printers = printers;
        this.timestamp = timestamp;
    }

    public int getLength() {
        return printers.size();
    }

    public List<Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(List<Printer> printers) {
        Collections.sort(printers, new PrintersByName());
        this.printers = printers;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

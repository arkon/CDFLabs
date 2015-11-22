package me.echeung.cdflabs.printers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrintQueue {

    private Map<String, Printer> printers;
    private String timestamp;

    public PrintQueue(Map<String, Printer> printers, String timestamp) {
        this.printers = printers;
        this.timestamp = timestamp;
    }

    public int getLength() {
        return printers.size();
    }

    public List<String> getSortedKeys() {
        List keys = new ArrayList(printers.keySet());
        Collections.sort(keys);
        return keys;
    }

    public Map<String, Printer> getPrinters() {
        return printers;
    }

    public void setPrinters(Map<String, Printer> printers) {
        this.printers = printers;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

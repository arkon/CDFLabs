package me.echeung.cdflabs.printers;

import java.util.ArrayList;
import java.util.List;

public class Printer {

    private String name;
    private List<PrintQueue> printQueue;
    private String timestamp;

    public Printer(String name, String timestamp) {
        this.name = name;
        this.printQueue = new ArrayList<>();
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrintQueue> getPrintQueue() {
        return printQueue;
    }

    public void addToQueue(PrintQueue queueItem) {
        this.printQueue.add(queueItem);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

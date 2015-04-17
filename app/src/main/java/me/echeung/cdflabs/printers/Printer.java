package me.echeung.cdflabs.printers;

import java.util.ArrayList;
import java.util.List;

public class Printer {

    private String name;
    private List<PrintQueue> printQueue;

    public Printer(String name) {
        this.name = name;
        this.printQueue = new ArrayList<>();
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
}

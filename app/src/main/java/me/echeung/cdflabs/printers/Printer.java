package me.echeung.cdflabs.printers;

import java.util.ArrayList;
import java.util.List;

public class Printer {

    private String name;
    private List<PrintJob> printQueue;

    public Printer(String name, String timestamp) {
        this.name = name;
        this.printQueue = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrintJob> getPrintQueue() {
        return printQueue;
    }

    public void addToQueue(PrintJob queueItem) {
        this.printQueue.add(queueItem);
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();

        for (PrintJob p : getPrintQueue()) {
            sBuilder.append(p.toString());
        }

        return sBuilder.toString();
    }
}

package me.echeung.cdflabs.printers;

import java.util.List;

public class PrintersListItem {
    private String name;
    private String description;
    private List<PrintJob> queue;

    public PrintersListItem(String name, String description, List<PrintJob> queue) {
        this.name = name;
        this.description = description;
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PrintJob> getQueue() {
        return queue;
    }

    public void setQueue(List<PrintJob> queue) {
        this.queue = queue;
    }
}

package me.echeung.cdflabs.printers;

import java.util.List;

public class Printer {

    private String name;
    private List<PrintJob> jobs;
    private String description;
    private int length;
    private int queued;

    public Printer(String name, List<PrintJob> jobs, String description, int length) {
        this.name = name;
        this.jobs = jobs;
        this.description = description;
        this.length = length;

        calculateQueued();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PrintJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<PrintJob> jobs) {
        this.jobs = jobs;

        calculateQueued();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getQueued() {
        return queued;
    }

    private void calculateQueued() {
        int queued = 0;
        for (final PrintJob job : jobs) {
            if (!job.getRaw().contains("ERROR") && !job.getRank().equals("done")) {
                queued++;
            }
        }

        this.queued = queued;
    }
}

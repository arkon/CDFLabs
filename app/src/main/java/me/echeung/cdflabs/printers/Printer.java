package me.echeung.cdflabs.printers;

import java.util.List;

public class Printer {

    private String name;
    private List<PrintJob> jobs;
    private String description;
    private int length;

    public Printer(String name, List<PrintJob> jobs, String description, int length) {
        this.name = name;
        this.jobs = jobs;
        this.description = description;
        this.length = length;
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

    @Override
    public String toString() {
        final StringBuilder sBuilder = new StringBuilder();

        for (PrintJob job : getJobs()) {
            sBuilder.append(job.toString());
        }

        return sBuilder.toString();
    }
}

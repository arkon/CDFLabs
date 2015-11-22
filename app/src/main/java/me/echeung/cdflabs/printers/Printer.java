package me.echeung.cdflabs.printers;

import java.util.List;

public class Printer {

    private String name;
    private List<PrintJob> jobs;

    public Printer(String name, List<PrintJob> jobs) {
        this.name = name;
        this.jobs = jobs;
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

    public void addToJobs(PrintJob job) {
        this.jobs.add(job);
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();

        for (PrintJob p : getJobs()) {
            sBuilder.append(p.toString());
        }

        return sBuilder.toString();
    }
}

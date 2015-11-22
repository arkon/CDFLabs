package me.echeung.cdflabs.printers;

import java.util.List;

public class Printer {

    private String name;
    private List<PrintJob> jobs;
    private String description;

    public Printer(String name, List<PrintJob> jobs, String description) {
        this.name = name;
        this.jobs = jobs;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

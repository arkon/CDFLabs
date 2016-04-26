package me.echeung.cdflabs.printers;

public class PrintJob {

    private String rank;
    private String size;
    private String job;
    private String time;
    private String error;

    public PrintJob(String rank, String size, String job, String time, String error) {
        this.rank = rank;
        this.size = size;
        this.job = job;
        this.time = time;
        this.error = error;
    }

    public String getRank() {
        return rank;
    }

    public String getSize() {
        return size;
    }

    public String getJob() {
        return job;
    }

    public String getTime() {
        return time;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return !error.isEmpty();
    }
}

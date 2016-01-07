package me.echeung.cdflabs.printers;

import com.google.gson.annotations.SerializedName;

public class PrintJob {

    private String rank;
    private String size;
    @SerializedName("class")
    private String klass;
    private String files;
    private String job;
    private String time;
    private String owner;
    private String raw;

    public PrintJob(String rank, String size, String klass, String files, String job, String time,
                    String owner, String raw) {
        this.rank = rank;
        this.size = size;
        this.klass = klass;
        this.files = files;
        this.job = job;
        this.time = time;
        this.owner = owner;
        this.raw = raw;
    }

    public String getRank() {
        return rank;
    }

    public String getSize() {
        return size;
    }

    public String getJobClass() {
        return klass;
    }

    public String getFiles() {
        return files;
    }

    public String getJob() {
        return job;
    }

    public String getTime() {
        return time;
    }

    public String getOwner() {
        return owner;
    }

    public String getRaw() {
        return raw;
    }
}

package me.echeung.cdflabs.printers;

public class PrintQueue {

    private String rank;
    private String size;
    private String klass;
    private String files;
    private String job;
    private String time;
    private String owner;

    public PrintQueue(String rank, String size, String klass,
                      String files, String job, String time,
                      String owner) {
        this.rank = rank;
        this.size = size;
        this.klass = klass;
        this.files = files;
        this.job = job;
        this.time = time;
        this.owner = owner;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQueueClass() {
        return klass;
    }

    public void setQueueClass(String klass) {
        this.klass = klass;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                getRank(), getOwner(), getQueueClass(), getJob(),
                getFiles(), getSize(), getTime());
    }
}

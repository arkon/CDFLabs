package me.echeung.cdflabs.labs;

public class Lab {

    private String lab, timestamp;
    private int avail, busy, total;
    private double percent;

    public Lab() {
        this.lab       = null;
        this.avail     = 0;
        this.busy      = 0;
        this.total     = 0;
        this.percent   = 0;
        this.timestamp = null;
    }

    public String getLab() {
        return lab;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getAvail() {
        return avail;
    }

    public int getBusy() {
        return busy;
    }

    public int getTotal() {
        return total;
    }

    public double getPercent() {
        return percent;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setAvail(int avail) {
        this.avail = avail;
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}

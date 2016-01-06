package me.echeung.cdflabs.labs;

public class Lab {

    private String name;
    private int available;
    private int busy;
    private int total;
    private double percent;

    public Lab() {
        this.name = "";
        this.available = 0;
        this.busy = 0;
        this.total = 0;
        this.percent = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getBusy() {
        return busy;
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}

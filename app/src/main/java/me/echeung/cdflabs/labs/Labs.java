package me.echeung.cdflabs.labs;

import java.util.List;

public class Labs {

    private List<Lab> labs;
    private String timestamp;

    public Labs(List<Lab> labs, String timestamp) {
        this.labs = labs;
        this.timestamp = timestamp;
    }

    public int getLength() {
        return labs.size();
    }

    public List<Lab> getLabs() {
        return labs;
    }

    public void setLabs(List<Lab> printers) {
        this.labs = printers;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

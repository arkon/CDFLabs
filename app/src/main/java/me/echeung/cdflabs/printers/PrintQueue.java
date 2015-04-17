package me.echeung.cdflabs.printers;

public class PrintQueue {

    private String pRank;
    private String pSize;
    private String pClass;
    private String pFiles;
    private String pJob;
    private String pTime;
    private String pOwner;

    public PrintQueue(String pRank, String pSize, String pClass,
                      String pFiles, String pJob, String pTime,
                      String pOwner) {
        this.pRank = pRank;
        this.pSize = pSize;
        this.pClass = pClass;
        this.pFiles = pFiles;
        this.pJob = pJob;
        this.pTime = pTime;
        this.pOwner = pOwner;
    }

    public String getRank() {
        return pRank;
    }

    public void setRank(String pRank) {
        this.pRank = pRank;
    }

    public String getSize() {
        return pSize;
    }

    public void setSize(String pSize) {
        this.pSize = pSize;
    }

    public String getQueueClass() {
        return pClass;
    }

    public void setQueueClass(String pClass) {
        this.pClass = pClass;
    }

    public String getFiles() {
        return pFiles;
    }

    public void setFiles(String pFiles) {
        this.pFiles = pFiles;
    }

    public String getJob() {
        return pJob;
    }

    public void setJob(String pJob) {
        this.pJob = pJob;
    }

    public String getTime() {
        return pTime;
    }

    public void setTime(String pTime) {
        this.pTime = pTime;
    }

    public String getOwner() {
        return pOwner;
    }

    public void setOwner(String pOwner) {
        this.pOwner = pOwner;
    }
}

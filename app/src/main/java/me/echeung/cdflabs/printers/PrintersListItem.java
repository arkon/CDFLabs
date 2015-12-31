package me.echeung.cdflabs.printers;

public class PrintersListItem<T> {
    private int type;
    private T item;

    public PrintersListItem(int type, T item) {
        this.type = type;
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}

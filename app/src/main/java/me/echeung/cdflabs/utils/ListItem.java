package me.echeung.cdflabs.utils;

public class ListItem<T> {
    private int type;
    private T item;

    public ListItem(int type, T item) {
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

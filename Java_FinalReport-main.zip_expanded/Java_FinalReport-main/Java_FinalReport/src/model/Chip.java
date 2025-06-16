package model;

public class Chip {
    private String ownerId;
    private int value;

    public Chip(String ownerId, int value) {
        this.ownerId = ownerId;
        this.value = value;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return ownerId + "籌碼(" + value + ")";
    }
}


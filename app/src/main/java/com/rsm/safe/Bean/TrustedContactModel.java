package com.rsm.safe.Bean;

public class TrustedContactModel {
    private int ID;
    private String Name;
    private long Number;

    public TrustedContactModel(int ID, String name, long number) {
        this.ID = ID;
        Name = name;
        Number = number;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public long getNumber() {
        return Number;
    }
}

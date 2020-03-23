package com.rsm.safe.Bean;

public class TrustedContactModel {
    private int ID;
    private String Name;
    private String Number;

    public TrustedContactModel(int ID, String name, String number) {
        this.ID = ID;
        Name = name;
        Number = number;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }
}

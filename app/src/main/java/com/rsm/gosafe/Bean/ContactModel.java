package com.rsm.gosafe.Bean;

public class ContactModel {
    private String Name;
    private String Number;

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

    public boolean equals(ContactModel contactModel){
        return (this.Name.equals(contactModel.getName()) && this.Number.equals(contactModel.getNumber()));
    }
}

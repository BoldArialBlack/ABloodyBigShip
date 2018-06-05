package com.edu.scnu.phonebook;

/**
 * Created by ASUS on 2018/3/28.
 */

public class PhoneInfo {
    private String name;
    private String number;

    public PhoneInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

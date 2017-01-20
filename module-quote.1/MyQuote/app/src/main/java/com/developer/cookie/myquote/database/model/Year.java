package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class Year extends RealmObject {

    private long id;
    private int yearNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(int yearNumber) {
        this.yearNumber = yearNumber;
    }
}

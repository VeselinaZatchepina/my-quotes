package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class Year extends RealmObject {

    private long id;
    private String yearNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getYearNumber() {
        return yearNumber;
    }

    public void setYearNumber(String yearNumber) {
        this.yearNumber = yearNumber;
    }
}

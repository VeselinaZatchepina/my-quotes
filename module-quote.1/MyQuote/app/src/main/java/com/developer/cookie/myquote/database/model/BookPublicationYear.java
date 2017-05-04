package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

/**
 * Realm model class for table named "year".
 * It helps get and set year number for every book contains quote.
 */
public class BookPublicationYear extends RealmObject {
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
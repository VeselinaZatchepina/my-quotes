package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

/**
 * Realm model class for table named "page".
 * It helps get and set book page number for every quote.
 */
public class BookPage extends RealmObject {
    private long id;
    private String pageNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
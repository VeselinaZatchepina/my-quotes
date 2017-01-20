package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class Page extends RealmObject {

    private long id;
    private long pageNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }
}

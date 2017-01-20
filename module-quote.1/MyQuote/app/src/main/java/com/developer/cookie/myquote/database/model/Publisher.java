package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class Publisher extends RealmObject {

    private long id;
    private String publisherName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}

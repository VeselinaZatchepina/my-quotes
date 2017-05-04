package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

/**
 * Realm model class for table named "publisher".
 * It helps get and set publisher for every book contains quote.
 */
public class BookPublisher extends RealmObject {
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
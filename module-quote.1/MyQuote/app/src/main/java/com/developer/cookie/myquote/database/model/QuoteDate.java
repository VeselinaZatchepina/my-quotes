package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class QuoteDate extends RealmObject {

    private long id;
    private String quoteDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }
}

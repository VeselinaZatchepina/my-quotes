package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Realm model class for table named "quote_date".
 * It helps get and set creation date (when user save quote) for every quote.
 */
public class QuoteCreationDate extends RealmObject {
    private long id;
    @Required
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
package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

/**
 * Realm model class for table named "category".
 * It helps get and set category for every quote.
 * Example for quote category: "IT", "Math", "literature".
 */
public class QuoteCategory extends RealmObject {
    private long id;
    private String category;
    private int quoteCountCurrentCategory = 0;
    private QuoteType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuoteCountCurrentCategory() {
        return quoteCountCurrentCategory;
    }

    public void setQuoteCountCurrentCategory(int quoteCountCurrentCategory) {
        this.quoteCountCurrentCategory = quoteCountCurrentCategory;
    }
    public QuoteType getType() {
        return type;
    }

    public void setType(QuoteType type) {
        this.type = type;
    }

}
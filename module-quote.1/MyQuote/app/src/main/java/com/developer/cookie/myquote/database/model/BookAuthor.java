package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Realm model class for table named "book_author".
 * It helps get and set book author for every book contains quote.
 */
public class BookAuthor extends RealmObject {
    private long id;
    @Required
    private String bookAuthor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookAuthorName() {
        return bookAuthor;
    }

    public void setBookAuthorName(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
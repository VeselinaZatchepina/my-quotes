package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

/**
 * Realm model class for table named "book_author".
 * It helps get and set book author for every book contains quote.
 */
public class BookAuthor extends RealmObject {
    private long id;
    private String bookAuthor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
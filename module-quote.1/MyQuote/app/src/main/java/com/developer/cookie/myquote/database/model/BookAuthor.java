package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class BookAuthor extends RealmObject {

    private long id;
    private String bookAuthor;

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

package com.developer.cookie.myquote.database.model;


import io.realm.RealmList;
import io.realm.RealmObject;


public class BookName extends RealmObject {

    private long id;
    private String bookName;

    //1-many
    private Publisher publisher;

    //1-many
    private Year year;

    //many-many
    private RealmList<BookAuthor> bookAuthors;

    public RealmList<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(RealmList<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}

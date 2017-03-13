package com.developer.cookie.myquote.database.model;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Realm model class for table named "book_name".
 * It helps get and set book name for every book contains quote
 * and has link to "publisher", "year", "book_author" tables.
 */
public class BookName extends RealmObject {
    private long id;
    private String bookName;
    //Relationship one-to-many
    private BookPublisher publisher;
    //Relationship one-to-many
    private BookPublicationYear year;
    //Relationship many-to-many
    private RealmList<BookAuthor> bookAuthors;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BookPublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(BookPublisher publisher) {
        this.publisher = publisher;
    }

    public BookPublicationYear getYear() {
        return year;
    }

    public void setYear(BookPublicationYear year) {
        this.year = year;
    }

    public RealmList<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(RealmList<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }
}

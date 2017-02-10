package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Realm model class for table named "quote_text".
 * It helps get and set quote text for every quote
 * and has link to "book_name", "page", "quote_date", "category", "type" tables.
 */
public class QuoteText extends RealmObject {
    private long id;
    @Required
    private String quoteText;
    //Relationship one-to-many
    private BookName bookName;
    //Relationship one-to-many
    private BookPage page;
    //Relationship one-to-many
    private QuoteCreationDate date;
    //Relationship one-to-many
    private QuoteCategory category;
    //Relationship one-to-many
    private QuoteType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BookName getBookName() {
        return bookName;
    }

    public void setBookName(BookName bookName) {
        this.bookName = bookName;
    }

    public QuoteCategory getCategory() {
        return category;
    }

    public void setCategory(QuoteCategory category) {
        this.category = category;
    }

    public QuoteCreationDate getDate() {
        return date;
    }

    public void setDate(QuoteCreationDate date) {
        this.date = date;
    }

    public BookPage getPage() {
        return page;
    }

    public void setPage(BookPage page) {
        this.page = page;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public QuoteType getType() {
        return type;
    }

    public void setType(QuoteType type) {
        this.type = type;
    }
}
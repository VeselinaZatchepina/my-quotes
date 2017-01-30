package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class QuoteText extends RealmObject {

    private long id;
    private String quoteText;

    //1-many
    private BookName bookName;

    //1-many
    private Page page;

    //1-many
    private QuoteDate date;

    //1-many
    private Category category;

    //1-many
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BookName getBookName() {
        return bookName;
    }

    public void setBookName(BookName bookName) {
        this.bookName = bookName;
    }

    public QuoteDate getDate() {
        return date;
    }

    public void setDate(QuoteDate date) {
        this.date = date;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

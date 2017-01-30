package com.developer.cookie.myquote.database.model;


import io.realm.RealmObject;

public class Category extends RealmObject {

    private long id;
    private String category;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

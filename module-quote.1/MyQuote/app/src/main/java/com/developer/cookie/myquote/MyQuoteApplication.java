package com.developer.cookie.myquote;


import android.app.Application;

import io.realm.Realm;


public class MyQuoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        }
    }
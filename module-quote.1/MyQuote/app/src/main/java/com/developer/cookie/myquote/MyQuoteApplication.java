package com.developer.cookie.myquote;


import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MyQuoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("quotes.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
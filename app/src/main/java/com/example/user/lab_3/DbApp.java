package com.example.user.lab_3;

import android.app.Application;

/**
 * Created by User on 20.11.2016.
 */

public class DbApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DbHelper.init(getApplicationContext());
    }

}

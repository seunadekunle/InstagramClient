package com.example.instagramclient.classes;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("XWEvRW9LWzJPU1JLdB5JT2WZ7kSWJiNdhKzVjeL8")
                .clientKey("RNwTGUPiFk5hPB9fWvYHcCQdTupwFiKrTOB42Z5n")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

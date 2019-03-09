package com.github.swipedemo;

import android.app.Application;

import com.github.swipeback.utils.ActivityStack;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityStack.init(this);
    }
}

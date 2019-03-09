package com.github.swipedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.swipeback.view.SwipeBackActivity;

public class SecondActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}

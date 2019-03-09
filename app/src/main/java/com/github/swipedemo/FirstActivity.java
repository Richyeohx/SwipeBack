package com.github.swipedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.swipeback.view.SwipeBackActivity;

public class FirstActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void jumpToSecond(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}

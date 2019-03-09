package com.github.swipeback.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.github.swipeback.utils.ActivityStack;

public class SwipeBackActivity extends AppCompatActivity implements SwipeBackLayout.ISwipeBack {
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.setLayoutParams(params);
        mSwipeBackLayout.injectToActivity(this);
    }

    @Override
    public SwipeBackLayout getPreLayout() {
        return mSwipeBackLayout;
    }

    @Override
    public SwipeBackLayout.ISwipeBack getPreActivity() {
        return (SwipeBackLayout.ISwipeBack) ActivityStack.getInstance().getPreActivity();
    }
}

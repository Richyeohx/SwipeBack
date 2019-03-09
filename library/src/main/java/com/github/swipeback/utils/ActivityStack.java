package com.github.swipeback.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

public final class ActivityStack implements Application.ActivityLifecycleCallbacks {
    private Stack<Activity> mStack;

    private static class ActivityStackHolder {
        private static final ActivityStack INSTANCE = new ActivityStack();
    }

    /**
     * 单例模式利用了ClassLoader机制，在内部看是一个饿汉式的，在外部看是个懒汉式的。
     *
     * @return ActivityStack
     */
    public static ActivityStack getInstance() {
        return ActivityStackHolder.INSTANCE;
    }

    /**
     * 初始化，需要在Application中初始化，注册Activity生命周期
     *
     * @param app
     */
    public static void init(Application app) {
        app.registerActivityLifecycleCallbacks(getInstance());
    }

    private ActivityStack() {
        mStack = new Stack<>();
    }

    /**
     * 获取栈大小
     *
     * @return 栈大小
     */
    public int getStackSize() {
        return mStack.size();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mStack.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mStack.pop();
    }
}

package com.github.swipeback.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.support.annotation.NonNull;

import java.lang.reflect.Method;

public class ActivityUtils {
    /**
     * 将Activity从透明中转换
     *
     * @param activity 需要转换的Activity
     */
    @SuppressLint("PrivateApi")
    public static void convertFromTranslucent(@NonNull Activity activity) {
        try {
            Method convertFromTranslucent = Activity.class.getDeclaredMethod("convertFromTranslucent");
            convertFromTranslucent.setAccessible(true);
            convertFromTranslucent.invoke(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 将Activity转换成透明
     * 在Android5.0之前转换透明可能会有问题，Android 5.0之后比较稳定
     *
     * @param activity 需要转换的Activity
     */
    public static void convertToTranslucent(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertToTranslucentAfterL(activity);
        } else {
            convertToTranslucentBeforeL(activity);
        }
    }

    /**
     * Android5.0之前将Activity转换成透明方法
     *
     * @param activity 需要转换的Activity
     */
    @SuppressLint("PrivateApi")
    private static void convertToTranslucentBeforeL(@NonNull Activity activity) {
        try {
            //TranslucentConversionListener Class
            Class<?>[] declaredClasses = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionClazz = null;
            for (Class<?> clazz : declaredClasses) {
                if (clazz.getName().contains("TranslucentConversionListener")) {
                    translucentConversionClazz = clazz;
                }
            }
            //convertToTranslucent Method
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionClazz);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, new Object[]{null});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Android5.0之后将Activity转换成透明方法
     *
     * @param activity 需要转换的Activity
     */
    @SuppressLint("PrivateApi")
    private static void convertToTranslucentAfterL(@NonNull Activity activity) {
        try {
            //TranslucentConversionListener Class
            Class<?>[] declaredClasses = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionClazz = null;
            for (Class<?> clazz : declaredClasses) {
                if (clazz.getName().contains("TranslucentConversionListener")) {
                    translucentConversionClazz = clazz;
                }
            }
            //getActivityOptions Method
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object activityOptions = getActivityOptions.invoke(activity);
            //convertToTranslucent Method
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionClazz, ActivityOptions.class);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, null, activityOptions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package com.crn.shopping.application;

import android.app.Application;

/**
 * Created by ceren on 6/17/17.
 */

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }
}

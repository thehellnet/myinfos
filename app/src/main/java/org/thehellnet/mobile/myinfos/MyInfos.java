package org.thehellnet.mobile.myinfos;

import android.app.Application;
import android.content.Context;

/**
 * Created by sardylan on 29/08/15.
 */
public class MyInfos extends Application {

    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }

    public static Context getAppContext() {
        return applicationContext;
    }
}

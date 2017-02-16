package org.thehellnet.mobile.myinfos;

import android.app.Application;
import android.content.Context;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by sardylan on 29/08/15.
 */
public class MyInfos extends Application {

    public static final String[] PERMISSIONS = new String[]{
            ACCESS_NETWORK_STATE,
            READ_PHONE_STATE,
            ACCESS_COARSE_LOCATION
    };

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

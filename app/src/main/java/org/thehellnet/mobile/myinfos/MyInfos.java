package org.thehellnet.mobile.myinfos;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;

import android.app.Application;

/**
 * Created by sardylan on 29/08/15.
 */
public class MyInfos extends Application {

    public static final String[] PERMISSIONS = new String[]{
            ACCESS_NETWORK_STATE,
            READ_PHONE_STATE,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION
    };

}

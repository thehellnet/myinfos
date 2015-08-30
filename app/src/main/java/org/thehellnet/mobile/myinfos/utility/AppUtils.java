package org.thehellnet.mobile.myinfos.utility;

import org.thehellnet.mobile.myinfos.BuildConfig;
import org.thehellnet.mobile.myinfos.MyInfos;

/**
 * Created by sardylan on 30/08/15.
 */
public final class AppUtils {

    public static String getAppVersion() {
        return String.format("%s - %s",
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE);
    }

    public static String getDensityName() {
        float density = MyInfos.getAppContext().getResources().getDisplayMetrics().density;

        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1.0) {
            return "mdpi";
        }
        return "ldpi";
    }

}

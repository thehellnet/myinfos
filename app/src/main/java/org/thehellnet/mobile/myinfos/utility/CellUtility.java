package org.thehellnet.mobile.myinfos.utility;

/**
 * Created by sardylan on 16/02/17.
 */

public final class CellUtility {

    public static double parsePosition(int value) {
        return ((value / 0.25f) / 60.0f) / 60.0f;
    }
}

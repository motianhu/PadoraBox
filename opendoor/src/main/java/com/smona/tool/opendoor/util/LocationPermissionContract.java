package com.smona.tool.opendoor.util;

import android.app.Activity;

public class LocationPermissionContract  {

    public static void tryOpenLocation(Activity activity, OnPermissionListener listener) {
        listener.onPermissionGranted();
    }

    public interface OnPermissionGrantedListener {
        void onPermissionGranted();
    }

    public interface OnPermissionListener extends OnPermissionGrantedListener {
        void onPermissionDenied(boolean notAsk);
    }
}

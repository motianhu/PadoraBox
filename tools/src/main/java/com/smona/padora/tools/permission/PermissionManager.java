package com.smona.padora.tools.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

public class PermissionManager {

    private static PermissionManager sInstance = null;

    private PermissionManager() {

    }

    public static synchronized PermissionManager getsInstance() {
        if (sInstance == null) {
            sInstance = new PermissionManager();
        }
        return sInstance;
    }

    public void requestPermissions(final Activity activity,
                                   final String[] permissions, final int requestCode) {
        if (AndroidSdkUtil.sdkLessThan23()) {
            return;
        }
        activity.requestPermissions(permissions, requestCode);
    }

    public boolean checkSelfPermission(final Activity activity,
                                       String permission) {
        if (AndroidSdkUtil.sdkLessThan23()) {
            return false;
        } else {
            return activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED;
        }
    }

    public void processPermission(final Activity activity, int requestCode) {
        if (AndroidSdkUtil.sdkLessThan23()) {
            return;
        }
        boolean checkPhonePermission = PermissionManager.getsInstance()
                .checkSelfPermission(activity,
                        Manifest.permission.READ_PHONE_STATE);

        boolean checkMediaPermission = PermissionManager.getsInstance()
                .checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean checkCameraPermission = PermissionManager.getsInstance()
                .checkSelfPermission(activity,
                        Manifest.permission.CAMERA);

        boolean checkLocationPermission = PermissionManager.getsInstance()
                .checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (checkPhonePermission || checkMediaPermission || checkCameraPermission || checkLocationPermission) {
            Intent intent = new Intent();
            intent.setClass(activity, PermissionSettingActivity.class);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public interface IPermissionsResultCallback {
        void onRequestPermissionsResult(int var1, String[] var2, int[] var3);
    }
}

package com.smona.padora.tools.permission;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by motianhu on 5/13/17.
 */

public class PermissionActivity extends AppCompatActivity {
    private static final String TAG = PermissionActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initPermission() {
        PermissionManager.getsInstance().processPermission(this,
                PermissionConstants.PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode: " + requestCode
                + ";resultCode: " + resultCode);
        if (requestCode == PermissionConstants.PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            } else {

            }
        }
    }
}

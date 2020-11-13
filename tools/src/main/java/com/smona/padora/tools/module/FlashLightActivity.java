package com.smona.padora.tools.module;

import android.hardware.Camera;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.smona.padora.tools.R;
import com.smona.padora.tools.permission.PermissionActivity;

import java.util.List;

/**
 * Created by motianhu on 5/11/17.
 */

public class FlashLightActivity extends PermissionActivity {
    private static final String TAG = "FlashLightActivity";

    private Camera mCamera;
    private boolean mIsFlashlightOn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        initCamera();
        final View powerButton = findViewById(R.id.powerButton);
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFlashlightOn) {
                    turnLightOff(mCamera);
                } else {
                    turnLightOn(mCamera);
                }
                powerButton.setPressed(mIsFlashlightOn);
                mIsFlashlightOn = !mIsFlashlightOn;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private void initCamera() {
        if (mCamera == null) {
            mCamera = getCamera(0);
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "getCamera e: " + e);
        }
        return camera;
    }

    private static void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }

    private static void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
                Log.e(TAG, "FLASH_MODE_OFF not supported");
            }
        }
    }
}

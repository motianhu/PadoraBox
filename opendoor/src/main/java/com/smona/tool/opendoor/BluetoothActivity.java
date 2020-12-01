package com.smona.tool.opendoor;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;
import com.smona.tool.opendoor.api.OpenDoorCallback;
import com.smona.tool.opendoor.chain.ChainStep;
import com.smona.tool.opendoor.chain.ChainsExecutor;
import com.smona.tool.opendoor.chain.IActionCallback;
import com.smona.tool.opendoor.widget.BleOpenDoorContainer;
import com.smona.tool.opendoor.widget.BleStartupContainer;

public class BluetoothActivity extends Activity implements IActionCallback, IClickCallback {

    private BleStartupContainer bleStartupContainer;
    private BleOpenDoorContainer bleOpenDoorContainer;

    private ChainsExecutor actionExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        initContentView();
        ApiOpenDoorImpl.getInstance().registerConnection(getApplicationContext(), new OpenDoorCallback() {
            @Override
            public void onSuccess() {
                Log.e("motianhu", "registerConnection onSuccess!!!");
                Toast.makeText(BluetoothActivity.this, "registerConnection onSuccess" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(boolean shouldRetry) {
                Toast.makeText(BluetoothActivity.this, "registerConnection onFailed" , Toast.LENGTH_SHORT).show();
                Log.e("motianhu", "registerConnection onFailed!!!");
            }
        });
        requestPer();
    }

    private boolean hasLocationPermissions() {
        return (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);

    }

    private static final int REQUEST_LOCATION_PERMISSION = 10;

    private void requestLocationPermission() {
        requestPermissions(getPermissions(),
                REQUEST_LOCATION_PERMISSION);
    }

    public static String[] getPermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ?
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION} :
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            executeChain();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiOpenDoorImpl.getInstance().unRegisterConnection();
    }

    protected void initContentView() {
        initSerializable();
        initHeader();
        initViews();
    }

    private void initSerializable() {
//        enterHotelOrderBean = (EnterHotelOrderBean) getIntent().getSerializableExtra(ARoutePath.PATH_TO_BLUETOOTH_OPEN_DOOR);
//        if (enterHotelOrderBean == null) {
//            finish();
//        }
    }

    private void initHeader() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        ImageView backImg = findViewById(R.id.iv_back);
        backImg.setImageResource(R.drawable.return_icon_white);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        TextView textView = findViewById(R.id.room_no);
        textView.setText("1001");
        textView = findViewById(R.id.room_name);
        textView.setText("10201");
        textView = findViewById(R.id.room_date);
        textView.setText("2010");

        bleStartupContainer = findViewById(R.id.startup_ble);
        bleStartupContainer.setOnCallback(this);
        bleStartupContainer.startStepOne(BleStartupContainer.INIT);
        bleStartupContainer.startStepTwo(BleStartupContainer.INIT);
        bleStartupContainer.startStepThree(BleStartupContainer.INIT);
        bleOpenDoorContainer = findViewById(R.id.open_door);

        actionExecutor = new ChainsExecutor();
        actionExecutor.buildChains();
    }

    private void requestPer() {
        if (hasLocationPermissions()) {
            executeChain();
        } else {
            requestLocationPermission();
        }
    }

    private void executeChain() {
        actionExecutor.executeStart(this, this);
    }

    private void executeAmmk(String inviteCode) {
        actionExecutor.executeAmmk(inviteCode, this, this);
    }

    public void onInviteCode() {
        if(ApiOpenDoorImpl.getInstance().isEndpointSetupComplete()) {
            executeAmmk("");
        } else {
            executeAmmk("BD4G-U3SJ-DQV3-FXSG");
        }
    }

    @Override
    public void onStepSuccess(ChainStep step) {
        Log.e("motianhu", "onStepSuccess " + step);
        if (step == ChainStep.PERMISSION) {
            bleStartupContainer.startStepOne(BleStartupContainer.INIT);
        } else if (step == ChainStep.OPEN_BLUETOOTH) {
            bleStartupContainer.startStepOne(BleStartupContainer.SUCCESS);
        } else if (step == ChainStep.STARTUP) {
            bleStartupContainer.startStepTwo(BleStartupContainer.PROCESS);
            onInviteCode();
        } else if (step == ChainStep.ENDPOINT_SETUP) {
            bleStartupContainer.startStepTwo(BleStartupContainer.PROCESS);
        } else if (step == ChainStep.ENDPOINT_UPDATE) {
            bleStartupContainer.startStepTwo(BleStartupContainer.PROCESS);
        } else if (step == ChainStep.MOBILE_KEY) {
            bleStartupContainer.startStepTwo(BleStartupContainer.SUCCESS);
        } else if (step == ChainStep.AUTH_FACE) {
            bleStartupContainer.startStepThree(BleStartupContainer.SUCCESS);
            bleStartupContainer.setVisibility(View.VISIBLE);
            bleOpenDoorContainer.setVisibility(View.VISIBLE);
        } else if (step == ChainStep.LOCK_IN_RANGE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bleStartupContainer.startStepTwo(BleStartupContainer.SUCCESS);
                }
            });
        } else if (step == ChainStep.UNREGISTER_ENDPOINT) {
            bleStartupContainer.startStepTwo(BleStartupContainer.INIT);
        }
    }

    @Override
    public void onStepFailed(ChainStep step) {
        Log.e("motianhu", "onStepFailed " + step);
        if (step == ChainStep.PERMISSION) {
            bleStartupContainer.startStepOne(BleStartupContainer.FAILED);
        } else if (step == ChainStep.OPEN_BLUETOOTH) {
            bleStartupContainer.startStepOne(BleStartupContainer.FAILED);
        } else if (step == ChainStep.STARTUP) {
            bleStartupContainer.startStepTwo(BleStartupContainer.FAILED);
        } else if (step == ChainStep.ENDPOINT_SETUP) {
            bleStartupContainer.startStepTwo(BleStartupContainer.FAILED);
        } else if (step == ChainStep.ENDPOINT_UPDATE) {
            bleStartupContainer.startStepTwo(BleStartupContainer.FAILED);
        } else if (step == ChainStep.MOBILE_KEY) {
            bleStartupContainer.startStepTwo(BleStartupContainer.FAILED);
        } else if (step == ChainStep.AUTH_FACE) {
            bleStartupContainer.startStepThree(BleStartupContainer.FAILED);
        } else if (step == ChainStep.LOCK_IN_RANGE) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bleStartupContainer.startStepThree(BleStartupContainer.FAILED);
                }
            });
        } else if (step == ChainStep.UNREGISTER_ENDPOINT) {
        }

    }

    @Override
    public void onRefreshBluetooth() {
        executeChain();
    }

    @Override
    public void onRefreshMobileKeys() {
        executeAmmk("");
    }

    @Override
    public void onGotoAuth() {
    }
}

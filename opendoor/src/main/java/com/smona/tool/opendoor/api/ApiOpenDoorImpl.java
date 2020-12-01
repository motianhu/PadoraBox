package com.smona.tool.opendoor.api;

import android.app.Notification;
import android.content.Context;

import com.assaabloy.mobilekeys.api.ApiConfiguration;
import com.assaabloy.mobilekeys.api.EndpointSetupConfiguration;
import com.assaabloy.mobilekeys.api.MobileKey;
import com.assaabloy.mobilekeys.api.MobileKeysApi;
import com.assaabloy.mobilekeys.api.MobileKeysCallback;
import com.assaabloy.mobilekeys.api.MobileKeysException;
import com.assaabloy.mobilekeys.api.ReaderConnectionController;
import com.assaabloy.mobilekeys.api.ReaderConnectionInfoType;
import com.assaabloy.mobilekeys.api.ble.*;
import com.assaabloy.mobilekeys.api.hce.HceConnectionCallback;
import com.assaabloy.mobilekeys.api.hce.HceConnectionListener;
import com.assaabloy.mobilekeys.api.hce.NfcConfiguration;
import com.smona.tool.opendoor.api.notification.UnlockNotification;
import com.smona.tool.opendoor.api.open.ClosestLockTrigger;

import java.util.Collections;
import java.util.List;

public class ApiOpenDoorImpl {

    private static ApiOpenDoorImpl apiOpenDoor = new ApiOpenDoorImpl();
    private ClosestLockTrigger closestLockTrigger;
    private ReaderConnectionCallback readerConnectionCallback;
    private HceConnectionCallback hceConnectionCallback;

    public static ApiOpenDoorImpl getInstance() {
        return apiOpenDoor;
    }

    public void init(Context appContext) {
        OpeningTrigger[] openingTriggers = {new TapOpeningTrigger(appContext)};
        ScanConfiguration scanConfiguration = new ScanConfiguration.Builder(openingTriggers, 1)
                .setBluetoothModeIfSupported(BluetoothMode.DUAL)
                .setAllowBackgroundScanning(true)
                .setRssiSensitivity(RssiSensitivity.HIGH)
                .setScanMode(ScanMode.OPTIMIZE_PERFORMANCE)
                .build();

        ApiConfiguration apiConfiguration = new ApiConfiguration.Builder()
                .setApplicationId("EXAMPLE")
                .setApplicationDescription("ASSA ABLOY Mobile Keys Example Implementation")
                .setNfcParameters(new NfcConfiguration.Builder()
                        .unsafe_setAttemptNfcWithScreenOff(false)
                        .build())
                .build();
        MobileKeysApi.getInstance().initialize(appContext, apiConfiguration, scanConfiguration);
        if (!MobileKeysApi.getInstance().isInitialized()) {
            throw new IllegalStateException();
        }

        MobileKeysApi.getInstance().getReaderConnectionController().enableHce();
    }

    public boolean isEndpointSetupComplete() {
        try {
            return MobileKeysApi.getInstance().getMobileKeys().isEndpointSetupComplete();
        } catch (MobileKeysException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void startupApplication(OpenDoorCallback callback) {
        MobileKeysApi.getInstance().getMobileKeys().applicationStartup(new MobileKeysCallback() {
            @Override
            public void handleMobileKeysTransactionCompleted() {
                callback.onSuccess();
            }

            @Override
            public void handleMobileKeysTransactionFailed(MobileKeysException e) {
                callback.onFailed(false);
            }
        });
    }

    public void setupEndpoint(String inviteCode, OpenDoorCallback callback) {
        MobileKeysApi.getInstance().getMobileKeys().endpointSetup(new MobileKeysCallback() {
            @Override
            public void handleMobileKeysTransactionCompleted() {
                callback.onSuccess();
            }

            @Override
            public void handleMobileKeysTransactionFailed(MobileKeysException e) {
                callback.onFailed(false);
            }
        }, inviteCode, new EndpointSetupConfiguration.Builder().build());
    }

    public void unregisterEndpoint(OpenDoorCallback callback) {
        MobileKeysApi.getInstance().getMobileKeys().unregisterEndpoint(new MobileKeysCallback() {
            @Override
            public void handleMobileKeysTransactionCompleted() {
                callback.onSuccess();
            }

            @Override
            public void handleMobileKeysTransactionFailed(MobileKeysException e) {
                callback.onFailed(false);
            }
        });
    }

    public void listMobileKeys(Context context, OpenDoorCallback callback) {
        List<MobileKey> data = null;
        try {
            data = MobileKeysApi.getInstance().getMobileKeys().listMobileKeys();
        } catch (MobileKeysException e) {
            e.printStackTrace();
        }
        if (data == null) {
            data = Collections.emptyList();
        }
        if (data.size() > 0) {
            callback.onSuccess();
        } else {
            callback.onFailed(false);
        }
        ReaderConnectionController controller = MobileKeysApi.getInstance().getReaderConnectionController();
        if (data.isEmpty()) {
            controller.stopScanning();
        } else {
            controller.enableHce();

            Notification notification = UnlockNotification.create(context);
            controller.startForegroundScanning(notification);
        }
    }

    public void endpointUpdate(OpenDoorCallback callback) {
        MobileKeysApi.getInstance().getMobileKeys().endpointUpdate(new MobileKeysCallback() {
            @Override
            public void handleMobileKeysTransactionCompleted() {
                callback.onSuccess();
            }

            @Override
            public void handleMobileKeysTransactionFailed(MobileKeysException e) {
                callback.onFailed(false);
            }
        });
    }

    public void registerTrigger(OpenDoorCallback callback) {
        if (closestLockTrigger != null) {
            unRegisterTrigger();
        }
        closestLockTrigger = new ClosestLockTrigger(new ClosestLockTrigger.LockInRangeListener() {
            @Override
            public void onLockInRange(boolean lockInRange) {
                if (lockInRange) {
                    callback.onSuccess();
                } else {
                    callback.onFailed(false);
                }
            }
        });
        MobileKeysApi.getInstance().getReaderConnectionController().getScanConfiguration().getRootOpeningTrigger().add(closestLockTrigger);
    }

    public void unRegisterTrigger() {
        ReaderConnectionController controller = MobileKeysApi.getInstance().getReaderConnectionController();
        controller.getScanConfiguration().getRootOpeningTrigger().remove(closestLockTrigger);
        closestLockTrigger = null;
        controller.stopScanning();
    }

    public void registerConnection(Context appContext, OpenDoorCallback callback) {
        readerConnectionCallback = new ReaderConnectionCallback(appContext);
        readerConnectionCallback.registerReceiver(new ReaderConnectionListener() {
            @Override
            public void onReaderConnectionOpened(Reader reader, OpeningType openingType) {
                callback.onSuccess();
            }

            @Override
            public void onReaderConnectionClosed(Reader reader, OpeningResult openingResult) {
                callback.onFailed(false);
            }

            @Override
            public void onReaderConnectionFailed(Reader reader, OpeningType openingType, OpeningStatus openingStatus) {
                callback.onFailed(false);
            }
        });
        hceConnectionCallback = new HceConnectionCallback(appContext);
        hceConnectionCallback.registerReceiver(new HceConnectionListener() {
            @Override
            public void onHceSessionOpened() {
                callback.onSuccess();
            }

            @Override
            public void onHceSessionClosed(int i) {
                callback.onFailed(false);
            }

            @Override
            public void onHceSessionInfo(ReaderConnectionInfoType readerConnectionInfoType) {
                callback.onFailed(false);
            }
        });

    }

    public void unRegisterConnection() {
        readerConnectionCallback.unregisterReceiver();
        hceConnectionCallback.unregisterReceiver();
    }
}

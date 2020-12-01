package com.smona.tool.opendoor.chain;

import android.app.Activity;
import android.text.TextUtils;

public class ChainsExecutor {
    private AbstractChain mStartAction;
    private AbstractChain mAmmkAction;

    public void buildChains() {
        mAmmkAction = new StartupAmmkChain(ChainStep.STARTUP);
        AbstractChain openBluetoothAction = new OpenBluetoothChain(ChainStep.OPEN_BLUETOOTH, mAmmkAction);
        mStartAction = new PermissionChain(ChainStep.PERMISSION, openBluetoothAction);
    }

    public void executeStart(Activity activity, IActionCallback callback) {
        mStartAction.execute(activity, callback);
    }

    public void executeAmmk(String inviteCode, Activity activity, IActionCallback callback) {
        AbstractChain openDoorChain = new LockInRangeChain(ChainStep.LOCK_IN_RANGE, null);
        AbstractChain authFaceChain = new AuthFaceChain(ChainStep.AUTH_FACE, openDoorChain);
        AbstractChain mobileKeysChain = new MobileKeysChain(activity, ChainStep.MOBILE_KEY, authFaceChain);
        AbstractChain endPointUpdateChain = new EndPointUpdateChain(ChainStep.ENDPOINT_UPDATE, mobileKeysChain);
        if (TextUtils.isEmpty(inviteCode)) {
            mAmmkAction.addNextAction(endPointUpdateChain);
        } else {
            AbstractChain endPointSetupChain = new EndPointSetupChain(inviteCode, ChainStep.ENDPOINT_SETUP, endPointUpdateChain);
            mAmmkAction.addNextAction(endPointSetupChain);
        }
        mAmmkAction.execute(activity, callback);
    }
}

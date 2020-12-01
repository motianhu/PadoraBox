package com.smona.tool.opendoor.chain;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;

public class OpenBluetoothChain extends AbstractChain {

    public OpenBluetoothChain(ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        if (BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_ON) {
            if(isCheckPass()) {
                executeNextAction(activity, actionCallback);
                return;
            }
            actionCallback.onStepSuccess(getStep());
            executeNextAction(activity, actionCallback);
        } else {
            setCheckPass(false);
            actionCallback.onStepFailed(getStep());
        }
    }
}

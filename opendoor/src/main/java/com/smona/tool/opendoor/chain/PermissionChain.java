package com.smona.tool.opendoor.chain;

import android.app.Activity;

import com.smona.tool.opendoor.util.LocationPermissionContract;

public class PermissionChain extends AbstractChain {

    public PermissionChain(ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        if(isCheckPass()) {
            executeNextAction(activity, actionCallback);
            return;
        }
        LocationPermissionContract.tryOpenLocation(activity, new LocationPermissionContract.OnPermissionListener() {
            @Override
            public void onPermissionDenied(boolean notAsk) {
                setCheckPass(false);
                actionCallback.onStepFailed(getStep());
            }

            @Override
            public void onPermissionGranted() {
                actionCallback.onStepSuccess(getStep());
                executeNextAction(activity, actionCallback);
            }
        });
    }
}

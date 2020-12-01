package com.smona.tool.opendoor.chain;

import android.app.Activity;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;
import com.smona.tool.opendoor.api.OpenDoorCallback;

public class MobileKeysChain extends AbstractChain {

    private Activity activity;

    public MobileKeysChain(Activity activity, ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
        this.activity = activity;
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        ApiOpenDoorImpl.getInstance().listMobileKeys(activity, new OpenDoorCallback() {
            @Override
            public void onSuccess() {
                actionCallback.onStepSuccess(getStep());
                executeNextAction(activity, actionCallback);
            }

            @Override
            public void onFailed(boolean shouldRetry) {
                actionCallback.onStepFailed(getStep());
            }
        });
    }
}

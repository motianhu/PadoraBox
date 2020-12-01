package com.smona.tool.opendoor.chain;

import android.app.Activity;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;
import com.smona.tool.opendoor.api.OpenDoorCallback;

public class EndPointUpdateChain extends AbstractChain {

    public EndPointUpdateChain(ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        ApiOpenDoorImpl.getInstance().endpointUpdate(new OpenDoorCallback() {
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
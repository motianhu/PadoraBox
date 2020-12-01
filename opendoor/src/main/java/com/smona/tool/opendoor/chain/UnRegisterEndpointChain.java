package com.smona.tool.opendoor.chain;

import android.app.Activity;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;
import com.smona.tool.opendoor.api.OpenDoorCallback;

public class UnRegisterEndpointChain extends AbstractChain {

    public UnRegisterEndpointChain(ChainStep curStep) {
        super(curStep);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        ApiOpenDoorImpl.getInstance().unregisterEndpoint(new OpenDoorCallback() {
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

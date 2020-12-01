package com.smona.tool.opendoor.chain;

import android.app.Activity;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;
import com.smona.tool.opendoor.api.OpenDoorCallback;

public class StartupAmmkChain extends AbstractChain {

    public StartupAmmkChain(ChainStep curStep) {
        super(curStep);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        if(isCheckPass()) {
            executeNextAction(activity, actionCallback);
            return;
        }
        ApiOpenDoorImpl.getInstance().startupApplication(new OpenDoorCallback() {
            @Override
            public void onSuccess() {
                setCheckPass(true);
                actionCallback.onStepSuccess(getStep());
                executeNextAction(activity, actionCallback);
            }

            @Override
            public void onFailed(boolean shouldRetry) {
                setCheckPass(false);
                actionCallback.onStepFailed(getStep());
            }
        });
    }
}

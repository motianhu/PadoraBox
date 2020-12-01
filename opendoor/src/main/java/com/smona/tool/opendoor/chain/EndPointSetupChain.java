package com.smona.tool.opendoor.chain;

import android.app.Activity;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;
import com.smona.tool.opendoor.api.OpenDoorCallback;

public class EndPointSetupChain extends AbstractChain {

    private String inviteCode;
    public EndPointSetupChain(String inviteCode, ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
        this.inviteCode = inviteCode;
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        if(isCheckPass()) {
            executeNextAction(activity, actionCallback);
            return;
        }
        ApiOpenDoorImpl.getInstance().setupEndpoint(inviteCode, new OpenDoorCallback() {
            @Override
            public void onSuccess() {
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

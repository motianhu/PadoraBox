package com.smona.tool.opendoor.chain;

import android.app.Activity;

public class AuthFaceChain extends AbstractChain {

    private boolean checkFace = true;

    public AuthFaceChain(ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {
        if (checkFace) {
            actionCallback.onStepSuccess(getStep());
            executeNextAction(activity, actionCallback);
        } else {
            actionCallback.onStepFailed(getStep());
        }
    }
}

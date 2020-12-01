package com.smona.tool.opendoor.chain;

import android.app.Activity;

public class FinishChain extends AbstractChain {

    public FinishChain(ChainStep curStep, AbstractChain nextAction) {
        super(curStep, nextAction);
    }

    @Override
    public void execute(Activity activity, IActionCallback actionCallback) {

    }
}

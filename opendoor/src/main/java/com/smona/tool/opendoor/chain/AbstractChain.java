package com.smona.tool.opendoor.chain;

import android.app.Activity;

public abstract class AbstractChain implements IChain {

    private boolean checkPass = false;
    private ChainStep curStep;
    private AbstractChain nextAction;

    public AbstractChain(ChainStep curStep) {
        this(curStep, null);
    }

    public AbstractChain(ChainStep curStep, AbstractChain nextAction) {
        this.curStep = curStep;
        this.nextAction = nextAction;
    }

    public void addNextAction(AbstractChain nextAction) {
        this.nextAction = nextAction;
    }

    public void executeNextAction(Activity activity, IActionCallback actionCallback) {
        setCheckPass(true);
        if (nextAction != null) {
            nextAction.execute(activity, actionCallback);
        }
    }

    public boolean isCheckPass() {
        return checkPass;
    }

    public void setCheckPass(boolean checkPass) {
        this.checkPass = checkPass;
    }

    protected ChainStep getStep() {
        return curStep;
    }
}

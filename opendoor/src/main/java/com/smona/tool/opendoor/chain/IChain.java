package com.smona.tool.opendoor.chain;

import android.app.Activity;

public interface IChain {
    void execute(Activity activity, IActionCallback actionCallback);
}

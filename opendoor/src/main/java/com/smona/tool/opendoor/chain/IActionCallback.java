package com.smona.tool.opendoor.chain;

public interface IActionCallback {
    void onStepSuccess(ChainStep step);
    void onStepFailed(ChainStep step);
}

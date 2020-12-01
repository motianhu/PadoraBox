package com.smona.tool.opendoor.api;

public interface OpenDoorCallback {
    void onSuccess();
    void onFailed(boolean shouldRetry);
}

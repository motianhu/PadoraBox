package com.smona.app.tools;

import android.app.Application;

import com.smona.tool.opendoor.api.ApiOpenDoorImpl;

public class ModuleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiOpenDoorImpl.getInstance().init(this);
    }
}

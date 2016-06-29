package com.frankpi.videole2;

import com.frankpi.utils.CrashHandler;
import com.frankpi.utils.Log2;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class MainApplication extends Application {

    public static final String TAG = "gameassist";
    private Intent serverIntent;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        serverIntent = new Intent(getApplicationContext(), MyService.class);
        startService(serverIntent);
    }
}

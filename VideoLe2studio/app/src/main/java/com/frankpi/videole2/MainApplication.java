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
        if (!Log2.isNetworkAvailable(getApplicationContext())) {
            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            Log.i(TAG, "网络异常，软件不可用");
        } else {
            serverIntent = new Intent(getApplicationContext(), MyService.class);
            startService(serverIntent);
        }
    }
}

package com.gameassist.plugin.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gameassist.plugin.center.MyLog;
import com.gameassist.plugin.center.PluginEntry;

/**
 * Created by frankpi on 16-8-2.
 */
public class GGInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();

        if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
//            08-02 10:21:45.467 31854-31854/net.hexage.reaper I/GameAssist: hide gg
//            08-02 10:21:45.468 31854-31854/net.hexage.reaper W/System.err: android.content.pm.PackageManager$NameNotFoundException: com.iplay.assistant
//            08-02 10:21:45.468 31854-31854/net.hexage.reaper W/System.err:     at android.app.ApplicationPackageManager.getPackageInfo(ApplicationPackageManager.java:82)
//            08-02 10:21:45.468 31854-31854/net.hexage.reaper W/System.err:     at com.gameassist.plugin.utils.GGInstallReceiver.onReceive(GGInstallReceiver.java:26)
//            08-02 10:21:45.468 31854-31854/net.hexage.reaper W/System.err:     at android.app.LoadedApk$ReceiverDispatcher$Args.run(LoadedApk.java:761)
//            08-02 10:21:45.469 31854-31854/net.hexage.reaper W/System.err:     at android.os.Handler.handleCallback(Handler.java:733)
//            08-02 10:21:45.469 31854-31854/net.hexage.reaper W/System.err:     at android.os.Handler.dispatchMessage(Handler.java:95)
//            08-02 10:21:45.469 31854-31854/net.hexage.reaper W/System.err:     at android.os.Looper.loop(Looper.java:136)
//            08-02 10:21:45.469 31854-31854/net.hexage.reaper W/System.err:     at android.app.ActivityThread.main(ActivityThread.java:5016)
//            08-02 10:21:45.469 31854-31854/net.hexage.reaper W/System.err:     at java.lang.reflect.Method.invokeNative(Native Method)
//            08-02 10:21:45.470 31854-31854/net.hexage.reaper W/System.err:     at java.lang.reflect.Method.invoke(Method.java:515)
//            08-02 10:21:45.470 31854-31854/net.hexage.reaper W/System.err:     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:792)
//            08-02 10:21:45.470 31854-31854/net.hexage.reaper W/System.err:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:608)
//            08-02 10:21:45.470 31854-31854/net.hexage.reaper W/System.err:     at dalvik.system.NativeStart.main(Native Method)
            if (TextUtils.equals("com.iplay.assistant", packageName)) {
                PluginEntry.setGgvercode(303);
            }

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (TextUtils.equals("com.iplay.assistant", packageName)) {
                PluginEntry.setGgvercode(303);
            }

        } else if (TextUtils.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (TextUtils.equals("com.iplay.assistant", packageName)) {
//                try {
//                    MyLog.i("vccc"+pm.getPackageInfo(packageName,0).versionCode);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
                PluginEntry.setGgvercode(0);
            }
        }
    }

}

package com.gameassist.plugin.center;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gameassist.plugin.ActivityCallback;
import com.gameassist.plugin.Plugin;
import com.gameassist.plugin.menu.DialogPurchaseView;
import com.gameassist.plugin.utils.ClientConnect;
import com.gameassist.plugin.utils.GGInstallReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PluginEntry extends Plugin implements ActivityCallback {

    private static final String COM_GAMEASSIST_LVL_SDK = "com.gameassist.lvl.sdk";
    private static final String COM_GAMEASSIST_PLUGINMANAGER = "com.gameassist.pluginmanager";
    private static final String COM_GAMEASSIST_PLUGIN_CENTER = "com.gameassist.plugin.center";
    private static final String COM_GAMEASSIST_PLUGIN_SUPPORT = "com.gameassist.plugin.support";

    private HashMap<Activity, PluginManagerView> pluginManagerMap = new HashMap<Activity, PluginManagerView>();

    private static Plugin _instance;
    private static Context targetGame;

    private PluginManagerView pmView;
    private static String gameId;
    private ClientConnect clientConnect;

    public static int getGgvercode() {
        return ggvercode;
    }

    public static void setGgvercode(int ggvercode) {
        PluginEntry.ggvercode = ggvercode;
    }

    private static int ggvercode = 0;

    public static String getGameId() {
        return gameId;
    }

    public static Context getTargetGame() {
        return targetGame;
    }

    public static Plugin getInstance() {
        return _instance;
    }

    /**
     * 先执行
     */
    @Override
    public boolean OnPluginCreate() {
        MyLog.i("OnPluginCreate");
        targetGame = getTargetApplication();
        _instance = this;
        registerActivityCallback(this);
        return true;
    }

    @Override
    public void OnPlguinDestroy() {
    }

    @Override
    public void OnActivityResume(Activity activity) {
        PluginManagerView v = pluginManagerMap.get(activity);
        if (v != null) {
            v.onActivityResumed(activity);
        }
    }

    @Override
    public void OnActivityPause(Activity activity) {
        PluginManagerView v = pluginManagerMap.get(activity);
        if (v != null) {
            v.onActivityPaused(activity);
        }
    }

    @Override
    public void OnActivityDestroy(Activity activity) {
        PluginManagerView v = pluginManagerMap.get(activity);
        if (v != null) {
            v.onActivityDestroy(activity);
            pluginManagerMap.remove(activity);
            pmView = null;
        }
    }

    /**
     * 初始化sdk
     */
    @Override
    public void OnActivityCreate(Activity activity, Bundle bundle) {

        try {
            ApplicationInfo appInfo;
            appInfo = getTargetApplication().getPackageManager()
                    .getApplicationInfo(
                            getTargetApplication().getPackageName(),
                            PackageManager.GET_META_DATA);
            List<PackageInfo> packageInfos = getTargetApplication().getPackageManager().getInstalledPackages(0);
            for (PackageInfo pInfo :
                    packageInfos) {
                if (TextUtils.equals("com.iplay.assistant",pInfo.packageName)){
                    ggvercode = pInfo.versionCode;
                }
            }
            gameId = appInfo.metaData.getString("gameid");
            JSONObject jsobj = new JSONObject();
            jsobj.put("Function", "Init");
            jsobj.put("Gameid", gameId);
            clientConnect = new ClientConnect(getTargetApplication());
            clientConnect.connectJo(5000, jsobj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            pmView = new PluginManagerView(context);
            List<Plugin> mList = getLoadedPlugins();
            try {
                mList.remove(getLoadedPlugin(COM_GAMEASSIST_PLUGIN_CENTER));
                mList.remove(getLoadedPlugin(COM_GAMEASSIST_PLUGIN_SUPPORT));
                mList.remove(getLoadedPlugin(COM_GAMEASSIST_PLUGINMANAGER));
                getLoadedPlugin(COM_GAMEASSIST_LVL_SDK).pluginHasUI();
                mList.remove(getLoadedPlugin(COM_GAMEASSIST_LVL_SDK));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            for (Plugin plugin : mList) {
                MyLog.i(plugin.getPluginName());
                if (plugin.getPluginName().contentEquals(
                        COM_GAMEASSIST_PLUGINMANAGER)) {
                    mList.remove(plugin);
                }
            }
            pmView.addPlugins(mList);
            pmView.onActivityCreate(activity);
            pluginManagerMap.put(activity, pmView);

        } catch (Exception e) {
            e.printStackTrace();
            MyLog.e(e);
        }
    }

    @Override
    public View OnPluginUIShow() {
        return null;

    }

    @Override
    public void OnPluginUIHide() {
    }

    @Override
    public boolean pluginHasUI() {
        return false;
    }

    @Override
    public boolean pluginAutoHide() {
        return false;
    }

    private PrePurchaseTask prePurchaseTask;
    private String commodity_id;
    private String name;
    private String price;
    private String desc;

    /**
     * 关闭，隐藏，软键盘
     */
    @Override
    public Bundle pluginCall(Plugin plugin, String pluginName, int cmd,
                             Bundle params) {
        // TODO Auto-generated method stub
        switch (cmd) {
            case 0:
                pmView.closePlugin();
                break;
            case 1:
                pmView.hidePlugin();
                break;
            case 2:
                pmView.enableKeypadFocus(true);
                break;
            case 3:
                pmView.enableKeypadFocus(false);
                break;

            //获取商品信息显示购买的dialog
            case 7:
                String dataDetail = params.getString("data");
                if (TextUtils.isEmpty(dataDetail)) {
                    purcharseHandler.sendEmptyMessage(400);
                    break;
                }
                try {
                    JSONObject jsonObject = new JSONObject(dataDetail);
                    commodity_id = jsonObject.getString("commodity_id");
                    name = jsonObject.getString("name");
                    price = jsonObject.getString("price");
                    desc = jsonObject.getString("desc");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                purcharseHandler.sendEmptyMessage(7);
                break;
            case 10:
                ggvercode = params.getInt("versionCode");
                MyLog.i("vcc:" + ggvercode);
                purcharseHandler.sendEmptyMessageDelayed(10, 5000);
                break;
            default:
                break;
        }
        return super.pluginCall(plugin, pluginName, cmd, params);
    }

    private JSONObject jsonObjectBuy;

    private DialogPurchaseView dialogPurchaseView;
    private Handler purcharseHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 7:
                    dialogPurchaseView = new DialogPurchaseView(getContext());
                    pmView.frameLayoutDialog.removeAllViews();
                    pmView.frameLayoutDialog.addView(dialogPurchaseView);
                    dialogPurchaseView.comPrice.setText(price);
                    dialogPurchaseView.comName.setText(name);
                    dialogPurchaseView.comDesc.setText(desc);
                    try {
                        jsonObjectBuy = new JSONObject();
                        jsonObjectBuy.put("Function", "Buy");
                        jsonObjectBuy.put("Commodityid", commodity_id);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    dialogPurchaseView
                            .setClicklistener(new DialogPurchaseView.ClickListenerInterface() {

                                @Override
                                public void doConfirm() {
                                    // TODO Auto-generated method stub
                                    dialogPurchaseView.purchaseConfirm.setVisibility(View.GONE);
                                    dialogPurchaseView.purchaseLoading.setVisibility(View.VISIBLE);
                                    prePurchaseTask = new PrePurchaseTask();
                                    prePurchaseTask.execute(jsonObjectBuy
                                            .toString());
                                }

                                @Override
                                public void doCancel() {
                                    // TODO Auto-generated method stub
                                    pmView.frameLayoutDialog.removeAllViews();
                                }
                            });
                    break;
                case 10:
                    pmView.showUpgradeGG(ggvercode);
                    GGInstallReceiver ggInstallReceiver = new GGInstallReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
                    intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
                    intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
                    intentFilter.addDataScheme("package");
                    getTargetGame().registerReceiver(ggInstallReceiver, intentFilter);

                    break;
                case 400:
                    Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    class PrePurchaseTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String rev = null;
            try {
                ClientConnect client = new ClientConnect(getTargetApplication());
                client.connect(5000);
                client.send(jsonObjectBuy.toString().getBytes());
                rev = client.recv();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
                purcharseHandler.sendEmptyMessage(400);
            }

            return rev;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            pmView.frameLayoutDialog.removeAllViews();
            pmView.isLogin();
            super.onPostExecute(result);
        }

    }
}

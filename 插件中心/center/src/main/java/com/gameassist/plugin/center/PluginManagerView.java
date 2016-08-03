package com.gameassist.plugin.center;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.Plugin;
import com.gameassist.plugin.menu.DialogLoginView;
import com.gameassist.plugin.menu.DialogLoginView.ClickListenerInterface;
import com.gameassist.plugin.menu.DialogUpgradeView;
import com.gameassist.plugin.menu.GuideView;
import com.gameassist.plugin.menu.MessageView;
import com.gameassist.plugin.menu.ScoresView;
import com.gameassist.plugin.menu.ServiceView;
import com.gameassist.plugin.model.LoginInfo;
import com.gameassist.plugin.utils.ClientConnect;
import com.gameassist.plugin.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PluginManagerView extends LinearLayout implements
        View.OnTouchListener, View.OnClickListener {

    private static boolean firstRe = false;
    private TextView headerTVTips;
    public FrameLayout ggFrameLayoutDialog;
    private ScoresView scoresView;

    private enum ShowState {
        ShowDrag, ShowManager, ShowPlugView
    }

    public static boolean firstToastShowed = false;
    private static boolean firstToastFinished = false;
    private static boolean usrHidePluginManager = false;
    private static boolean firstPluginManagerShow = true;
    private static boolean firstPluginManagerHideTipshow = true;
    private PluginManagerViewHandler handler;
    private static Integer prevX = null, prevY = null;
    private static ShowState prevShow = ShowState.ShowManager;
    private static ShowState currentShow = ShowState.ShowDrag;
    private static View currentPluginView;
    private static View firstToastView;
    private List<Plugin> listDatas = new ArrayList<Plugin>();
    private PluginListAdapter listAdapter = new PluginListAdapter();

    private Context context;
    private Animation fadeInAnim;
    private DragViewContainer pluginContainerView;
    private View sidebarView;
    private ImageView dragView;
    private ListView listView;
    // modify
    private boolean pluginAutoHide = false, resumed = false;

    private Activity currentActivity;
    private WindowManager.LayoutParams layoutParams;
    private SensorManager sensorManager;
    private WindowManager sysWindowManager;

    private static final int SHOWTIME_FIRST_TOAST = 6000;
    private static final int SHOWTIME_FIRST_TIP = 5000;
    private static final int MSG_SHOW_PLUGINMANAGER = 0;
    private static final int MSG_HIDE_FIRST_TOAST = 3;
    private static final int MSG_SHOW_FIRST_TOAST = 4;
    private static final int MSG_SHOW_ADV = 5;
    private static final int PUSH_NEW = 100;

    long startShowPluginTime = 0, startShowPMTime = 0;

    private String tokenLogin;
    private LinearLayout loginedLL;
    private LinearLayout unloginLL;
    private TextView headerTVName;
    private ImageView headerIVAvatar;
    private TextView headerTVScores;
    private Button headerBTNLogin;
    private ClientConnect clientConnect;
    private Button headerBTNScores;
    private LoginInfo loginInfo;
    private ImageView messageRedpoint;
    private String profile;
    private String avatarPath;
    public FrameLayout frameLayoutDialog;

    private class PluginManagerViewHandler extends Handler {

        PluginManagerViewHandler(Looper l) {
            super(l);
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //刚启动logo
                case MSG_SHOW_PLUGINMANAGER:
                    if (currentActivity == null)
                        return;
                    if (!resumed || !firstToastFinished) {
                        sendEmptyMessageDelayed(MSG_SHOW_PLUGINMANAGER, 500);
                        return;
                    }
                    try {
                        WindowManager wm = currentActivity.getWindowManager();
                        if (currentActivity.getClass().getName()
                                .equals("com.apportable.activity.VerdeActivity")) {
                            if (context.checkPermission(
                                    "android.permission.SYSTEM_ALERT_WINDOW",
                                    Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    currentActivity.getPackageManager()
                                            .getPackageInfo("miui", 0);
                                    Toast.makeText(currentActivity,
                                            R.string.MIUI_WARNING,
                                            Toast.LENGTH_LONG).show();
                                } catch (Exception ignored) {
                                }
                                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                                wm = sysWindowManager;
                                MyLog.w(" <PluginManagerView.MSG_SHOW_PLUGINMANAGER> add pm to system window ... ");
                            }
                        }
                        wm.addView(PluginManagerView.this, layoutParams);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    if (firstPluginManagerShow
                            || currentShow == ShowState.ShowManager) {
                        switchShowState(ShowState.ShowDrag, ShowState.ShowManager,
                                false);
                        firstPluginManagerShow = false;
                    } else if (currentShow == ShowState.ShowPlugView
                            && currentPluginView != null) {
                        addPluginViewToContainer(currentPluginView);
                        switchShowState(ShowState.ShowManager,
                                ShowState.ShowPlugView, false);
                    } else {
                        switchShowState(ShowState.ShowManager, ShowState.ShowDrag,
                                false);
                    }
                    if (prevX != null && prevY != null) {
                        WindowManager.LayoutParams p = (WindowManager.LayoutParams) getLayoutParams();
                        p.x = prevX;
                        p.y = prevY;
                        updateLayout(p);
                    }
                    setVisibility(usrHidePluginManager ? View.GONE : View.VISIBLE);
                    if (!usrHidePluginManager) {
                        startShowPMTime = System.currentTimeMillis();
                    }
                    break;
                case MSG_SHOW_FIRST_TOAST:
                    if (firstToastShowed)
                        return;
                    firstToastShowed = true;
                    long delay = SHOWTIME_FIRST_TOAST / 2;
                    try {
                        Context target = PluginEntry.getInstance()
                                .getTargetApplication();
                        Context selfContext = PluginEntry.getInstance()
                                .getContext();
                        firstToastView = LayoutInflater.from(selfContext).inflate(
                                R.layout.plugin_manager_toast, null);
                        LinearLayout ll = (LinearLayout) firstToastView
                                .findViewById(R.id.pluginDescs);
                        int h = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 30, target
                                        .getResources().getDisplayMetrics());
                        int pad = (int) SystemInfo.dip2px(target, 6);
                        for (Plugin item : listDatas) {
                            TextView tv = new TextView(selfContext);
                            tv.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.enabled, 0, 0, 0);
                            tv.setPadding(pad, 0, 0, 0);
                            tv.setTextColor(Color.WHITE);
                            tv.setGravity(Gravity.CENTER_VERTICAL);
                            tv.setText(item.getPluginLabel());
                            ll.addView(tv, new LinearLayout.LayoutParams(
                                    LayoutParams.MATCH_PARENT, h));
                        }

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.gravity = Gravity.CENTER;
                        lp.width = LayoutParams.MATCH_PARENT;
                        lp.height = LayoutParams.MATCH_PARENT;
                        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        lp.format = PixelFormat.TRANSLUCENT;
                        lp.type = WindowManager.LayoutParams.TYPE_TOAST;
                        sysWindowManager.addView(firstToastView, lp);
                    } catch (Exception e) {
                        MyLog.e(" <PluginManagerView.MSG_SHOW_FIRST_TOAST> exception:");
                        e.printStackTrace();
                    }
                    sendEmptyMessageDelayed(MSG_HIDE_FIRST_TOAST, delay);
                    break;
                case MSG_HIDE_FIRST_TOAST:
                    firstToastFinished = true;
                    try {
                        sysWindowManager.removeView(firstToastView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    firstToastView = null;
                    break;
                //反转提示
                case MSG_SHOW_ADV:
                    Context selfContext = PluginEntry.getInstance().getContext();
                    final ImageView iv = new ImageView(selfContext);
                    iv.setImageResource(R.drawable.gg_tips);
                    iv.setScaleType(ScaleType.CENTER_INSIDE);
                    frameLayoutDialog.removeAllViews();
                    frameLayoutDialog.addView(iv);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frameLayoutDialog.removeAllViews();
                        }
                    }, SHOWTIME_FIRST_TIP);

                    break;
                case PUSH_NEW:
                    messageRedpoint.setVisibility(VISIBLE);
                    break;
                case 200:
                    if (!TextUtils.isEmpty(isLogin())) {
                        Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 400:
                    Toast.makeText(getContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    public PluginManagerView(Context context) {
        super(context);
        this.context = context;
        sysWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        handler = new PluginManagerViewHandler(Looper.getMainLooper());

        LayoutInflater.from(context).inflate(R.layout.plugin_center_main, this);
        pluginContainerView = (DragViewContainer) findViewById(R.id.pluginView);
        sidebarView = findViewById(R.id.sidebar);
        dragView = (ImageView) findViewById(R.id.dragbar);
        listView = (ListView) findViewById(R.id.pluginsList);
        listView.setAdapter(listAdapter);

        frameLayoutDialog = (FrameLayout) findViewById(R.id.purchase_dialog);
        ggFrameLayoutDialog = (FrameLayout) findViewById(R.id.gg_dialog);

        findViewById(R.id.menu_message).setOnClickListener(this);
        findViewById(R.id.menu_guide).setOnClickListener(this);
        findViewById(R.id.menu_service).setOnClickListener(this);
        findViewById(R.id.menu_comment).setOnClickListener(this);
        messageRedpoint = (ImageView) findViewById(R.id.menu_message_redpoint);
        messageRedpoint.setVisibility(GONE);

        headerTVTips = (TextView) findViewById(R.id.header_tv_tips);
        loginedLL = (LinearLayout) findViewById(R.id.header_logined);
        unloginLL = (LinearLayout) findViewById(R.id.header_unlogin);
        headerTVName = (TextView) findViewById(R.id.header_usr_name);
        headerIVAvatar = (ImageView) findViewById(R.id.header_usr_avatar);
        headerTVScores = (TextView) findViewById(R.id.header_usr_scores);
        headerBTNScores = (Button) findViewById(R.id.header_btn_scores);
        headerBTNScores.setOnClickListener(this);
        headerBTNLogin = (Button) findViewById(R.id.header_btn_login);
        headerBTNLogin.setOnClickListener(this);

        isLogin();

        setOnTouchListener(this);
        dragView.setOnTouchListener(new DragListener());
        dragView.setOnClickListener(this);

        pluginContainerView
                .setOnDragedListener(new DragViewContainer.OnDragedListener() {
                    private int startX, startY, fullWidth, fullHeight;

                    @Override
                    public void onDragingOffset(int x, int y) {
                        updateLayout(startX, startY, x, y, fullWidth,
                                fullHeight);
                    }

                    @Override
                    public void onDragStart() {
                        fullWidth = currentActivity.getWindow().getDecorView()
                                .getRight()
                                - currentActivity.getWindow().getDecorView()
                                .getLeft()
                                - pluginContainerView.getWidth();
                        fullHeight = currentActivity.getWindow().getDecorView()
                                .getBottom()
                                - currentActivity.getWindow().getDecorView()
                                .getTop()
                                - pluginContainerView.getHeight();
                        startX = layoutParams.x;
                        startY = layoutParams.y;
                    }
                });

        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            // layoutParams.gravity = Gravity.CENTER;
            layoutParams.width = LayoutParams.WRAP_CONTENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            layoutParams.format = PixelFormat.TRANSLUCENT;
            layoutParams.type = WindowManager.LayoutParams.LAST_SUB_WINDOW;
        }
        updateLayout(layoutParams);
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        handler.sendEmptyMessageDelayed(MSG_SHOW_ADV, 3000);

    }

    //新消息推送
    private void pushNew() {
        // TODO Auto-generated method stub
        try {
            ClientConnect client = new ClientConnect(PluginEntry.getTargetGame());
            String strPushNew = client.connectJo(5000,
                    new JSONObject().put("Function", "PushNew"));
            if (!TextUtils.isEmpty(strPushNew)) {
                if (strPushNew.contains("action")) {
                    handler.sendEmptyMessage(PUSH_NEW);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //是否登陆，profile
    public String isLogin() {
        firstRe = true;
        clientConnect = new ClientConnect(PluginEntry.getTargetGame());
        try {
            tokenLogin = clientConnect.connectJo(5000,
                    new JSONObject().put("Function", "isLogin"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(tokenLogin)) {
            loginedLL.setVisibility(VISIBLE);
            unloginLL.setVisibility(GONE);
            try {
                clientConnect.connect(5000);
                JSONObject jsProfile = new JSONObject();
                jsProfile.put("Function", "getProfile");
                clientConnect.send(jsProfile.toString().getBytes());
                profile = clientConnect.recv();
                clientConnect.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            JSONObject infoObject;
            try {
                if (!TextUtils.isEmpty(profile)) {
                    JSONObject jsonObject = new JSONObject(profile);
                    infoObject = jsonObject.getJSONObject("profile");
                } else {
                    infoObject = new JSONObject(tokenLogin);
                }
                if (TextUtils.isEmpty(avatarPath)) {
                    try {
                        avatarPath = clientConnect.connectJo(5000,
                                new JSONObject().put("Function", "getAvatar").put("avatarUrl", infoObject.get("avatar_url")));
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
                loginInfo = new LoginInfo();
                loginInfo.setAvatar_url(avatarPath);
                loginInfo.setScore(infoObject.getInt("score"));
                loginInfo.setNickname(infoObject.getString("nickname"));
                Bitmap bitmap = BitmapFactory.decodeFile(avatarPath);
                loginedLL.post(new Runnable() {
                    @Override
                    public void run() {
                        headerTVName.setText(loginInfo.getNickname());
                        headerTVScores.setText("" + loginInfo.getScore());
                        if (scoresView != null) {
                            scoresView.userScore.setText("" + loginInfo.getScore());
                        }
                    }
                });
                headerIVAvatar
                        .setImageBitmap(CommonUtils.makeRoundCorner(bitmap));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        } else {
            unloginLL.setVisibility(VISIBLE);
            loginedLL.setVisibility(GONE);
        }
        return tokenLogin;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //缩小隐藏
            case R.id.dragbar:
                // if (currentShow == prevShow) {
                // prevShow = ShowState.ShowManager;
                // currentShow = ShowState.ShowDrag;
                // }
                // switchShowState(currentShow, prevShow, true);

                if (currentShow == ShowState.ShowDrag) {
                    switchShowState(currentShow, prevShow, true);
                    dragView.setImageResource(R.drawable.icon);
                } else {
                    switchShowState(currentShow, ShowState.ShowDrag, true);
                    dragView.setImageResource(R.drawable.icon_normal);
                }

                return;
//            case R.id.minimun:
//                switchShowState(currentShow, ShowState.ShowDrag, true);
//                break;
//            case R.id.close:
//                hidePluginM();
//                return;
            case R.id.header_btn_scores:
                scoresView = new ScoresView(context, this, loginInfo);
                addPluginViewToContainer(scoresView);
                switchShowState(ShowState.ShowManager, ShowState.ShowPlugView,
                        false);
                break;
            case R.id.header_btn_login:
                login();
                break;
            case R.id.menu_message:
                MessageView messageView = new MessageView(context, this);
                addPluginViewToContainer(messageView);
                switchShowState(ShowState.ShowManager, ShowState.ShowPlugView,
                        false);
                messageRedpoint.setVisibility(GONE);
                break;
            case R.id.menu_guide:
                GuideView guideView = new GuideView(context, this);
                addPluginViewToContainer(guideView);
                switchShowState(ShowState.ShowManager, ShowState.ShowPlugView,
                        false);

                break;
            case R.id.menu_comment:
                ComponentName cn = new ComponentName("com.iplay.assistant",
                        "com.iplay.assistant.ui.market_new.detail_new.GameDetailActivity");
                Intent intent = new Intent();
                intent.setComponent(cn);
                intent.putExtra("extra_gameid", PluginEntry.getGameId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PluginEntry.getTargetGame().startActivity(intent);
                break;
            case R.id.menu_service:
                ServiceView serviceView = new ServiceView(context, this);
                addPluginViewToContainer(serviceView);
                switchShowState(ShowState.ShowManager, ShowState.ShowPlugView,
                        false);
                break;

        }
    }

    private void hidePluginM() {
        usrHidePluginManager = true;
        if (firstPluginManagerHideTipshow) {
            firstPluginManagerHideTipshow = false;
            Context selfContext = PluginEntry.getInstance().getContext();
            final ImageView iv = new ImageView(selfContext);
            iv.setImageResource(R.drawable.gg_tips);
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.gravity = layoutParams.gravity;
            lp.x = layoutParams.x;
            lp.y = layoutParams.y;
            lp.width = LayoutParams.WRAP_CONTENT;
            lp.height = (int) SystemInfo.dip2px(currentActivity, 100);
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            lp.format = PixelFormat.TRANSLUCENT;
            lp.type = WindowManager.LayoutParams.TYPE_TOAST;
            sysWindowManager.addView(iv, lp);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    sysWindowManager.removeView(iv);
                }
            }, SHOWTIME_FIRST_TOAST / 2);
        }
    }

    private void login() {
        if (!SystemInfo.checkConnectivity(getContext())) {
            Toast.makeText(getContext(), "未联网", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessage(400);
            return;
        }
        if (showUpgradeGG(PluginEntry.getGgvercode())) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    clientConnect.connect(0);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Function", "Login");
                    clientConnect.send(jsonObject.toString().getBytes());
                    tokenLogin = clientConnect.recv();
                    clientConnect.close();
                    handler.sendEmptyMessage(200);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(400);
                }
            }
        }).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if ((currentShow == ShowState.ShowPlugView && pluginAutoHide)) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            if ((event.getAction() == MotionEvent.ACTION_DOWN)
                    && ((x < 0) || (x >= v.getWidth()) || (y < 0) || (y >= v
                    .getHeight()))) {
                switchShowState(currentShow, ShowState.ShowDrag, true);
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                switchShowState(currentShow, ShowState.ShowDrag, true);
                return true;
            }
        }
        return false;
    }

    public void addPlugins(Collection<Plugin> plugins) {
        listDatas.clear();
        listDatas.addAll(plugins);
        listAdapter.notifyDataSetChanged();
    }

    public void onActivityCreate(Activity activity) {
        currentActivity = activity;
        handler.sendEmptyMessage(MSG_SHOW_FIRST_TOAST);
    }

    public void onActivityResumed(Activity activity) {
        if (currentActivity == activity) {
            resumed = true;
            if (sensorManager != null)
                sensorManager.registerListener(sensorEventListener,
                        sensorManager
                                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
            handler.sendEmptyMessage(MSG_SHOW_PLUGINMANAGER);
        }
        if (firstRe) {
            isLogin();
        }
        pushNew();
    }

    public void onActivityPaused(Activity activity) {
        if (currentActivity == activity) {
            resumed = false;
            hidePluginManager();
            if (sensorManager != null)
                sensorManager.unregisterListener(sensorEventListener);
        }
    }

    public void onActivityDestroy(Activity activity) {
        if (currentActivity == activity) {
            handler.removeMessages(MSG_SHOW_PLUGINMANAGER);
            WindowManager wm = currentActivity.getWindowManager();
            if (layoutParams.type == WindowManager.LayoutParams.TYPE_SYSTEM_ALERT) {
                wm = (WindowManager) currentActivity
                        .getSystemService(Context.WINDOW_SERVICE);
            }
            wm.removeView(PluginManagerView.this);
            currentActivity = null;
            tokenLogin = null;
        }
    }

    public void hidePlugin() {
        switchShowState(currentShow, ShowState.ShowDrag, true);
    }

    public void closePlugin() {
        switchShowState(currentShow, ShowState.ShowManager, true);
    }

    public void enableKeypadFocus(boolean enable) {
        try {
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) getLayoutParams();
            if (enable)
                // p.flags &= ~8 结果：p.flags
                p.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            else
                // p.flags |= 8 结果：p.flags+8
                p.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            updateLayout(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hidePluginManager() {
        setVisibility(View.GONE);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        long lastime = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                if (z < -8 && x < 5 && x > -5 && y < 5 && y > -5) {
                    if (System.currentTimeMillis() - lastime > 1500) {
                        if (getVisibility() != View.GONE) {
                            usrHidePluginManager = true;
                            hidePluginManager();
                        } else {
                            setVisibility(View.VISIBLE);
                            if (currentShow != ShowState.ShowPlugView)
                                switchShowState(currentShow,
                                        ShowState.ShowDrag, false);
                            usrHidePluginManager = false;
                            startShowPMTime = System.currentTimeMillis();
                        }
                        lastime = System.currentTimeMillis();
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private class DragListener implements View.OnTouchListener {
        private int touchX, touchY, startX, startY;
        private boolean isDraged = false;
        private int x, y;
        private int fullWidth, fullHeight;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            touchX = (int) event.getRawX();
            touchY = (int) event.getRawY();
            boolean isHandle = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    fullWidth = currentActivity.getWindow().getDecorView()
                            .getRight()
                            - currentActivity.getWindow().getDecorView().getLeft();
                    fullHeight = currentActivity.getWindow().getDecorView()
                            .getBottom()
                            - currentActivity.getWindow().getDecorView().getTop();
                    if (currentShow == ShowState.ShowDrag) {
                        FrameLayout.LayoutParams ll = (FrameLayout.LayoutParams) dragView
                                .getLayoutParams();
                        if (layoutParams.x == 0 || layoutParams.y == 0) {
                            ll.leftMargin = ll.topMargin = ll.bottomMargin = ll.rightMargin = 0;
                        }
                        dragView.setLayoutParams(ll);
                        fullWidth -= dragView.getWidth();
                        fullHeight -= dragView.getHeight();
                    } else if (currentShow == ShowState.ShowManager) {
                        fullWidth -= sidebarView.getWidth();
                        fullHeight -= sidebarView.getHeight();
                    }
                    isDraged = false;
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    x = layoutParams.x;
                    y = layoutParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDraged) {
                        updateLayout(x, y, touchX - startX, touchY - startY,
                                fullWidth, fullHeight);
                    } else if (Math.abs(touchX - startX) >= v.getWidth() / 4
                            || Math.abs(touchY - startY) >= v.getHeight() / 4) {
                        isDraged = true;
                    }
                    isHandle = isDraged;
                    break;
                case MotionEvent.ACTION_UP:
                    if (isDraged && currentShow == ShowState.ShowDrag) {
                        FrameLayout.LayoutParams ll = (FrameLayout.LayoutParams) dragView
                                .getLayoutParams();
                        if (layoutParams.x == 0) {
                            ll.leftMargin = -dragView.getWidth() / 2;
                        }
                        if (layoutParams.y == 0) {
                            ll.topMargin = -dragView.getHeight() / 2;
                        }
                        dragView.setLayoutParams(ll);
                    }
                    isHandle = isDraged;
                    isDraged = false;
                    break;
            }
            return isHandle;
        }
    }

    //视图转换
    private void switchShowState(ShowState from, ShowState to, boolean anim) {
        prevShow = from;
        currentShow = to;
        if (to == ShowState.ShowPlugView) {
            sidebarView.setVisibility(View.GONE);
            pluginContainerView.setVisibility(View.VISIBLE);
            if (anim && fadeInAnim != null)
                pluginContainerView.startAnimation(fadeInAnim);
            updateLayout((WindowManager.LayoutParams) getLayoutParams());
            startShowPluginTime = System.currentTimeMillis();
        } else {
            if (to == ShowState.ShowManager) {
                startShowPMTime = System.currentTimeMillis();
                pluginContainerView.setVisibility(View.GONE);
                sidebarView.setVisibility(View.VISIBLE);
                if (anim && fadeInAnim != null)
                    sidebarView.startAnimation(fadeInAnim);
            } else {
                sidebarView.setVisibility(View.GONE);
                pluginContainerView.setVisibility(View.GONE);
                // dragView.setVisibility(View.VISIBLE);
                // if (from == ShowState.ShowPlugView) {
                // dragView.setImageResource(R.drawable.icon_normal);
                // } else {
                // dragView.setImageResource(R.drawable.icon);
                // }
                if (anim && fadeInAnim != null)
                    dragView.startAnimation(fadeInAnim);

                FrameLayout.LayoutParams ll = (FrameLayout.LayoutParams) dragView
                        .getLayoutParams();
                if (layoutParams.x < 5) {
                    ll.leftMargin = -dragView.getWidth() / 2;
                    layoutParams.x = 0;
                }
                if (layoutParams.y < 5) {
                    ll.topMargin = -dragView.getHeight() / 2;
                    layoutParams.y = 0;
                }
                dragView.setLayoutParams(ll);
            }
            layoutParams.width = layoutParams.height = LayoutParams.WRAP_CONTENT;
            updateLayout(layoutParams);
        }
    }

    private void updateLayout(int startX, int startY, int offsetX, int offsetY,
                              int width, int height) {
        prevX = layoutParams.x = Math.min(width,
                Math.max(0, offsetX + startX));
        prevY = layoutParams.y = Math.min(height,
                Math.max(0, offsetY + startY));
        updateLayout(layoutParams);
    }

    private void updateLayout(WindowManager.LayoutParams lp) {
        // setLayoutParams(lp);
        if (currentActivity != null) {
            try {
                WindowManager wm = currentActivity.getWindowManager();
                if (layoutParams.type == WindowManager.LayoutParams.TYPE_SYSTEM_ALERT) {
                    wm = (WindowManager) currentActivity
                            .getSystemService(Context.WINDOW_SERVICE);
                }
                wm.updateViewLayout(PluginManagerView.this, lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // this.layoutParams = (WindowManager.LayoutParams)
            // getLayoutParams();
        }
    }

    private class PluginListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listDatas.size();
        }

        @Override
        public Plugin getItem(int position) {
            return listDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.plugin_item, null);
            }
            Plugin item = getItem(position);
            convertView.findViewById(R.id.icon).setVisibility(
                    item.pluginHasUI() ? View.VISIBLE : View.GONE);

            ((TextView) convertView.findViewById(R.id.label)).setText(item
                    .getPluginLabel());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Plugin item = getItem(position);
                    if (!item.pluginHasUI()) {
                        Toast.makeText(getContext(), R.string.Plugin_No_UI,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pluginAutoHide = item.pluginAutoHide();
                    View pluginView = item.OnPluginUIShow();
//                    if (TextUtils.isEmpty(tokenLogin)) {
//                        DialogLoginView dialogLoginView = new DialogLoginView(
//                                getContext());
//                        frameLayoutDialog.removeAllViews();
//                        frameLayoutDialog.addView(dialogLoginView);
//                        dialogLoginView
//                                .setClicklistener(new ClickListenerInterface() {
//
//                                    @Override
//                                    public void doConfirm() {
//                                        // TODO Auto-generated method stub
//                                        login();
//                                        frameLayoutDialog.removeAllViews();
//                                    }
//
//                                    @Override
//                                    public void doCancel() {
//                                        // TODO Auto-generated method stub
//                                        frameLayoutDialog.removeAllViews();
//                                    }
//                                });
//                        return;
//                    }
                    if (pluginView == null)
                        return;
                    addPluginViewToContainer(pluginView);
                    switchShowState(ShowState.ShowManager,
                            ShowState.ShowPlugView, false);
                }
            });
            return convertView;
        }
    }

    private void addPluginViewToContainer(View pluginView) {
        if (pluginView.getParent() != null) {
            ((ViewGroup) pluginView.getParent()).removeView(pluginView);
        }
        pluginContainerView.removeAllViews();
        pluginContainerView.addView(pluginView);
        currentPluginView = pluginView;
    }

    public boolean showUpgradeGG(int ggvercode) {
        MyLog.i("vcc:" + ggvercode);
        if (ggvercode >= 302) {
            return false;
        }
        DialogUpgradeView dialogUpgradeView = new DialogUpgradeView(getContext());
        if (ggvercode == 0) {
            dialogUpgradeView.upgradeBg.setBackgroundResource(R.drawable.upgradegg);
            dialogUpgradeView.upgradeConfirm.setText("下载GG助手");
        } else if (ggvercode < 302) {
            dialogUpgradeView.upgradeBg.setBackgroundResource(R.drawable.upgradegg2);
            dialogUpgradeView.upgradeConfirm.setText("更新GG助手");
        }
        ggFrameLayoutDialog.removeAllViews();
        ggFrameLayoutDialog.addView(dialogUpgradeView);
        dialogUpgradeView.setClicklistener(new DialogUpgradeView.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                ggFrameLayoutDialog.removeAllViews();
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://123.59.76.239/view/v1/dl?id=theapk");
                    intent.setData(content_url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PluginEntry.getTargetGame().startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            @Override
            public void doCancel() {
                ggFrameLayoutDialog.removeAllViews();
            }
        });
        return true;
    }

}

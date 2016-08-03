package com.gameassist.plugin.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameassist.plugin.center.PluginEntry;
import com.gameassist.plugin.center.PluginManagerView;
import com.gameassist.plugin.center.R;
import com.gameassist.plugin.model.LoginInfo;
import com.gameassist.plugin.utils.CommonUtils;

public class ScoresView extends FrameLayout implements OnClickListener {

    private PluginManagerView pluginManagerView;
    private LoginInfo loginInfo;
    public TextView userScore;

    public ScoresView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScoresView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public ScoresView(Context context) {
        super(context);
        init();
    }

    public ScoresView(Context context, PluginManagerView pluginManagerView,
                      LoginInfo loginInfo) {
        super(context);
        this.pluginManagerView = pluginManagerView;
        this.loginInfo = loginInfo;
        init();
    }

    void init() {
        inflate(getContext(), R.layout.plugin_menu_scores, this);
        findViewById(R.id.scores_back).setOnClickListener(this);
        findViewById(R.id.scores_earn).setOnClickListener(this);
        findViewById(R.id.scores_recharge).setOnClickListener(this);
        ImageView userAvatar = (ImageView) findViewById(R.id.scores_usr_avatar);
        TextView userName = (TextView) findViewById(R.id.scores_usr_name);
        userScore = (TextView) findViewById(R.id.scores_usr_score);
        userName.setText(loginInfo.getNickname());
        userScore.setText(loginInfo.getScore() + "");
        if (!TextUtils.isEmpty(loginInfo.getAvatar_url())) {
            try {
                userAvatar.setImageBitmap(CommonUtils.makeRoundCorner(BitmapFactory
                        .decodeFile(loginInfo.getAvatar_url())));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.scores_back:
                pluginManagerView.closePlugin();
                break;
            case R.id.scores_earn:
                ComponentName cn = new ComponentName("com.iplay.assistant", "com.iplay.assistant.ui.profile.activity.EarnScoreActivity");
                Intent intent = new Intent();
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PluginEntry.getTargetGame().startActivity(intent);

                break;
            case R.id.scores_recharge:
                ComponentName cnpay = new ComponentName("com.iplay.assistant", "com.iplay.assistant.ui.profile.activity.PayCloseActivity");
                Intent intentpay = new Intent();
                intentpay.setComponent(cnpay);
                intentpay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PluginEntry.getTargetGame().startActivity(intentpay);
                break;
            default:
                break;
        }
    }

}

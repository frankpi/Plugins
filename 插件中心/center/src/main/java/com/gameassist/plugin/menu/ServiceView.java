package com.gameassist.plugin.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.center.PluginEntry;
import com.gameassist.plugin.center.PluginManagerView;
import com.gameassist.plugin.center.R;

public class ServiceView extends FrameLayout implements OnClickListener {

    private PluginManagerView pluginManagerView;
    private TextView tvQQ;

    public ServiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ServiceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public ServiceView(Context context) {
        super(context);
        init();
    }

    public ServiceView(Context context, PluginManagerView pluginManagerView) {
        // TODO Auto-generated constructor stub
        super(context);
        this.pluginManagerView = pluginManagerView;
        init();
    }

    void init() {
        inflate(getContext(), R.layout.plugin_menu_service, this);
        findViewById(R.id.service_back).setOnClickListener(this);
        findViewById(R.id.service_copy_qq).setOnClickListener(this);
        tvQQ = (TextView) findViewById(R.id.service_qq);
        findViewById(R.id.service_send).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.service_back:
                pluginManagerView.closePlugin();
                break;
            case R.id.service_copy_qq:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvQQ.getText());
                Toast.makeText(getContext(), "复制成功，可以联系我们了。", Toast.LENGTH_LONG).show();
                break;
            case R.id.service_send:
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=3508928085";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PluginEntry.getTargetGame().startActivity(intent);
                break;
            default:
                break;
        }
    }
}

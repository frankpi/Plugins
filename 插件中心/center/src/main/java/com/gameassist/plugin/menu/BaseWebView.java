package com.gameassist.plugin.menu;

import android.content.Context;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gameassist.plugin.center.R;
import com.gameassist.plugin.center.SystemInfo;

public class BaseWebView extends LinearLayout implements OnClickListener {

    public WebView myWebView;
    private ProgressBar mProgressBar;
    private TextView tvTitle;

    public BaseWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public void init() {
        // TODO Auto-generated method stub
        LayoutInflater.from(getContext()).inflate(
                R.layout.plugin_menu_guide_webview, this);
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        height = (int) (height - SystemInfo.dip2px(getContext(), 42));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,
                height);
        setLayoutParams(params);

        myWebView = (WebView) findViewById(R.id.guideWebView);

        findViewById(R.id.ivGoBack).setOnClickListener(this);
        findViewById(R.id.ivRefresh).setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLayoutAlgorithm(
                LayoutAlgorithm.SINGLE_COLUMN);
         myWebView.setOnTouchListener(new MyTouchListener());
        myWebView.setWebViewClient(new HelloWebViewClient());
        myWebView.setWebChromeClient(new WebChromeClientBase());
    }

    private class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        }
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class WebChromeClientBase extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // Log.i("gameassist", "pro:" + newProgress);
//			 mProgressBar.setVisibility(VISIBLE);
            mProgressBar.setProgress(newProgress * 10);
//			if (newProgress >= 100) {
//				 mProgressBar.setVisibility(GONE);
//			}
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            tvTitle.setText(title);

        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url,
                                           boolean precomposed) {
            // TODO Auto-generated method stub
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            // TODO Auto-generated method stub
            return super.onCreateWindow(view, isDialog, isUserGesture,
                    resultMsg);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.ivGoBack:
                setVisibility(GONE);
                break;
            case R.id.ivRefresh:
                myWebView.reload();
                break;
        }
    }
}
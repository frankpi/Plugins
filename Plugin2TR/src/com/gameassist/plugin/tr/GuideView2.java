package com.gameassist.plugin.tr;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GuideView2 extends FrameLayout implements OnClickListener {

	public GuideView2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public GuideView2(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}

	public GuideView2(Context context) {
		super(context);
		init();
	}

	private WebView webView2;
	private ProgressBar mProgressBar;
	private TextView tvTitle;
	private View guideView;

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	void init() {
		guideView = inflate(getContext(), R.layout.floor_mainview_guide2, this);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width
				- dip2px(getContext(), 92), height);
		guideView.setLayoutParams(params);

		findViewById(R.id.ivGoBack).setOnClickListener(this);
		findViewById(R.id.ivGoForward).setOnClickListener(this);
		findViewById(R.id.ivRefresh).setOnClickListener(this);
		findViewById(R.id.ivHome).setOnClickListener(this);
		findViewById(R.id.ivMin).setOnClickListener(this);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setOnClickListener(this);
		webView2 = (WebView) findViewById(R.id.webView2);
		webView2.getSettings().setJavaScriptEnabled(true);
		webView2.getSettings()
				.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView2.loadUrl("http://wap1.yyhudong.com/html/gonglve/06df7e48.html");
		webView2.setOnTouchListener(new MyTouchListener());
		webView2.setWebViewClient(new HelloWebViewClient());
		webView2.setWebChromeClient(new WebChromeClientBase());
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
			mProgressBar.setProgress(newProgress * 10);
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
			if (webView2.canGoBack()) {
				webView2.goBack();
			}
			break;

		case R.id.ivGoForward:
			if (webView2.canGoForward()) {
				webView2.goForward();
			}
			break;

		case R.id.ivRefresh:
			webView2.reload();
			break;

		case R.id.ivHome:
			webView2.loadUrl("http://wap1.yyhudong.com/html/gonglve/06df7e48.html");
			break;

		case R.id.ivMin:
			setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

}

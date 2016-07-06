package com.gameassist.plugin.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class DesktopView {

	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayout;
	// 声明屏幕的宽高
	float x, y;
	int top;

	/**
	 * 显示DesktopLayout
	 */
	public void showDesk(View mView) {
		mWindowManager.addView(mView, mLayout);
	}

	/**
	 * 关闭DesktopLayout
	 */
	public  void closeDesk(View mView) {
		mWindowManager.removeView(mView);
	}

	/**
	 * 设置WindowManager
	 */
	public void createWindowManager(Context context) {
		// 取得系统窗体
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// 窗体的布局样式
		mLayout = new WindowManager.LayoutParams();
		// 设置窗体显示类型——TYPE_SYSTEM_ALERT(系统提示)
		mLayout.type = WindowManager.LayoutParams.TYPE_TOAST;
		// 设置窗体焦点及触摸：
		// FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
		mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// 设置显示的模式
		mLayout.format = PixelFormat.RGBA_8888;
		// 设置对齐的方法
		mLayout.gravity = Gravity.TOP | Gravity.RIGHT ;
		// 设置窗体宽度和高度
		mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
		
	}
}
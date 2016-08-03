


package com.gameassist.plugin.center;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class MyLog {
	public static final String TAG = "com.gameassist.plugin";
	public static void w(String msg){
		Log.w(TAG, commonTag() + msg);
	}
	
	public static void w(String format, Object... args) {
		w(String.format(format, args));
    }
	
	public static void e(String msg){
		Log.e(TAG, commonTag() + msg);
	}
	
	public static void e(String format, Object... args) {
		e(String.format(format, args));
    }
	
	public static void e(Exception e, String msg){
		Log.e(TAG, commonTag() + msg);
		e(e);
	}
	
	public static void e(Exception e, String format, Object... args) {
		e(e, String.format(format, args));
    }
	
	public static void e(Exception e){
		try {
			e.printStackTrace();
			for(StackTraceElement s: e.getStackTrace()){
				if(s.getClassName().startsWith("com.gameassist.")){
					Log.e(TAG, commonTag() + "     at " + s);
					return;
				}
			}
		} catch (Exception e1) {
		}
	}
	
	public static void i(String msg){
		Log.i(TAG, commonTag() + msg);
	}
	
	public static void i(String format, Object... args) {
		i(String.format(format, args));
    }
	
	public static void d(String msg){
		Log.d(TAG, commonTag() + msg);
	}
	
	public static void d(String format, Object... args) {
		d(String.format(format, args));
    }
	
	public static void v(String msg){
		Log.v(TAG, commonTag() + msg);
	}
	
	public static void v(String format, Object... args) {
		v(String.format(format, args));
    }
	
	public static String commonTag(){
		 return "(" + formatTime(System.currentTimeMillis()) + " ";
	}
	
	public static String formatTime(long time){
		return new SimpleDateFormat("MM月dd HH:mm:ss)", Locale.US).format(new Date(time));
	}
	
	private static final long TIME_ONE_MINUTE = 60 * 1000;
	private static final long TIME_ONE_HOUR = 60 * 60 * 1000;
	public static String formatTimePeriod(long time){
		StringBuilder sb = new StringBuilder();
		if(time > TIME_ONE_HOUR){
			long hour = time/TIME_ONE_HOUR;
			sb.append(hour).append("小时");
			time = time - hour * TIME_ONE_HOUR;
		}
		if(time > TIME_ONE_MINUTE){
			long hour = time/TIME_ONE_MINUTE;
			sb.append(hour).append("分钟");
			time = time - hour * TIME_ONE_MINUTE;
		}
		sb.append(time/1000).append("秒");
		return sb.toString();
	}
	
}

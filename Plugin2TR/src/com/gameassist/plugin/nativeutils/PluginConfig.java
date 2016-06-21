package com.gameassist.plugin.nativeutils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PluginConfig {
	public static SharedPreferences conf;
	public static void preSb(String key,String value){
		if (key.equals("cbLockRain") && value.equals("true")) {
			NativeUtils.nativeDoTimeCheat(12, 0, 1);
		}
		if (key.equals("mCheckBoxAlwaysFlying") && value.equals("true")) {
			NativeUtils.nativeDoRoleCheat(7, 0, 1);
		}
		if (key.equals("mCheckBoxIsFriendly") && value.equals("true")) {
			NativeUtils.nativeDoRoleCheat(4, 0, 1); 
		}
		if (key.equals("mCheckBoxMP") && value.equals("true")) {
			NativeUtils.nativeDoRoleCheat(2, 0, 1);
		}
		if (key.equals("mCheckBoxInvi") && value.equals("true")) {
			NativeUtils.nativeDoRoleCheat(3, 0, 1);
		}
		if (key.equals("mCheckBoxLockTime") && value.equals("true")) {
			NativeUtils.nativeDoTimeCheat(2, 0, 1);
		}
		if (key.equals("mCheckBoxHP") && value.equals("true")) {
			NativeUtils.nativeDoRoleCheat(1, 0, 1);
		}
		if (key.equals("floatBtnAddWing") && isNum(value)) {
			if(!value.equals("0")){
			NativeUtils.nativeDoRoleCheat(6, 0, Integer.parseInt(value));
			}
		}
		if (key.equals("mLockLightning") && value.equals("true")) {
			NativeUtils.nativeDoTimeCheat(11, 0, 1);
		}
	}
	  public static boolean isNum(String str) {
		  
	        try {
	            new BigDecimal(str);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	public static String readConfig(Context context,String key) {
		if (conf == null) {
			conf = context.getSharedPreferences("pluginConfig",Activity.MODE_PRIVATE); 
			return conf.getString(key,"empty"); //如果为空返回字符串'empty'
		}else{
			return conf.getString(key,"empty"); //如果为空返回字符串'empty'
		}
	}

	public static void saveConfig(Context context,String key,String value) {
		if (conf == null) {
			conf = context.getSharedPreferences("pluginConfig",Activity.MODE_PRIVATE); 
			SharedPreferences.Editor  editor  =  conf.edit();
			editor.putString(key,value);
			editor.commit();
		}else{
			SharedPreferences.Editor  editor  =  conf.edit();
			editor.putString(key,value);
			editor.commit();	
		}
	
	}

	public static void  initGame(Context context){
		if (conf == null) {
			conf = context.getSharedPreferences("pluginConfig",Activity.MODE_PRIVATE);
				  Iterator<?>  it = conf.getAll().entrySet().iterator();
			  while (it.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String, String> entry = (Entry<String, String>) it
						.next();
				String key = entry.getKey();
				String value = entry.getValue();
				preSb(key,value);
			}
		}else{
			Iterator<?> it = conf.getAll().entrySet().iterator();
			  while (it.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<String, String> entry = (Entry<String, String>) it
							.next();
					String key = entry.getKey();
					String value = entry.getValue();
					preSb(key,value);
				}
		}	
	}
}

package com.gameassist.plugin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 */
public class SharedPreferUtil {

   private static SharedPreferences mSharedPreferences = null;

     private  static SharedPreferences getSharedPreferences(Context context,String title){
         if (mSharedPreferences == null){
             mSharedPreferences = context.getSharedPreferences(title, Context.MODE_PRIVATE);
         }
         return mSharedPreferences;
     }

    public static void putFloat(Context context,String title,String key,float defstr){
              getSharedPreferences(context,title).edit().putFloat(key,defstr).commit();
    }
    
    public static void putString(Context context,String title,String key,String defstr){
    		getSharedPreferences(context,title).edit().putString(key,defstr).commit();
    }
    
    public static void putInt(Context context,String title,String key,int defstr){
		getSharedPreferences(context,title).edit().putInt(key,defstr).commit();
    }

    
    public static float getFloat(Context context,String title,String key,float defstr){
        return getSharedPreferences(context,title).getFloat(key,defstr);
    }

    public static String getString(Context context,String title,String key,String defstr){
    	return getSharedPreferences(context,title).getString(key,defstr);
    }

    public static int getInt(Context context,String title,String key,int defstr){
    	return getSharedPreferences(context,title).getInt(key,defstr);
    }


}

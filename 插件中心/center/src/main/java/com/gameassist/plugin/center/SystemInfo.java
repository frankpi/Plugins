package com.gameassist.plugin.center;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class SystemInfo {

	@SuppressWarnings("deprecation")
	public static String getGlobalDeviceId(final Context appContext) {
		String number = null;
		try {
			TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
			number = tm.getDeviceId();
		} catch (Exception e) {
		}

		if (TextUtils.isEmpty(number)) {
			try {
				number = System.getString(appContext.getContentResolver(), System.ANDROID_ID);
			} catch (Exception e) {
			}
		}

		if (TextUtils.isEmpty(number) && Build.VERSION.SDK_INT > 8) {
			try {
				Class<Build> cls = Build.class;
				Field field = cls.getDeclaredField("SERIAL");
				field.setAccessible(true);
				number = (String) field.get(null);
			} catch (Exception e) {
			}
		}

		if (TextUtils.isEmpty(number))
			return "UNKNOWN";
		else
			return number;
	}

	public static String getNetworkType(final Context appContext, TelephonyManager telephonyManager) {
		WifiManager wifiManager = (WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);

		// this will only work for apps which already have wifi permissions.
		try {
			if (wifiManager.isWifiEnabled()) {
				return "wifi";
			}
		} catch (Exception e) {
		}

		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "edge";
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "UMTS";
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return "unknown";
		}
		return "none";
	}


	public static boolean checkConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		try {
			NetworkInfo networkinfo = manager.getActiveNetworkInfo();
			if (networkinfo != null && networkinfo.isConnectedOrConnecting()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isWapNetwork(Context context) {
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connMgr.getActiveNetworkInfo();
			if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == 9) {
				return false;
			}
			String currentAPN = info.getExtraInfo();
			if (currentAPN == null)
				return false;
			return currentAPN.equalsIgnoreCase("cmwap") || currentAPN.equalsIgnoreCase("ctwap") || currentAPN.equalsIgnoreCase("3gwap") || currentAPN.equalsIgnoreCase("uniwap");
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isWifiNetwork(Context context) {
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connMgr.getActiveNetworkInfo();
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isMobileNetwork(Context context) {
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connMgr.getActiveNetworkInfo();
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static float dip2px(Context context, float dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return px;
	}

	public static float sp2px(Context context, float sp) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
		return px;
	}

	public static float px2sp(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (pxValue / scale + 0.5f);
	}

	public static CharSequence getLabel(Context context, String packageName, String className, String defaultValue) {
		try {
			final PackageManager pm = context.getPackageManager();
			CharSequence label = null;
			ComponentName componentName = new ComponentName(packageName, className);
			label = pm.getActivityInfo(componentName, 0).loadLabel(pm);
			if (label == null) {
				label = packageName;
			}
			return label;
		} catch (Exception e) {
		}
		return defaultValue;
	}


	public static boolean isMIUI(Context ctx) {
		boolean isMIUI = false;
		try {
			ctx.getPackageManager().getPackageInfo("miui", 0);
			isMIUI = true;
		} catch (NameNotFoundException e) {
		}
		return isMIUI;
	}


	private static Method mReadProcLines;

	public static final void readProcLines(String path, String[] reqFields, long[] outSize) {
		if (mReadProcLines == null) {
			try {
				Class<android.os.Process> cls = android.os.Process.class;
				mReadProcLines = cls.getDeclaredMethod("readProcLines", String.class, String[].class, long[].class);
			} catch (Exception e) {
			}
		}
		try {
			mReadProcLines.invoke(null, path, reqFields, outSize);
		} catch (Exception e) {
		}
	}

	public static final int getParentPid(int pid) {
		String[] procStatusLabels = {"PPid:"};
		long[] procStatusValues = new long[1];
		procStatusValues[0] = -1;
		readProcLines("/proc/" + pid + "/status", procStatusLabels, procStatusValues);
		return (int) procStatusValues[0];
	}

	public static final int getUidForPid(int pid) {
		String[] procStatusLabels = {"Uid:"};
		long[] procStatusValues = new long[1];
		procStatusValues[0] = -1;
		readProcLines("/proc/" + pid + "/status", procStatusLabels, procStatusValues);
		return (int) procStatusValues[0];
	}

	public static String getPidCmdline(int pid) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(String.format("/proc/%1$s/cmdline", pid)));
			return br.readLine().trim();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
		}
		return getCommandLineOutput(String.format("cat /proc/%1$s/cmdline", pid)).trim();
	}

	private static String getCommandLineOutput(String cmdLine) {
		String output = "";
		try {
			Process p = Runtime.getRuntime().exec(cmdLine);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				output += (line + '\n');
			}
			input.close();
		} catch (Exception e) {
		}
		return output;
	}

	public static String getCurrentCountryString(Context context) {
		return context.getResources().getConfiguration().locale.getDisplayCountry();
	}
	
	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
	

	public static String getWifiMacAddress(Context context){
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if(wifiInfo != null){
				return wifiInfo.getMacAddress();
			}
		} catch (Exception ignore) {
		}
		BluetoothAdapter bAdapt= BluetoothAdapter.getDefaultAdapter();
		if(bAdapt != null){
			return bAdapt.getAddress();
		}
		return "";
	}
	
	
	public static List<File> queryExternalStorage(Context context) {
		ArrayList<File> result = new ArrayList<File>();
		File defaultSD = Environment.getExternalStorageDirectory();
		result.add(defaultSD);
		try {
			if (Build.VERSION.SDK_INT > 14) {
				StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);// 4.0.3开始有本方法
				Method m = StorageManager.class.getDeclaredMethod("getVolumePaths");
				m.setAccessible(true);
				String[] ps = (String[]) m.invoke(sm);
				for (String p : ps) {
					File f = new File(p);
					if (f.exists() && f.canWrite() && f.canRead() && !f.getAbsolutePath().equals(defaultSD.getAbsolutePath())) {
						try {
							new StatFs(p);
							result.add(f);
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e1) {
		}
		return result;
	}
	
	public static boolean isPad(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		double screenInches = Math.sqrt(x + y);
		MyLog.i("ScreenSize:" + screenInches);
		return screenInches >= 6.0;
	}
	
}
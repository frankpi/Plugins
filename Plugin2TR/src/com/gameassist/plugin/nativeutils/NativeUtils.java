package com.gameassist.plugin.nativeutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class NativeUtils {
	
	public static native void nativeDoScreenshotCheat();
	
	public static native void nativeDoGameEvent(int flag, int arg1, int arg2);
	public static native void nativeDoTimeCheat(int flag, int arg1, int arg2);
	public static native int nativeDoAddItemCheat(int flag, int itemID, int count);  //返回0时背包已满，返回1时添加完成，返回其他时表示传入count有问题。
	public static native void nativeDoRoleCheat(int flag, int arg1, int arg2);
	public static native void nativeDoGameCheat(int flag, int arg1, int arg2);
	public static native int nativeIsInGame();
	
	
	public static void copyFilesFromAssets(Context context, String srcpath, String dstpath) {
		try {
			String fileNames[] = context.getAssets().list(srcpath);// 获取assets目录下的所有文件及目录名
			File dst = new File(dstpath);
			if (fileNames.length > 0) {// 如果是目录
				if(!dst.exists())
					dst.mkdirs();
				for (String fileName : fileNames) {
					copyFilesFromAssets(context, srcpath + "/" + fileName, dstpath + "/" + fileName);
				}
			} else {
				dst.delete();
				InputStream is = context.getAssets().open(srcpath);
				FileOutputStream fos = new FileOutputStream(dstpath);
				byte[] buffer = new byte[409600];
				int byteCount = 0;
				while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
					fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
				}
				fos.flush();// 刷新缓冲区
				is.close();
				fos.close();
				Log.w("GameAssist", "Import:" + dstpath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

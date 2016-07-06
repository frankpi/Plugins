package com.gameassist.plugin.nativeutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class NativeUtils {
	
	public static final int CHEAT1_ENABLE = 0x1;
	public static final int CHEAT2_ENABLE = 0x2;
	public static final int CHEAT3_ENABLE = 0x4;
	public static final int CHEAT4_ENABLE = 0x8;
	public static final int CHEAT5_ENABLE = 0x10;
	
	public static void copyFilesFromAssets(Context context, String srcpath, String dstpath) {
		try {
			String fileNames[] = context.getAssets().list(srcpath);// 获取assets目录下的所有文件及目录名
			File dst = new File(dstpath);
			if (fileNames.length > 0||dstpath.endsWith("/")) {// 如果是目录
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
			}
			Log.i("cccc", "co");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static native void nativeDoCheat(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat2(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat3(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat4(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat5(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat6(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat7(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat8(int flag, int arg1, int arg2);
//	public static native void nativeDoCheat9(int flag, int arg1, int arg2);
//	public static native void nativeDoCheata(int flag, int arg1, int arg2);
//	public static native void nativeDoCheatb(int flag, int arg1, int arg2);
//	
	

	
	
}

package com.gameassist.plugin.nativeutils;

import static android.opengl.GLES10.GL_RGBA;
import static android.opengl.GLES10.GL_UNSIGNED_BYTE;
import static android.opengl.GLES10.glGetIntegerv;
import static android.opengl.GLES10.glReadPixels;
import static android.opengl.GLES11.GL_VIEWPORT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ScreenshotHelper  {
    public static Bitmap bmpBuffer=null;
	public static String fileName = "GG";
	public static ShowImage listerner;
	public static File retFile=null;
	public ScreenshotHelper() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	public  static void takeScreenshot() {
	
		//Log.i("gameassist", "filename:"+fileName);
		// grab the current screen size
		int[] screenDim = new int[4];

		glGetIntegerv(GL_VIEWPORT, screenDim, 0);
		// build a buffer to hold the screenshot
		ByteBuffer buf = ByteBuffer.allocateDirect(screenDim[2] * screenDim[3]
				* 4); // 4 bytes per pixel
		// based partially on Spout's Caustic screenshot utility
		glReadPixels(screenDim[0], screenDim[1], screenDim[2], screenDim[3],
				GL_RGBA, GL_UNSIGNED_BYTE, buf);
		// now write this to a file - hand off to new thread for processing

		int width = screenDim[2];
		int height = screenDim[3];

		bmpBuffer = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		buf.rewind();
		byte[] rowBuffer = new byte[width * 4 * 2];
		int stride = width * 4;
		for (int y = 0; y < height / 2; ++y) {
			// exchange the rows to
			// invert the image somewhat in-place.
			buf.position(y * stride);
			buf.get(rowBuffer, 0, stride); // top row
			buf.position((height - y - 1) * stride);
			buf.get(rowBuffer, stride, stride); // bottom row
			buf.position((height - y - 1) * stride);
			buf.put(rowBuffer, 0, stride);
			buf.position(y * stride);
			buf.put(rowBuffer, stride, stride);
		}
		
		rowBuffer = null;
		buf.rewind();

		bmpBuffer.copyPixelsFromBuffer(buf);
		buf = null;
		listerner.onShowImage(bmpBuffer);	
		bmpBuffer = comp(bmpBuffer);
	}

	public static void clearBmp() {
		if(bmpBuffer!=null){
			bmpBuffer.recycle();
			bmpBuffer=null;
			System.gc();
		}
	}
    
	
	private static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
    
	 public static void savehotImage(){
		 if(bmpBuffer!=null){
		 File file = createOutputFile(fileName);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				bmpBuffer.compress(Bitmap.CompressFormat.PNG, 100, fos);
//				 listerner.onShowImage(bmpBuffer);	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				 if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
					}
				 }
			}	 
		 }
	 }
	 
	@SuppressLint("NewApi")
	private static  File createOutputFile(String prefix) {
		File allPicturesFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File picturesFolder = new File(allPicturesFolder, "BlockLauncher");
		picturesFolder.mkdirs();
		String currentTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US).format(new Date());
		retFile = new File(picturesFolder, prefix + "-" + currentTime
				+ ".png");
		int postFix = 1;
		while (retFile.exists()) {
			postFix++;
			retFile = new File(picturesFolder, prefix + "-" + currentTime + "_"
					+ postFix + ".png");
		}

		return retFile;
	}
	
	public   void setOnShowImageClicklisenter( ShowImage   listerner ){
		this.listerner = listerner;
	}
	
	public interface ShowImage{
		void  onShowImage(Bitmap bmp);
	}
	
}

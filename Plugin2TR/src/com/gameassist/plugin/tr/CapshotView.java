package com.gameassist.plugin.tr;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gameassist.plugin.tr.R;
import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.nativeutils.ScreenshotHelper;
import com.gameassist.plugin.nativeutils.ScreenshotHelper.ShowImage;

public class CapshotView extends FrameLayout implements OnClickListener,
		ShowImage {

	public CapshotView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public CapshotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public CapshotView(Context context) {
		super(context);
		initView();
	}

	public ImageView imageViewCapshot;
	public ProgressBar progressBar;
	public Bitmap bmp;
	Button capshot;

	private void initView() {
		inflate(getContext(), R.layout.floor_mainview_capshot, this);
		capshot = (Button) findViewById(R.id.floatBtnCapshotStart);
		capshot.setOnClickListener(this);
		findViewById(R.id.floatCapshotSave).setOnClickListener(this);
		findViewById(R.id.floatCapshotCancel).setOnClickListener(this);
		imageViewCapshot = (ImageView) findViewById(R.id.floatImageCapshot);
		//  postInvalidate();
		 // invalidate();

	}

	boolean a = true;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.floatBtnCapshotStart:
			ScreenshotHelper.clearBmp();
			capshot.setEnabled(false);
			new ScreenshotHelper()
					.setOnShowImageClicklisenter(CapshotView.this);
			NativeUtils.nativeDoScreenshotCheat();
			
			break;
		case R.id.floatCapshotSave:
			if(ScreenshotHelper.bmpBuffer!=null){
				ScreenshotHelper.savehotImage();
				Toast.makeText(getContext(), "保存成功!\n路径:" + ScreenshotHelper.retFile, Toast.LENGTH_LONG)
					.show();
				ScreenshotHelper.clearBmp();
			}else {
				Toast.makeText(getContext(), "没有要保存的图片!", Toast.LENGTH_LONG)
				.show();
			}
			postImage();
			break;
		case R.id.floatCapshotCancel:
			ScreenshotHelper.clearBmp();
			//清除显示
			if(ScreenshotHelper.retFile!=null){
			  ScreenshotHelper.retFile.delete();
			}
			postImage();
		 
			break;
		default:
			break;
		}
		invalidate();

	}

	private void postImage() {
		post(new Runnable() {	
				@Override
				public void run() {
					  imageViewCapshot.setImageResource(R.drawable.ic_unknown);
				}
			});
	}

	@Override
	public void onShowImage(final Bitmap bitmap) {
		
		  post(new Runnable() {	
			@Override
			public void run() {
				  imageViewCapshot.setImageBitmap(bitmap);
				  capshot.setEnabled(true);
			}
		});
	   
	}
	

}

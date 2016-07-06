package com.gameassist.plugin.mc;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.nativeutils.ScreenshotHelper;
import com.gameassist.plugin.nativeutils.ScreenshotHelper.ShowImage;
import com.gameassist.plugin.utils.SharedPreferUtil;
import com.squareup.picasso.Picasso;

public class DeliverView extends FrameLayout implements OnClickListener ,ShowImage{
	
	private Context context;

	public DeliverView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	public DeliverView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.context = context;
		init();
	}
	
	public DeliverView(Context context) {
		super(context);
		this.context = context;
		init();
		
	}
	
	private float saveX;
	private float saveZ;
	private float saveY;
	private TextView tvSavePos1,tvSavePos2,tvSavePos3;
	private TextView tvSaveName1,tvSaveName2,tvSaveName3;
	private ImageView mImageView1,mImageView2,mImageView3;
	private String pos;
	
	void init(){
		inflate(getContext(), R.layout.floor_mainview_deliver, this);
		findViewById(R.id.floatBtnDeliverSave1).setOnClickListener(this);
		tvSavePos1 = (TextView) findViewById(R.id.floatTextDeliverPos1);
		findViewById(R.id.floatBtnDeliverGoto1).setOnClickListener(this);
		
		tvSaveName1 = (TextView) findViewById(R.id.floatTextDeliverName1);
		tvSaveName2 = (TextView) findViewById(R.id.floatTextDeliverName2);
		tvSaveName3 = (TextView) findViewById(R.id.floatTextDeliverName3);
		
		findViewById(R.id.floatBtnDeliverSave2).setOnClickListener(this);
		tvSavePos2 = (TextView) findViewById(R.id.floatTextDeliverPos2);
		findViewById(R.id.floatBtnDeliverGoto2).setOnClickListener(this);
		
		findViewById(R.id.floatBtnDeliverSave3).setOnClickListener(this);
		tvSavePos3 = (TextView) findViewById(R.id.floatTextDeliverPos3);
		findViewById(R.id.floatBtnDeliverGoto3).setOnClickListener(this);
	
		mImageView1 = (ImageView) findViewById(R.id.floatDeliverImageView1);
		mImageView2 = (ImageView) findViewById(R.id.floatDeliverImageView2);
		mImageView3 = (ImageView) findViewById(R.id.floatDeliverImageView3);

		int currentSeed = SharedPreferUtil.getInt(getContext(), ""+NativeUtils.nativeGetMyWorldSeed(),"seed",0);
//		Log.i("gameassist", NativeUtils.nativeGetMyWorldSeed()+"1");
//	 	Log.i("gameassist", currentSeed+"2");
		if (currentSeed ==  NativeUtils.nativeGetMyWorldSeed()) {
			initPosition(currentSeed);
		}
}
	
	public void initPosition(final int seed) {
		// TODO Auto-generated method stub
		post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				float x1 = SharedPreferUtil.getFloat(getContext(), ""+seed,"x1", 0);
				float x2 = SharedPreferUtil.getFloat(getContext(), ""+seed,"x2", 0);
				float x3 = SharedPreferUtil.getFloat(getContext(), ""+seed,"x3", 0);
				if(x1>0){
					getXYZ(1);
					showPos1();
					Picasso.with(context)
						.load(new File(pos))
						.placeholder(context.getResources().getDrawable(R.drawable.ic_unknown))
						.error(context.getResources().getDrawable(R.drawable.g_icon_error))
						.into(mImageView1);
				}
				if(x2>0){
					getXYZ(2);
					showPos2();
					Picasso.with(context)
						.load(new File(pos))
						.placeholder(context.getResources().getDrawable(R.drawable.ic_unknown))
						.error(context.getResources().getDrawable(R.drawable.g_icon_error))
						.into(mImageView2);
				}
				if(x3>0){
					getXYZ(3);
					showPos3();
					Picasso.with(context)
						.load(new File(pos))
						.placeholder(context.getResources().getDrawable(R.drawable.ic_unknown))
						.error(context.getResources().getDrawable(R.drawable.g_icon_error))
						.into(mImageView3);
				}
			}
		});
	}

	int mBtnControl = 0;

	private Handler mh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
				if(ScreenshotHelper.bmpBuffer!=null||msg.what!=0){
					ScreenshotHelper.savehotImage();
					putXYZ(mBtnControl);
					Toast.makeText(getContext(), "保存成功!\n路径:" + ScreenshotHelper.retFile, Toast.LENGTH_LONG)
						.show();
					ScreenshotHelper.clearBmp();
				}else {
					Toast.makeText(getContext(), "保存失败!重新保存!", Toast.LENGTH_LONG)
					.show();
				}
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.floatBtnDeliverSave1:
			// TODO Auto-generated method stub
			mBtnControl = 1;
			new ScreenshotHelper()
				.setOnShowImageClicklisenter(DeliverView.this);
			NativeUtils.nativeDoScreenshotCheat();	
			
			break;
		case R.id.floatBtnDeliverGoto1:
			getXYZ(1);
			NativeUtils.nativeDoCheatPostXYZ(saveX, saveY, saveZ);
			break;
		case R.id.floatBtnDeliverSave2:
			mBtnControl = 2;
			new ScreenshotHelper()
				.setOnShowImageClicklisenter(DeliverView.this);
			NativeUtils.nativeDoScreenshotCheat();	
//			showPos2();
			break;
		case R.id.floatBtnDeliverGoto2:
			getXYZ(2);
			NativeUtils.nativeDoCheatPostXYZ(saveX, saveY, saveZ);
			break;
		case R.id.floatBtnDeliverSave3:
			mBtnControl = 3;
			new ScreenshotHelper()
				.setOnShowImageClicklisenter(DeliverView.this);
			NativeUtils.nativeDoScreenshotCheat();	
//			showPos3();
			break;
		case R.id.floatBtnDeliverGoto3:
			getXYZ(3);
			NativeUtils.nativeDoCheatPostXYZ(saveX, saveY, saveZ);
			break;
		}
	}

	private void showPos1() {
		tvSavePos1.setText("X:" + Math.round(saveX) + "|Y:"
				+ Math.round(saveY) + "|Z:" + Math.round(saveZ));
		tvSaveName1.setText("传送位置1");
	}
	
	private void showPos2() {
		tvSavePos2.setText("X:" + Math.round(saveX) + "|Y:"
				+ Math.round(saveY) + "|Z:" + Math.round(saveZ));
		tvSaveName2.setText("传送位置2");

	}
	
	private void showPos3() {
		tvSavePos3.setText("X:" + Math.round(saveX) + "|Y:"
				+ Math.round(saveY) + "|Z:" + Math.round(saveZ));
		tvSaveName3.setText("传送位置3");

	}


	private void getXYZ(int index){
		int seed = NativeUtils.nativeGetMyWorldSeed();
//		Log.i("gameassist", "chick"+seed+"get");
		saveX = SharedPreferUtil.getFloat(getContext(), ""+seed,"x"+index, 0);
		saveY = SharedPreferUtil.getFloat(getContext(), ""+seed,"y"+index, 0);
		saveZ = SharedPreferUtil.getFloat(getContext(), ""+seed,"z"+index, 0);
		pos = SharedPreferUtil.getString(getContext(), ""+seed, "pos"+index, "null");
//		SharedPreferUtil.getInt(getContext(), "position"+seed,"seed",0);
		
		
	}
	
	private void putXYZ(int index) {
		saveX = NativeUtils.nativeDoCallbackGetX();
		saveY = NativeUtils.nativeDoCallbackGetY();
		saveZ = NativeUtils.nativeDoCallbackGetZ();
		int seed = NativeUtils.nativeGetMyWorldSeed();
		File pos = ScreenshotHelper.retFile;
		SharedPreferUtil.putFloat(getContext(), ""+seed,"x"+index, saveX);
		SharedPreferUtil.putFloat(getContext(), ""+seed,"y"+index, saveY);
		SharedPreferUtil.putFloat(getContext(), ""+seed,"z"+index, saveZ);
		SharedPreferUtil.putString(getContext(), ""+seed,"pos"+index,pos+"");
		SharedPreferUtil.putInt(getContext(), ""+NativeUtils.nativeGetMyWorldSeed(),"seed",NativeUtils.nativeGetMyWorldSeed());
//		Log.i("gameassist", seed+"put");
	}

	@Override
	public void onShowImage(final Bitmap bitmap) {
		  post(new Runnable() {	
			@Override
			public void run() {
				Message msg = mh.obtainMessage();
				if(mBtnControl ==1){
					mImageView1.setImageBitmap(bitmap);
					showPos1();
				}else if(mBtnControl == 2){
					mImageView2.setImageBitmap(bitmap);
					showPos2();
				}else if (mBtnControl ==3) {
					mImageView3.setImageBitmap(bitmap);
					showPos3();
				}
				msg.what = mBtnControl;
				mh.sendEmptyMessage(msg.what);
			}
		});
	}

}

package com.gameassist.plugin.mc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.utils.DesktopView;

//import com.gameassist.plugin.mc.PluginEntry.SubviewObserver;

public class GameView extends FrameLayout implements OnClickListener,OnSeekBarChangeListener{

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}
	
	public GameView(Context context) {
		super(context);
		init();
	}
	
	private SeekBar sbTime;
	private SeekBar sbWeather,mSeekBarGameView;
	private TextView tvTime;
	private TextView tvWeather;
	private CheckBox mCheckBoxLockTime,mCheckBoxNoDrop;
	private RadioButton mRadioButtonCreate,mRadioButtonLive;

	DesktopView mDesktopView= new DesktopView();
	
	void init(){
		inflate(getContext(), R.layout.floor_mainview_game, this);
		tvTime =(TextView) findViewById(R.id.tv_time);
		sbTime = (SeekBar) findViewById(R.id.sb_time);
		sbTime.setOnSeekBarChangeListener(this);
		tvWeather =(TextView) findViewById(R.id.tv_weather);
		sbWeather = (SeekBar) findViewById(R.id.sb_weather);
		sbWeather.setOnSeekBarChangeListener(this);
		
		findViewById(R.id.floatBtnKillSelf).setOnClickListener(this);
		
		mCheckBoxLockTime = (CheckBox) findViewById(R.id.cb_lockTime);
		mCheckBoxLockTime.setOnClickListener(this);
		
		mSeekBarGameView = (SeekBar) findViewById(R.id.sb_gameview);
		mSeekBarGameView.setOnSeekBarChangeListener(this);
		
		mRadioButtonLive = (RadioButton) findViewById(R.id.rbRoleLive);
		mRadioButtonLive.setOnClickListener(this);
		mRadioButtonCreate = (RadioButton) findViewById(R.id.rbRoleCreate);
		mRadioButtonCreate.setOnClickListener(this);
		if (NativeUtils.nativeGetGameCurrentMode()==1) {
			mRadioButtonCreate.setChecked(true);
		}else {
			mRadioButtonLive.setChecked(true);
		}
		
		mCheckBoxNoDrop = (CheckBox) findViewById(R.id.cb_diednodrop);
		mCheckBoxNoDrop.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbRoleCreate:
			NativeUtils.nativeDoGameCheat(1, 0, 1);
			break;
		case R.id.rbRoleLive:
			NativeUtils.nativeDoGameCheat(1, 0, 0);
			break;
		case R.id.cb_lockTime:
			if (mCheckBoxLockTime.isChecked()) {
				NativeUtils.nativeDoCheat(11, 0, 1);
			}else {
				NativeUtils.nativeDoCheat(11, 0, 0);
			}
			break;
		case R.id.floatBtnKillSelf:
				NativeUtils.nativeDoGameCheat(18, 0, 1);
			break;
		case R.id.cb_diednodrop:
			if (mCheckBoxNoDrop.isChecked()) {
				NativeUtils.nativeDoGameCheat(2, 0, 1);
			}else {
				NativeUtils.nativeDoGameCheat(2, 0, 0);
			}
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {
		case R.id.sb_time:
			String a = "";
			if (progress >=0 && progress <25) {
				sbTime.setProgress(0);
				a = "早上";
				NativeUtils.nativeDoCheat(3, 0, 0);
			} else if (progress >25 && progress <= 75) {
				sbTime.setProgress(50);
				a = "黄昏";
				NativeUtils.nativeDoCheat(3, 0, 1);
			} else if (progress > 75 && progress <= 100) {
				sbTime.setProgress(100);
				a = "晚上";
				NativeUtils.nativeDoCheat(3, 0, 2);
			}
			tvTime.setText("时间:" + a);
			break;
			
		case R.id.sb_weather:	
			String a1 = "";
			if (progress >=0 && progress <25) {
				sbWeather.setProgress(0);
				NativeUtils.nativeDoCheat(4, 0, 0);
				a1 = "关";
			} else if (progress >25 && progress <= 75) {
				sbWeather.setProgress(50);
				NativeUtils.nativeDoCheat(4, 0, 1);
				a1 = "小";
			} else if (progress > 75 && progress <= 100) {
				sbWeather.setProgress(100);
				a1 = "大";
				NativeUtils.nativeDoCheat(4, 0, 2);
			}
			tvWeather.setText("雨雪:" + a1);
			break;
			
		case R.id.sb_gameview:	
			String a11 = "";
			if (progress >=0 && progress <25) {
				mSeekBarGameView.setProgress(0);
				NativeUtils.nativeDoCheat(5, 0, 0);
				a11 = "关";
			} else if (progress >25 && progress <= 75) {
				mSeekBarGameView.setProgress(50);
				NativeUtils.nativeDoCheat(5, 0, 1);
				a11 = "小";
			} else if (progress > 75 && progress <= 100) {
				mSeekBarGameView.setProgress(100);
				a11 = "大";
				NativeUtils.nativeDoCheat(5, 0, 2);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}

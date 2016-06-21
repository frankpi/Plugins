package com.gameassist.plugin.tr;

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
import android.widget.Toast;

import com.gameassist.plugin.tr.R;
import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.nativeutils.PluginConfig;

//import com.gameassist.plugin.mc.PluginEntry.SubviewObserver;

public class GameView extends FrameLayout implements OnClickListener{

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
	
	private CheckBox mCheckBoxLockTime,mLockRain,mLockLightning;
	private RadioButton rbGameDay,rbGameNight;
	private SeekBar timeSpeedBar;
	private TextView timeSpeedTextView;
	
	void init(){
		inflate(getContext(), R.layout.floor_mainview_game, this);
		mCheckBoxLockTime = (CheckBox) findViewById(R.id.cb_lockTime);
		mCheckBoxLockTime.setOnClickListener(this);
		String stat=PluginConfig.readConfig(getContext(), "mCheckBoxLockTime");
		if(stat.equals("true")){
			mCheckBoxLockTime.setChecked(true);	
		}
		rbGameDay = (RadioButton) findViewById(R.id.rbGameDay);
		rbGameDay.setOnClickListener(this);
		rbGameNight = (RadioButton) findViewById(R.id.rbGameNight);
		rbGameNight.setOnClickListener(this);
		
		mLockLightning = (CheckBox) findViewById(R.id.cbLockLightning);
		mLockLightning.setOnClickListener(this);
		stat=PluginConfig.readConfig(getContext(), "mLockLightning");
		if(stat.equals("true")){
			mLockLightning.setChecked(true);	
		}
		mLockRain = (CheckBox) findViewById(R.id.cbLockRain);
		mLockRain.setOnClickListener(this);
		stat=PluginConfig.readConfig(getContext(), "mLockRain");
	//	Log.i("GameAssist",""+stat);
		if(stat.equals("true")){
			mLockRain.setChecked(true);	
		}
		timeSpeedBar=(SeekBar) findViewById(R.id.timeSpeed);
		timeSpeedTextView=(TextView) findViewById(R.id.timeSpeedText);
		timeSpeedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(progress==0){
					timeSpeedTextView.setText("时间加速:"+timeSpeedBar.getProgress()+"X");
					NativeUtils.nativeDoTimeCheat(17, 0,0);	
				}else if(progress>0&&progress<=50){
					seekBar.setProgress(50);
					timeSpeedTextView.setText("时间加速:"+10+"X");
					NativeUtils.nativeDoTimeCheat(17, 0,10);
				}else if(progress>50&&progress<100){
					seekBar.setProgress(100);	
				timeSpeedTextView.setText("时间加速:"+20+"X");
				NativeUtils.nativeDoTimeCheat(17, 0,20);
				}
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rbGameDay:
			NativeUtils.nativeDoTimeCheat(3, 0, 1);
			Toast.makeText(getContext(), rbGameDay.getText(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.rbGameNight:
			NativeUtils.nativeDoTimeCheat(4, 0, 1);
			Toast.makeText(getContext(), rbGameNight.getText(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.cb_lockTime:
			if (mCheckBoxLockTime.isChecked()) {
				NativeUtils.nativeDoTimeCheat(2, 0, 1);
				PluginConfig.saveConfig(getContext(), "mCheckBoxLockTime", "true");
			}else {
				NativeUtils.nativeDoTimeCheat(2, 0, 0);
				PluginConfig.saveConfig(getContext(), "mCheckBoxLockTime", "false");
			}
			break;
		case R.id.cbLockLightning:
			if (mLockLightning.isChecked()) {
				NativeUtils.nativeDoTimeCheat(11, 0, 1);
				PluginConfig.saveConfig(getContext(), "mLockLightning", "true");
			}else {
				NativeUtils.nativeDoTimeCheat(11, 0, 0);
				PluginConfig.saveConfig(getContext(), "mLockLightning", "false");
			}
			break;
		case R.id.cbLockRain:
			if (mLockRain.isChecked()) {
				NativeUtils.nativeDoTimeCheat(12, 0, 1);
				PluginConfig.saveConfig(getContext(), "mLockRain", "true");
			}else {
				NativeUtils.nativeDoTimeCheat(12, 0, 0);
				PluginConfig.saveConfig(getContext(), "mLockRain", "false");
			}
			break;
		default:
			break;
		}
		// TODO Auto-generated method stub
		
	}
}

package com.gameassist.plugin.mc;

import java.util.Timer;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.utils.DesktopView;

public class RoleView extends FrameLayout implements OnClickListener,OnSeekBarChangeListener {

	
	public RoleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public RoleView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}
	
	public RoleView(Context context) {
		super(context);
		init();
	}

	private CheckBox cbAnimalMove,cbShowPos;
	private CheckBox cbFly;
	private CheckBox cbInvi;
	private SeekBar sbLevel;
	private TextView tvLevel;
	private SeekBar sbSpeed;
	private TextView tvSpeed;
	
	public TextView tvPos;
	
	void init(){
		inflate(getContext(), R.layout.floor_mainview_role, this);
		tvLevel = (TextView) findViewById(R.id.tv_roleLevel);
		sbLevel = (SeekBar) findViewById(R.id.sb_roleLevel);
		sbLevel.setOnSeekBarChangeListener(this);
		tvSpeed = (TextView) findViewById(R.id.tvRoleSpeed);
		sbSpeed = (SeekBar) findViewById(R.id.sbRoleSpeed);
		sbSpeed.setOnSeekBarChangeListener(this);
		cbFly = (CheckBox) findViewById(R.id.cb_fly);
		cbFly.setOnClickListener(this);
		cbAnimalMove =(CheckBox) findViewById(R.id.cb_animalMove);
		cbAnimalMove.setOnClickListener(this);
		cbInvi = (CheckBox) findViewById(R.id.cb_invincible);
		cbInvi.setOnClickListener(this);
		//坐标
//		cbShowPos = (CheckBox) findViewById(R.id.cb_showPos);
//		cbShowPos.setOnClickListener(this);
		
	}
	/*
	 * 显示坐标
	 */
	private Handler mh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==0) {
				int x = Math.round(NativeUtils.nativeDoCallbackGetX());
				int y = Math.round(NativeUtils.nativeDoCallbackGetY());
				int z = Math.round(NativeUtils.nativeDoCallbackGetZ());
				tvPos.setText("X:" + x + "|Y:" + y + "|Z:" + z);
			}
		}
	};
	
    boolean posControl2 = true;
    public Timer mTimerShowPos;  
    public DesktopView desktop;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.cb_invincible:
				if (cbInvi.isChecked()) {
					NativeUtils.nativeDoCheat(2, 0, 1);
				} else {
					NativeUtils.nativeDoCheat(2, 0, 0);
				}
				break;
			case R.id.cb_fly:
				if (cbFly.isChecked()) {
					NativeUtils.nativeDoCheat(16, 0, 1);
				} else {
					NativeUtils.nativeDoCheat(16, 0, 0);
				}
				break;
			case R.id.cb_animalMove:
				if (cbAnimalMove.isChecked()) {
					NativeUtils.nativeDoCheat(14, 0, 1);
				} else {
					NativeUtils.nativeDoCheat(14, 0, 0);
				}
				break;
//			case R.id.cb_showPos:
//				if(posControl2){
//					desktop = new DesktopView();
//					tvPos = new TextView(getContext());
//					tvPos.setTextColor(Color.WHITE);
//					tvPos.setLayoutParams(new LayoutParams(200,200));
//					desktop.createWindowManager(getContext());
//					posControl2= false;
//				}
//				if (cbShowPos.isChecked()) {
//					mTimerShowPos = new Timer(); 
//					desktop.showDesk(tvPos);
//					mTimerShowPos.schedule(new TimerTask() {  
//			            @Override  
//			            public void run() {  
//			                Message message = new Message();  
//			                message.what = 0;  
//			                mh.sendMessage(message);  
//			            }  
//			        }, 10, 500/* 表示1000毫秒之後，每隔1000毫秒執行一次 */); 
//				} else {
//					desktop.closeDesk(tvPos);
//					mTimerShowPos.cancel();
//					mTimerShowPos= null;
//				}
//				break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
//		Log.i("gameassist", seekBar.getId()+"");
		switch (seekBar.getId()) {
		case R.id.sb_roleLevel:
			tvLevel.setText("级别:" + progress);
			NativeUtils.nativeDoCheat(1, 0, progress);
			break;
		case R.id.sbRoleSpeed:
			tvSpeed.setText("速度:" + progress);
			NativeUtils.nativeDoRoleCheat(2, 0, progress);
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

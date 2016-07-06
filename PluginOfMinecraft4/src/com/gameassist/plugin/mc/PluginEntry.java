package com.gameassist.plugin.mc;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gameassist.plugin.Plugin;
import com.gameassist.plugin.nativeutils.NativeUtils;

public class PluginEntry extends Plugin implements OnClickListener{

//	interface SubviewObserver();

	private static final String LIB_HOOK_PLUGIN = "pgodmode";
	public static Context ctx;
	private Timer mTimer;
	private int misInGame;
	private static int preLocalPlayer;
	@Override
	public boolean OnPluginCreate() {
		System.loadLibrary(LIB_HOOK_PLUGIN);
		getPluginManager().hideLibrary(this, LIB_HOOK_PLUGIN);
		ctx = context;
		mTimer = new Timer(); 
		mTimer.schedule(new TimerTask() {  
	        @Override  
	        public void run() {  
	            Message message = new Message();  
	            message.what = 0;  
	            mdh.sendMessage(message);  
	        }  
	    }, 10, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */); 
		return false;

	}

	public boolean pluginHasUI() {
		return true;
	}

	public boolean pluginAutoHide() {
		return false;
	}
	
	@Override
	public void OnPlguinDestroy() {

		mTimer.cancel();
		mTimer= null;
	}

	@Override
	public void OnPluginUIHide() {
		
	}

	private RadioButton radioMenuDeliver, radioMenuGame, radioMenuRole;
	private FrameLayout frame,gameFrame,roleFrame;
	private View pluginView;
	private GameView subGameView;
	private RoleView subRoleView;
	private DeliverView subDeliverView;
	private CapshotView mCapshotView;
	private PackView mPackView;
    private boolean ishide=true;
	
	private Handler mdh = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==0) {
				misInGame = NativeUtils.nativeIsInGame();
//				Log.i("gameassist", "local"+mLocalPlayer+"\npre:"+preLocalPlayer);
				if (misInGame==0) {
					 pluginView = null;
					 //closeSelf(); 
					 if(ishide){
						 closeSelf(); 
						 //hideSelf();
						 ishide=false;
					 }
				}else{
					ishide=true;	
				}
			}
		}
	};
	   
	
	@Override
	public View OnPluginUIShow() {
		misInGame = NativeUtils.nativeIsInGame();
		if(misInGame==0){
			Toast.makeText(getContext(), "请进入游戏后开启插件!", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		if (pluginView == null) {
			pluginView = LayoutInflater.from(getContext()).inflate(
					R.layout.floor_mainview, null);
			pluginView.findViewById(R.id.close).setOnClickListener(this);
			pluginView.findViewById(R.id.minimum).setOnClickListener(this);
			/*
			 * 主菜单控制
			 */
			radioMenuDeliver = (RadioButton) pluginView
					.findViewById(R.id.RadioMenuDeliver);
			radioMenuDeliver.setOnClickListener(this);
			radioMenuRole = (RadioButton) pluginView
					.findViewById(R.id.RadioMenuRole);
			radioMenuRole.setOnClickListener(this);
			radioMenuGame = (RadioButton) pluginView
					.findViewById(R.id.RadioMenuGame);
			radioMenuGame.setOnClickListener(this);
			
			pluginView.findViewById(R.id.RadioMenuPack).setOnClickListener(this);
			pluginView.findViewById(R.id.RadioMenuCapshot).setOnClickListener(this);
			
			/*
			 * 子布局
			 */
			frame = (FrameLayout) pluginView.findViewById(R.id.floatLayoutFrame);
			roleFrame= (FrameLayout) pluginView.findViewById(R.id.floatLayoutRoleFrame);
			gameFrame = (FrameLayout) pluginView.findViewById(R.id.floatLayoutGameFrame);

			subRoleView = new RoleView(context);
			subGameView = new GameView(context);
			roleFrame.addView(subRoleView);
			gameFrame.addView(subGameView);
			gameFrame.setVisibility(View.GONE);
		}
		return pluginView;
	}


	/*
	 * 主菜单控制
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.close:
			closeSelf();
			break;
		case R.id.minimum:
			hideSelf();
			break;
		case R.id.RadioMenuRole:
			roleFrame.setVisibility(View.VISIBLE);
			gameFrame.setVisibility(View.GONE);
			frame.setVisibility(View.GONE);
			break;
		case R.id.RadioMenuGame:
			roleFrame.setVisibility(View.GONE);
			gameFrame.setVisibility(View.VISIBLE);
			frame.setVisibility(View.GONE);
			break;
		case R.id.RadioMenuDeliver:
			roleFrame.setVisibility(View.GONE);
			gameFrame.setVisibility(View.GONE);
			frame.setVisibility(View.VISIBLE);
			frame.removeAllViews();
			subDeliverView = new DeliverView(context);
			frame.addView(subDeliverView);
			break;
		case R.id.RadioMenuPack:
			roleFrame.setVisibility(View.GONE);
			gameFrame.setVisibility(View.GONE);
			frame.setVisibility(View.VISIBLE);
			frame.removeAllViews();
			mPackView = new PackView(context);
			frame.addView(mPackView);
			break;
		case R.id.RadioMenuCapshot:
			roleFrame.setVisibility(View.GONE);
			gameFrame.setVisibility(View.GONE);
			frame.setVisibility(View.VISIBLE);
			frame.removeAllViews();
			mCapshotView = new CapshotView(context);
			frame.addView(mCapshotView);
			break;
		
		}
	}
}

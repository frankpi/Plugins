package com.gameassist.plugin.tr;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gameassist.plugin.Plugin;
import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.nativeutils.PluginConfig;

public class PluginEntry extends Plugin{

//	interface SubviewObserver();

	private static final String LIB_HOOK_PLUGIN = "pgodmode";
	public static Context targetContext;
	private Timer mTimer,startGameinitStat,stopGameinitStat;
	
	private int misInGame;
	@Override
	public boolean OnPluginCreate() {
		System.loadLibrary(LIB_HOOK_PLUGIN);
		getPluginManager().hideLibrary(this, LIB_HOOK_PLUGIN);
		targetContext = getTargetApplication();
		mTimer = new Timer(); 
		mTimer.schedule(new TimerTask() {  
	        @Override  
	        public void run() {  
	            Message message = new Message();  
	            message.what = 0;  
	            mdh.sendMessage(message);  
	        }  
	    }, 10, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */); 
		startGameinitStat = new Timer(); 
		startGameinitStat.schedule(new TimerTask() {  
	        @Override  
	        public void run() {  
	            Message message = new Message();  
	            message.what = 0;  
	            mdhStart.sendMessage(message);  
	            
	        }  
	    }, 10, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */); 
		mWingView = new WingView(getContext());
		mFestivalView = new FestivalView(getContext());
		mCapshotView = new CapshotView(context);
		mEventView = new EventView(getContext());			
		mPackView = new PackView(getContext());
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

	private FrameLayout frame;
	private View pluginView;
	private GameView subGameView;
	private RoleView subRoleView;
	private CapshotView mCapshotView;
	private WingView mWingView;
	private FestivalView mFestivalView;
	private EventView mEventView;
	private PackView mPackView;
	private GuideView2 mGuideView2;
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
						 ishide=false;
					 }
				}else{
					ishide=true;	
				}
			}
		}
	};
	private Handler mdhStart = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==0) {
				misInGame = NativeUtils.nativeIsInGame();

				if (misInGame!=0) {
					PluginConfig.initGame(getContext());
					stopGameinitStat = new Timer(); 
					stopGameinitStat.schedule(new TimerTask() {  
				        @Override  
				        public void run() {  
				           Message message = new Message();  
				            message.what = 0;  
				            mdhStop.sendMessage(message);  
				        }  
				    }, 10, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */); 
					startGameinitStat.cancel(); //取消当前线程
					startGameinitStat=null;
				}
			}
		}
	};
	private Handler mdhStop = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==0) {
				misInGame = NativeUtils.nativeIsInGame();
//				Log.i("gameassist", "local"+mLocalPlayer+"\npre:"+preLocalPlayer);
				if (misInGame==0) {
					startGameinitStat = new Timer(); 
					startGameinitStat.schedule(new TimerTask() {  
				        @Override  
				        public void run() {  
				           Message message = new Message();  
				            message.what = 0;  
				            mdhStart.sendMessage(message);  
				        }  
				    }, 10, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */); 
					stopGameinitStat.cancel(); //取消当前线程
					stopGameinitStat=null;	
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
			pluginView.findViewById(R.id.close).setOnClickListener(new SubControlOnClickListener());
			pluginView.findViewById(R.id.minimum).setOnClickListener(new SubControlOnClickListener());
			
			/*
			 * 主菜单控制
			 */
			pluginView.findViewById(R.id.radioMenuWing).setOnClickListener(new MenuControlOnClickListener());
			pluginView.findViewById(R.id.radioMenuFestival).setOnClickListener(new MenuControlOnClickListener());
			pluginView.findViewById(R.id.radioMenuRole).setOnClickListener(new MenuControlOnClickListener());
			pluginView.findViewById(R.id.radioMenuGame).setOnClickListener(new MenuControlOnClickListener());
			
			pluginView.findViewById(R.id.radioMenuCapshot).setOnClickListener(new MenuControlOnClickListener());
			pluginView.findViewById(R.id.radioMenuEvents).setOnClickListener(new MenuControlOnClickListener());
			pluginView.findViewById(R.id.radioMenuGuide).setOnClickListener(new MenuControlOnClickListener());
//			pluginView.findViewById(R.id.radioMenuPack).setOnClickListener(new MenuControlOnClickListener());//物品添加
			
			/*
			 * 子布局
			 */
			frame = (FrameLayout) pluginView.findViewById(R.id.floatLayoutFrame);

			subRoleView = new RoleView(context);
			subGameView = new GameView(context);
			mGuideView2 = new GuideView2(context);
			frame.addView(subRoleView);
		}
		return pluginView;
	}

	/*
	 * 主菜单控制
	 */
	
	private class SubControlOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			enableKeypadFocus(false);
			switch (v.getId()) {
			case R.id.close:
				closeSelf();
				break;
			case R.id.minimum:
				hideSelf();
				break;
			}
		}
	}
	
	private class MenuControlOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			enableKeypadFocus(false);
			frame.removeAllViews();
			switch (v.getId()) {
			case R.id.radioMenuRole:
				frame.addView(subRoleView);
				break;
			case R.id.radioMenuGame:
				frame.addView(subGameView);
				break;
	
			case R.id.radioMenuWing:
				frame.addView(mWingView);
				break;
			
			case R.id.radioMenuFestival:
				frame.addView(mFestivalView);
				break;

			case R.id.radioMenuEvents:
				frame.addView(mEventView);

				break;
				//物品添加
//			case R.id.radioMenuPack:
//				enableKeypadFocus(true);
//				frame.addView(mPackView);
//				break;
			case R.id.radioMenuGuide:
				mGuideView2.setVisibility(View.VISIBLE);
				frame.addView(mGuideView2);
				break;
			case R.id.radioMenuCapshot:
				frame.addView(mCapshotView);
				break;
			}
		}
	}
}

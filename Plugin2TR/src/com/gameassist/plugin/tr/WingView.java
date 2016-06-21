package com.gameassist.plugin.tr;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.gameassist.plugin.adapter.WingsGridViewAdapter;
import com.gameassist.plugin.entity.WingItem;
import com.gameassist.plugin.tr.R;
import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.nativeutils.PluginConfig;


public class WingView extends FrameLayout implements OnClickListener{

	private GridView mGirdViewItem;

	private  List<WingItem> mListWings = new ArrayList<WingItem>();
	private WingsGridViewAdapter mWingsGridViewAdapter;

	public WingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
		initView();
	}

	public WingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
		initView();
	}
	
	public WingView(Context context) {
		super(context);
		initData();
		initView();
	}

	private CheckBox mCheckBoxAlwaysFlying;
	private Button mBtnClear;
	private void initView() {
		
		inflate(getContext(),R.layout.floor_mainview_wings,this);
		mGirdViewItem = (GridView)findViewById(R.id.floatGridItemList);
		mWingsGridViewAdapter = new WingsGridViewAdapter(getContext());
		mGirdViewItem.setAdapter(mWingsGridViewAdapter);
		findViewById(R.id.floatBtnAddWing).setOnClickListener(this);
		mBtnClear = (Button) findViewById(R.id.floatBtnWingClear);
		mBtnClear.setOnClickListener(this);
		mCheckBoxAlwaysFlying = (CheckBox) findViewById(R.id.cbFlying);
		mCheckBoxAlwaysFlying.setOnClickListener(this);
		String stat =PluginConfig.readConfig(getContext(), "mCheckBoxAlwaysFlying");
		if(stat.equals("true")){
			mCheckBoxAlwaysFlying.setChecked(true);	
		}
		mWingsGridViewAdapter.addDatas(mListWings);
		  String oder =PluginConfig.readConfig(getContext(), "floatBtnAddWing");
		  if(PluginConfig.isNum(oder)){
			  int oderid=Integer.parseInt(oder);
		  if(Integer.parseInt(oder)>0){
			 mWingsGridViewAdapter.setIsSelected(oderid,true);  
			 mBtnClear.setEnabled(true);
		  }
		  }
		
	}

	private void initData() {
		mListWings.add(new WingItem(2280, 0, false, "蜜蜂之翼", 0));
	    mListWings.add(new WingItem(2609, 0, false, "猪鲨翅膀", 1));
	    mListWings.add(new WingItem(5035, 0, false, "闪耀之翼", 2));
	    mListWings.add(new WingItem(2494, 0, false, "蝴蝶之翼", 3));
	    mListWings.add(new WingItem(1866, 0, false, "悬浮滑板", 4));
	    mListWings.add(new WingItem(1871, 0, false, "圣诞之翼", 5));
	    mListWings.add(new WingItem(665, 0, false, "瑞德之翼", 6));
	    mListWings.add(new WingItem(492, 0, false, "恶魔之翼", 7));
	    mListWings.add(new WingItem(948, 0, false, "蒸汽鹏格之翼", 9));

		mListWings.add(new WingItem(1162, 0, false, "叶之翼", 10));
	    mListWings.add(new WingItem(761, 0, false, "精灵之翼", 11));
	    mListWings.add(new WingItem(823, 0, false, "幽魂之翼", 12));
	    mListWings.add(new WingItem(1830, 0, false, "鬼木之翼", 13));
	    mListWings.add(new WingItem(1586, 0, false, "花蝴蝶之翼", 14));
	    mListWings.add(new WingItem(1797, 0, false, "破落妖精翅膀",15));
	    mListWings.add(new WingItem(1165, 0, false, "蝙蝠之翼", 16));
	    mListWings.add(new WingItem(1515, 0, false, "蜜蜂之翼", 17));
	    mListWings.add(new WingItem(1583, 0, false, "鬼之翼", 18));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.floatBtnAddWing:
			if( mWingsGridViewAdapter.getmItemStack()!=null){
				int itemId = mWingsGridViewAdapter.getmItemStack().getItemId();
				PluginConfig.saveConfig(getContext(), "floatBtnAddWing", ""+itemId);
//				Log.i("gameassist","id:"+ itemId);
				mBtnClear.setEnabled(true);
				Toast.makeText(getContext(), mWingsGridViewAdapter.getmItemStack().getItemName()+"被装备", Toast.LENGTH_SHORT).show();
				NativeUtils.nativeDoRoleCheat(6, 0, itemId);
			}else{
				Toast.makeText(getContext(), "没有翅膀被选中", Toast.LENGTH_SHORT).show();
			}
				
			break;
		case R.id.floatBtnWingClear:   //清除翅膀
			NativeUtils.nativeDoRoleCheat(6, 0, 0);
			PluginConfig.saveConfig(getContext(), "floatBtnAddWing", "0");
			mBtnClear.setEnabled(false);
			Toast.makeText(getContext(), "翅膀被清除", Toast.LENGTH_SHORT).show();
			mWingsGridViewAdapter.clearSelected();
			break;
		case R.id.cbFlying:    //飞行时间无限
			if (mCheckBoxAlwaysFlying.isChecked()) {
				PluginConfig.saveConfig(getContext(), "mCheckBoxAlwaysFlying", "true");
				NativeUtils.nativeDoRoleCheat(7, 0, 1);
			}else {
				PluginConfig.saveConfig(getContext(), "mCheckBoxAlwaysFlying", "false");
				NativeUtils.nativeDoRoleCheat(7, 0, 0);
			}
			break;

		default:
			break;
		}
	}
	
}

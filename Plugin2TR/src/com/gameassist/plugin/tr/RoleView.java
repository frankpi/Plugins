package com.gameassist.plugin.tr;

import android.content.Context;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.gameassist.plugin.tr.R;
import com.gameassist.plugin.nativeutils.NativeUtils;
import com.gameassist.plugin.nativeutils.PluginConfig;

public class RoleView extends FrameLayout implements OnClickListener {

	
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

	private CheckBox mCheckBoxHP,mCheckBoxMP,mCheckBoxInvi;
	private CheckBox mCheckBoxIsFriendly;
	
	void init(){
		inflate(getContext(), R.layout.floor_mainview_role, this);
		mCheckBoxHP = (CheckBox) findViewById(R.id.cbHP);
		mCheckBoxHP.setOnClickListener(this);
		String stat=PluginConfig.readConfig(getContext(), "mCheckBoxHP");
		if(stat.equals("true")){
			mCheckBoxHP.setChecked(true);
		}
		mCheckBoxMP = (CheckBox) findViewById(R.id.cbMP);
		mCheckBoxMP.setOnClickListener(this);
		stat=PluginConfig.readConfig(getContext(), "mCheckBoxMP");
		if(stat.equals("true")){
			mCheckBoxMP.setChecked(true);
		}
		mCheckBoxInvi =(CheckBox) findViewById(R.id.cbInvi);
		mCheckBoxInvi.setOnClickListener(this);
		stat=PluginConfig.readConfig(getContext(), "mCheckBoxInvi");
		if(stat.equals("true")){
			mCheckBoxInvi.setChecked(true);
		}
		mCheckBoxIsFriendly = (CheckBox) findViewById(R.id.cbIsFriendly);
		mCheckBoxIsFriendly.setOnClickListener(this);
		stat=PluginConfig.readConfig(getContext(), "mCheckBoxIsFriendly");
		if(stat.equals("true")){
			mCheckBoxIsFriendly.setChecked(true);
		}
		findViewById(R.id.btnKillSelf).setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.cbHP:
				if (mCheckBoxHP.isChecked()) {
					NativeUtils.nativeDoRoleCheat(1, 0, 1);
					PluginConfig.saveConfig(getContext(), "mCheckBoxHP", "true");
				} else {
					NativeUtils.nativeDoRoleCheat(1, 0, 0);
					PluginConfig.saveConfig(getContext(), "mCheckBoxHP", "false");
				}
				break;
			case R.id.cbMP:
				if (mCheckBoxMP.isChecked()) {
					PluginConfig.saveConfig(getContext(), "mCheckBoxMP", "true");
					NativeUtils.nativeDoRoleCheat(2, 0, 1);
				} else {
					PluginConfig.saveConfig(getContext(), "mCheckBoxMP", "false");
					NativeUtils.nativeDoRoleCheat(2, 0, 0);
				}
				break;
			case R.id.cbInvi:
				if (mCheckBoxInvi.isChecked()) {
					PluginConfig.saveConfig(getContext(), "mCheckBoxInvi", "true");
					NativeUtils.nativeDoRoleCheat(3, 0, 1);
				} else {
					PluginConfig.saveConfig(getContext(), "mCheckBoxInvi", "false");
					NativeUtils.nativeDoRoleCheat(3, 0, 0);
				}
				break;
			case R.id.cbIsFriendly:
				if (mCheckBoxIsFriendly.isChecked()) {
					PluginConfig.saveConfig(getContext(), "mCheckBoxIsFriendly", "true");
					NativeUtils.nativeDoRoleCheat(4, 0, 1);
				} else {
					PluginConfig.saveConfig(getContext(), "mCheckBoxIsFriendly", "false");
					NativeUtils.nativeDoRoleCheat(4, 0, 0);
				}
				break;
			case R.id.btnKillSelf:
				NativeUtils.nativeDoRoleCheat(5, 0, 1);
				break;
		}
	}
}

package com.gameassist.plugin.kritikamobile;

import android.util.Log;
import android.view.View;
import com.gameassist.plugin.Plugin;



public class PluginEntry extends Plugin {

	private static final String LIB_HOOK_PLUGIN = "plugin123";

	private static final String TAG = "gameassist";


	@Override
	public boolean OnPluginCreate() {
		Log.i(TAG, "oncreate");
		System.loadLibrary(LIB_HOOK_PLUGIN);
		getPluginManager().hideLibrary(this, LIB_HOOK_PLUGIN);
		return false;
	} 
	@Override
	public void OnPlguinDestroy() {
	}

	@Override
	public View OnPluginUIShow() {
		return null;
	}


	@Override
	public void OnPluginUIHide() {
	}
	
	@Override
	public boolean pluginHasUI() {
		return false;
	}
	

}

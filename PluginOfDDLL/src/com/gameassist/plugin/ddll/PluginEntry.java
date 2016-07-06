package com.gameassist.plugin.ddll;
import android.view.View;
import com.gameassist.plugin.Plugin;
public class PluginEntry extends Plugin {
	private static final String LIB_HOOKER = "ddll";

	
	public boolean OnPluginCreate() {
		System.loadLibrary(LIB_HOOKER);
		getPluginManager().hideLibrary(this, LIB_HOOKER);
//		NativeUtils.nativeProcessCheat(1, 8888, 0);
		return true;
	}
	public boolean pluginAutoHide(){
		return false;
	}
	public void OnPlguinDestroy() {
	}
	public void OnPluginUIHide() {
	}
	public View OnPluginUIShow() {
		return null;
	}
}
package com.example.testcydia;

import java.lang.reflect.Method;

import android.util.Log;

import com.saurik.substrate.MS;

public class Main {
	static void initialize() {
		// ... code to run when extension is loaded
		MS.hookClassLoad("android.content.res.Resources",new MS.ClassLoadHook() {
			
			@Override
			public void classLoaded(Class<?> resources) {
				// TODO Auto-generated method stub
				Method getColor;
				try {
					getColor = resources.getMethod("getColor", Integer.TYPE);
				} catch (NoSuchMethodException e) {
					getColor = null;
				}

				if (getColor != null) {
					final MS.MethodPointer old = new MS.MethodPointer();

					MS.hookMethod(resources, getColor, new MS.MethodHook() {
						public Object invoked(Object resources, Object... args)
								throws Throwable {
							int color = (Integer) old.invoke(resources, args);
							Log.i("gameassist", "cccccccccccc");
							return color & ~0x0000ff00 | 0x00ff0000;
						}
					}, old);
				}
			}
		});
	}

}
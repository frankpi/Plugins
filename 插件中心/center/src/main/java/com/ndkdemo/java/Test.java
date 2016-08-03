package com.ndkdemo.java;

import java.lang.reflect.Method;

import android.util.Log;

public class Test {

	public static String getString(){
		StringBuilder sb = new StringBuilder("I'm hooked!!\n");
        return sb.toString();
    }
}

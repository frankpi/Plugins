package com.example.testdl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements Callback{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View view  = new View(this);
		findViewById(R.id.textView1).setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			        Intent intent = new Intent();  
			        intent.setClass(MainActivity.this, ProxyActivity.class);
			        String a = Environment.getExternalStorageDirectory()+"/dl/TestPlugin.apk";
			        Log.i("33333", a);
			        intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, a);
			        intent.putExtra(ProxyActivity.EXTRA_CLASS, "com.example.testplugin.MainActivity");
			        
			        startActivity(intent);  
			}
		});

//		myHandler.sendMessage(msg)
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
}

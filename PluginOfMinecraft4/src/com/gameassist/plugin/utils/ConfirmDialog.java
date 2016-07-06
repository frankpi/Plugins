package com.gameassist.plugin.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameassist.plugin.mc.R;


public class ConfirmDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    private Bitmap bmp;
    
    
    public ConfirmDialog(Context context, Bitmap bmp) {
		super(context);
		this.context = context;
		this.bmp = bmp;
	}
    
    public ConfirmDialog(Context context) {
		super(context);
		this.context = context;
		
	}

	public ConfirmDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}


	public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
       
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.float_dialog_screenshots, null);
        setContentView(layout);
        ImageView imageView = (ImageView) layout.findViewById(R.id.floatImageCapshot);
        TextView tvConfirm = (TextView) layout.findViewById(R.id.floatCapshotSave);
        TextView tvCancel = (TextView) layout.findViewById(R.id.floatCapshotCancel);
        
	    if(bmp !=null){
	    	imageView.setImageBitmap(bmp);
	    }

	    tvConfirm.setOnClickListener(new clickListener());
	    tvCancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    
    
    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
            case R.id.floatCapshotSave:
                clickListenerInterface.doConfirm();
                break;
            case R.id.floatCapshotCancel:
                clickListenerInterface.doCancel();
                break;
            }
        }

    };

}
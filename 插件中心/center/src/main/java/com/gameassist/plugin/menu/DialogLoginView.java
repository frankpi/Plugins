package com.gameassist.plugin.menu;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameassist.plugin.center.R;

public class DialogLoginView extends FrameLayout {

	public DialogLoginView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}

	private ClickListenerInterface clickListenerInterface;

	public interface ClickListenerInterface {


		void doConfirm();

		void doCancel();
	}

	public void init() {
		inflate(getContext(),R.layout.plugin_menu_dialog_login, this);

		ImageView loginClose = (ImageView) findViewById(R.id.login_close);
		TextView loginConfirm = (TextView) findViewById(R.id.login_confirm);

		loginClose.setOnClickListener(new clickListener());
		loginConfirm.setOnClickListener(new clickListener());

		DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width,
				height);
		setLayoutParams(params);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.getParent().requestDisallowInterceptTouchEvent(true);
			}
		});
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
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
			case R.id.login_confirm:
				clickListenerInterface.doConfirm();
				break;
			case R.id.login_close:
				clickListenerInterface.doCancel();
				break;
			}
		}

	}

}
package com.gameassist.plugin.menu;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gameassist.plugin.center.R;

public class DialogUpgradeView extends FrameLayout {

	public ImageView upgradeClose;
	public TextView upgradeConfirm;
	public RelativeLayout upgradeBg;

	public DialogUpgradeView(Context context) {
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
		inflate(getContext(),R.layout.plugin_menu_dialog_ggvercode, this);

		upgradeBg = (RelativeLayout) findViewById(R.id.upgrade_bg);
		upgradeClose = (ImageView) findViewById(R.id.upgrade_close);
		upgradeConfirm = (TextView) findViewById(R.id.upgrade_confirm);

		upgradeClose.setOnClickListener(new clickListener());
		upgradeConfirm.setOnClickListener(new clickListener());

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

	private class clickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.upgrade_confirm:
				clickListenerInterface.doConfirm();
				break;
			case R.id.upgrade_close:
				clickListenerInterface.doCancel();
				break;
			}
		}

	}

}
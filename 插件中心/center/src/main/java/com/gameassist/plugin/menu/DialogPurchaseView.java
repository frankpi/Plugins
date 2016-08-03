package com.gameassist.plugin.menu;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gameassist.plugin.center.R;

public class DialogPurchaseView extends FrameLayout {

	private ClickListenerInterface clickListenerInterface;
	public LinearLayout purchaseConfirm;
	public TextView comName;
	public TextView comDesc;
	public TextView comPrice;
	public TextView purchaseLoading;

	public interface ClickListenerInterface {

		void doConfirm();

		void doCancel();
	}

	public DialogPurchaseView(Context context) {
		super(context);
		init();
	}

	public void init() {
		inflate(getContext(), R.layout.plugin_menu_dialog_purchase, this);
		ImageView purchaseClose = (ImageView) findViewById(R.id.purchase_close);
		purchaseConfirm = (LinearLayout) findViewById(R.id.purchase_price_btn);

		comName = (TextView) findViewById(R.id.commodity_name);
		comDesc = (TextView) findViewById(R.id.commodity_desc);
		comPrice = (TextView) findViewById(R.id.commodity_price);
		
		purchaseLoading = (TextView)findViewById(R.id.purchase_loading);
		purchaseLoading.setVisibility(GONE);

		purchaseClose.setOnClickListener(new clickListener());
		purchaseConfirm.setOnClickListener(new clickListener());

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
				// TODO Auto-generated method stub
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
			case R.id.purchase_price_btn:
				clickListenerInterface.doConfirm();
				break;
			case R.id.purchase_close:
				clickListenerInterface.doCancel();
				break;
			}
		}

	}

}
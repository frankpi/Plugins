package com.gameassist.plugin.center;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class DragViewContainer extends LinearLayout {

	public DragViewContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DragViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	
	public DragViewContainer(Context context) {
		super(context);
	}
	
	public interface OnDragedListener{
		void onDragingOffset(int x, int y);
		void onDragStart();
	}
	
	private int offset;
	
	private OnDragedListener listner;
	public void setOnDragedListener(OnDragedListener listner){
		this.listner = listner;
		Resources r = getContext().getResources();
		offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, r.getDisplayMetrics());
		
	}
	private float touchX, touchY, startX, startY;
	private boolean isDraged = false;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(listner == null)
			return super.onTouchEvent(event);
		touchX = event.getRawX();
		touchY = event.getRawY();
		boolean result = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isDraged = false;
			startX = touchX;
			startY = touchY;
			break;
		case MotionEvent.ACTION_MOVE:
			if (isDraged) {
				result = true;
				listner.onDragingOffset( (int)(touchX - startX), (int)(touchY - startY));
			} else if (Math.abs(touchX - startX) >= offset || Math.abs(touchY - startY) >= offset) {
				isDraged = true;
				result = true;
				listner.onDragStart();
			}
			break;
		case MotionEvent.ACTION_UP:
			result = isDraged;
			isDraged = false;
			break;
		}
//		Log.e("GameAssist", "onInterceptTouchEvent: " + event.getAction() + " -> " + touchX + "/" + touchY + " <-- " +startX +"/" + startY );
		return result;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if(listner == null)
			return false;
		touchX = event.getRawX();
		touchY = event.getRawY();
		boolean result = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isDraged = false;
			startX = touchX;
			startY = touchY;
			break;
		case MotionEvent.ACTION_MOVE:
			if (isDraged) {
				result = true;
				listner.onDragingOffset( (int)(touchX - startX), (int)(touchY - startY));
			} else if (Math.abs(touchX - startX) >= offset || Math.abs(touchY - startY) >= offset) {
				isDraged = true;
				result = true;
				listner.onDragStart();
			}
			break;
		case MotionEvent.ACTION_UP:
			result = isDraged;
			isDraged = false;
			break;
		}
//		Log.e("GameAssist", "onInterceptTouchEvent: " + event.getAction() + " -> " + touchX + "/" + touchY + " <-- " +startX +"/" + startY );
		return result;
	}  
}

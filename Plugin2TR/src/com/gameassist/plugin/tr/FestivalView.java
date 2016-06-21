package com.gameassist.plugin.tr;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gameassist.plugin.tr.R;
import com.gameassist.plugin.adapter.FestivalListViewAdapter;
import com.gameassist.plugin.entity.FestivalItem;
import com.gameassist.plugin.nativeutils.NativeUtils;

//import com.gameassist.plugin.mc.PluginEntry.SubviewObserver;

public class FestivalView extends FrameLayout implements OnClickListener{

	public FestivalView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public FestivalView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}
	
	public FestivalView(Context context) {
		super(context);
		init();
	}
	
	private Button mXuanze,mQingchu;
	private List<FestivalItem>  festivalItemStacks= new ArrayList<FestivalItem>();
	private ListView listView;
	private FestivalListViewAdapter mFestivalListViewAdapter;
	private FestivalItem festivalItemStack;
	
	void init(){
		inflate(getContext(), R.layout.floor_mainview_festival, this);
		initData();
		listView = (ListView) findViewById(R.id.float_listview_fes);
		mFestivalListViewAdapter = new FestivalListViewAdapter(getContext());
		listView.setAdapter(mFestivalListViewAdapter);
		mFestivalListViewAdapter.addDatas(festivalItemStacks);
		
		mXuanze = (Button) findViewById(R.id.btnXuanze);
		mXuanze.setOnClickListener(this);
		mQingchu = (Button) findViewById(R.id.btnXuanzeQingchu);
		mQingchu.setOnClickListener(this);
	}
	
	void initData(){
		festivalItemStacks.add(new FestivalItem(false, false, 1,1, "新年"));
		festivalItemStacks.add(new FestivalItem(false, false, 2,2, "情人节"));
		festivalItemStacks.add(new FestivalItem(false, false, 3,3, "圣帕特里节"));
		festivalItemStacks.add(new FestivalItem(false, false, 4,4, "复活节"));
		festivalItemStacks.add(new FestivalItem(false, false, 5,5, "啤酒节"));
		festivalItemStacks.add(new FestivalItem(false, false, 6,6, "万圣节"));
		festivalItemStacks.add(new FestivalItem(false, false, 7,7, "感恩节"));
		festivalItemStacks.add(new FestivalItem(false, false, 8,8, "圣诞节"));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//0关闭
		case R.id.btnXuanze:
			festivalItemStack = mFestivalListViewAdapter.getFestivalItemStack();
			if(festivalItemStack!=null){
				NativeUtils.nativeDoTimeCheat(1, 0, festivalItemStack.getItemId());
				mQingchu.setEnabled(true);
				Toast.makeText(getContext(), festivalItemStack.getItemName()+"被激活", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(getContext(), "没有节日被选中", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnXuanzeQingchu:	
				mQingchu.setEnabled(false);	
				mFestivalListViewAdapter.clearSelected();
				Toast.makeText(getContext(), "节日被清除", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}
}

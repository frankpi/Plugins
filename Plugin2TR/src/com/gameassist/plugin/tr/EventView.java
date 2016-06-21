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

import com.gameassist.plugin.adapter.EventListViewAdapter;
import com.gameassist.plugin.entity.EventItem;
import com.gameassist.plugin.entity.PackItem;
import com.gameassist.plugin.nativeutils.NativeUtils;

//import com.gameassist.plugin.mc.PluginEntry.SubviewObserver;

public class EventView extends FrameLayout implements OnClickListener {

	public EventView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public EventView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}

	public EventView(Context context) {
		super(context);
		init();
	}

	private List<EventItem> eventItemStacks = new ArrayList<EventItem>();
	private EventListViewAdapter eventListViewAdapter;
	private ListView listView;
	private Button clearBtn;

	void init() {
		inflate(getContext(), R.layout.floor_mainview_events, this);
		findViewById(R.id.btnChufa).setOnClickListener(this);
		clearBtn = (Button) findViewById(R.id.btnChufaClear);
		clearBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listViewEvent);
		initData();
		eventListViewAdapter = new EventListViewAdapter(getContext());
		listView.setAdapter(eventListViewAdapter);
		eventListViewAdapter.addDatas(eventItemStacks);
	}

	void initData() {
		eventItemStacks.add(new EventItem(false, false, 15, 1, "日食"));
		eventItemStacks.add(new EventItem(false, false, 13, 2, "血月"));
		eventItemStacks.add(new EventItem(false, false, 16, 3, "霜月"));
		eventItemStacks.add(new EventItem(false, false, 14, 4, "南瓜月"));
		eventItemStacks.add(new EventItem(false, false, 1, 5, "哥布林入侵"));
		eventItemStacks.add(new EventItem(false, false, 2, 6, "海盗入侵"));
		eventItemStacks.add(new EventItem(false, false, 3, 7, "雪人入侵"));
	}

	int eventState = 0;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnChufa:
			EventItem eventItemStack = eventListViewAdapter
					.getEventItemStack();
			if (eventItemStack == null) {
				Toast.makeText(getContext(), "没有事件被选中", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getContext(),
						eventItemStack.getItemName() + "事件被激活",  Toast.LENGTH_LONG).show();
				clearBtn.setEnabled(true);
				eventState = eventItemStack.getItemId();
				if (eventState > 10) {
					NativeUtils.nativeDoTimeCheat(eventState, 0, 1);
				}
				if (eventState < 10 && eventState > 0) {
					if () {
						
					}
//					isSuccess  = NativeUtils.nativeDoAddItemCheat(1,602 ,1);
//					mItemStacks.add(new PackItem(false, false,602,602,"雪球仪"));
//					mItemStacks.add(new PackItem(false, false,361,361,"哥布林战旗"));
//					mItemStacks.add(new PackItem(false, false,1315,1315,"海盗地图"));
					NativeUtils.nativeDoGameEvent(eventState, 0, 1);
				}
			}
			break;
		case R.id.btnChufaClear:
			if(eventState!=0){
				NativeUtils.nativeDoTimeCheat(eventState, 0, 0);
			}
			clearBtn.setEnabled(false);
			eventListViewAdapter.clearSelected();
			Toast.makeText(getContext(), "事件被清除",  Toast.LENGTH_LONG).show();
			break;
		
		}

	}

}

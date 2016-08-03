package com.gameassist.plugin.menu;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gameassist.plugin.center.MyLog;
import com.gameassist.plugin.center.PluginEntry;
import com.gameassist.plugin.center.PluginManagerView;
import com.gameassist.plugin.center.R;
import com.gameassist.plugin.utils.ClientConnect;
import com.gameassist.plugin.utils.CommonUtils;

public class MessageView extends FrameLayout {

	private PluginManagerView pluginManagerView;
	private TextView msgLoading;
	private ArrayList<MessageInfo> listDataMsg;
	private MessageAdapter messageAdapter;
	private BaseWebView baseWebView;

	public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MessageView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}

	public MessageView(Context context) {
		super(context);
		init();
	}

	public MessageView(Context context, PluginManagerView pluginManagerView) {
		// TODO Auto-generated constructor stub
		super(context);
		this.pluginManagerView = pluginManagerView;
		init();
	}

	void init() {
		inflate(getContext(), R.layout.plugin_menu_message, this);
		findViewById(R.id.message_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pluginManagerView.closePlugin();
					}
				});
		// message_listview
		ListView messageListView = (ListView) findViewById(R.id.message_listview);
		msgLoading = (TextView) findViewById(R.id.message_loading);
		listDataMsg = new ArrayList<MessageInfo>();
		messageAdapter = new MessageAdapter();
		messageListView.setAdapter(messageAdapter);

		baseWebView = new BaseWebView(getContext());
		FrameLayout msgWebView = (FrameLayout) findViewById(R.id.message_myWebView);
		baseWebView.setVisibility(GONE);
		msgWebView.addView(baseWebView);
		LoadMsgDataAsyncTask loadMsgDataAsyncTask = new LoadMsgDataAsyncTask();
		loadMsgDataAsyncTask.execute(listDataMsg);

	}

	public class LoadMsgDataAsyncTask extends
			AsyncTask<Object, Object, List<MessageInfo>> {

		@Override
		protected ArrayList<MessageInfo> doInBackground(Object... params) {
			// TODO Auto-generated method stub
			ClientConnect clientConnect = new ClientConnect(
					PluginEntry.getTargetGame());
			try {
				JSONObject jspush = new JSONObject();
				jspush.put("Function", "PushAll");
				String strpush = clientConnect.connectJo(5000, jspush);
				JSONObject jsonObject = new JSONObject(strpush);
				JSONArray jsonArray = new JSONArray(
						jsonObject.optString("msgs"));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject msgObj = (JSONObject) jsonArray.get(i);
					listDataMsg
							.add(new MessageInfo(CommonUtils
									.dateFormat(msgObj
											.getString("create_time")), msgObj
									.getInt("action"),
									msgObj.getString("desc"), msgObj
											.getString("href"), msgObj
											.getString("title")));

				}
			} catch (Exception e) {
				// TODO: handle exception
				MyLog.e(e.getMessage());
				e.printStackTrace();
			}
			return listDataMsg;
		}

		@Override
		protected void onPostExecute(List<MessageInfo> result) {
			// TODO Auto-generated method stub
			msgLoading.setVisibility(GONE);
			messageAdapter.addData(listDataMsg);
			super.onPostExecute(result);
		}
	}

	public class MessageInfo {
		public MessageInfo(String createTime, int action, String desc,
						   String href, String title) {
			super();
			this.createTime = createTime;
			this.action = action;
			this.desc = desc;
			this.href = href;
			this.title = title;
		}

		private String createTime;
		private int action;
		private String desc;
		private String href;
		private String title;

		public int getAction() {
			return action;
		}

		public String getDesc() {
			return desc;
		}

		public String getHref() {
			return href;
		}

		public String getTitle() {
			return title;
		}

		public String getCreateTime() {
			return createTime;
		}

	}

	private class MessageAdapter extends BaseAdapter {

		private List<MessageInfo> listData = new ArrayList<MessageView.MessageInfo>();

		public void addData(List<MessageInfo> listDataMsg) {
			this.listData.clear();
			this.listData.addAll(listDataMsg);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.plugin_item_message, null);
			}
			final MessageInfo item = (MessageInfo) getItem(position);
			((TextView) convertView.findViewById(R.id.message_desc))
					.setText(item.getDesc());

			((TextView) convertView.findViewById(R.id.message_label))
					.setText(item.getTitle());
			((TextView) convertView.findViewById(R.id.message_createTime))
					.setText(item.getCreateTime());

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (TextUtils.isEmpty(item.getHref())) {
						return;
					}
					switch (item.getAction()) {
					case 1:
						baseWebView.setVisibility(VISIBLE);
						baseWebView.myWebView.loadUrl(item.getHref());

						break;
					case 2:
						try {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url = Uri.parse(item.getHref());
							intent.setData(content_url);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							PluginEntry.getTargetGame().startActivity(intent);

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						break;
					case 3:
						Intent intent2;
						try {
							intent2 = Intent.parseUri(item.getHref(), 0);
							intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							PluginEntry.getTargetGame().startActivity(intent2);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					default:
						break;
					}

				}
			});
			return convertView;
		}
	}

}

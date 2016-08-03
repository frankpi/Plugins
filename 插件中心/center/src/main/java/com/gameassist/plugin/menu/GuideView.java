package com.gameassist.plugin.menu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gameassist.plugin.center.PluginEntry;
import com.gameassist.plugin.center.PluginManagerView;
import com.gameassist.plugin.center.R;
import com.gameassist.plugin.utils.ClientConnect;

public class GuideView extends FrameLayout {

	private PluginManagerView pluginManagerView;
	private ArrayList<GuideInfo> listData;
	private BaseWebView guideWebView;
	public GuideAdapter guideAdapter;
	private View guideLoading;

	public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public GuideView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		init();
	}

	public GuideView(Context context) {
		super(context);
		init();
	}

	public GuideView(Context context, PluginManagerView pluginManagerView) {
		// TODO Auto-generated constructor stub
		super(context);
		this.pluginManagerView = pluginManagerView;
		init();
	}



	void init() {
		inflate(getContext(), R.layout.plugin_menu_guide, this);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.guide_myWebView);
		guideLoading = findViewById(R.id.guide_loading);
		findViewById(R.id.guide_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pluginManagerView.closePlugin();

			}
		});
		ListView guideListView = (ListView) findViewById(R.id.guide_listview);
		listData = new ArrayList<GuideInfo>();
		guideWebView = new BaseWebView(getContext());
		frameLayout.removeAllViews();
		frameLayout.addView(guideWebView);
		guideWebView.setVisibility(GONE);
		guideAdapter = new GuideAdapter();
		guideListView.setAdapter(guideAdapter);
		LoadGuideAsyncTask loadGuideAsyncTask = new LoadGuideAsyncTask();
		loadGuideAsyncTask.execute(listData);

	}

	public class LoadGuideAsyncTask extends
			AsyncTask<Object, Object, List<GuideInfo>> {

		@Override
		protected ArrayList<GuideInfo> doInBackground(Object... params) {
			// TODO Auto-generated method stub
			ClientConnect clientConnect = new ClientConnect(PluginEntry.getTargetGame());
			try {
				JSONObject jsguide = new JSONObject();
				jsguide.put("Function", "getGuide");
				String strguide = clientConnect.connectJo(5000, jsguide);
				JSONObject jsonObject = new JSONObject(strguide);
				JSONObject jsonobj = new JSONObject(
						jsonObject.optString("data"));
				JSONArray jsonArray = new JSONArray(
						jsonobj.getString("article_list"));
				for (int i = 0; i <= jsonArray.length(); i++) {
					JSONObject msgObj = (JSONObject) jsonArray.get(i);
					listData.add(new GuideInfo(msgObj.getString("url"), msgObj.getString("title")));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return listData;
		}

		@Override
		protected void onPostExecute(List<GuideInfo> result) {
			// TODO Auto-generated method stub
			guideLoading.setVisibility(GONE);
			guideAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	private class GuideInfo {
		public GuideInfo(String url, String title) {
			super();
			this.url = url;
			this.title = title;
		}

		private String url;
		private String title;

		public String getUrl() {
			return url;
		}

		public String getTitle() {
			return title;
		}
	}

	private class GuideAdapter extends BaseAdapter {

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
						R.layout.plugin_item_guide, null);
			}
			final GuideInfo item = (GuideInfo) getItem(position);
			((TextView) convertView.findViewById(R.id.guide_label))
					.setText(item.getTitle());
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					guideWebView.setVisibility(VISIBLE);
					guideWebView.myWebView.loadUrl(item.getUrl());
				}
			});
			return convertView;
		}
	}

}

package com.gameassist.plugin.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameassist.plugin.entity.PackItem;
import com.gameassist.plugin.tr.R;

public class PackGridViewAdapter extends BaseAdapter {
	private final Context context;
	private List<PackItem> mList = new ArrayList<PackItem>();

	public PackGridViewAdapter(Context context) {
		this.context = context;
	}

	public void addDatas(Collection<? extends PackItem> mList) {
		this.mList.clear();
		this.mList.addAll(mList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * 反射获取图片在R文件中的id
	 */
	private int getItemImageId(String m) {
		ClassLoader clloader = getClass().getClassLoader();
		String entryClass = "com.gameassist.plugin.tr.R$drawable"; // 反射包名R类获取入口类
		Class<?> cls;
		int id = 0;
		try {
			// 去R文件找入口类
			cls = clloader.loadClass(entryClass);
			Field filed = cls.getDeclaredField(m);
			filed.setAccessible(true);
			id = filed.getInt(cls);
			return id;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 2130837796;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = View.inflate(context, R.layout.gridview_pack_item,
					null);
			viewHolder = new ViewHolder();

			viewHolder.item_bg = (ImageView) convertView
					.findViewById(R.id.item_bg);
			viewHolder.item_selected = (ImageView) convertView
					.findViewById(R.id.item_selected);
			viewHolder.item_name = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.bg = (ImageView) convertView.findViewById(R.id.bg);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}
		final PackItem mPackItemStack = mList.get(position);
		int mImageId = getItemImageId("item_" + mPackItemStack.getItemImgId());
		// Log.i("gameassist", "R$id"+mImageId);
		// Picasso.with(context).l.error(context.getResources().getDrawable(mImageId)).into(viewHolder.item_bg);
		viewHolder.item_bg.setImageDrawable(context.getResources().getDrawable(mImageId));
//		viewHolder.item_bg.setImageResource(R.drawable.item_10);
		viewHolder.item_name.setText(mPackItemStack.getItemName());
		setOnClickListener(viewHolder, mPackItemStack);

		if (mPackItemStack.isCheck()) {
			viewHolder.item_selected.setVisibility(View.VISIBLE);
		} else {
			viewHolder.item_selected.setVisibility(View.GONE);
		}

		return convertView;
	}

	private void setOnClickListener(final ViewHolder viewHolder,
			final PackItem mPackItemStack) {

		// 多选
		viewHolder.bg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPackItemStack.isCheck()) {
					mPackItemStack.setCheck(false);
					//
					// viewHolder.item_selected.setBackgroundResource(0);
				} else {
					mPackItemStack.setCheck(true);
					//
					// viewHolder.item_selected.setBackgroundResource(1);
				}
				notifyDataSetChanged();
			}
		});
	}
	
	public List<PackItem> getItemList() {
		List<PackItem> list = null;
        if (!mList.isEmpty()) {
        	 list = new ArrayList<PackItem>(mList.size());
			for (PackItem is : mList) {
				if (is.isCheck()) {
					list.add(is);
				}
			}
		}
		notifyDataSetChanged();
		return list;
	}

	public void clearSelected() {
		for (PackItem is : mList) {
			is.setCheck(false);
		}
		notifyDataSetChanged();
	}

	static class ViewHolder {
		ImageView item_bg;
		ImageView item_selected;
		TextView item_name;
		ImageView bg;
	}
}

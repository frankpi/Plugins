package com.gameassist.plugin.adapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameassist.plugin.entity.FestivalItem;
import com.gameassist.plugin.tr.R;

public class FestivalListViewAdapter extends BaseAdapter {
    private final Context context;
    private List<FestivalItem> mList= new ArrayList<FestivalItem>();

    public FestivalListViewAdapter(Context context) {
        this.context = context;
    }

    public void addDatas(Collection<? extends FestivalItem> mList) {
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
			//去R文件找入口类
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

  
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.listview_festival_item, null);
            viewHolder = new ViewHolder();

            viewHolder.festival_select_img = (ImageView) convertView.findViewById(R.id.festival_select_img);
            viewHolder.festival_name = (TextView) convertView.findViewById(R.id.festival_name);
            viewHolder.festival_img= (ImageView)convertView.findViewById(R.id.festival_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        final FestivalItem festivalItemStack = mList.get(position);
        int  mImageId = getItemImageId("fes_"+festivalItemStack.getItemImgId());
//        Log.i("gameassist", "R$id"+mImageId);
//        viewHolder.item_bg.setImageDrawable(context.getResources().getDrawable(R.drawable.wing_1));
//		Picasso.with(context).l.error(context.getResources().getDrawable(mImageId)).into(viewHolder.item_bg);
        viewHolder.festival_img.setImageDrawable(context.getResources().getDrawable(mImageId));//item图片
        viewHolder.festival_name.setText(festivalItemStack.getItemName());
        setOnClickListener(convertView, festivalItemStack);

        if (festivalItemStack.isCheck()){
        	convertView.setBackgroundResource(R.drawable.bg_et_input);
        }else{
        	convertView.setBackgroundResource(0);
        }
        
        if (festivalItemStack.isSellect()){
            viewHolder.festival_select_img.setVisibility(View.VISIBLE);
        }else{
            viewHolder.festival_select_img.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void setOnClickListener(final View convertView, final FestivalItem festivalItemStack) {

        // 多选
//        viewHolder.bg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mItemStack.isCheck()) {
//                    mItemStack.setCheck(false);
//                    //
////                    viewHolder.item_selected.setBackgroundResource(0);
//                } else {
//                    mItemStack.setCheck(true);
//                    //
////                    viewHolder.item_selected.setBackgroundResource(1);
//                }
//                notifyDataSetChanged();
//            }
//        });

        // 单选
    	convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (FestivalItem is : mList){
                    is.setCheck(false);
                }
                festivalItemStack.setCheck(true);
                notifyDataSetChanged();
            }
        });
    }

    public FestivalItem getFestivalItemStack() {
    	FestivalItem festivalItemStack = null;
        for (FestivalItem is : mList){
        	is.setSellect(false);
            if(is.isCheck()){
            	is.setSellect(true);
            	festivalItemStack = is;
            }
        }
        notifyDataSetChanged();
		return festivalItemStack;
	}
    
    public void clearSelected(){
    	 for (FestivalItem is : mList){
             is.setCheck(false);
             is.setSellect(false);
         }
    	 notifyDataSetChanged();
    }


	static class ViewHolder {
        ImageView festival_select_img;
        ImageView festival_img;
        TextView festival_name;
    }
}

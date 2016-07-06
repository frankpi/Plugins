package com.gameassist.plugin.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameassist.plugin.entity.ItemStack;
import com.gameassist.plugin.mc.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GridViewItemAdapter extends BaseAdapter {
    private final Context context;
    private List<ItemStack> mList = new ArrayList<ItemStack>();

    public GridViewItemAdapter(Context context) {
        this.context = context;
    }

    public void addDatas(List<ItemStack> mList) {
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
		String entryClass = "com.gameassist.plugin.mc.R$drawable"; // 反射包名R类获取入口类
		Class cls;
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
			Log.i("gameassist", "ex:"+ex);
			return 2130837801;
		}
	}

  
	@SuppressWarnings("deprecation")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.gridview_item, null);
            viewHolder = new ViewHolder();

            viewHolder.item_bg = (ImageView) convertView.findViewById(R.id.item_bg);
            viewHolder.item_selected = (ImageView) convertView.findViewById(R.id.item_selected);
            viewHolder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.bg= (ImageView)convertView.findViewById(R.id.bg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        final ItemStack mItemStack = mList.get(position);
        int  mImageId;
        if (mItemStack.getItemDmg()>0 ||(mItemStack.getItemImgId()==1)){
        	if (mItemStack.getmOrgDmgId() > 0) {
        		if (mItemStack.getItemImgId()==11) {
        			Log.i("gameassist", "itemid"+(position - 1));
        			final ItemStack mItemStack2 = mList.get(position - 1 );
            		mImageId = getItemImageId("item_"+mItemStack2.getmOrgItemId()+"_"+mItemStack2.getmOrgDmgId());
				}else {
					mImageId = getItemImageId("item_"+mItemStack.getmOrgItemId()+"_"+mItemStack.getmOrgDmgId());
				}
			}else{
				mImageId = getItemImageId("item_"+mItemStack.getItemId()+"_"+mItemStack.getItemDmg());
			}
		}else {
				mImageId = getItemImageId("item_"+mItemStack.getItemId());
		}
//        Log.i("gameassist", "R$id"+mImageId);
//        context.getResources().getDrawable(mImageId);
       
        viewHolder.item_bg.setImageDrawable(context.getResources().getDrawable(mImageId));//item图片
        viewHolder.item_name.setText(mItemStack.getItemName());
        setOnClickListener(viewHolder, mItemStack);

        if (mItemStack.isCheck()){
            viewHolder.item_selected.setVisibility(View.VISIBLE);
        }else{
            viewHolder.item_selected.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void setOnClickListener(final ViewHolder viewHolder, final ItemStack mItemStack) {

        // 多选
        viewHolder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemStack.isCheck()) {
                    mItemStack.setCheck(false);
                    //
//                    viewHolder.item_selected.setBackgroundResource(0);
                } else {
                    mItemStack.setCheck(true);
                    //
//                    viewHolder.item_selected.setBackgroundResource(1);
                }
                notifyDataSetChanged();
            }
        });

//        // 单选
//        viewHolder.item_selected.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (ItemStack is : mList){
//                    is.setCheck(false);
//                }
//                mItemStack.setCheck(true);
//                notifyDataSetChanged();
//            }
//        });
    }

    static class ViewHolder {
        ImageView item_bg;
        ImageView item_selected;
        TextView item_name;
        ImageView bg;
    }

    /**
     * 获取当前页面的Item的位置
     *
     * @return
     */
    public List<ItemStack> getCurrentPageItem() {
        List<ItemStack> listItems = null;
        if (!mList.isEmpty()) {
            int size = mList.size();
            listItems = new ArrayList<ItemStack>(size);
            for (int i = 0; i < size; i++) {
               ItemStack is = mList.get(i);
                if(is.isCheck()){
                	listItems.add(is);
                }
            }
        }
        return listItems;
    }

}

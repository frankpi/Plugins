package com.gameassist.plugin.reaper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FuncsAdapter extends BaseAdapter {

	private List<FunctionItem> mList = new ArrayList<FunctionItem>();
	private Context context;
	private FunctionItem mFunctionItem;
	private Function function;
	public FunctionItem getmFunctionItem() {
		return mFunctionItem;
	}

	public void setmFunctionItem(FunctionItem mFunctionItem) {
		this.mFunctionItem = mFunctionItem;
	}

	public FuncsAdapter(Context context,Application application) {
		// TODO Auto-generated constructor stub
		super();
		function = new Function(application);
		this.context = context;
		
	}

	public void addDatas(Collection<? extends FunctionItem> list) {
		this.mList.clear();
		this.mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = View.inflate(context, R.layout.listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.func_tv_name = (TextView) convertView
					.findViewById(R.id.func_tv_name);
			viewHolder.func_tv_value = (TextView) convertView
					.findViewById(R.id.func_tv_value);
			viewHolder.func_tv_level = (TextView) convertView
					.findViewById(R.id.func_tv_level);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}
		final FunctionItem functionItem = mList.get(position);
		// int mImageId = getItemImageId("event_"+functionItem.getImageName());
		// Log.i("gameassist", "R$id"+mImageId);
		// viewHolder.festival_img.setImageDrawable(context.getResources().getDrawable(mImageId));//item图片
		viewHolder.func_tv_name.setText(functionItem.getName());
		viewHolder.func_tv_level.setText("x" + functionItem.getLevel());
		
//		viewHolder.func_tv_value.setText(function.getValue(functionItem.getFieldName(), 1));
		if (functionItem.isChecked()) {
			convertView.setBackgroundResource(R.drawable.style_bg_green);
		}else {
			convertView.setBackgroundResource(0);
		}
		setOnClick(convertView,functionItem);
		return convertView;
	}
	
	
	private void setOnClick(View convertView, final FunctionItem functionItem) {
		// TODO Auto-generated method stub
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (FunctionItem fi : mList) {
					fi.setChecked(false);
				}
				functionItem.setChecked(true);
				setmFunctionItem(functionItem);
				notifyDataSetChanged();
			}
		});
	}

	static class ViewHolder {
		ImageView func_bg;
		ImageView func_iv_flag;
		TextView func_tv_name;
		TextView func_tv_value;
		TextView func_tv_level;

	}

	void initData() {
		mList.add(new FunctionItem("护甲", "D", 1, 0, false));
		mList.add(new FunctionItem("头盔", "E", 1, 0, false));
		mList.add(new FunctionItem("血量", "F", 1, 0, false));
		mList.add(new FunctionItem("力量", "H", 1, 0, false));
		mList.add(new FunctionItem("能量", "I", 1, 0, false));

		mList.add(new FunctionItem("兴奋", "J", 1, 0, false));
		mList.add(new FunctionItem("兴奋撞击", "K", 1, 0, false));
		mList.add(new FunctionItem("兴奋击杀", "L", 1, 0, false));

		mList.add(new FunctionItem("移动速度", "N", 1, 0, false));

		mList.add(new FunctionItem("跳跃限制", "O", 1, 0, false));
		mList.add(new FunctionItem("跳跃速度", "P", 1, 0, false));
		mList.add(new FunctionItem("跳跃速度小数", "Q", 1, 0, false));

		mList.add(new FunctionItem("格档机率", "S", 1, 0, false));

		mList.add(new FunctionItem("暴击率", "ar", 1, 0, false));
		mList.add(new FunctionItem("暴击伤害", "as", 1, 0, false));
		mList.add(new FunctionItem("暴击晕眩几率", "at", 1, 0, false));
		mList.add(new FunctionItem("暴击命中几率", "au", 1, 0, false));
		mList.add(new FunctionItem("暴击背刺几率", "av", 1, 0, false));

		mList.add(new FunctionItem("冲锋伤害", "aw", 1, 0, false));
		mList.add(new FunctionItem("冲锋距离", "ax", 1, 0, false));
		mList.add(new FunctionItem("冲锋晕眩几率", "ay", 1, 0, false));
		mList.add(new FunctionItem("冲锋晕眩时间", "az", 1, 0, false));

		mList.add(new FunctionItem("上挑伤害", "aA", 1, 0, false));
		mList.add(new FunctionItem("上挑眩晕几率", "aB", 1, 0, false));
		mList.add(new FunctionItem("上挑眩晕时间", "aC", 1, 0, false));

		mList.add(new FunctionItem("叩击伤害", "aD", 1, 0, false));
		mList.add(new FunctionItem("叩击眩晕几率", "aE", 1, 0, false));
		mList.add(new FunctionItem("叩击眩晕时间", "aF", 1, 0, false));
		mList.add(new FunctionItem("叩击暴击几率", "aG", 1, 0, false));

		mList.add(new FunctionItem("旋风斩伤害", "aH", 1, 0, false));
		mList.add(new FunctionItem("旋风格铛几率", "aI", 1, 0, false));

		mList.add(new FunctionItem("重踏范围", "aJ", 1, 0, false));
		mList.add(new FunctionItem("重踏伤害", "aK", 1, 0, false));
		mList.add(new FunctionItem("重踏眩晕几率", "aL", 1, 0, false));
		mList.add(new FunctionItem("重踏眩晕时间", "aM", 1, 0, false));

		mList.add(new FunctionItem("掠夺", "aN", 1, 0, false));
		mList.add(new FunctionItem("掠夺奖励", "aO", 1, 0, false));
		mList.add(new FunctionItem("掠夺几率", "aP", 1, 0, false));

		mList.add(new FunctionItem("精神伤害", "aQ", 1, 0, false));

		mList.add(new FunctionItem("复活几率", "aR", 1, 0, false));
		mList.add(new FunctionItem("复活血量", "aS", 1, 0, false));

		mList.add(new FunctionItem("伤害系数", "aT", 1, 0, false));

		// v2_1.a("\n\t chargeDamage => ").c(this.aw);
		// v2_1.a("\n\t chargeDistance => ").c(this.ax);
		// v2_1.a("\n\t chargeStunChance => ").c(this.ay);

		// bh.a(v2_1.a("\n\t chargeStunDuration => "), this.az);
		// v2_1.a("\n\t uppercutDamage => ").c(this.aA);
		// v2_1.a("\n\t uppercutStunChance => ").c(this.aB);
		// bh.a(v2_1.a("\n\t uppercutStunDuration => "), this.aC);

		// v2_1.a("\n\t slamDamage => ").c(this.aD);
		// v2_1.a("\n\t slamStunChance => ").c(this.aE);
		// bh.a(v2_1.a("\n\t slamStunDuration => "), this.aF);
		// v2_1.a("\n\t slamCriticalChance => ").c(this.aG);

		// v2_1.a("\n\t whirlwindDamage => ").c(this.aH);
		// v2_1.a("\n\t whirlwindBlockChance => ").c(this.aI);

		// v2_1.a("\n\t stompRange => ").c(this.aJ);
		// v2_1.a("\n\t stompDamage => ").c(this.aK);
		// v2_1.a("\n\t stompStunChance => ").c(this.aL);
		// bh.a(v2_1.a("\n\t stompStunDuration => "), this.aM);

		// v2_1.a("\n\t lootGreed => ").c(this.aN);
		// bh.a(v2_1.a("\n\t lootBonus => "), this.aO);
		// v2_1.a("\n\t lootChance => ").c(this.aP);

		// v2_1.a("\n\t awarenessDamage => ").c(this.aQ);

		// v2_1.a("\n\t resurrectionChance => ").c(this.aR);
		// v2_1.a("\n\t resurrectionHealth => ").c(this.aS);

		// v2_1.a("\n\t damageFactor => ").c(this.aT);

		// v2_1.a("\n\t armor => ").c(this.D);
		// v2_1.a("\n\t armorSkull => ").c(this.E);
		// v2_1.a("\n\t health => ").c(this.F);
		// v2_1.a("\n\t power=> ").c(this.I);
		// v2_1.a("\n\t strength => ").c(this.H);
		// v2_1.a("\n\t adrenaline => ").c(this.J);
		// v2_1.a("\n\t adrenalineHit => ").c(this.K);
		// v2_1.a("\n\t adrenalineKill => ").c(this.L);
		// v2_1.a("\n\t runSpeed => ").c(this.N);
		// v2_1.a("\n\t jumpLimit => ").c(this.O);
		// v2_1.a("\n\t jumpSpeed => ").c(this.P);
		// v2_1.a("\n\t jumpSpeedDouble => ").c(this.Q);
		// v2_1.a("\n\t blockChance => ").c(this.S);
		// v2_1.a("\n\t criticalChance => ").c(this.ar);
		// v2_1.a("\n\t criticalDamage => ").c(this.as);
		// v2_1.a("\n\t criticalStunnedChance => ").c(this.at);
		// v2_1.a("\n\t criticalFocusedChance => ").c(this.au);
		// v2_1.a("\n\t criticalBackstabChance => ").c(this.av);

	}
}

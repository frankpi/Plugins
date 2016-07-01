package com.gameassist.plugin.reaper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import android.R.interpolator;
import android.animation.IntArrayEvaluator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.Plugin;

public class ReaperEntry extends Plugin implements OnClickListener,
		OnSeekBarChangeListener {

	private static final String tag = "gameassist";
	private ArrayList<FunctionItem> mListRole, mListGame, mListAddi;
	private Set<FunctionItem> mSet;
	protected boolean isAuto = true;

	@Override
	public boolean OnPluginCreate() {
		Log.i("GameAssist", "oncreate");
		// 提示：这个类是调用了Health的类
		// this.N.a(ek.c("WEAK"), -65281);
		// this.M.a(hz.f, 1);
		// this.M.a(ek.c("BLOCKED"), -16744193);
		// this.O.a(hz.e, 1);
		// this.O.a(ek.c("CRITICAL"), -11776);
		// 1.4.10 net.hexage.reaper.ek
		// 1.4.13 net.hexage.reaper.el

		// this.X = this.a(this.T, 9, dd.c("Next"));
		// this.Z = this.a(this.T, 5, dd.c("Travel"));
		// this.aa = this.a(this.T, 6, dd.c("Combat"));
		// this.Y = this.a(this.T, v4, dd.c("Quest"));
		// this.ab = this.a(this.T, 7, dd.c("Shop"));
		// this.ac = this.a(this.T, v5, dd.c("Fortune"));

		function = new Function(getTargetApplication());
		mListRole = new ArrayList<FunctionItem>();
		mListGame = new ArrayList<FunctionItem>();
		mListAddi = new ArrayList<FunctionItem>();
		mSet = new HashSet<FunctionItem>();
		return false;
	}

	public boolean pluginHasUI() {
		return true;
	}

	@Override
	public void OnPlguinDestroy() {
		isAuto = false;
	}

	@Override
	public void OnPluginUIHide() {
	}

	public boolean pluginAutoHide() {
		return false;
	}

	private View pluginView;
	private ListView lvFunctions;
	private FuncsAdapter funcsAdapter;
	private FunctionItem functionItem;
	private Button btnAdd, btnSub;
	private Function function;
	private SeekBar seekBar;
	private int factor;
	private TextView tvLevel;

	@Override
	public View OnPluginUIShow() {
		if (pluginView == null) {
			Log.i("GameAssist", "uishow");
			pluginView = LayoutInflater.from(getContext()).inflate(
					R.layout.prompt, null);
			pluginView.findViewById(R.id.close).setOnClickListener(this);
			pluginView.findViewById(R.id.minimum).setOnClickListener(this);
			pluginView.findViewById(R.id.radioMenuRole)
					.setOnClickListener(this);
			pluginView.findViewById(R.id.radioMenuGame)
					.setOnClickListener(this);
			pluginView.findViewById(R.id.radioMenuAddion).setOnClickListener(
					this);
			btnAdd = (Button) pluginView.findViewById(R.id.btnAdd);
			btnAdd.setOnClickListener(this);
			btnSub = (Button) pluginView.findViewById(R.id.btnSub);
			btnSub.setOnClickListener(this);
			seekBar = (SeekBar) pluginView.findViewById(R.id.func_sb_level);
			seekBar.setOnSeekBarChangeListener(this);
			tvLevel = (TextView) pluginView.findViewById(R.id.func_level);

			lvFunctions = (ListView) pluginView.findViewById(R.id.lv_functions);
			funcsAdapter = new FuncsAdapter(context, getTargetApplication());
			lvFunctions.setAdapter(funcsAdapter);
			addDataRole();
			addDataGame();
			addDataAddion();
			funcsAdapter.addDatas(mListRole);
			lvFunctions.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						v.getParent().requestDisallowInterceptTouchEvent(true);
					}
					return false;
				}
			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean a = true;
					while (a) {
						for (FunctionItem functionItem : mListRole) {
							setItem(functionItem);
						}
						for (FunctionItem functionItem : mListGame) {
							setItem(functionItem);
						}
						for (FunctionItem functionItem : mListAddi) {
							setItem(functionItem);
						}
						a = (function.getValueOnce("F") == 0);
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();

			// if (isAuto) {
			// if (!mSet.isEmpty()) {
			// for (FunctionItem functionItem : mSet) {
			// function.setValueFac(
			// functionItem.getFieldName(),
			// functionItem.getValue(),
			// functionItem.getLevel());
			// }
			// isAuto = false;
			// }
			// }
			//

			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						if (isAuto) {
							Log.i(tag, "size" + mSet.size());
							if (!mSet.isEmpty()) {
								for (FunctionItem functionItem : mSet) {
									int a = function.setValueFac(
											functionItem.getFieldName(),
											functionItem.getValue(),
											functionItem.getLevel());
									function.getValueOnce(functionItem
											.getFieldName());
								}
							}
						}
						int intu = function.initEL("u");
						Log.i(tag, "u----" + intu);
						if (intu == 1) {
							isAuto = false;
						} else if (intu == 0) {
							isAuto = true;
						} else if (intu == 2) {
							isAuto = true;
						}
						// Log.i(tag, "m----" + function.initDD("m"));
						// Log.i(tag, "v----" + function.initFF("v"));
						// Log.i(tag, "T----" + function.initDD("T"));
						// Log.i(tag, "az----" + function.initDD("az"));
						// Log.i(tag, "q----" + function.initFF("q"));
						// Log.i(tag, "z----" + function.initEL("z"));
						// Log.i(tag, "a----" + function.getValueOnce("a"));
						// Log.i(tag, "G----" + function.getValueOnce("G"));
						// Log.i(tag, "bo----" + function.getValueOnce("bo"));
						// Log.i(tag, "bp----" + function.getValueOnce("bp"));
						// Log.i(tag, "v----" + function.getValueOnce("v"));
						// if (function.initDD("m") == 1) {
						// isAuto = true;
						// } else {
						// isAuto = (function.getValueOnce("F") != 0) ? false
						// : true;
						// }
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		return pluginView;
	}

	private void setItem(FunctionItem functionItem) {
		int a = function.getValueOnce(functionItem.getFieldName());
		functionItem.setValue(a);
		// Log.i(tag,
		// functionItem.getName() + "---" + a + "---"
		// + functionItem.getFieldName());
	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // TODO Auto-generated method stub
	// for (FunctionItem fi : mList) {
	// fi.setChecked(false);
	// }
	// mList.get(position).setChecked(true);
	// functionItem = mList.get(position);
	// funcsAdapter.notifyDataSetChanged();
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			closeSelf();
			break;
		case R.id.minimum:
			hideSelf();
			break;
		case R.id.radioMenuRole:
			funcsAdapter.addDatas(mListRole);
			break;
		case R.id.radioMenuGame:
			funcsAdapter.addDatas(mListGame);
			break;
		case R.id.radioMenuAddion:
			funcsAdapter.addDatas(mListAddi);
			break;
		case R.id.btnAdd:
			functionItem = funcsAdapter.getmFunctionItem();
			if (functionItem != null) {
				if (factor <= 1) {
					factor = 1;
				}
				if (function.getValueOnce(functionItem.getFieldName()) == 0) {
					function.setValueZero(functionItem.getFieldName());
					Log.i(tag,
							function.getValueOnce(functionItem.getFieldName())
									+ "----" + functionItem.getName());
				} else {
					function.setValueFac(functionItem.getFieldName(),
							functionItem.getValue(), factor);
					Log.i(tag,
							function.getValueOnce(functionItem.getFieldName())
									+ "----" + functionItem.getName());
				}
				mSet.add(functionItem);
				functionItem.setLevel(factor);
				Toast.makeText(getContext(),
						functionItem.getName() + "x" + functionItem.getLevel(),
						Toast.LENGTH_SHORT).show();
				funcsAdapter.notifyDataSetChanged();
			}
			break;
		case R.id.btnSub:
			Log.i(tag, "" + factor);
			if (!mSet.isEmpty()) {
				for (FunctionItem functionItem : mSet) {
					function.setValueFac(functionItem.getFieldName(),
							functionItem.getValue(), functionItem.getLevel());
				}
			}
			break;
		// case R.id.btnSub:
		// Log.i(tag, "" + factor);
		// functionItem = funcsAdapter.getmFunctionItem();
		// if (functionItem != null) {
		// factor = functionItem.getLevel();
		// factor = (int) (factor * 0.5);
		// if (factor <= 1) {
		// factor = 1;
		// } else {
		// function.setValue(functionItem.getFieldName(), 0);
		// }
		// functionItem.setLevel(factor);
		// funcsAdapter.notifyDataSetChanged();
		// }
		// break;

		}
	}

	// case R.id.func1:
	// try {
	// Class<?> clz1 = clLoader.loadClass("net.hexage.reaper.el");
	// Field fieldb = clz1.getDeclaredField("b");
	// fieldb.setAccessible(true);
	// Object objectb = fieldb.get(clz1);
	// Field fieldp = objectb.getClass().getDeclaredField("p");
	// fieldp.setAccessible(true);
	// Object objectp = fieldp.get(objectb);
	//
	// Field health = objectp.getClass().getDeclaredField("F");
	// Field resurrectionChance = objectp.getClass().getDeclaredField(
	// "aR");
	// Field criticalDamage = objectp.getClass()
	// .getDeclaredField("as");
	// Field resurrectionHealth = objectp.getClass().getDeclaredField(
	// "aS");
	//
	// health.setAccessible(true);
	// resurrectionChance.setAccessible(true);
	// criticalDamage.setAccessible(true);
	// resurrectionHealth.setAccessible(true);
	//
	// health.set(objectp, 65536000);
	// resurrectionChance.set(objectp, 655360);
	// resurrectionHealth.set(objectp, 6553600);
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// break;

	private void addDataRole() {
		mListRole.add(new FunctionItem("护甲", "D", 1, 0, false));
		mListRole.add(new FunctionItem("头盔", "E", 1, 0, false));
		mListRole.add(new FunctionItem("血量", "F", 1, 0, false));
		mListRole.add(new FunctionItem("力量", "H", 1, 0, false));
		mListRole.add(new FunctionItem("能量", "I", 1, 0, false));

		mListRole.add(new FunctionItem("兴奋", "J", 1, 0, false));
		mListRole.add(new FunctionItem("兴奋撞击", "K", 1, 0, false));
		mListRole.add(new FunctionItem("兴奋击杀", "L", 1, 0, false));

		mListRole.add(new FunctionItem("移动速度", "N", 1, 0, false));

		mListRole.add(new FunctionItem("跳跃限制", "O", 1, 0, false));
		mListRole.add(new FunctionItem("跳跃速度", "P", 1, 0, false));
		mListRole.add(new FunctionItem("跳跃速度小数", "Q", 1, 0, false));
	}

	private void addDataGame() {
		mListGame.add(new FunctionItem("格档机率", "S", 1, 0, false));

		mListGame.add(new FunctionItem("暴击率", "ar", 1, 0, false));
		mListGame.add(new FunctionItem("暴击伤害", "as", 1, 0, false));
		mListGame.add(new FunctionItem("暴击晕眩几率", "at", 1, 0, false));
		mListGame.add(new FunctionItem("暴击命中几率", "au", 1, 0, false));
		mListGame.add(new FunctionItem("暴击背刺几率", "av", 1, 0, false));

		mListGame.add(new FunctionItem("冲锋伤害", "aw", 1, 0, false));
		mListGame.add(new FunctionItem("冲锋距离", "ax", 1, 0, false));
		mListGame.add(new FunctionItem("冲锋晕眩几率", "ay", 1, 0, false));
		mListGame.add(new FunctionItem("冲锋晕眩时间", "az", 1, 0, false));

		mListGame.add(new FunctionItem("上挑伤害", "aA", 1, 0, false));
		mListGame.add(new FunctionItem("上挑眩晕几率", "aB", 1, 0, false));
		mListGame.add(new FunctionItem("上挑眩晕时间", "aC", 1, 0, false));

		mListGame.add(new FunctionItem("叩击伤害", "aD", 1, 0, false));
		mListGame.add(new FunctionItem("叩击眩晕几率", "aE", 1, 0, false));
		mListGame.add(new FunctionItem("叩击眩晕时间", "aF", 1, 0, false));
		mListGame.add(new FunctionItem("叩击暴击几率", "aG", 1, 0, false));

		mListGame.add(new FunctionItem("旋风斩伤害", "aH", 1, 0, false));
		mListGame.add(new FunctionItem("旋风格铛几率", "aI", 1, 0, false));

		mListGame.add(new FunctionItem("重踏范围", "aJ", 1, 0, false));
		mListGame.add(new FunctionItem("重踏伤害", "aK", 1, 0, false));
		mListGame.add(new FunctionItem("重踏眩晕几率", "aL", 1, 0, false));
		mListGame.add(new FunctionItem("重踏眩晕时间", "aM", 1, 0, false));
	}

	private void addDataAddion() {
		mListAddi.add(new FunctionItem("掠夺", "aN", 1, 0, false));
		mListAddi.add(new FunctionItem("掠夺奖励", "aO", 1, 0, false));
		mListAddi.add(new FunctionItem("掠夺几率", "aP", 1, 0, false));

		mListAddi.add(new FunctionItem("精神伤害", "aQ", 1, 0, false));

		mListAddi.add(new FunctionItem("复活几率", "aR", 1, 0, false));
		mListAddi.add(new FunctionItem("复活血量", "aS", 1, 0, false));

		mListAddi.add(new FunctionItem("伤害系数", "aT", 1, 0, false));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress <= 0) {
			progress = 1;
		}
		tvLevel.setText("x" + progress);
		factor = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

}

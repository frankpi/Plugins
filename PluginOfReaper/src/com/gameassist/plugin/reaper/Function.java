package com.gameassist.plugin.reaper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Application;
import android.util.Log;

public class Function {

	public Function() {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final String TAG = "gameassist";
	private static ClassLoader clLoader;
	private Object op, objectb;
	private Class<?> clzek;
	private Field fieldb, fieldp;

	public Function(Application application) {
		// TODO Auto-generated constructor stub
		super();
		clLoader = application.getClassLoader();
	}

	void init() {
		try {
			clzek = clLoader.loadClass("net.hexage.reaper.el");
			fieldb = clzek.getDeclaredField("b");
			fieldb.setAccessible(true);
			objectb = fieldb.get(clzek);
			fieldp = objectb.getClass().getDeclaredField("p");
			fieldp.setAccessible(true);
			op = fieldp.get(objectb);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int setValueFac(String fieldName, int value, int fac) {
		init();
		Field mField = null;
		try {
			mField = op.getClass().getDeclaredField(fieldName);
			mField.setAccessible(true);
			mField.setInt(op, value * fac);
			value = mField.getInt(op);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return value;
	}

	public int setValueZero(String fieldName) {
		init();
		Field mField = null;
		int value = 0;
		try {
			mField = op.getClass().getDeclaredField(fieldName);
			mField.setAccessible(true);
			value = mField.getInt(op);
			value = 65536;
			mField.setInt(op, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return value;
	}

	public int getValueOnce(String fieldName) {
		init();
		Field mField = null;
		int value = 0;
		try {
			mField = op.getClass().getDeclaredField(fieldName);
			mField.setAccessible(true);
			value = mField.getInt(op);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return value;
	}

	public void setValue(String fieldName, int d) {
		init();
		Field mField = null;
		int value = 0;
		try {
			mField = op.getClass().getDeclaredField(fieldName);
			mField.setAccessible(true);
			value = mField.getInt(op);
			if (d == 0) {
				value = (int) (value * 0.5);
				mField.setInt(op, value);
			} else {
				value = (int) (value + value);
				mField.set(op, value);
			}
			Log.i(TAG, fieldName + "---" + value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "没有");
		}
	}

	int initDD(String fieldname) {
		// 分数，金币，经验又一个反射方法；
		int intm = 0;
		try {
			clzek = clLoader.loadClass("net.hexage.reaper.dd");
			Field fdb = clzek.getDeclaredField("b");
			fdb.setAccessible(true);
			Object db = fdb.get(clzek);
			// Method dba = db.getClass().getDeclaredMethod("a", boolean.class,
			// boolean.class, int.class, int.class, int.class);
			// dba.invoke(db, true, true, 0, 100000, 10000);// ，分数，金币，经验
			Field m = db.getClass().getDeclaredField(fieldname);
			m.setAccessible(true);
			intm = m.getInt(db);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		return intm;
	}
	
	
	int initFF(String fieldname) {
		// 分数，金币，经验又一个反射方法；
		int intm = 0;
		try {
			clzek = clLoader.loadClass("net.hexage.reaper.ff");
			Field fdb = clzek.getDeclaredField("m");
			fdb.setAccessible(true);
			Object db = fdb.get(clzek);
			// Method dba = db.getClass().getDeclaredMethod("a", boolean.class,
			// boolean.class, int.class, int.class, int.class);
			// dba.invoke(db, true, true, 0, 100000, 10000);// ，分数，金币，经验
			Field m = db.getClass().getDeclaredField(fieldname);
			m.setAccessible(true);
			intm = m.getInt(db);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		return intm;
	}
	
	
	public int initEL(String fieldname) {
		// 分数，金币，经验又一个反射方法；
		int intm = 0;
		try {
			clzek = clLoader.loadClass("net.hexage.reaper.el");
			Field fdb = clzek.getDeclaredField("b");
			fdb.setAccessible(true);
			Object db = fdb.get(clzek);
			// Method dba = db.getClass().getDeclaredMethod("a", boolean.class,
			// boolean.class, int.class, int.class, int.class);
			// dba.invoke(db, true, true, 0, 100000, 10000);// ，分数，金币，经验
			Field m = db.getClass().getDeclaredField(fieldname);
			m.setAccessible(true);
			intm = m.getInt(db);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		return intm;
	}
	
	public int initFr(String fieldname) {
		// 分数，金币，经验又一个反射方法；
		int intm = 0;
		try {
			clzek = clLoader.loadClass("net.hexage.reaper.fr");
			Field fdb = clzek.getDeclaredField("a");
			fdb.setAccessible(true);
			Object db = fdb.get(clzek);
			// Method dba = db.getClass().getDeclaredMethod("a", boolean.class,
			// boolean.class, int.class, int.class, int.class);
			// dba.invoke(db, true, true, 0, 100000, 10000);// ，分数，金币，经验
			Field m = db.getClass().getDeclaredField(fieldname);
			m.setAccessible(true);
			intm = m.getInt(db);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
		return intm;
	}
}

/*
 * v2_1.a("\n\t armor => ").c(this.D); v2_1.a("\n\t armorSkull => ").c(this.E);
 * v2_1.a("\n\t health => ").c(this.F); v2_1.a("\n\t power=> ").c(this.I);
 * v2_1.a("\n\t strength => ").c(this.H);
 * 
 * 肾上腺，兴奋 v2_1.a("\n\t adrenaline => ").c(this.J);
 * v2_1.a("\n\t adrenalineHit => ").c(this.K);
 * v2_1.a("\n\t adrenalineKill => ").c(this.L);
 * 
 * 跳跃 v2_1.a("\n\t jumpLimit => ").c(this.O);
 * v2_1.a("\n\t jumpSpeed => ").c(this.P);
 * v2_1.a("\n\t jumpSpeedDouble => ").c(this.Q);
 * 
 * v2_1.a("\n\t blockChance => ").c(this.S);
 * 
 * 暴击 v2_1.a("\n\t criticalChance => ").c(this.ar);
 * v2_1.a("\n\t criticalDamage => ").c(this.as);
 * v2_1.a("\n\t criticalStunnedChance => ").c(this.at);
 * v2_1.a("\n\t criticalFocusedChance => ").c(this.au);
 * v2_1.a("\n\t criticalBackstabChance => ").c(this.av);
 * 
 * 冲锋 v2_1.a("\n\t chargeDamage => ").c(this.aw);
 * v2_1.a("\n\t chargeDistance => ").c(this.ax);
 * v2_1.a("\n\t chargeStunChance => ").c(this.ay);
 * bg.a(v2_1.a("\n\t chargeStunDuration => "), this.az);
 * 
 * stun 击昏
 * 
 * 上勾 v2_1.a("\n\t uppercutDamage => ").c(this.aA);
 * v2_1.a("\n\t uppercutStunChance => ").c(this.aB);
 * bg.a(v2_1.a("\n\t uppercutStunDuration => "), this.aC);
 * 
 * 撞击 v2_1.a("\n\t slamDamage => ").c(this.aD);
 * v2_1.a("\n\t slamStunChance => ").c(this.aE);
 * bg.a(v2_1.a("\n\t slamStunDuration => "), this.aF);
 * v2_1.a("\n\t slamCriticalChance => ").c(this.aG);
 * 
 * 回旋 v2_1.a("\n\t whirlwindDamage => ").c(this.aH);
 * v2_1.a("\n\t whirlwindBlockChance => ").c(this.aI);
 * 
 * 重踏 v2_1.a("\n\t stompRange => ").c(this.aJ);
 * v2_1.a("\n\t stompDamage => ").c(this.aK);
 * v2_1.a("\n\t stompStunChance => ").c(this.aL);
 * bg.a(v2_1.a("\n\t stompStunDuration => "), this.aM);
 * 
 * 掠夺，战利品 v2_1.a("\n\t lootGreed => ").c(this.aN);
 * bg.a(v2_1.a("\n\t lootBonus => "), this.aO);
 * v2_1.a("\n\t lootChance => ").c(this.aP);
 * 
 * 警觉力 v2_1.a("\n\t awarenessDamage => ").c(this.aQ);
 * 
 * 复活 v2_1.a("\n\t resurrectionChance => ").c(this.aR);
 * v2_1.a("\n\t resurrectionHealth => ").c(this.aS);、 伤害
 * v2_1.a("\n\t damageFactor => ").c(this.aT);
 */

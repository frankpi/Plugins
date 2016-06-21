package com.gameassist.plugin.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class WingItem implements Serializable {

	private boolean isCheck;
    private int amount;
    private short durability;
    private short id;
    private int mInBagIndex;
    private int mInGameBagIndex;
    private boolean mIsAutoBuild;
    
    public void setIsAutoBuild(boolean mIsAutoBuild) {
		this.mIsAutoBuild = mIsAutoBuild;
	}

	private int mItemCount;
    private int mItemDmg;
    private int mItemId;
    private int mItemImgId;
    private String mItemName;
    private Bitmap mJsBitmap;
    private static final long serialVersionUID = 8918321225553688992L;

    public WingItem(int arg2, int arg3, boolean arg4, String arg5, int arg6) {
        super();
        this.mJsBitmap = null;
        this.setItemInfo(arg2, arg3, arg4, arg5, arg6);
    }

    public WingItem(WingItem arg2) {
        super();
        this.mJsBitmap = null;
        this.setItemInfo(arg2);
    }

    public WingItem(short arg2, short arg3, int arg4) {
        super();
        this.mJsBitmap = null;
        this.id = arg2;
        this.durability = arg3;
        this.amount = arg4;
    }

    public WingItem() {
		// TODO Auto-generated constructor stub
	}

	public void clearItemInfo() {
        this.mItemName = "";
        this.mJsBitmap = null;
        this.mIsAutoBuild = false;
        this.mInBagIndex = -1;
        this.mItemImgId = 0;
        this.mItemDmg = 0;
        this.mItemId = 0;
        this.mItemCount = 0;
    }

    public int getAmount() {
        return this.amount;
    }

    public short getDurability() {
        return this.durability;
    }

    public int getItemBagIdx() {
        return this.mInBagIndex;
    }

    public boolean getItemBuild() {
        return this.mIsAutoBuild;
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    public int getItemDmg() {
        return this.mItemDmg;
    }

    public int getItemId() {
        return this.mItemId;
    }

    public Bitmap getItemImage() {
        return this.mJsBitmap;
    }

    public int getItemImgId() {
        return this.mItemImgId;
    }

    public String getItemName() {
        return this.mItemName;
    }

    public short getTypeId() {
        return this.id;
    }

    public int getmInGameBagIndex() {
        return this.mInGameBagIndex;
    }

    public void setAmount(int arg1) {
        this.amount = arg1;
    }

    public void setDurability(short arg1) {
        this.durability = arg1;
    }

    public void setItemBagIdx(int arg1) {
        this.mInBagIndex = arg1;
    }

    public void setItemBitmap(Bitmap arg1) {
        this.mJsBitmap = arg1;
    }

    public void setItemCount(int arg1) {
        this.mItemCount = arg1;
    }

    public void setItemInfo(int arg2, int arg3, boolean arg4, String arg5, int arg6) {
        this.mInBagIndex = 0;
        this.mItemCount = 0;
        this.mItemId = arg2;
        this.mItemDmg = arg3;
        this.mItemName = arg5;
        this.mItemImgId = arg6;
        this.mIsAutoBuild = arg4;
        this.id = ((short)arg2);
        this.durability = ((short)arg3);
        this.amount = 1;
    }

    public void setItemInfo(WingItem arg2) {
        if(arg2 != null) {
            this.mItemId = arg2.mItemId;
            this.mItemDmg = arg2.mItemDmg;
            this.mItemName = arg2.mItemName;
            this.mJsBitmap = arg2.mJsBitmap;
            this.mItemImgId = arg2.mItemImgId;
            this.mItemCount = arg2.mItemCount;
            this.mInBagIndex = arg2.mInBagIndex;
            this.mIsAutoBuild = arg2.mIsAutoBuild;
        }
    }

    public void setTypeId(short arg1) {
        this.id = arg1;
    }

    public void setmInGameBagIndex(int arg1) {
        this.mInGameBagIndex = arg1;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}


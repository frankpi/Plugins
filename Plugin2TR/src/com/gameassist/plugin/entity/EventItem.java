package com.gameassist.plugin.entity;


public class EventItem {

	private int itemId;
	private String  itemName;
	private int itemImgId;
	private boolean isCheck;
	private boolean isSellect;
	/**
	 * 
	 */
	public EventItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param itemId
	 * @param itemName
	 * @param itemImgId
	 * @param isCheck
	 * @param isSellect
	 */
	public EventItem(boolean isCheck, boolean isSellect,int itemId, int itemImgId,
			String itemName) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemImgId = itemImgId;
		this.isCheck = isCheck;
		this.isSellect = isSellect;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemImgId() {
		return itemImgId;
	}
	public void setItemImgId(int itemImgId) {
		this.itemImgId = itemImgId;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public boolean isSellect() {
		return isSellect;
	}
	public void setSellect(boolean isSellect) {
		this.isSellect = isSellect;
	}
	

}

package com.gameassist.plugin.reaper;

import android.os.Parcelable;

public class FunctionItem {



	public FunctionItem( String name, String fieldName,int level,
			int value, boolean checked) {
		super();
		this.level = level;
		this.name = name;
		this.fieldName = fieldName;
		this.value = value;
		this.checked = checked;
	}

	public FunctionItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	int level;
	String name;
	String fieldName;
	int value;
	boolean checked;
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


}

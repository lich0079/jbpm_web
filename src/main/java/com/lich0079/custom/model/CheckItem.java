package com.lich0079.custom.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckItem implements Serializable{
	String descript = "";
	String item = "";
	String note = "";
	boolean checked = false;
	boolean patientSpecific = false;

	public boolean needData = false;
	boolean alert;
	String patientID = "";

	List dataList = null;
	String dataType = "";
	
	

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isPatientSpecific() {
		return patientSpecific;
	}

	public void setPatientSpecific(boolean patientSpecific) {
		this.patientSpecific = patientSpecific;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public void addData(Object data) {
		if (null == dataList) {
			dataList = new ArrayList();
		}
		dataList.add(data);
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean hasData() {
		return null != dataList && !dataList.isEmpty();
	}

	public boolean isNeedData() {
		return needData;
	}

	public void setNeedData(boolean needData) {
		this.needData = needData;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	@Override
	public String toString() {
		return "CheckItem [descript=" + descript + ", item=" + item + ", note="
				+ note + ", alert=" + alert + ", patientID=" + patientID
				+ ", dataList=" + dataList + ", dataType=" + dataType + "]";
		

	}

	
}

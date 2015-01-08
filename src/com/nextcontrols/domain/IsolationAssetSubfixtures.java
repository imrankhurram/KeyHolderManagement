package com.nextcontrols.domain;

import java.io.Serializable;

public class IsolationAssetSubfixtures   implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int alarmAssetId;
	private long subfixtureId;
	
	public IsolationAssetSubfixtures(){}
	
	public IsolationAssetSubfixtures(int alarmAssetId,long subfixtureId){
		this.alarmAssetId=alarmAssetId;
		this.subfixtureId=subfixtureId;
	}
	
	public void setAlarmAssetId(int alarmAssetId) {
		this.alarmAssetId = alarmAssetId;
	}
	public int getAlarmAssetId() {
		return alarmAssetId;
	}
	public void setSubfixtureId(long subfixtureId) {
		this.subfixtureId = subfixtureId;
	}
	public long getSubfixtureId() {
		return subfixtureId;
	} 

}

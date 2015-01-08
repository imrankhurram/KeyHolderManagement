package com.nextcontrols.domain;

import java.io.Serializable;

public class AssetSearchStringIsolation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long searchStringId;
	private int alarmAssetId;
	private String controllerSearch;
	private String alarmSearch;
	private String subfixture;
	private boolean isolate;
	
	public AssetSearchStringIsolation(){}
	
	public AssetSearchStringIsolation(long searchStringId,int alarmAssetId,String controllerSearch, String alarmSearch,String subfixture,boolean isolate){
		this.searchStringId=searchStringId;
		this.alarmAssetId=alarmAssetId;
		this.controllerSearch=controllerSearch;
		this.alarmSearch=alarmSearch;
		this.subfixture=subfixture;
		this.isolate=isolate;
	}
	
	public void setSearchStringId(long searchStringId) {
		this.searchStringId = searchStringId;
	}
	public long getSearchStringId() {
		return searchStringId;
	}
	
	public void setAlarmAssetId(int alarmAssetId) {
		this.alarmAssetId = alarmAssetId;
	}
	public int getAlarmAssetId() {
		return alarmAssetId;
	}
	
	public void setControllerSearch(String controllerSearch) {
		this.controllerSearch = controllerSearch;
	}
	public String getControllerSearch() {
		return controllerSearch;
	}
	
	public void setAlarmSearch(String alarmSearch) {
		this.alarmSearch = alarmSearch;
	}
	public String getAlarmSearch() {
		return alarmSearch;
	}

	public void setSubfixture(String subfixture) {
		this.subfixture = subfixture;
	}

	public String getSubfixture() {
		return subfixture;
	}

	public void setIsolate(boolean isolate) {
		this.isolate = isolate;
	}

	public boolean isIsolate() {
		return isolate;
	}
}

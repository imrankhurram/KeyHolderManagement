package com.nextcontrols.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import com.nextcontrols.dao.IsolationDAO;


public class AlarmAssetIsolation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int alarmAssetId;
	private int departmentId;
	private String fixtureName;
	private String fixtureType;
	private boolean wholeFixture;
	private boolean subfixtureConfig;

	private String Value_Currency;
	private double Stock_Value;
	private double Low_Temp;
	private double High_Temp;
	private double Low_Temp_Offset;	
	private double High_Temp_Offset;
	private Date Installation_Date;
	
	private List<AssetSearchStringIsolation> subfixtureIdList;
	private int subfixIdsSize=0;
	
	public AlarmAssetIsolation(){
		this.subfixtureIdList=new ArrayList<AssetSearchStringIsolation>();
	}
	
	public AlarmAssetIsolation(int alarmAssetId,int departmentId, String fixtureName,
			String fixtureType,boolean wholeFixture,boolean subfixtureConfig){
		this.subfixtureIdList=new ArrayList<AssetSearchStringIsolation>();
		this.alarmAssetId=alarmAssetId;
		this.departmentId=departmentId;
		this.fixtureName=fixtureName;
		this.fixtureType=fixtureType;
		this.wholeFixture=wholeFixture;
		this.subfixtureConfig=subfixtureConfig;
		
	}
	
	public void setAlarmAssetId(int alarmAssetId) {
		this.alarmAssetId = alarmAssetId;
	}
	public int getAlarmAssetId() {
		return alarmAssetId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setFixtureName(String fixtureName) {
		this.fixtureName = fixtureName;
	}
	public String getFixtureName() {
		return fixtureName;
	}
	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}
	public String getFixtureType() {
		return fixtureType;
	}
	public void setWholeFixture(boolean wholeFixture) {
		this.wholeFixture = wholeFixture;
	}
	public boolean isWholeFixture() {
		return wholeFixture;
	}
	public void setSubfixtureConfig(boolean subfixtureConfig) {
		this.subfixtureConfig = subfixtureConfig;
	}
	public boolean isSubfixtureConfig() {
		return subfixtureConfig;
	}

	public void setSubfixtureIdList() {
		this.subfixtureIdList = IsolationDAO.getInstance().getSubfixtureIsolation(alarmAssetId);
	}
	
	public void setSubfixtureIdList(List<AssetSearchStringIsolation> SubfixtureIdList) {
		this.subfixtureIdList = SubfixtureIdList;

	}

	public List<AssetSearchStringIsolation> getSubfixtureIdList() {
		if (subfixtureIdList.size()==0){
			setSubfixtureIdList();
		}
		return subfixtureIdList;
	}
	
	public void clearSubfixChoiceList(){
		for (int i=0;i<=this.subfixtureIdList.size()-1;i++){
			this.subfixtureIdList.get(i).setIsolate(false);
		}
		subfixIdsSize=0;
	}

	public void setSubfixIdsSize() {
		subfixIdsSize=0;
		if (this.subfixtureIdList.size()>0){
			for (int i=0;i<=this.subfixtureIdList.size()-1;i++){
				if (this.subfixtureIdList.get(i).isIsolate()==true){
					subfixIdsSize++;
				}
			}
		}else{
			subfixIdsSize=0;
		}
	}

	public int getSubfixIdsSize() {
		setSubfixIdsSize();
		if (subfixIdsSize>0){
			this.setWholeFixture(false);
		}
		return subfixIdsSize;
	}
	
	public void addSubfixtures(ActionEvent e){
		setSubfixIdsSize();
		if (subfixIdsSize>0){
			this.setWholeFixture(false);
		}
	}

	public String getValue_Currency() {
		return Value_Currency;
	}

	public void setValue_Currency(String value_Currency) {
		Value_Currency = value_Currency;
	}

	public double getStock_Value() {
		return Stock_Value;
	}

	public void setStock_Value(double stock_Value) {
		Stock_Value = stock_Value;
	}

	public double getLow_Temp() {
		return Low_Temp;
	}

	public void setLow_Temp(double low_Temp) {
		Low_Temp = low_Temp;
	}

	public double getHigh_Temp() {
		return High_Temp;
	}

	public void setHigh_Temp(double high_Temp) {
		High_Temp = high_Temp;
	}

	public double getLow_Temp_Offset() {
		return Low_Temp_Offset;
	}

	public void setLow_Temp_Offset(double low_Temp_Offset) {
		Low_Temp_Offset = low_Temp_Offset;
	}

	public double getHigh_Temp_Offset() {
		return High_Temp_Offset;
	}

	public void setHigh_Temp_Offset(double high_Temp_Offset) {
		High_Temp_Offset = high_Temp_Offset;
	}

	public Date getInstallation_Date() {
		return Installation_Date;
	}

	public void setInstallation_Date(Date installation_Date) {
		Installation_Date = installation_Date;
	}

}

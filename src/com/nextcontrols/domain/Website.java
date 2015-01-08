package com.nextcontrols.domain;

// default package
// Generated 09-Feb-2010 15:54:24 by Hibernate Tools 3.2.2.GA

//import com.nextcontrols.utility.ROT;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;

public class Website implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int websiteId;
	private String name;
	private String meterName;
	private String hvacName;
	private String fixtureName;
	private String alarmName;
	private String imagePath;
	private String branchListName;
	private String Logo;
	private boolean typetutela;
	private int inactivityTimeout;
	private String countryCode;

	public Website() {
	}

	public Website(int websiteId, String name, String meterName,
			String hvacName, String fixtureName, String alarmName,
			String imagePath, String branchListName, String logo,
			boolean typetutela, int inactivityTimeout, String countryCode) {
		super();
		this.websiteId = websiteId;
		this.name = name;
		this.meterName = meterName;
		this.hvacName = hvacName;
		this.fixtureName = fixtureName;
		this.alarmName = alarmName;
		this.imagePath = imagePath;
		this.branchListName = branchListName;
		Logo = logo;
		this.typetutela = typetutela;
		this.inactivityTimeout = inactivityTimeout;
		this.countryCode = countryCode;
	}
	  public Website(int pID){
	    	this.websiteId = pID;
	    	this.name = "";
	        this.alarmName= "";
	        this.branchListName= "";
	        this.imagePath = "";
	        this.Logo = "";
	 		this.inactivityTimeout = 5;
	 		this.meterName = "";
	     	this.hvacName = "";
	     	this.fixtureName = "";
	     	this.countryCode="GBR";
		}
	public int getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public String getHvacName() {
		return hvacName;
	}

	public void setHvacName(String hvacName) {
		this.hvacName = hvacName;
	}

	public String getFixtureName() {
		return fixtureName;
	}

	public void setFixtureName(String fixtureName) {
		this.fixtureName = fixtureName;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getBranchListName() {
		return branchListName;
	}

	public void setBranchListName(String branchListName) {
		this.branchListName = branchListName;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public boolean isTypetutela() {
		return typetutela;
	}

	public void setTypetutela(boolean typetutela) {
		this.typetutela = typetutela;
	}

	public int getInactivityTimeout() {
		return inactivityTimeout;
	}

	public void setInactivityTimeout(int inactivityTimeout) {
		this.inactivityTimeout = inactivityTimeout;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

}
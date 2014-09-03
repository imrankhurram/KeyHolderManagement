package com.nextcontrols.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UserAudit implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private Timestamp auditDate;
	private String actionType;
	private String actionDescription;
	private String siteCode;
	private String siteName="";
	private String userName="";
	private String loggedDate;
	
	
	

	public UserAudit(int puserId, Timestamp pauditDate, String pactionType, String pactionDescription,String siteCode) {
		this.userId = puserId;
		this.auditDate = pauditDate;
		if(this.auditDate!=null){
			setLoggedDate(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.auditDate));
		}
		this.actionType = pactionType;
		this.actionDescription = pactionDescription;
		this.siteCode=siteCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
	public String getLoggedDate() {
		return loggedDate;
	}

	public void setLoggedDate(String loggedDate) {
		this.loggedDate = loggedDate;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
}

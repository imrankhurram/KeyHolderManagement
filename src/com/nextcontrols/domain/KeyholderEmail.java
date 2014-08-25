package com.nextcontrols.domain;

import java.io.Serializable;

public class KeyholderEmail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int emailId;
	private String email;
	
	public KeyholderEmail(){}
	
	public KeyholderEmail(int emailId,String email){
		this.emailId=emailId;
		this.email = email;
	}
	
	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}
	public int getEmailId() {
		return emailId;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
}

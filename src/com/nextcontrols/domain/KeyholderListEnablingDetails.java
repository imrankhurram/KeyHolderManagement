package com.nextcontrols.domain;

public class KeyholderListEnablingDetails implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int keyholderListId;
	private String listName;
	private String listType;
	private String displayName;
	private String fullDisplayName;
	private boolean enabled;
	private boolean contactPhone;
	private boolean contactSMS;
	private boolean contactEmail;
	private int contact_type;

	public KeyholderListEnablingDetails() {
		
	}

	public KeyholderListEnablingDetails(String listType,
			String displayName) {

		this.listType = listType;
		this.displayName = displayName;
	}

	public KeyholderListEnablingDetails(String listName, String listType,
			String displayName, boolean enabled, boolean contactPhone,
			boolean contactSMS, boolean contactEmail) {
		super();
		this.displayName="";
		this.listName = listName;
		this.listType = listType;
		this.displayName = displayName;
		this.enabled = enabled;
		this.contactPhone = contactPhone;
		this.contactSMS = contactSMS;
		this.contactEmail = contactEmail;
	}

	public int getKeyholderListId() {
		return keyholderListId;
	}

	public void setKeyholderListId(int keyholderListId) {
		this.keyholderListId = keyholderListId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(boolean contactPhone) {
		this.contactPhone = contactPhone;
		reverseContactSetup();
	}

	public boolean isContactSMS() {
		return contactSMS;
	}

	public void setContactSMS(boolean contactSMS) {
		this.contactSMS = contactSMS;
		reverseContactSetup();
	}

	public boolean isContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(boolean contactEmail) {
		this.contactEmail = contactEmail;
		reverseContactSetup();
	}

	public String getFullDisplayName() {
		return fullDisplayName;
	}

	public void setFullDisplayName(String fullDisplayName) {
		this.fullDisplayName = fullDisplayName;
	}
	
	public int getContact_type() {
		return contact_type;
	}

	public void setContact_type(int contact_type) {
		this.contact_type = contact_type;
	}

	private void reverseContactSetup(){
		if ((this.contactPhone==true)&&(this.contactSMS==false)&&(this.contactEmail==false)){
			this.setContact_type(1);
		}else if ((this.contactPhone==true)&&(this.contactSMS==true)&&(this.contactEmail==false)){
			this.setContact_type(2);
		}else if ((this.contactPhone==false)&&(this.contactSMS==true)&&(this.contactEmail==false)){
			this.setContact_type(3);
		}else if ((this.contactPhone==false)&&(this.contactSMS==true)&&(this.contactEmail==true)){
			this.setContact_type(4);
		}else if ((this.contactPhone==false)&&(this.contactSMS==false)&&(this.contactEmail==true)){
			this.setContact_type(5);
		}else if ((this.contactPhone==true)&&(this.contactSMS==false)&&(this.contactEmail==true)){
			this.setContact_type(6);
		}else if ((this.contactPhone==true)&&(this.contactSMS==true)&&(this.contactEmail==true)){
			this.setContact_type(7);
		}else{
			this.setContact_type(0);
		}
	}
	
}

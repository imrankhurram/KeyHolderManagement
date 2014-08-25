package com.nextcontrols.domain;

public class ContactTypeDetails implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int contactType;
    private String contactString;
	private String iconName;
	
	public ContactTypeDetails() {
	}
	public ContactTypeDetails(int contactType, String contactString,
			String iconName) {
		super();
		this.contactType = contactType;
		this.contactString = contactString;
		this.iconName = iconName;
	}
	public int getContactType() {
		return contactType;
	}
	public void setContactType(int contactType) {
		this.contactType = contactType;
	}
	public String getContactString() {
		return contactString;
	}
	public void setContactString(String contactString) {
		this.contactString = contactString;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
}

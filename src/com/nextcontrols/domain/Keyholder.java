package com.nextcontrols.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Keyholder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int keyholderId;
	private String contactName;
	private String fullName;
	private String namePosition;
	private String position;
	private boolean active;
	private String phone;
	private String mobile;
	private String fax;
	private String email;
	private String branchCode;
	private int priority;
	private int contact_type;
	private boolean activeinlist;
	private boolean redial;
	private boolean contactPhone;
	private boolean contactSMS;
	private boolean contactEmail;
	private String listType;
	// fields used in UI and pdf only
	private String contactString;
	private String iconName;
	// //////////
	private String displayName;// display name of keyholder list containing this
								// keyholder
	private int keyholderListId;
	private List<Integer> keyholderListIds;
	private Map<Integer,ContactTypeDetails> idWithContactType;
	private List<KeyholderList> keyholderList;

	public Keyholder() {
		contactPhone = false;
		contactSMS = false;
		contactEmail = false;
	}

	public Keyholder(int keyholderId, String contactName, String position,
			boolean active, String phone, String mobile, String fax,
			String email, String branchCode, int contact_type, boolean redial) {
		idWithContactType=new HashMap<Integer,ContactTypeDetails>();
		contactPhone = false;
		contactSMS = false;
		contactEmail = false;
		this.keyholderId = keyholderId;
		this.contactName = contactName;
		this.setFullName();
		this.position = position;
		this.setNamePosition();
		this.active = active;
		this.phone = phone;
		this.mobile = mobile;
		this.fax = fax;
		this.email = email;
		this.branchCode = branchCode;
		this.contact_type = contact_type;
		this.contactSetup(this.contact_type);
		this.redial = redial;
	}

	public int getKeyholderId() {
		return this.keyholderId;
	}

	public void setKeyholderId(int keyholderId) {
		this.keyholderId = keyholderId;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
		this.setNamePosition();
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isActiveinlist() {
		return activeinlist;
	}

	public void setActiveinlist(boolean activeinlist) {
		this.activeinlist = activeinlist;
	}

	public void setFullName() {
		this.fullName = this.contactName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setNamePosition() {
		this.namePosition = this.contactName + " (" + this.position + ")";
	}

	public String getNamePosition() {
		return namePosition;
	}

	public boolean equals(Object o) {

		if (o == this) {
			return true;
		}
		if (!(o instanceof Keyholder)) {
			return false;
		}
		Keyholder t = (Keyholder) o;
		return t.getKeyholderId() == getKeyholderId();
	}

	public int hashCode() {
		return getKeyholderId();
	}

	private void contactSetup(int contactType) {
		switch (contactType) {
		case 1:
			this.setContactPhone(true);
			this.setContactSMS(false);
			this.setContactEmail(false);
			break;
		case 2:
			this.setContactPhone(true);
			this.setContactSMS(true);
			this.setContactEmail(false);
			break;
		case 3:
			this.setContactPhone(false);
			this.setContactSMS(true);
			this.setContactEmail(false);
			break;
		case 4:
			this.setContactPhone(false);
			this.setContactSMS(true);
			this.setContactEmail(true);
			break;
		case 5:
			this.setContactPhone(false);
			this.setContactSMS(false);
			this.setContactEmail(true);
			break;
		case 6:
			this.setContactPhone(true);
			this.setContactSMS(false);
			this.setContactEmail(true);
			break;
		case 7:
			this.setContactPhone(true);
			this.setContactSMS(true);
			this.setContactEmail(true);
			break;
		default:
			this.setContactPhone(false);
			this.setContactSMS(false);
			this.setContactEmail(false);
			break;
		}
	}

	private void reverseContactSetup() {
		if ((this.contactPhone == true) && (this.contactSMS == false)
				&& (this.contactEmail == false)) {
			this.setContact_type(1);
		} else if ((this.contactPhone == true) && (this.contactSMS == true)
				&& (this.contactEmail == false)) {
			this.setContact_type(2);
		} else if ((this.contactPhone == false) && (this.contactSMS == true)
				&& (this.contactEmail == false)) {
			this.setContact_type(3);
		} else if ((this.contactPhone == false) && (this.contactSMS == true)
				&& (this.contactEmail == true)) {
			this.setContact_type(4);
		} else if ((this.contactPhone == false) && (this.contactSMS == false)
				&& (this.contactEmail == true)) {
			this.setContact_type(5);
		} else if ((this.contactPhone == true) && (this.contactSMS == false)
				&& (this.contactEmail == true)) {
			this.setContact_type(6);
		} else if ((this.contactPhone == true) && (this.contactSMS == true)
				&& (this.contactEmail == true)) {
			this.setContact_type(7);
		} else {
			this.setContact_type(0);
		}
	}

	public void setContact_type(int contact_type) {
		this.contact_type = contact_type;

	}

	public int getContact_type() {
		return this.contact_type;
	}

	public void setContactPhone(boolean contactPhone) {
		this.contactPhone = contactPhone;
		this.reverseContactSetup();
	}

	public boolean isContactPhone() {
		return contactPhone;
	}

	public void setContactSMS(boolean contactSMS) {
		this.contactSMS = contactSMS;
		this.reverseContactSetup();
	}

	public boolean isContactSMS() {
		return contactSMS;
	}

	public void setContactEmail(boolean contactEmail) {
		this.contactEmail = contactEmail;
		this.reverseContactSetup();
	}

	public boolean isContactEmail() {
		return contactEmail;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
		this.setNamePosition();
	}

	public String getContactName() {
		return contactName;
	}

	public void setRedial(boolean redial) {
		this.redial = redial;
	}

	public boolean isRedial() {
		return redial;
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

	public int getKeyholderListId() {
		return keyholderListId;
	}

	public void setKeyholderListId(int keyholderListId) {
		this.keyholderListId = keyholderListId;
	}

	public List<KeyholderList> getKeyholderList() {
		return keyholderList;
	}

	public void setKeyholderList(List<KeyholderList> keyholderList) {
		this.keyholderList = keyholderList;
	}

	public List<Integer> getKeyholderListIds() {
		return keyholderListIds;
	}

	public void setKeyholderListIds(List<Integer> keyholderListIds) {
		this.keyholderListIds = keyholderListIds;
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

	public Map<Integer, ContactTypeDetails> getIdWithContactType() {
		return idWithContactType;
	}

	public void setIdWithContactType(
			Map<Integer, ContactTypeDetails> idWithContactType) {
		this.idWithContactType = idWithContactType;
	}

	

}

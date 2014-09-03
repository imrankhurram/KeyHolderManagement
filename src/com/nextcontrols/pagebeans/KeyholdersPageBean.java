package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.dao.KeyholderDAO;
import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.domain.ContactTypeDetails;
import com.nextcontrols.domain.Department;
import com.nextcontrols.domain.Keyholder;
import com.nextcontrols.domain.KeyholderEmail;
import com.nextcontrols.domain.KeyholderList;
import com.nextcontrols.domain.KeyholderListEnablingDetails;
import com.nextcontrols.domain.UserAudit;
import com.nextcontrols.utility.ServiceProperties;

@ManagedBean(name = "keyholders")
@SessionScoped
public class KeyholdersPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Keyholder> keyHoldersList;
	private List<Keyholder> listKeyHolders;
	private KeyholderList selectedList;// contains only list id and display name
	private boolean showNormal;
	private Department selectedDepartment;
	private List<KeyholderList> listTypes;
	private int listKeyHoldersSize;
	private Keyholder selectedKeyholder;
	// private KeyholderList chosenList;
	private List<KeyholderListEnablingDetails> normalList;
	private List<KeyholderListEnablingDetails> holidayList;
	private Keyholder newKeyholder;
	private List<KeyholderListEnablingDetails> newNormalList;
	private List<KeyholderListEnablingDetails> newHolidayList;
	private List<String> contactType;
	private List<SelectItem> contactTypes;
	private boolean emailFlag;
	private List<KeyholderEmail> emails;
	private List<KeyholderEmail> modEmails;
	private int emailCount;
	private boolean selectedVoiceOnly;
	private boolean selectedSMSOnly;
	private boolean selectedEmailOnly;
	private int selectedListId;

	public KeyholdersPageBean() {
		this.showNormal = true;
		this.keyHoldersList = new ArrayList<Keyholder>();
		this.listKeyHolders = new ArrayList<Keyholder>();
		this.listTypes = new ArrayList<KeyholderList>();
		this.normalList = new ArrayList<KeyholderListEnablingDetails>();
		this.holidayList = new ArrayList<KeyholderListEnablingDetails>();
		// this.chosenList = null;
		this.selectedDepartment = null;
		this.selectedList = null;
		this.selectedKeyholder = null;
		this.newKeyholder = null;
		this.newNormalList = new ArrayList<KeyholderListEnablingDetails>();
		this.newHolidayList = new ArrayList<KeyholderListEnablingDetails>();
		this.listKeyHoldersSize = 0;
		this.contactTypes = new ArrayList<SelectItem>();
		this.emailFlag = false;
	}

	public void initializeKeyholders(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
		this.keyHoldersList.clear();
		this.listTypes.clear();
		this.showNormal = true;
		initializeKeyholders();
	}

	public String showKeyholders(boolean showNormal) {
		// System.out.println("show normal: "+showNormal);
		this.showNormal = showNormal;
		initializeKeyholders();
		RequestContext.getCurrentInstance().update("frmKeyholdersPage");
		return "KeyholdersNormalCallListPage.xhtml?faces-redirect=true";

	}

	private void initializeKeyholders() {
		if (this.showNormal) {
			this.listTypes = KeyholderDAO.getInstance().getKeyholdersTypes(
					this.selectedDepartment.getBranch_code(),
					this.selectedDepartment.getDep_id(), false);
			this.keyHoldersList = KeyholderDAO.getInstance().getFullKeyholders(
					this.selectedDepartment.getBranch_code(),
					this.selectedDepartment.getDep_id());

			// return "KeyholdersNormalCallListPage.xhtml?faces-redirect=true";
		} else {
			this.listTypes = KeyholderDAO.getInstance().getKeyholdersTypes(
					this.selectedDepartment.getBranch_code(),
					this.selectedDepartment.getDep_id(), true);
			this.keyHoldersList = KeyholderDAO.getInstance()
					.getSpecialKeyholders(
							this.selectedDepartment.getBranch_code(),
							this.selectedDepartment.getDep_id());
			// return "KeyholdersNormalCallListPage.xhtml?faces-redirect=true";
		}

	}

	public void retrieveListKeyholders(KeyholderList selectedList) {
		this.selectedList = selectedList;
		this.listKeyHolders.clear();
		// System.out.println("selected list : " +
		// selectedList.getDisplayName());
		// this.chosenList = KeyholderDAO.getInstance().getKeyholdersList(
		// this.selectedDepartment.getBranch_code(),
		// selectedList.getKeyholderListId());
		this.listKeyHolders = KeyholderDAO.getInstance().getListOfKeyholders(
				this.selectedDepartment.getBranch_code(),
				selectedList.getKeyholderListId());
		this.listKeyHoldersSize = this.listKeyHolders.size();

	}

	// public void processPriority(){
	// System.out.println("process priority called");
	// for (int i = 0; i < listKeyHolders.size(); i++) {
	// System.out.println("name: "+listKeyHolders.get(i).getContactName());
	// listKeyHolders.get(i).setPriority(i + 1);
	//
	// }
	// }
	public void saveKeyHoldersSequence() {
		// System.out.println("save keyholder sequence called");
		for (int i = 0; i < listKeyHolders.size(); i++) {
			// System.out.println("name: "+listKeyHolders.get(i).getContactName());
			listKeyHolders.get(i).setPriority(i + 1);

		}
		KeyholderDAO.getInstance().updateKeyholdersList(listKeyHolders,
				this.selectedList.getKeyholderListId());
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "KeyholdersInListUpdated",
						"The sequence of keyholders in the list "
								+ selectedList.getDisplayName()
								+ " was updated", this.selectedDepartment
								.getBranch_code()));
		// org.primefaces.context.RequestContext context =
		// RequestContext.getCurrentInstance();
		// context.execute("sequenceDialog.hide()");

	}

	public void editKeyholder(Keyholder keyholder) {
		// System.out.println("keyholder id: " + keyholder.getKeyholderId());
		this.normalList.clear();
		this.holidayList.clear();

		this.selectedKeyholder = KeyholderDAO.getInstance().getKeyholderById(
				this.selectedDepartment.getBranch_code(),
				keyholder.getKeyholderId());
		if (selectedKeyholder.getEmail() != null) {
			modEmails = new ArrayList<KeyholderEmail>();
			String[] keyModEmail = selectedKeyholder.getEmail().split(",");
			for (int i = 0; i <= keyModEmail.length - 1; i++) {
				modEmails.add(new KeyholderEmail(i + 1, keyModEmail[i]));
			}
			// System.out.println("keyModEmail.length: "+keyModEmail.length);
			emailCount = keyModEmail.length;
		}
		if ("Email".equalsIgnoreCase(selectedKeyholder.getPosition())) {
			this.emailFlag = true;
		} else {
			this.emailFlag = false;
		}

		List<KeyholderList> normalKeyholderList = KeyholderDAO.getInstance()
				.getAllNormalLists(this.selectedDepartment.getDep_id());

		List<Keyholder> contactTypes = KeyholderDAO.getInstance()
				.getContactTypes(keyholder.getKeyholderId());
		for (KeyholderList keyholderList : normalKeyholderList) {
			KeyholderListEnablingDetails details = new KeyholderListEnablingDetails(
					keyholderList.getListType(), keyholderList.getDisplayName());
			details.setKeyholderListId(keyholderList.getKeyholderListId());
			for (Keyholder keyHolder4Contact : contactTypes) {
				// this check needs to be considered (should be on id)

				// for(KeyholderList list:keyHolder4Contact.getKeyholderList()){
				// System.out.println("contact list id: "+list.getKeyholderListId());
				// if(list.getKeyholderListId()==details.getKeyholderListId()){
				// details.setContactEmail(keyHolder4Contact.isContactEmail());
				// details.setContactPhone(keyHolder4Contact.isContactPhone());
				// details.setContactSMS(keyHolder4Contact.isContactSMS());
				// }
				// }

				if (keyHolder4Contact.getKeyholderListId() == details
						.getKeyholderListId()) {
					details.setContactEmail(keyHolder4Contact.isContactEmail());
					details.setContactPhone(keyHolder4Contact.isContactPhone());
					details.setContactSMS(keyHolder4Contact.isContactSMS());
				}

			}
			//
			// System.out.println("all list ids: "+keyholderList.getKeyholderListId());
			boolean enableList = false;
			for (KeyholderList selectedKeyholderLists : this.selectedKeyholder
					.getKeyholderList()) {
				// System.out.println("selected keyholder list ids: "+selectedKeyholderLists.getKeyholderListId());
				if (selectedKeyholderLists.getKeyholderListId() == keyholderList
						.getKeyholderListId())
					enableList = true;

				else
					enableList = enableList || false;

			}
			details.setEnabled(enableList);
			// if (this.selectedKeyholder.getKeyholderListNames().contains(
			// keyholderList.getDisplayName()))
			// details.setEnabled(true);
			// else
			// details.setEnabled(false);
			if (keyholderList.getDisplayName() != null) {
				switch (keyholderList.getDisplayName()) {
				case "24/7":
					details.setFullDisplayName("24Hrs 7 Days a week (24/7)");
					break;
				case "WKD":
					details.setFullDisplayName("Week Days Mon-Fri (WKD)");
					break;
				case "WKN":
					details.setFullDisplayName("Week Nights Mon-Fri (WKN)");
					break;
				case "SAD":
					details.setFullDisplayName("Saturday Days (SAD)");
					break;
				case "SAN":
					details.setFullDisplayName("Saturday Nights (SAN)");
					break;
				case "SUD":
					details.setFullDisplayName("Sunday Days (SUD)");
					break;
				case "SUN":
					details.setFullDisplayName("Sunday Nights (SUN)");
					break;
				default:
					details.setFullDisplayName(keyholderList.getDisplayName());
					break;
				}

			} else
				details.setFullDisplayName(keyholderList.getListName());
			this.normalList.add(details);
		}
		List<KeyholderList> holidayKeyholderList = KeyholderDAO.getInstance()
				.getAllHolidayLists(this.selectedDepartment.getDep_id());
		for (KeyholderList keyholderList : holidayKeyholderList) {
			KeyholderListEnablingDetails details = new KeyholderListEnablingDetails(
					keyholderList.getListType(), keyholderList.getDisplayName());
			details.setKeyholderListId(keyholderList.getKeyholderListId());
			for (Keyholder keyHolder4Contact : contactTypes) {

				if (keyHolder4Contact.getKeyholderListId() == details
						.getKeyholderListId()) {
					details.setContactEmail(keyHolder4Contact.isContactEmail());
					details.setContactPhone(keyHolder4Contact.isContactPhone());
					details.setContactSMS(keyHolder4Contact.isContactSMS());
				}
			}
			boolean enableList = false;
			for (KeyholderList selectedKeyholderLists : this.selectedKeyholder
					.getKeyholderList()) {
				// System.out.println("selected keyholder list ids: "+selectedKeyholderLists.getKeyholderListId());
				if (selectedKeyholderLists.getKeyholderListId() == keyholderList
						.getKeyholderListId())
					enableList = true;

				else
					enableList = enableList || false;

			}
			details.setEnabled(enableList);
			details.setFullDisplayName(keyholderList.getListName() + " ("
					+ keyholderList.getDisplayName() + ")");
			this.holidayList.add(details);
		}

	}

	public boolean validateEditedKeyholder() {
		FacesMessage message = null;
		for (KeyholderListEnablingDetails keyholderList : this.normalList) {
			if (keyholderList.isEnabled()) {
				if (!keyholderList.isContactSMS()
						&& !keyholderList.isContactPhone()
						&& !keyholderList.isContactEmail()) {
					message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please select contact type for the list you have enabled!",
							"Please select contact type for the list you have enabled!");
					FacesContext.getCurrentInstance().addMessage(null, message);
					RequestContext.getCurrentInstance().update("updateMsg");
					return false;
				}
			}
		}
		for (KeyholderListEnablingDetails keyholderList : this.holidayList) {
			if (keyholderList.isEnabled()) {
				if (!keyholderList.isContactSMS()
						&& !keyholderList.isContactPhone()
						&& !keyholderList.isContactEmail()) {
					message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please select contact type for the list you have enabled!",
							"Please select contact type for the list you have enabled!");
					FacesContext.getCurrentInstance().addMessage(null, message);
					RequestContext.getCurrentInstance().update("updateMsg");
					return false;
				}
			}
		}
		return true;
	}

	public void saveEditedKeyholder() {
		if (!validateEditedKeyholder()) {
			return;
		}
		// ///////editing emails and other things
		String keyEmail = "";
		for (int i = 0; i <= modEmails.size() - 1; i++) {
			if (modEmails.get(i).getEmail() != null) {
				if (modEmails.get(i).getEmail().indexOf("@") != -1) {
					keyEmail += modEmails.get(i).getEmail() + ",";
				}
			}
		}
		selectedKeyholder.setEmail(keyEmail);
		if (selectedKeyholder.getPhone() != null) {
			if (selectedKeyholder.getPhone().equals("")) {
				selectedKeyholder.setPhone(null);
			}
		}
		if (selectedKeyholder.getMobile() != null) {
			if (selectedKeyholder.getMobile().equals("")) {
				selectedKeyholder.setMobile(null);
			}
		}
		if (selectedKeyholder.getEmail() != null) {
			if (selectedKeyholder.getEmail().indexOf("@") == -1) {
				selectedKeyholder.setEmail(null);
			}
		}
		if (selectedKeyholder.getMobile() != null) {
			selectedKeyholder.setMobile(selectedKeyholder.getMobile()
					.replaceAll("\\s+", ""));// remove all the gaps in the
												// mobile number
		}

		if (selectedKeyholder.getPhone() != null) {
			selectedKeyholder.setPhone(selectedKeyholder.getPhone().replaceAll(
					"\\s+", ""));// remove all the gaps in the phone number
		}
		// System.out.println("saving details");
		KeyholderDAO.getInstance().updateSiteKeyholder(this.selectedKeyholder);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		// UserAuditDAO.getInstance().insertUserAudit(
		// new UserAudit(Integer.parseInt(session.getAttribute("userId")
		// .toString()), new Timestamp(Calendar.getInstance()
		// .getTime().getTime()), "SiteKeyholderModified",
		// "The site keyholder "
		// + this.selectedKeyholder.getFullName()
		// + " was modified", this.selectedDepartment
		// .getBranch_code()));
		List<String> auditListNames = new ArrayList<String>();
		for (KeyholderListEnablingDetails keyholderList : this.normalList) {
			if (keyholderList.isEnabled()) {
				KeyholderDAO.getInstance().saveKeyholderList_Keyholder(
						this.selectedKeyholder, keyholderList);
				auditListNames.add(keyholderList.getDisplayName());
			} else {
				KeyholderDAO.getInstance().deleteKeyholderList_Keyholder(
						this.selectedKeyholder, keyholderList);
			}
			// UserAuditDAO.getInstance().insertUserAudit(
			// new UserAudit(Integer.parseInt(session.getAttribute(
			// "userId").toString()), new Timestamp(Calendar
			// .getInstance().getTime().getTime()),
			// "KeyholdersListModified", "The keyholders list "
			// + keyholderList.getDisplayName()
			// + " was modified", this.selectedDepartment
			// .getBranch_code()));
		}

		for (KeyholderListEnablingDetails keyholderList : this.holidayList) {
			if (keyholderList.isEnabled()) {
				// System.out.println("list name: "+keyholderList.getDisplayName());
				KeyholderDAO.getInstance().saveKeyholderList_Keyholder(
						this.selectedKeyholder, keyholderList);
				auditListNames.add(keyholderList.getDisplayName());

			} else {
				KeyholderDAO.getInstance().deleteKeyholderList_Keyholder(
						this.selectedKeyholder, keyholderList);
				// UserAuditDAO.getInstance().insertUserAudit(new
				// UserAudit(Integer.parseInt(session.getAttribute("userId").toString()),new
				// Timestamp(Calendar.getInstance().getTime().getTime()),"KeyholdersListModified",
				// "The keyholders list "+ keyholderList.getDisplayName() +
				// " was modified",this.selectedDepartment.getBranch_code()));
			}

		}
		String auditString = "";
		if (auditListNames.size() > 1) {
			auditString = "The keyholder lists " + auditListNames
					+ " were modified";
			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"KeyholdersListModified", "The site keyholder "
									+ this.selectedKeyholder.getFullName()
									+ " was modified: " + auditString,
							this.selectedDepartment.getBranch_code()));
		} else if (auditListNames.size() == 1) {
			auditString = "The keyholder list " + auditListNames
					+ " was modified";
			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"KeyholdersListModified", "The site keyholder "
									+ this.selectedKeyholder.getFullName()
									+ " was modified: " + auditString,
							this.selectedDepartment.getBranch_code()));
		}

		this.modEmails = null;
		initializeKeyholders();
		RequestContext.getCurrentInstance().update("frmKeyholdersPage");
		RequestContext.getCurrentInstance().execute(
				"PF('editKeyholderDlg').hide();");
	}

	public void initializeNewKeyholder() {
		this.newKeyholder = new Keyholder();
		this.emailFlag = false;
		this.newNormalList.clear();
		this.newHolidayList.clear();
		List<KeyholderList> normalKeyholderList = KeyholderDAO.getInstance()
				.getAllNormalLists(this.selectedDepartment.getDep_id());

		for (KeyholderList keyholderList : normalKeyholderList) {
			KeyholderListEnablingDetails details = new KeyholderListEnablingDetails(
					keyholderList.getListType(), keyholderList.getDisplayName());
			details.setKeyholderListId(keyholderList.getKeyholderListId());
			details.setContactEmail(false);
			details.setContactPhone(false);
			details.setContactSMS(false);
			details.setEnabled(false);

			if (keyholderList.getDisplayName() != null) {
				switch (keyholderList.getDisplayName()) {
				case "24/7":
					details.setFullDisplayName("24Hrs 7 Days a week (24/7)");
					break;
				case "WKD":
					details.setFullDisplayName("Week Days Mon-Fri (WKD)");
					break;
				case "WKN":
					details.setFullDisplayName("Week Nights Mon-Fri (WKN)");
					break;
				case "SAD":
					details.setFullDisplayName("Saturday Days (SAD)");
					break;
				case "SAN":
					details.setFullDisplayName("Saturday Nights (SAN)");
					break;
				case "SUD":
					details.setFullDisplayName("Sunday Days (SUD)");
					break;
				case "SUN":
					details.setFullDisplayName("Sunday Nights (SUN)");
					break;
				default:
					details.setFullDisplayName(keyholderList.getDisplayName());
					break;
				}

			} else
				details.setFullDisplayName(keyholderList.getListName());
			this.newNormalList.add(details);
		}

		List<KeyholderList> holidayKeyholderList = KeyholderDAO.getInstance()
				.getAllHolidayLists(this.selectedDepartment.getDep_id());
		for (KeyholderList keyholderList : holidayKeyholderList) {
			KeyholderListEnablingDetails details = new KeyholderListEnablingDetails(
					keyholderList.getListType(), keyholderList.getDisplayName());
			details.setKeyholderListId(keyholderList.getKeyholderListId());
			details.setContactEmail(false);
			details.setContactPhone(false);
			details.setContactSMS(false);
			details.setEnabled(false);
			details.setFullDisplayName(keyholderList.getListName() + " ("
					+ keyholderList.getDisplayName() + ")");
			this.newHolidayList.add(details);
		}
		// System.out.println("initialization of new keyholder completed");

	}

	public boolean validateNewKeyholder() {
		FacesMessage message = null;
		for (KeyholderListEnablingDetails keyholderList : this.newNormalList) {
			if (keyholderList.isEnabled()) {
				if (!keyholderList.isContactSMS()
						&& !keyholderList.isContactPhone()
						&& !keyholderList.isContactEmail()) {
					message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please select contact type for the list you have enabled!",
							"Please select contact type for the list you have enabled!");
					FacesContext.getCurrentInstance().addMessage(null, message);
					RequestContext.getCurrentInstance().update("saveMsg");
					return false;
				}
			}
		}
		for (KeyholderListEnablingDetails keyholderList : this.newHolidayList) {
			if (keyholderList.isEnabled()) {
				if (!keyholderList.isContactSMS()
						&& !keyholderList.isContactPhone()
						&& !keyholderList.isContactEmail()) {
					message = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Please select contact type for the list you have enabled!",
							"Please select contact type for the list you have enabled!");
					FacesContext.getCurrentInstance().addMessage(null, message);
					RequestContext.getCurrentInstance().update("saveMsg");
					return false;
				}
			}
		}
		return true;
	}

	public void saveNewKeyholder() {
		if (!validateNewKeyholder()) {
			return;
		}
		String keyEmail = "";
		for (int i = 0; i <= emails.size() - 1; i++) {
			if (emails.get(i).getEmail() != null) {
				if (emails.get(i).getEmail().indexOf("@") != -1) {
					keyEmail += emails.get(i).getEmail() + ",";
				}
			}
		}
		newKeyholder.setEmail(keyEmail);
		if (newKeyholder.getPhone().equals("")) {
			newKeyholder.setPhone(null);
		}
		if (newKeyholder.getMobile().equals("")) {
			newKeyholder.setMobile(null);
		}
		if (newKeyholder.getEmail().indexOf("@") == -1) {
			newKeyholder.setEmail(null);
		}
		if (newKeyholder.getMobile() != null) {
			newKeyholder.setMobile(newKeyholder.getMobile().replaceAll("\\s+",
					"")); // remove all the gaps in the mobile number
		}

		if (newKeyholder.getPhone() != null) {
			newKeyholder.setPhone(newKeyholder.getPhone()
					.replaceAll("\\s+", "")); // remove all the gaps in the
												// phone number
		}
		int keyholderId = KeyholderDAO.getInstance().addBranchKeyholder(
				newKeyholder.getContactName(), newKeyholder.getPosition(),
				true, newKeyholder.getPhone(), newKeyholder.getMobile(), "",
				newKeyholder.getEmail(),
				this.selectedDepartment.getBranch_code());
		newKeyholder.setKeyholderId(keyholderId);
		// System.out.println("saved keyholder");
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		// UserAuditDAO.getInstance().insertUserAudit(
		// new UserAudit(Integer.parseInt(session.getAttribute("userId")
		// .toString()), new Timestamp(Calendar.getInstance()
		// .getTime().getTime()), "SiteKeyholderAdded",
		// "A new site keyholder " + newKeyholder.getFullName()
		// + " was added", this.selectedDepartment
		// .getBranch_code()));
		// List<String> auditListNames=new ArrayList<String>();
		StringBuilder auditString = new StringBuilder("");
		for (KeyholderListEnablingDetails keyholderList : this.newNormalList) {
			if (keyholderList.isEnabled()) {
				KeyholderDAO.getInstance().saveKeyholderList_Keyholder(
						this.newKeyholder, keyholderList);
				auditString.append("The keyholders list "
						+ keyholderList.getDisplayName() + " was modified, ");
			} else {
				KeyholderDAO.getInstance().deleteKeyholderList_Keyholder(
						this.newKeyholder, keyholderList);
				// auditString.append("The keyholders list "+keyholderList.getDisplayName()+" was modified, ");
			}

		}

		// if (auditString.length()> 1) {
		// UserAuditDAO.getInstance().insertUserAudit(
		// new UserAudit(Integer.parseInt(session.getAttribute(
		// "userId").toString()), new Timestamp(Calendar
		// .getInstance().getTime().getTime()),
		// "KeyholdersListModified", auditString.toString(),
		// this.selectedDepartment
		// .getBranch_code()));
		// }
		// auditString = new StringBuilder("");
		for (KeyholderListEnablingDetails keyholderList : this.newHolidayList) {
			if (keyholderList.isEnabled()) {
				KeyholderDAO.getInstance().saveKeyholderList_Keyholder(
						this.newKeyholder, keyholderList);
				auditString.append("The keyholders list "
						+ keyholderList.getDisplayName() + " was modified, ");
			} else {
				KeyholderDAO.getInstance().deleteKeyholderList_Keyholder(
						this.newKeyholder, keyholderList);
				// auditString.append("The keyholders list "+keyholderList.getDisplayName()+" was modified, ");
			}

			// UserAuditDAO.getInstance().insertUserAudit(
			// new UserAudit(Integer.parseInt(session.getAttribute(
			// "userId").toString()), new Timestamp(Calendar
			// .getInstance().getTime().getTime()),
			// "KeyholdersListModified", "The keyholders list "
			// + keyholderList.getDisplayName()
			// + " was modified", this.selectedDepartment
			// .getBranch_code()));
		}
		// UserAuditDAO.getInstance().insertUserAudit(
		// new UserAudit(Integer.parseInt(session.getAttribute("userId")
		// .toString()), new Timestamp(Calendar.getInstance()
		// .getTime().getTime()), "SiteKeyholderAdded",
		// "A new site keyholder " + newKeyholder.getFullName()
		// + " was added", this.selectedDepartment
		// .getBranch_code()));
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "KeyholdersListModified",
						"A new site keyholder " + newKeyholder.getFullName()
								+ " was added: " + auditString.toString(),
						this.selectedDepartment.getBranch_code()));

		this.selectedKeyholder = null;
		this.newKeyholder = new Keyholder();
		this.emails = null;
		initializeKeyholders();
		RequestContext.getCurrentInstance().update("frmKeyholdersPage");
		RequestContext.getCurrentInstance().execute(
				"PF('addKeyholderDlg').hide();");
	}

	public void deleteKeyholder() {
		Keyholder keyholderToDel = this.selectedKeyholder;
		KeyholderDAO.getInstance().deleteBranchKeyholder(
				keyholderToDel.getKeyholderId());

		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "SiteKeyholderDeleted",
						"The site keyholder with id "
								+ keyholderToDel.getKeyholderId()
								+ " was deleted", this.selectedDepartment
								.getBranch_code()));
		// FacesMessage message = new FacesMessage();
		// message.setDetail("The selected keyholder " +
		// keyholderToDel.getFullName() + " was deleted");
		// message.setSummary("The selected keyholder " +
		// keyholderToDel.getFullName() + " was deleted");
		// message.setSeverity(FacesMessage.SEVERITY_INFO);
		// FacesContext.getCurrentInstance().addMessage(null,message);
		initializeKeyholders();
		this.selectedKeyholder = null;
		RequestContext.getCurrentInstance().update("frmKeyholdersPage");

	}

	public void editContactType(Keyholder keyholder, int listId) {
		this.selectedKeyholder = keyholder;
		this.selectedListId = listId;
		ContactTypeDetails details = keyholder.getIdWithContactType().get(
				listId);
		switch (details.getContactType()) {
		case 1:
			this.setSelectedVoiceOnly(true);
			this.setSelectedSMSOnly(false);
			this.setSelectedEmailOnly(false);
			break;
		case 2:
			this.setSelectedVoiceOnly(true);
			this.setSelectedSMSOnly(true);
			this.setSelectedEmailOnly(false);
			break;
		case 3:
			this.setSelectedVoiceOnly(false);
			this.setSelectedSMSOnly(true);
			this.setSelectedEmailOnly(false);
			break;
		case 4:
			this.setSelectedVoiceOnly(false);
			this.setSelectedSMSOnly(true);
			this.setSelectedEmailOnly(true);
			break;
		case 5:
			this.setSelectedVoiceOnly(false);
			this.setSelectedSMSOnly(false);
			this.setSelectedEmailOnly(true);
			break;
		case 6:
			this.setSelectedVoiceOnly(true);
			this.setSelectedSMSOnly(false);
			this.setSelectedEmailOnly(true);
			break;
		case 7:
			this.setSelectedVoiceOnly(true);
			this.setSelectedSMSOnly(true);
			this.setSelectedEmailOnly(true);
			break;
		default:
			this.setSelectedVoiceOnly(false);
			this.setSelectedSMSOnly(false);
			this.setSelectedEmailOnly(false);
			break;
		}
		// System.out.println("type: " + details.getContactType());
	}

	public void updateEditedContactType() {
		KeyholderListEnablingDetails details = new KeyholderListEnablingDetails();
		details.setKeyholderListId(this.selectedListId);
		details.setContactEmail(this.selectedEmailOnly);
		details.setContactPhone(this.selectedVoiceOnly);
		details.setContactSMS(this.selectedSMSOnly);
		KeyholderDAO.getInstance().saveKeyholderList_Keyholder(
				this.selectedKeyholder, details);
		// System.out.println("type: " + details.getContact_type());

		initializeKeyholders();
		this.selectedKeyholder = null;
		RequestContext.getCurrentInstance().update("frmKeyholdersPage");
	}

	public List<Keyholder> getKeyHoldersList() {
		return keyHoldersList;
	}

	public void setKeyHoldersList(List<Keyholder> keyHoldersList) {
		this.keyHoldersList = keyHoldersList;
	}

	public KeyholderList getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(KeyholderList selectedList) {
		this.selectedList = selectedList;
	}

	public boolean isShowNormal() {
		return showNormal;
	}

	public void setShowNormal(boolean showNormal) {
		this.showNormal = showNormal;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public List<KeyholderList> getListTypes() {
		return listTypes;
	}

	public void setListTypes(List<KeyholderList> listTypes) {
		this.listTypes = listTypes;
	}

	public List<Keyholder> getListKeyHolders() {
		return listKeyHolders;
	}

	public void setListKeyHolders(List<Keyholder> listKeyHolders) {
		this.listKeyHolders = listKeyHolders;
	}

	public int getListKeyHoldersSize() {
		return listKeyHoldersSize;
	}

	public void setListKeyHoldersSize(int listKeyHoldersSize) {
		this.listKeyHoldersSize = listKeyHoldersSize;
	}

	public Keyholder getSelectedKeyholder() {
		return selectedKeyholder;
	}

	public void setSelectedKeyholder(Keyholder selectedKeyholder) {
		this.selectedKeyholder = selectedKeyholder;
	}

	// public KeyholderList getChosenList() {
	// return chosenList;
	// }
	//
	// public void setChosenList(KeyholderList chosenList) {
	// this.chosenList = chosenList;
	// }

	public List<KeyholderListEnablingDetails> getNormalList() {
		return normalList;
	}

	public void setNormalList(List<KeyholderListEnablingDetails> normalList) {
		this.normalList = normalList;
	}

	public List<KeyholderListEnablingDetails> getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(List<KeyholderListEnablingDetails> holidayList) {
		this.holidayList = holidayList;
	}

	public Keyholder getNewKeyholder() {
		return newKeyholder;
	}

	public void setNewKeyholder(Keyholder newKeyholder) {
		this.newKeyholder = newKeyholder;
	}

	public List<KeyholderListEnablingDetails> getNewNormalList() {
		return newNormalList;
	}

	public void setNewNormalList(
			List<KeyholderListEnablingDetails> newNormalList) {
		this.newNormalList = newNormalList;
	}

	public List<KeyholderListEnablingDetails> getNewHolidayList() {
		return newHolidayList;
	}

	public void setNewHolidayList(
			List<KeyholderListEnablingDetails> newHolidayList) {
		this.newHolidayList = newHolidayList;
	}

	public void setContactType() {
		this.contactType = new ArrayList<String>();
		String contactTypes = ServiceProperties.getInstance().getContactTypes();
		String[] separatedTypes = contactTypes.split(",");
		for (int i = 0; i <= separatedTypes.length - 1; i++) {
			this.contactType.add(separatedTypes[i]);
		}
	}

	public List<String> getContactType() {
		setContactType();
		return contactType;
	}

	public void setContactTypes() {
		this.contactTypes = new ArrayList<SelectItem>();
		for (int i = 0; i <= getContactType().size() - 1; i++) {
			this.contactTypes.add(new SelectItem(contactType.get(i),
					contactType.get(i)));
		}
	}

	public List<SelectItem> getContactTypes() {
		setContactTypes();
		return contactTypes;
	}

	public void filterTypes() {
		if ("Email".equalsIgnoreCase(this.newKeyholder.getPosition())) {
			this.newKeyholder.setPhone("");
			this.newKeyholder.setMobile("");
			this.emailFlag = true;
		} else {
			this.emailFlag = false;
		}
	}

	public void filterModTypes() {
		if ("Email".equalsIgnoreCase(this.selectedKeyholder.getPosition())) {
			this.selectedKeyholder.setPhone("");
			this.selectedKeyholder.setMobile("");
			this.emailFlag = true;
		} else {
			this.emailFlag = false;
		}
	}

	/**
	 * add a new email address for the new keyholder
	 * 
	 * @param e
	 */
	public void addEmail(ActionEvent e) {
		if (emailCount < 5) {
			emailCount++;
			emails.add(new KeyholderEmail(emailCount, null));
		}
	}

	/**
	 * add a new email address for the selected modified keyholder
	 * 
	 * @param e
	 */
	public void addModEmail(ActionEvent e) {
		if (emailCount < 5) {
			emailCount++;
			modEmails.add(new KeyholderEmail(emailCount, null));
		}
	}

	public void setEmails() {
		if (emails == null) {
			emails = new ArrayList<KeyholderEmail>();
			emails.add(new KeyholderEmail(1, null));
			emailCount = 1;
		}
	}

	public void setModEmails() {
		if (modEmails == null) {
			modEmails = new ArrayList<KeyholderEmail>();
			modEmails.add(new KeyholderEmail(1, null));
			emailCount = 1;
		}
	}

	/**
	 * cancel the addittion of a new site keyholder
	 */
	public void addCancel(ActionEvent e) {
		emails = null;
		emailCount = 1;
	}

	/**
	 * cancel the modification of the selected site keyholder
	 */
	public void modCancel(ActionEvent e) {
		modEmails = null;
		emailCount = 1;
		selectedKeyholder = null;
	}

	public boolean isEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(boolean emailFlag) {
		this.emailFlag = emailFlag;
	}

	public void setContactType(List<String> contactType) {
		this.contactType = contactType;
	}

	public void setContactTypes(List<SelectItem> contactTypes) {
		this.contactTypes = contactTypes;
	}

	public List<KeyholderEmail> getEmails() {
		setEmails();
		return emails;
	}

	public void setEmails(List<KeyholderEmail> emails) {
		this.emails = emails;
	}

	public List<KeyholderEmail> getModEmails() {
		setModEmails();
		return modEmails;
	}

	public void setModEmails(List<KeyholderEmail> modEmails) {
		this.modEmails = modEmails;
	}

	public int getEmailCount() {
		return emailCount;
	}

	public void setEmailCount(int emailCount) {
		this.emailCount = emailCount;
	}

	public boolean isSelectedVoiceOnly() {
		return selectedVoiceOnly;
	}

	public void setSelectedVoiceOnly(boolean selectedVoiceOnly) {
		this.selectedVoiceOnly = selectedVoiceOnly;
	}

	public boolean isSelectedSMSOnly() {
		return selectedSMSOnly;
	}

	public void setSelectedSMSOnly(boolean selectedSMSOnly) {
		this.selectedSMSOnly = selectedSMSOnly;
	}

	public boolean isSelectedEmailOnly() {
		return selectedEmailOnly;
	}

	public void setSelectedEmailOnly(boolean selectedEmailOnly) {
		this.selectedEmailOnly = selectedEmailOnly;
	}

	public int getSelectedListId() {
		return selectedListId;
	}

	public void setSelectedListId(int selectedListId) {
		this.selectedListId = selectedListId;
	}

}

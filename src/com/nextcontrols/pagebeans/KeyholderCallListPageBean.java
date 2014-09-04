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
import org.primefaces.event.CloseEvent;

import com.nextcontrols.dao.KeyholderDAO;
import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.domain.Department;
import com.nextcontrols.domain.Keyholder;
import com.nextcontrols.domain.KeyholderList;
import com.nextcontrols.domain.UserAudit;

@ManagedBean(name = "keyholdercalllist")
@SessionScoped
public class KeyholderCallListPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean showNormal;
	private List<KeyholderList> departmentKeyholdersLists;
	private Department selectedDepartment;
	private KeyholderList newCallList;
	private String startHour;
	private String startMinutes;
	private String endHour;
	private String endMinutes;
	private List<SelectItem> hours;
	private List<SelectItem> minutes;
	private List<SelectItem> endHours;
	private List<SelectItem> listType;
	private int typeNumb;
	private KeyholderList selectedCallList;
	private List<Keyholder> listKeyHolders;
	private int listKeyHoldersSize;

	public KeyholderCallListPageBean() {
		this.showNormal = true;
		this.departmentKeyholdersLists = new ArrayList<KeyholderList>();
		this.listKeyHolders = new ArrayList<Keyholder>();
		hours = new ArrayList<SelectItem>();
		minutes = new ArrayList<SelectItem>();
		endHours = new ArrayList<SelectItem>();
		listType = new ArrayList<SelectItem>();
		typeNumb = 0;
		this.listKeyHoldersSize = 0;
	}

	public void initializeKeyholdersLists(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
		this.departmentKeyholdersLists.clear();
		this.showNormal = true;
		initializeKeyholdersLists();
	}

	public void initializeKeyholdersLists() {
		if (this.showNormal) {
			departmentKeyholdersLists = KeyholderDAO.getInstance()
					.retrieveDepartmentKeyholderListNormal(
							this.selectedDepartment.getDep_id());
		} else {
			departmentKeyholdersLists = KeyholderDAO.getInstance()
					.retrieveDepartmentKeyholderListHoliday(
							this.selectedDepartment.getDep_id());
		}

	}

	public String showKeyholders(boolean showNormal) {
		// System.out.println("show normal: "+showNormal);
		this.showNormal = showNormal;
		initializeKeyholdersLists();
		RequestContext.getCurrentInstance().update("frmKeyholderCallListPage");
		return "KeyholderCallListPage.xhtml?faces-redirect=true";

	}

	public void initializeNewCallList() {
		this.newCallList = new KeyholderList();
		this.startHour = "";
		this.startMinutes = "";
		this.endHour = "";
		this.endMinutes = "";
		// if(!this.showNormal)
		// this.newCallList.setListType("Special Occasion");
	}

	public void saveNewCallList() {
		// System.out.println("list type selectd: "
		// + this.newCallList.getListType());
		this.newCallList.setListName(this.newCallList.getDisplayName());
		if (typeNumb == 1 || typeNumb == 3) {// special occasion or custom list
												// type
			KeyholderDAO.getInstance().addDeptKeyholderList(
					this.newCallList.getDisplayName(),
					this.newCallList.getDescription(),
					this.newCallList.getListName(),
					this.newCallList.getListType(),
					this.newCallList.getStartDate(),
					this.newCallList.getEndDate(),
					this.startHour + ":" + this.startMinutes,
					this.endHour + ":" + this.endMinutes,
					this.newCallList.getComments(),
					this.selectedDepartment.getDep_id(),
					this.newCallList.getWeekdaysActive());
		} else {
			KeyholderDAO.getInstance().addDeptKeyholderList(
					this.newCallList.getDisplayName(),
					this.newCallList.getDescription(),
					this.newCallList.getListName(),
					this.newCallList.getListType(), null, null,
					this.startHour + ":" + this.startMinutes,
					this.endHour + ":" + this.endMinutes,
					this.newCallList.getComments(),
					this.selectedDepartment.getDep_id(),
					this.newCallList.getWeekdaysActive());
		}

		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "KeyholdersListAdded",
						"A keyholder list " + this.newCallList.getListName()
								+ " was added", this.selectedDepartment
								.getBranch_code()));
		this.newCallList = new KeyholderList();
		typeNumb = 0;
		initializeKeyholdersLists();
		RequestContext.getCurrentInstance().update("frmKeyholderCallListPage");
	}

	public void deleteCallList() {
		KeyholderList callList = this.selectedCallList;
		if (callList != null) {
			if (callList.getListType().equals("Default") == false) {
				KeyholderDAO.getInstance().delDeptKeyholderList(
						callList.getKeyholderListId());
				ExternalContext ectx = FacesContext.getCurrentInstance()
						.getExternalContext();
				HttpSession session = (HttpSession) ectx.getSession(false);
				UserAuditDAO.getInstance().insertUserAudit(
						new UserAudit(Integer.parseInt(session.getAttribute(
								"userId").toString()), new Timestamp(Calendar
								.getInstance().getTime().getTime()),
								"KeyholdersListDeleted", "The keyholders list "
										+ callList.getDisplayName()
										+ " was deleted",
								this.selectedDepartment.getBranch_code()));
				// FacesMessage message = new FacesMessage();
				// message.setDetail("The selected call list "
				// + callList.getDisplayName() + " was deleted");
				// message.setSummary("The selected call list "
				// + callList.getDisplayName() + " was deleted");
				// message.setSeverity(FacesMessage.SEVERITY_INFO);
				// FacesContext.getCurrentInstance().addMessage(null, message);
				this.selectedCallList = null;
				initializeKeyholdersLists();
				RequestContext.getCurrentInstance().update(
						"frmKeyholderCallListPage");

			} else {
				FacesMessage message = new FacesMessage();
				message.setDetail("The Default KeyholderList cannot be deleted");
				message.setSummary("The Default KeyholderList cannot be deleted");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update("varMsg");
				// RequestContext.getCurrentInstance().update(
				// "frmKeyholderCallListPage");
			}
		}

	}

	public void editCallListPreProcessor(KeyholderList keyholderList) {
		this.selectedCallList = keyholderList;
		if (this.selectedCallList != null) {
			String[] listStart = selectedCallList.getOccupancyStart()
					.split(":");
			startHour = listStart[0];
			startMinutes = listStart[1];

			String[] listEnd = selectedCallList.getOccupancyEnd().split(":");
			endHour = listEnd[0];
			endMinutes = listEnd[1];

			if (this.selectedCallList.getListType().equals("Special Occasion")) {
				typeNumb = 1;
			} else if ((this.selectedCallList.getListType()
					.equals("Weekday Occupancy"))
					|| (this.selectedCallList.getListType()
							.equals("Weekday OutOfHours"))) {
				typeNumb = 2;
			} else if (this.selectedCallList.getListType().equals("Default")
					|| this.selectedCallList.getListType().equals(
							"Saturday Occupancy")
					|| this.selectedCallList.getListType().equals(
							"Saturday OutOfHours")
					|| this.selectedCallList.getListType().equals(
							"Sunday Occupancy")
					|| this.selectedCallList.getListType().equals(
							"Sunday OutOfHours")
					|| this.selectedCallList.getListType().equals("Christmas")
					|| this.selectedCallList.getListType().equals(
							"New Year's Day")
					|| this.selectedCallList.getListType().equals("Boxing Day")
					|| this.selectedCallList.getListType().equals(
							"Waitrose Special Occasion")) {
				typeNumb = 0;
			} else {
				typeNumb = 3;
			}
			System.out.println("typeNumb: " + typeNumb);

		}

	}

	public void editCallList() {
		if (selectedCallList != null) {
			if (typeNumb == 1 || typeNumb == 3) {// special occasion or custom
													// list type
				KeyholderDAO.getInstance().modifyKeyholderList(
						selectedCallList.getListName(),
						selectedCallList.getListType(),
						selectedCallList.getStartDate(),
						selectedCallList.getEndDate(),
						startHour + ":" + startMinutes,
						endHour + ":" + endMinutes,
						selectedCallList.getComments(),
						selectedCallList.getKeyholderListId(),
						selectedCallList.getWeekdaysActive(),
						selectedCallList.getDescription(),
						selectedCallList.getDisplayName());
			} else {
				KeyholderDAO.getInstance().modifyKeyholderList(
						selectedCallList.getListName(),
						selectedCallList.getListType(), null, null,
						startHour + ":" + startMinutes,
						endHour + ":" + endMinutes,
						selectedCallList.getComments(),
						selectedCallList.getKeyholderListId(),
						selectedCallList.getWeekdaysActive(),
						selectedCallList.getDescription(),
						selectedCallList.getDisplayName());
			}
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"KeyholdersListModified", "The keyholders list "
									+ this.selectedCallList.getDisplayName()
									+ " was modified", this.selectedDepartment
									.getBranch_code()));
			selectedCallList = new KeyholderList();
			typeNumb = 0;
		}
		initializeKeyholdersLists();
		RequestContext.getCurrentInstance().update("frmKeyholderCallListPage");
	}

	public void retrieveListKeyholders(KeyholderList selectedList) {
		this.selectedCallList = selectedList;
		this.listKeyHolders.clear();
		// System.out.println("selected list : " +
		// selectedList.getDisplayName());
		// this.chosenList = KeyholderDAO.getInstance().getKeyholdersList(
		// this.selectedDepartment.getBranch_code(),
		// selectedList.getKeyholderListId());
		// System.out.println("selected dept:"+this.selectedDepartment.getBranch_code());
		// System.out.println("selected list:"+selectedList.getKeyholderListId());
		this.listKeyHolders = KeyholderDAO.getInstance().getListOfKeyholders(
				this.selectedDepartment.getBranch_code(),
				selectedList.getKeyholderListId());
		this.listKeyHoldersSize = this.listKeyHolders.size();

	}

	public void saveKeyHoldersSequence() {
		// System.out.println("save keyholder sequence called");
		for (int i = 0; i < listKeyHolders.size(); i++) {
			// System.out.println("name: "+listKeyHolders.get(i).getContactName());
			listKeyHolders.get(i).setPriority(i + 1);

		}
		KeyholderDAO.getInstance().updateKeyholdersList(listKeyHolders,
				this.selectedCallList.getKeyholderListId());
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "KeyholdersInListUpdated",
						"The sequence of keyholders in the list "
								+ this.selectedCallList.getDisplayName()
								+ " was updated", this.selectedDepartment
								.getBranch_code()));
		// org.primefaces.context.RequestContext context =
		// RequestContext.getCurrentInstance();
		// context.execute("sequenceDialog.hide()");

	}

	public KeyholderList getSelectedCallList() {
		return selectedCallList;
	}

	public void setSelectedCallList(KeyholderList selectedCallList) {
		this.selectedCallList = selectedCallList;
	}

	public List<KeyholderList> getDepartmentKeyholdersLists() {
		return departmentKeyholdersLists;
	}

	public void setDepartmentKeyholdersLists(
			List<KeyholderList> departmentKeyholdersLists) {
		this.departmentKeyholdersLists = departmentKeyholdersLists;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public boolean isShowNormal() {
		return showNormal;
	}

	public void setShowNormal(boolean showNormal) {
		this.showNormal = showNormal;
	}

	public KeyholderList getNewCallList() {
		return newCallList;
	}

	public void setNewCallList(KeyholderList newCallList) {
		this.newCallList = newCallList;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getStartMinutes() {
		return startMinutes;
	}

	public void setStartMinutes(String startMinutes) {
		this.startMinutes = startMinutes;
	}

	public String getEndHour() {
		return endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}

	public String getEndMinutes() {
		return endMinutes;
	}

	public void setEndMinutes(String endMinutes) {
		this.endMinutes = endMinutes;
	}

	public void setListType() {
		this.listType = new ArrayList<SelectItem>();
		listType.add(new SelectItem("Default", "Default"));
		listType.add(new SelectItem("Weekday Occupancy", "Weekday Occupancy"));
		listType.add(new SelectItem("Weekday OutOfHours", "Weekday OutOfHours"));
		listType.add(new SelectItem("Weekend Occupancy", "Weekend Occupancy"));
		listType.add(new SelectItem("Weekend OutOfHours", "Weekend OutOfHours"));
		listType.add(new SelectItem("Christmas", "Christmas"));
		listType.add(new SelectItem("Boxing Day", "Boxing Day"));
		listType.add(new SelectItem("New Years Day", "New Years Day"));
		listType.add(new SelectItem("Special Occasion", "Special Occasion"));
	}

	public List<SelectItem> getListType() {
		setListType();
		return listType;
	}

	public void setHours() {
		hours = new ArrayList<SelectItem>();
		for (int i = 0; i <= 23; i++) {
			if (i < 10) {
				hours.add(new SelectItem("0" + Integer.toString(i), "0"
						+ Integer.toString(i)));
			} else {
				hours.add(new SelectItem(Integer.toString(i), Integer
						.toString(i)));
			}
		}
	}

	public List<SelectItem> getHours() {
		setHours();
		return hours;
	}

	public void setMinutes() {
		minutes = new ArrayList<SelectItem>();
		for (int i = 0; i <= 59; i++) {
			if (i < 10) {
				minutes.add(new SelectItem("0" + Integer.toString(i), "0"
						+ Integer.toString(i)));
			} else {
				minutes.add(new SelectItem(Integer.toString(i), Integer
						.toString(i)));
			}
		}
	}

	public List<SelectItem> getMinutes() {
		setMinutes();
		return minutes;
	}

	public void setEndHours() {
		endHours = new ArrayList<SelectItem>();
		for (int i = 1; i <= 23; i++) {
			if (i < 10) {
				endHours.add(new SelectItem("0" + Integer.toString(i), "0"
						+ Integer.toString(i)));
			} else {
				endHours.add(new SelectItem(Integer.toString(i), Integer
						.toString(i)));
			}
		}
	}

	public List<SelectItem> getEndHours() {
		setEndHours();
		return endHours;
	}

	public int getTypeNumb() {
		return typeNumb;
	}

	public void setTypeNumb(int typeNumb) {
		this.typeNumb = typeNumb;
	}

	public void closeListener(CloseEvent e) {
		this.newCallList = new KeyholderList();
		this.selectedCallList = new KeyholderList();
		typeNumb = 0;
	}

	/**
	 * sets the list type according to the current user input whilst adding a
	 * list
	 * 
	 * @param e
	 */
	public void setType() {
		if (this.newCallList.getListType().equals("Special Occasion")) {
			typeNumb = 1;
		} else if ((this.newCallList.getListType().equals("Weekday Occupancy"))
				|| (this.newCallList.getListType().equals("Weekday OutOfHours"))) {
			typeNumb = 2;
		} else if (this.newCallList.getListType().equals("Default")
				|| this.newCallList.getListType().equals("Saturday Occupancy")
				|| this.newCallList.getListType().equals("Saturday OutOfHours")
				|| this.newCallList.getListType().equals("Sunday Occupancy")
				|| this.newCallList.getListType().equals("Sunday OutOfHours")
				|| this.newCallList.getListType().equals("Christmas")
				|| this.newCallList.getListType().equals("New Year's Day")
				|| this.newCallList.getListType().equals("Boxing Day")
				|| this.newCallList.getListType().equals(
						"Waitrose Special Occasion")) {
			typeNumb = 0;
		} else {
			typeNumb = 3;
		}
	}

	/**
	 * sets the list type according to the current user input whilst modifying a
	 * list
	 * 
	 * @param e
	 */
	public void setModType(ActionEvent e) {
		if (this.selectedCallList.getListType().equals("Special Occasion")) {
			typeNumb = 1;
		} else if ((this.selectedCallList.getListType()
				.equals("Weekday Occupancy"))
				|| (this.selectedCallList.getListType()
						.equals("Weekday OutOfHours"))) {
			typeNumb = 2;
		} else if (this.newCallList.getListType().equals("Default")
				|| this.newCallList.getListType().equals("Saturday Occupancy")
				|| this.newCallList.getListType().equals("Saturday OutOfHours")
				|| this.newCallList.getListType().equals("Sunday Occupancy")
				|| this.newCallList.getListType().equals("Sunday OutOfHours")
				|| this.newCallList.getListType().equals("Christmas")
				|| this.newCallList.getListType().equals("New Year's Day")
				|| this.newCallList.getListType().equals("Boxing Day")
				|| this.newCallList.getListType().equals(
						"Waitrose Special Occasion")) {
			typeNumb = 0;
		} else {
			typeNumb = 3;
		}
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

}

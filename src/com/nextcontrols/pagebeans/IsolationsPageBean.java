package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import com.nextcontrols.dao.AlarmTrapDAO;
import com.nextcontrols.dao.BranchDAO;
import com.nextcontrols.dao.CustomerDAO;
import com.nextcontrols.dao.DepartmentDAO;
import com.nextcontrols.dao.IsolationDAO;
import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.domain.AlarmAssetIsolation;
import com.nextcontrols.domain.AlarmTrap;
import com.nextcontrols.domain.Branch;
import com.nextcontrols.domain.Customer;
import com.nextcontrols.domain.Department;
import com.nextcontrols.domain.IsolatedAsset;
import com.nextcontrols.domain.Isolation;
import com.nextcontrols.domain.IsolationAssetSubfixtures;
import com.nextcontrols.domain.UserAudit;
import com.nextcontrols.utility.Status;

@ManagedBean(name = "isolations")
@SessionScoped
public class IsolationsPageBean implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private List<SelectItem> customers;
	private List<SelectItem> sites;
	private List<SelectItem> departments;
	private List<SelectItem> filterIsolationTypes;
	private String selectedCustomer;
	private String selectedSite;
	private String selectedDepartment;
	private String selectedIsolationType;
	private List<Isolation> deptIsolations;
	private Isolation tempIsolation;
	private Isolation newIsolation;
	private Isolation chosenIsolation;
	private List<AlarmTrap> viewIsolatedAlarmTraps;
	private List<AlarmTrap> isolatedAlarmTraps;
	private List<IsolatedAsset> viewIsolatedAlarmAssets;
	private String selectedStatus;
	private List<String> statusList;
	private boolean showstatus;
	private TimeZone siteTimeZone;
	private String startingHour;
	private String startingMinutes;
	private String endHour;
	private String endMinutes;
	private List<AlarmTrap> alarmTraps;
	private List<AlarmAssetIsolation> isolationAlarmAssets;
	private Date maxDate;
	private UIData almAssetData;
	private UIData isolatedAssetData;
	private List<SelectItem> hours;
	private List<SelectItem> minutes;
	private List<SelectItem> endHours;
	private String style;
	private boolean toggle;
	private List<SelectItem> weekdays;
	private AlarmAssetIsolation chosenAlmAssetIsolation;
	private AlarmAssetIsolation chosenIsolatedAlmAsset;
	private List<AlarmAssetIsolation> isolatedAlarmAssets;

	public IsolationsPageBean() {
		customers = new ArrayList<SelectItem>();
		sites = new ArrayList<SelectItem>();
		departments = new ArrayList<SelectItem>();
		filterIsolationTypes = new ArrayList<SelectItem>();
		selectedCustomer = "";
		selectedSite = "";
		selectedDepartment = "";
		selectedIsolationType = "";
		viewIsolatedAlarmTraps = new ArrayList<AlarmTrap>();
		viewIsolatedAlarmAssets = new ArrayList<IsolatedAsset>();
		this.statusList = new ArrayList<String>();
		startingHour = "";
		startingMinutes = "";
		endHour = "";
		endMinutes = "";
		alarmTraps = new ArrayList<AlarmTrap>();
		newIsolation = new Isolation(0, "", "Once", null, Calendar
				.getInstance().getTime(), null, true, 0, Calendar.getInstance()
				.getTime(), "", "", 0, "");
		tempIsolation = new Isolation();
		chosenIsolation = new Isolation();
		hours = new ArrayList<SelectItem>();
		minutes = new ArrayList<SelectItem>();
		endHours = new ArrayList<SelectItem>();
		toggle = false;
		weekdays = new ArrayList<SelectItem>();
		chosenAlmAssetIsolation = new AlarmAssetIsolation();
		isolatedAlarmTraps = new ArrayList<AlarmTrap>();
		isolatedAlarmAssets = new ArrayList<AlarmAssetIsolation>();
	}

	public String updateIsl() {
		List<Isolation> deptIsolation = new ArrayList<Isolation>();
		if (selectedIsolationType.equals("All")) {
			deptIsolation = IsolationDAO.getInstance().getGlobalIsolations(
					Integer.parseInt(selectedDepartment), selectedSite, null);
		} else {
			deptIsolation = IsolationDAO.getInstance().getGlobalIsolations(
					Integer.parseInt(selectedDepartment), selectedSite,
					selectedIsolationType);
		}
		this.deptIsolations = deptIsolation;
		if (deptIsolations != null && deptIsolations.size() > 0) {
			this.convertDateTime(deptIsolations);
		}
		setShowstatus(false);
		// DataTable table = (DataTable) (FacesContext.getCurrentInstance()
		// .getViewRoot()
		// .findComponent("globalIsolationsFrm:tbIsolationList"));
		// table.setValue(deptIsolation);
		return "IsolationsPage.xhtml?faces-redirect=true";
	}

	public String enableIsolation() {
		IsolationDAO.getInstance().changeIsolationActivity(
				tempIsolation.getIsolationId(), true);
		if (selectedIsolationType.equals("All")) {
			deptIsolations = IsolationDAO.getInstance().getGlobalIsolations(
					Integer.parseInt(selectedDepartment), selectedSite, null);
		} else {
			deptIsolations = IsolationDAO.getInstance().getGlobalIsolations(
					Integer.parseInt(selectedDepartment), selectedSite,
					selectedIsolationType);
		}
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "AlarmIsolationEnabled",
						"The alarm isolation " + tempIsolation.getIsolationId()
								+ " was enabled", tempIsolation.getSiteCode()));

		return "IsolationsPage.xhtml?faces-redirect=true";
	}

	public String disableIsolation() {

		IsolationDAO.getInstance().changeIsolationActivity(
				tempIsolation.getIsolationId(), false);
		if (selectedIsolationType.equals("All")) {
			deptIsolations = IsolationDAO.getInstance().getGlobalIsolations(
					Integer.parseInt(selectedDepartment), selectedSite, null);
		} else {
			deptIsolations = IsolationDAO.getInstance().getGlobalIsolations(
					Integer.parseInt(selectedDepartment), selectedSite,
					selectedIsolationType);
		}
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);

		UserAuditDAO.getInstance()
				.insertUserAudit(
						new UserAudit(Integer.parseInt(session.getAttribute(
								"userId").toString()), new Timestamp(Calendar
								.getInstance().getTime().getTime()),
								"AlarmIsolationDisabled",
								"The alarm isolation "
										+ tempIsolation.getIsolationId()
										+ " was disabled", tempIsolation
										.getSiteCode()));

		return "IsolationsPage.xhtml?faces-redirect=true";
	}

	public void viewDetails() {
		viewIsolatedAlarmTraps = IsolationDAO.getInstance()
				.getIsolatedAlmTraps(tempIsolation.getIsolationId(),
						tempIsolation.getDepId());
		viewIsolatedAlarmAssets = IsolationDAO.getInstance()
				.getIsolatedAlmAssets(tempIsolation.getIsolationId(),
						tempIsolation.getDepId());
		if (viewIsolatedAlarmTraps.size() == 0) {
			viewIsolatedAlarmTraps = null;
		}
		if (viewIsolatedAlarmAssets.size() == 0) {
			viewIsolatedAlarmAssets = null;
		}
		System.out.println("done getting details");
	}

	public void updateStatusChanged() {
		if (this.selectedStatus.equalsIgnoreCase("active")) {
			setShowstatus(true);
			if (this.selectedIsolationType.equalsIgnoreCase("All")) {
				this.deptIsolations = IsolationDAO.getInstance()
						.getActiveIsolations(this.selectedSite, null,
								Integer.parseInt(this.selectedDepartment));
			} else {
				this.deptIsolations = IsolationDAO.getInstance()
						.getActiveIsolations(this.selectedSite,
								this.selectedIsolationType,
								Integer.parseInt(this.selectedDepartment));
			}
		} else if (this.selectedStatus.equalsIgnoreCase("disabled")) {
			setShowstatus(true);
			if (this.selectedIsolationType.equalsIgnoreCase("All")) {
				this.deptIsolations = IsolationDAO.getInstance()
						.getDisabledIsolations(this.selectedSite, null,
								Integer.parseInt(this.selectedDepartment));
			} else {
				this.deptIsolations = IsolationDAO.getInstance()
						.getDisabledIsolations(this.selectedSite,
								this.selectedIsolationType,
								Integer.parseInt(this.selectedDepartment));
			}
		} else if (this.selectedStatus.equalsIgnoreCase("expired")) {
			setShowstatus(true);
			if (this.selectedIsolationType.equalsIgnoreCase("All")) {
				this.deptIsolations = IsolationDAO.getInstance()
						.getExpiredIsolations(this.selectedSite, null,
								Integer.parseInt(this.selectedDepartment));
			} else {
				this.deptIsolations = IsolationDAO.getInstance()
						.getExpiredIsolations(this.selectedSite,
								this.selectedIsolationType,
								Integer.parseInt(this.selectedDepartment));
			}
		} else if (this.selectedStatus.equalsIgnoreCase("inactive")) {
			setShowstatus(true);
			if (this.selectedIsolationType.equalsIgnoreCase("All")) {
				this.deptIsolations = IsolationDAO.getInstance()
						.getInactiveIsolations(this.selectedSite, null,
								Integer.parseInt(this.selectedDepartment));
			} else {
				this.deptIsolations = IsolationDAO.getInstance()
						.getInactiveIsolations(this.selectedSite,
								this.selectedIsolationType,
								Integer.parseInt(this.selectedDepartment));
			}
		} else {
			setShowstatus(true);
			if (selectedIsolationType.equals("All")) {
				this.deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, null);
			} else {
				this.deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, selectedIsolationType);
			}
		}
		 if(this.deptIsolations!=null && this.deptIsolations.size()>0){
			 this.convertDateTime(deptIsolations);
		 }
		DataTable table = (DataTable) (FacesContext.getCurrentInstance()
				.getViewRoot()
				.findComponent("frmIsolationsPage:tbIsolationList"));
		table.setValue(this.deptIsolations);
	}

	public void addPreProcess(ActionEvent e) {
		TimeZone timeZone = getSiteTimeZone();
		Calendar currentTime = Calendar.getInstance();
		if (timeZone != null) {
			currentTime.setTimeZone(timeZone);
		}
		if (currentTime.get(Calendar.HOUR_OF_DAY) < 10) {
			this.startingHour = "0" + currentTime.get(Calendar.HOUR_OF_DAY);
			currentTime.add(Calendar.HOUR_OF_DAY, 4);
			if (currentTime.get(Calendar.HOUR_OF_DAY) < 10) {
				this.endHour = "0" + currentTime.get(Calendar.HOUR_OF_DAY);
			} else {
				this.endHour = String.valueOf(currentTime
						.get(Calendar.HOUR_OF_DAY));
			}
		} else {
			this.startingHour = String.valueOf(currentTime
					.get(Calendar.HOUR_OF_DAY));
			currentTime.add(Calendar.HOUR_OF_DAY, 4);
			if (currentTime.get(Calendar.HOUR_OF_DAY) < 10) {
				this.endHour = "0" + currentTime.get(Calendar.HOUR_OF_DAY);
			} else {
				this.endHour = String.valueOf(currentTime
						.get(Calendar.HOUR_OF_DAY));
			}
		}
		if (currentTime.get(Calendar.MINUTE) < 10) {
			this.startingMinutes = "0" + currentTime.get(Calendar.MINUTE);
			this.endMinutes = startingMinutes;
		} else {
			this.startingMinutes = String.valueOf(currentTime
					.get(Calendar.MINUTE));
			this.endMinutes = startingMinutes;
		}
		if ((alarmTraps == null) || (alarmTraps.size() == 0)) {
			alarmTraps = AlarmTrapDAO.getInstance().getAlarmTraps(
					Integer.parseInt(selectedDepartment));
		} else if (alarmTraps.get(0).getDep_id() != Integer
				.parseInt(selectedDepartment)) {
			alarmTraps = AlarmTrapDAO.getInstance().getAlarmTraps(
					Integer.parseInt(selectedDepartment));
		}
		if ((isolationAlarmAssets == null)
				|| (isolationAlarmAssets.size() == 0)) {
			isolationAlarmAssets = IsolationDAO.getInstance()
					.getAlarmAssetsIsolationAdd(
							Integer.parseInt(selectedDepartment));
		} else if (isolationAlarmAssets.get(0).getDepartmentId() != Integer
				.parseInt(selectedDepartment)) {
			isolationAlarmAssets = IsolationDAO.getInstance()
					.getAlarmAssetsIsolationAdd(
							Integer.parseInt(selectedDepartment));
		}

		setToggle(false);
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext != null) {
			requestContext.update("addIsolationFrm:renderAssets");
		}

		Calendar max = Calendar.getInstance();
		max.add(Calendar.DAY_OF_YEAR, 7);
		this.maxDate = max.getTime();

	}

	private final TimeZone getSiteTimeZone() {
		String siteCode = this.selectedSite.split("-")[0];
		this.siteTimeZone = BranchDAO.getInstance().getSiteTimezone(siteCode);
		// System.out.println(this.siteTimeZone.getDisplayName());
		return siteTimeZone;
	}

	private void convertDateTime(List<Isolation> tempDeptIsolations) {
		if (tempDeptIsolations != null && tempDeptIsolations.size() > 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date startDate = null;
			Date endDate = null;
			String startTimeInGB = null;
			String endTimeInGB = null;
			String startHourInGB = null;
			String endHourInGB = null;
			Date convertedStartTime = null;
			String convertedStartHour = null;
			Date convertedEndTime = null;
			String convertedEndHour = null;
			TimeZone siteLocalTimeZone = this.getSiteTimeZone();
			TimeZone timezoneGB = TimeZone.getTimeZone("GB");
			if (!siteLocalTimeZone.equals(timezoneGB)) {
				for (Isolation isolation : tempDeptIsolations) {
					startDate = isolation.getStartTime();
					if (startDate != null) {
						startTimeInGB = formatter.format(startDate);
					}
					startHourInGB = isolation.getStartHour();
					endDate = isolation.getEndTime();
					if (endDate != null) {
						endTimeInGB = formatter.format(endDate);
					}
					endHourInGB = isolation.getEndHour();
					String convertedDateTime = this.convertTimeZone(
							startTimeInGB, startHourInGB, timezoneGB,
							siteLocalTimeZone);
					if (convertedDateTime != null) {
						String[] convertedDateTimeSplit = convertedDateTime
								.split("\\s");
						if (convertedDateTimeSplit[0].indexOf("/") > 0) {
							try {
								convertedStartTime = formatter
										.parse(convertedDateTimeSplit[0]);
								isolation.setStartTime(convertedStartTime);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (convertedDateTimeSplit[1].indexOf(":") > 0) {
							convertedStartHour = convertedDateTimeSplit[1];
							isolation.setStartHour(convertedStartHour);
						}

					}
					convertedDateTime = this.convertTimeZone(endTimeInGB,
							(endHourInGB.isEmpty() || endHourInGB
									.matches("\\s+")) ? null : endHourInGB,
							timezoneGB, siteLocalTimeZone);
					if (convertedDateTime != null) {
						String[] convertedDateTimeSplit = convertedDateTime
								.split("\\s");
						if (convertedDateTimeSplit[0].indexOf("/") > 0) {
							try {
								convertedEndTime = formatter
										.parse(convertedDateTimeSplit[0]);
								isolation.setEndTime(convertedEndTime);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (convertedDateTimeSplit[1].indexOf(":") > 0) {
							convertedEndHour = convertedDateTimeSplit[1];
							isolation.setEndHour(convertedEndHour);
						}
					}
				}
			}
		}
	}

	public void handleToggle(ToggleEvent event) {

		Visibility visibility = event.getVisibility();
		// System.out.println("toggle fired.... !!! "+ "\n" +
		// (visibility.name().toString() == Visibility.VISIBLE.toString()) ) ;
		if (visibility.name().toString() == Visibility.VISIBLE.toString()) {
			setToggle(true);
		} else {
			setToggle(false);
		}
		setStyle();
		// System.out.println("updating panel");
		RequestContext.getCurrentInstance().update(
				"frmAddIsolationsPage:tbChooseAssets");
	}

	public void chooseType() {
		if (newIsolation.getIsolationType().equals("Once")) {
			// once=true;
		} else {
			// once=false;
			newIsolation.setDayOfWeek(null);
		}
	}

	public void chooseTypeChosen(ActionEvent e) {
		if (chosenIsolation.getIsolationType().equals("Once")
				|| chosenIsolation.getIsolationType().equals("Daily")) {
			// chosenIsolation.setEndTime(Calendar.getInstance().getTime());
		} else {
			chosenIsolation.setEndTime(null);
		}
	}

	private String convertTimeZone(String date, String time,
			TimeZone fromTimezone, TimeZone toTimeZone) {
		if (!fromTimezone.equals(toTimeZone)) {
			Calendar cal = Calendar.getInstance(fromTimezone);
			String[] dateSplit = null;
			String[] timeSplit = null;
			if (time != null) {
				timeSplit = time.split(":");
			}
			if (date != null) {
				dateSplit = date.split("/");
			}
			if (dateSplit != null) {
				cal.set(Calendar.DATE, Integer.parseInt(dateSplit[0]));
				cal.set(Calendar.MONTH, Integer.parseInt(dateSplit[1]) - 1);
				cal.set(Calendar.YEAR, Integer.parseInt(dateSplit[2]));
			}
			if (timeSplit != null) {
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
				cal.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
				cal.get(Calendar.HOUR_OF_DAY);
				cal.get(Calendar.MINUTE);
			}
			// System.out.println("Time in " + fromTimezone.getDisplayName() +
			// " : " + cal.get(Calendar.DATE) +"/"+
			// (cal.get(Calendar.MONTH)+1)+"/"+ cal.get(Calendar.YEAR) +" " +
			// ((cal.get(Calendar.HOUR_OF_DAY)<10) ?
			// ("0"+cal.get(Calendar.HOUR_OF_DAY) ):
			// (cal.get(Calendar.HOUR_OF_DAY)))
			// +":" + (cal.get(Calendar.MINUTE)<10 ?
			// "0"+cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)) );
			cal.setTimeZone(toTimeZone);

			// System.out.println("Time in " + toTimeZone.getDisplayName() +
			// " : " + cal.get(Calendar.DATE) +"/"+
			// (cal.get(Calendar.MONTH)+1)+"/"+ cal.get(Calendar.YEAR) +" " +
			// ((cal.get(Calendar.HOUR_OF_DAY)<10) ?
			// ("0"+cal.get(Calendar.HOUR_OF_DAY) ):
			// (cal.get(Calendar.HOUR_OF_DAY)))
			// +":" + (cal.get(Calendar.MINUTE)<10 ?
			// "0"+cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)) );
			if (date != null) {
				return cal.get(Calendar.DATE)
						+ "/"
						+ (cal.get(Calendar.MONTH) + 1)
						+ "/"
						+ cal.get(Calendar.YEAR)
						+ " "
						+ ((cal.get(Calendar.HOUR_OF_DAY) < 10) ? ("0" + cal
								.get(Calendar.HOUR_OF_DAY)) : (cal
								.get(Calendar.HOUR_OF_DAY)))
						+ ":"
						+ (cal.get(Calendar.MINUTE) < 10 ? "0"
								+ cal.get(Calendar.MINUTE) : cal
								.get(Calendar.MINUTE));
			} else if (date == null && time != null) {
				return null
						+ " "
						+ ((cal.get(Calendar.HOUR_OF_DAY) < 10) ? ("0" + cal
								.get(Calendar.HOUR_OF_DAY)) : (cal
								.get(Calendar.HOUR_OF_DAY)))
						+ ":"
						+ (cal.get(Calendar.MINUTE) < 10 ? "0"
								+ cal.get(Calendar.MINUTE) : cal
								.get(Calendar.MINUTE));
			} else {
				return null;
			}
		} else {
			// System.out.println("Both the timezones are equal...");
			return date + " " + time;
		}
	}

	public String addAlarmIsolation() {
		// System.out.println("add alarm called");
		String startTime = this.startingHour + ":" + this.startingMinutes;
		String convertedStartDateTime = this.convertTimeZone(
				newIsolation.getStartDate(), startTime, getSiteTimeZone(),
				TimeZone.getTimeZone("GB"));
		if (convertedStartDateTime != null) {
			String[] convertedDateTimeSplit = convertedStartDateTime
					.split("\\s");
			String convertedDate = convertedDateTimeSplit[0];
			String convertedTime = convertedDateTimeSplit[1];
			if (convertedDate.indexOf("/") > 0) {
				try {
					newIsolation
							.setStartTime(new SimpleDateFormat("dd/MM/yyyy")
									.parse(convertedDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (convertedTime.indexOf(":") > 0) {
				newIsolation.setStartHour(convertedTime);
			}
		}
		String endTime = this.endHour + ":" + this.endMinutes;
		String convertedEndDateTime = this.convertTimeZone(
				newIsolation.getEndDate(), endTime, getSiteTimeZone(),
				TimeZone.getTimeZone("GB"));
		if (convertedEndDateTime != null) {
			String[] convertedEndDateTimeSplit = convertedEndDateTime
					.split("\\s");
			String convertedDate = convertedEndDateTimeSplit[0];
			String convertedTime = convertedEndDateTimeSplit[1];
			if (convertedDate.indexOf("/") > 0) {
				try {
					newIsolation.setEndTime(new SimpleDateFormat("dd/MM/yyyy")
							.parse(convertedDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (convertedTime.indexOf(":") > 0) {
				newIsolation.setEndHour(convertedTime);
			}
		}
		// alarm traps
		ArrayList<Integer> trapIds = new ArrayList<Integer>();
		for (int i = 0; i <= alarmTraps.size() - 1; i++) {
			// System.out.println("inside the loop");
			if (alarmTraps.get(i).isChosen() == true) {
				trapIds.add(alarmTraps.get(i).getAlarmTrapId());
			}
		}

		// adding the assets and subfixtures
		ArrayList<IsolationAssetSubfixtures> assetSubfixtures = new ArrayList<IsolationAssetSubfixtures>();
		for (int j = 0; j <= isolationAlarmAssets.size() - 1; j++) {
			if (isolationAlarmAssets.get(j).isWholeFixture() == true) {
				assetSubfixtures.add(new IsolationAssetSubfixtures(
						isolationAlarmAssets.get(j).getAlarmAssetId(), 0));
			} else if (isolationAlarmAssets.get(j).getSubfixIdsSize() > 0) {
				for (int z = 0; z <= isolationAlarmAssets.get(j)
						.getSubfixtureIdList().size() - 1; z++) {
					if (isolationAlarmAssets.get(j).getSubfixtureIdList()
							.get(z).isIsolate() == true) {
						assetSubfixtures.add(new IsolationAssetSubfixtures(
								isolationAlarmAssets.get(j).getAlarmAssetId(),
								isolationAlarmAssets.get(j)
										.getSubfixtureIdList().get(z)
										.getSearchStringId()));
					}
				}
			}
		}
		if (assetSubfixtures.size() > 0) {
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);

			int userId = (Integer) session.getAttribute("userId");
			if (newIsolation.getIsolationType().equals("Permanent")) {
				IsolationDAO.getInstance().addDeptIsolation(selectedSite,
						newIsolation.getIsolationType(),
						newIsolation.getDayOfWeek(),
						newIsolation.getStartTime(), null, true, userId,
						Calendar.getInstance().getTime(), trapIds,
						assetSubfixtures, newIsolation.getStartHour(), null,
						Integer.parseInt(selectedDepartment),
						newIsolation.getName(), newIsolation.getNameMore());
			} else {
				IsolationDAO.getInstance().addDeptIsolation(selectedSite,
						newIsolation.getIsolationType(),
						newIsolation.getDayOfWeek(),
						newIsolation.getStartTime(), newIsolation.getEndTime(),
						true, userId, Calendar.getInstance().getTime(),
						trapIds, assetSubfixtures, newIsolation.getStartHour(),
						newIsolation.getEndHour(),
						Integer.parseInt(selectedDepartment),
						newIsolation.getName(), newIsolation.getNameMore());
			}

			if (selectedIsolationType.equals("All")) {
				deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, null);
				this.convertDateTime(deptIsolations);
			} else {
				deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, selectedIsolationType);
				this.convertDateTime(deptIsolations);
			}
			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"AlarmIsolationAdded", "A new alarm isolation "
									+ newIsolation.getName() + " was added",
							selectedSite));

			newIsolation = new Isolation(0, "", "Once", null, Calendar
					.getInstance().getTime(), null, true, 0, Calendar
					.getInstance().getTime(), "", "", 0, "");
			hours = new ArrayList<SelectItem>();
			minutes = new ArrayList<SelectItem>();
			isolationAlarmAssets = new ArrayList<AlarmAssetIsolation>();
			alarmTraps = new ArrayList<AlarmTrap>();
			weekdays = new ArrayList<SelectItem>();
			startingHour = "";
			startingMinutes = "";
			endHour = "";
			endMinutes = "";
			// once=false;
			System.out.println("relocating");
			return "IsolationsPage?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("At least one Alarm Asset must be selected");
			message.setSummary("At least one Alarm Asset must be selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
	}

	public void populateChosenAssetSubfixtures() {
		setChosenAlmAssetIsolation((AlarmAssetIsolation) almAssetData
				.getRowData());
	}

	public void populateIsolatedAssetSubfixtures() {
		setChosenIsolatedAlmAsset((AlarmAssetIsolation) isolatedAssetData
				.getRowData());

	}

	public void modificationPreProcess(ActionEvent e) {
		setToggle(false);
		if ((chosenIsolation != null) && (chosenIsolation.getDepId() > 0)) {
			String[] isolationStart = chosenIsolation.getStartHour().split(":");
			startingHour = isolationStart[0];
			startingMinutes = isolationStart[1];
			if (chosenIsolation.getIsolationType().equals("Permanent") == false) {
				String[] isolationEnd = chosenIsolation.getEndHour().split(":");
				endHour = isolationEnd[0];
				endMinutes = isolationEnd[1];

			}
			// if (chosenIsolation.getIsolationType().equals("Once")){
			// once=true;
			// setOnce(true);
			// }

		}
	}

	public String modifyAlmIsolationPage() {
		if ((chosenIsolation != null) && (chosenIsolation.getDepId() > 0)) {
			return "ModifyIsolationPage?faces-redirect=true";
		} else {
			return "IsolationsPage?faces-redirect=true";
		}
	}

	public void populateSites() {
		setSites(Integer.parseInt(this.getSelectedCustomer()));
	}

	public void populateDepts() {
		setDepartments(selectedSite);
	}

	public void checkboxClickedIsolated(ActionEvent e) {
		AlarmAssetIsolation temp = (AlarmAssetIsolation) isolatedAssetData
				.getRowData();
		if (temp.getSubfixIdsSize() > 0) {
			temp.setWholeFixture(false);
		}
	}

	public String modAlarmIsolation() {
		this.siteTimeZone = BranchDAO.getInstance().getSiteTimezone(
				this.chosenIsolation.getSiteCode());
		ArrayList<Integer> trapIds = new ArrayList<Integer>();
		for (int i = 0; i <= isolatedAlarmTraps.size() - 1; i++) {
			if (isolatedAlarmTraps.get(i).isChosen() == true) {
				trapIds.add(isolatedAlarmTraps.get(i).getAlarmTrapId());
			}
		}

		// adding the assets and subfixtures
		ArrayList<IsolationAssetSubfixtures> assetSubfixtures = new ArrayList<IsolationAssetSubfixtures>();
		for (int j = 0; j <= isolatedAlarmAssets.size() - 1; j++) {
			if (isolatedAlarmAssets.get(j).isWholeFixture() == true) {
				assetSubfixtures.add(new IsolationAssetSubfixtures(
						isolatedAlarmAssets.get(j).getAlarmAssetId(), 0));
			} else if (isolatedAlarmAssets.get(j).getSubfixIdsSize() > 0) {
				for (int z = 0; z <= isolatedAlarmAssets.get(j)
						.getSubfixtureIdList().size() - 1; z++) {
					if (isolatedAlarmAssets.get(j).getSubfixtureIdList().get(z)
							.isIsolate() == true) {
						assetSubfixtures.add(new IsolationAssetSubfixtures(
								isolatedAlarmAssets.get(j).getAlarmAssetId(),
								isolatedAlarmAssets.get(j)
										.getSubfixtureIdList().get(z)
										.getSearchStringId()));
					}
				}
			}
		}
		if (assetSubfixtures.size() > 0) {
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);

			if (chosenIsolation.getIsolationType().equals("Permanent") == true) {
				chosenIsolation.setEndTime(null);
				chosenIsolation.setEndHour(null);
				String s_hour = this.startingHour + ":" + this.startingMinutes;
				chosenIsolation.setStartHour(s_hour);
				this.endHour = null;
				this.endMinutes = null;
			}

			if (chosenIsolation.getIsolationType().equals("Weekly") == false) {
				chosenIsolation.setDayOfWeek(null);
			} else {
				chosenIsolation.setEndTime(null);
			}
			String startTime = this.startingHour + ":" + this.startingMinutes;
			String convertedStartDateTime = this.convertTimeZone(
					chosenIsolation.getStartDate(), startTime,
					getSiteTimeZone(), TimeZone.getTimeZone("GB"));
			if (convertedStartDateTime != null) {
				String[] convertedDateTimeSplit = convertedStartDateTime
						.split("\\s");
				String convertedDate = convertedDateTimeSplit[0];
				String convertedTime = convertedDateTimeSplit[1];
				if (convertedDate.indexOf("/") > 0) {
					try {
						chosenIsolation.setStartTime(new SimpleDateFormat(
								"dd/MM/yyyy").parse(convertedDate));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (convertedTime.indexOf(":") > 0) {
					chosenIsolation.setStartHour(convertedTime);
				}
			}
			String endTime = (endHour != null && endMinutes != null) ? (this.endHour
					+ ":" + this.endMinutes)
					: null;
			String convertedEndDateTime = this.convertTimeZone(
					chosenIsolation.getEndDate(), endTime, getSiteTimeZone(),
					TimeZone.getTimeZone("GB"));
			if (convertedEndDateTime != null) {
				String[] convertedEndDateTimeSplit = convertedEndDateTime
						.split("\\s");
				String convertedDate = convertedEndDateTimeSplit[0];
				String convertedTime = convertedEndDateTimeSplit[1];
				if (convertedDate.indexOf("/") > 0) {
					try {
						chosenIsolation.setEndTime(new SimpleDateFormat(
								"dd/MM/yyyy").parse(convertedDate));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				if (convertedTime.indexOf(":") > 0) {
					chosenIsolation.setEndHour(convertedTime);
				}
			}

			if (chosenIsolation.getIsolationType().equals("Permanent")) {
				IsolationDAO.getInstance().modDeptIsolation(chosenIsolation,
						chosenIsolation.getStartHour(), null, trapIds,
						assetSubfixtures);
			} else {
				IsolationDAO.getInstance()
						.modDeptIsolation(chosenIsolation,
								chosenIsolation.getStartHour(),
								chosenIsolation.getEndHour(), trapIds,
								assetSubfixtures);

				// String s_hour = this.startingHour+ ":" +
				// this.startingMinutes;
				// chosenIsolation.setStartHour(s_hour);
				// String e_hour = this.endHour +":" + this.endMinutes;
				// chosenIsolation.setEndHour(e_hour);
			}
			if (selectedIsolationType.equals("All")) {
				deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, null);
				this.convertDateTime(deptIsolations);
			} else {
				deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, selectedIsolationType);
				this.convertDateTime(deptIsolations);
			}
			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"AlarmIsolationModified", "The alarm isolation "
									+ chosenIsolation.getIsolationId()
									+ " was modified", chosenIsolation
									.getSiteCode()));

			chosenIsolation = new Isolation();
			hours = new ArrayList<SelectItem>();
			minutes = new ArrayList<SelectItem>();
			isolationAlarmAssets = new ArrayList<AlarmAssetIsolation>();
			alarmTraps = new ArrayList<AlarmTrap>();
			startingHour = "";
			startingMinutes = "";
			endHour = "";
			endMinutes = "";
			// once=false;
			return "IsolationsPage?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("At least one Alarm Asset must be selected");
			message.setSummary("At least one Alarm Asset must be selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}
	}

	public void delAlmIsolations(ActionEvent e) {

		if ((chosenIsolation != null) && (chosenIsolation.getIsolationId() > 0)) {

			IsolationDAO.getInstance().delIsolation(
					chosenIsolation.getIsolationId(),
					chosenIsolation.getDepId());

			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			String delinfo = "An Isolation was deleted for the department "
					+ chosenIsolation.getDepId() + " with the id "
					+ chosenIsolation.getIsolationId();
			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"IsolationDeleted", delinfo, null));
			if (selectedIsolationType.equals("All")) {
				deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, null);
			} else {
				deptIsolations = IsolationDAO.getInstance()
						.getGlobalIsolations(
								Integer.parseInt(selectedDepartment),
								selectedSite, selectedIsolationType);
			}
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance()
					.getViewRoot()
					.findComponent("frmIsolationsPage:tbIsolationList");
			dataTable.setValue(deptIsolations);
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("No Isolation was selected");
			message.setSummary("No Isolation was selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		chosenIsolation = new Isolation();
	}

	public void setCustomers(List<SelectItem> customers) {
		if (this.customers.size() <= 0 && customers.size() <= 0) {
			List<Customer> customerList = CustomerDAO.getInstance()
					.getCustomers();
			for (int i = 0; i <= customerList.size() - 1; i++) {
				this.customers.add(new SelectItem(customerList.get(i).getId(),
						customerList.get(i).getName()));
			}
		} else
			this.customers = customers;

	}

	public List<SelectItem> getCustomers() {
		if (customers.size() <= 0) {
			List<SelectItem> custs = new ArrayList<SelectItem>();
			List<Customer> customerList = CustomerDAO.getInstance()
					.getCustomers();
			for (int i = 0; i <= customerList.size() - 1; i++) {
				custs.add(new SelectItem(customerList.get(i).getId(),
						customerList.get(i).getName()));
			}
			setCustomers(custs);
		}
		return customers;
	}

	public List<SelectItem> getSites() {
		return sites;
	}

	public void setSites(int customerId) {
		List<Branch> branches = BranchDAO.getInstance().getBranch(customerId);
		sites = new ArrayList<SelectItem>();
		for (int i = 0; i <= branches.size() - 1; i++) {
			sites.add(new SelectItem(branches.get(i).getBranchCode(), branches
					.get(i).getBranchCode() + " - " + branches.get(i).getName()));
		}
		if (sites.size() > 0) {
			setDepartments(sites.get(0).getValue().toString());
		}
		this.sites = sites;
	}

	public void setDepartments(String siteCode) {
		List<String> siteCodes = new ArrayList<String>();
		siteCodes.add(siteCode);
		List<Department> depts = DepartmentDAO.getInstance().getDepartmentList(
				siteCodes);
		departments = new ArrayList<SelectItem>();
		departments.add(new SelectItem("0", "All"));
		for (int i = 0; i <= depts.size() - 1; i++) {
			departments.add(new SelectItem(depts.get(i).getDep_id(), depts.get(
					i).getName()));
		}
	}

	public List<SelectItem> getDepartments() {
		return departments;
	}

	public String getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(String selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public String getSelectedSite() {
		return selectedSite;
	}

	public void setSelectedSite(String selectedSite) {
		this.selectedSite = selectedSite;
	}

	public String getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(String selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public String getSelectedIsolationType() {
		return selectedIsolationType;
	}

	public void setSelectedIsolationType(String selectedIsolationType) {
		this.selectedIsolationType = selectedIsolationType;
	}

	public List<SelectItem> getFilterIsolationTypes() {
		setFilterIsolationTypes();
		return filterIsolationTypes;
	}

	public void setFilterIsolationTypes() {
		filterIsolationTypes = new ArrayList<SelectItem>();
		filterIsolationTypes.add(new SelectItem("All", "All"));
		filterIsolationTypes.add(new SelectItem("Once", "Once"));
		filterIsolationTypes.add(new SelectItem("Daily", "Daily"));
		filterIsolationTypes.add(new SelectItem("Weekly", "Weekly"));
		filterIsolationTypes.add(new SelectItem("Permanent", "Permanent"));
	}

	public List<Isolation> getDeptIsolations() {
		return deptIsolations;
	}

	public void setDeptIsolations(List<Isolation> deptIsolations) {
		this.deptIsolations = deptIsolations;
	}

	public Isolation getTempIsolation() {
		return tempIsolation;
	}

	public void setTempIsolation(Isolation tempIsolation) {
		this.tempIsolation = tempIsolation;
	}

	public List<AlarmTrap> getViewIsolatedAlarmTraps() {
		return viewIsolatedAlarmTraps;
	}

	public void setViewIsolatedAlarmTraps(List<AlarmTrap> viewIsolatedAlarmTraps) {
		this.viewIsolatedAlarmTraps = viewIsolatedAlarmTraps;
	}

	public List<IsolatedAsset> getViewIsolatedAlarmAssets() {
		return viewIsolatedAlarmAssets;
	}

	public void setViewIsolatedAlarmAssets(
			List<IsolatedAsset> viewIsolatedAlarmAssets) {
		this.viewIsolatedAlarmAssets = viewIsolatedAlarmAssets;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public List<String> getStatusList() {
		if (this.statusList == null || this.statusList.size() == 0) {
			setStatusList();
		}
		return statusList;
	}

	public void setStatusList() {
		for (Status status : Status.values()) {
			this.statusList.add(status.toString());
		}
	}

	public boolean isShowstatus() {
		return showstatus;
	}

	public void setShowstatus(boolean showstatus) {
		this.showstatus = showstatus;
	}

	public void setSiteTimeZone(TimeZone siteTimeZone) {
		this.siteTimeZone = siteTimeZone;
	}

	public String getStartingHour() {
		return startingHour;
	}

	public void setStartingHour(String startingHour) {
		this.startingHour = startingHour;
	}

	public String getStartingMinutes() {
		return startingMinutes;
	}

	public void setStartingMinutes(String startingMinutes) {
		this.startingMinutes = startingMinutes;
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

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Isolation getNewIsolation() {
		return newIsolation;
	}

	public void setNewIsolation(Isolation newIsolation) {
		this.newIsolation = newIsolation;
	}

	public UIData getAlmAssetData() {
		return almAssetData;
	}

	public void setAlmAssetData(UIData almAssetData) {
		this.almAssetData = almAssetData;
	}

	public UIData getIsolatedAssetData() {
		return isolatedAssetData;
	}

	public void setIsolatedAssetData(UIData isolatedAssetData) {
		this.isolatedAssetData = isolatedAssetData;
	}

	public List<SelectItem> getHours() {
		setHours();
		return hours;
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

	public List<SelectItem> getMinutes() {
		setMinutes();
		return minutes;
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

	public List<SelectItem> getEndHours() {
		setEndHours();
		return endHours;
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

	public List<AlarmTrap> getAlarmTraps() {
		return alarmTraps;
	}

	public void setAlarmTraps(List<AlarmTrap> alarmTraps) {
		this.alarmTraps = alarmTraps;
	}

	public List<AlarmAssetIsolation> getIsolationAlarmAssets() {
		return isolationAlarmAssets;
	}

	public void setIsolationAlarmAssets(
			List<AlarmAssetIsolation> isolationAlarmAssets) {
		this.isolationAlarmAssets = isolationAlarmAssets;
	}

	public void setStyle() {
		if (toggle == false) {
			style = "bigIsolation";
		} else {
			style = "smallIsolation";
		}
	}

	public String getStyle() {
		setStyle();
		return style;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setWeekdays() {
		weekdays = new ArrayList<SelectItem>();
		weekdays.add(new SelectItem("Monday", "Monday"));
		weekdays.add(new SelectItem("Tuesday", "Tuesday"));
		weekdays.add(new SelectItem("Wednesday", "Wednesday"));
		weekdays.add(new SelectItem("Thursday", "Thursday"));
		weekdays.add(new SelectItem("Friday", "Friday"));
		weekdays.add(new SelectItem("Saturday", "Saturday"));
		weekdays.add(new SelectItem("Sunday", "Sunday"));
	}

	public List<SelectItem> getWeekdays() {
		setWeekdays();
		return weekdays;
	}

	public AlarmAssetIsolation getChosenAlmAssetIsolation() {
		return chosenAlmAssetIsolation;
	}

	public void setChosenAlmAssetIsolation(
			AlarmAssetIsolation chosenAlmAssetIsolation) {
		this.chosenAlmAssetIsolation = chosenAlmAssetIsolation;
	}

	public Isolation getChosenIsolation() {
		return chosenIsolation;
	}

	public void setChosenIsolation(Isolation chosenIsolation) {
		this.chosenIsolation = chosenIsolation;
		List<AlarmTrap> isolatedAlmTraps = new ArrayList<AlarmTrap>();
		List<AlarmAssetIsolation> isolatedAlmAssets = new ArrayList<AlarmAssetIsolation>();
		if (chosenIsolation != null) {
			isolatedAlmTraps = IsolationDAO.getInstance()
					.getSpecificIsolationAlmTraps(
							chosenIsolation.getIsolationId(),
							chosenIsolation.getDepId());
			setIsolatedAlarmTraps(isolatedAlmTraps);
		}
		if (chosenIsolation != null) {
			isolatedAlmAssets = IsolationDAO.getInstance()
					.getSpecificIsolationAlmAssets(
							chosenIsolation.getIsolationId(),
							chosenIsolation.getDepId());
			setIsolatedAlarmAssets(isolatedAlmAssets);
		}
	}

	public AlarmAssetIsolation getChosenIsolatedAlmAsset() {
		return chosenIsolatedAlmAsset;
	}

	public void setChosenIsolatedAlmAsset(
			AlarmAssetIsolation chosenIsolatedAlmAsset) {
		this.chosenIsolatedAlmAsset = chosenIsolatedAlmAsset;
	}

	public List<AlarmTrap> getIsolatedAlarmTraps() {
		return isolatedAlarmTraps;
	}

	public void setIsolatedAlarmTraps(List<AlarmTrap> isolatedAlarmTraps) {
		this.isolatedAlarmTraps = isolatedAlarmTraps;
	}

	public List<AlarmAssetIsolation> getIsolatedAlarmAssets() {
		return isolatedAlarmAssets;
	}

	public void setIsolatedAlarmAssets(
			List<AlarmAssetIsolation> isolatedAlarmAssets) {
		this.isolatedAlarmAssets = isolatedAlarmAssets;
	}

}
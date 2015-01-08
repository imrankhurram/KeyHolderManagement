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
import com.nextcontrols.dao.AssetGroupDAO;
import com.nextcontrols.dao.BranchDAO;
import com.nextcontrols.dao.IsolationDAO;
import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.domain.AlarmAssetIsolation;
import com.nextcontrols.domain.AlarmTrap;
import com.nextcontrols.domain.Asset;
import com.nextcontrols.domain.IsolatedAsset;
import com.nextcontrols.domain.Isolation;
import com.nextcontrols.domain.IsolationAssetSubfixtures;
import com.nextcontrols.domain.UserAudit;
import com.nextcontrols.utility.Status;

@ManagedBean(name = "assetgroup")
@SessionScoped
public class AssetGroupPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Asset> assetList = null;
	private Asset selectedAsset;
	private Isolation selectedIsolation;
	private AlarmAssetIsolation isolationAlarmAsset;

	private String startingHour;
	private String startingMinutes;
	private String endHour;
	private String endMinutes;
	private List<SelectItem> hours;
	private List<SelectItem> minutes;
	private List<SelectItem> endHours;
	private Date maxDate;
	private List<SelectItem> weekdays;
	private boolean toggle;
	private String style;
	private List<AlarmTrap> alarmTraps;
	private TimeZone siteTimeZone;
	private int assetGroupId;
	private boolean showIsolations;
	

	// /edit
	private List<Isolation> selectedIsolations;
	private Isolation chosenIsolation;
	private Isolation tempIsolation;
	private List<AlarmTrap> viewIsolatedAlarmTraps;
	private List<IsolatedAsset> viewIsolatedAlarmAssets;
	private String selectedStatus;
	private List<String> statusList;
	private boolean showstatus;

	public AssetGroupPageBean() {
		startingHour = "";
		startingMinutes = "";
		endHour = "";
		endMinutes = "";
		hours = new ArrayList<SelectItem>();
		minutes = new ArrayList<SelectItem>();
		endHours = new ArrayList<SelectItem>();
		weekdays = new ArrayList<SelectItem>();
		alarmTraps = new ArrayList<AlarmTrap>();
		statusList = new ArrayList<String>();
	}

	public void initializeAssets(int id) {
		this.showIsolations=false;
		this.assetGroupId = id;
		assetList = AssetGroupDAO.getInstance().listAssets(id);
		List<Integer> activeIsolatedAssetIds = IsolationDAO.getInstance()
				.getActiveIsolatedAssetIds(assetList);
		List<Integer> isolatedAssetIds = IsolationDAO.getInstance()
				.getIsolatedAssetIds(assetList);
		// for(Integer idd:isolatedAssetIds){
		// System.out.println("isolated asset id: "+idd);
		// }
		// System.out.println("asset group id:" + id);
		for (Asset asset : assetList) {
			// System.out.println("id: " + asset.getId());
			asset.setIsolated(activeIsolatedAssetIds.contains(asset.getId()));
			asset.setHasIsolations(isolatedAssetIds.contains(asset.getId()));
		}
		// IsolationDAO.getInstance().getAllAlarmAssetsIsolations(departmentId)
	}

	public void isolateAsset(Asset selectedAsset) {
		this.selectedAsset = selectedAsset;
		isolateFixturePreProcessor();
//		return "IsolateAlarmAssetPage.xhtml?faces-redirect=true";
	}

	public void chooseType() {
		if (this.selectedIsolation.getIsolationType().equals("Once")) {
			// once=true;
		} else {
			// once=false;
			this.selectedIsolation.setDayOfWeek(null);
		}
	}

/*	public void handleToggle(ToggleEvent event) {

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
				"frmIsolateAssetPage:tbChooseAssets");
	}*/

	public void isolateFixturePreProcessor() {
		selectedIsolation = new Isolation(0, "", "Once", null, Calendar
				.getInstance().getTime(), null, true, 0, Calendar.getInstance()
				.getTime(), "", "", 0, "");
		
//		System.out.println("selected asset id: " + this.selectedAsset.getId());

		alarmTraps = AlarmTrapDAO.getInstance().getAlarmTrapsForAsset(
				this.selectedAsset.getId());

		isolationAlarmAsset = IsolationDAO.getInstance()
				.getAlarmAssetsIsolationByAlarmAssetId(
						this.selectedAsset.getAlarmasset_id());

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

//		setToggle(false);
//		RequestContext requestContext = RequestContext.getCurrentInstance();
//		if (requestContext != null) {
//			requestContext.update("frmIsolateAssetPage:renderAssets");
//		}

		Calendar max = Calendar.getInstance();
		max.add(Calendar.DAY_OF_YEAR, 7);
		this.maxDate = max.getTime();
	}

	private final TimeZone getSiteTimeZone() {
		if (alarmTraps != null || alarmTraps.size() > 0)
			this.siteTimeZone = BranchDAO.getInstance().getSiteTimezone(
					alarmTraps.get(0).getSiteCode());
		// System.out.println(this.siteTimeZone.getDisplayName());
		return siteTimeZone;
	}

	public void addAlarmIsolation() {
		// System.out.println("add alarm called");
		String startTime = this.startingHour + ":" + this.startingMinutes;
		String convertedStartDateTime = this.convertTimeZone(
				this.selectedIsolation.getStartDate(), startTime,
				getSiteTimeZone(), TimeZone.getTimeZone("GB"));
		if (convertedStartDateTime != null) {
			String[] convertedDateTimeSplit = convertedStartDateTime
					.split("\\s");
			String convertedDate = convertedDateTimeSplit[0];
			String convertedTime = convertedDateTimeSplit[1];
			if (convertedDate.indexOf("/") > 0) {
				try {
					selectedIsolation.setStartTime(new SimpleDateFormat(
							"dd/MM/yyyy").parse(convertedDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (convertedTime.indexOf(":") > 0) {
				selectedIsolation.setStartHour(convertedTime);
			}
		}
		String endTime = this.endHour + ":" + this.endMinutes;
		String convertedEndDateTime = this.convertTimeZone(
				selectedIsolation.getEndDate(), endTime, getSiteTimeZone(),
				TimeZone.getTimeZone("GB"));
		if (convertedEndDateTime != null) {
			String[] convertedEndDateTimeSplit = convertedEndDateTime
					.split("\\s");
			String convertedDate = convertedEndDateTimeSplit[0];
			String convertedTime = convertedEndDateTimeSplit[1];
			if (convertedDate.indexOf("/") > 0) {
				try {
					selectedIsolation.setEndTime(new SimpleDateFormat(
							"dd/MM/yyyy").parse(convertedDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (convertedTime.indexOf(":") > 0) {
				selectedIsolation.setEndHour(convertedTime);
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
		isolationAlarmAsset.setWholeFixture(true);
//		if (isolationAlarmAsset.isWholeFixture() == true) {
			assetSubfixtures.add(new IsolationAssetSubfixtures(
					isolationAlarmAsset.getAlarmAssetId(), 0));
//		} else if (isolationAlarmAsset.getSubfixIdsSize() > 0) {
//			for (int z = 0; z <= isolationAlarmAsset.getSubfixtureIdList()
//					.size() - 1; z++) {
//				if (isolationAlarmAsset.getSubfixtureIdList().get(z)
//						.isIsolate() == true) {
//					assetSubfixtures.add(new IsolationAssetSubfixtures(
//							isolationAlarmAsset.getAlarmAssetId(),
//							isolationAlarmAsset.getSubfixtureIdList().get(z)
//									.getSearchStringId()));
//				}
//			}
//		}
		if (assetSubfixtures.size() > 0) {
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			selectedIsolation.setName((String)session.getAttribute("user"));
			selectedIsolation.setNameMore(isolationAlarmAsset.getFixtureName());
			int userId = (Integer) session.getAttribute("userId");
			if (selectedIsolation.getIsolationType().equals("Permanent")) {
				IsolationDAO.getInstance().addDeptIsolation(
						alarmTraps.get(0).getSiteCode(),
						selectedIsolation.getIsolationType(),
						selectedIsolation.getDayOfWeek(),
						selectedIsolation.getStartTime(), null, true, userId,
						Calendar.getInstance().getTime(), trapIds,
						assetSubfixtures, selectedIsolation.getStartHour(),
						null, alarmTraps.get(0).getDep_id(),
						selectedIsolation.getName(),
						selectedIsolation.getNameMore());
			} else {
				IsolationDAO.getInstance().addDeptIsolation(
						alarmTraps.get(0).getSiteCode(),
						selectedIsolation.getIsolationType(),
						selectedIsolation.getDayOfWeek(),
						selectedIsolation.getStartTime(),
						selectedIsolation.getEndTime(), true, userId,
						Calendar.getInstance().getTime(), trapIds,
						assetSubfixtures, selectedIsolation.getStartHour(),
						selectedIsolation.getEndHour(),
						alarmTraps.get(0).getDep_id(),
						selectedIsolation.getName(),
						selectedIsolation.getNameMore());
			}

			UserAuditDAO.getInstance().insertUserAudit(
					new UserAudit(Integer.parseInt(session.getAttribute(
							"userId").toString()), new Timestamp(Calendar
							.getInstance().getTime().getTime()),
							"AlarmIsolationAdded", "A new alarm isolation "
									+ selectedIsolation.getName()
									+ " was added", alarmTraps.get(0)
									.getSiteCode()));

			selectedIsolation = new Isolation(0, "", "Once", null, Calendar
					.getInstance().getTime(), null, true, 0, Calendar
					.getInstance().getTime(), "", "", 0, "");

			hours = new ArrayList<SelectItem>();
			minutes = new ArrayList<SelectItem>();
			isolationAlarmAsset = null;
			alarmTraps = new ArrayList<AlarmTrap>();
			weekdays = new ArrayList<SelectItem>();
			startingHour = "";
			startingMinutes = "";
			endHour = "";
			endMinutes = "";
			// once=false;
			System.out.println("relocating");
			
			if(this.showIsolations){
				this.selectedIsolations = IsolationDAO.getInstance()
						.getIsolationsByAlarmAssetId(
								this.selectedAsset.getAlarmasset_id());
				System.out.println("selected isolations: "+this.selectedIsolations.size());
				RequestContext.getCurrentInstance().update(":frmAlarmAssetIsolationsPage");
				RequestContext.getCurrentInstance().execute(
						"PF('addIsolationAlarmAssetDlg').hide();");
			}
			else{
				// re-loading asset statuses
				List<Integer> activeIsolatedAssetIds = IsolationDAO.getInstance()
						.getActiveIsolatedAssetIds(assetList);
				List<Integer> isolatedAssetIds = IsolationDAO.getInstance()
						.getIsolatedAssetIds(assetList);
				// System.out.println("asset group id:" + id);
				for (Asset asset : assetList) {
					// System.out.println("id: " + asset.getId());
					asset.setIsolated(activeIsolatedAssetIds.contains(asset.getId()));
					asset.setHasIsolations(isolatedAssetIds.contains(asset.getId()));
				}
			}
			// /////////

//			return "SensorListPage?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("At least one Alarm Asset must be selected");
			message.setSummary("At least one Alarm Asset must be selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
//			return null;
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

	public String showAssetIsolationPreProcessor(Asset selectedAsset) {
		this.selectedAsset = selectedAsset;
		this.showIsolations=true;
		selectedIsolations = IsolationDAO.getInstance()
				.getIsolationsByAlarmAssetId(
						this.selectedAsset.getAlarmasset_id());
		System.out.println("selected isolations size: "+selectedIsolations.size());
		return "AlarmAssetIsolationsPage.xhtml?faces-redirect=true";
	}

	public String disableIsolation() {
		IsolationDAO.getInstance().changeIsolationActivity(
				tempIsolation.getIsolationId(), false);
		selectedIsolations = IsolationDAO.getInstance()
				.getIsolationsByAlarmAssetId(
						this.selectedAsset.getAlarmasset_id());
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
		return "AlarmAssetIsolationsPage.xhtml?faces-redirect=true";
	}

	public String enableIsolation() {
		IsolationDAO.getInstance().changeIsolationActivity(
				tempIsolation.getIsolationId(), true);
		selectedIsolations = IsolationDAO.getInstance()
				.getIsolationsByAlarmAssetId(
						this.selectedAsset.getAlarmasset_id());
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().insertUserAudit(
				new UserAudit(Integer.parseInt(session.getAttribute("userId")
						.toString()), new Timestamp(Calendar.getInstance()
						.getTime().getTime()), "AlarmIsolationEnabled",
						"The alarm isolation " + tempIsolation.getIsolationId()
								+ " was enabled", tempIsolation.getSiteCode()));

		return "AlarmAssetIsolationsPage.xhtml?faces-redirect=true";
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
			this.selectedIsolations = IsolationDAO.getInstance()
					.getActiveIsolations(
							selectedIsolations.get(0).getSiteCode(), null,
							selectedIsolations.get(0).getDepId());
		} else if (this.selectedStatus.equalsIgnoreCase("disabled")) {
			setShowstatus(true);
			this.selectedIsolations = IsolationDAO.getInstance()
					.getDisabledIsolations(
							selectedIsolations.get(0).getSiteCode(), null,
							selectedIsolations.get(0).getDepId());
		} else if (this.selectedStatus.equalsIgnoreCase("expired")) {
			setShowstatus(true);
			this.selectedIsolations = IsolationDAO.getInstance()
					.getExpiredIsolations(
							selectedIsolations.get(0).getSiteCode(), null,
							selectedIsolations.get(0).getDepId());
		} else if (this.selectedStatus.equalsIgnoreCase("inactive")) {
			setShowstatus(true);
			this.selectedIsolations = IsolationDAO.getInstance()
					.getInactiveIsolations(
							selectedIsolations.get(0).getSiteCode(), null,
							selectedIsolations.get(0).getDepId());
		} else {
			setShowstatus(true);
			this.selectedIsolations = IsolationDAO.getInstance()
					.getGlobalIsolations(selectedIsolations.get(0).getDepId(),
							selectedIsolations.get(0).getSiteCode(), null);
		}
		 if(this.selectedIsolations!=null && this.selectedIsolations.size()>0){
			 this.convertDateTime(selectedIsolations);
		 }
		DataTable table = (DataTable) (FacesContext.getCurrentInstance()
				.getViewRoot()
				.findComponent("frmAlarmAssetIsolationsPage:tbIsolationList"));
		table.setValue(this.selectedIsolations);
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
			this.isolationAlarmAsset = IsolationDAO.getInstance()
					.getAlarmAssetsIsolationByAlarmAssetId(
							this.selectedAsset.getAlarmasset_id());

		}
		 else {
				FacesMessage message = new FacesMessage();
				message.setDetail("At least one Alarm Isolation must be selected");
				message.setSummary("At least one Alarm Isolation must be selected");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, message);
//				return "AlarmAssetIsolationsPage?faces-redirect=true";
			}
	}

	public void modifyAlmIsolationPage() {
		if ((chosenIsolation != null) && (chosenIsolation.getDepId() > 0)) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":modIsolationsAlarmAssetDlg");
			context.execute("PF('modIsolationAlarmAssetDlg').show();");
//			return "ModifyAlarmAssetIsolationPage?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("At least one Alarm Isolation must be selected");
			message.setSummary("At least one Alarm Isolation must be selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
//			return "AlarmAssetIsolationsPage?faces-redirect=true";
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

	public void modAlarmIsolation() {
		this.siteTimeZone = BranchDAO.getInstance().getSiteTimezone(
				this.chosenIsolation.getSiteCode());
		ArrayList<Integer> trapIds = new ArrayList<Integer>();
		for (int i = 0; i <= alarmTraps.size() - 1; i++) {
			if (alarmTraps.get(i).isChosen() == true) {
				trapIds.add(alarmTraps.get(i).getAlarmTrapId());
			}
		}

		// adding the assets and subfixtures
		ArrayList<IsolationAssetSubfixtures> assetSubfixtures = new ArrayList<IsolationAssetSubfixtures>();
		isolationAlarmAsset.setWholeFixture(true);
//		if (isolationAlarmAsset.isWholeFixture() == true) {
			assetSubfixtures.add(new IsolationAssetSubfixtures(
					isolationAlarmAsset.getAlarmAssetId(), 0));
			
//		} else if (isolationAlarmAsset.getSubfixIdsSize() > 0) {
//			for (int z = 0; z <= isolationAlarmAsset.getSubfixtureIdList()
//					.size() - 1; z++) {
//				if (isolationAlarmAsset.getSubfixtureIdList().get(z)
//						.isIsolate() == true) {
//					assetSubfixtures.add(new IsolationAssetSubfixtures(
//							isolationAlarmAsset.getAlarmAssetId(),
//							isolationAlarmAsset.getSubfixtureIdList().get(z)
//									.getSearchStringId()));
//				}
//			}
//		}
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (convertedTime.indexOf(":") > 0) {
					chosenIsolation.setEndHour(convertedTime);
				}
			}
			chosenIsolation.setName((String)session.getAttribute("user"));
			chosenIsolation.setNameMore(isolationAlarmAsset.getFixtureName());
//			if (chosenIsolation.getIsolationType().equals("Permanent")) {
//				IsolationDAO.getInstance().modDeptIsolation(chosenIsolation,
//						chosenIsolation.getStartHour(), null, trapIds,
//						assetSubfixtures);
//			} else {
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
//			}
			selectedIsolation = new Isolation(0, "", "Once", null, Calendar
					.getInstance().getTime(), null, true, 0, Calendar
					.getInstance().getTime(), "", "", 0, "");

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
			isolationAlarmAsset = null;
			alarmTraps = new ArrayList<AlarmTrap>();
			startingHour = "";
			startingMinutes = "";
			endHour = "";
			endMinutes = "";
			// once=false;
			this.selectedIsolations = IsolationDAO.getInstance()
					.getIsolationsByAlarmAssetId(
							this.selectedAsset.getAlarmasset_id());
//			System.out.println("selected isolations: "+this.selectedIsolations.size());
			RequestContext.getCurrentInstance().update(":frmAlarmAssetIsolationsPage");
			RequestContext.getCurrentInstance().execute(
					"PF('modIsolationAlarmAssetDlg').hide();");
//			List<Integer> activeIsolatedAssetIds = IsolationDAO.getInstance()
//					.getActiveIsolatedAssetIds(assetList);
//			List<Integer> isolatedAssetIds = IsolationDAO.getInstance()
//					.getIsolatedAssetIds(assetGroupId);
//			// System.out.println("asset group id:" + id);
//			for (Asset asset : assetList) {
//				// System.out.println("id: " + asset.getId());
//				asset.setIsolated(activeIsolatedAssetIds.contains(asset.getId()));
//				asset.setHasIsolations(isolatedAssetIds.contains(asset.getId()));
//			}
//			return "SensorListPage?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("At least one Alarm Asset must be selected");
			message.setSummary("At least one Alarm Asset must be selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
//			return null;
		}
	}

	public void checkboxClickedIsolated(ActionEvent e) {
		if (isolationAlarmAsset.getSubfixIdsSize() > 0) {
			isolationAlarmAsset.setWholeFixture(false);
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
			selectedIsolations = IsolationDAO.getInstance()
					.getIsolationsByAlarmAssetId(
							this.selectedAsset.getAlarmasset_id());
			DataTable dataTable = (DataTable) FacesContext
					.getCurrentInstance()
					.getViewRoot()
					.findComponent(
							"frmAlarmAssetIsolationsPage:tbIsolationList");
			dataTable.setValue(selectedIsolations);
		} else {
			FacesMessage message = new FacesMessage();
			message.setDetail("No Isolation was selected");
			message.setSummary("No Isolation was selected");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		chosenIsolation = new Isolation();
	}

	public List<Asset> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<Asset> assetList) {
		this.assetList = assetList;
	}

	public Asset getSelectedAsset() {
		return selectedAsset;
	}

	public void setSelectedAsset(Asset selectedAsset) {
		this.selectedAsset = selectedAsset;
	}

	public Isolation getSelectedIsolation() {
		return selectedIsolation;
	}

	public void setSelectedIsolation(Isolation selectedIsolation) {
		this.selectedIsolation = selectedIsolation;
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

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public List<SelectItem> getWeekdays() {
		setWeekdays();
		return weekdays;
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

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
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

	public List<AlarmTrap> getAlarmTraps() {
		return alarmTraps;
	}

	public void setAlarmTraps(List<AlarmTrap> alarmTraps) {
		this.alarmTraps = alarmTraps;
	}

	public AlarmAssetIsolation getIsolationAlarmAsset() {
		return isolationAlarmAsset;
	}

	public void setIsolationAlarmAsset(AlarmAssetIsolation isolationAlarmAsset) {
		this.isolationAlarmAsset = isolationAlarmAsset;
	}

	public void setSiteTimeZone(TimeZone siteTimeZone) {
		this.siteTimeZone = siteTimeZone;
	}

	public List<Isolation> getSelectedIsolations() {
		return selectedIsolations;
	}

	public void setSelectedIsolations(List<Isolation> selectedIsolations) {
		this.selectedIsolations = selectedIsolations;
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
			setAlarmTraps(isolatedAlmTraps);
		}
		if (chosenIsolation != null) {
			isolatedAlmAssets = IsolationDAO.getInstance()
					.getSpecificIsolationAlmAssets(
							chosenIsolation.getIsolationId(),
							chosenIsolation.getDepId());
			setIsolationAlarmAsset(isolatedAlmAssets.get(0));
		}
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

	public boolean isShowIsolations() {
		return showIsolations;
	}

	public void setShowIsolations(boolean showIsolations) {
		this.showIsolations = showIsolations;
	}

}

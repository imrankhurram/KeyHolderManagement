package com.nextcontrols.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.nextcontrols.dao.BranchDAO;

public class Isolation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int isolationId;
	private String siteCode;
	private String isolationType;
	private String dayOfWeek;
	private Date startTime;
	private Date endTime;
	private boolean enabled;
	private int userId;
	private Date createdOn;
	private String startHour;
	private String endHour;
	private int depId;
	private String name;
	private String name_more; 
	private String colour;
	private String startDate;
	private String endDate;
	private SimpleDateFormat dateFormat;
	private String username; 
	
	public Isolation(){dateFormat= new SimpleDateFormat("dd/MM/yyyy");}
	
	
	public Isolation(int isolationId,String siteCode,String isolationType,String dayOfWeek,Date startTime,Date endTime,boolean enabled,
			int userId,Date createdOn,String startHour,String endHour,int depId,String name){
		dateFormat= new SimpleDateFormat("dd/MM/yyyy");
				this.isolationId=isolationId;
				this.siteCode=siteCode;
				this.dayOfWeek=dayOfWeek;
				this.startTime=startTime;
				this.endTime=endTime;
				this.isolationType=isolationType;
				this.enabled=enabled;
				this.userId=userId;
				this.createdOn=createdOn;
				this.startHour=startHour;
				this.endHour=endHour;
				this.depId=depId;
				this.name=name;
				this.setStartDate();
				this.setEndDate();
			}
	
	//Added new field for name_more 7 Sep 2011 by Nayyab
	public Isolation(int isolationId,String siteCode,String isolationType,String dayOfWeek,Date startTime,Date endTime,boolean enabled,
	int userId,Date createdOn,String startHour,String endHour,int depId,String name, String more_name){
		dateFormat= new SimpleDateFormat("dd/MM/yyyy");
		this.isolationId=isolationId;
		this.siteCode=siteCode;
		this.dayOfWeek=dayOfWeek;
		this.startTime=startTime;
		this.endTime=endTime;
		this.isolationType=isolationType;
		this.enabled=enabled;
		this.userId=userId;
		this.createdOn=createdOn;
		this.startHour=startHour;
		this.endHour=endHour;
		this.depId=depId;
		this.name=name;
		this.name_more = more_name;
		
		this.setStartDate();
		this.setEndDate();
	}
	//Added by Nayyab Aug 26, 2011 to show the username in constructor
	//Added by Nayyab Sep 07,2011 to show new name column added to Isolation table
	public Isolation(int isolationId,String siteCode,String isolationType,String dayOfWeek,Date startTime,Date endTime,boolean enabled,
			int userId,Date createdOn,String startHour,String endHour,int depId,String name,String uname,String more_name){
		dateFormat= new SimpleDateFormat("dd/MM/yyyy");
				this.isolationId=isolationId;
				this.siteCode=siteCode;
				this.dayOfWeek=dayOfWeek;
				this.startTime=startTime;
				this.endTime=endTime;
				this.isolationType=isolationType;
				this.enabled=enabled;
				this.userId=userId;
				this.createdOn=createdOn;
				this.startHour=startHour;
				this.endHour=endHour;
				this.depId=depId;
				this.name=name;
				this.name_more = more_name;
				this.setStartDate();
				this.setEndDate();
				this.username = uname;
				

			}
	public void setIsolationId(int isolationId) {
		this.isolationId = isolationId;
	}
	public int getIsolationId() {
		return isolationId;
	}
	public void setIsolationType(String isolationType) {
		this.isolationType = isolationType;
	}
	public String getIsolationType() {
		return isolationType;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		this.setStartDate();
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		this.setEndDate();
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setStartHour(String startHour) {
	
		this.startHour = startHour;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getEndHour() {
		if (this.getIsolationType().equals("Permanent")){
			return " ";
		}else{
			return endHour;
		}
	}
	public void setDepId(int depId) {
		this.depId = depId;
	}
	public int getDepId() {
		return depId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	//added by Nayyab Aug 26, 2011 for username
	
	public void setUserName(String uname) {
		this.username = uname;
	}
	public String getUserName() {
		return username;
	}
	
	//Added by Nayyab on Sep 07, 2011 to show new editable column name
	
	public void setNameMore(String more_name) {
		this.name_more = more_name;
	}
	public String getNameMore() {
		return name_more;
	}
	//End adding Nayyab
	public void setColour() {
		int weekday=0;
		this.colour="";
		String endingHour="";
		String endgingMinutes="";
		TimeZone siteTimeZone = BranchDAO.getInstance().getSiteTimezone(getSiteCode());
		Calendar endTimeDate=Calendar.getInstance(siteTimeZone);
		if (this.getDayOfWeek()!=null){
			if (this.getDayOfWeek().equals("Sunday")){
				weekday=1;
			}else if (this.getDayOfWeek().equals("Monday")){
				weekday=2;
			}else if (this.getDayOfWeek().equals("Tuesday")){
				weekday=3;
			}else if (this.getDayOfWeek().equals("Wednesday")){
				weekday=4;
			}else if (this.getDayOfWeek().equals("Thursday")){
				weekday=5;
			}else if (this.getDayOfWeek().equals("Friday")){
				weekday=6;
			}else if (this.getDayOfWeek().equals("Saturday")){
				weekday=7;
			}		
		}
		Calendar currTime=Calendar.getInstance(siteTimeZone);
		Calendar nowDate = Calendar.getInstance();
		nowDate.set(Calendar.HOUR, 0);
		nowDate.set(Calendar.MINUTE, 0);
		nowDate.set(Calendar.SECOND, 0);
		String[] startTime=this.getStartHour().split(":");
		String startingHour=startTime[0];
		String startingMinutes=startTime[1];
		Calendar startTimeDate=Calendar.getInstance(siteTimeZone);
		if (this.startTime!=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.startTime);
			startTimeDate.set(Calendar.DATE, cal.get(Calendar.DATE));
			startTimeDate.set(Calendar.MONTH, cal.get(Calendar.MONTH));
			startTimeDate.set(Calendar.YEAR, cal.get(Calendar.YEAR));
//			startTimeDate.setTime(this.startTime);
		}
		startTimeDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startingHour));
		startTimeDate.set(Calendar.MINUTE, Integer.parseInt(startingMinutes));
		
		if (this.getIsolationType().equals("Permanent")==false){
			String[] endTime=this.getEndHour().split(":");
			endingHour=endTime[0];
			endgingMinutes=endTime[1];
			
			if (this.endTime!=null){
//				endTimeDate.setTime(this.endTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.endTime);
				endTimeDate.set(Calendar.DATE, cal.get(Calendar.DATE));
				endTimeDate.set(Calendar.MONTH, cal.get(Calendar.MONTH));
				endTimeDate.set(Calendar.YEAR, cal.get(Calendar.YEAR));
			}
			endTimeDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endingHour));
			endTimeDate.set(Calendar.MINUTE, Integer.parseInt(endgingMinutes));
		}
		
		if (this.getIsolationType().equals("Once") && (this.getEndTime()!=null)){
			Date temp_date=null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			temp_date=sdf.parse( sdf.format(nowDate.getTime()));
			}
			catch(ParseException pe) {
				pe.printStackTrace();
			}
			if (this.getEndTime().before(temp_date)) {
				this.colour="expired";
			}
			
			
			else{
				if (this.isEnabled()==false){
					this.colour="disabled";
				} 
				
				
				else if (this.getEndTime().compareTo(temp_date)==0){
					if (currTime.compareTo(endTimeDate)>0){
						this.colour="expired";
					}else{
						if ((currTime.compareTo(startTimeDate)>=0)){
							this.colour="active";
						}else{
							this.colour="inactive";
						}
					}
				}else if ((this.getStartTime().compareTo(nowDate.getTime())<=0)&&(this.getEndTime().after(nowDate.getTime()))){
						if ((startTimeDate.compareTo(currTime)<=0) && (endTimeDate.after(currTime))){
							this.colour="active";
						}else if (endTimeDate.compareTo(currTime)==0){
							this.colour="active";
						}else{
							this.colour="inactive";
						}
				}else if (this.getStartTime().compareTo(nowDate.getTime())>0){
					this.colour="inactive";
				}
				
			
			}
		}else if (this.getIsolationType().equals("Weekly")){
			if (this.getStartTime().compareTo(nowDate.getTime())<=0){
					if (this.isEnabled()==false){
						this.colour="disabled";
					}else if ((weekday==Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
							if ((startTimeDate.compareTo(currTime)<=0) && (endTimeDate.after(currTime))){
								this.colour="active";
							}else if (endTimeDate.compareTo(currTime)==0){
								this.colour="active";
							}else{
								this.colour="inactive";
							}
						
					}else if (this.getStartTime().compareTo(nowDate.getTime())>0){
						this.colour="active";
					}else{
						this.colour="inactive";
					}
				}else{
					this.colour="inactive";
				}
		}else if (this.getIsolationType().equals("Permanent")){
			if (this.getStartTime().compareTo(nowDate.getTime())<=0){
				if (this.isEnabled()==false){
					this.colour="disabled";
				}else if(startTimeDate.compareTo(currTime)<=0){
					this.colour="active";
				}else{
					this.colour="inactive";
				}
			}else{
				this.colour="inactive";
			}
		}
		
		else{
			if (this.getStartTime().compareTo(nowDate.getTime())<=0){
				if (this.isEnabled()==false){
					this.colour="disabled";
				}else if (this.getStartTime().compareTo(nowDate.getTime())<=0){
						if ((startTimeDate.compareTo(currTime)<=0) && (endTimeDate.after(currTime))){
							this.colour="active";
						}else if (endTimeDate.compareTo(currTime)==0){
							this.colour="active";
						}else{
							this.colour="inactive";
						}
					
				}else if (this.getStartTime().compareTo(nowDate.getTime())>0){
					this.colour="inactive";
				}
			}else{
				this.colour="inactive";
			}
		}
	}

	public String getColour() {
		setColour(); 
		return colour;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setStartDate() {
		if (this.startTime!=null){
			this.startDate = dateFormat.format(this.startTime);
		}
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate() {
		if (this.endTime!=null){
			this.endDate = dateFormat.format(this.endTime);
		}
		else {
			this.endDate = null;
		}
	}

	public String getEndDate() {
		return endDate;
	}
	

}

package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.dao.UserActivityDAO;
import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.dao.UserDAO;
import com.nextcontrols.dao.WebsiteDAO;
import com.nextcontrols.domain.User;
import com.nextcontrols.domain.UserAudit;
import com.nextcontrols.domain.Website;
import com.nextcontrols.utility.ServiceProperties;
import com.nextcontrols.utility.UserRegistry;

@ManagedBean(name="userInfo")
@SessionScoped
public class UserInfoPageBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private static boolean wrongDetails=false;
	static int sec =0;
    static int min =0;
    static int hr = 0;
    private List<String> phoneExtensions;  
	private String mUserName;  //operator's username
	private String mPassword;  //operator's password
	private String mFirstName; //operator's first name
	private String mLastName; //operator's last name
	private String mEmail; //operator's email
	private String mStatus;
	private String userType; //operator's user type
	private String userBureauType; //operator's bureau type
	private String extension;  ////operator's phone extensions
	private String checkVar;
    private String checkStatusH;
	private String checkReciversOk;
	private boolean facesMessage;
	private boolean flashFlag;
	boolean shown;
    int stampHour;
    int stampSec;
    int stampMin;
    Calendar date1; 
	SimpleDateFormat dateFormat;
	////Live Statistics//////
	private String currentTime;
	private String avgResponseTime;
	private int userId;
	private int dueActivities;
	private int unresolvedEvents;
	private int almsPerHour;
	private int almsLastYear;
	private int actionedAlms;
	private int overdueSize;
	private int allAlarmsSize;
	private int usersOnlineCount;
	private boolean areManyWebsites;
	////////////////////////////
	
	public UserInfoPageBean() {
		phoneExtensions=new ArrayList<String>(); 
		facesMessage=false;
		shown= false;
		areManyWebsites=false;
		checkVar="";
		date1 = Calendar.getInstance();
		dateFormat = new SimpleDateFormat("dd MMMMMMM yyyy HH:mm");
	}
	public void setCheckReciversOk() {
//		checkReceiverActivie();
		if(flashFlag== false) {
//			this.alarmReceiver=null;
		}
	}
	public String getCheckReciversOk() {
		setCheckReciversOk();
		if(flashFlag== false) {
//			this.alarmReceiver=null;
		}
		return checkReciversOk;
	}
	public void setCheckVar(String checkVar) {
    	this.checkVar=checkVar;
    }
    public String getCheckVar() {
//    	checkReceiverActivie();
    	if(flashFlag== false){
//    		this.alarmReceiver=null;
    	}
    	return checkVar;
    }
    public void setCheckStatusH(String checkStatusH) {
    	this.checkStatusH=checkStatusH;
    }
    public String getCheckStatusH() {
//    	checkReceiverActivie();
    		if(flashFlag==true) {
    			FacesMessage facesMsg = new FacesMessage();
    			facesMsg.setDetail("Alarm Receiver Down");
    			facesMsg.setSummary("Alarm Receiver Down");
    			facesMsg.setSeverity(FacesMessage.SEVERITY_ERROR);
    			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    		}else {
//    				this.alarmReceiver=null;
    		}
    	return checkStatusH;
    }

	
	public boolean isAreManyWebsites() {
		return areManyWebsites;
	}
	public void setAreManyWebsites(boolean areManyWebsites) {
		this.areManyWebsites = areManyWebsites;
	}
	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String mFirstName) {
		this.mFirstName = mFirstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String mLastName) {
		this.mLastName = mLastName;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getStatus() {
		return mStatus;
	}

	public void setStatus(String mStatus) {
		this.mStatus = mStatus;
	}

	public String getUserName() {
		return mUserName;
	}
	
	public void setUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}
	
	public void setUserId(int userId){
		this.userId=userId;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserType(String userType){
		this.userType=userType;
	}
	
	public String getUserType(){
		return userType;
	}
	
	public void setUserBureauType(String userBureauType) {
		this.userBureauType = userBureauType;
	}

	public String getUserBureauType() {
		return userBureauType;
	}
	
	public void updateUsersActivity1() {
		Calendar cal = Calendar.getInstance();
        stampSec =  cal.get(Calendar.SECOND);
        stampMin = cal.get(Calendar.MINUTE);
        stampHour = cal.get(Calendar.HOUR_OF_DAY);
        if((cal.getTime().getTime()-date1.getTimeInMillis())>=(5*60*1000)) {
//			checkReceiverActivie();			
		}
	}
	

	
	public void updateUsersActivity(){   //overloading
		/*ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		
		Calendar calendar=Calendar.getInstance();
		Date now=calendar.getTime();
		
		UserActivityDAO updateUserActivity=new UserActivityDAO();*/
		/*this.setAlmsPerHour();
		this.setActionedAlms();
		this.setAlmsLastYear();
		this.setAvgResponseTime();*/
		//this.checkFoldersForError();
	}


	public int getDueActivities() {
//		this.setDueActivities();
		return dueActivities;
	}

	public void setExtension(String extension) {
		this.extension = extension;
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		session.setAttribute("extension", extension);
	}

	public String getExtension() {
		return extension;
	}

	public List<String> completePhoneExtension(String query){
		List<String> extensionsList=new ArrayList<String>();
		String allExtensions=ServiceProperties.getInstance().getPhoneExtensions();
		String [] separatedExtensions=allExtensions.split(",");
		for (int i=0;i<=separatedExtensions.length-1;i++){
			if (separatedExtensions[i].indexOf(query)!=-1){
				extensionsList.add(separatedExtensions[i]);
			}
		}
		return extensionsList;
	}

	public String loginAction() {
        String action = null;
        
        if ( mUserName.equalsIgnoreCase("user") && mPassword.equalsIgnoreCase("password") ){
            action = "logon_failure";
        }else{
        	UserRegistry.getInstance().addUser(this);
            action = "logon_success";
        }
        return action;
    }
	
	public String action()
	  {
	    FacesContext context = FacesContext.getCurrentInstance();
	    FacesMessage message = new FacesMessage("Clicked on User: " + getUserName());
	    context.addMessage(null, message);
	    return null;
	  }
	
	public String add()
	  {
	    return "success";
	  }
	
	public void idleMonitor(){
		if (this.getUserId()!=0){
			UserAuditDAO.getInstance().insertUserAudit(new UserAudit(this.getUserId(),new Timestamp(Calendar.getInstance().getTime().getTime()),"UserIdle","The user has been inactive for 15 minutes",null));
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false); 
			session.removeAttribute("user");
			session.invalidate();
		}
	}
	
	public boolean checkUserName(){
		if (this.getUserName()==null){
			return false;
		}else{
			return true;
		}
	}
	
	public void dbInsertLogin(){
		UserActivityDAO.getInstance().userLogin(getUserName(), new Timestamp(Calendar.getInstance().getTime().getTime()) , new Timestamp(Calendar.getInstance().getTime().getTime()), this.getUserType());
	}
	
	public void loginAudit(){
//		UserAuditDAO.getInstance().insertUserAudit(new UserAudit(this.getUserId(),new Timestamp(Calendar.getInstance().getTime().getTime()),"UserLogin","The user has logged in",null));
	}
	
	public void extensionSelected(){
		if ((this.getUserName()!=null)&&(this.getPassword()!=null)){
			processLogin();
		}
	}
	
	/**
	 * login function
	 * @return
	 */
	public String processLogin(){
		if ((UserDAO.getInstance().correctPassword(this.getUserName(), encryptPassword(this.getPassword()))==true)){
			User currentUser=UserDAO.getInstance().getSpecificUser(this.getUserName());
			this.setUserId(currentUser.getUserId());
			this.setFirstName(currentUser.getFirstName());
			this.setLastName(currentUser.getLastName());
			this.setUserType(currentUser.getUserWebType());
			this.setUserBureauType(currentUser.getUserBureauType());
			this.dbInsertLogin();
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false);
			session.setAttribute("user", this.getUserName()); 
			session.setAttribute("userId", this.getUserId());
			session.setAttribute("name", this.getFirstName() + " " + this.getLastName());
			this.updateUsersActivity();
			this.loginAudit();
			wrongDetails=false;
			WebsitesPageBean websitesPageBean = (WebsitesPageBean) session
					.getAttribute("websites");
			if(websitesPageBean!=null){
				session.removeAttribute("websites");
				websitesPageBean.initializeWebsites();
				session.setAttribute("websites", websitesPageBean);
			}else{
				websitesPageBean=new WebsitesPageBean();
				websitesPageBean.initializeWebsites();
				session.setAttribute("websites", websitesPageBean);
			}
//			List<Website> websites=WebsiteDAO.getInstance().getAssignedWebsites(22);
//			for(Website website:websitesPageBean.getWebsitesList()){
//				System.out.println("website id: "+website.getWebsiteId()+"--website name: "+website.getName());
//			}
			if(websitesPageBean.getWebsitesList().size()==1){
				DepartmentsPageBean departmentsPageBean = (DepartmentsPageBean) session
						.getAttribute("departments");
				
				if (departmentsPageBean != null) {
					session.removeAttribute("departments");
					List<String> branchCodes=WebsiteDAO.getInstance().getBranchCodes(websitesPageBean.getWebsitesList().get(0).getWebsiteId());
//					System.out.println("branch code: "+branchCodes.get(0));
					departmentsPageBean.initializeDepartments(branchCodes);
					session.setAttribute("departments", departmentsPageBean);
				}else{
					departmentsPageBean=new DepartmentsPageBean();
					List<String> branchCodes=WebsiteDAO.getInstance().getBranchCodes(websitesPageBean.getWebsitesList().get(0).getWebsiteId());
//					System.out.println("branch code: "+branchCodes.get(0));
					departmentsPageBean.initializeDepartments(branchCodes);
					session.setAttribute("departments", departmentsPageBean);	
				}
				areManyWebsites=false;
				session.setAttribute("areManyWebsites", areManyWebsites);
				return "DepartmentsPage?faces-redirect=true";
			}
			areManyWebsites=true;
			session.setAttribute("areManyWebsites", areManyWebsites);
			return "WebsitesPage?faces-redirect=true";
//			return "DepartmentsPage?faces-redirect=true";
			
		}else{
			wrongDetails=true;
			return "login?faces-redirect=true";
		}
	 }
	
	/**
	 * LH login function
	 * @return
	 */
	public String processLHLogin(){
		if ((UserDAO.getInstance().correctPassword(this.getUserName(), encryptPassword(this.getPassword()))==true)){
			User currentUser=UserDAO.getInstance().getSpecificUser(this.getUserName());
			this.setUserId(currentUser.getUserId());
			this.setFirstName(currentUser.getFirstName());
			this.setLastName(currentUser.getLastName());
			this.setUserType(currentUser.getUserWebType());
			this.setUserBureauType(currentUser.getUserBureauType());
			this.dbInsertLogin();
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
			HttpSession session = (HttpSession)ectx.getSession(false);
			session.setAttribute("user", this.getUserName()); 
			session.setAttribute("userId", this.getUserId());
			session.setAttribute("name", this.getFirstName() + " " + this.getLastName());
			this.updateUsersActivity();
			this.loginAudit();
			wrongDetails=false;
			if (this.getUserBureauType().equals("LHCustomer")){
				return "LHCustomerAreasPage?faces-redirect=true";
			}else{
				return "LHLogin?faces-redirect=true";
			}
		}else{
			wrongDetails=true;
			return "login?faces-redirect=true";
		}
	 }
	

	/**
	 * encrypts user's password
	 * @param password
	 * @return
	 */
	private String encryptPassword(String password) {
		//encrypting the password before saving it
		byte[] defaultBytes = password.getBytes();
        StringBuffer hexString = new StringBuffer();
        try{
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
                           for (int i=0;i<messageDigest.length;i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length()==1)
                    hexString.append('0');
                hexString.append(hex);
                }
            }catch (Exception e){e.printStackTrace();}
		password = hexString.toString();
		return password;
	}
	
	/**
	 * function which resets the user's password
	 * @return
	 */
	public String resetPassword(){
		String newPassword=generatePassword();
		String message="Your password for the Remote Monitoring Bureau was reset.\nYour new password is: " + newPassword;
		String emailAddr=UserDAO.getInstance().getUsersEmail(this.getUserName());
		fn_SendEmail(1, emailAddr, message, "PasswordGenerator");
		wrongDetails=true;
		return "login?faces-redirect=true";
	}
	
	/**
	 * generate a new user's password
	 * @return
	 */
	private String generatePassword(){
		String strNewPass= "";
		String encryptedPass="";
	    int whatsNext;
	    String characters="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.,;[]#=-+*&^%$£";

	    for(int i=0;i<12;i++)
	    {
	    	whatsNext = (int) Math.floor(Math.random() * characters.length());
	    	strNewPass=strNewPass + characters.charAt(whatsNext);
	    }
	    encryptedPass=encryptPassword(strNewPass);
	    if (UserDAO.getInstance().passwordExists(encryptedPass)==true){
	    	strNewPass=this.generatePassword();
	    }
	    UserDAO.getInstance().resetPassword(this.getUserName(), encryptedPass);
	   return strNewPass;
	}
	
	private void fn_SendEmail(int pSendFlag, String pEmails,
	           String message, String pUserName) {
//		 IBureauMessagingService service = TextMessageServiceBean.getInstance();
//			try {
//				 service.fn_sendEmailMessage(message, pEmails.split(","), "", pUserName).equalsIgnoreCase("OK");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
	}

	public void setWrongDetails(boolean WrongDetails) {
		wrongDetails = WrongDetails;
	}

	public boolean isWrongDetails() {
		return wrongDetails;
	}
	
	
	public void setActionedAlms() {
//		this.actionedAlms = ActiveAlarmDAO.getInstance().actionedAlmCount();
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (requestContext!=null){
			requestContext.update("AlmsActioned");
		}
	}

	public int getActionedAlms() {
		return actionedAlms;
	}

	public void setAvgResponseTime() {
//		int responseTime= ActiveAlarmDAO.getInstance().averageResponseTime();
//		int hours = responseTime / 3600;
//		int remainder = responseTime % 3600;
//		int minutes = remainder / 60;
//		int seconds = remainder % 60;
//		this.avgResponseTime = ( (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds< 10 ? "0" : "") + seconds );
//		
//		RequestContext requestContext = RequestContext.getCurrentInstance();
//		if (requestContext!=null){
//			requestContext.update("AvgAlmsResponsetime");
//		}
	}

	public String getAvgResponseTime() {
		return avgResponseTime;
	}

	public void setAlmsPerHour() {
		//ActiveAlarmDAO activeAlarmDAO = new ActiveAlarmDAO();
		//this.almsPerHour = activeAlarmDAO.receivedAlmsPerHour();
	}

	public int getAlmsPerHour() {
		return almsPerHour;
	}
	
	public void setAlmsLastYear() {
//		this.almsLastYear = ActiveAlarmDAO.getInstance().receivedAlmsLastYear();
	}

	public int getAlmsLastYear() {
		return almsLastYear;
	}

	public void setPhoneExtensions() {
		String allExtensions=ServiceProperties.getInstance().getPhoneExtensions();
		String [] separatedExtensions=allExtensions.split(",");
		for (int i=0;i<=separatedExtensions.length-1;i++){
				phoneExtensions.add(separatedExtensions[i]);
			}
	}

	public List<String> getPhoneExtensions() {
		setPhoneExtensions();
		return phoneExtensions;
	}

	public void setUsersOnlineCount() {
		/*UserActivityDAO activityDB = new UserActivityDAO();
		this.usersOnlineCount = activityDB.getNumberUsersOnline();*/
	}

	public int getUsersOnlineCount() {
		setUsersOnlineCount();
		return usersOnlineCount;
	}

	public void setCurrentTime() {
		this.currentTime = dateFormat.format(Calendar.getInstance().getTime());
	}

	public String getCurrentTime() {
		setCurrentTime();
		return currentTime;
		
	}

	public void setUnresolvedEvents() {
//		this.unresolvedEvents = EventDAO.getInstance().getNumberUnresolvedEvents();
	}

	public int getUnresolvedEvents() {
		setUnresolvedEvents();
		return unresolvedEvents;
	}

	public void setOverdueSize() {
//		this.overdueSize = ActiveAlarmDAO.getInstance().getOverdueAlarmsCount();
	}

	public int getOverdueSize() {
		//setOverdueSize();
		return overdueSize;
	}

	public void setAllAlarmsSize() {
//		this.allAlarmsSize = ActiveAlarmDAO.getInstance().allActiveAlarmsCount();
	}

	public int getAllAlarmsSize() {
		//setAllAlarmsSize();
		return allAlarmsSize;
	}
	
	/**
	 * calls the check_folders stored procedure
	 * if there are errors displays a growl message
	 * for each error
	 */
	public void checkFoldersForError(){
/*		FacesContext context=FacesContext.getCurrentInstance();
		ActiveAlarmDAO alarmDB = new ActiveAlarmDAO();
		String message=null;
		message = alarmDB.checkFolders();
		String[] errors = message.split("\\|");
		
		Growl error= (Growl) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmmenuleft:growl");
		if (errors.length>1){
			
				for (int i=1;i<=errors.length-1;i++){
					System.out.println("Erros contents afetr split: " + errors[i]);
					System.out.println("context message.. "+ context.getMessageList().size());
					System.out.println("Flag's Value.. "+ shown);
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", errors[i]));
					System.out.println("context message..after add  Size.."+ context.getMessageList().size());
					
				}
				setFacesMessage(true);
				error.setRendered(true);
			
		}else{
			error.setRendered(false);
		}*/
		
	}

	public void setFacesMessage(boolean facesMessage) {
		this.facesMessage = facesMessage;
	}

	public boolean getFacesMessage() {
		return facesMessage;
	}
	public void setFlashFlag(boolean flashFlag) {
		this.flashFlag=flashFlag;
	}
	public boolean isFlashFlag() {
		return flashFlag;
	}
}

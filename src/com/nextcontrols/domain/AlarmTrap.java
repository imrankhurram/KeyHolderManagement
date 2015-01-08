package com.nextcontrols.domain;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.nextcontrols.utility.ServiceProperties;

public class AlarmTrap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AlarmTrap instance;
	private boolean enabled;
	private boolean updateEnabled;
	private boolean allowHold;
	private boolean allowDial;
	private boolean allowClear;
	private boolean autoCheckEnabled;
	private boolean chosen;
	private int alarmTrapId;
	private int responseTime;
	private int maxOperatorHoldTime;
	private int priority;
	private int workflowId;
	private int stock_valuation_id;
	private int clearThreshold;
	private int dep_id;
//	private Workflow workflow;
//	private StockValue stockValuation;
//	private Site site;
	private String alarmTrapType;
	private String siteCode;
	private String name;
	private String controllerName;
	private boolean emailEnabled;
	private boolean smsEnabled;
	private String emailListString="";
	private String mobileListString="";
	private String emailFormat;

	public static AlarmTrap getInstance() { 
        if (instance != null) {
            return instance; 
        } else {
            return new AlarmTrap();
        }
    }
	
	public AlarmTrap(){}
	
	public AlarmTrap(int palarmTrapId, boolean penabled, String pName, int responseTime, int pmaxOperatorHoldTime, boolean pallowHold,
			int ppriority, int pworkflow, int pstockValuation, boolean pallowDial, boolean pallowClear,
			int pclearThreshold, String palarmTrapType, boolean pautoCheckEnabled,String pSitecode, String pControllerName,int dep_id){
		
		this.alarmTrapId = palarmTrapId;
		this.name = pName;
		this.enabled = penabled;
		this.updateEnabled=penabled;
		this.responseTime = responseTime;
		this.maxOperatorHoldTime = pmaxOperatorHoldTime;
		this.allowHold = pallowHold;
		this.priority = ppriority;
		this.workflowId = pworkflow;
		this.stock_valuation_id = pstockValuation;
		this.allowDial = pallowDial;
		this.allowClear = pallowClear;
		this.clearThreshold = pclearThreshold;
		this.alarmTrapType = palarmTrapType;
		this.autoCheckEnabled = pautoCheckEnabled;
		this.siteCode=pSitecode;
		this.controllerName = pControllerName;
		this.dep_id=dep_id;
		this.chosen=false;
		
}
	public AlarmTrap(int palarmTrapId, boolean penabled, String pName, int responseTime, int pmaxOperatorHoldTime, boolean pallowHold,
			int ppriority, int pworkflow, int pstockValuation, boolean pallowDial, boolean pallowClear,
			int pclearThreshold, String palarmTrapType, boolean pautoCheckEnabled,String pSitecode, String pControllerName,int dep_id,
			 String emailListString, boolean emailEnabled, String mobileListString ,  boolean smsEnabled, String emailFormat){
		
		this.alarmTrapId = palarmTrapId;
		this.name = pName;
		this.enabled = penabled;
		this.updateEnabled=penabled;
		this.responseTime = responseTime;
		this.maxOperatorHoldTime = pmaxOperatorHoldTime;
		this.allowHold = pallowHold;
		this.priority = ppriority;
		this.workflowId = pworkflow;
		this.stock_valuation_id = pstockValuation;
		this.allowDial = pallowDial;
		this.allowClear = pallowClear;
		this.clearThreshold = pclearThreshold;
		this.alarmTrapType = palarmTrapType;
		this.autoCheckEnabled = pautoCheckEnabled;
		this.siteCode=pSitecode;
		this.controllerName = pControllerName;
		this.dep_id=dep_id;
		this.chosen=false;
		this.emailEnabled = emailEnabled;
		this.emailListString = emailListString;
		this.smsEnabled = smsEnabled;
		this.mobileListString = mobileListString;
		this.emailFormat = emailFormat;
}
	
	public int getAlarmTrapId() {
		return alarmTrapId;
	}

	public void setAlarmTrapId(int alarmTrapId) {
		this.alarmTrapId = alarmTrapId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public int getMaxOperatorHoldTime() {
		return maxOperatorHoldTime;
	}

	public void setMaxOperatorHoldTime(int maxOperatorHoldTime) {
		this.maxOperatorHoldTime = maxOperatorHoldTime;
	}

	public boolean isAllowHold() {
		return allowHold;
	}

	public void setAllowHold(boolean allowHold) {
		this.allowHold = allowHold;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
//
//	public Workflow getWorkflow() {
//		return workflow;
//	}
//
//	public void setWorkflow(Workflow workflow) {
//		this.workflow = workflow;
//	}
//
//	public StockValue getStockValuation() {
//		return stockValuation;
//	}
//
//	public void setStockValuation(StockValue stockValuation) {
//		this.stockValuation = stockValuation;
//	}
//
//	public Site getSite() {
//		return site;
//	}
//
//	public void setSite(Site site) {
//		this.site = site;
//	}

	public boolean isAllowDial() {
		return allowDial;
	}

	public void setAllowDial(boolean allowDial) {
		this.allowDial = allowDial;
	}

	public boolean isAllowClear() {
		return allowClear;
	}

	public void setAllowClear(boolean allowClear) {
		this.allowClear = allowClear;
	}

	public int getClearThreshold() {
		return clearThreshold;
	}

	public void setClearThreshold(int clearThreshold) {
		this.clearThreshold = clearThreshold;
	}

	public String getAlarmTrapType() {
		return alarmTrapType;
	}

	public void setAlarmTrapType(String alarmTrapType) {
		this.alarmTrapType = alarmTrapType;
	}

	public boolean isAutoCheckEnabled() {
		return autoCheckEnabled;
	}

	public void setAutoCheckEnabled(boolean autoCheckEnabled) {
		this.autoCheckEnabled = autoCheckEnabled;
	}
	
	public void setSiteCode(String sitecode){
		this.siteCode=sitecode;
	}
	
	public String getSiteCode(){
		return siteCode;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}
	
	public AlarmTrap getAlarmTrap(String pAlarmTrapKey) {
	       Connection connection = null;
	       Statement statement = null;
	       ResultSet result = null;
	       
	       String [] keyparts = pAlarmTrapKey.split("_");
	       String sql = "SELECT [alarm_trap_id], " + "[enabled]"
	               + ",[response_time]" + ",[maxoperatorhold_time]"
	               + ",[allow_hold]" + ",[priority]" + ",[workflow_id]"
	               + ",[stock_valuation_id]" + ",[allow_dial]" + ",[allow_clear]"
	               + ",[clear_threshold]" + ",[autocheck_enabled]"
	               + " FROM [Alarming].[AlarmTrap]"
	               + " WHERE [alarmtrap_type] = '" + keyparts[1] + "'"
	               + " AND [site_code] = '" + keyparts[0] + "'";
	       AlarmTrap at = new AlarmTrap();
	       try {
	    	   connection = ServiceProperties.getInstance().getSQLConnection();
	           statement = connection.createStatement();
	           result = statement.executeQuery(sql);
	           while (result.next()) {
	               at.setAlarmTrapId(result.getInt("alarm_trap_id"));
	               at.setAlarmTrapType(keyparts[1]);
	               at.setAllowClear(result.getBoolean("allow_clear"));
	               at.setAllowDial(result.getBoolean("allow_dial"));
	               at.setAllowHold(result.getBoolean("allow_hold"));
	               at.setClearThreshold(result.getInt("clear_threshold"));
	               at.setEnabled(result.getBoolean("enabled"));
	               at.setMaxOperatorHoldTime(result
	                       .getInt("maxoperatorhold_time"));
	               at.setPriority(result.getInt("priority"));
	               at.setResponseTime(result.getInt("response_time"));
	               at.setAutoCheckEnabled(result.getBoolean("autocheck_enabled"));
	               at.setSiteCode(keyparts[0]);
	           }

	       } catch (SQLException e) {
	           e.printStackTrace();
	       } finally {
	           try {
	               result.close();
	               statement.close();
	           } catch (SQLException e) {
	               e.printStackTrace();
	           }
	           result = null;
	           statement = null;
	           connection = null;
	       }
	       return at;
	}

	public void setDep_id(int dep_id) {
		this.dep_id = dep_id;
	}

	public int getDep_id() {
		return dep_id;
	}

	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}

	public int getWorkflowId() {
		return workflowId;
	}

	public void setStock_valuation_id(int stock_valuation_id) {
		this.stock_valuation_id = stock_valuation_id;
	}

	public int getStock_valuation_id() {
		return stock_valuation_id;
	}

	public void setUpdateEnabled(boolean updateEnabled) {
		this.updateEnabled = updateEnabled;
	}

	public boolean isUpdateEnabled() {
		return updateEnabled;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	public boolean isChosen() {
		return chosen;
	}

	public boolean isEmailEnabled() {
		return emailEnabled;
	}

	public void setEmailEnabled(boolean emailEnabled) {
		this.emailEnabled = emailEnabled;
	}

	public boolean isSmsEnabled() {
		return smsEnabled;
	}

	public void setSmsEnabled(boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	public String getEmailListString() {
		return emailListString;
	}

	public void setEmailListString(String emailListString) {
		this.emailListString = emailListString;
	}
	public String getMobileListString() {
		return mobileListString;
	}
	public void setMobileListString(String mobileListString) {
		this.mobileListString = mobileListString;
	}

	public String getEmailFormat() {
		return emailFormat;
	}

	public void setEmailFormat(String emailFormat) {
		this.emailFormat = emailFormat;
	}
	
	
}

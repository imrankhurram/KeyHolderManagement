package com.nextcontrols.dao;

import java.util.List;

import com.nextcontrols.domain.AlarmTrap;


public interface IAlarmTrapDAO {
//	public List<AlarmTrap> getAlarmTraps(String pSiteCode);
	public List<AlarmTrap> getAlarmTraps(int dep_id);
	public List<AlarmTrap> getAlarmTrapsForAsset(int assetId);
//	public List<AlarmTrap> getGlobalAlarmTraps();
//	public void addDepartmentAlarmTrap(String name,boolean enabled, int responseTime, int maxOperatorHoldTime, boolean allowHold,
//			int priority, String workflow_id, String stock_valuation_id,String sitecode, boolean allowDial, boolean allowClear,
//			int clearThreshold, String alarmTrapType, boolean autoCheckEnabled, String controllerName,int dep_id, String emailAlert, boolean allowEmail, String smsAlert,
//			boolean allowSms, String workflowType, String emailFormat);
//	public void addGlobalAlarmTrap(String name,boolean enabled, int responseTime, int maxOperatorHoldTime, boolean allowHold,
//			int priority, String workflow_id, String stock_valuation_id, boolean allowDial, boolean allowClear,
//			int clearThreshold, String alarmTrapType, boolean autoCheckEnabled, String controllerName
//			, String alertEmail, boolean allowEmail, String alertSms, boolean allowSms, String workflowType, String emailFormat);
//	public void modifyDepartmentAlarmTrap(int alarm_trap_id,String name,boolean enabled, int responseTime, int maxOperatorHoldTime, boolean allowHold,
//			int priority, boolean allowDial, boolean allowClear,int clearThreshold,String alarmTrapType, boolean autoCheckEnabled, String controllerName
//			, boolean emailChecked, String emailList, boolean smsChecked, String mobileList, String emailFormat);
//	public int deleteAlarmTrap(int alarm_trap_id);//CHANGED BY Nayyab 19-Sep2011
//	public void updateAlarmTrapActivity(int alarm_trap_id,boolean enabled);
//	public List<String> getAlarmTrapType(String query);
//	public int getAlarmTrapDepId(int alarm_trap_id);
//	public AlarmTrap getAlarmTrap(Connection dbConn,int alarm_trap_id);
//	public List<String> getAlarmTypes();
}

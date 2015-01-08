package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.domain.AlarmTrap;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class AlarmTrapDAO implements IAlarmTrapDAO, Serializable{

	private static final long serialVersionUID = 1L;
	private static IAlarmTrapDAO instance;
	public static IAlarmTrapDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new AlarmTrapDAO();
		}
	}
	public static void setInstance(IAlarmTrapDAO inst) {
		instance=inst;
	}
	private AlarmTrapDAO() {}
	
//	@Override
//	public List<AlarmTrap> getAlarmTraps(String pSiteCode){
//		List<AlarmTrap> alarmtraps=new ArrayList<AlarmTrap>();
//		Connection dbConn=null;
//		Statement stmnt=null;
//		ResultSet result=null;
//		String query="SELECT * FROM `bureauv2alarms`.`alarming_alarmtrap` where site_code = '"+pSiteCode+"' ORDER BY `name` asc;";
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()){
//				AlarmTrap db_alarmtrap=new AlarmTrap(result.getInt("alarm_trap_id"),result.getBoolean("enabled"),result.getString("name"),
//						result.getInt("response_time"),result.getInt("maxoperatorhold_time"),result.getBoolean("allow_hold"),result.getInt("priority"),
//						result.getInt("workflow_id"),0,result.getBoolean("allow_dial"),result.getBoolean("allow_clear"),result.getInt("clear_threshold"),
//						result.getString("alarmtrap_type"),result.getBoolean("autocheck_enabled"),result.getString("site_code"),result.getString("controller_name"),
//						result.getInt("dep_id"));
//				alarmtraps.add(db_alarmtrap);
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//			result=null;
//		}
//		return alarmtraps;
//	}
//	
	@Override
	public List<AlarmTrap> getAlarmTraps(int dep_id) {
		List<AlarmTrap> alarmtraps=new ArrayList<AlarmTrap>();
		Connection dbConn=null;
		Statement stmnt=null;
		ResultSet result=null;
		String query="SELECT * FROM `bureauv2alarms`.`alarming_alarmtrap` where `dep_id` = '"+dep_id+"' ORDER BY `name` asc;";
		System.out.println("query: "+query);
		try {
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()){
				AlarmTrap db_alarmtrap=new AlarmTrap(result.getInt("alarm_trap_id"),result.getBoolean("enabled"),result.getString("name"),
						result.getInt("response_time"),result.getInt("maxoperatorhold_time"),result.getBoolean("allow_hold"),result.getInt("priority"),
						result.getInt("workflow_id"),0,result.getBoolean("allow_dial"),result.getBoolean("allow_clear"),result.getInt("clear_threshold"),
						result.getString("alarmtrap_type"),result.getBoolean("autocheck_enabled"),result.getString("site_code"),result.getString("controller_name"),dep_id
						, result.getString("emails"), result.getBoolean("allow_email"), result.getString("mobile"), result.getBoolean("allow_sms"), result.getString("email_format"));
				alarmtraps.add(db_alarmtrap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
				dbConn=null;
				stmnt=null;
				result=null;
		}
		return alarmtraps;
	}
	@Override
	public List<AlarmTrap> getAlarmTrapsForAsset(int assetId) {
		List<AlarmTrap> alarmtraps=new ArrayList<AlarmTrap>();
		Connection dbConn=null;
		Statement stmnt=null;
		ResultSet result=null;
		String query="SELECT * FROM bureauv2alarms.alarming_alarmassets aa inner join asset a on aa.alarmasset_id=a.alarmasset_id inner join alarming_alarmtrap at on aa.dep_id=at.dep_id where a.asset_id="+assetId+" ORDER BY `name` asc;";
		System.out.println("query: "+query);
		try {
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()){
				AlarmTrap db_alarmtrap=new AlarmTrap(result.getInt("alarm_trap_id"),result.getBoolean("enabled"),result.getString("name"),
						result.getInt("response_time"),result.getInt("maxoperatorhold_time"),result.getBoolean("allow_hold"),result.getInt("priority"),
						result.getInt("workflow_id"),0,result.getBoolean("allow_dial"),result.getBoolean("allow_clear"),result.getInt("clear_threshold"),
						result.getString("alarmtrap_type"),result.getBoolean("autocheck_enabled"),result.getString("site_code"),result.getString("controller_name"),result.getInt("dep_id")
						, result.getString("emails"), result.getBoolean("allow_email"), result.getString("mobile"), result.getBoolean("allow_sms"), result.getString("email_format"));
				alarmtraps.add(db_alarmtrap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
				dbConn=null;
				stmnt=null;
				result=null;
		}
		
		return alarmtraps;
	}
	
//	@Override
//	public void addDepartmentAlarmTrap(String name, boolean enabled,
//			int responseTime, int maxOperatorHoldTime, boolean allowHold,
//			int priority, String workflow_id, String stock_valuation_id,
//			String sitecode, boolean allowDial, boolean allowClear, int clearThreshold,
//			String alarmTrapType, boolean autoCheckEnabled, String controllerName,
//			int dep_id,  String emailAlert, boolean allowEmail, String smsAlert, boolean allowSms, String workflowType, String emailFormat) {
//
//		String query = "CALL bureauv2alarms.`create_workflows_alarmtraps_web`(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
//		Connection dbConn = null;
//		PreparedStatement stmnt = null;
//		try {
//			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = dbConn.prepareStatement(query);
//			stmnt.setString(1, name);
//			stmnt.setBoolean(2, enabled);
//			stmnt.setInt(3, responseTime);
//			stmnt.setInt(4, maxOperatorHoldTime);
//			stmnt.setBoolean(5, allowHold);
//			stmnt.setInt(6, priority);
//			stmnt.setString(7, sitecode);
//			stmnt.setBoolean(8, allowDial);
//			stmnt.setBoolean(9, allowClear);
//			stmnt.setInt(10, clearThreshold);
//			stmnt.setString(11, alarmTrapType);
//			stmnt.setBoolean(12, autoCheckEnabled);
//			stmnt.setString(13, controllerName);
//			stmnt.setInt(14, dep_id);
//			if(emailAlert!=null && emailAlert.isEmpty()) {
//				stmnt.setNull(15, Types.VARCHAR);
//			}
//			else {
//				stmnt.setString(15, emailAlert);
//			}
//			stmnt.setBoolean(16, allowEmail);
//			if(smsAlert!=null && smsAlert.isEmpty()){
//				stmnt.setNull(17, Types.VARCHAR);
//			}
//			else {
//				stmnt.setString(17, smsAlert);
//			}
//			stmnt.setBoolean(18, allowSms);
//			stmnt.setString(19, workflowType);
//			stmnt.setString(20, emailFormat);
//			stmnt.executeQuery();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn = null;
//			stmnt = null;
//		}
//	}
//	
//	@Override
//	public void modifyDepartmentAlarmTrap(int alarm_trap_id,String name, boolean enabled,
//			int responseTime, int maxOperatorHoldTime, boolean allowHold,
//			int priority, boolean allowDial, boolean allowClear, int clearThreshold,
//			String alarmTrapType, boolean autoCheckEnabled, String controllerName, boolean emailChecked, String emailList, boolean smsChecked, String mobileList, String emailFormat ) {
//		Connection dbConn=null;
//		PreparedStatement stmnt=null;
////		String query="UPDATE `bureauv2alarms`.`alarming_alarmtrap`" +
////				" SET `name` = '" + name + "'," +
////				"`enabled` = " + (byte) (enabled?1:0) + "," +
////				"`response_time` = " + responseTime + "," +
////				"`maxoperatorhold_time` = " + maxOperatorHoldTime + "," +
////				"`allow_hold` = " + (byte) (allowHold?1:0) + "," +
////				"`priority` = " + priority + "," +
////				"`allow_dial` = " + (byte) (allowDial?1:0) + "," +
////				"`allow_clear` = " + (byte) (allowClear?1:0) + "," +
////				"`clear_threshold` = " + clearThreshold + "," +
////				"`alarmtrap_type` = '" + alarmTrapType + "'," +
////				"`autocheck_enabled` = " + (byte) (autoCheckEnabled?1:0) + "," +
////				"`controller_name` = '" + controllerName + "' WHERE `alarm_trap_id`=" + alarm_trap_id + ";";
//		String query="UPDATE `bureauv2alarms`.`alarming_alarmtrap` "
//				+ " SET name = ?, enabled = ?, response_time = ?, maxoperatorhold_time = ?, allow_hold = ?, priority = ?, "
//				+ " allow_dial = ?, allow_clear = ?, clear_threshold = ?,  alarmtrap_type = ?, autocheck_enabled = ?, controller_name = ?,"
//				+ " allow_email = ?, emails = ?, allow_sms = ?, mobile = ? , email_format = ?  WHERE alarm_trap_id = ? ";
//		
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.prepareStatement(query);
//			stmnt.setString(1, name);
//			stmnt.setBoolean(2, enabled);
//			stmnt.setInt(3, responseTime);
//			stmnt.setInt(4, maxOperatorHoldTime);
//			stmnt.setBoolean(5, allowHold);
//			stmnt.setInt(6, priority);
//			stmnt.setBoolean(7, allowDial);
//			stmnt.setBoolean(8, allowClear);
//			stmnt.setInt(9, clearThreshold);
//			stmnt.setString(10, alarmTrapType);
//			stmnt.setBoolean(11, autoCheckEnabled);
//			stmnt.setString(12, controllerName);
//			stmnt.setBoolean(13, emailChecked);
//			if(emailList!=null && emailList.isEmpty()){
//				stmnt.setNull(14, Types.VARCHAR);
//			}
//			else {
//			    stmnt.setString(14, emailList);
//			}
//			stmnt.setBoolean(15, smsChecked);
//			if(mobileList!=null && mobileList.isEmpty()){
//				stmnt.setNull(16, Types.VARCHAR);
//			}
//			else {
//		     	stmnt.setString(16, mobileList);
//			}
//			stmnt.setString(17, emailFormat);
//			stmnt.setInt(18, alarm_trap_id);
//
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				if(stmnt!=null)
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//		}
//	}
//
//	@Override
//	public int deleteAlarmTrap(int alarm_trap_id) {
//		int i=0;
//		Connection dbConn=null;
//		Statement stmnt=null;
//		String query="DELETE FROM `bureauv2alarms`.`alarming_alarmtrap` WHERE `alarm_trap_id`=" + alarm_trap_id+";";
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.createStatement();
//			stmnt.executeUpdate(query);
//			stmnt.close();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//		}
//		
//		Statement selstmnt = null;
//		ResultSet rs = null;
//		String selquery ="Select * FROM Alarming_Isolation_Alarmtraps WHERE alarmtrap_id= " + alarm_trap_id;
//		String delquery= "DELETE FROM Alarming_Isolation_Alarmtraps WHERE alarmtrap_id= "+ alarm_trap_id;
//		Statement delstmnt = null;
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			 selstmnt=dbConn.createStatement();
//			 rs = selstmnt.executeQuery(selquery);//Query to check if any isolation exist for the alarm trap 20-09-2011
//			 selstmnt.close();
//			 if (rs!= null){
//					try{
//						 delstmnt=dbConn.createStatement();
//						 delstmnt.executeUpdate(delquery);//delete only from the joining table
//						 delstmnt.close();
//						 i=1;
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				rs.close();
//				delstmnt.close();
//				selstmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn = null;	
//			delstmnt = null;
//			selstmnt = null;
//			rs = null;
//		}
//		return i;
//	}
//
//	@Override
//	public void updateAlarmTrapActivity(int alarm_trap_id, boolean enabled) {
//		Connection dbConn=null;
//		Statement stmnt=null;
//		String query="UPDATE `bureauv2alarms`.`alarming_alarmtrap` SET `enabled` = " + (byte) (enabled?1:0) + " WHERE `alarm_trap_id`=" + alarm_trap_id +";";
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//		}
//	}
//
//	@Override
//	public List<String> getAlarmTrapType(String searchQuery) {
//		List<String> alarmTrapType=new ArrayList<String> ();
//		Connection dbConn=null;
//		Statement stmnt=null;
//		ResultSet result=null;
//		String query="SELECT Distinct `Alarm_Type` FROM `bureauv2alarms`.`alarming_alarmtypesearchstrings` WHERE `Alarm_Type` like '" + searchQuery + "%';";
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()){
//				String trapType=result.getString(1);
//				alarmTrapType.add(trapType);
//			};	
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//			result=null;
//		}
//		return alarmTrapType;
//	}
//
//	@Override
//	public int getAlarmTrapDepId(int alarm_trap_id) {
//		int dep_id=0;
//		Connection dbConn=null;
//		Statement stmnt=null;
//		ResultSet result=null;
//		String query="SELECT `dep_id` FROM `bureauv2alarms`.`alarming_alarmtrap` WHERE `alarm_trap_id` = " + alarm_trap_id +";";
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()){
//				dep_id=result.getInt("dep_id");
//			}	
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//			result=null;
//		}
//		return dep_id;
//	}
//
//	@Override
//	public AlarmTrap getAlarmTrap(Connection dbConn,int alarm_trap_id) {
//		AlarmTrap alarmtrap=new AlarmTrap ();
//		String query="SELECT * FROM `bureauv2alarms`.`alarming_alarmtrap` WHERE `alarm_trap_id`= " + alarm_trap_id +";";
//		Statement stmnt=null;
//		ResultSet result=null;
//		try {
//			stmnt=dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()){
//				alarmtrap=new AlarmTrap(result.getInt("alarm_trap_id"),result.getBoolean("enabled"),result.getString("name"),
//						result.getInt("response_time"),result.getInt("maxoperatorhold_time"),result.getBoolean("allow_hold"),result.getInt("priority"),
//						result.getInt("workflow_id"),0,result.getBoolean("allow_dial"),result.getBoolean("allow_clear"),result.getInt("clear_threshold"),
//						result.getString("alarmtrap_type"),result.getBoolean("autocheck_enabled"),result.getString("site_code"),result.getString("controller_name"),result.getInt("dep_id"));
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//			result=null;
//		}
//		return alarmtrap;
//	}
//
//
//	@Override
//	public List<AlarmTrap> getGlobalAlarmTraps() {
//		List<AlarmTrap> alarmtraps=new ArrayList<AlarmTrap>();
//		Connection dbConn=null;
//		Statement stmnt=null;
//		ResultSet result=null;
//		String query="SELECT * FROM `bureauv2alarms`.`alarming_alarmtrap` where `dep_id` IS NULL ORDER BY `name` asc;";
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()){
//				AlarmTrap db_alarmtrap=new AlarmTrap(result.getInt("alarm_trap_id"),result.getBoolean("enabled"),result.getString("name"),
//						result.getInt("response_time"),result.getInt("maxoperatorhold_time"),result.getBoolean("allow_hold"),result.getInt("priority"),
//						result.getInt("workflow_id"),0,result.getBoolean("allow_dial"),result.getBoolean("allow_clear"),result.getInt("clear_threshold"),
//						result.getString("alarmtrap_type"),result.getBoolean("autocheck_enabled"),"",result.getString("controller_name"),0, result.getString("emails"), result.getBoolean("allow_email"), result.getString("mobile"), 
//						result.getBoolean("allow_sms"), result.getString("email_format"));
//				alarmtraps.add(db_alarmtrap);
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbConn=null;
//			stmnt=null;
//			result=null;
//		}
//		return alarmtraps;
//	}
//
//	@Override
//	public void addGlobalAlarmTrap(String name, boolean enabled,
//			int responseTime, int maxOperatorHoldTime, boolean allowHold,
//			int priority, String workflow_id, String stock_valuation_id,
//			boolean allowDial, boolean allowClear,
//			int clearThreshold, String alarmTrapType, boolean autoCheckEnabled,
//			String controllerName, String emailAlert, boolean allowEmail, String smsAlert, boolean allowSms, String workflowType, String emailFormat) {
//	String query="CALL bureauv2alarms.`create_workflows_alarmtraps_web`(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
//	Connection dbConn=null;
//	PreparedStatement stmnt=null;
//	try{
//		dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//		stmnt = dbConn.prepareStatement(query);
//		stmnt.setString(1, name);
//		stmnt.setBoolean(2, enabled);
//		stmnt.setInt(3, responseTime);
//		stmnt.setInt(4, maxOperatorHoldTime);
//		stmnt.setBoolean(5, allowHold);
//		stmnt.setInt(6, priority);
//		stmnt.setNull(7,Types.VARCHAR);
//		stmnt.setBoolean(8, allowDial);
//		stmnt.setBoolean(9, allowClear);
//		stmnt.setInt(10, clearThreshold);
//		stmnt.setString(11, alarmTrapType);
//		stmnt.setBoolean(12, autoCheckEnabled);
//		stmnt.setString(13, controllerName);
//		stmnt.setNull(14, Types.INTEGER);
//		if(emailAlert.isEmpty()){
//			stmnt.setNull(15, Types.VARCHAR);
//		}
//		else {
//	      	stmnt.setString(15, emailAlert);
//		}
//		stmnt.setBoolean(16, allowEmail);
//		if(smsAlert.isEmpty()){
//			stmnt.setNull(17, Types.VARCHAR);
//		}
//		else {
//	     	stmnt.setString(17, smsAlert);
//		}
//		stmnt.setBoolean(18, allowSms);
//		stmnt.setString(19, workflowType);
//		stmnt.setString(20, emailFormat);
//		stmnt.executeQuery();
//	}catch(SQLException e){
//		e.printStackTrace();
//	}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		dbConn=null;
//		stmnt=null;
//	}
//	}
//	@Override
//	public List<String> getAlarmTypes() {
//		// TODO Auto-generated method stub
//		List<String> alarmTypes = null; 
//		Connection conn = null;
//		PreparedStatement stmnt = null;
//		ResultSet rs = null; 
//		//String query="SELECT distinct name FROM `bureauv2alarms`.`alarming_alarmtrap` ORDER BY name";
//		String query = "SELECT DISTINCT alarm_type FROM `bureauv2alarms`.`alarming_alarmtypesearchstrings` ORDER BY alarm_type";
//		try {
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			rs = stmnt.executeQuery();
//			if(rs!=null){
//				alarmTypes = new ArrayList<String>();
//			}
//			while(rs.next()){
//				alarmTypes.add(rs.getString("alarm_type").trim());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			try {
//			    if(stmnt != null){
//				    stmnt.close();
//				}
//			    if(rs != null){
//					rs.close();
//			    }
//			   }
//			catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			finally {
//				rs=null;
//				stmnt = null; 
//				conn = null;
//			}
//			}
//		return alarmTypes;
//	}
}

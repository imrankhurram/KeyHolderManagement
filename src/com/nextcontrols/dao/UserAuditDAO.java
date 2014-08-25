package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nextcontrols.domain.UserAudit;
import com.nextcontrols.utility.ServiceProperties;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class UserAuditDAO implements IUserAuditDAO, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static IUserAuditDAO instance;
 	
	/** *************** Factory Methods ************************* */
	public static IUserAuditDAO getInstance() { 
        if (instance != null) {
            return instance; 
        } else {
            return new UserAuditDAO();
        }
    }
    
	public static void setInstance(IUserAuditDAO inst) {
		instance = inst;
	} 
    private UserAuditDAO() {}
    
	@Override
	public List<UserAudit> getUserAudits() {
		List<UserAudit> listOfAudits=new ArrayList<UserAudit>();
		String query="SELECT * FROM `audits_useraudit`;";
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				UserAudit newAudit=new UserAudit(result.getInt("user_id"),result.getTimestamp("audit_date"),result.getString("action_type"),result.getString("action_description"),result.getString("site_code"));
				listOfAudits.add(newAudit);
			}
		}catch (SQLException e){
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
		
		return listOfAudits;
	}
	public List<UserAudit> getIdleUserAudits(java.util.Date dateFrom, java.util.Date dateTo, int userId) {
		List<UserAudit> idleList = new ArrayList<UserAudit>();
		String query = "SELECT * FROM `audits_useraudit` where (action_type='useridle' OR action_type='userlogin') AND user_id= " +userId+" AND (audit_date>='"+new Timestamp(dateFrom.getTime())+"' AND audit_date<='"+new Timestamp(dateTo.getTime())+"') " +
				"order by audit_date DESC ;";
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet result = null; 
		try {
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmt = dbConn.createStatement();
			result = stmt.executeQuery(query);
			while(result.next()) {
				UserAudit newAudit = new UserAudit(result.getInt("user_id"), result.getTimestamp("audit_date"), result.getString("action_type"), result.getString("action_description"), result.getString("site_code"));
				idleList.add(newAudit);
			}
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}finally {
			try {
				if(result!=null){
				   result.close();
				}
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			stmt= null; 
			result=null;
		}
		return idleList;
	}
	
	@Override
	public void insertUserAudit(UserAudit userAudit) {
		/*Statement stmnt=null;
		String query="";
		if (userAudit.getSiteCode()!=null){
			query="INSERT INTO `audits_useraudit` (`user_id`,`audit_date`,`action_type`,`action_description`,`site_code`) " +
				"VALUES("+ userAudit.getUserId() + ",'" + userAudit.getAuditDate() + "','" + userAudit.getActionType() + 
				"','"+ userAudit.getActionDescription() +  "','" + userAudit.getSiteCode() +"');";
		}else{
			query="INSERT INTO `audits_useraudit` (`user_id`,`audit_date`,`action_type`,`action_description`) " +
			"VALUES("+ userAudit.getUserId() + ",'" + userAudit.getAuditDate() + "','" + userAudit.getActionType() + 
			"','"+ userAudit.getActionDescription() +"');";
		}
		try{
			dbConnection();
			stmnt=dbConn.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			stmnt=null;
		}*/
		PreparedStatement pstmt=null;
		Connection dbConn=null;
		String query="";
		//if (userAudit.getSiteCode()!=null){
			query="INSERT INTO `audits_useraudit` (`user_id`,`audit_date`,`action_type`,`action_description`,`site_code`) " +
				"VALUES(?,?,?,?,?);";
		/*}else{
			query="INSERT INTO `audits_useraudit` (`user_id`,`audit_date`,`action_type`,`action_description`) " +
			"VALUES(?,?,?,?,?);";
		}*/
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	 	  	pstmt = dbConn.prepareStatement (query);
	 	  	pstmt.setInt(1, userAudit.getUserId());
	 	  	pstmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTime().getTime()));//pstmt.setTimestamp(2, userAudit.getAuditDate());
	 	  	pstmt.setString(3, userAudit.getActionType());
	 	  	pstmt.setString(4, userAudit.getActionDescription());
	 	  	if (userAudit.getSiteCode()!=null){
	 	  		pstmt.setString(5, userAudit.getSiteCode());
	 	  	}else{
	 	  		pstmt.setNull(5, Types.VARCHAR);
	 	  	}
	 	  	pstmt.executeUpdate ();
	 	  	
		}catch (SQLException e){
            e.printStackTrace();		
        }finally{
        	try {
        		pstmt.close ();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pstmt=null;
			dbConn=null;
		}
	}

	@Override
	public int countUserIdles(int userId) {
		int idles=0;
		String query="SELECT count(*) FROM `audits_useraudit` WHERE user_id=? and action_description like 'The user has been inactive%';";
		Connection dbConn = null;
		PreparedStatement count=null;
		ResultSet result=null;
		try{
			dbConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
			count=dbConn.prepareStatement(query);
			count.setInt(1, userId);
			result=count.executeQuery();
			while (result.next()){
				idles=result.getInt(1);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				result.close();
				count.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			count=null;
			result=null;
		}
		
		return idles;
	}

	@Override
	public List<UserAudit> getSpecificUserAudits(int user_id, String siteCode) {
		IUserDAO userDB= UserDAO.getInstance();
		List<UserAudit> listOfAudits=new ArrayList<UserAudit>();
		String query="SELECT * FROM `audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "';";
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				UserAudit newAudit=new UserAudit(result.getInt("user_id"),result.getTimestamp("audit_date"),result.getString("action_type"),result.getString("action_description"),result.getString("site_code"));
				newAudit.setUserName(userDB.getUserName(newAudit.getUserId()));
				listOfAudits.add(newAudit);
			}
		}catch (SQLException e){
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
		
		return listOfAudits;
	}
	public List<UserAudit> getDateSpecificUserAudits(Date dateFrom, Date dateTo) {
		List<UserAudit>  listOfAudits = new ArrayList<UserAudit>();
		String query ="SELECT * FROM `audits_useraudit` WHERE action_type='useridle' and audit_date>='" + new java.sql.Timestamp(dateFrom.getTime()) + "' and audit_date<='" + new java.sql.Timestamp(dateTo.getTime())+ "';";
        Connection dbConn = null;
		Statement stmt = null; 
        ResultSet result = null ; 
        try {
        	dbConn=ConnectionBean.getInstance().getMYSQLConnection();
        	stmt = dbConn.createStatement();
        	result = stmt.executeQuery(query);
        	while(result.next()) {
        		listOfAudits.add(new UserAudit(result.getInt("user_id"), result.getTimestamp("audit_date"), result.getString("action_type"), result.getString("action_description"),result.getString("site_code")));
        	}
        }
        catch(SQLException ex) {
        	ex.printStackTrace();
        }
        finally {
        	try {
				result.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	dbConn= null; 
        	stmt = null; 
        	result = null;
        	
        }
        return listOfAudits;
	}

	@Override
	public List<UserAudit> getDateSpecificUserAudits(int user_id,
			String siteCode, Date dateFrom, Date dateTo) {
		List<UserAudit> listOfAudits=new ArrayList<UserAudit>();
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT * FROM `audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT * FROM `audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "' and audit_date>='" + new java.sql.Date(dateFrom.getTime()) + "';";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT * FROM `audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "' and audit_date<='" + new java.sql.Date(dateTo.getTime()) + "';";
		}else{
			query="SELECT * FROM `audits_useraudit` WHERE user_id=" + user_id + " and site_code='" + siteCode + "' and audit_date>='" + new java.sql.Date(dateFrom.getTime()) + "' and audit_date<='" + new java.sql.Date(dateTo.getTime()) + "';";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				UserAudit newAudit=new UserAudit(result.getInt("user_id"),result.getTimestamp("audit_date"),result.getString("action_type"),result.getString("action_description"),result.getString("site_code"));
				newAudit.setUserName(UserDAO.getInstance().getUserName(newAudit.getUserId()));
				listOfAudits.add(newAudit);
			}
		}catch (SQLException e){
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
		
		return listOfAudits;
	}

	@Override
	public int avgAlarmHandlingTime(String username, Date dateFrom, Date dateTo) {
		int avgTime=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT AVG(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT AVG(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT AVG(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT AVG(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				avgTime=result.getInt(1);
			}
		}catch (SQLException e){
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
		return avgTime;
	}

	@Override
	public int maxAlarmHandlingTime(String username, Date dateFrom, Date dateTo) {
		int maxTime=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT MAX(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT MAX(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT MAX(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT MAX(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				maxTime=result.getInt(1);
			}
		}catch (SQLException e){
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
		return maxTime;
	}

	@Override
	public int minAlarmHandlingTime(String username, Date dateFrom, Date dateTo) {
		int minTime=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT MIN(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT MIN(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT MIN(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT MIN(time_taken) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				minTime=result.getInt(1);
			}
		}catch (SQLException e){
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
		return minTime;
	}

	@Override
	public int totalAlmsHandled(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and `audits_alarmaudit`.action_taken!='Cleared';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `audits_alarmaudit`.action_taken!='Cleared' " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `audits_alarmaudit`.action_taken!='Cleared' " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `audits_alarmaudit`.action_taken!='Cleared' and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
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
		return count;
	}

	@Override
	public int totalAlmsCleared(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and `audits_alarmaudit`.action_taken='Cleared';";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `audits_alarmaudit`.action_taken='Cleared' " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `audits_alarmaudit`.action_taken='Cleared' " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `audits_alarmaudit`.action_taken='Cleared' and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
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
		return count;
	}

	@Override
	public int totalAlmsHeld(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and `alarming_alarm`.time_held>0;";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `alarming_alarm`.time_held>0 " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `alarming_alarm`.time_held>0 " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and `alarming_alarm`.time_held>0 and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
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
		return count;
	}

	@Override
	public int totalAlmsOverdue(String username, Date dateFrom, Date dateTo) {
		int count=0;
		String query="";
		if ((dateFrom==null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username + "' and (finish_time is not null) and timestampdiff(MINUTE,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time)>30;";
		}else if ((dateFrom!=null)&&(dateTo==null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
					"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and timestampdiff(MINUTE,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time)>30 " +
							"and finish_time>='" + dateFrom +"');";
		}else if ((dateFrom==null)&&(dateTo!=null)){
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and timestampdiff(MINUTE,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time)>30 " +
					"and finish_time<='" + dateTo +"');";
		}else{
			query="SELECT COUNT(*) FROM `alarming_alarm` INNER JOIN `audits_alarmaudit` on `alarming_alarm`.alarm_id=`audits_alarmaudit`.alarm_id " +
			"WHERE `audits_alarmaudit`.username='" + username +"' and (finish_time is not null) and timestampdiff(MINUTE,`alarming_alarm`.receive_time,`alarming_alarm`.finish_time)>30 and " +
					"(finish_time>='" + dateFrom +"' and finish_time<='" + dateTo + "');";
		}
		Connection dbConn = null;
		Statement stmnt=null;
		ResultSet result=null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result=stmnt.executeQuery(query);
			while(result.next()){
				count=result.getInt(1);
			}
		}catch (SQLException e){
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
		return count;
	}
}

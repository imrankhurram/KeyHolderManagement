package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class UserActivityDAO implements IUserActivityDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static IUserActivityDAO instance;
	public static IUserActivityDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new UserActivityDAO();
		}
	}
	public static void setInstance(IUserActivityDAO inst) {
		instance=inst;
	}
	private UserActivityDAO() {}
	
	@Override
	public void userLogin(String username, Timestamp login_time,
			Timestamp last_check, String userType){
		
		Connection dbConn=null;
		Statement stmnt=null;
		String deleteQuery="DELETE FROM `loggedin_users` WHERE username='" + username + "';";
		String query="INSERT INTO `loggedin_users` (`username`,`login_time`,`last_update`,`user_type`) " +
				"VALUES ('"+ username + "','" + login_time + "','" + last_check +"','" + userType + "');" ;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			stmnt.executeUpdate(deleteQuery);
			stmnt.close();
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
		}
	}

	@Override
	public void userLogout(String username) {
		String query="DELETE FROM `loggedin_users` WHERE username='" + username + "';";
		Connection connect=null;
		Statement stmnt=null;
		try{
			connect=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=connect.createStatement();
			stmnt.executeUpdate(query);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connect=null;
			stmnt=null;
		}
	}

	@Override
	public List<String> userUpdateCheck(String username, Timestamp last_update) {
		String query="UPDATE `loggedin_users` SET last_update='" + last_update + "' WHERE username='" + username + "';";
		Connection dbConn=null;
		Statement stmnt=null;
		ResultSet results = null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
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
			stmnt=null;
		}
		
	
		Calendar last_check=Calendar.getInstance();
		Date new_date=new Date(last_update.getTime());
		last_check.setTime(new_date);
		last_check.setTimeInMillis(last_check.getTimeInMillis() - 120000); //setting the time 2 minutes back
		new_date=last_check.getTime();
		query="DELETE FROM `loggedin_users` WHERE last_update<'" + new Timestamp(new_date.getTime()) + "';";
		try{
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
			stmnt=null;
		}
		
		List<String> usersLoggedIn=new ArrayList<String>();
		query="SELECT username FROM `loggedin_users`";
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				String userName=results.getString("username");
				usersLoggedIn.add(userName);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt=null;
			dbConn = null;
		}
		return usersLoggedIn;
	}
	
	public int getNumberUsersOnline(){
		int number=0;
		String query="SELECT COUNT(DISTINCT username) FROM `loggedin_users`;";
		Connection dbConn=null;
		Statement stmnt=null;
		ResultSet result= null;
		try{
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result = stmnt.executeQuery(query);
			result.next();
			number=result.getInt(1);
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt=null;
			result=null;
			dbConn = null;
		}
		return number;
	}

}

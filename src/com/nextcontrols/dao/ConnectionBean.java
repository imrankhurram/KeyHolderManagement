package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import com.nextcontrols.utility.ServiceProperties;


public class ConnectionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Connection dbSQLConn;
	private Connection dbBureauConn;
	private Connection dbMYSQLConn;
//	private Connection dbAlarmMYSQLConn;
	private static ConnectionBean connBean;
	private static int count;
	
	private ConnectionBean(){}
	
	public synchronized static ConnectionBean getInstance(){
		if (connBean==null){
			connBean=new ConnectionBean();
		}
		return connBean;
	}
//	public Connection getSQLConnection() throws SQLException{
////		System.out.println((dbBureauConn==null) ? true :  dbBureauConn.isClosed());
//		if (dbSQLConn==null){
//			dbSQLConn=ServiceProperties.getInstance().getBureauConnection();
//			System.out.println("Creating SQL Connection when null " );
//		}
//		else if (dbSQLConn.isClosed()){
//		
//			dbSQLConn=ServiceProperties.getInstance().getBureauConnection();	
//			System.out.println("Creating SQL Connection when closed " );
//		}
//		return dbSQLConn;
//	}
	public Connection getBureauConnection() throws SQLException{
//		System.out.println((dbBureauConn==null) ? true :  dbBureauConn.isClosed());
		if (dbBureauConn==null){
			dbBureauConn=ServiceProperties.getInstance().getBureauConnection();
			System.out.println("Creating Bureau Connection when null " );
		}
		else if (dbBureauConn.isClosed()){
			count++;
			dbBureauConn=ServiceProperties.getInstance().getBureauConnection();	
			System.out.println("Creating Bureau Connection when closed " );
		}
		return dbBureauConn;
	}
	
	public Connection getMYSQLConnection() throws SQLException{
		if (dbMYSQLConn==null){
			dbMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
			System.out.println("Creating getMYSQLConnection when null");
		}
		else if (dbMYSQLConn.isClosed()){
			dbMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();	
		}
		return dbMYSQLConn;
	}
	
//	public Connection getAlarmMYSQLConnection() throws SQLException{
//		if (dbAlarmMYSQLConn==null){
//			dbAlarmMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
//			System.out.println("Creating  AlarmMYSQLConnection when null");
//		}
//	   else if (dbAlarmMYSQLConn.isClosed()){
//		   dbAlarmMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();	
//     	}
//		return dbAlarmMYSQLConn;
//	}
	
	public void openConnections(){
		try {
//			dbAlarmMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
			dbMYSQLConn=ServiceProperties.getInstance().getConnectionMQSQLDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void closeConnections() throws SQLException
	{
	 // dbBureauConn.close();
	  //dbMYSQLConn.close();
		dbBureauConn=null;
	    dbMYSQLConn=null;
//	    dbSQLConn=null;
	} 
	
	public void closeMYSQLConnection() throws SQLException
	{
		dbBureauConn=null;
		dbMYSQLConn=null;
		
	}
}

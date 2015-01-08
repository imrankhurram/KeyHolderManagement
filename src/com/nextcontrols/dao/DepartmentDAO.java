package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.domain.Department;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class DepartmentDAO implements IDepartmentDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static IDepartmentDAO instance;

	public static IDepartmentDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new DepartmentDAO();
		}
	}

	public static void setInstance(IDepartmentDAO inst) {
		instance = inst;
	}

	private DepartmentDAO() {}

	/*public void dbConnect() {
		try {
			dbConn = ConnectionBean.getInstance().getAlarmMYSQLConnection();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public List<Department> getDepartmentList(List<String> branch_codes) {
		List<Department> departmentList=new ArrayList<Department> ();
		if(branch_codes==null)
			return departmentList;
		StringBuilder branchCodeList=new StringBuilder("(");
		for(String code:branch_codes){
			branchCodeList.append("'");
			branchCodeList.append(code);
			branchCodeList.append("'");
			branchCodeList.append(",");
		}
		branchCodeList.replace(branchCodeList.lastIndexOf(","), branchCodeList.length(), ")");
		Connection dbConn=null;
		Statement stmnt=null;
		ResultSet result=null;
//		System.out.println("list: "+branchCodeList.toString());
		String query="SELECT dep_id,version,name,type,display_name,branch_code FROM `departments` WHERE branch_code IN " + branchCodeList.toString() +";";
		System.out.println("query: "+query);
		try {
			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
			stmnt=dbConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()){
				Department newDept=new Department(result.getInt("dep_id"),result.getInt("version"),result.getString("branch_code"),result.getString("name"),result.getString("type"),result.getString("display_name"));
				departmentList.add(newDept);
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
		return departmentList;
	}
//
//	@Override
//	public void addDepartment(int version, String branch_code,
//			String name, String type) {
//		Connection dbConn=null;
//		Statement stmnt=null;
//		String query="INSERT INTO `departments`(version,branch_code,name,type)" +
//				" VALUES(" + version + ",'" + branch_code + "','" + name + "','" + type + "');";
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
//		
//	}
//	@Override
//	public int addDept(int version, String branch_code, String name, String type) {
//		// TODO Auto-generated method stub
//		Connection dbConn=null;
//		int LAST_INS_ID =0;
//		String query="INSERT INTO `departments`(version,branch_code,name,type)" +
//				" VALUES(?,?,?,?);";
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			try(PreparedStatement stmnt = dbConn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
//				stmnt.setInt(1, version);
//				stmnt.setString(2, branch_code);
//				stmnt.setString(3, name);
//				stmnt.setString(4, type);
//				stmnt.execute();
//				try(ResultSet rs = stmnt.getGeneratedKeys()){
//					if(rs!=null && rs.next()){
//						LAST_INS_ID = rs.getInt(1);
//					}
//				}
//			}
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			dbConn=null;
//		}
//		return LAST_INS_ID;
//	}
//	@Override
//	public void modifyDepartment(int dep_id, int version, String name,
//			String type) {
//		Connection dbConn=null;
//		Statement stmnt=null;
//		String query="UPDATE `departments` " +
//				"SET version = " + version + ", " +
//				"name = '" + name + "'," +
//				"type = '" + type + "' WHERE dep_id=" + dep_id +";";
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
//		
//	}
//
//	@Override
//	public void deleteDepartment(int dep_id) {
//		Connection dbConn=null;
//		Statement stmnt=null;
//		String query="DELETE FROM `departments` WHERE dep_id=" + dep_id +";";
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
//	public int copyDepartment(int version, String branch_code, String name,
//			String type) {
//		int depId=0;
//		Connection dbConn=null;
//		Statement stmnt=null;
//		ResultSet result=null;
//		String query="INSERT INTO `departments`(version,branch_code,name,type) " +
//				"VALUES(" + version + ",'" + branch_code + "','" + name + "','" + type + "');";
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			
//			stmnt=dbConn.createStatement();
//			stmnt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
//			result=stmnt.getGeneratedKeys();
//			while (result.next()){
//				depId=result.getInt(1);
//			}
//		}catch (SQLException e) {
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
//			
//		}
//		return depId;
//	}
//
//	@Override
//	public String getDepartmentName(Connection dbConn,int dep_id) {
//		String deptName="";
//		PreparedStatement stmnt=null;
//		ResultSet result=null;
//		String query="SELECT name FROM `departments` WHERE dep_id=?;";
//		try{
//			stmnt=dbConn.prepareStatement(query);
//			stmnt.setInt(1, dep_id);
//			result=stmnt.executeQuery();
//			while (result.next()){
//				deptName=result.getString("name");
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			result = null;
//			stmnt = null;
//			dbConn = null;
//		}
//		return deptName;
//	}
//	
//	public String getDepartmentName(int dep_id) {
//		String deptName="";
//		String query="SELECT name FROM `departments` WHERE dep_id=?;";
//		Connection dbConn=null;
//		PreparedStatement stmnt=null;
//		ResultSet result=null;
//		try{
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbConn.prepareStatement(query);
//			stmnt.setInt(1, dep_id);
//			result=stmnt.executeQuery();
//			while (result.next()){
//				deptName=result.getString("name");
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//				dbConn=null;
//				stmnt=null;
//				result=null;
//		}
//		return deptName;
//	}
//
//	@Override
//	public int getDepartmentId(String name, String siteCode) {
//		// TODO Auto-generated method stub
//		String query="SELECT dep_id FROM bureauv2alarms.departments WHERE name = ? AND branch_code = ?";
//		System.out.println(name + " " + siteCode);
//		int depId=0;
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		ResultSet rs = null; 
//		try {
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, name);
//			stmnt.setString(2, siteCode);
////			rs = stmnt.executeQuery();
////			if(rs.next()){
////				depId = rs.getInt("dep_id");
////			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return depId;
//	}

	
}

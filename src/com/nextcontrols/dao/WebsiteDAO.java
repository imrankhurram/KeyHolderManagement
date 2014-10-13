package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.domain.Website;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class WebsiteDAO implements IWebsiteDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerId;
	private static IWebsiteDAO instance;

	public static IWebsiteDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new WebsiteDAO();
		}
	}

	public static void setInstance(IWebsiteDAO ins) {
		instance = ins;
	}

	private WebsiteDAO() {
	}

	public List<Website> getAssignedWebsites(int userId) {
	
		List<Website> assignedWebsites =null;
		Connection conn=null; 
		CallableStatement stmnt =null; 
		ResultSet rs =null; 
		String query=" [key_get_assignedwebsites](?)";
		try {
			conn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = conn.prepareCall(query);
			stmnt.setInt(1, userId);
			
			rs = stmnt.executeQuery();
			assignedWebsites = new ArrayList<Website>();
			while(rs.next()){
				assignedWebsites.add(new Website(rs.getInt("website_id"),rs.getString("name"),rs.getString("meterName"),rs.getString("hvacName"),rs.getString("fixtureName")
						,rs.getString("alarmName"),rs.getString("imagePath"),rs.getString("branchListName"),rs.getString("Logo"),rs.getBoolean("typetutela"),rs.getInt("inactivityTimeout")
						,rs.getString("CountryCode")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs =null;
			stmnt =null;
			conn = null;	
		}
		
		return assignedWebsites;
	}
	public List<String> getBranchCodes(int websiteId){
		List<String> branchCodes=new ArrayList<String>();
		String query="SELECT DISTINCT branch_code FROM [branch_views] WHERE website_id='" + websiteId +"'";
		Connection dbConn = null;
		Statement stmnt =null;
		ResultSet results = null;
		try{
			dbConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				branchCodes.add(results.getString("branch_code").trim());

			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results =null;
			stmnt =null;
			dbConn = null;
		}

		return branchCodes;
	}
}

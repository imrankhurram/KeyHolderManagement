package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.nextcontrols.domain.Branch;

public class BranchDAO implements IBranchDAO,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Connection dbBureauConn;
	private static IBranchDAO instance;

	/** *************** Factory Methods ************************* */
	public static IBranchDAO getInstance() { 
		if (instance != null) {
			return instance; 
		} else {
			return new BranchDAO();
		}
	}

	public static void setInstance(IBranchDAO inst) {
		instance = inst;
	}
	private BranchDAO() {}
	/*public void dbBureauConnect(){
		try {
			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

	/*public void dbMYSQLBureauConnect(){
		try {
			dbBureauConn2=ConnectionBean.getInstance().getMYSQLConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/


	@Override
	public List<Branch> getBranch(int customerId) {
		List<Branch> branches=new ArrayList<Branch> ();
		String query="SELECT * FROM branches WHERE customer_id="+customerId + " and deleted=0 ORDER BY branch_code";
		Connection dbBureauConn=null;
		Statement stmnt= null;
		ResultSet result = null;
		try{
			dbBureauConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbBureauConn.createStatement();
			result=stmnt.executeQuery(query);
			while (result.next()){
				Branch db_branch=new Branch(result.getString("branch_code"),result.getString("name"),customerId,result.getString("address1"),
						result.getString("address2"),result.getString("city"),result.getString("zip"),result.getString("country"),result.getString("timezone"),
						result.getString("fax"),result.getString("phone"),result.getString("email"),result.getDate("OrderDate"),result.getString("AgreementNumber"),
						result.getInt("AgreementTerm"),result.getDate("CommencementDate"),result.getDate("InstallationDate"),result.getInt("repeatalarminterval"),result.getString("nextEngineer"),result.getString("siteContact"),
						result.getString("refrigerationContractor"),result.getString("hvacContractor"),result.getString("maintenanceManager"),result.getString("siteGroup"),result.getByte("transferedToNewBureau"),result.getInt("alarmPriority"),
						result.getBoolean("outOfContract"), result.getBoolean("specialKeyholderFlag"), result.getString("refrigerated_system"));
				branches.add(db_branch);
				db_branch.setCustomerName(CustomerDAO.getInstance().getCustomerNameFromSite(db_branch.getBranchCode()));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			result = null;
			dbBureauConn = null;
		}
		return branches;
	}

//	@Override
//	public boolean addBranch(String branchCode,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,String email,int contact_id,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup,int alarmPriority, String refSystem) {
//		String query="INSERT INTO branches (branch_code,version,name,customer_id,address1,address2" +
//				",city,zip,country,timezone,phone,email,fax,contact_id,OrderDate" +
//				",AgreementNumber,AgreementTerm,CommencementDate,InstallationDate,repeatalarminterval,nextEngineer,siteContact," +
//				"refrigerationContractor,hvacContractor,maintenanceManager,siteGroup,alarmPriority,refrigerated_system) " +
//				" VALUES ('" + branchCode + "',1,'"  + name.replaceAll("'", "''") + "'," + 
//				customer_id + ",'" + address1.replaceAll("'", "''") + "','" + address2.replaceAll("'", "''") + "','" + city + "','" +
//				zip + "','" + country + "','" + timezone + "','" + phone + "','" + email + "','" +  fax + "'," + contact_id + ",'" + new java.sql.Date(orderDate.getTime()) + "','" +
//				agreementNumber + "'," + agreementTerm + ",'" + new java.sql.Date(commencementDate.getTime()) + "','" +
//				new java.sql.Date(installationDate.getTime()) + "',"+ repeatalarminterval + ",'" + nextEngineer + "','" + siteContact + "','" + refrigerationContractor + "','" + hvacContractor  + "','" + maintenanceManager + "','" +siteGroup +"'," + 
//				alarmPriority + ",'" + refSystem + "')";
//		Connection dbBureauConn = null;
//		Connection dbConn=null;
//		Statement stmnt = null;
//
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			query="INSERT INTO bureauv2alarms.departments (version,branch_code,name,type) VALUES (1,'" + branchCode + "','Default','Default')";
//			stmnt=dbConn.createStatement();
//			stmnt.executeUpdate(query);
//
//			//Changed by Nayyab tofix add site error
//			//inserting default asset
//			query="INSERT INTO bureauv2alarms.alarming_alarmassets (dep_id,Fixture_Name,Fixture_Type,Stock_Value,Low_Temp,High_Temp,Low_Temp_Offset,High_Temp_Offset,Installation_Date,Value_Currency) " +
//					"SELECT dep_id," +
//					"'Unknown','Unknown',CONVERT(0,DECIMAL),CONVERT(0,DECIMAL),CONVERT(0,DECIMAL),CONVERT(0,DECIMAL),CONVERT(0,DECIMAL),CURDATE(),'GBP' FROM bureauv2alarms.departments WHERE branch_code='" + branchCode + "';";
//			stmnt=dbConn.createStatement();
//			stmnt.executeUpdate(query);
//
//			//inserting default opening hours
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Monday',null,'"+branchCode+"','09:00','18:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Tuesday',null,'"+branchCode+"','09:00','18:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Wednesday',null,'"+branchCode+"','09:00','18:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Thursday',null,'"+branchCode+"','09:00','18:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Friday',null,'"+branchCode+"','09:00','18:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Saturday',null,'"+branchCode+"','09:00','18:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Sunday',null,'"+branchCode+"','10:00','17:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Christmas',null,'"+branchCode+"','10:00','16:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('Boxing Day',null,'"+branchCode+"','10:00','16:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('New Years Day',null,'"+branchCode+"','10:00','16:00')";
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//			return true;
//		}catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//			dbConn=null;
//		}
//
//	}
//	
//	@Override
//	public void addBranchMYSQL(String branchCode, String name,
//			int customer_id, String address1, String address2, String city,
//			String zip, String country, String timezone, String fax,
//			String phone, String email, int contact_id, Date orderDate,
//			String agreementNumber, int agreementTerm, Date commencementDate,
//			Date installationDate, int repeatalarminterval,
//			String nextEngineer, String siteContact,
//			String refrigerationContractor, String hvacContractor,
//			String maintenanceManager, String siteGroup, int alarmPriority, String refSystem) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query="INSERT INTO transferredsites (site_code,version,name,customer_id,address1,address2" +
//					",city,zip,country,timezone,phone,email,fax,contact_id,OrderDate" +
//					",AgreementNumber,AgreementTerm,CommencementDate,InstallationDate,repeatalarminterval,nextEngineer,siteContact," +
//					"refrigerationContractor,hvacContractor,maintenanceManager,siteGroup,alarmPriority, refrigerated_system, alarmingMethod) " +
//					" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, branchCode);
//			stmnt.setInt(2, 1);
//			stmnt.setString(3,  name.replaceAll("'", "''"));
//			stmnt.setInt(4, customer_id);
//			stmnt.setString(5, address1.replaceAll("'", "''"));
//			stmnt.setString(6, address2.replaceAll("'", "''"));
//			stmnt.setString(7, city);
//			stmnt.setString(8, zip);
//			stmnt.setString(9, country);
//			stmnt.setString(10, timezone);
//			stmnt.setString(11, phone);
//			stmnt.setString(12, email);
//			stmnt.setString(13, fax);
//			stmnt.setInt(14, contact_id);
//			stmnt.setTimestamp(15, new Timestamp(orderDate.getTime()));
//			stmnt.setString(16, agreementNumber);
//			stmnt.setInt(17, agreementTerm);
//			stmnt.setTimestamp(18, new Timestamp(commencementDate.getTime()));
//			stmnt.setTimestamp(19, new Timestamp(installationDate.getTime()));
//			stmnt.setInt(20, repeatalarminterval);
//			stmnt.setString(21, nextEngineer);
//			stmnt.setString(22, siteContact);
//			stmnt.setString(23, refrigerationContractor);
//			stmnt.setString(24, hvacContractor);
//			stmnt.setString(25, maintenanceManager);
//			stmnt.setString(26, siteGroup);
//			stmnt.setInt(27, alarmPriority);
//			stmnt.setString(28, refSystem);
//			stmnt.setInt(29, 0);
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			stmnt = null; 
//			conn = null;
//		}
//		
//	}
//
//	@Override
//	public void deleteBranch(String branch_code) {
//		String query="UPDATE branches SET deleted=1 WHERE branch_code='" + branch_code +"'";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//	@Override
//	public void deleteBranchMYSQL(String branch_code) {
//		// TODO Auto-generated method stub
//		String query="UPDATE transferredsites SET deleted= ? WHERE site_code= ?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setBoolean(1, true);
//			stmnt.setString(2, branch_code);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public void modifyBranch(String branchCode,String name,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,String email,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup,int alarmPriority, String refSystem) {
//
//		String query="UPDATE branches SET " +
//				"name='" + name.replaceAll("'", "''") + "', " +
//				"address1='" + address1.replaceAll("'", "''") + "', " +
//				"address2='" + address2.replaceAll("'", "''") + "', " +
//				"city='" + city + "', " +
//				"zip='" + zip + "', " +
//				"country='" + country + "', " +
//				"timezone='" + timezone + "', " +
//				"phone='" + phone + "', " +
//				"email='" + email + "', " +
//				"fax='" + fax + "', " +
//				"OrderDate='" + new java.sql.Date(orderDate.getTime()) + "', " +
//				"AgreementNumber='" + agreementNumber + "', " +
//				"AgreementTerm=" + agreementTerm + ", " +
//				"CommencementDate='" + new java.sql.Date(commencementDate.getTime()) + "', " +
//				"InstallationDate='" + new java.sql.Date(installationDate.getTime()) + "', " +
//				"repeatalarminterval=" + repeatalarminterval + ", " +
//				"nextEngineer='" + nextEngineer + "', " +
//				"siteContact='" + siteContact + "', " +
//				"refrigerationContractor='" + refrigerationContractor + "', " +
//				"hvacContractor='" + hvacContractor + "', "+
//				"maintenanceManager='" + maintenanceManager + "', "+
//				"siteGroup='" + siteGroup + "', " +
//				"alarmPriority=" + alarmPriority + "," +
//				"refrigerated_system='"+ refSystem+"' " +
//				"WHERE branch_code='" + branchCode + "'";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//	@Override
//	public void modifyBranchMYSQL(Branch branch) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query= "UPDATE transferredsites SET " +
//			"name= ?, address1= ?, address2= ?,city = ?, zip= ? , country= ?, timezone= ? , phone= ? , email=?, fax=?, OrderDate= ?,"+ 
//			" AgreementNumber= ?,AgreementTerm= ? , CommencementDate= ?,InstallationDate= ?,repeatalarminterval= ? , nextEngineer= ?, " +
//			"siteContact= ?,refrigerationContractor= ?,hvacContractor= ?, maintenanceManager= ?,siteGroup= ?, alarmPriority= ?," +
//			"refrigerated_system= ? WHERE site_code= ? ";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, branch.getName().replaceAll("'", "''"));
//			stmnt.setString(2, branch.getAddress1().replaceAll("'", "''"));
//			stmnt.setString(3, branch.getAddress2().replaceAll("'", "''"));
//			stmnt.setString(4, branch.getCity());
//			stmnt.setString(5, branch.getZip());
//			stmnt.setString(6, branch.getCountry());
//			stmnt.setString(7, branch.getTimezone());
//			stmnt.setString(8, branch.getPhone());
//			stmnt.setString(9, branch.getEmail());
//			stmnt.setString(10, branch.getFax());
//			stmnt.setTimestamp(11, new Timestamp(branch.getOrderDate().getTime()));
//			stmnt.setString(12, branch.getAgreementNumber());
//			stmnt.setInt(13, branch.getAgreementTerm());
//			stmnt.setTimestamp(14, new Timestamp(branch.getCommencementDate().getTime()));
//			stmnt.setTimestamp(15, new Timestamp(branch.getInstallationDate().getTime()));
//			stmnt.setInt(16, branch.getRepeatalarminterval());
//			stmnt.setString(17,branch.getNextEngineer());
//			stmnt.setString(18, branch.getSiteContact());
//			stmnt.setString(19, branch.getRefrigerationContractor());
//			stmnt.setString(20, branch.getHvacContractor());
//			stmnt.setString(21, branch.getMaintenanceManager());
//			stmnt.setString(22, branch.getSiteGroup());
//			stmnt.setInt(23, branch.getAlarmPriority());
//			stmnt.setString(24, branch.getRefrigeratedSystem());
//			stmnt.setString(25, branch.getBranchCode());
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			stmnt=null; 
//			conn= null;
//		}
//		
//	}
//
//	@Override
//	public List<OpeningHours> getOpeningHours(String branch_code) {
//		List<OpeningHours> openHours=new ArrayList<OpeningHours>();
//		String query="SELECT openinghr_id,day,date,opening_time,closing_time FROM Store.OpeningHours WHERE branch_code='"+branch_code+"' ORDER BY  " +
//				"CASE WHEN Day = 'Monday' THEN 1 " +
//				"WHEN Day = 'Tuesday' THEN 2 " +
//				"WHEN Day = 'Wednesday' THEN 3 " +
//				"WHEN Day = 'Thursday' THEN 4 " +
//				"WHEN Day = 'Friday' THEN 5 " +
//				"WHEN Day = 'Saturday' THEN 6 " +
//				"WHEN Day = 'Sunday' THEN 7 " +
//				"WHEN day=  'Christmas' THEN 8 " +
//				"WHEN day=  'Boxing Day' THEN 9 " +
//				"WHEN day=  'New Years Day'  THEN 10 " +
//				"END ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet result = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			result=stmnt.executeQuery(query);
//			//			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//			while (result.next()){
//				OpeningHours new_hours=new OpeningHours(result.getInt("openinghr_id"),result.getString("day"),result.getDate("date"),result.getString("opening_time"),result.getString("closing_time"));
//				openHours.add(new_hours);
//			}
//		}catch (SQLException  e) {
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
//			dbBureauConn = null;
//		}
//		return openHours;
//	}
//
//	public void deleteOpeningHours(int openinghr_id){
//		String query="DELETE FROM Store.OpeningHours WHERE openinghr_id=" + openinghr_id;
//		Connection dbBureauConn = null;
//		Statement stmnt=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	public void addOpeningHours(String newDay,Date exactDate,String openingTime,String closingTime,String branch_code){
//		String query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('"+newDay+"',"+"'"+new java.sql.Date(exactDate.getTime())+"','"+branch_code+"','"+openingTime+"','"+closingTime+"')";
//		Connection dbBureauConn;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	public void modifyOpeningHours(String newDay,Date exactDate,String openingTime,String closingTime,int openinghr_id){
//		String query="UPDATE Store.OpeningHours " +
//				"SET day = '"+newDay + "'"+
//				",date = '" + new java.sql.Date(exactDate.getTime()) +"'"+
//				",opening_time = '" + openingTime + "'"+
//				",closing_time = '" + closingTime + "'"+
//				"WHERE openinghr_id= " + openinghr_id;
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	public void modifyWeekOpeningHours(String openingTime,String closingTime,int openinghr_id){
//		String query="UPDATE Store.OpeningHours " +
//				"SET opening_time = '" + openingTime + "'"+
//				",closing_time = '" + closingTime + "'"+
//				"WHERE openinghr_id= " + openinghr_id;
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
	@Override
	public TimeZone getSiteTimezone(String siteCode) {
		TimeZone tmZone=TimeZone.getDefault();
		String query="SELECT timezone FROM dbo.branches WHERE branch_code='" + siteCode + "'";
		Connection dbBureauConn = null;
		Statement stmnt = null;
		ResultSet result = null;
		try{
			dbBureauConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbBureauConn.createStatement();
			result=stmnt.executeQuery(query);
			while (result.next()){
				tmZone=TimeZone.getTimeZone(result.getString("timezone"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			result = null;
			stmnt = null;
			dbBureauConn = null;
		}
		return tmZone;
	}
//
//
//	@Override
//	public List<OpeningHours> getWeekdayOpeningHours(String branch_code) {
//		List<OpeningHours> weekOpenHours=new ArrayList<OpeningHours>();
//		String query="SELECT openinghr_id,day,date,opening_time,closing_time FROM Store.OpeningHours WHERE branch_code='"+branch_code+"' and date is null";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet result = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			result=stmnt.executeQuery(query);
//			//			DateFormat format = new SimpleDateFormat("HH:mm");
//			while (result.next()){
//				OpeningHours new_hours=new OpeningHours(result.getInt("openinghr_id"),result.getString("day"),result.getDate("date"),result.getString("opening_time"),result.getString("closing_time"));
//				weekOpenHours.add(new_hours);
//			}
//		}catch (SQLException  e) {
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
//			dbBureauConn = null;
//		}
//		return weekOpenHours;
//	}
//
//
//	@Override
//	public List<OpeningHours> getSpecialOpeningHours(String branch_code) {
//		List<OpeningHours> weekOpenHours=new ArrayList<OpeningHours>();
//		String query="SELECT openinghr_id,day,date,opening_time,closing_time FROM Store.OpeningHours WHERE branch_code='"+branch_code+"' and date is not null ORDER BY date";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet result = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			result=stmnt.executeQuery(query);
//			//			DateFormat format = new SimpleDateFormat("HH:mm");
//			while (result.next()){
//				OpeningHours new_hours=new OpeningHours(result.getInt("openinghr_id"),result.getString("day"),result.getDate("date"),result.getString("opening_time"),result.getString("closing_time"));
//				weekOpenHours.add(new_hours);
//			}
//		}catch (SQLException  e) {
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
//			dbBureauConn = null;
//		}
//		return weekOpenHours;
//	}
//
//
//	@Override
//	public void addWeekdayOpeningHours(String day, String branch_code,
//			String opening_time, String closing_time) {
//		String query="INSERT INTO Store.OpeningHours (day,date,branch_code,opening_time,closing_time) VALUES ('"+day+"',"+ null +",'"+branch_code+"','"+opening_time+"','"+closing_time+"')";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//
//	@Override
//	public Branch getSpecificBranch(String branchCode) {
//		Branch specBranch=null;
//		String query="SELECT * FROM branches WHERE branch_code='"+ branchCode +"' and outOfContract=0";
//		Connection dbBureauConn = null;
//		Statement stmnt= null;
//		ResultSet result=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			result=stmnt.executeQuery(query);
//			while (result.next()){
//				specBranch=new Branch(result.getString("branch_code"),result.getString("name"),result.getInt("customer_id"),result.getString("address1"),
//						result.getString("address2"),result.getString("city"),result.getString("zip"),result.getString("country"),result.getString("timezone"),
//						result.getString("fax"),result.getString("phone"),result.getString("email"),result.getDate("OrderDate"),result.getString("AgreementNumber"),
//						result.getInt("AgreementTerm"),result.getDate("CommencementDate"),result.getDate("InstallationDate"),result.getInt("repeatalarminterval"),result.getString("nextEngineer"),result.getString("siteContact"),
//						result.getString("refrigerationContractor"),result.getString("hvacContractor"),result.getString("maintenanceManager"),result.getString("siteGroup"),result.getByte("transferedToNewBureau"),result.getInt("alarmPriority"),
//						result.getBoolean("outOfContract"), result.getString("refrigerated_system"));
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
//			dbBureauConn = null;
//		}
//		return specBranch;
//	}
//
//
//	@Override
//	public List<String> getAllBranchCodes() {
//		List<String> branchCodes=new ArrayList<String>();
//		String query="SELECT branch_code FROM branches WHERE branch_code!='Unknown' and deleted=0 and outOfContract=0";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		ResultSet results=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				String siteCode=results.getString(1);
//				branchCodes.add(siteCode);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			results = null;
//			dbBureauConn = null;
//		}
//		return branchCodes;
//	}
//
//
//	@Override
//	public List<Branch> getAllBranches() {
//		List<Branch> branchCodes=new ArrayList<Branch>();
//		String query="SELECT branch_code,name FROM branches WHERE branch_code!='Unknown' and deleted=0 and outOfContract=0";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		ResultSet results=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				Branch temp=new Branch();
//				temp.setBranchCode(results.getString("branch_code"));
//				temp.setName(results.getString("name"));
//				branchCodes.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return branchCodes;
//	}
//
//
//	@Override
//	public List<String> getNextEngineers() {
//		List<String> engineers = new ArrayList<String>();
//		String query="SELECT DISTINCT next_engineer FROM next_engineer order by next_engineer asc";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		ResultSet results=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				String temp=results.getString("next_engineer");
//				engineers.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return engineers;
//	}
//
//
//	@Override
//	public List<String> getRefrigirationContractors() {
//		List<String> contractors = new ArrayList<String>();
//		String query="SELECT DISTINCT Refrigiration_Contractor FROM RefrigirationContractors order by Refrigiration_Contractor asc";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				String temp=results.getString("Refrigiration_Contractor");
//				contractors.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			results = null;
//			dbBureauConn = null;
//		}
//		return contractors;
//	}
//
//
//	@Override
//	public List<String> getHVACContractors() {
//		List<String> contractors = new ArrayList<String>();
//		String query="SELECT DISTINCT HVAC_Contractor FROM HVAC_Contractors order by HVAC_Contractor asc";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				String temp=results.getString("HVAC_Contractor");
//				contractors.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return contractors;
//	}
//
//
//	@Override
//	public List<String> getMaintenanceManagers() {
//		List<String> managers = new ArrayList<String>();
//		String query="SELECT DISTINCT Maintenance_Manager FROM MaintenanceManagers order by Maintenance_Manager asc";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				String temp=results.getString("Maintenance_Manager");
//				managers.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return managers;
//	}
//
//
//	@Override
//	public List<String> getSiteGroups() {
//		List<String> groups = new ArrayList<String>();
//		String query="SELECT DISTINCT Site_Group FROM SiteGroups order by Site_Group asc";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			results=stmnt.executeQuery();
//			while (results.next()){
//				String temp=results.getString("Site_Group");
//				groups.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return groups;
//	}
//
//
//	@Override
//	public void addCopyBranch(String branchCode, int version,String name, int customer_id,
//			String address1, String address2, String city, String zip,
//			String country, String timezone, String fax, String phone,
//			int contact_id, Date orderDate, String agreementNumber,
//			int agreementTerm, Date commencementDate, Date installationDate,
//			int repeatalarminterval, String nextEngineer,
//			String refrigerationContractor, String hvacContractor,
//			String maintenanceManager, String siteGroup, String refSystem) {
//
//		String query="INSERT INTO branches (branch_code,version,name,customer_id,address1,address2" +
//				",city,zip,country,timezone,phone,fax,contact_id,OrderDate" +
//				",AgreementNumber,AgreementTerm,CommencementDate,InstallationDate,repeatalarminterval,nextEngineer," +
//				"refrigerationContractor,hvacContractor,maintenanceManager,siteGroup, refrigerated_system) " +
//				" VALUES ('" + branchCode + "'," + version + ",'"  + name + "'," + 
//				customer_id + ",'" + address1 + "','" + address2 + "','" + city + "','" +
//				zip + "','" + country + "','" + timezone + "','" + phone + "','" + fax + "'," + contact_id + ",'" + new java.sql.Date(orderDate.getTime()) + "'," +
//				agreementNumber + "," + agreementTerm + ",'" + new java.sql.Date(commencementDate.getTime()) + "','" +
//				new java.sql.Date(installationDate.getTime()) + "',"+ repeatalarminterval + ",'" + nextEngineer + "','" + refrigerationContractor + "','" + hvacContractor  + "','" + maintenanceManager + "','" +siteGroup +"','"+ refSystem+"')";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			stmnt.executeUpdate(query);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//	
//
//	@Override
//	public void addCopyBranchMYSQL(String branchCode, int version,String name, int customer_id,
//			String address1, String address2, String city, String zip,
//			String country, String timezone, String fax, String phone,
//			int contact_id, Date orderDate, String agreementNumber,
//			int agreementTerm, Date commencementDate, Date installationDate,
//			int repeatalarminterval, String nextEngineer,
//			String refrigerationContractor, String hvacContractor,
//			String maintenanceManager, String siteGroup, String refSystem) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		String query="INSERT INTO transferredsites (site_code,version,name,customer_id,address1,address2" +
//				",city,zip,country,timezone,phone,fax,contact_id,OrderDate" +
//				",AgreementNumber,AgreementTerm,CommencementDate,InstallationDate,repeatalarminterval,nextEngineer," +
//				"refrigerationContractor,hvacContractor,maintenanceManager,siteGroup, refrigerated_system, alarmingMethod) " +
//				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
//		try {
//		conn = ConnectionBean.getInstance().getMYSQLConnection();
//		stmnt = conn.prepareStatement(query);
//		stmnt.setString(1, branchCode);
//		stmnt.setInt(2, 1);
//		stmnt.setString(3,  name.replaceAll("'", "''"));
//		stmnt.setInt(4, customer_id);
//		stmnt.setString(5, address1.replaceAll("'", "''"));
//		stmnt.setString(6, address2.replaceAll("'", "''"));
//		stmnt.setString(7, city);
//		stmnt.setString(8, zip);
//		stmnt.setString(9, country);
//		stmnt.setString(10, timezone);
//		stmnt.setString(11, phone);
//		stmnt.setString(12, fax);
//		stmnt.setInt(13, contact_id);
//		stmnt.setTimestamp(14, new Timestamp(orderDate.getTime()));
//		stmnt.setString(15, agreementNumber);
//		stmnt.setInt(16, agreementTerm);
//		stmnt.setTimestamp(17, new Timestamp(commencementDate.getTime()));
//		stmnt.setTimestamp(18, new Timestamp(installationDate.getTime()));
//		stmnt.setInt(19, repeatalarminterval);
//		stmnt.setString(20, nextEngineer);
//		stmnt.setString(21, refrigerationContractor);
//		stmnt.setString(22, hvacContractor);
//		stmnt.setString(23, maintenanceManager);
//		stmnt.setString(24, siteGroup);
//		stmnt.setString(25, refSystem);
//		stmnt.setInt(26, 0);
//		stmnt.executeUpdate();
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	finally {
//		if(stmnt!=null){
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		stmnt = null; 
//		conn = null;
//	}
//	
//	}
//
//
//	@Override
//	public List<NextEngineer> getAllNextEngineers() {
//		List<NextEngineer> nextEngineers = new ArrayList<NextEngineer>();
//		String query="SELECT next_engineer,next_engineer_id FROM next_engineer ORDER BY next_engineer ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			results = stmnt.executeQuery(query);
//			while (results.next()){
//				NextEngineer temp = new NextEngineer(results.getInt("next_engineer_id"), results.getString("next_engineer"));
//				nextEngineers.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return nextEngineers;
//	}
//
//
//	@Override
//	public void addNextEngineer(NextEngineer engineer) {
//		String query="INSERT INTO next_engineer (next_engineer) VALUES(?)";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, engineer.getNextEngineer());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//
//	@Override
//	public void deleteNextEngineer(int engineerId) {
//		String query="DELETE FROM next_engineer WHERE next_engineer_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setInt(1, engineerId);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//
//	@Override
//	public void modifyNextEngineer(NextEngineer engineer) {
//		String query="UPDATE next_engineer SET next_engineer = ? WHERE next_engineer_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, engineer.getNextEngineer());
//			stmnt.setInt(2, engineer.getEngineerId());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//
//	@Override
//	public List<HVAC_Contractor> getAllHvacContractors() {
//		List<HVAC_Contractor> hvacContractors = new ArrayList<HVAC_Contractor>();
//		String query="SELECT HVAC_Contractor,HVAC_Contractor_id FROM HVAC_Contractors ORDER BY HVAC_Contractor ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			results = stmnt.executeQuery(query);
//			while (results.next()){
//				HVAC_Contractor temp = new HVAC_Contractor(results.getInt("HVAC_Contractor_id"), results.getString("HVAC_Contractor"));
//				hvacContractors.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return hvacContractors;
//	}
//
//
//	@Override
//	public void addHvacContractor(HVAC_Contractor contractor) {
//		String query="INSERT INTO HVAC_Contractors (HVAC_Contractor) VALUES(?)";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, contractor.getHvacContractor());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//
//	@Override
//	public void deleteHvacContractor(int contractorId) {
//		String query="DELETE FROM HVAC_Contractors WHERE HVAC_Contractor_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setInt(1,contractorId);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public void modifyHvacContractor(HVAC_Contractor contractor) {
//		String query="UPDATE dbo.HVAC_Contractors SET HVAC_Contractor = ? WHERE HVAC_Contractor_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, contractor.getHvacContractor());
//			stmnt.setInt(2, contractor.getContractorId());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//
//
//	@Override
//	public List<MaintenanceManager> getAllMaintenanceManagers() {
//		List<MaintenanceManager> maintenanceManagers = new ArrayList<MaintenanceManager>();
//		String query="SELECT Maintenance_Manager,Maintenance_Manager_id FROM MaintenanceManagers ORDER BY Maintenance_Manager ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			results = stmnt.executeQuery(query);
//			while (results.next()){
//				MaintenanceManager temp = new MaintenanceManager(results.getInt("Maintenance_Manager_id"), results.getString("Maintenance_Manager"));
//				maintenanceManagers.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return maintenanceManagers;
//	}
//
//
//	@Override
//	public void addMaintenanceManager(MaintenanceManager manager) {
//		String query="INSERT INTO MaintenanceManagers (Maintenance_Manager) VALUES(?)";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, manager.getMaintenanceManager());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public void deleteMaintenanceManager(int managerId) {
//		String query="DELETE FROM MaintenanceManagers WHERE Maintenance_Manager_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setInt(1,managerId);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public void modifyMaintenanceManager(MaintenanceManager manager) {
//		String query="UPDATE MaintenanceManagers SET Maintenance_Manager = ? WHERE Maintenance_Manager_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, manager.getMaintenanceManager());
//			stmnt.setInt(2, manager.getManagerId());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public List<RefrigerationContractor> getAllRefrigererationContractors() {
//		List<RefrigerationContractor> refrigerationContractor = new ArrayList<RefrigerationContractor>();
//		String query="SELECT Refrigiration_Contractor,Refrigiration_Contractor_id FROM RefrigirationContractors ORDER BY Refrigiration_Contractor ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			results = stmnt.executeQuery(query);
//			while (results.next()){
//				RefrigerationContractor temp = new RefrigerationContractor(results.getInt("Refrigiration_Contractor_id"), results.getString("Refrigiration_Contractor"));
//				refrigerationContractor.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return refrigerationContractor;
//	}
//
//	@Override
//	public void addRefrigerationContractor(RefrigerationContractor contractor) {
//		String query="INSERT INTO RefrigirationContractors (Refrigiration_Contractor) VALUES(?)";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, contractor.getRefrigerationContractor());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public void deleteRefrigerationContractor(int contractorId) {
//		String query="DELETE FROM RefrigirationContractors WHERE Refrigiration_Contractor_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setInt(1,contractorId);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//
//	@Override
//	public void modifyRefrigerationContractor(RefrigerationContractor contractor) {
//		String query="UPDATE RefrigirationContractors SET Refrigiration_Contractor = ? WHERE Refrigiration_Contractor_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, contractor.getRefrigerationContractor());
//			stmnt.setInt(2, contractor.getContractorId());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	@Override
//	public List<FixtureType> getAlFixtureTypes() {
//		List<FixtureType> fixtureTypes = new ArrayList<FixtureType>();
//		String query="SELECT Fixture_Type,Fixture_Type_id FROM FixtureTypes ORDER BY Fixture_Type ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			results = stmnt.executeQuery(query);
//			while (results.next()){
//				FixtureType temp = new FixtureType(results.getInt("Fixture_Type_id"), results.getString("Fixture_Type"));
//				fixtureTypes.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return fixtureTypes;
//	}
//
//
//	@Override
//	public void addFixtureType(FixtureType type) {
//		String query="INSERT INTO FixtureTypes (Fixture_Type) VALUES(?)";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, type.getFixtureType());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//
//
//	@Override
//	public void deleteFixtureType(int typeId) {
//		String query="DELETE FROM FixtureTypes WHERE Fixture_Type_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setInt(1,typeId);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//
//
//	@Override
//	public void modifyFixtureType(FixtureType type) {
//		String query="UPDATE FixtureTypes SET Fixture_Type = ? WHERE Fixture_Type_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, type.getFixtureType());
//			stmnt.setInt(2, type.getTypeId());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//
//	///////////////////////////
//	@Override
//	public List<FixtureCategory> getAlFixtureCategories() {
//		List<FixtureCategory> fixtureCategories = new ArrayList<FixtureCategory>();
//		String query="SELECT Fixture_Category,Fixture_Category_id FROM FixtureCategories ORDER BY Fixture_Category ASC";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			results = stmnt.executeQuery(query);
//			while (results.next()){
//				FixtureCategory temp = new FixtureCategory(results.getInt("Fixture_Category_id"), results.getString("Fixture_Category"));
//				fixtureCategories.add(temp);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results = null;
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return fixtureCategories;
//	}
//
//
//	@Override
//	public void addFixtureCategory(FixtureCategory category) {
//		String query="INSERT INTO FixtureCategories (Fixture_Category) VALUES(?)";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, category.getFixtureCategory());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//
//
//	@Override
//	public void deleteFixtureCategory(int categoryId) {
//		String query="DELETE FROM FixtureCategories WHERE Fixture_Category_id=?";
//		System.out.println(query);
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setInt(1,categoryId);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//
//
//	@Override
//	public void modifyFixtureCategory(FixtureCategory category) {
//		String query="UPDATE FixtureCategories SET Fixture_Category = ? WHERE Fixture_Category_id=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, category.getFixtureCategory());
//			stmnt.setInt(2, category.getTypeId());
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//	}
//	////////////////////////////
//	@Override
//	public String getReportSites(int customerId) {
//		String reportSites="";
//		String query="SELECT branch_code,name FROM branches WHERE customer_id="+customerId + " and deleted=0 and outOfContract=0 ORDER BY branch_code";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet result = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			result=stmnt.executeQuery(query);
//			while (result.next()){
//				if (result.isLast()==false){
//					reportSites+=result.getString("branch_code") + "?" + result.getString("name") + ","; 
//				}else{
//					reportSites+=result.getString("branch_code") + "?" + result.getString("name");
//				}
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
//			dbBureauConn = null;
//		}
//		return reportSites;
//	}
//
//	@Override
//	public void setBranchContract(boolean contract,String branchCode) {
//		String query="UPDATE [branches] SET [outOfContract] ="+ (contract?1:0) +" WHERE [branch_code]=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt = dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, branchCode);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//	
//	@Override
//	public void setBranchContractMYSQL(boolean contract,String branchCode) {
//		String query="UPDATE `bureauv2alarms`.`transferredsites` SET outOfContract ="+ (contract?1:0) +" WHERE site_code=?";
//		Connection dbBureauConn = null;
//		PreparedStatement stmnt=null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = dbBureauConn.prepareStatement(query);
//			stmnt.setString(1, branchCode);
//			stmnt.executeUpdate();
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//
//	}
//
//	@Override
//	public String getSiteTelephone(String branchCode) {
//		String telephone=null;
//		String query="SELECT [phone] FROM branches WHERE branch_code='"+ branchCode +"'";
//		Connection dbBureauConn = null;
//		Statement stmnt = null;
//		ResultSet result = null;
//		try{
//			dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbBureauConn.createStatement();
//			result=stmnt.executeQuery(query);
//			while (result.next()){
//				telephone=result.getString("phone");
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			stmnt = null;
//			dbBureauConn = null;
//		}
//		return telephone;
//	}
//
//	public List<String> getSitesCodesFromActiveAlms() {
//		List<String> activeSiteCodes = new ArrayList<String>();
//		String query="SELECT distinct site_code FROM `bureauv2alarms`.`alarming_activealarm`;";
//		Connection dbConn=null;
//		Statement stmnt =null; 
//		ResultSet result =null;
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt= dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while(result.next()) {
//				activeSiteCodes.add(result.getString("site_code"));
//			}
//		}catch(SQLException exception) {
//			exception.printStackTrace();
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
//		return activeSiteCodes;
//	}
//
//	public String getSiteName(String site_code) {
//		String query="SELECT site_name FROM `bureauv2alarms`.`alarming_activealarm` WHERE site_code='"+site_code+"';";
//		String site_name="";
//		Connection dbConn=null;
//		Statement stmnt = null;
//		ResultSet result =null;
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt= dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while(result.next()) {
//				site_name = result.getString("site_name");
//			}
//		}
//		catch(SQLException exception) {
//			exception.printStackTrace();
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
//		return site_name;
//
//
//	}
//
//	public String getAlarmsCount(String site_code) {
//		String query="SELECT count(*) FROM `bureauv2alarms`.`alarming_activealarm` WHERE site_code='"+site_code+"';";
//		String count="";
//		Connection dbConn=null;
//		Statement stmnt = null;
//		ResultSet result =null;
//		try {
//			dbConn=ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt= dbConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while(result.next()) {
//
//				count = Integer.toString(result.getInt(1));
//			}
//		}
//		catch(SQLException exception) {
//			exception.printStackTrace();
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
//		return count;
//	}
//	public void addOpeningHoursNormal(OpeningHours hours){
//		String query="INSERT INTO [Store].[OpeningHours] ([day],[date],[branch_code] ,[opening_time],[closing_time]) " +
//				"VALUES(?,?,?,?,?) ";
//		PreparedStatement stmnt=null; 
//		Connection conn=null; 
//		try {
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, hours.getDay());
//			if(hours.getDate()!=null){
//				stmnt.setTimestamp(2, new Timestamp(hours.getDate().getTime()));
//			}
//			else {
//				stmnt.setNull(2, Types.TIMESTAMP);
//			}
//			stmnt.setString(3, ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getAttribute("sitecode").toString());
//			stmnt.setString(4, hours.getOpeningTime());
//			stmnt.setString(5, hours.getClosingTime());
//			stmnt.executeUpdate();
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally{
//			try {
//				stmnt.close();
//			}
//			catch(SQLException ex) {
//				ex.printStackTrace();
//			}
//			stmnt=null; 
//			conn = null;
//		}
//	}
//	public String getBranchTimeZone(int id) {
//		String timezone="";
//		String query="SELECT timeZone FROM [dbo].[branches] where customer_id="+ id;
//		Statement stmnt = null; 
//		ResultSet rs = null; 
//		Connection conn = null; 
//		try {
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.createStatement();
//			rs = stmnt.executeQuery(query);
//			while(rs.next()) {
//				timezone = rs.getString("timezone");
//			}
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		return timezone;
//	}
//
//	@Override
//	public List<Integer> getDepartmentId(List<String> siteCodes) {
//		Connection conn= null;
//		PreparedStatement stmnt = null; 
//		ResultSet rs = null; 
//		List<Integer> depIds = null; 
//		String query="SELECT keyholderList.keyholder_list_id FROM `bureauv2alarms`.`keyholders_department_keyholderlist` AS keyholderList " +
//				"LEFT JOIN `bureauv2alarms`.`departments` AS dept ON keyholderList.dep_id = dept.dep_id where dept.branch_code IN ("+createPlaceHolders(siteCodes)+")";
//		try {
//			conn = ConnectionBean.getInstance().getMYSQLConnection() ;
//			stmnt = conn.prepareStatement(query);
//			for(int i=1; i<=siteCodes.size(); i++){
//				stmnt.setString(i, siteCodes.get(i-1));
//			}
//			rs = stmnt.executeQuery();
//			depIds = new ArrayList<>();
//			while(rs.next()){
//				depIds.add(rs.getInt("keyholder_list_id"));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally{
//			try {
//				if(stmnt!=null){
//					stmnt.close();
//				}
//				if(rs!=null){
//					rs.close();
//				}
//			}
//			catch(SQLException ex){
//				ex.printStackTrace();
//			}
//		}
//		return depIds;
//	}
//	public List<Integer> getKeyholderListIds(String siteCode) {
//		Connection conn= null;
//		PreparedStatement stmnt = null; 
//		ResultSet rs = null; 
//		List<Integer> depIds = null; 
//		String query="SELECT keyholderList.keyholder_list_id FROM `bureauv2alarms`.`keyholders_department_keyholderlist` AS keyholderList " +
//				"LEFT JOIN `bureauv2alarms`.`departments` AS dept ON keyholderList.dep_id = dept.dep_id where dept.branch_code = ?";
//		try {
//			conn = ConnectionBean.getInstance().getMYSQLConnection() ;
//			stmnt = conn.prepareStatement(query);
//				stmnt.setString(1, siteCode);
//			rs = stmnt.executeQuery();
//			depIds = new ArrayList<>();
//			while(rs.next()){
//				depIds.add(rs.getInt("keyholder_list_id"));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally{
//			try {
//				if(stmnt!=null){
//					stmnt.close();
//				}
//				if(rs!=null){
//					rs.close();
//				}
//			}
//			catch(SQLException ex){
//				ex.printStackTrace();
//			}
//		}
//		return depIds;
//	}
//	private String createPlaceHolders(List<String> list){
//		StringBuilder builder = new StringBuilder(); 
//		for(int i=0; i<list.size(); i++){
//			builder.append("?");
//			if(i!=list.size()-1){
//				builder.append(",");
//			}
//		}
//		return builder.toString();
//	}
//
//	@Override
//	public void configureSplecialFlag(Map<String, Boolean> map) {
//		final int batchSize = 1000;
//		int count = 0;
//		Connection conn = null; 
//		PreparedStatement stmnt = null;
//		String query="UPDATE [dbo].[branches] SET specialKeyholderFlag = ? WHERE branch_code =?";
//		try {
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			for(Map.Entry<String, Boolean> en: map.entrySet()){
//				stmnt.setInt(1, en.getValue()?1:0);
//				stmnt.setString(2, en.getKey());
//				stmnt.addBatch();
//				if(++count % batchSize == 0) {
//			        stmnt.executeBatch();
//			    }
//			}
//			stmnt.executeBatch();
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				if(stmnt!=null){
//					stmnt.clearBatch();
//					stmnt.close();
//				}
//			}
//			catch(SQLException ex) {
//				ex.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public String getBranchCode(int deptId) {
//		String query= "SELECT branch_code FROM `bureauv2alarms`.`departments` where dep_id= ?;";
//		String branchCode= null; 
//		Connection conn = null; 
//		PreparedStatement stmnt = null;
//		ResultSet rs = null; 
//		try {
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setInt(1, deptId);
//			rs = stmnt.executeQuery();
//			if(rs.next()){
//				branchCode = rs.getString("branch_code");
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return branchCode;
//	}
//
//	@Override
//	public int getDepId(String siteCode) {
//		int dep_id=0;
//		String query="SELECT dep_id FROM `bureauv2alarms`.`departments` WHERE branch_code = ?";
//		Connection conn = null; 
//		PreparedStatement stmnt = null;
//		ResultSet rs = null; 
//		try {
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, siteCode);
//			rs = stmnt.executeQuery();
//			if(rs.next()){
//				dep_id = rs.getInt("dep_id");
//			}
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		return dep_id;
//	}
//
//	@Override
//	public List<String> getBranchNames(int customerId) {
//		// TODO Auto-generated method stub
//		List<String> siteNames = null; 
//		String query="SELECT name FROM [dbo].[branches] WHERE customer_id = ?";
//		Connection conn = null; 
//		PreparedStatement stmnt = null;
//		ResultSet rs = null; 
//		try {
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setInt(1, customerId);
//			rs = stmnt.executeQuery();
//			siteNames = new ArrayList<String>();
//			while(rs.next()){
//			    siteNames.add(rs.getString("name"));
//			}
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		return siteNames;
//	}
//
//	@Override
//	public String getBranchCode(String branchName) {
//		// TODO Auto-generated method stub
//		String branchCode =null;
//		String query ="SELECT  branch_code FROM [dbo].[branches] WHERE name =? and outOfContract=? and deleted= ?" ;
//		Connection conn = null;
//		PreparedStatement stmnt = null;
//		ResultSet result = null;
//		try {
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, branchName);
//			stmnt.setInt(2, 0);
//			stmnt.setInt(3, 0);
//			 result = stmnt.executeQuery();
//				if(result.next()){
//					branchCode = result.getString("branch_code");
//				}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			try {
//			    if(stmnt != null){
//				    stmnt.close();
//				}
//			    if(result != null){
//			    	result.close();
//			    }
//			   }
//			catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			finally {
//				result=null;
//				stmnt = null; 
//				conn = null;
//			}
//			}
//		return branchCode;
//	}
//
//	@Override
//	public void modifyBranchMaintenanceMngr(String currName, String updatedName) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query = "UPDATE [dbo].[branches] SET  maintenanceManager =  ? WHERE maintenanceManager =  ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, updatedName);
//			stmnt.setString(2, currName);
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt = null; 
//				conn = null; 
//			}
//			 query = "UPDATE `bureauv2alarms`.`transferredsites` SET  maintenanceManager =  ? WHERE maintenanceManager =  ?";
//				conn = ConnectionBean.getInstance().getMYSQLConnection();
//				stmnt = conn.prepareStatement(query);
//				stmnt.setString(1, updatedName);
//				stmnt.setString(2, currName);
//				stmnt.executeUpdate();
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				finally {
//					stmnt = null; 
//					conn = null;
//				}
//			}
//		}
//		
//	}
//
//	@Override
//	public void modifyBranchNextEngineer(String currName, String updatedName) {
//		// TODO Auto-generated method stub
//				Connection conn = null; 
//				PreparedStatement stmnt = null; 
//				try {
//					String query = "UPDATE [dbo].[branches] SET  nextEngineer =  ? WHERE nextEngineer =  ?";
//					conn = ConnectionBean.getInstance().getBureauConnection();
//					stmnt = conn.prepareStatement(query);
//					stmnt.setString(1, updatedName);
//					stmnt.setString(2, currName);
//					stmnt.executeUpdate();
//					if(stmnt!=null) {
//						stmnt.close();
//						stmnt = null; 
//						conn = null; 
//					}
//					query = "UPDATE `bureauv2alarms`.`transferredsites` SET  nextEngineer =  ? WHERE nextEngineer =  ?";
//					conn = ConnectionBean.getInstance().getMYSQLConnection();
//					stmnt = conn.prepareStatement(query);
//					stmnt.setString(1, updatedName);
//					stmnt.setString(2, currName);
//					stmnt.executeUpdate();
//				}
//				catch(SQLException ex) {
//					ex.printStackTrace();
//				}
//				finally {
//					if(stmnt!=null){
//						try {
//							stmnt.close();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						finally {
//							stmnt = null; 
//							conn = null;
//						}
//					}
//				}
//				
//	}
//
//	@Override
//	public void modifyBranchHVACContractors(String currName, String updatedName) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query = "UPDATE [dbo].[branches] SET  hvacContractor =  ? WHERE hvacContractor =  ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, updatedName);
//			stmnt.setString(2, currName);
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt= null; 
//				conn = null;
//			}
//			query = "UPDATE `bureauv2alarms`.`transferredsites` SET  hvacContractor =  ? WHERE hvacContractor =  ?";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, updatedName);
//			stmnt.setString(2, currName);
//			stmnt.executeUpdate();
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				finally {
//					stmnt = null; 
//					conn = null;
//				}
//			}
//		}		
//	}
//
//	@Override
//	public void modifyBranchFridgeContractors(String currName,
//			String updatedName) {
//		// TODO Auto-generated method stub
//				Connection conn = null; 
//				PreparedStatement stmnt = null; 
//				try {
//					String query = "UPDATE [dbo].[branches] SET  refrigerationContractor =  ? WHERE refrigerationContractor =  ?";
//					conn = ConnectionBean.getInstance().getBureauConnection();
//					stmnt = conn.prepareStatement(query);
//					stmnt.setString(1, updatedName);
//					stmnt.setString(2, currName);
//					stmnt.executeUpdate();
//					if(stmnt!=null){
//						stmnt.close();
//						stmnt = null; 
//						conn = null; 
//					}
//					query= "UPDATE `bureauv2alarms`.`transferredsites` SET  refrigerationContractor =  ? WHERE refrigerationContractor =  ?";
//					conn = ConnectionBean.getInstance().getMYSQLConnection();
//					stmnt = conn.prepareStatement(query);
//					stmnt.setString(1, updatedName);
//					stmnt.setString(2, currName);
//					stmnt.executeUpdate();
//				}
//				catch(SQLException ex) {
//					ex.printStackTrace();
//				}
//				finally {
//					if(stmnt!=null){
//						try {
//							stmnt.close();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						finally {
//							stmnt = null; 
//							conn = null;
//						}
//					}
//				}		
//		
//	}
//
//	@Override
//	public void removeBranchMaintenanceMngr(String name) {
//		// TODO Auto-generated method stub
//				Connection conn = null; 
//				PreparedStatement stmnt = null; 
//				try {
//					String query = "UPDATE [dbo].[branches] SET  maintenanceManager =  ? WHERE maintenanceManager =  ?";
//					conn = ConnectionBean.getInstance().getBureauConnection();
//					stmnt = conn.prepareStatement(query);
//					stmnt.setString(1, "");
//					stmnt.setString(2, name);
//					stmnt.executeUpdate();
//					if(stmnt!=null){
//						stmnt.close();
//						stmnt = null; 
//						conn = null; 
//					}
//					query= "UPDATE `bureauv2alarms`.`transferredsites` SET  maintenanceManager =  ? WHERE maintenanceManager =  ?";
//					conn = ConnectionBean.getInstance().getMYSQLConnection();
//					stmnt = conn.prepareStatement(query);
//					stmnt.setString(1, "");
//					stmnt.setString(2, name);
//					stmnt.executeUpdate();
//				}
//				catch(SQLException ex) {
//					ex.printStackTrace();
//				}
//				finally {
//					if(stmnt!=null){
//						try {
//							stmnt.close();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						finally {
//							stmnt = null; 
//							conn = null;
//						}
//					}
//				}
//		
//	}
//
//	@Override
//	public void removeBranchNextEngineer(String name) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query = "UPDATE [dbo].[branches] SET  nextEngineer =  ? WHERE nextEngineer =  ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, name);
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt = null; 
//				conn = null; 
//			}
//			query = "UPDATE `bureauv2alarms`.`transferredsites` SET  nextEngineer =  ? WHERE nextEngineer =  ?";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, name);
//			stmnt.executeUpdate();
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				finally {
//					stmnt = null; 
//					conn = null;
//				}
//			}
//		}
//		
//	}
//
//	@Override
//	public void removeBranchHVACContractor(String name) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query = "UPDATE [dbo].[branches] SET  hvacContractor =  ? WHERE hvacContractor =  ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, name);
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt = null; 
//				conn = null; 
//			}
//			 query = "UPDATE `bureauv2alarms`.`transferredsites` SET  hvacContractor =  ? WHERE hvacContractor =  ?";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, name);
//			stmnt.executeUpdate();
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				finally {
//					stmnt = null; 
//					conn = null;
//				}
//			}
//		}
//		
//	}
//
//	@Override
//	public void removeBranchRefContractor(String name) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query = "UPDATE [dbo].[branches] SET  refrigerationContractor =  ? WHERE refrigerationContractor =  ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, name);
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt =null; 
//				conn = null; 
//			}
//			query="UPDATE `bureauv2alarms`.`transferredsites` SET  refrigerationContractor =  ? WHERE refrigerationContractor =  ?";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, name);
//			stmnt.executeUpdate();
//		}
//		catch(SQLException ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				finally {
//					stmnt = null; 
//					conn = null;
//				}
//			}
//		}		
//	}
//
//	@Override
//	public List<RefrigeratedSystem> populateRefSystems() {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		ResultSet result = null; 
//		List<RefrigeratedSystem> refSystems = null;
//		try {
//			String query="SELECT * FROM [dbo].[RefrigiratedSystems];";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			result = stmnt.executeQuery();
//			if(result!=null){
//				refSystems = new ArrayList<RefrigeratedSystem>();
//				while(result.next()){
//					refSystems.add(new RefrigeratedSystem(result.getString("refrigerated_system"), result.getInt("refrigerated_system_id")));
//				}
//			}
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			if(result!=null){
//				try {
//					result.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			stmnt = null; 
//			result = null; 
//			conn = null;
//		}
//		return refSystems;
//	}
//
//	@Override
//	public void addRefSystem(RefrigeratedSystem refSystem) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query="INSERT INTO [dbo].[RefrigiratedSystems] (refrigerated_system) VALUES(?);";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, refSystem.getRefrigeratedSystem());
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stmnt = null; 
//				conn = null; 
//			}
//		}
//		
//	}
//
//	@Override
//	public void delRefSystem(RefrigeratedSystem refSystem) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query="DELETE FROM [dbo].[RefrigiratedSystems] WHERE refrigerated_system = ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, refSystem.getRefrigeratedSystem());
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stmnt = null; 
//				conn = null; 
//			}
//		}
//	}
//	@Override
//	public void delBranchRefSystem(RefrigeratedSystem refSystem) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query="UPDATE [dbo].[branches] SET refrigerated_system = ?  WHERE refrigerated_system = ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, refSystem.getRefrigeratedSystem());
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt = null; 
//				conn = null; 
//			}
//			query="UPDATE `bureauv2alarms`.`transferredsites` SET refrigerated_system = ?  WHERE refrigerated_system = ?";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, "");
//			stmnt.setString(2, refSystem.getRefrigeratedSystem());
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stmnt = null; 
//				conn = null; 
//			}
//		}
//	}
//
//
//
//
//	@Override
//	public void modifyRefSystem(RefrigeratedSystem refSystem) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query="UPDATE [dbo].[RefrigiratedSystems] SET  refrigerated_system = ? WHERE refrigerated_system_id = ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, refSystem.getRefrigeratedSystem());
//			stmnt.setInt(2, refSystem.getRefrigeratedSystemId());
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stmnt = null; 
//				conn = null; 
//			}
//		}	
//	}
//
//	@Override
//	public void modifyBranchRefSystem(String currName, String updatedName) {
//		// TODO Auto-generated method stub
//		Connection conn = null; 
//		PreparedStatement stmnt = null; 
//		try {
//			String query="UPDATE [dbo].[branches] SET refrigerated_system = ? WHERE refrigerated_system = ?";
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, updatedName);
//			stmnt.setString(2, currName);
//			stmnt.executeUpdate();
//			if(stmnt!=null){
//				stmnt.close();
//				stmnt =null; 
//				conn = null;
//			}
//			query="UPDATE  `bureauv2alarms`.`transferredsites` SET refrigerated_system = ? WHERE refrigerated_system = ?";
//			conn = ConnectionBean.getInstance().getMYSQLConnection();
//			stmnt = conn.prepareStatement(query);
//			stmnt.setString(1, updatedName);
//			stmnt.setString(2, currName);
//			stmnt.executeUpdate();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			if(stmnt!=null){
//				try {
//					stmnt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stmnt = null; 
//				conn = null; 
//			}
//		}	
//	}
//
//	@Override
//	public List<String> getSiteCode(int customer_id , String siteGroup) {
//		// TODO Auto-generated method stub
//		List<String> sites = null;
//		Connection conn = null; 
//		String query="SELECT [branch_code] FROM [dbo].[branches] WHERE customer_id = ? AND siteGroup = ?";
//		try {
//			conn = ConnectionBean.getInstance().getBureauConnection();
//			try(PreparedStatement stmnt = conn.prepareStatement(query)) {
//				stmnt.setInt(1, customer_id);
//				stmnt.setString(2, siteGroup);
//				try(ResultSet rs = stmnt.executeQuery()){
//					if(rs!=null){
//						sites = new ArrayList<String>();
//						while(rs.next()){
//							sites.add(rs.getString("branch_code"));
//						}
//					}
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			conn = null; 
//		}
//		return sites;
//	}
}

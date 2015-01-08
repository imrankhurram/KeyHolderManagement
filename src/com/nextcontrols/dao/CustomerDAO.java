package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.domain.Customer;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class CustomerDAO implements ICustomerDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private Connection dbBureauConn=null;
	private static ICustomerDAO instance;

	private CustomerDAO() {
	}

	public static ICustomerDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new CustomerDAO();
		}
	}

	public static void setInstance(ICustomerDAO inst) {
		instance = inst;
	}

	@Override
	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		String query = "SELECT * FROM [customers] WHERE [name]<>'SITELIST' and [deleted]=0 ORDER BY [name]";
		Connection dbBureauConn = null;
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbBureauConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbBureauConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Customer db_customer = new Customer(
						result.getInt("customer_id"), result.getInt("version"),
						result.getString("name"),
						result.getString("businesstype"));
				customers.add(db_customer);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
		return customers;
	}

	// @Override
	// public int checkCustomer(String siteCode) {
	// // String query =
	// "select b.customer_id from `bureauv2alarms`.`alarming_activealarm` as a INNER JOIN `bureauv2alarms`.`transferredsites` as b on a.site_code = b.site_code where a.alarm_id='"+alarm_id+"'";
	// String
	// query="SELECT customer_id FROM  `bureauv2alarms`.`transferredsites` WHERE site_code = '"+siteCode+"' ";
	// int customer_id=0;
	// Connection dbConn=null;
	// Statement stmnt = null;
	// ResultSet rs = null;
	// try {
	// dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt = dbConn.createStatement();
	// rs = stmnt.executeQuery(query);
	// while(rs.next()) {
	// customer_id= rs.getInt("customer_id");
	// }
	// }
	// catch(SQLException ex) {
	// ex.printStackTrace();
	// }
	// finally {
	// try {
	// rs.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// rs=null;
	// dbConn=null;
	// stmnt=null;
	// }
	// return customer_id;
	// }

	// @Override
	// public void addCustomer(int version, String name, String businesstype) {
	// /*dbConnect();
	// String query="INSERT INTO [customers]([version],[name],[businesstype])" +
	// "VALUES ("+ version + ",'" + name + "','" + businesstype +"')";
	// Statement stmnt = null;
	// try{
	// stmnt=dbBureauConn.createStatement();
	// stmnt.executeUpdate(query);
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// stmnt = null;
	// dbBureauConn = null;
	// }*/
	// Connection dbBureauConn = null;
	// PreparedStatement pstmt=null;
	// try{
	// String query="INSERT INTO [customers]([version],[name],[businesstype])" +
	// "VALUES (?,?,?)";
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// pstmt = dbBureauConn.prepareStatement (query);
	// pstmt.setInt(1, version);
	// pstmt.setString(2, name);
	// pstmt.setString(3, businesstype);
	// pstmt.executeUpdate ();
	//
	// }catch (SQLException e){
	// e.printStackTrace();
	// }finally{
	// try {
	// pstmt.close ();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// pstmt=null;
	// dbBureauConn=null;
	// }
	// }
	//
	// @Override
	// public void deleteCustomer(int customer_id) {
	// String query="UPDATE [customers] SET [deleted]=1 WHERE [customer_id]=" +
	// customer_id;
	// Connection dbBureauConn = null;
	// Statement stmnt = null;
	// try{
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// stmnt=dbBureauConn.createStatement();
	// stmnt.executeUpdate(query);
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// stmnt = null;
	// dbBureauConn = null;
	// }
	//
	// }
	//
	// @Override
	// public void modifyCustomer(int customer_id, int version, String name,
	// String businesstype) {
	//
	// /*dbConnect();
	// String query="UPDATE [dbo].[customers] SET [version] = " + version +
	// ",[name]='" + name + "', [businesstype]='" + businesstype +
	// "' WHERE [customer_id]=" + customer_id;
	// Statement stmnt = null;
	// try{
	// stmnt=dbBureauConn.createStatement();
	// stmnt.executeUpdate(query);
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// stmnt = null;
	// dbBureauConn = null;
	// }*/
	// Connection dbBureauConn = null;
	// PreparedStatement pstmt=null;
	// try{
	// String
	// query="UPDATE [dbo].[customers] SET [version] = ?,[name]=?, [businesstype]=? WHERE [customer_id]=?";
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// pstmt = dbBureauConn.prepareStatement (query);
	// pstmt.setInt(1, version);
	// pstmt.setString(2, name);
	// pstmt.setString(3, businesstype);
	// pstmt.setInt(4, customer_id);
	// pstmt.executeUpdate ();
	//
	// }catch (SQLException e){
	// e.printStackTrace();
	// }finally{
	// try {
	// pstmt.close ();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// pstmt=null;
	// dbBureauConn=null;
	// }
	// }
	//
	// @Override
	// public Customer getSpecificCustomer(int customer_id) {
	// Customer specCustomer=null;
	// String query="SELECT * FROM [customers] WHERE [customer_id]=?";
	// Connection dbBureauConn = null;
	// PreparedStatement stmnt=null;
	// ResultSet result = null;
	// try{
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// stmnt=dbBureauConn.prepareStatement(query);
	// stmnt.setInt(1, customer_id);
	// result=stmnt.executeQuery();
	// while (result.next()){
	// specCustomer=new
	// Customer(result.getInt("customer_id"),result.getInt("version"),result.getString("name"),result.getString("businesstype"));
	// }
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// result.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// result = null;
	// stmnt = null;
	// dbBureauConn = null;
	// }
	// return specCustomer;
	// }
	//
	// @Override
	// public boolean checkIfWaitrose(int customer_id) {
	// Customer specCustomer=null;
	// String
	// query="SELECT * FROM [customers] WHERE [customer_id]=? and [name]='Waitrose'";
	// Connection dbBureauConn = null;
	// PreparedStatement stmnt = null;
	// ResultSet result = null;
	// try{
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// stmnt=dbBureauConn.prepareStatement(query);
	// stmnt.setInt(1, customer_id);
	// result=stmnt.executeQuery();
	// while (result.next()){
	// specCustomer=new
	// Customer(result.getInt("customer_id"),result.getInt("version"),result.getString("name"),result.getString("businesstype"));
	// }
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// result.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// result = null;
	// stmnt = null;
	// dbBureauConn = null;
	// }
	//
	// if (specCustomer==null){
	// return false;
	// }else{
	// return true;
	// }
	// }
	//
	// @Override
	//
	// public List<CustomerBusinessType> getBusinessTypes() {
	// List<CustomerBusinessType> types = new ArrayList<CustomerBusinessType>();
	// String
	// query="SELECT * FROM `bureauv2alarms`.`alarming_business_type` order by business_type_id asc;";
	// Connection dbConn=null;
	// PreparedStatement stmnt=null;
	// ResultSet result=null;
	// try{
	// dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt = dbConn.prepareStatement(query);
	// result=stmnt.executeQuery();
	// while (result.next()){
	// CustomerBusinessType temp = new
	// CustomerBusinessType(result.getInt("business_type_id"),result.getString("business_type"));
	// types.add(temp);
	// }
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// result.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbConn=null;
	// stmnt=null;
	// result=null;
	// }
	// return types;
	// }
	// @Override
	// public String getBusinessType(int customer_id) {
	// String businessType=null;
	// String
	// query="SELECT businesstype FROM [customers] where [customer_id]="+customer_id;
	// Connection dbBureauConn = null;
	// Statement stmnt=null;
	// ResultSet result=null;
	// try{
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// stmnt = dbBureauConn.createStatement();
	// result=stmnt.executeQuery(query);
	// while(result.next()) {
	// businessType= result.getString("businesstype");
	// }
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// result.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbBureauConn=null;
	// stmnt=null;
	// result=null;
	// }
	//
	// return businessType;
	// }
	//
	// @Override
	// public void addBusinessType(CustomerBusinessType newType) {
	// String
	// query="INSERT INTO `bureauv2alarms`.`alarming_business_type`(`business_type`) VALUES(?);";
	// Connection dbConn=null;
	// PreparedStatement stmnt = null;
	// try{
	// dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt = dbConn.prepareStatement(query);
	// stmnt.setString(1, newType.getBusinessType());
	// stmnt.executeUpdate();
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbConn=null;
	// stmnt=null;
	// }
	// }
	//
	// @Override
	// public void deleteBusinessType(int typeId) {
	// String
	// query="DELETE FROM `bureauv2alarms`.`alarming_business_type` WHERE `business_type_id`=?;";
	// Connection dbConn=null;
	// PreparedStatement stmnt = null;
	// try{
	// dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt = dbConn.prepareStatement(query);
	// stmnt.setInt(1, typeId);
	// stmnt.executeUpdate();
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbConn=null;
	// stmnt=null;
	// }
	// }
	//
	// @Override
	// public void modifyBusinessType(CustomerBusinessType type) {
	// String
	// query="UPDATE `bureauv2alarms`.`alarming_business_type` SET `business_type` = ? WHERE `business_type_id`=?;";
	// Connection dbConn=null;
	// PreparedStatement stmnt = null;
	// try{
	// dbConn=ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt = dbConn.prepareStatement(query);
	// stmnt.setString(1, type.getBusinessType());
	// stmnt.setInt(2,type.getTypeId());
	// stmnt.executeUpdate();
	// }catch (SQLException e) {
	// e.printStackTrace();
	// }finally{
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbConn=null;
	// stmnt=null;
	// }
	// }
	//
	@Override
	public String getCustomerNameFromSite(String branchCode) {
		String query = "SELECT cust.name FROM [customers] cust INNER JOIN [branches] branch ON cust.customer_id=branch.customer_id WHERE branch.branch_code='"
				+ branchCode + "'";
		String customerName = null;
		Connection dbBureauConn = null;
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbBureauConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbBureauConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				customerName = result.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbBureauConn = null;
			stmnt = null;
			result = null;
		}
		return customerName;
	}

	// @Override
	// public int getCustomerId(String branchCode) {
	// int customer_id=0;
	// String query=
	// "SELECT *FROM [customers] cust INNER JOIN [branches] branch ON cust.customer_id=branch.customer_id WHERE branch.branch_code='"+branchCode+"'";
	// Connection dbBureauConn = null;
	// Statement stmnt = null;
	// ResultSet result = null;
	// try {
	// dbBureauConn=ConnectionBean.getInstance().getBureauConnection();
	// stmnt = dbBureauConn.createStatement();
	// result = stmnt.executeQuery(query);
	// while(result.next()) {
	// customer_id = result.getInt("customer_id");
	// }
	// }
	// catch(SQLException ex) {
	// ex.printStackTrace();
	// }
	// finally {
	// try {
	// result.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbBureauConn=null;
	// stmnt=null;
	// result = null;
	// }
	// return customer_id;
	// }
	// @Override
	// public String getTimeZonesForUsSites(String branchCode) {
	// String timeZone="";
	// String query="SELECT timezone FROM [dbo].[branches] WHERE branch_code=?";
	// PreparedStatement stmnt=null;
	// Connection conn = null;
	// ResultSet rs = null;
	// try {
	// conn = ConnectionBean.getInstance().getBureauConnection();
	// stmnt = conn.prepareStatement(query);
	// stmnt.setString(1, branchCode);
	// rs = stmnt.executeQuery();
	// if(rs.next()){
	// timeZone = rs.getString("timezone");
	// }
	// }
	// catch(SQLException ex) {
	// ex.printStackTrace();
	// }
	// finally{
	// try{
	// if(stmnt!=null){
	// stmnt.close();
	// stmnt=null;
	// }
	// if(rs!=null){
	// rs.close();
	// rs=null;
	// }
	// }
	// catch(SQLException ex) {
	// ex.printStackTrace();
	// }
	// }
	// return timeZone;
	// }
	// @Override
	// public List<Customer> getCustomers(long businessType) {
	// String
	// query="SELECT * FROM `bureauv2alarms`.`customers` as cust INNER JOIN " +
	// "`bureauv2alarms`.`alarming_business_type` as business ON cust.businesstype = business.business_type "
	// +
	// "WHERE business_type_id=" +businessType+ " ORDER BY name" ;
	// PreparedStatement stmnt=null;
	// Connection conn = null;
	// ResultSet result = null;
	// List<Customer> custList=null;
	// try {
	// conn = ConnectionBean.getInstance().getAlarmMYSQLConnection();
	// stmnt = conn.prepareStatement(query);
	// result = stmnt.executeQuery();
	// custList = new ArrayList<Customer>();
	// while(result.next()){
	// Customer db_customer=new
	// Customer(result.getInt("customer_id"),result.getInt("version"),result.getString("name"),result.getString("businesstype"));
	// custList.add(db_customer);
	// }
	// }
	// catch(SQLException ex) {
	// ex.printStackTrace();
	// }
	// finally {
	// try{
	// if(stmnt!=null){
	// stmnt.close();
	// stmnt=null;
	// }
	// if(result!=null){
	// result.close();
	// result=null;
	// }
	// }
	// catch(SQLException ex) {
	// ex.printStackTrace();
	// }
	// }
	// return custList;
	// }
	// @Override
	// public String getBusinessTypeName(int businessTypeId) {
	// // TODO Auto-generated method stub
	// String name=null;
	// Connection conn = null;
	// PreparedStatement stmnt = null;
	// ResultSet result = null;
	// String
	// query="SELECT business_type FROM `bureauv2alarms`.`alarming_business_type` WHERE business_type_id =?;";
	// try {
	// conn = ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt = conn.prepareStatement(query);
	// stmnt.setInt(1, businessTypeId);
	// result = stmnt.executeQuery();
	// if(result.next()){
	// name = result.getString("business_type");
	// }
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// finally{
	// try {
	// result.close();
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// stmnt = null;
	// result = null;
	// conn = null;
	// }
	// return name;
	// }

}

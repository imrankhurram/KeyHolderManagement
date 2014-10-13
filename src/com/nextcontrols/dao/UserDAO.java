package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nextcontrols.domain.Customer;
import com.nextcontrols.domain.User;
import com.nextcontrols.utility.ServiceProperties;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class UserDAO implements IUserDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerId;
	private static IUserDAO instance;

	public static IUserDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new UserDAO();
		}
	}

	public static void setInstance(IUserDAO ins) {
		instance = ins;
	}

	private UserDAO() {
	}

	public List<Integer> getCustomerIds(int userId) {
		List<Integer> customerList = new ArrayList<Integer>();
		String query = "SELECT * FROM [user_customer] WHERE [user_id]="
				+ userId;
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				customerList.add(results.getInt("customer_id"));
				// System.out.println("user id: "+newUser.getUserId()+"--pin expires: "+results.getTimestamp("pincodeExpires"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return customerList;

	}

	@Override
	public List<Customer> getCustomerOfUser(int userId) {
		List<Customer> customerList = new ArrayList<Customer>();
		String query = "SELECT * FROM [user_customer] as uc join [Customers] as cust on uc.customer_id=cust.customer_id WHERE [user_id]="
				+ userId;
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				Customer cust = new Customer(results.getInt("customer_id"),
						results.getInt("version"), results.getString("name"),
						results.getString("businesstype"));
				customerList.add(cust);
				// System.out.println("user id: "+newUser.getUserId()+"--pin expires: "+results.getTimestamp("pincodeExpires"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return customerList;

	}

	@Override
	public List<User> getUserList(List<Integer> customerIds) {
		// int customer_id=Integer.parseInt(getCustomerId());
		StringBuilder customerIdsList = new StringBuilder("(");
		for (Integer id : customerIds) {
			customerIdsList.append("'");
			customerIdsList.append(id);
			customerIdsList.append("'");
			customerIdsList.append(",");
		}
		customerIdsList.replace(customerIdsList.lastIndexOf(","),
				customerIdsList.length(), ")");
		List<User> userList = new ArrayList<User>();
		String query = "SELECT * FROM [users] join [customers] on users.customer_id=customers.customer_id WHERE users.customer_id IN"
				+ customerIdsList.toString()
				+ " order by customers.name, users.firstName, users.lastName";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				User newUser = new User(results.getInt("user_id"),
						results.getString("userWebType"),
						results.getString("userConfgType"),
						results.getString("title"),
						results.getString("firstName"),
						results.getString("lastName"),
						results.getString("email"),
						results.getString("workphone"),
						results.getString("contactNumber"),
						results.getString("mobilePhone"),
						results.getString("address"),
						results.getString("city"), results.getString("zip"),
						results.getString("county"),
						results.getString("country"),
						results.getString("username"),
						results.getString("password"),
						results.getByte("termsAndConditions"),
						results.getByte("termsAndConditionsOfService"),
						results.getInt("customer_id"),
						results.getString("pincode"),
						results.getByte("enabled"),
						results.getTimestamp("passwordExpires"),
						results.getTimestamp("pincodeExpires"),
						results.getShort("pincodeFailureCount"),
						results.getByte("isdeleted"),
						results.getString("userBureauType"));
				newUser.setDivisionName(results.getString("name"));
				userList.add(newUser);
				// System.out.println("user id: "+newUser.getUserId()+"--pin expires: "+results.getTimestamp("pincodeExpires"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return userList;
	}

	@Override
	public List<Integer> getUsersListInCustomer(int userId) {
		List<Integer> userIdsList = new ArrayList<Integer>();
		String query = "SELECT a.user_id FROM [users] as a join [users] as u on a.customer_id=u.customer_id WHERE u.user_id="
				+ userId;
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				// User newUser=new User(results.getInt("user_id"),
				// results.getString("userWebType"),
				// results.getString("userConfgType"),results.getString("title"),results.getString("firstName"),
				// results.getString("lastName"),
				// results.getString("email"),
				// results.getString("workphone"),results.getString("contactNumber"),results.getString("mobilePhone"),results.getString("address"),
				// results.getString("city"),results.getString("zip"),results.getString("county"),
				// results.getString("country"), results.getString("username"),
				// results.getString("password"),
				// results.getByte("termsAndConditions"),
				// results.getByte("termsAndConditionsOfService"),results.getInt("customer_id"),
				// results.getString("pincode"), results.getByte("enabled"),
				// results.getTimestamp("passwordExpires"),
				// results.getTimestamp("pincodeExpires"),
				// results.getShort("pincodeFailureCount"),
				// results.getByte("isdeleted"),
				// results.getString("userBureauType"));
				// newUser.setDivisionName(results.getString("name"));
				userIdsList.add((Integer) results.getInt("user_id"));
				// System.out.println("user id: "+newUser.getUserId()+"--pin expires: "+results.getTimestamp("pincodeExpires"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return userIdsList;
	}

	public List<Customer> getCustomerList() {
		List<Customer> customerList = new ArrayList<Customer>();
		String query = "SELECT * FROM [customers] order by name";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				Customer customer = new Customer(results.getInt("customer_id"),
						results.getInt("version"), results.getString("name"),
						results.getString("businesstype"));

				customerList.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return customerList;
	}

	public List<User> getUserList() {
		List<User> userList = new ArrayList<User>();
		String query = "SELECT * FROM [users]";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				User newUser = new User(results.getInt("user_id"),
						results.getString("userWebType"),
						results.getString("userConfgType"),
						results.getString("title"),
						results.getString("firstName"),
						results.getString("lastName"),
						results.getString("email"),
						results.getString("workphone"),
						results.getString("contactNumber"),
						results.getString("mobilePhone"),
						results.getString("address"),
						results.getString("city"), results.getString("zip"),
						results.getString("county"),
						results.getString("country"),
						results.getString("username"),
						results.getString("password"),
						results.getByte("termsAndConditions"),
						results.getByte("termsAndConditionsOfService"),
						results.getInt("customer_id"),
						results.getString("pincode"),
						results.getByte("enabled"),
						results.getTimestamp("passwordExpires"),
						results.getTimestamp("pincodeExpires"),
						results.getShort("pincodeFailureCount"),
						results.getByte("isdeleted"),
						results.getString("userBureauType"));
				userList.add(newUser);
				// System.out.println("user id: "+newUser.getUserId()+"--pin expires: "+results.getTimestamp("pincodeExpires"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return userList;
	}

	public Map<Integer, String> getDivisionNames(List<Integer> customerIds) {
		Map<Integer, String> divisionNames = new HashMap<Integer, String>();
		// if(customerIds==null)
		// return departmentList;
		StringBuilder customerIdsList = new StringBuilder("(");
		for (Integer id : customerIds) {
			customerIdsList.append("'");
			customerIdsList.append(id);
			customerIdsList.append("'");
			customerIdsList.append(",");
		}
		customerIdsList.replace(customerIdsList.lastIndexOf(","),
				customerIdsList.length(), ")");
		String query = "SELECT * FROM [customers] WHERE [customer_id] IN "
				+ customerIdsList + ";";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				divisionNames.put(results.getInt("customer_id"),
						results.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return divisionNames;

	}

	public void addUser(String userWebType,
			String userConfgType, String title,String firstName, String lastName,
			String email, String workPhone,String contactNumber,String mobilePhone,String address,
			String city,String zip,String county,String country, String username, String password,
			byte termsAndConditions, byte termsAndConditionsOfService,
			String pincode, byte enabled, Date passwordExpires,
			Date pincodeExpires, short pincodeFailureCount, byte isdeleted,
			String userBureauType,int customer_id, List<Customer> customers){

		String query;
		Connection dbConn = null;
		PreparedStatement stmnt = null;
		ResultSet results = null;
		int userId=-1;
//		java.sql.Date passwdExpire=new java.sql.Date(passwordExpires.getTime());
//		java.sql.Date pinExpire=new java.sql.Date(pincodeExpires.getTime());
//		query="INSERT INTO [users]" +
//		"VALUES ('"+userWebType + "','" + userConfgType + "','" + title + "','" + firstName + "','" +
//		lastName + "','" + email + "','" + workPhone + "','" + contactNumber + "','" + mobilePhone + "','" +
//		address + "','" + city + "','" + zip + "','" + county + "','" + country + "','" + username + "','" +
//		password + "'," + termsAndConditions + "," + termsAndConditionsOfService + "," + customer_id + ",'" +
//		pincode + "'," + enabled + ",'" + passwdExpire + "','" + pinExpire + "'," + pincodeFailureCount + "," +
//		isdeleted + ",'" + userBureauType + "')";
		query="INSERT INTO [users] ([userWebType] ,[userConfgType] ,[title] ,[firstName]  ,[lastName] ,[email]" +
				" ,[workPhone],[contactNumber] ,[mobilePhone]  ,[address] ,[city]  ,[zip]  ,[county] ,[country] " +
				" ,[username] ,[password]  ,[termsAndConditions]  ,[termsAndConditionsOfService] ,[customer_id] " +
				" ,[pincode]  ,[enabled]  ,[passwordExpires] ,[pincodeExpires] ,[pincodeFailureCount] ,[isdeleted] ,[userBureauType]) " +
				" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			dbConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbConn.prepareStatement(query,
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmnt.setString(1, userWebType);
			stmnt.setString(2, userConfgType);
			stmnt.setString(3, title);
			stmnt.setString(4, firstName);
			stmnt.setString(5, lastName);
			stmnt.setString(6, email);
			stmnt.setString(7, workPhone);
			stmnt.setString(8, contactNumber);
			stmnt.setString(9, mobilePhone);
			stmnt.setString(10, address);
			stmnt.setString(11, city);
			stmnt.setString(12, zip);
			stmnt.setString(13, county);
			stmnt.setString(14,country);
			stmnt.setString(15, username);
			stmnt.setString(16, password);
			stmnt.setByte(17, termsAndConditions);
			stmnt.setByte(18, termsAndConditionsOfService);
			stmnt.setInt(19, customer_id);
			stmnt.setString(20, pincode);
			stmnt.setByte(21,enabled);
			stmnt.setTimestamp(22,new java.sql.Timestamp(passwordExpires.getTime()));
			stmnt.setTimestamp(23,new java.sql.Timestamp(pincodeExpires.getTime()));
			stmnt.setShort(24, pincodeFailureCount);
			stmnt.setByte(25, isdeleted);
			stmnt.setString(26, userBureauType);
			
			stmnt.executeUpdate();
			results = stmnt.getGeneratedKeys();
			while (results.next()) {
				userId = results.getInt(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
		}
		try {
			String queryMysql = "INSERT INTO `users` VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
			String querySql = "SELECT TOP 1 * FROM [users] ORDER BY user_id DESC";
			stmnt = dbConn.prepareStatement(querySql);
			ResultSet rs = stmnt.executeQuery();
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			PreparedStatement statement = dbConn.prepareStatement(queryMysql);
			while(rs.next()) {
				statement.setInt(1, rs.getInt("user_id"));
				statement.setString(2, rs.getString("userWebType"));
				statement.setString(3, rs.getString("userConfgType"));
				statement.setString(4, rs.getString("title"));
				statement.setString(5, rs.getString("firstName"));
				statement.setString(6, rs.getString("lastName"));
				statement.setString(7, rs.getString("email"));
				statement.setString(8, rs.getString("workPhone"));
				statement.setString(9, rs.getString("contactNumber"));
				statement.setString(10, rs.getString("mobilePhone"));
				statement.setString(11, rs.getString("address"));
				statement.setString(12, rs.getString("city"));
				statement.setString(13, rs.getString("zip"));
				statement.setString(14, rs.getString("county"));
				statement.setString(15, rs.getString("country"));
				statement.setString(16, rs.getString("username"));
				statement.setString(17, rs.getString("password"));
				statement.setByte(18, rs.getByte("termsAndConditions"));
				statement.setByte(19, rs.getByte("termsAndConditionsOfService"));
				statement.setInt(20, rs.getInt("customer_id"));
				statement.setString(21, rs.getString("pincode"));
				statement.setByte(22, rs.getByte("enabled"));
				statement.setTimestamp(23, rs.getTimestamp("passwordExpires"));
				statement.setTimestamp(24, rs.getTimestamp("pincodeExpires"));
				statement.setShort(25, rs.getShort("pincodeFailureCount"));
				statement.setByte(26, rs.getByte("isdeleted"));
				statement.setString(27, rs.getString("userBureauType"));
			}
			statement.executeUpdate();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}finally{
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			dbConn = null;
		}
		query="INSERT INTO [user_customer] ([user_id] ,[customer_id]) ";
		//working here
				for(Customer customer:customers){
				query+=" VALUES(?,?),";
				}
				query=query.substring(0,query.lastIndexOf(","));
		try{
			dbConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbConn.prepareStatement(query,
					PreparedStatement.RETURN_GENERATED_KEYS);
			int i=1;
			for(Customer customer:customers){
			stmnt.setInt(i, userId);
			stmnt.setInt((++i), customer.getId());
			i++;
			}
			stmnt.executeUpdate();
			results = stmnt.getGeneratedKeys();
			while (results.next()) {
				userId = results.getInt(1);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
		}
	}

	public boolean checkUserExits(String username) {
		String query = "SELECT username  FROM [users]WHERE username='"
				+ username + "';";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				String s = result.getString("username");
				if (s.equals(username)) {
					return true;
				} else
					break;
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			result = null;
			stmnt = null;
			dbConn = null;
		}
		return false;
	}

	public void modifyUser(int user_id, String userWebType,
			String userConfgType, String title, String firstName,
			String lastName, String email, String workPhone,
			String contactNumber, String mobilePhone, String address,
			String city, String zip, String county, String country,
			String username, String password, byte termsAndConditions,
			byte termsAndConditionsOfService, String pincode, byte enabled,
			Date passwordExpires, Date pincodeExpires,
			short pincodeFailureCount, byte isdeleted, String userBureauType) {

		java.sql.Date passwdExpire = new java.sql.Date(
				passwordExpires.getTime());
		java.sql.Date pinExpire = new java.sql.Date(pincodeExpires.getTime());
		String query = "UPDATE [users] " + "SET [userWebType] = '"
				+ userWebType + "'," + "[userConfgType] = '" + userConfgType
				+ "'," + "[title] = '" + title + "'," + "[firstName] = '"
				+ firstName + "'," + "[lastName] = '" + lastName + "',"
				+ "[email] = '" + email + "'," + "[workPhone] = '" + workPhone
				+ "'," + "[contactNumber] = '" + contactNumber + "',"
				+ "[mobilePhone] = '" + mobilePhone + "'," + "[address] = '"
				+ address + "'," + "[city] = '" + city + "'," + "[zip] = '"
				+ zip + "'," + "[county] = '" + county + "'," + "[country] = '"
				+ country + "'," + "[username] = '" + username + "',"
				+ "[password] = '" + password + "',"
				+ "[termsAndConditions] = " + termsAndConditions + ","
				+ "[termsAndConditionsOfService] = "
				+ termsAndConditionsOfService + "," + "[pincode] = '" + pincode
				+ "'," + "[enabled] = " + enabled + ","
				+ "[passwordExpires] = '" + passwdExpire + "',"
				+ "[pincodeExpires] = '" + pinExpire + "'," + "[isDeleted] = "
				+ isdeleted + "," + "[userBureauType] = '" + userBureauType
				+ "'" + " WHERE [user_id]=" + user_id;
		String querySql = "UPDATE `users` SET userWebType = '" + userWebType
				+ "'," + " userConfgType = '" + userConfgType + "',"
				+ " title = '" + title + "'," + " firstName = '" + firstName
				+ "'," + " lastName = '" + lastName + "'," + " email = '"
				+ email + "'," + " workPhone = '" + workPhone + "',"
				+ " contactNumber = '" + contactNumber + "',"
				+ " mobilePhone = '" + mobilePhone + "'," + " address = '"
				+ address + "'," + " city = '" + city + "'," + " zip = '" + zip
				+ "'," + " county = '" + county + "'," + " country = '"
				+ country + "'," + " username = '" + username + "',"
				+ " password = '" + password + "'," + " termsAndConditions = "
				+ termsAndConditions + "," + " termsAndConditionsOfService = "
				+ termsAndConditionsOfService + "," + " pincode = '" + pincode
				+ "'," + " enabled = " + enabled + "," + " passwordExpires = '"
				+ passwdExpire + "'," + " pincodeExpires = '" + pinExpire
				+ "'," + " isDeleted = " + isdeleted + ","
				+ " userBureauType = '" + userBureauType + "'"
				+ " WHERE user_id=" + user_id;

		Connection dbConn = null;
		Statement stmnt = null;

		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);
			stmnt.close();
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(querySql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			dbConn = null;
		}

	}
	public void modifyUserCustomers(int user_id, List<Customer> customers){
		String query = "DELETE FROM [user_customer] WHERE [user_id] = "+user_id;

		Connection dbConn = null;
		Statement stmnt = null;

		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);
			stmnt.close();
			query="INSERT INTO [user_customer] (user_id,customer_id) VALUES ";
			for(Customer customer:customers){
				query+="("+user_id + ","+customer.getId()+"),";
			}
			query=query.substring(0,query.lastIndexOf(","));
			
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			dbConn = null;
		}


	}
	public void modifyUsers(List<User> users) {
		String queryMySql = "update `users`" + "set enabled =" + "case";
		String query = "update [users]" + " set [enabled] =" + " case";
		for (User user : users) {
			query += " when [user_id] =" + user.getUserId() + " then "
					+ user.getEnabled() + "";
			queryMySql += " when user_id =" + user.getUserId() + " then "
					+ user.getEnabled() + "";
		}
		query += " else [enabled]";
		query += " end";
		query += " ,[passwordExpires] =" + " case";

		queryMySql += " else enabled";
		queryMySql += " end";
		queryMySql += " ,passwordExpires =" + " case";

		for (User user : users) {
			// System.out.println("pExp:"+new
			// java.sql.Date(user.getPasswordExpires().getTime()));
			query += " when [user_id] ="
					+ user.getUserId()
					+ " then '"
					+ (new java.sql.Timestamp(user.getPasswordExpires()
							.getTime())) + "'";
			queryMySql += " when user_id ="
					+ user.getUserId()
					+ " then '"
					+ (new java.sql.Timestamp(user.getPasswordExpires()
							.getTime())) + "'";
		}
		query += " else [passwordExpires]";
		query += " end";
		query += " ,[pincodeExpires] =" + " case";

		queryMySql += " else passwordExpires";
		queryMySql += " end";
		queryMySql += " ,pincodeExpires =" + " case";
		for (User user : users) {
			query += " when [user_id] ="
					+ user.getUserId()
					+ " then '"
					+ (new java.sql.Timestamp(user.getPincodeExpires()
							.getTime())) + "'";
			queryMySql += " when user_id ="
					+ user.getUserId()
					+ " then '"
					+ (new java.sql.Timestamp(user.getPincodeExpires()
							.getTime())) + "'";
		}
		query += " else [pincodeExpires]";
		query += " end";

		queryMySql += " else pincodeExpires";
		queryMySql += " end";

		Connection dbConn = null;
		Statement stmnt = null;

		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);
			stmnt.close();
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(queryMySql);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			dbConn = null;
		}

	}

	public void deleteUser(int user_id) {
		String query = "DELETE FROM [users] WHERE [user_id]=" + user_id;
		String querySQL = "DELETE FROM users WHERE user_id=" + user_id;
		Connection dbConn = null;
		Statement stmnt = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);
			try {
				dbConn = ConnectionBean.getInstance().getMYSQLConnection();
				stmnt = dbConn.createStatement();
				stmnt.executeUpdate(querySQL);
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					stmnt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				stmnt = null;
				dbConn = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateUserStatus(int user_id, byte enabled) {

		String query = "UPDATE [users] SET [enabled]=" + enabled
				+ " WHERE [user_id]=" + user_id;
		Connection dbConn = null;
		Statement stmnt = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			dbConn = null;
		}
	}

	@Override
	public List<User> getAdminUserList() {
		int customer_id = 0;
		List<User> userList = new ArrayList<User>();
		String query = "SELECT [customer_id] FROM [customers] WHERE [name]='Next Control Systems'";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				customer_id = results.getInt("customer_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
		}

		query = "SELECT * FROM [users] WHERE [customer_id]=" + customer_id;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				User newUser = new User(results.getInt("user_id"),
						results.getString("userWebType"),
						results.getString("userConfgType"),
						results.getString("title"),
						results.getString("firstName"),
						results.getString("lastName"),
						results.getString("email"),
						results.getString("workphone"),
						results.getString("contactNumber"),
						results.getString("mobilePhone"),
						results.getString("address"),
						results.getString("city"), results.getString("zip"),
						results.getString("county"),
						results.getString("country"),
						results.getString("username"),
						results.getString("password"),
						results.getByte("termsAndConditions"),
						results.getByte("termsAndConditionsOfService"),
						results.getInt("customer_id"),
						results.getString("pincode"),
						results.getByte("enabled"),
						results.getDate("passwordExpires"),
						results.getDate("pincodeExpires"),
						results.getShort("pincodeFailureCount"),
						results.getByte("isdeleted"),
						results.getString("userBureauType"));
				userList.add(newUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return userList;
	}

	@Override
	public User getUser(int userId) {
		User user = new User();
		String query = "SELECT * FROM [users] WHERE [user_id]=" + userId;
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				user = new User(results.getInt("user_id"),
						results.getString("userWebType"),
						results.getString("userConfgType"),
						results.getString("title"),
						results.getString("firstName"),
						results.getString("lastName"),
						results.getString("email"),
						results.getString("workphone"),
						results.getString("contactNumber"),
						results.getString("mobilePhone"),
						results.getString("address"),
						results.getString("city"), results.getString("zip"),
						results.getString("county"),
						results.getString("country"),
						results.getString("username"),
						results.getString("password"),
						results.getByte("termsAndConditions"),
						results.getByte("termsAndConditionsOfService"),
						results.getInt("customer_id"),
						results.getString("pincode"),
						results.getByte("enabled"),
						results.getDate("passwordExpires"),
						results.getDate("pincodeExpires"),
						results.getShort("pincodeFailureCount"),
						results.getByte("isdeleted"),
						results.getString("userBureauType"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return user;
	}

	@Override
	public User getSpecificUser(String username) {
		User user = new User();
		String query = "SELECT * FROM [users] WHERE username='" + username
				+ "'";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				user = new User(results.getInt("user_id"),
						results.getString("userWebType"),
						results.getString("userConfgType"),
						results.getString("title"),
						results.getString("firstName"),
						results.getString("lastName"),
						results.getString("email"),
						results.getString("workphone"),
						results.getString("contactNumber"),
						results.getString("mobilePhone"),
						results.getString("address"),
						results.getString("city"), results.getString("zip"),
						results.getString("county"),
						results.getString("country"),
						results.getString("username"),
						results.getString("password"),
						results.getByte("termsAndConditions"),
						results.getByte("termsAndConditionsOfService"),
						results.getInt("customer_id"),
						results.getString("pincode"),
						results.getByte("enabled"),
						results.getDate("passwordExpires"),
						results.getDate("pincodeExpires"),
						results.getShort("pincodeFailureCount"),
						results.getByte("isdeleted"),
						results.getString("userBureauType"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return user;
	}

	@Override
	public boolean passwordExists(String password) {
		String query = "SELECT [user_id] FROM [users] WHERE [password]='"
				+ password + "'";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return false;
	}

	@Override
	public boolean correctPassword(String username, String password) {
		String query = "SELECT user_id FROM [users] WHERE password=? AND username=? AND userBureauType IS NOT NULL";
		Connection dbConn = null;
		PreparedStatement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, password);
			stmnt.setString(2, username);
			results = stmnt.executeQuery();
			if (results.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			results = null;
			dbConn = null;
		}
		return false;
	}

	@Override
	public void resetPassword(String username, String password) {
		String query = "UPDATE [users] SET [password] ='" + password
				+ "' WHERE [username]='" + username + "'";
		Connection dbConn = null;
		Statement stmnt = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			stmnt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			dbConn = null;
		}

	}

	@Override
	public String getUsersEmail(String username) {
		String email = "";
		String query = "SELECT [email] FROM [users] WHERE [username]='"
				+ username + "'";
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				email = results.getString("email");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}

		return email;
	}

	@Override
	public int countUserHandledAlms(String username) {
		int almsCount = 0;
		String query = "SELECT (SELECT count(*) FROM `audits_alarmaudit` WHERE username='SDimov')/COUNT(*) FROM ( SELECT DATE_FORMAT(t1.audit_time, '%Y-%m-%d %H:59:59') AS period"
				+ " FROM `audits_alarmaudit` t1 INNER JOIN (SELECT DATE_FORMAT(audit_time, '%Y-%m-%d %H') AS period FROM `audits_alarmaudit` "
				+ "WHERE username=? GROUP BY DATE_FORMAT(audit_time, '%Y-%m-%d %H')) as rollups ON DATE_FORMAT(t1.audit_time, '%Y-%m-%d %H') >= rollups.period "
				+ "WHERE username=? GROUP BY DATE_FORMAT(t1.audit_time, '%Y-%m-%d %H')) AS a; ";
		Connection dbConn = null;
		PreparedStatement stmnt = null;
		ResultSet result = null;
		try {
			dbConn = ServiceProperties.getInstance().getConnectionMQSQLDB();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, username);
			stmnt.setString(2, username);
			result = stmnt.executeQuery();
			while (result.next()) {
				almsCount = result.getInt(1);
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
			dbConn = null;
		}

		return almsCount;
	}

	@Override
	public String getUserName(int userId) {
		String username = "";
		String query = "SELECT [username] FROM [users] WHERE [user_id]="
				+ userId;
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try {
			dbConn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				username = results.getString("username");
			}
		} catch (SQLException e) {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			results = null;
			stmnt = null;
			dbConn = null;
		}
		return username;
	}

	//
	// public void setCustomerId() {
	// // this.customerId = ServiceProperties.getInstance().getCustomerId();
	//
	// }
	//
	//
	// public String getCustomerId() {
	// setCustomerId();
	// return customerId;
	// }

}

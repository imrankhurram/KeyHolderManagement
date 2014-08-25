package com.nextcontrols.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nextcontrols.domain.User;

public interface IUserDAO extends Serializable{
	public List<User> getUserList();
	public List<User> getAdminUserList();
	public User getUser(int userId);
	public User getSpecificUser(String username);
	public boolean passwordExists(String password);
	public boolean correctPassword(String username,String password);
	public void resetPassword(String username,String password);
	public String getUsersEmail(String username);
	public int countUserHandledAlms(String username);
	public String getUserName(int userId);
	public boolean checkUserExits(String username);
	public void addUser(String userWebType,
			String userConfgType, String title,String firstName, String lastName,
			String email, String workPhone,String contactNumber,String mobilePhone,String address,
			String city,String zip,String county,String country, String username, String password,
			byte termsAndConditions, byte termsAndConditionsOfService,
			String pincode, byte enabled, Date passwordExpires,
			Date pincodeExpires, short pincodeFailureCount, byte isdeleted,
			String userBureauType);
	public void modifyUser(int user_id,String userWebType,
			String userConfgType, String title,String firstName, String lastName,
			String email, String workPhone,String contactNumber,String mobilePhone,String address,
			String city,String zip,String county,String country, String username, String password,
			byte termsAndConditions, byte termsAndConditionsOfService,
			String pincode, byte enabled, Date passwordExpires,
			Date pincodeExpires, short pincodeFailureCount, byte isdeleted,
			String userBureauType);
	public void deleteUser(int user_id);
	public void updateUserStatus(int user_id,byte enabled);
	
}

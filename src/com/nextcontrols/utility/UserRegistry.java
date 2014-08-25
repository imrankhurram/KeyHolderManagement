package com.nextcontrols.utility;

import java.util.ArrayList;

import com.nextcontrols.pagebeans.UserInfoPageBean;

public class UserRegistry {
	private static UserRegistry mInstance = new UserRegistry();
	private ArrayList<UserInfoPageBean> mUsers;
	
	public UserRegistry(){
		mUsers = new ArrayList<UserInfoPageBean>();		
	}
	
	public static UserRegistry getInstance(){
		return mInstance;
	}
	
	public void addUser(UserInfoPageBean pUser){
		mUsers.add(pUser);
	}
	
	public void removeUser(UserInfoPageBean pUser){
		mUsers.remove(pUser);
	}
	
	public ArrayList<UserInfoPageBean> getActiveUsers(){
		return mUsers;
	}
}

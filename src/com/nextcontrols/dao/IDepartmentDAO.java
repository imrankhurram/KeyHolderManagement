package com.nextcontrols.dao;

import java.sql.Connection;
import java.util.List;

import com.nextcontrols.domain.Department;

public interface IDepartmentDAO {
	
	public List<Department> getDepartmentList(List<String> branch_codes);
//	public void addDepartment(int version,String branch_code,String name,String type);
//	public int copyDepartment(int version,String branch_code,String name,String type);
//	public void modifyDepartment(int dep_id,int version,String name,String type);
//	public void deleteDepartment(int dep_id);
//	public String getDepartmentName(Connection dbConn,int dep_id);
//	public String getDepartmentName(int dep_id);
//	public int getDepartmentId(String name, String siteCode);
//	public int addDept(int version,String branch_code,String name,String type);
}

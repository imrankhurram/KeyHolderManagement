package com.nextcontrols.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.nextcontrols.dao.DepartmentDAO;
import com.nextcontrols.domain.Department;

public class DepartmentDAOTest {

	@Test
	public void testGetInstance() {
		Assert.assertNotNull("DepartmentDAO object is null", DepartmentDAO.getInstance());
	}

	@Test
	public void testSetInstance() {
		
	}

	@Test
	public void testGetDepartmentList() {
		List<String> listBranchCodes=new ArrayList<String>();
		listBranchCodes.add("NHSRGBQE01");
		Assert.assertNotNull("Departments list for this branch code is null",DepartmentDAO.getInstance().getDepartmentList(listBranchCodes));
//		Assert.assertEquals(5,DepartmentDAO.getInstance().getDepartmentList(listBranchCodes).size());
		listBranchCodes.clear();
		listBranchCodes.add("fdfds");
		Assert.assertEquals(new ArrayList<Department>(),DepartmentDAO.getInstance().getDepartmentList(listBranchCodes));//for wrong branch code
		Assert.assertEquals(new ArrayList<Department>(), DepartmentDAO.getInstance().getDepartmentList(null));//for null
	}

}

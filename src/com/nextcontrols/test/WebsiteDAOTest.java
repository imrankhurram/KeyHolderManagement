package com.nextcontrols.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.nextcontrols.dao.WebsiteDAO;
import com.nextcontrols.domain.Department;
import com.nextcontrols.domain.Website;

public class WebsiteDAOTest {

	@Test
	public void testGetInstance() {
		Assert.assertNotNull("WebsiteDAO object is null", WebsiteDAO.getInstance());
	}

	@Test
	public void testSetInstance() {
	}

	@Test
	public void testGetAssignedWebsites() {
		Assert.assertNotNull("Website for this user id is null",WebsiteDAO.getInstance().getAssignedWebsites(22));
		Assert.assertEquals(new ArrayList<Department>(),WebsiteDAO.getInstance().getAssignedWebsites(-1));//for wrong branch code
	}

	@Test
	public void testGetBranchCodes() {
		
		Assert.assertNotNull("Website for this user id is null",WebsiteDAO.getInstance().getBranchCodes(1));
		Assert.assertEquals(new ArrayList<Department>(),WebsiteDAO.getInstance().getBranchCodes(-1));//for wrong website id
	}

}

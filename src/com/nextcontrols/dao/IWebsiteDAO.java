package com.nextcontrols.dao;

import java.io.Serializable;
import java.util.List;

import com.nextcontrols.domain.Website;

public interface IWebsiteDAO extends Serializable {
	public List<Website> getAssignedWebsites(int userId);

	public List<String> getBranchCodes(int websiteId);
}

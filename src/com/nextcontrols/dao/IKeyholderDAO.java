package com.nextcontrols.dao;

import java.util.Date;
import java.util.List;

import com.nextcontrols.domain.Keyholder;
import com.nextcontrols.domain.KeyholderList;
import com.nextcontrols.domain.KeyholderListEnablingDetails;

public interface IKeyholderDAO {
	public List<Keyholder> getFullKeyholders(String branch_code, int deptId);

	public List<KeyholderList> getKeyholdersTypes(String branch_code,
			int deptId, boolean isSpecial);

	public List<Keyholder> getSpecialKeyholders(String branch_code, int deptId);

	public List<Keyholder> getListOfKeyholders(String branch_code, int listId);

	public Keyholder getKeyholderById(String branch_code, int listId,
			int keyholderId);

	public void updateKeyholdersList(List<Keyholder> listKeyholders,
			int keyholderListId);

//	public KeyholderList getKeyholdersList(String branch_code, int listid);

	public List<KeyholderList> getAllNormalLists(int deptId);

	public List<Keyholder> getContactTypes(int keyholderId);

	public void saveKeyholderList_Keyholder(Keyholder keyholder,
			KeyholderListEnablingDetails details);

	public void deleteKeyholderList_Keyholder(Keyholder keyholder,
			KeyholderListEnablingDetails details);

	public Keyholder getKeyholderById(String branch_code, int keyholderId);

	public void updateSiteKeyholder(Keyholder keyholder);

	public List<KeyholderList> getAllHolidayLists(int deptId);

	public int addBranchKeyholder(String contactName, String position,
			boolean active, String phone, String mobile, String fax,
			String email, String branchCode);

	public void deleteBranchKeyholder(int keyholderId);

	public List<KeyholderList> retrieveDepartmentKeyholderListNormal(int dep_id);

	public List<KeyholderList> retrieveDepartmentKeyholderListHoliday(int dep_id);

	// public void saveNewSiteKeyholder(Keyholder keyholder);
	public void addDeptKeyholderList(String displayName, String description,
			String listName, String listType, Date startDate, Date endDate,
			String occupancyStart, String occupancyEnd, String comments,
			int dep_id, String weekdaysActive);
	
	public KeyholderList getKeyholderListById(int keyholderListId);
	
	public void delDeptKeyholderList(int keyholder_list_id);
	
	public void modifyKeyholderList(String listName,String listType,Date startDate, Date endDate,String occupancyStart,
			String occupancyEnd,String comments,int keyholder_list_id,String weekdaysActive,String description,String displayName);
}

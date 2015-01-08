package com.nextcontrols.dao;

import java.util.List;
import java.util.TimeZone;

import com.nextcontrols.domain.Branch;

public interface IBranchDAO {

	public List<Branch> getBranch(int customerId);
//	public Branch getSpecificBranch(String branchCode);
//	public boolean addBranch(String branchCode,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,String email,int contact_id,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup, 
//			int alarmPriority, String refSystem);
//	public void addBranchMYSQL(String branchCode,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,String email,int contact_id,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup, 
//			int alarmPriority, String refSystem);
//	public void addCopyBranch(String branchCode,int version,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,int contact_id,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup, String refSystem);
//	public void addCopyBranchMYSQL(String branchCode,int version,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,int contact_id,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup, String refSystem);
//	public void deleteBranch(String branch_code);
//	public void deleteBranchMYSQL(String branch_code);
//	public void modifyBranch(String branchCode,String name,String address1,String address2,String city,String zip,String country,String timezone,
//			String fax,String phone,String email,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
//			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup,int alarmPriority, String refSystem);
//	public void modifyBranchMYSQL(Branch branch);
//	public List<OpeningHours> getOpeningHours(String branch_code);
	public TimeZone getSiteTimezone(String siteCode);
//	public String getReportSites(int customerId);
//	public List<OpeningHours> getWeekdayOpeningHours(String branch_code);
//	public List<OpeningHours> getSpecialOpeningHours(String branch_code);
//	public void addWeekdayOpeningHours(String day,String branch_code,String opening_time,String closing_time);
//	public List<String> getAllBranchCodes();
//	public List<Branch> getAllBranches();
//	public List<String> getNextEngineers();
//	public List<String> getRefrigirationContractors();
//	public List<String> getHVACContractors();
//	public List<String> getMaintenanceManagers();
//	public List<String> getSiteGroups();
//	
//	public List<NextEngineer> getAllNextEngineers();
//	public void addNextEngineer(NextEngineer engineer);
//	public void deleteNextEngineer(int engineerId);
//	public void modifyNextEngineer(NextEngineer engineer);
//
//	public List<HVAC_Contractor> getAllHvacContractors();
//	public void addHvacContractor(HVAC_Contractor contractor);
//	public void deleteHvacContractor(int contractorId);
//	public void modifyHvacContractor(HVAC_Contractor contractor);
//	
//	public List<MaintenanceManager> getAllMaintenanceManagers();
//	public void addMaintenanceManager(MaintenanceManager manager);
//	public void deleteMaintenanceManager(int managerId);
//	public void modifyMaintenanceManager(MaintenanceManager manager);
//	
//	public List<RefrigerationContractor> getAllRefrigererationContractors();
//	public void addRefrigerationContractor(RefrigerationContractor contractor);
//	public void deleteRefrigerationContractor(int contractorId);
//	public void modifyRefrigerationContractor(RefrigerationContractor contractor);
//	
//	public List<FixtureType> getAlFixtureTypes();
//	public void addFixtureType(FixtureType type);
//	public void deleteFixtureType(int typeId);
//	public void modifyFixtureType(FixtureType type);
//	
//	public List<FixtureCategory> getAlFixtureCategories();
//	public void addFixtureCategory(FixtureCategory category);
//	public void deleteFixtureCategory(int categoryId);
//	public void modifyFixtureCategory(FixtureCategory category);
//	
//	public void setBranchContract(boolean contract,String branchCode);
//	public void setBranchContractMYSQL(boolean contract,String branchCode);
//
//	public String getSiteTelephone(String branchCode);
//	public String getAlarmsCount(String site_code) ;
//	public String getSiteName(String site_code);
//	public List<String> getSitesCodesFromActiveAlms();
//	public void deleteOpeningHours(int openinghr_id);
//	public void addOpeningHours(String newDay,Date exactDate,String openingTime,String closingTime,String branch_code);
//	public void modifyOpeningHours(String newDay,Date exactDate,String openingTime,String closingTime,int openinghr_id);
//	public void modifyWeekOpeningHours(String openingTime,String closingTime,int openinghr_id);
//	public void addOpeningHoursNormal(OpeningHours hours);
//	public String getBranchTimeZone(int id);
//	public List<Integer> getDepartmentId(List<String> siteCodes); 
//	public List<Integer> getKeyholderListIds(String siteCode);
//	public void configureSplecialFlag(Map<String, Boolean> map);
//	public String getBranchCode(int deptId);
//	public int getDepId(String siteCode);
//	public List<String> getBranchNames(int customerId);
//	public String getBranchCode(String branchName);
//	public void modifyBranchMaintenanceMngr(String currName, String updatedName);
//	public void modifyBranchNextEngineer(String currName, String updatedName);
//	public void modifyBranchHVACContractors(String currName, String updatedName);
//	public void modifyBranchFridgeContractors(String currName, String updatedName);
//    public void removeBranchMaintenanceMngr(String name);
//    public void removeBranchNextEngineer(String name);
//    public void removeBranchHVACContractor(String name);
//    public void removeBranchRefContractor(String name);
//    public List<RefrigeratedSystem> populateRefSystems();
//    public void addRefSystem(RefrigeratedSystem refSystem);
//    public void delRefSystem(RefrigeratedSystem refSystem);
//    public void delBranchRefSystem(RefrigeratedSystem refSystem);
//    public void modifyRefSystem(RefrigeratedSystem refSystem);
//    public void modifyBranchRefSystem(String currName, String updatedName);
//    public List<String> getSiteCode(int customer_id, String siteGroup);
}


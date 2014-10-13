package com.nextcontrols.dao;

import java.util.Date;
import java.util.List;

import com.nextcontrols.domain.UserAudit;

public interface IUserAuditDAO {
	public void insertUserAudit(final UserAudit puserAudit);
	public void insertUserAdminAudit(String userId, String event, String eventDesc,String branchCode,int websiteId);
	public List<UserAudit> getUserAudits() ;
	public int countUserIdles(int userId);
	public List<UserAudit> getUserAuditsforUsers(List<Integer> userIds,String siteCode,Date fromDate,Date toDate);
	public List<UserAudit> getSpecificUserAudits(int user_id,String siteCode);
	public List<UserAudit> getDateSpecificUserAudits(int user_id,String siteCode,Date dateFrom, Date dateTo);
	public int avgAlarmHandlingTime(String username,Date dateFrom,Date dateTo);
	public int maxAlarmHandlingTime(String username,Date dateFrom,Date dateTo);
	public int minAlarmHandlingTime(String username,Date dateFrom,Date dateTo);
	public int totalAlmsHandled(String username,Date dateFrom,Date dateTo);
	public int totalAlmsCleared(String username,Date dateFrom,Date dateTo);
	public int totalAlmsHeld(String username,Date dateFrom,Date dateTo);
	public int totalAlmsOverdue(String username,Date dateFrom,Date dateTo);
	public List<UserAudit> getDateSpecificUserAudits(Date dateFrom, Date dateTo);
	public List<UserAudit> getIdleUserAudits(java.util.Date dateFrom, java.util.Date dateTo, int userId);
}
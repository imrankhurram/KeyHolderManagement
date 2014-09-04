package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.dao.UserDAO;
import com.nextcontrols.domain.UserAudit;

@ManagedBean(name = "useractivity")
@SessionScoped
public class UserActivityPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserAudit> userActivities;
	private Date dateFrom;
	private Date dateTo;
	private String siteCode;

	public UserActivityPageBean() {

	}

	public void initializeAudits(String siteCode) {
		this.siteCode=siteCode;
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		List<Integer> userIds4Customer = UserDAO.getInstance()
				.getUsersListInCustomer((int) session.getAttribute("userId"));
		// for(Integer i: userIds4Customer){
		// System.out.println("user id: "+i);
		// }
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DATE, 1);
		dateTo = to.getTime();
		Calendar before = Calendar.getInstance();
		before.add(Calendar.DATE, -29);
		dateFrom = before.getTime();
		
		this.userActivities = UserAuditDAO.getInstance().getUserAuditsforUsers(
				userIds4Customer, siteCode,dateFrom,dateTo);
	}
	public void updateDataTable() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");

		List<Integer> userIds4Customer = UserDAO.getInstance()
				.getUsersListInCustomer((int) session.getAttribute("userId"));
		this.userActivities = UserAuditDAO.getInstance().getUserAuditsforUsers(
				userIds4Customer, this.siteCode,dateFrom,dateTo);

		RequestContext.getCurrentInstance().update("frmUsersAuditPage");
//		return "QuotationsPage.xhtml?faces-redirect=true";
	}
	public List<UserAudit> getUserActivities() {
		return userActivities;
	}

	public void setUserActivities(List<UserAudit> userActivities) {
		this.userActivities = userActivities;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

}

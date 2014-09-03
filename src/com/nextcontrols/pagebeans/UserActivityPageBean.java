package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

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
	
	public UserActivityPageBean() {

	}
	public void initializeAudits(String siteCode){
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		List<Integer> userIds4Customer=UserDAO.getInstance().getUsersListInCustomer((int) session.getAttribute("userId"));
//		for(Integer i: userIds4Customer){
//			System.out.println("user id: "+i);
//		}
		this.userActivities=UserAuditDAO.getInstance().getUserAuditsforUsers(userIds4Customer, siteCode);
	}
	public List<UserAudit> getUserActivities() {
		return userActivities;
	}

	public void setUserActivities(List<UserAudit> userActivities) {
		this.userActivities = userActivities;
	}

}

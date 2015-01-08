package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.dao.IUserActivityDAO;
import com.nextcontrols.dao.IUserAuditDAO;
import com.nextcontrols.dao.UserActivityDAO;
import com.nextcontrols.dao.UserAuditDAO;
import com.nextcontrols.domain.UserAudit;

@ManagedBean(name = "keyholdersmenubar")
@SessionScoped
public class MenuBarKeyholdersPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuBarKeyholdersPageBean() {
	}

	public String actionKeyholdersBack() {
		return "DepartmentsPage?faces-redirect=true";
	}

	public String actionKeyholderListBack() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		DepartmentsPageBean departmentsPageBean = (DepartmentsPageBean) session
				.getAttribute("departments");
		if (departmentsPageBean != null) {
			departmentsPageBean.initializeKeyholders();
			return "KeyholdersNormalCallListPage.xhtml?faces-redirect=true";
		} else
			return "";

	}

	public String actionKeyholderList() {
		initializeKeyholderList();
		return "KeyholderCallListPage.xhtml?faces-redirect=true";

	}

	public void initializeKeyholderList() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		KeyholderCallListPageBean keyholderListPageBean = (KeyholderCallListPageBean) session
				.getAttribute("keyholdercalllist");
		KeyholdersPageBean keyholdersPageBean = (KeyholdersPageBean) session
				.getAttribute("keyholders");
		if (keyholderListPageBean != null) {
			session.removeAttribute("keyholdercalllist");
			keyholderListPageBean.initializeKeyholdersLists(keyholdersPageBean
					.getSelectedDepartment());
			session.setAttribute("keyholdercalllist", keyholderListPageBean);
		} else {
			keyholderListPageBean = new KeyholderCallListPageBean();
			keyholderListPageBean.initializeKeyholdersLists(keyholdersPageBean
					.getSelectedDepartment());
			session.setAttribute("keyholdercalllist", keyholderListPageBean);
		}
	}

	public String actionDepartmentsBack() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		if ((Boolean) session.getAttribute("usersOnly")) {
			return "";
		}
		boolean areManyWebsites = (boolean) session
				.getAttribute("areManyWebsites");
		if (areManyWebsites) {
			return "WebsitesPage?faces-redirect=true";
		} else
			return "";// TODO back to tutela website
	}

	public String actionLogout() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		IUserActivityDAO logoutUser = UserActivityDAO.getInstance();
		logoutUser.userLogout(session.getAttribute("user").toString());
		IUserAuditDAO logoutAudit = UserAuditDAO.getInstance();
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();

		logoutAudit.insertUserAudit(new UserAudit(Integer.parseInt(session
				.getAttribute("userId").toString()), new Timestamp(now
				.getTime()), "UserLogout", "The user has logged out", null));
		session.removeAttribute("user");
		session.invalidate();
		return "login?faces-redirect=true";
	}

	public String actionUsersButton() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UsersPageBean userListPageBean = (UsersPageBean) session
				.getAttribute("users");
		String userType = (String) session.getAttribute("userType");
		if (userType.equals("WebAdmin") || userType.equals("WebReadOnly")) {
			if (userListPageBean != null) {
				session.removeAttribute("users");
				userListPageBean.intitializeCustomers();
				session.setAttribute("users", userListPageBean);
			} else {
				userListPageBean = new UsersPageBean();
				userListPageBean.intitializeCustomers();
				session.setAttribute("users", userListPageBean);
			}
			return "CustomersPage.xhtml?faces-redirect=true";

		} else {
			if (userListPageBean != null) {
				session.removeAttribute("users");
				userListPageBean.initializeUsers();
				session.setAttribute("users", userListPageBean);
			} else {
				userListPageBean = new UsersPageBean();
				userListPageBean.initializeUsers();
				session.setAttribute("users", userListPageBean);
			}
			return "UsersPage.xhtml?faces-redirect=true";
		}
	}

	public String actionIsolationsButton() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		SitePageBean sitePageBean = (SitePageBean) session
				.getAttribute("sites");
		if (sitePageBean == null) {
			sitePageBean = new SitePageBean();
			session.setAttribute("sites", sitePageBean);
		}
		return "SiteListPage.xhtml?faces-redirect=true";
	}

	public String actionIsolationsFilterButton() {
		return "IsolationsFilterPage.xhtml?faces-redirect=true";
	}
	public String actionSensorsBack() {
		return "SiteListPage?faces-redirect=true";
	}
	public String actionAlarmAssetIsolationsBack() {
		return "SensorListPage?faces-redirect=true";
	}
}

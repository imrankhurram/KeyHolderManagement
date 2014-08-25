package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.dao.WebsiteDAO;
import com.nextcontrols.domain.Website;

@ManagedBean(name = "websites")
@SessionScoped
public class WebsitesPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Website> websitesList;
//	private Website selectedWebsite;

	public WebsitesPageBean() {
		websitesList = new ArrayList<Website>();

	}

	public void initializeWebsites() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		websitesList = WebsiteDAO.getInstance().getAssignedWebsites(
				(int) session.getAttribute("userId"));
		
//		for (Website website : websitesList) {
//			System.out.println("website id: " + website.getWebsiteId()
//					+ "--website name: " + website.getName());
//		}
	}

	public String showDepartments(int websiteId) {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		DepartmentsPageBean departmentsPageBean = (DepartmentsPageBean) session
				.getAttribute("departments");
		if (departmentsPageBean != null) {
			session.removeAttribute("departments");
			List<String> branchCodes = WebsiteDAO.getInstance().getBranchCodes(
					websiteId);
			departmentsPageBean.initializeDepartments(branchCodes);
			session.setAttribute("departments", departmentsPageBean);
		} else {
			departmentsPageBean = new DepartmentsPageBean();
			List<String> branchCodes = WebsiteDAO.getInstance().getBranchCodes(
					websiteId);
			departmentsPageBean.initializeDepartments(branchCodes);
			session.setAttribute("departments", departmentsPageBean);
		}
		return "DepartmentsPage?faces-redirect=true";
	}

	public List<Website> getWebsitesList() {
		return websitesList;
	}

	public void setWebsitesList(List<Website> websitesList) {
		this.websitesList = websitesList;
	}

}

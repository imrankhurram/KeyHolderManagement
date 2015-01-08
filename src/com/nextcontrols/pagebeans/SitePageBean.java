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
import com.nextcontrols.domain.AssetGroup;
import com.nextcontrols.domain.SiteView;
import com.nextcontrols.domain.SiteViewsAssetGroupList;

@ManagedBean(name = "sites")
@SessionScoped
public class SitePageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SiteViewsAssetGroupList siteViewsAssetGroupList;
	private int selectedWebsiteId;
	private List<SiteView> siteviewsList;
	private List<SiteViewsAssetGroupList> siteList;

	public SitePageBean() {
		siteList = new ArrayList<SiteViewsAssetGroupList>();
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		WebsitesPageBean websitePageBean = (WebsitesPageBean) session
				.getAttribute("websites");
		int siteId = websitePageBean.getSelectedWebsiteId();

		siteviewsList = WebsiteDAO.getInstance().getWebsiteSiteViews(siteId);
		for (SiteView view : siteviewsList) {
			SiteViewsAssetGroupList site = new SiteViewsAssetGroupList(
					view.getId(), view.getBranchviewName(), view.getBranch()
							.getBranchCode(), false);
			siteList.add(site);
			for (AssetGroup group : view.getAssetGroups()) {
				SiteViewsAssetGroupList assetGroup = new SiteViewsAssetGroupList(
						group.getId(), group.getAssetGroupName(), "", true);
				siteList.add(assetGroup);
			}
		}
	}

	public String showSensors(int id) {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		AssetGroupPageBean assetGroupPageBean = (AssetGroupPageBean) session
				.getAttribute("assetgroup");
		if (assetGroupPageBean == null) {
			assetGroupPageBean = new AssetGroupPageBean();
		}
		assetGroupPageBean.initializeAssets(id);
		session.setAttribute("assetgroup", assetGroupPageBean);
		return "SensorListPage.xhtml?faces-redirect=true";
	}

	public SiteViewsAssetGroupList getSiteViewsAssetGroupList() {
		return siteViewsAssetGroupList;
	}

	public void setSiteViewsAssetGroupList(
			SiteViewsAssetGroupList siteViewsAssetGroupList) {
		this.siteViewsAssetGroupList = siteViewsAssetGroupList;
	}

	public int getSelectedWebsiteId() {
		return selectedWebsiteId;
	}

	public void setSelectedWebsiteId(int selectedWebsiteId) {
		this.selectedWebsiteId = selectedWebsiteId;
	}

	public List<SiteView> getSiteviewsList() {
		return siteviewsList;
	}

	public void setSiteviewsList(List<SiteView> siteviewsList) {
		this.siteviewsList = siteviewsList;
	}

	public List<SiteViewsAssetGroupList> getSiteList() {
		return siteList;
	}

	public void setSiteList(List<SiteViewsAssetGroupList> siteList) {
		this.siteList = siteList;
	}

}

package com.nextcontrols.domain;

public class SiteViewsAssetGroupList implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String branchviewName;
	private String branchCode;
	private boolean assetGroup;

	public SiteViewsAssetGroupList() {

	}
	

	public SiteViewsAssetGroupList(int id, String branchviewName,
			String branchCode, boolean isAssetGroup) {
		super();
		this.id = id;
		this.branchviewName = branchviewName;
		this.branchCode = branchCode;
		this.assetGroup = isAssetGroup;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBranchviewName() {
		return branchviewName;
	}

	public void setBranchviewName(String branchviewName) {
		this.branchviewName = branchviewName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


	public boolean isAssetGroup() {
		return assetGroup;
	}


	public void setAssetGroup(boolean isAssetGroup) {
		this.assetGroup = isAssetGroup;
	}



}

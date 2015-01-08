package com.nextcontrols.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

public class SiteView implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***************** Persistent Properties ******************/
    
    private int id;
    private String branchviewName;
    private boolean calledAlarmsOnly;
    private boolean suppressFlashing;
    private boolean suppressValidation;
    private boolean suppressIncident;
    private boolean suppressIncidentAudit;
    private boolean incidentWithoutPin;
    private boolean showCheckReadingsFlashingIcon;
    private boolean showWebGUIButton;
    private boolean showAverageSensorsButton;
    private boolean showSelectDateRangeButton;
    private int flashingDateLimit;
    private Site branch;
    private Website website;
    private Set <AssetGroup> assetGroups ;
    private Set <Asset> assets ;

  
    /***************** Constructors ****************************/
    public SiteView(){}
    
    
    public SiteView(int id, String branchviewName,
			boolean calledAlarmsOnly, boolean suppressFlashing,
			boolean suppressValidation, boolean suppressIncident,
			boolean suppressIncidentAudit, boolean incidentWithoutPin,
			boolean showCheckReadingsFlashingIcon, boolean showWebGUIButton,
			boolean showAverageSensorsButton,
			boolean showSelectDateRangeButton, int flashingDateLimit,
			Site branch, Website website, Set<AssetGroup> assetGroups,
			Set<Asset> assets) {
		this.id = id;
		this.branchviewName = branchviewName;
		this.calledAlarmsOnly = calledAlarmsOnly;
		this.suppressFlashing = suppressFlashing;
		this.suppressValidation = suppressValidation;
		this.suppressIncident = suppressIncident;
		this.suppressIncidentAudit = suppressIncidentAudit;
		this.incidentWithoutPin = incidentWithoutPin;
		this.showCheckReadingsFlashingIcon = showCheckReadingsFlashingIcon;
		this.showWebGUIButton = showWebGUIButton;
		this.showAverageSensorsButton = showAverageSensorsButton;
		this.showSelectDateRangeButton = showSelectDateRangeButton;
		this.flashingDateLimit = flashingDateLimit;
		this.branch = branch;
		this.website = website;
		this.assetGroups = assetGroups;
		this.assets = assets;
	}
	    
    /***************** Accessors *******************************/
    
    public int getId() { return id; }
    @SuppressWarnings("unused") // Hibernate
    private void setId(int id) { this.id = id; }

    public Site getBranch() { return branch; }
    public void setBranch(Site branch) { this.branch = branch; }
    
    public Website getWebsite() { return website; }
    protected void setWebsite(Website website) { this.website = website; }
    
    public Set <AssetGroup> getAssetGroups() { return assetGroups; }
    public void setAssetGroups(Set <AssetGroup> assetGroups) { this.assetGroups = assetGroups; }

    public Set<Asset> getAssets() {return assets;}
	public void setAssets(Set<Asset> assets) {this.assets = assets;}

	public String getBranchviewName() {return branchviewName;}
	public void setBranchviewName(String branchviewName) {this.branchviewName = branchviewName;}

	public boolean isCalledAlarmsOnly() {return calledAlarmsOnly;}
	public void setCalledAlarmsOnly(boolean calledAlarmsOnly) {this.calledAlarmsOnly = calledAlarmsOnly;}

	public boolean isSuppressFlashing() {return suppressFlashing;}
	public void setSuppressFlashing(boolean suppressFlashing) {this.suppressFlashing = suppressFlashing;}

	public boolean isSuppressIncident() {return suppressIncident;}
	public void setSuppressIncident(boolean suppressIncident) {	this.suppressIncident = suppressIncident;}

	public boolean isSuppressValidation() {return suppressValidation;}
	public void setSuppressValidation(boolean suppressValidation) {this.suppressValidation = suppressValidation;}

	public boolean isSuppressIncidentAudit() {return suppressIncidentAudit;}
	public void setSuppressIncidentAudit(boolean suppressIncidentAudit) {this.suppressIncidentAudit = suppressIncidentAudit;}

	public boolean isShowCheckReadingsFlashingIcon() {return showCheckReadingsFlashingIcon;}
	public void setShowCheckReadingsFlashingIcon(
			boolean showCheckReadingsFlashingIcon) {
		this.showCheckReadingsFlashingIcon = showCheckReadingsFlashingIcon;
	}
	public boolean isShowAverageSensorsButton() {return showAverageSensorsButton;}
	public void setShowAverageSensorsButton(boolean showAverageSensorsButton) {this.showAverageSensorsButton = showAverageSensorsButton;}
	
	public boolean isShowSelectDateRangeButton() {return showSelectDateRangeButton;}
	public void setShowSelectDateRangeButton(boolean showSelectDateRangeButton) {this.showSelectDateRangeButton = showSelectDateRangeButton;}
	
	public boolean isShowWebGUIButton() {return showWebGUIButton;}
	public void setShowWebGUIButton(boolean showWebGUIButton) {this.showWebGUIButton = showWebGUIButton;}
		
	public boolean isIncidentWithoutPin() {return incidentWithoutPin;}
	public void setIncidentWithoutPin(boolean incidentWithoutPin) {this.incidentWithoutPin = incidentWithoutPin;}
	
	public int getFlashingDateLimit() {	return flashingDateLimit;}
	public void setFlashingDateLimit(int flashingDateLimit) {this.flashingDateLimit = flashingDateLimit;}


	/***************** Common Methods **************************/
    
	//public String toString() { return getBranch().toString(); }
	public String toString() { return getBranchviewName(); }
	
	public boolean equals(Object o) {
	        if (o == this) {
	            return true;
	        }
	        if (!(o instanceof SiteView)) {
	            return false;
	        }
	        SiteView t = (SiteView) o;
	        if(t.getId()==getId())
	        	return true;
	        else
	        	return false; 
	    }
	  public int hashCode() {
	        return new Integer(getId()).hashCode();
	    }
    /***************** Business Methods ************************/
    
    public Set <AssetGroup> listAssetGroups() { return Collections.unmodifiableSet(assetGroups); }

    public Set <Asset> listAssets() { return assets; }

    public AssetGroup newAssetGroup() {
        AssetGroup result = new AssetGroup("");
        result.setBranchView(this);
        assetGroups.add(result);
        return result;
    }
    
    public void addAssetGroup(AssetGroup assetGroup) {
    	if(assetGroup.getBranchView() != this) {
    		throw new IllegalArgumentException("Assetgp Group not owned by this branch");
    	}
    	assetGroup.setBranchView(this);
    	assetGroups.add(assetGroup);
    	
    }

    public void removeAssetGroup(AssetGroup assetGroup) {
        if (assetGroup.getBranchView() != this) {
            throw new IllegalArgumentException("WebPage Group not owned by this branch");
        }
        
        assetGroup.setBranchView(null);
        assetGroups.remove(assetGroup);
    }

    public void addAsset(Asset asset) {
        asset.setBranchView(this);
        assets.add(asset);
    }

    public void removeAsset(Asset asset) {
        if (asset.getBranchView() != this) {
            throw new IllegalArgumentException("Asset not owned by this branchView");
        }
        
        for (AssetTable table:listAssetTables()) {
            table.removeAsset(asset);
        }
       
        asset.setBranchView(null);
        assets.remove(asset);
        
    }

    public Collection <AssetTable> listAssetTables() {
        Collection <AssetTable> result = new Vector <AssetTable> ();
        for (AssetGroup group:assetGroups) {
            result.addAll(group.listAssetTables());
        }
        
        return Collections.unmodifiableCollection(result);
    }
}

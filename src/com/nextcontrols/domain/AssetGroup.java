package com.nextcontrols.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class AssetGroup  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private static final int MAX_TABLES = 1;
    /***************** Persistent Properties ******************/

	private int id;
	private String assetGroupName;
	private String buttonImageName;
    private List <AssetTable> assetTables ;
    private SiteView branchView;
    
    /***************** Constructors ****************************/
    public AssetGroup(){
    	this.assetGroupName = "AllAssets";
    	this.buttonImageName="";
    	this.assetTables = new Vector <AssetTable> ();
	}
    
    public AssetGroup(String assetGroupName) {
    	this.assetGroupName = assetGroupName;
    	this.buttonImageName="";
    	this.assetTables = new Vector <AssetTable> ();
    }

    public AssetGroup(int assetID, String assetGroupName, SiteView psiteview) {
    	this.id = assetID;
    	this.assetGroupName = assetGroupName;
    	this.buttonImageName="";
    	this.assetTables = new Vector <AssetTable> ();
    	this.branchView = psiteview;
    }
    /***************** Accessors *******************************/

    public List <AssetTable> getAssetTables() { return assetTables; }
    public void setAssetTables(List <AssetTable> assetTables) { this.assetTables = assetTables; }

    public SiteView getBranchView() { return branchView; }
    public void setBranchView(SiteView branchView) { this.branchView = branchView; }

	public String getAssetGroupName() {return assetGroupName;}
	public void setAssetGroupName(String assetGroupName) {this.assetGroupName = assetGroupName;}
	
	public String getButtonImageName() {return buttonImageName;}
	public void setButtonImageName(String buttonImageName) {this.buttonImageName = buttonImageName;}
	
	public int getId() {return id;}
	@SuppressWarnings("unused")
    private void setId(int id) {this.id = id;}

	/***************** Common Methods **************************/

    public String toString() {
        return assetGroupName;
    }
    /***************** Business Methods ************************/

	public List <AssetTable> listAssetTables(){ return Collections.unmodifiableList(assetTables);}
	
    public AssetTable newAssetTable() {
        if (assetTables.size() == MAX_TABLES) {
            throw new IllegalStateException("Maximum number of tables reached");
        }
        AssetTable result = new AssetTable("");
        result.setAssetGroup(this);
        assetTables.add(result);
        return result;
    }
    
    public void addAssetTable(AssetTable assetTable) {
    	if(assetTable.getAssetGroup() != this) {
    		throw new IllegalArgumentException("Asset Group not owned by this branch");
    	}
    	assetTables.add(assetTable);
    }
    
    public Boolean removeAssetTable(AssetTable assetTable) {
        if (assetTable.getAssetGroup() != this) {
            throw new IllegalArgumentException("Asset Table is not owned by this group");
        }
        assetTable.setAssetGroup(null);
        try{
        	assetTables.remove(assetTable);
        }
    	catch(Exception e)
    	{
    		return false;
    	}
    	return true;
    }
    	
}

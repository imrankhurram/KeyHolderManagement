package com.nextcontrols.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class AssetTable implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    private static final int MAX_POINTS = 6;

    /***************** Persistent Properties ******************/

	private int id;
	private AssetGroup assetGroup;
	private Set <Asset> assets ;
    private List <String> points ;
    private String name;

    /***************** Constructors ****************************/
    public AssetTable(){
    	name="Default";
    }
    protected AssetTable(String tableName) {
    	this.assets = new LinkedHashSet <Asset> ();
    	this.points = new Vector <String> ();
    	this.name = "Default";
    } 
    
    public AssetTable(AssetGroup group) {
        this.assetGroup = group;
        this.assets = new LinkedHashSet <Asset> ();
        this.points = new Vector <String> ();
        this.name = "Default";
    }

    /***************** Accessors *******************************/
    public int getId() {return id;}
    @SuppressWarnings("unused") 
    private void setId(int id) {this.id = id;}

    public Set <Asset> getAssets() { return assets; }
    public void setAssets(Set <Asset> assets) { this.assets = assets; }

    public AssetGroup getAssetGroup() { return assetGroup; }
    protected void setAssetGroup(AssetGroup group) { this.assetGroup = group; }
    
    public String getName() { return name; }
    public void setName(String name) {this.name = name;}

    public List<String> getPoints() { return points; }
    public void setPoints(List<String> points) { this.points = points; }
	
    /***************** Common Methods **************************/
    public String toString() {
        return name;
    }
    
    /***************** Business Methods ************************/
    
	public Set <Asset> listAssets() {return assets;}
	
    public boolean addAsset(Asset asset) {
        if (!assetGroup.getBranchView().listAssets().contains(asset)) {
            throw new IllegalArgumentException("Asset not associeated with branch");
        }
        return assets.add(asset);
    }
    
    public boolean removeAsset(Asset asset) {
        if (assets.remove(asset)) {
//            points.retainAll(getAvailablePoints());
            return true;
        } else {
            return false;
        }
    }
    
    public void clearAssets() {
        assets.clear();
        points.clear();
    }
    
    public void updateAssets(Collection <Asset> newAssets) {
        assets.clear();
        assets.addAll(newAssets);
//        points.retainAll(getAvailablePoints());
    }
//    
//    public boolean addPoint(String point) {
//        if (points.size() == MAX_POINTS) {
//            throw new IllegalStateException("Maximum number of points reached");
//        }
//        if (!getAvailablePoints().contains(point)) {
//            throw new IllegalArgumentException("This point is not available");
//        }
//        return points.add(point);
//    }
    
    public boolean removePoint(String point) {
        return points.remove(point);
    }
    
    public void clearPoints() {
        points.clear();
    }
    
    public List <String> listPoints() {
        return Collections.unmodifiableList(points);
    }
    
//    public Set <String> getAvailablePoints() {
//        Set <String> result = new TreeSet <String> ();
//        
//        for (Asset asset:assets) {
//            for (Input input:asset.listInputs()) {
//                result.add(input.getApplicationName());
//            }
//        }
//        return result;
//    }
//  
}

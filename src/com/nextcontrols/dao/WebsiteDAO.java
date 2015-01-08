package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.nextcontrols.domain.Asset;
import com.nextcontrols.domain.AssetGroup;
import com.nextcontrols.domain.Site;
import com.nextcontrols.domain.SiteView;
import com.nextcontrols.domain.Website;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class WebsiteDAO implements IWebsiteDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerId;
	private static IWebsiteDAO instance;

	public static IWebsiteDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new WebsiteDAO();
		}
	}

	public static void setInstance(IWebsiteDAO ins) {
		instance = ins;
	}

	private WebsiteDAO() {
	}

	public List<Website> getAssignedWebsites(int userId) {
	
		List<Website> assignedWebsites =null;
		Connection conn=null; 
		CallableStatement stmnt =null; 
		ResultSet rs =null; 
		String query=" [key_get_assignedwebsites](?)";
		try {
			conn = ConnectionBean.getInstance().getSQLConnection();
			stmnt = conn.prepareCall(query);
			stmnt.setInt(1, userId);
			
			rs = stmnt.executeQuery();
			assignedWebsites = new ArrayList<Website>();
			while(rs.next()){
				assignedWebsites.add(new Website(rs.getInt("website_id"),rs.getString("name"),rs.getString("meterName"),rs.getString("hvacName"),rs.getString("fixtureName")
						,rs.getString("alarmName"),rs.getString("imagePath"),rs.getString("branchListName"),rs.getString("Logo"),rs.getBoolean("typetutela"),rs.getInt("inactivityTimeout")
						,rs.getString("CountryCode")));
//				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				rs.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs =null;
			stmnt =null;
			conn = null;	
		}
		
		return assignedWebsites;
	}
	public List<String> getBranchCodes(int websiteId){
		List<String> branchCodes=new ArrayList<String>();
		String query="SELECT DISTINCT branch_code FROM [branch_views] WHERE website_id='" + websiteId +"'";
		Connection dbConn = null;
		Statement stmnt =null;
		ResultSet results = null;
		try{
			dbConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				branchCodes.add(results.getString("branch_code").trim());

			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			results =null;
			stmnt =null;
			dbConn = null;
		}

		return branchCodes;
	}
//	//   //   //  //  // // //  //  //  //  //  
	public List<SiteView> getWebsiteSiteViews(int pWebsiteID){

		List<SiteView> siteviewsList=new ArrayList<SiteView>();
			
		String query=" SELECT  brview.[branchview_id],brview.[website_id],brview.[branch_code],brview.[branchviewName],brview.[calledAlarmsOnly],brview.[suppressFlashing],"+
					 " brview.[suppressValidation],brview.[suppressIncident],brview.[suppressIncidentAudit],brview.[incidentWithoutPin],brview.[showCheckReadingsFlashingIcon],"+
					 " brview.[checkReadingsFlashingDueDate],brview.[showWebGUIButton],brview.[showAverageSensorsButton],brview.[showSelectDateRangeButton],brview.[FlashingDateLimit],brview.[showAlertSettingsIcon],"+
					 " agroup.[asset_group_id],agroup.[assetGroupName]" +
					 //", atable.[asset_table_id],atable.[name] as assettablename,"+
					 //" asset.[asset_id],asset.[assetName],asset.[description],asset.[showmeankineticenergy],asset.[showbattery],asset.[alarmasset_id],asset.[alarmassetSubFixture],asset.[isAlert],asset.[isduplicate]"+
					 " FROM [branch_views] as brview "+
					 " Inner Join [Asset_groups] as agroup on brview.branchview_id = agroup.branch_code"+
					 //" Inner Join [Number6v2].[dbo].[Asset_tables] as atable on agroup.asset_group_id = atable.asset_group_id"+
					 //" Inner Join [Number6v2].[dbo].[asset_table_asset] as alinktable on atable.asset_table_id = alinktable.asset_table_id"+
					 //" Inner Join [Number6v2].[dbo].[Asset] as asset on alinktable.asset_id = asset.asset_id"+
					 " where brview.website_id = "+pWebsiteID+" order by brview.[branchview_id] asc";

		System.out.println(query);
		Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try{
		dbConn=ConnectionBean.getInstance().getSQLConnection();
		stmnt=dbConn.createStatement();
		results=stmnt.executeQuery(query);
		//AssetGroup assetgp = null;
		SiteView siteView = null;
		int brachViewID=0;
		while (results.next()){
			if(brachViewID != results.getInt("branchview_id")){
				Site site = new Site(results.getString("branch_code"));
				Website website = new Website(pWebsiteID);
				siteView = new SiteView(results.getInt("branchview_id"), results.getString("branchviewName"), 
						results.getBoolean("calledAlarmsOnly"), results.getBoolean("suppressFlashing"), results.getBoolean("suppressValidation"),
						results.getBoolean("suppressIncident"), results.getBoolean("suppressIncidentAudit"),results.getBoolean("incidentWithoutPin"), 
						results.getBoolean("showCheckReadingsFlashingIcon"), results.getBoolean("showWebGUIButton"), results.getBoolean("showAverageSensorsButton"), 
						results.getBoolean("showSelectDateRangeButton"), results.getInt("flashingDateLimit"), site, website, new HashSet<AssetGroup> (), new HashSet<Asset> ());
				
				if(results.getInt("asset_group_id")!=0){
					siteView.addAssetGroup(new AssetGroup(results.getInt("asset_group_id"), results.getString("assetGroupName"),siteView));
				}
				siteviewsList.add(siteView);

			}else{
				siteviewsList.remove(siteView);
				siteView.addAssetGroup(new AssetGroup(results.getInt("asset_group_id"), results.getString("assetGroupName"),siteView));
				siteviewsList.add(siteView);
			}
				
			brachViewID = results.getInt("branchview_id");
		}
	}catch (SQLException e){
		e.printStackTrace();
	}finally{
		try {
			results.close();
			stmnt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		results =null;
		stmnt =null;
		dbConn = null;
	}
	return siteviewsList;
	  
	}
}

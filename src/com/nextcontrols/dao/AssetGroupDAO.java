package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.domain.Asset;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class AssetGroupDAO implements IAssetGroupDAO,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static IAssetGroupDAO instance;
	public static IAssetGroupDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new AssetGroupDAO();
		}
	}
	public static void setInstance(IAssetGroupDAO ins) {
		instance=ins;
	}
	private AssetGroupDAO() {}
//	public List<Integer> listOfFlashingAlarmAssets(String pSiteCode, String pField){
//		List<Integer> alamassetsList=new ArrayList<Integer>();
//    	String query="SELECT DISTINCT mysqlaa.alarmasset_id " +
//                "FROM `bureauv2alarms`.`alarming_alarm` as mysqlaa " +
//                "LEFT JOIN `audits_incident_audit` as audit ON mysqlaa.alarm_id = audit.alarm_id " +
//                "LEFT JOIN `alarming_alarm_call` as mysqlalarmcall on mysqlaa.alarm_id = mysqlalarmcall.alarm_id " +
//                "LEFT JOIN `audits_callaudit` as mysqlcallaudit on mysqlalarmcall.call_id = mysqlcallaudit.call_id " +
//                "LEFT JOIN `audits_emailaudit` as mysqlemailaudit on mysqlaa.alarm_id = mysqlemailaudit.alarm_id " +
//                "LEFT JOIN `audits_smsmessageaudit` as mysqlsmsmessageaudit on mysqlaa.alarm_id = mysqlsmsmessageaudit.alarm_id " +
//                "WHERE(audit."+  pField + " Is NULL) " +
//                "AND (audit.audits_on <> '0' OR audit.audits_on IS NULL) " +
//                "AND mysqlaa.alarmasset_id is not null " +
//                "AND (mysqlaa.Receive_Time >= @mindate AND mysqlaa.Receive_Time <= @maxdate) " +
//                "AND (mysqlcallaudit.successful = 1 OR  " +
//                "((mysqlcallaudit.successful = 0 OR mysqlcallaudit.successful is null) AND mysqlemailaudit.email_id is not null) OR  " +
//                "((mysqlcallaudit.successful = 0 OR mysqlcallaudit.successful is null) AND mysqlsmsmessageaudit.sms_id is not null)  " +
//                ") AND mysqlaa.Site_Code = '"+pSiteCode+"'";
//
//    	Connection dbConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbConn.createStatement();
//			results=stmnt.executeQuery(query);
//			while (results.next()){
//				alamassetsList.add(results.getInt("alarmasset_id"));
//			}
//		}catch (SQLException e){
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results =null;
//			stmnt =null;
//			dbConn = null;
//		}
//		return alamassetsList;
//	}
	
	public List<Asset> listAssets(int pAssetGroupID)
    {
		List<Asset> assetsList=new ArrayList<Asset>();
    	String query=" SELECT agroup.[asset_group_id],agroup.[assetGroupName], atable.[asset_table_id],atable.[name] as assettablename,"+
    				 " asset.[asset_id],asset.[assetName],asset.[description],asset.[showmeankineticenergy],asset.[showbattery],asset.[alarmasset_id],asset.[alarmassetSubFixture],asset.[isAlert],asset.[isduplicate]"+
    				 " FROM [Asset_groups] as agroup"+
    				 " Inner Join [Asset_tables] as atable on agroup.asset_group_id = atable.asset_group_id"+
    				 " Inner Join [asset_table_asset] as alinktable on atable.asset_table_id = alinktable.asset_table_id"+
    				 " Inner Join [Asset] as asset on alinktable.asset_id = asset.asset_id"+
    				 " where agroup.asset_group_id = "+pAssetGroupID;
//    	System.out.println(query);
    	Connection dbConn = null;
		Statement stmnt = null;
		ResultSet results = null;
		try{
			dbConn=ConnectionBean.getInstance().getSQLConnection();
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				Asset asset = new Asset(results.getInt("asset_id"), results.getString("assetName"), results.getString("description"), results.getBoolean("showmeankineticenergy")
						,results.getBoolean("showbattery"), results.getInt("alarmasset_id"),results.getString("alarmassetSubFixture"), results.getBoolean("isAlert"), results.getBoolean("isduplicate"));
//				asset.addAssetInputRanges(this.listAssetInputRange(asset));
//				asset.addAssetSetpointRanges(this.listAssetSetpointRange(asset));
//				asset.addAssetVirtualPointRanges(this.listAssetVirtualPointRange(asset));
				assetsList.add(asset);
							
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
		return assetsList;
	}
    
//	private List<AssetInputRange> listAssetInputRange(Asset pAsset){
//		List<AssetInputRange> assetinputranges = new ArrayList<AssetInputRange>();
//		
//		String query = " select assetinput.input_id,input.[applicationName],input.input_order,input.[unit] as unit,controller.branch_code,controller.controllerCode,controller.controllerType"+
//					   " , assetinput.low_limit,assetinput.high_limit,assetinput.unit as displayunit"+
//					   " from [Number6v2].[dbo].[Asset] as asset"+
//					   " INNER Join [Number6v2].[dbo].[asset_input_ranges] as assetinput on assetinput.asset_id = asset.asset_id"+
//					   " INNER JOIN [Number6v2].[dbo].[inputs] as input on assetinput.input_id = input.input_id"+
//					   " INNER JOIN [Number6v2].[dbo].[controllers] as controller on input.controller_id = controller.controller_id"+
//					   " where asset.asset_id = "+pAsset.getId();
//		
//		Connection dbConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbConn.createStatement();
//			results=stmnt.executeQuery(query);
//			Site site = null;
//			Controller controller = null;
//			Input input = null;
//			while (results.next()){
//				if(site ==null ){
//					site = new Site(results.getString("branch_code"));
//				}
//				controller = new Controller(results.getInt("controllerCode"), results.getInt("controllerType"),site);
//				input = new Input(results.getInt("input_id"), results.getString("applicationName"), results.getString("unit"), results.getInt("input_order"),controller);
//				assetinputranges.add(new AssetInputRange( pAsset, input,results.getDouble("low_limit"),results.getDouble("high_limit"), results.getString("displayunit"))); 
//			}
//		}catch (SQLException e){
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results =null;
//			stmnt =null;
//			dbConn = null;
//		}
//		return assetinputranges;
//	}
//	
//	private List<AssetSetpointRange> listAssetSetpointRange(Asset pAsset){
//		List<AssetSetpointRange> assetsetpointranges = new ArrayList<AssetSetpointRange>();
//		String query = " select setpoint.setpoint_id,setpoint.[applicationName],setpoint.[setpoint_order],setpoint.[unit] as unit,controller.branch_code,controller.controllerCode,controller.controllerType"+
//					   " , assetsetpoint.low_limit,assetsetpoint.high_limit,assetsetpoint.unit as displayunit"+
//					   " from [Number6v2].[dbo].[Asset] as asset"+
//					   " INNER Join [Number6v2].[dbo].[asset_setpoint_ranges] as assetsetpoint on assetsetpoint.asset_id = asset.asset_id"+
//					   " INNER JOIN [Number6v2].[dbo].[setpoints] as setpoint on assetsetpoint.setpoint_id = setpoint.setpoint_id"+
//					   " INNER JOIN [Number6v2].[dbo].[controllers] as controller on setpoint.controller_id = controller.controller_id"+
//					   " where asset.asset_id = "+pAsset.getId();
//		
//		Connection dbConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbConn.createStatement();
//			results=stmnt.executeQuery(query);
//			Site site = null;
//			Controller controller = null;
//			Setpoint setpoint = null;
//			while (results.next()){
//				if(site ==null ){
//					site = new Site(results.getString("branch_code"));
//				}
//				controller = new Controller(results.getInt("controllerCode"), results.getInt("controllerType"),site);
//				setpoint = new Setpoint(results.getInt("setpoint_id"), results.getString("applicationName"), results.getString("unit"), results.getInt("setpoint_order"),controller);
//				assetsetpointranges.add(new AssetSetpointRange( pAsset, setpoint,results.getDouble("low_limit"),results.getDouble("high_limit"), results.getString("displayunit"))); 
//			}
//		}catch (SQLException e){
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results =null;
//			stmnt =null;
//			dbConn = null;
//		}
//		return assetsetpointranges;
//	}
//	
//	private List<AssetVirtualPointRange> listAssetVirtualPointRange(Asset pAsset){
//		List<AssetVirtualPointRange> assetvirtualpointranges = new ArrayList<AssetVirtualPointRange>();
//		String query = " select  virtualpoint.virtualpoint_id,virtualpoint.[applicationName],virtualpoint.[virtualpoint_order],virtualpoint.[unit] as unit,controller.branch_code,"+
//					   " controller.controllerCode,controller.controllerType, assetvirpoint.low_limit,assetvirpoint.high_limit,assetvirpoint.unit as displayunit"+	
//					   " from [Number6v2].[dbo].[Asset] as asset"+ 
//					   " INNER JOIN [Number6v2].[dbo].[asset_virtualpoint_ranges] as assetvirpoint on assetvirpoint.asset_id = asset.asset_id"+
//					   " INNER JOIN [Number6v2].[dbo].[virtualpoints] as virtualpoint on assetvirpoint.virtualpoint_id = virtualpoint.virtualpoint_id"+
//					   " INNER JOIN [Number6v2].[dbo].[controllers] as controller on virtualpoint.controller_id = controller.controller_id"+
//					   " where asset.asset_id = "+pAsset.getId();
//		
//		Connection dbConn = null;
//		Statement stmnt = null;
//		ResultSet results = null;
//		try{
//			dbConn=ConnectionBean.getInstance().getBureauConnection();
//			stmnt=dbConn.createStatement();
//			results=stmnt.executeQuery(query);
//			Site site = null;
//			Controller controller = null;
//			VirtualPoint virtualpoint = null;
//			while (results.next()){
//				if(site ==null ){
//					site = new Site(results.getString("branch_code"));
//				}
//				controller = new Controller(results.getInt("controllerCode"), results.getInt("controllerType"),site);
//				virtualpoint = new VirtualPoint(results.getInt("virtualpoint_id"), results.getString("applicationName"), results.getString("unit"), results.getInt("virtualpoint_order"),controller);
//				assetvirtualpointranges.add(new AssetVirtualPointRange( pAsset, virtualpoint,results.getDouble("low_limit"),results.getDouble("high_limit"))); 
//			}
//		}catch (SQLException e){
//			e.printStackTrace();
//		}finally{
//			try {
//				results.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			results =null;
//			stmnt =null;
//			dbConn = null;
//		}
//		return assetvirtualpointranges;
//	}
//    
}
	

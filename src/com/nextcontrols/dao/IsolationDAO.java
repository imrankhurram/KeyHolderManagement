package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nextcontrols.domain.AlarmAssetIsolation;
import com.nextcontrols.domain.AlarmTrap;
import com.nextcontrols.domain.Asset;
import com.nextcontrols.domain.AssetSearchStringIsolation;
import com.nextcontrols.domain.IsolatedAsset;
import com.nextcontrols.domain.Isolation;
import com.nextcontrols.domain.IsolationAssetSubfixtures;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class IsolationDAO implements IAlarmingIsolationDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private Connection dbBureauConn=null;
	private Connection dbBureauConn2 = null;
	private static IAlarmingIsolationDAO instance;

	public static IAlarmingIsolationDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new IsolationDAO();
		}
	}

	public static void setInstance(IAlarmingIsolationDAO inst) {
		instance = inst;
	}

	private IsolationDAO() {

	}

	/*
	 * public void dbBureauConnect(){ try {
	 * dbBureauConn=ConnectionBean.getInstance().getBureauConnection(); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 */

	public void dbMYSQLBureauConnect() {
		try {
			dbBureauConn2 = ConnectionBean.getInstance().getMYSQLConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// @Override
	// public List<Isolation> getAllDeptIsolations(int depId) {
	// List<Isolation> deptIsolations=new ArrayList<Isolation> ();
	// dbMYSQLBureauConnect();//Changed by Nayyab Table moved 10-10-11
	// //Commented by Nayyab Zia Aug 26, 2011 change in Query, change in
	// constructor initiation call
	// //String
	// query="SELECT * FROM Number6V2.Alarming.Isolation WHERE dep_id="+depId +
	// " ORDER BY start_time";
	// String query =
	// "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"+
	// " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"+
	// " FROM ALARMING_ISOLATION i" +
	// " JOIN USERS u " +
	// " ON i.user_id= u.user_id " +
	// " WHERE dep_id="+ depId + " ORDER BY i.start_time";
	// Statement stmnt = null;
	// ResultSet result = null;
	// try{
	// stmnt=dbBureauConn2.createStatement();
	// result=stmnt.executeQuery(query);
	// while (result.next()){
	// Isolation temp=new
	// Isolation(result.getInt("isolation_id"),result.getString("site_code"),result.getString("isolation_type"),result.getString("day_of_week"),
	// result.getDate("start_time"),result.getDate("end_time"),result.getBoolean("enabled"),result.getInt("user_id"),
	// result.getDate("created_on"),result.getString("start_hour"),result.getString("end_hour"),depId,result.getString("site_contact_name"),result.getString("username"),result.getString("name"));
	// deptIsolations.add(temp);
	// }
	// }catch (SQLException e) {
	// System.out.println("SQL Exception in function getAllDeptIsolations in class IsolationDAO"+
	// query);
	//
	// }finally{
	// dbBureauConn2=null;
	// stmnt=null;
	// result=null;
	// }
	// return deptIsolations;
	// }
	// Added by Nayyab 07 Sep 2011 to add new editable column in Isolations
	// table about more_name
	@Override
	public void addDeptIsolation(String siteCode, String isolationType,
			String dayOfWeek, Date startTime, Date endTime, boolean enabled,
			int userId, Date createdOn, ArrayList<Integer> alarmTrapId,
			ArrayList<IsolationAssetSubfixtures> alarmAssetSubfixtures,
			String startHour, String endHour, int depId, String name,
			String more_name) {

		int isolationId = 0;
		dbMYSQLBureauConnect();
		String query = "INSERT INTO Alarming_Isolation (site_code,isolation_type,day_of_week,start_time,end_time,enabled,user_id,created_on,"
				+ "start_hour,end_hour,dep_id,site_contact_name,name) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
		// LAST_INSERT_ID()

		ResultSet result = null;
		PreparedStatement stmnt = null;
		try {
			stmnt = dbBureauConn2.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			stmnt.setString(1, siteCode);
			stmnt.setString(2, isolationType);
			if (dayOfWeek == null) {
				stmnt.setNull(3, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(3, dayOfWeek);
			}
			stmnt.setDate(4, new java.sql.Date(startTime.getTime()));
			if (endTime == null) {
				stmnt.setNull(5, java.sql.Types.DATE);
			} else {
				stmnt.setDate(5, new java.sql.Date(endTime.getTime()));
			}
			byte enable = (byte) (enabled ? 1 : 0);
			stmnt.setByte(6, enable);
			stmnt.setInt(7, userId);
			stmnt.setDate(8, new java.sql.Date(createdOn.getTime()));
			stmnt.setString(9, startHour);
			if (endHour == null) {
				stmnt.setNull(10, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(10, endHour);
			}
			stmnt.setInt(11, depId);
			if (name == null) {
				stmnt.setNull(12, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(12, name);
			}

			// Added by Nayyab 07 Sep 2011 for new editable column name in
			// Isolation
			// table

			if (more_name == null) {
				stmnt.setNull(13, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(13, more_name);
			}

			// End addition by Nayyab
			// Changed by Nayyab to get last inserted field 10-10-2011
			stmnt.executeUpdate();
			// stmnt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			result = stmnt.getGeneratedKeys();

			while (result.next()) {
				isolationId = result.getInt(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function addDeptIsolation in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}

		if (alarmAssetSubfixtures != null) {
			dbMYSQLBureauConnect();
			PreparedStatement stmnt1 = null;
			try {
				for (int i = 0; i <= alarmAssetSubfixtures.size() - 1; i++) {
					query = "INSERT INTO Alarming_Isolation_AlarmAssets (isolation_id,alarmasset_id,Search_String_ID) "
							+ "VALUES (?,?,?);";

					stmnt1 = dbBureauConn2.prepareStatement(query);
					stmnt1.setInt(1, isolationId);
					stmnt1.setInt(2, alarmAssetSubfixtures.get(i)
							.getAlarmAssetId());
					if (alarmAssetSubfixtures.get(i).getSubfixtureId() != 0) {
						stmnt1.setLong(3, alarmAssetSubfixtures.get(i)
								.getSubfixtureId());
					} else {
						stmnt1.setNull(3, java.sql.Types.BIGINT);
					}
					stmnt1.executeUpdate();

				}
			} catch (SQLException e) {
				System.out
						.println("SQL Exception in function addDeptIsolation in class IsolationDAO");
			} finally {
				dbBureauConn2 = null;
				stmnt1 = null;
			}
		}

		if (alarmTrapId != null) {
			dbMYSQLBureauConnect();
			Statement stmnt2 = null;
			try {
				for (int j = 0; j <= alarmTrapId.size() - 1; j++) {
					query = "INSERT INTO Alarming_Isolation_AlarmTraps (isolation_id,alarmtrap_id) "
							+ "VALUES("
							+ isolationId
							+ ","
							+ alarmTrapId.get(j) + ")";
					stmnt2 = dbBureauConn2.createStatement();
					stmnt2.executeUpdate(query);

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out
						.println("SQL Exception in function addDeptIsolation in class IsolationDAO");
			} finally {
				dbBureauConn2 = null;
				stmnt2 = null;
			}
		}

		// ///////////////////////////////////////////////////////////
		isolationId = 0;
		query = "INSERT INTO Alarming.Isolation (site_code,isolation_type,day_of_week,start_time,end_time,enabled,user_id,created_on,"
				+ "start_hour,end_hour,dep_id,site_contact_name,name) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			dbBureauConn2 = ConnectionBean.getInstance().getSQLConnection();
			stmnt = dbBureauConn2.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);

			stmnt.setString(1, siteCode);
			stmnt.setString(2, isolationType);
			if (dayOfWeek == null) {
				stmnt.setNull(3, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(3, dayOfWeek);
			}
			stmnt.setDate(4, new java.sql.Date(startTime.getTime()));
			if (endTime == null) {
				stmnt.setNull(5, java.sql.Types.DATE);
			} else {
				stmnt.setDate(5, new java.sql.Date(endTime.getTime()));
			}
			byte enable = (byte) (enabled ? 1 : 0);
			stmnt.setByte(6, enable);
			stmnt.setInt(7, userId);
			stmnt.setDate(8, new java.sql.Date(createdOn.getTime()));
			stmnt.setString(9, startHour);
			if (endHour == null) {
				stmnt.setNull(10, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(10, endHour);
			}
			stmnt.setInt(11, depId);
			if (name == null) {
				stmnt.setNull(12, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(12, name);
			}

			// Added by Nayyab 07 Sep 2011 for new editable column name in
			// Isolation
			// table

			if (more_name == null) {
				stmnt.setNull(13, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(13, more_name);
			}

			// End addition by Nayyab
			// Changed by Nayyab to get last inserted field 10-10-2011
			stmnt.executeUpdate();
			// stmnt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			result = stmnt.getGeneratedKeys();

			while (result.next()) {
				isolationId = result.getInt(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function addDeptIsolation in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}

		if (alarmAssetSubfixtures != null) {
			PreparedStatement stmnt1 = null;
			try {
				dbBureauConn2 = ConnectionBean.getInstance().getSQLConnection();
				for (int i = 0; i <= alarmAssetSubfixtures.size() - 1; i++) {
					query = "INSERT INTO Alarming.Isolation_AlarmAssets (isolation_id,alarmasset_id,Search_String_ID) "
							+ "VALUES (?,?,?);";

					stmnt1 = dbBureauConn2.prepareStatement(query);
					stmnt1.setInt(1, isolationId);
					stmnt1.setInt(2, alarmAssetSubfixtures.get(i)
							.getAlarmAssetId());
					if (alarmAssetSubfixtures.get(i).getSubfixtureId() != 0) {
						stmnt1.setLong(3, alarmAssetSubfixtures.get(i)
								.getSubfixtureId());
					} else {
						stmnt1.setNull(3, java.sql.Types.BIGINT);
					}
					stmnt1.executeUpdate();

				}
			} catch (SQLException e) {
				System.out
						.println("SQL Exception in function addDeptIsolation in class IsolationDAO");
			} finally {
				dbBureauConn2 = null;
				stmnt1 = null;
			}
		}

		if (alarmTrapId != null) {
			Statement stmnt2 = null;
			try {
				dbBureauConn2 = ConnectionBean.getInstance().getSQLConnection();
				for (int j = 0; j <= alarmTrapId.size() - 1; j++) {
					query = "INSERT INTO Alarming.Isolation_AlarmTraps (isolation_id,alarmtrap_id) "
							+ "VALUES("
							+ isolationId
							+ ","
							+ alarmTrapId.get(j) + ")";
					stmnt2 = dbBureauConn2.createStatement();
					stmnt2.executeUpdate(query);

				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out
						.println("SQL Exception in function addDeptIsolation in class IsolationDAO");
			} finally {
				dbBureauConn2 = null;
				stmnt2 = null;
			}
		}
	}

	@Override
	public void changeIsolationActivity(int isolationId, boolean enabled) {
		String query = "UPDATE Alarming_Isolation SET enabled="
				+ (enabled ? 1 : 0) + " WHERE isolation_id=" + isolationId;
		dbMYSQLBureauConnect();
		Statement stmnt = null;
		try {
			stmnt = dbBureauConn2.createStatement();
			stmnt.executeUpdate(query);
			// System.out.println("I am changing Isolation disable enable"+
			// query);
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function changeIsolationActivity in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
		}
	}

	//
	// @Override
	// public ArrayList<Integer> getIsolationAlarmTraps(int isolationId) {
	// ArrayList<Integer> alarmTraps = new ArrayList<Integer> ();
	// String
	// query="SELECT alarmtrap_id FROM Alarming_Isolation_AlarmTraps WHERE isolation_id=?";
	// dbMYSQLBureauConnect();
	// PreparedStatement getTraps = null;
	// ResultSet result = null;
	// try{
	// getTraps=dbBureauConn2.prepareStatement(query);
	// getTraps.setInt(1, isolationId);
	// result=getTraps.executeQuery();
	// while (result.next()){
	// alarmTraps.add(result.getInt("alarmtrap_id"));
	// }
	// }catch (SQLException e) {
	// System.out.println("SQL Exception in function getIsolationAlarmTraps in class IsolationDAO");
	// }
	// finally{
	// dbBureauConn2=null;
	// getTraps=null;
	// result = null;
	// }
	// return alarmTraps;
	// }
	//
	//
	// @Override
	// public ArrayList<Integer> getIsolationAlarmAssets(int isolationId) {
	// ArrayList<Integer> alarmAssets = new ArrayList<Integer> ();
	// String
	// query="SELECT alarmasset_id FROM Alarming_Isolation_AlarmAssets WHERE isolation_id=?";
	// dbMYSQLBureauConnect();
	// PreparedStatement getTraps = null;
	// ResultSet result = null;
	// try{
	// getTraps=dbBureauConn2.prepareStatement(query);
	// getTraps.setInt(1, isolationId);
	// result=getTraps.executeQuery();
	// while (result.next()){
	// alarmAssets.add(result.getInt("alarmasset_id"));
	// }
	// }catch (SQLException e) {
	// System.out.println("SQL Exception in function getIsolationAlarmAssets in class IsolationDAO");
	// }
	// finally{
	// dbBureauConn2=null;
	// getTraps=null;
	// result = null;
	// }
	// return alarmAssets;
	// }
	//
	//
	@Override
	public List<AlarmAssetIsolation> getAlarmAssetsIsolationAdd(int departmentId) {
		List<AlarmAssetIsolation> alarmAssetsIsolation = new ArrayList<AlarmAssetIsolation>();
		// Changed by Nayyab Added unkown fixture type hidden 15 Sep 2011
		String query = "SELECT alarmasset_id,dep_id,Fixture_Name,Fixture_Type FROM bureauv2alarms.alarming_alarmassets WHERE dep_id="
				+ departmentId
				+ " and Fixture_Name!='Unknown' ORDER BY Fixture_Name " + ";";
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMYSQLBureauConnect();
			stmnt = dbBureauConn2.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				AlarmAssetIsolation newAsset = new AlarmAssetIsolation(
						result.getInt("alarmasset_id"),
						result.getInt("dep_id"),
						result.getString("Fixture_Name"),
						result.getString("Fixture_Type"), false, false);
				alarmAssetsIsolation.add(newAsset);
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getAlarmAssetsIsolationAdd in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;

		}
		return alarmAssetsIsolation;
	}

	@Override
	public AlarmAssetIsolation getAlarmAssetsIsolationByAlarmAssetId(
			int alarmAssetId) {
		AlarmAssetIsolation newAsset = null;
		String query = "SELECT alarmasset_id,dep_id,Fixture_Name,Fixture_Type FROM bureauv2alarms.alarming_alarmassets WHERE alarmasset_id="
				+ alarmAssetId
				+ " and Fixture_Name!='Unknown' ORDER BY Fixture_Name " + ";";
//		System.out.println("alarm asset query:" + query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMYSQLBureauConnect();
			stmnt = dbBureauConn2.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				newAsset = new AlarmAssetIsolation(
						result.getInt("alarmasset_id"),
						result.getInt("dep_id"),
						result.getString("Fixture_Name"),
						result.getString("Fixture_Type"), false, false);
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getAlarmAssetsIsolationAdd in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;

		}
		return newAsset;
	}

	@Override
	public List<Integer> getActiveIsolatedAssetIds(List<Asset> assetList) {
		String idList = "(";
		for (Asset asset : assetList) {
			idList += asset.getId() + ",";
		}
		idList = idList.substring(0, idList.lastIndexOf(","));
		idList += ")";
		List<Integer> isolatedAssetIds = new ArrayList<Integer>();
		String query = "SELECT aset.asset_id FROM "
				+ " asset as aset"
				+ " inner join alarming_isolation_alarmassets as isoAset on isoAset.alarmasset_id=aset.alarmasset_id"
				+ " inner join alarming_isolation as iso on iso.isolation_id=isoAset.isolation_id"
				+ " where  aset.asset_id IN "
				+ idList
				+ " AND( (isolation_type= 'once' AND (iso.enabled=1) AND(DATE_FORMAT(start_time, '%X %m %d')<DATE_FORMAT(NOW(), '%X %m %d')"
				+ " OR (DATE_FORMAT(start_time, '%X %m %d')=DATE_FORMAT(NOW(), '%X %m %d') AND (start_hour<=CURTIME()))) AND (DATE_FORMAT(end_time, '%X %m %d') > DATE_FORMAT(now(), '%X %m %d')"
				+ " OR ((DATE_FORMAT(end_time, '%X %m %d')=DATE_FORMAT(now(), '%X %m %d') AND end_hour>=CURTIME()))))"
				+ " OR (isolation_type='daily' AND iso.enabled=1 AND (DATE_FORMAT(start_time, '%X %m %d')<=DATE_FORMAT(NOW(), '%X %m %d') AND((start_hour<= CURTIME() AND end_hour>=CURTIME()))"
				+ " OR DATE_FORMAT(start_time, '%X %m %d')= DATE_FORMAT(NOW(), '%X %m %d') AND (start_hour<= CURTIME() AND end_hour>=CURTIME())))"
				+ " OR (isolation_type ='Weekly' AND (iso.enabled=1) AND (start_time<now() AND day_of_week = DAYNAME(NOW()) AND start_hour<=CURTIME() AND end_hour>=CURTIME()))"
				+ " OR (isolation_type like 'permanent' AND iso.enabled=1 AND ((DATE_FORMAT(start_time, '%X %m %d')<DATE_FORMAT(now(), '%X %m %d')"
				+ " OR (DATE_FORMAT(start_time, '%X %m %d')=DATE_FORMAT(NOW() , '%X %m %d')AND start_hour<=CURTIME())))))";

		Statement stmnt = null;
		ResultSet result = null;
		try {
//			System.out.println("query:" + query);
			dbBureauConn2 = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbBureauConn2.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				isolatedAssetIds.add(result.getInt("asset_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}
		return isolatedAssetIds;
	}

	@Override
	public List<Integer> getIsolatedAssetIds(List<Asset> assetList) {
//
//		List<Integer> isolatedAssetIds = new ArrayList<Integer>();
//		String query = "SELECT asset.[asset_id] FROM Asset_groups as agroup"
//				+ " inner Join Asset_tables as atable on agroup.asset_group_id = atable.asset_group_id"
//				+ " inner Join asset_table_asset as alinktable on atable.asset_table_id = alinktable.asset_table_id"
//				+ " Inner Join Asset as asset on alinktable.asset_id = asset.asset_id"
//				+ " inner join Alarming.Isolation_AlarmAssets as isoAset on isoAset.alarmasset_id=asset.alarmasset_id"
//				+ " inner join Alarming.Isolation as iso on iso.isolation_id=isoAset.isolation_id"
//				+ " where  agroup.asset_group_id = " + assetgroupId;
//		Statement stmnt = null;
//		ResultSet result = null;
//		try {
////			System.out.println("query:" + query);
//			dbBureauConn2 = ConnectionBean.getInstance().getSQLConnection();
//			stmnt = dbBureauConn2.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()) {
//				isolatedAssetIds.add(result.getInt("asset_id"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			dbBureauConn2 = null;
//			stmnt = null;
//			result = null;
//		}
//		return isolatedAssetIds;
		String idList = "(";
		for (Asset asset : assetList) {
			idList += asset.getId() + ",";
		}
		idList = idList.substring(0, idList.lastIndexOf(","));
		idList += ")";
		List<Integer> isolatedAssetIds = new ArrayList<Integer>();
		String query = "SELECT aset.asset_id FROM "
				+ " asset as aset"
				+ " inner join alarming_isolation_alarmassets as isoAset on isoAset.alarmasset_id=aset.alarmasset_id"
				+ " inner join alarming_isolation as iso on iso.isolation_id=isoAset.isolation_id"
				+ " where  aset.asset_id IN "
				+ idList;

		Statement stmnt = null;
		ResultSet result = null;
		try {
//			System.out.println("query:" + query);
			dbBureauConn2 = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = dbBureauConn2.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				isolatedAssetIds.add(result.getInt("asset_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}
		return isolatedAssetIds;
	
	}

	@Override
	public List<AssetSearchStringIsolation> getSubfixtureIsolation(
			int alarmAssetId) {
		List<AssetSearchStringIsolation> assetSearchStrings = new ArrayList<AssetSearchStringIsolation>();

		String query = "SELECT * FROM bureauv2alarms.alarming_alarmassetssearchstring WHERE alarmasset_id="
				+ alarmAssetId + ";";
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMYSQLBureauConnect();
			stmnt = dbBureauConn2.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				AssetSearchStringIsolation newSearchString = new AssetSearchStringIsolation(
						result.getLong("Search_String_ID"),
						result.getInt("alarmasset_id"),
						result.getString("Controller_Search"),
						result.getString("Alarm_Search"),
						result.getString("Sub_Fixture"), false);
				assetSearchStrings.add(newSearchString);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;

		}
		return assetSearchStrings;
	}

	//
	// //Edited by Nayyab 07 Sep 2011, change in constrcutor initiation, new
	// field is added for name
	// @Override
	// public Isolation getSpecificIsolation(int isolationId) {
	// Isolation specificIsolation = null;
	// String query="SELECT * FROM Alarming_Isolation WHERE isolation_id=?";
	// dbMYSQLBureauConnect();
	// PreparedStatement stmnt = null;
	// ResultSet result = null;
	// try{
	// stmnt=dbBureauConn2.prepareStatement(query);
	// stmnt.setInt(1, isolationId);
	// result = stmnt.executeQuery();
	// while (result.next()){
	// //changed by Nayyab 07 Sep 2011
	// specificIsolation=new
	// Isolation(result.getInt("isolation_id"),result.getString("site_code"),result.getString("isolation_type"),result.getString("day_of_week"),
	// result.getDate("start_time"),result.getDate("end_time"),result.getBoolean("enabled"),result.getInt("user_id"),
	// result.getDate("created_on"),result.getString("start_hour"),result.getString("end_hour"),result.getInt("dep_id"),result.getString("site_contact_name"),result.getString("name"));
	// }
	// }catch (SQLException e){
	// System.out.println("SQL Exception in function getSpecificIsolation in class IsolationDAO");
	// }
	// finally{
	// dbBureauConn2=null;
	// stmnt=null;
	// result = null;
	// }
	// return specificIsolation;
	// }
	//

	@Override
	public List<AlarmTrap> getSpecificIsolationAlmTraps(int isolationId,
			int depId) {
		List<AlarmTrap> isolationAlmTraps = new ArrayList<AlarmTrap>();
		List<Integer> chosenTraps = new ArrayList<Integer>();
		String query = "SELECT alarmtrap_id FROM Alarming_Isolation_AlarmTraps WHERE isolation_id=?";
		dbMYSQLBureauConnect();
		ResultSet result = null;
		PreparedStatement stmnt = null;
		try {
			stmnt = dbBureauConn2.prepareStatement(query);
			stmnt.setInt(1, isolationId);
			result = stmnt.executeQuery();
			while (result.next()) {
				chosenTraps.add(result.getInt("alarmtrap_id"));
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getSpecificIsolationAlmTraps in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}

		query = "SELECT * FROM bureauv2alarms.alarming_alarmtrap WHERE dep_id=?;";
		PreparedStatement stmnt1 = null;
		ResultSet result1 = null;
		try {
			dbMYSQLBureauConnect();
			stmnt1 = dbBureauConn2.prepareStatement(query);
			stmnt1.setInt(1, depId);
			result1 = stmnt1.executeQuery();
			while (result1.next()) {
				AlarmTrap temp = new AlarmTrap(result1.getInt("alarm_trap_id"),
						result1.getBoolean("enabled"),
						result1.getString("name"),
						result1.getInt("response_time"),
						result1.getInt("maxoperatorhold_time"),
						result1.getBoolean("allow_hold"),
						result1.getInt("priority"),
						result1.getInt("workflow_id"), 0,
						result1.getBoolean("allow_dial"),
						result1.getBoolean("allow_clear"),
						result1.getInt("clear_threshold"),
						result1.getString("alarmtrap_type"),
						result1.getBoolean("autocheck_enabled"),
						result1.getString("site_code"),
						result1.getString("controller_name"), depId);
				if (chosenTraps.indexOf(temp.getAlarmTrapId()) != -1) {
					temp.setChosen(true);
				}
				isolationAlmTraps.add(temp);
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getSpecificIsolationAlmTraps in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt1 = null;
			result1 = null;

		}

		return isolationAlmTraps;
	}

	@Override
	public List<AlarmAssetIsolation> getSpecificIsolationAlmAssets(
			int isolationId, int depId) {
		List<AlarmAssetIsolation> isolationAlmAssets = new ArrayList<AlarmAssetIsolation>();
		List<Integer> isolatedAssets = new ArrayList<Integer>();
		List<Long> isolatedSubfixtures = new ArrayList<Long>();

		String query = "SELECT alarmasset_id,Search_String_ID FROM Alarming_Isolation_AlarmAssets WHERE isolation_id=? ";
		dbMYSQLBureauConnect();
		PreparedStatement stmnt = null;
		ResultSet result = null;
		try {
			stmnt = dbBureauConn2.prepareStatement(query);
			stmnt.setInt(1, isolationId);
			result = stmnt.executeQuery();
			while (result.next()) {
				isolatedAssets.add(result.getInt("alarmasset_id"));
				isolatedSubfixtures.add(result.getLong("Search_String_ID"));
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getSpecificIsolationAlmAssets in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;

		}

		query = "SELECT alarmasset_id,Fixture_Name,Fixture_Type FROM bureauv2alarms.alarming_alarmassets WHERE dep_id=? and Fixture_Name!='Unknown' ORDER BY Fixture_Name;";
		PreparedStatement stmnt1 = null;
		ResultSet result1 = null;
		PreparedStatement stmnt2 = null;
		ResultSet subResults = null;
		try {
			dbMYSQLBureauConnect();
			stmnt1 = dbBureauConn2.prepareStatement(query);
			stmnt1.setInt(1, depId);
			result1 = stmnt1.executeQuery();
			while (result1.next()) {
				AlarmAssetIsolation temp = new AlarmAssetIsolation(
						result1.getInt("alarmasset_id"), depId,
						result1.getString("Fixture_Name"),
						result1.getString("Fixture_Type"), false, false);
				if (isolatedAssets.indexOf(temp.getAlarmAssetId()) != -1) {
					if (isolatedSubfixtures.get(isolatedAssets.indexOf(temp
							.getAlarmAssetId())) != 0) {
						temp.setSubfixtureConfig(true);
					} else {
						temp.setWholeFixture(true);
					}
				}

				String subQuery = "SELECT * FROM bureauv2alarms.alarming_alarmassetssearchstring WHERE alarmasset_id=?;";
				stmnt2 = dbBureauConn2.prepareStatement(subQuery);
				stmnt2.setInt(1, temp.getAlarmAssetId());
				List<AssetSearchStringIsolation> assetsSubfixtures = new ArrayList<AssetSearchStringIsolation>();
				subResults = stmnt2.executeQuery();
				if (temp.isWholeFixture() == false) {
					while (subResults.next()) {
						AssetSearchStringIsolation searchString = new AssetSearchStringIsolation(
								subResults.getLong("Search_String_ID"),
								temp.getAlarmAssetId(),
								subResults.getString("Controller_Search"),
								subResults.getString("Alarm_Search"),
								subResults.getString("Sub_Fixture"), false);
						if (isolatedSubfixtures.indexOf(searchString
								.getSearchStringId()) != -1) {
							searchString.setIsolate(true);
						}
						assetsSubfixtures.add(searchString);
					}
				} else {
					while (subResults.next()) {
						AssetSearchStringIsolation searchString = new AssetSearchStringIsolation(
								subResults.getLong("Search_String_ID"),
								temp.getAlarmAssetId(),
								subResults.getString("Controller_Search"),
								subResults.getString("Alarm_Search"),
								subResults.getString("Sub_Fixture"), false);
						assetsSubfixtures.add(searchString);
					}
				}
				temp.setSubfixtureIdList(assetsSubfixtures);
				isolationAlmAssets.add(temp);
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getSpecificIsolationAlmAssets in class IsolationDAO");
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt1 = null;
			result1 = null;
			stmnt2 = null;
			subResults = null;

		}
		return isolationAlmAssets;
	}

	@Override
	public void modDeptIsolation(Isolation modIsolation, String startHour,
			String endHour, ArrayList<Integer> alarmTrapId,
			ArrayList<IsolationAssetSubfixtures> alarmAssetSubfixtures) {
		String query = "UPDATE Alarming_Isolation "
				+ "SET isolation_type = ?, " + "day_of_week = ?, "
				+ "start_time = ?, " + "end_time= ?, " + "start_hour = ?, "
				+ "end_hour = ?, " + "site_contact_name = ?, " + "name = ? "
				+ "WHERE isolation_id=?";
		dbMYSQLBureauConnect();
		PreparedStatement stmnt = null;
		try {
			stmnt = dbBureauConn2.prepareStatement(query);
			stmnt.setString(1, modIsolation.getIsolationType());
			if (modIsolation.getDayOfWeek() == null) {
				stmnt.setNull(2, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(2, modIsolation.getDayOfWeek());
			}
			stmnt.setDate(3, new java.sql.Date(modIsolation.getStartTime()
					.getTime()));
			if (modIsolation.getEndTime() == null) {
				stmnt.setNull(4, java.sql.Types.DATE);
			} else {
				stmnt.setDate(4, new java.sql.Date(modIsolation.getEndTime()
						.getTime()));
			}
			stmnt.setString(5, startHour);
			if (endHour == null) {
				stmnt.setNull(6, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(6, endHour);
			}
			if (modIsolation.getName() == null) {
				stmnt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(7, modIsolation.getName());
			}
			if (modIsolation.getNameMore() == null) {
				stmnt.setNull(8, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(8, modIsolation.getNameMore());
			}
			stmnt.setInt(9, modIsolation.getIsolationId());
			stmnt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
		}

		query = "DELETE FROM Alarming_Isolation_AlarmAssets WHERE isolation_id="
				+ modIsolation.getIsolationId();
		Statement stmnt1 = null;
		dbMYSQLBureauConnect();
		try {
			stmnt1 = dbBureauConn2.createStatement();
			stmnt1.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function modDeptIsolation in class IsolationDAO AlarmAssets");
		} finally {
			dbBureauConn2 = null;
			stmnt1 = null;
		}
		dbMYSQLBureauConnect();
		query = "DELETE FROM Alarming_Isolation_AlarmTraps WHERE isolation_id="
				+ modIsolation.getIsolationId();
		Statement stmnt2 = null;
		try {
			stmnt2 = dbBureauConn2.createStatement();
			stmnt2.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function modDeptIsolation in class IsolationDAO alarmtraps");
		} finally {
			dbBureauConn2 = null;
			stmnt2 = null;
		}

		PreparedStatement stmnt3 = null;
		dbMYSQLBureauConnect();
		try {
			for (int i = 0; i <= alarmAssetSubfixtures.size()-1; i++) {
				query = "INSERT INTO Alarming_Isolation_AlarmAssets (isolation_id,alarmasset_id,Search_String_ID) "
						+ "VALUES (?,?,?)";
				stmnt3 = dbBureauConn2.prepareStatement(query);
				System.out.println("isolation id: "+modIsolation.getIsolationId());
				System.out.println("asset id: "+alarmAssetSubfixtures.get(i).getAlarmAssetId());
				stmnt3.setInt(1, modIsolation.getIsolationId());
				stmnt3.setInt(2, alarmAssetSubfixtures.get(i).getAlarmAssetId());
				if (alarmAssetSubfixtures.get(i).getSubfixtureId() != 0) {
					stmnt3.setLong(3, alarmAssetSubfixtures.get(i)
							.getSubfixtureId());
				} else {
					stmnt3.setNull(3, java.sql.Types.BIGINT);
				}
				int row=stmnt3.executeUpdate();
				System.out.println("rows affected: "+row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function modDeptIsolation in class IsolationDAO AlarmAssets");
		} finally {
			dbBureauConn2 = null;
			stmnt3 = null;
		}
		dbMYSQLBureauConnect();
		Statement stmnt4 = null;
		query = "INSERT INTO Alarming_Isolation_AlarmTraps (isolation_id,alarmtrap_id) "
				+ "VALUES";
		try {
			for (int j = 0; j <= alarmTrapId.size() - 1; j++) {
				query += "(" + modIsolation.getIsolationId() + ","
						+ alarmTrapId.get(j) + "),";
				stmnt4 = dbBureauConn2.createStatement();
			}
			if (stmnt != null) {
				stmnt4.executeUpdate(query.replaceAll(",$", ""));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function modDeptIsolation in class IsolationDAO alarmtraps");
		} finally {
			dbBureauConn2 = null;
			stmnt4 = null;
		}

	}

	@Override
	public List<Isolation> getGlobalIsolations(int depId, String siteCode,
			String isolationType) {
		List<Isolation> deptIsolations = new ArrayList<Isolation>();
		// System.out.println("Info from Global Isl dep Id.."+depId+"..siteCode.."+siteCode+"..Isolation Type.."+isolationType);
		dbMYSQLBureauConnect();
		String query = "";
		if ((depId != 0) && (isolationType == null)) {
			// Changed by Nayyab AUG 26, 2011
			// query="SELECT * FROM Number6V2.Alarming.Isolation WHERE dep_id="+depId
			// + " ORDER BY start_time";
			// Changed by Nayyab Sep 07, 2011 to show new column
			query = "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"
					+ " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"
					+ " FROM `bureauv2alarms`. `ALARMING_ISOLATION` i "
					+ "JOIN `bureauv2alarms`. `USERS` u"
					+ " ON i.user_id= u.user_id"
					+ " WHERE dep_id="
					+ depId
					+ " ORDER BY i.isolation_id desc;";

		} else if ((depId == 0) && (isolationType != null)) {
			if (isolationType.equals("Permanent")) {
				// Commented by Nayyab Aug 26,2011
				// query="SELECT * FROM Number6V2.Alarming.Isolation WHERE site_code='"
				// + siteCode
				// +"' and isolation_type='Once' and end_time IS NULL ORDER BY start_time";
				// Changed by Nayyab Sep 07, 2011 to show new column i.name
				query = "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"
						+ " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"
						+ " FROM ALARMING_ISOLATION i "
						+ "JOIN USERS u"
						+ " ON i.user_id= u.user_id"
						+ " WHERE site_code='"
						+ siteCode
						+ "' and isolation_type='Permanent' and end_time IS NULL ORDER BY i.isolation_id desc;";
			} else {
				query = "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"
						+ " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"
						+ " FROM ALARMING_ISOLATION i "
						+ "JOIN USERS u"
						+ " ON i.user_id= u.user_id"
						+ " WHERE site_code='"
						+ siteCode
						+ "' and isolation_type='"
						+ isolationType
						+ "' ORDER BY i.isolation_id desc;";

			}
		} else if ((depId == 0) && (isolationType == null)) {
			// Commented by Nayyab Aug 26,2011
			// query="SELECT * FROM Number6V2.Alarming.Isolation WHERE site_code='"
			// + siteCode +"' ORDER BY start_time";
			// Changed by Nayyab Sep 07, 2011 to show new column i.name
			query = "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"
					+ " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"
					+ " FROM ALARMING_ISOLATION i "
					+ "JOIN USERS u"
					+ " ON i.user_id= u.user_id"
					+ " WHERE site_code='"
					+ siteCode + "' ORDER BY i.isolation_id desc;";
		} else {
			if (isolationType.equals("Permanent")) {
				// Commented by Nayyab Aug 26,2011
				// query="SELECT * FROM Number6V2.Alarming.Isolation WHERE dep_id="+depId
				// +
				// " and  isolation_type='Once' and end_time IS NULL ORDER BY start_time";
				// Changed by Nayyab Sep 07, 2011 to show new column i.name
				query = "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"
						+ " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"
						+ " FROM ALARMING_ISOLATION i "
						+ "JOIN USERS u"
						+ " ON i.user_id= u.user_id"
						+ " WHERE dep_id= "
						+ depId
						+ " and  isolation_type='Permanent' and end_time IS NULL ORDER BY i.isolation_id desc";
			} else {
				// Commented by Nayyab Aug 26,2011
				// query="SELECT * FROM Number6V2.Alarming.Isolation WHERE dep_id="+depId
				// + " and  isolation_type='" + isolationType +
				// "' ORDER BY start_time";
				// Changed by Nayyab Sep 07, 2011 to show new column
				query = "SELECT i.isolation_id, i.site_code, i.isolation_type,i.day_of_week,i.start_time,i.end_time,"
						+ " i.enabled,i.user_id,i.created_on,i.start_hour,i.end_hour,dep_id,i.site_contact_name,i.name,u.username"
						+ " FROM ALARMING_ISOLATION i "
						+ "JOIN USERS u"
						+ " ON i.user_id= u.user_id"
						+ " WHERE dep_id= "
						+ depId
						+ " and  isolation_type='"
						+ isolationType
						+ "' ORDER BY i.isolation_id desc";
			}
		}
		Statement stmnt = null;
		ResultSet result = null;

		try {
			stmnt = dbBureauConn2.createStatement();
			// System.out.println("Query for GlobalISl.."+query);
			result = stmnt.executeQuery(query);
			while (result.next()) {
				// Changed by Nayyab Sep 07, 2011 to show new column "name"
				Isolation temp;

				temp = new Isolation(result.getInt("isolation_id"),
						result.getString("site_code"),
						result.getString("isolation_type"),
						result.getString("day_of_week"),
						result.getDate("start_time"),
						result.getDate("end_time"),
						result.getBoolean("enabled"), result.getInt("user_id"),
						result.getDate("created_on"),
						result.getString("start_hour"),
						result.getString("end_hour"), result.getInt("dep_id"),
						result.getString("site_contact_name"),
						result.getString("username"), result.getString("name"));
				deptIsolations.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("SQL Exception in function getGlobalIsolations in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}
		return deptIsolations;
	}

	/*
	 * private final String convertTimeZone(String date, String time, TimeZone
	 * fromTimezone, TimeZone toTimeZone){ Calendar cal =
	 * Calendar.getInstance(fromTimezone); String[] dateSplit = null; String[]
	 * timeSplit = null; if(time !=null){ timeSplit = time.split(":"); }
	 * if(date!=null){ dateSplit = date.split("/"); } if(dateSplit !=null){
	 * cal.set(Calendar.DATE, Integer.parseInt(dateSplit[0]));
	 * cal.set(Calendar.MONTH, Integer.parseInt(dateSplit[1])-1);
	 * cal.set(Calendar.YEAR, Integer.parseInt(dateSplit[2])); } if(timeSplit
	 * !=null){ cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
	 * cal.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
	 * cal.get(Calendar.HOUR_OF_DAY); cal.get(Calendar.MINUTE); } //
	 * System.out.println("Time in " + fromTimezone.getDisplayName() + " : " +
	 * cal.get(Calendar.DATE) +"/"+ (cal.get(Calendar.MONTH)+1)+"/"+
	 * cal.get(Calendar.YEAR) +" " + ((cal.get(Calendar.HOUR_OF_DAY)<10) ?
	 * ("0"+cal.get(Calendar.HOUR_OF_DAY) ): (cal.get(Calendar.HOUR_OF_DAY))) //
	 * +":" + (cal.get(Calendar.MINUTE)<10 ? "0"+cal.get(Calendar.MINUTE) :
	 * cal.get(Calendar.MINUTE)) ); cal.setTimeZone(toTimeZone);
	 * 
	 * // System.out.println("Time in " + toTimeZone.getDisplayName() + " : " +
	 * cal.get(Calendar.DATE) +"/"+ (cal.get(Calendar.MONTH)+1)+"/"+
	 * cal.get(Calendar.YEAR) +" " + ((cal.get(Calendar.HOUR_OF_DAY)<10) ?
	 * ("0"+cal.get(Calendar.HOUR_OF_DAY) ): (cal.get(Calendar.HOUR_OF_DAY))) //
	 * +":" + (cal.get(Calendar.MINUTE)<10 ? "0"+cal.get(Calendar.MINUTE) :
	 * cal.get(Calendar.MINUTE)) ); if(date!=null){ return
	 * cal.get(Calendar.DATE) +"/"+ (cal.get(Calendar.MONTH)+1)+"/"+
	 * cal.get(Calendar.YEAR) +" " + ((cal.get(Calendar.HOUR_OF_DAY)<10) ?
	 * ("0"+cal.get(Calendar.HOUR_OF_DAY) ): (cal.get(Calendar.HOUR_OF_DAY)))
	 * +":" + (cal.get(Calendar.MINUTE)<10 ? "0"+cal.get(Calendar.MINUTE) :
	 * cal.get(Calendar.MINUTE)); } else if(date==null && time !=null) { return
	 * null+" "+((cal.get(Calendar.HOUR_OF_DAY)<10) ?
	 * ("0"+cal.get(Calendar.HOUR_OF_DAY) ): (cal.get(Calendar.HOUR_OF_DAY)))
	 * +":" + (cal.get(Calendar.MINUTE)<10 ? "0"+cal.get(Calendar.MINUTE) :
	 * cal.get(Calendar.MINUTE)); } else { return null; } }
	 */

	@Override
	public List<AlarmTrap> getIsolatedAlmTraps(int isolationId, int depId) {
		List<AlarmTrap> isolationAlmTraps = new ArrayList<AlarmTrap>();
		List<Integer> chosenTraps = new ArrayList<Integer>();
		String query = "SELECT alarmtrap_id FROM Alarming_Isolation_AlarmTraps WHERE isolation_id=?";
		dbMYSQLBureauConnect();
		PreparedStatement stmnt = null;
		ResultSet result = null;
		try {
			stmnt = dbBureauConn2.prepareStatement(query);
			stmnt.setInt(1, isolationId);
			result = stmnt.executeQuery();
			while (result.next()) {
				chosenTraps.add(result.getInt("alarmtrap_id"));
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getIsolatedAlmTraps in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;
		}

		query = "SELECT * FROM bureauv2alarms.alarming_alarmtrap WHERE dep_id=?;";
		PreparedStatement stmnt1 = null;
		ResultSet result1 = null;
		dbMYSQLBureauConnect();
		try {

			stmnt1 = dbBureauConn2.prepareStatement(query);
			stmnt1.setInt(1, depId);
			result1 = stmnt1.executeQuery();
			while (result1.next()) {
				AlarmTrap temp = new AlarmTrap(result1.getInt("alarm_trap_id"),
						result1.getBoolean("enabled"),
						result1.getString("name"),
						result1.getInt("response_time"),
						result1.getInt("maxoperatorhold_time"),
						result1.getBoolean("allow_hold"),
						result1.getInt("priority"),
						result1.getInt("workflow_id"), 0,
						result1.getBoolean("allow_dial"),
						result1.getBoolean("allow_clear"),
						result1.getInt("clear_threshold"),
						result1.getString("alarmtrap_type"),
						result1.getBoolean("autocheck_enabled"),
						result1.getString("site_code"),
						result1.getString("controller_name"), depId);
				if (chosenTraps.indexOf(temp.getAlarmTrapId()) != -1) {
					temp.setChosen(true);
					isolationAlmTraps.add(temp);
				}
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getIsolatedAlmTraps in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt1 = null;
			result1 = null;

		}

		return isolationAlmTraps;
	}

	@Override
	public List<IsolatedAsset> getIsolatedAlmAssets(int isolationId, int depId) {
		List<IsolatedAsset> isolationAlmAssets = new ArrayList<IsolatedAsset>();
		List<Integer> isolatedAssets = new ArrayList<Integer>();
		List<Long> isolatedSubfixtures = new ArrayList<Long>();
		String query = "SELECT alarmasset_id,Search_String_ID FROM Alarming_Isolation_AlarmAssets WHERE isolation_id=?";
		PreparedStatement stmnt = null;
		ResultSet result = null;
		dbMYSQLBureauConnect();
		try {
			stmnt = dbBureauConn2.prepareStatement(query);
			stmnt.setInt(1, isolationId);
			result = stmnt.executeQuery();
			while (result.next()) {
				isolatedAssets.add(result.getInt("alarmasset_id"));
				isolatedSubfixtures.add(result.getLong("Search_String_ID"));
			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getIsolatedAlmAssets in class IsolationDAO");
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt = null;
			result = null;

		}
		PreparedStatement stmnt1 = null;
		ResultSet result1 = null;
		ResultSet subResults = null;
		PreparedStatement stmnt2 = null;
		query = "SELECT alarmasset_id,Fixture_Name,Fixture_Type FROM bureauv2alarms.alarming_alarmassets WHERE dep_id=?;";
		try {
			dbMYSQLBureauConnect();
			stmnt1 = dbBureauConn2.prepareStatement(query);
			stmnt1.setInt(1, depId);
			result1 = stmnt1.executeQuery();
			while (result1.next()) {
				AlarmAssetIsolation temp = new AlarmAssetIsolation(
						result1.getInt("alarmasset_id"), depId,
						result1.getString("Fixture_Name"),
						result1.getString("Fixture_Type"), false, false);
				if (isolatedAssets.indexOf(temp.getAlarmAssetId()) != -1) {
					if (isolatedSubfixtures.get(isolatedAssets.indexOf(temp
							.getAlarmAssetId())) != 0) {
						temp.setSubfixtureConfig(true);
						String subQuery = "SELECT * FROM bureauv2alarms.alarming_alarmassetssearchstring WHERE alarmasset_id=?;";
						stmnt2 = dbBureauConn2.prepareStatement(subQuery);
						stmnt2.setInt(1, temp.getAlarmAssetId());
						List<AssetSearchStringIsolation> assetsSubfixtures = new ArrayList<AssetSearchStringIsolation>();
						subResults = stmnt2.executeQuery();
						if (temp.isWholeFixture() == false) {
							while (subResults.next()) {
								AssetSearchStringIsolation searchString = new AssetSearchStringIsolation(
										subResults.getLong("Search_String_ID"),
										temp.getAlarmAssetId(),
										subResults
												.getString("Controller_Search"),
										subResults.getString("Alarm_Search"),
										subResults.getString("Sub_Fixture"),
										false);
								if (isolatedSubfixtures.indexOf(searchString
										.getSearchStringId()) != -1) {
									searchString.setIsolate(true);
									assetsSubfixtures.add(searchString);
								}
							}
						}
						temp.setSubfixtureIdList(assetsSubfixtures);
						for (int i = 0; i <= assetsSubfixtures.size() - 1; i++) {
							isolationAlmAssets.add(new IsolatedAsset(temp
									.getFixtureName(), temp.getFixtureType(),
									assetsSubfixtures.get(i).getSubfixture()));
						}
					} else {
						temp.setWholeFixture(true);
						isolationAlmAssets.add(new IsolatedAsset(temp
								.getFixtureName(), temp.getFixtureType(),
								"None"));
					}
				}

			}
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function getIsolatedAlmAssets in class IsolationDAO");
			e.printStackTrace();
		} finally {
			dbBureauConn2 = null;
			stmnt1 = null;
			result1 = null;
			stmnt2 = null;
			subResults = null;

		}
		return isolationAlmAssets;
	}

	// //Added by Nayyab to delete the Isolations from Table 05-09-2011
	@Override
	public void delIsolation(int isolationId, int depId) {
		String query = "delete from ALARMING_ISOLATION where Isolation_id= "
				+ isolationId;
		// System.out.println("Delete isoaltion with id "+ isolationId);
		// TODO:Do we need to delete Isolation from AlarmTrap, Alarm Asset?

		// "DELETE FROM [n6v2_toDelete].[dbo].[VoicePromptScripts] WHERE prompt_id="
		// + promptId;
		Statement stmnt = null;
		try {
			dbMYSQLBureauConnect();
			stmnt = dbBureauConn2.createStatement();
			stmnt.executeUpdate(query);
		} catch (SQLException e) {
			System.out
					.println("SQL Exception in function delIsolation in class IsolationDAO");
		} finally {
			dbBureauConn2 = null;
			stmnt = null;

		}

	}

	@Override
	public List<Isolation> getActiveIsolations(String siteCode, String type,
			int deptId) {
		// System.out.println(siteCode + "\n" + type+ "\n" + deptId);
		List<Isolation> isoList = new ArrayList<Isolation>();
		String query = "CALL `bureauv2alarms`.`GetActiveIsolations`(?,?,?)";
		Connection conn = null;
		CallableStatement stmnt = null;
		ResultSet result = null;
		try {
			conn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = conn.prepareCall(query);
			stmnt.setString(1, siteCode);
			stmnt.setString(2, type);
			stmnt.setInt(3, deptId);
			result = stmnt.executeQuery();
			while (result.next()) {
				Isolation temp = new Isolation(result.getInt("isolation_id"),
						result.getString("site_code"),
						result.getString("isolation_type"),
						result.getString("day_of_week"),
						result.getDate("start_time"),
						result.getDate("end_time"),
						result.getBoolean("enabled"), result.getInt("user_id"),
						result.getDate("created_on"),
						result.getString("start_hour"),
						result.getString("end_hour"), result.getInt("dep_id"),
						result.getString("site_contact_name"),
						result.getString("username"), result.getString("name"));
				isoList.add(temp);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmnt != null) {
					stmnt.close();
					stmnt = null;
				}
				if (result != null) {
					result.close();
					result = null;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

		return isoList;
	}

	@Override
	public List<Isolation> getInactiveIsolations(String siteCode, String type,
			int deptId) {
		String query = "CALL `bureauv2alarms`.`GetInactiveIsolations`(?,?,?)";
		Connection conn = null;
		CallableStatement stmnt = null;
		ResultSet result = null;
		List<Isolation> isoList = null;
		try {
			conn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = conn.prepareCall(query);
			stmnt.setString(1, siteCode);
			stmnt.setString(2, type);
			stmnt.setInt(3, deptId);
			if (stmnt.execute()) {
				result = stmnt.getResultSet();
				isoList = new ArrayList<Isolation>();
				while (result.next()) {
					Isolation temp = new Isolation(
							result.getInt("isolation_id"),
							result.getString("site_code"),
							result.getString("isolation_type"),
							result.getString("day_of_week"),
							result.getDate("start_time"),
							result.getDate("end_time"),
							result.getBoolean("enabled"),
							result.getInt("user_id"),
							result.getDate("created_on"),
							result.getString("start_hour"),
							result.getString("end_hour"),
							result.getInt("dep_id"),
							result.getString("site_contact_name"),
							result.getString("username"),
							result.getString("name"));
					isoList.add(temp);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmnt != null) {
					stmnt.close();
					stmnt = null;
				}
				if (result != null) {
					result.close();
					result = null;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return isoList;
	}

	@Override
	public List<Isolation> getDisabledIsolations(String siteCode, String type,
			int deptId) {
		String query = "CALL `bureauv2alarms`.`GetDisabledIsolations`(?,?,?)";
		Connection conn = null;
		CallableStatement stmnt = null;
		ResultSet result = null;
		List<Isolation> isoList = null;
		try {
			conn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = conn.prepareCall(query);
			stmnt.setString(1, siteCode);
			stmnt.setString(2, type);
			stmnt.setInt(3, deptId);
			result = stmnt.executeQuery();
			// System.out.println("in here");
			isoList = new ArrayList<Isolation>();
			while (result.next()) {
				Isolation temp = new Isolation(result.getInt("isolation_id"),
						result.getString("site_code"),
						result.getString("isolation_type"),
						result.getString("day_of_week"),
						result.getDate("start_time"),
						result.getDate("end_time"),
						result.getBoolean("enabled"), result.getInt("user_id"),
						result.getDate("created_on"),
						result.getString("start_hour"),
						result.getString("end_hour"), result.getInt("dep_id"),
						result.getString("site_contact_name"),
						result.getString("username"), result.getString("name"));
				isoList.add(temp);
			}
			// System.out.println("in here");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmnt != null) {
					stmnt.close();
					stmnt = null;
				}
				if (result != null) {
					result.close();
					result = null;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return isoList;
	}

	@Override
	public List<Isolation> getExpiredIsolations(String siteCode, String type,
			int deptId) {
		String query = "CALL `bureauv2alarms`.`GetExpiredIsolations`(?,?,?)";
		Connection conn = null;
		CallableStatement stmnt = null;
		ResultSet result = null;
		List<Isolation> isoList = null;
		try {
			conn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = conn.prepareCall(query);
			stmnt.setString(1, siteCode);
			stmnt.setString(2, type);
			stmnt.setInt(3, deptId);
			result = stmnt.executeQuery();
			isoList = new ArrayList<Isolation>();
			while (result.next()) {
				Isolation temp = new Isolation(result.getInt("isolation_id"),
						result.getString("site_code"),
						result.getString("isolation_type"),
						result.getString("day_of_week"),
						result.getDate("start_time"),
						result.getDate("end_time"),
						result.getBoolean("enabled"), result.getInt("user_id"),
						result.getDate("created_on"),
						result.getString("start_hour"),
						result.getString("end_hour"), result.getInt("dep_id"),
						result.getString("site_contact_name"),
						result.getString("username"), result.getString("name"));
				isoList.add(temp);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmnt != null) {
					stmnt.close();
					stmnt = null;
				}
				if (result != null) {
					result.close();
					result = null;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return isoList;
	}

	// @Override
	// public List<AlarmTrap> getIsolationAlmTrapsAdd(int depId) {
	// List<AlarmTrap> almTraps =null;
	// Connection conn =null;
	// PreparedStatement stmnt = null;
	// ResultSet rs = null;
	// try {
	// conn = ConnectionBean.getInstance().getMYSQLConnection();
	// stmnt =
	// conn.prepareStatement("SELECT * FROM `bureauv2alarms`.`alarming_alarmtrap` where dep_id=? and enabled=1;");
	// stmnt.setInt(1, depId);
	// rs = stmnt.executeQuery();
	// almTraps = new ArrayList<AlarmTrap>();
	// while(rs.next()){
	// almTraps.add(new AlarmTrap(rs.getInt("alarm_trap_id"),
	// rs.getBoolean("enabled"), rs.getString("name"),
	// rs.getInt("response_time"),
	// rs.getInt("maxOperatorHold_time"), rs.getBoolean("allow_hold"),
	// rs.getInt("priority"), rs.getInt("workflow_id"),
	// rs.getInt("stock_valuation_id"), rs.getBoolean("allow_dial"),
	// rs.getBoolean("allow_clear"), rs.getInt("clear_threshold"),
	// rs.getString("alarmtrap_type"), rs.getBoolean("autocheck_enabled"),
	// rs.getString("site_code"), rs.getString("controller_name"),
	// rs.getInt("dep_id")));
	// }
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// finally {
	// try {
	// stmnt.close();
	// rs.close();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// finally {
	// stmnt=null;
	// rs=null;
	// conn = null;
	// }
	// }
	// return almTraps;
	// }
	@Override
	public List<Isolation> getIsolationsByAlarmAssetId(int id) {
		String query = "SELECT * FROM alarming_isolation_alarmassets ia inner join alarming_isolation i on ia.isolation_id=i.isolation_id where ia.alarmasset_id="
				+ id;
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet result = null;
		List<Isolation> isoList = null;
//		System.out.println("query: " + query);
		try {
			conn = ConnectionBean.getInstance().getMYSQLConnection();
			stmnt = conn.prepareStatement(query);
			result = stmnt.executeQuery();
			isoList = new ArrayList<Isolation>();
			while (result.next()) {
				Isolation temp = new Isolation(result.getInt("isolation_id"),
						result.getString("site_code"),
						result.getString("isolation_type"),
						result.getString("day_of_week"),
						result.getDate("start_time"),
						result.getDate("end_time"),
						result.getBoolean("enabled"), result.getInt("user_id"),
						result.getDate("created_on"),
						result.getString("start_hour"),
						result.getString("end_hour"), result.getInt("dep_id"),
						result.getString("site_contact_name"),
						result.getString("name"));
				isoList.add(temp);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmnt != null) {
					stmnt.close();
					stmnt = null;
				}
				if (result != null) {
					result.close();
					result = null;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("isoList: "+isoList.size());
		return isoList;
	}
}

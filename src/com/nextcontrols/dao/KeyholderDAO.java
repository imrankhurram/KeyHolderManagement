package com.nextcontrols.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.nextcontrols.domain.ContactTypeDetails;
import com.nextcontrols.domain.Keyholder;
import com.nextcontrols.domain.KeyholderList;
import com.nextcontrols.domain.KeyholderListEnablingDetails;
import com.nextcontrols.utility.KeyholderUtility;

public class KeyholderDAO implements IKeyholderDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbMySQLConn;
	private static IKeyholderDAO instance;

	public static IKeyholderDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new KeyholderDAO();
		}
	}

	public void dbMySQLConnect() {
		try {
			dbMySQLConn = ConnectionBean.getInstance()
					.getMYSQLConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Keyholder> getFullKeyholders(String branch_code, int deptId) {
		// System.out.println("branch code:"+branch_code);
		// System.out.println("dept id: "+deptId);

		Map<Integer, Keyholder> keyholders = new TreeMap<Integer, Keyholder>();
		List<Keyholder> branchKeyholders = new ArrayList<Keyholder>();
		String query = "SELECT * from `keyholders_sitekeyholder` where branch_code='"
				+ branch_code + "'";
		// System.out.println("query: "+query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Keyholder new_keyholder = new Keyholder(
						result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				new_keyholder.setKeyholderList(new ArrayList<KeyholderList>());
				new_keyholder.setKeyholderListIds(new ArrayList<Integer>());
				keyholders.put(result.getInt("keyholder_id"), new_keyholder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		query = "SELECT * FROM `keyholders_sitekeyholder` as ksk "
				+ "inner join `keyholders_department_keyholderlist` as kdk "
				+ "inner join `keyholders_keyholderlist` as kk "
				+ "inner join `keyholders_keyholderlist_keyholders` as kkk "
				+ "WHERE ksk.branch_code = '" + branch_code + "' "
				+ "AND kdk.dep_id = " + deptId + " "
				+ "AND kk.keyholder_list_id = kdk.keyholder_list_id "
				+ "AND kkk.keyholder_list_id=kk.keyholder_list_id "
				+ "AND ksk.keyholder_id = kkk.keyholder_id "
				+ "AND (kk.list_type LIKE '%Weekday Occupancy%'"
				+ " OR kk.list_type LIKE '%Weekday OutOfHours%'"
				+ " OR kk.list_type LIKE '%Saturday Occupancy%'"
				+ " OR kk.list_type LIKE '%Saturday OutOfHours%'"
				+ " OR kk.list_type LIKE '%Sunday Occupancy%'"
				+ " OR kk.list_type LIKE '%Sunday OutOfHours%'"
				+ " OR kk.list_type LIKE '%Default%')";
		try {
			dbMySQLConnect();
//			System.out.println("query: "+query);
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Keyholder new_keyholder = new Keyholder(
						result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, result.getInt("contact_type"), false);
				new_keyholder.setListType(result.getString("list_type"));
				new_keyholder.setDisplayName(result.getString("display_name"));
				// System.out.println("keyholder id: "
				// + result.getInt("keyholder_id"));

				KeyholderList keyholderList = new KeyholderList(
						result.getInt("keyholder_list_id"),
						result.getString("list_name"),
						result.getString("list_type"),
						result.getTimestamp("start_date"),
						result.getTimestamp("end_date"),
						result.getString("occupancy_start"),
						result.getString("occupancy_end"),
						result.getString("comments"),
						result.getString("weekdays_active"));
				keyholderList.setDescription(result.getString("description"));
				keyholderList.setDisplayName(result.getString("display_name"));
			
				
				if (keyholders.get(result.getInt("keyholder_id")) != null) {
					Keyholder holder = keyholders.get(result
							.getInt("keyholder_id"));
					List<KeyholderList> list = holder.getKeyholderList();
					list.add(keyholderList);
					holder.setKeyholderList(list);
					ContactTypeDetails typeDetails=new ContactTypeDetails();
					typeDetails.setContactType(result.getInt("contact_type"));
					typeDetails.setContactString(KeyholderUtility.contactTypeText[result.getInt("contact_type")]);
					typeDetails.setIconName(KeyholderUtility.contactTypeIcon[result.getInt("contact_type")]);
					Map<Integer, ContactTypeDetails> contactMap=holder.getIdWithContactType();
					contactMap.put(keyholderList.getKeyholderListId(), typeDetails);
					holder.setIdWithContactType(contactMap);
//					for(Integer id: holder.getIdWithContactType().keySet()){
//						System.out.println("id: "+id+" - contact type: "+holder.getIdWithContactType().get(id));
//					}
					holder.setContact_type(result.getInt("contact_type"));
					//useless fields///
					holder.setContactString(KeyholderUtility.contactTypeText[result.getInt("contact_type")]);
					holder.setIconName(KeyholderUtility.contactTypeIcon[result.getInt("contact_type")]);
					//////////////////
					List<Integer> listNames = holder.getKeyholderListIds();
					listNames.add(keyholderList.getKeyholderListId());
					holder.setKeyholderListIds(listNames);
					keyholders.put(result.getInt("keyholder_id"), holder);
					
				} else {
					List<KeyholderList> keyholdersList = new ArrayList<KeyholderList>();
					keyholdersList.add(keyholderList);
					new_keyholder.setKeyholderList(keyholdersList);

					List<Integer> listNames = new ArrayList<Integer>();
					listNames.add(keyholderList.getKeyholderListId());
					new_keyholder.setKeyholderListIds(listNames);
					
					new_keyholder.setContact_type(result.getInt("contact_type"));
					ContactTypeDetails typeDetails=new ContactTypeDetails();
					typeDetails.setContactType(result.getInt("contact_type"));
					typeDetails.setContactString(KeyholderUtility.contactTypeText[result.getInt("contact_type")]);
					typeDetails.setIconName(KeyholderUtility.contactTypeIcon[result.getInt("contact_type")]);
					Map<Integer, ContactTypeDetails> contactMap=new_keyholder.getIdWithContactType();
					contactMap.put(keyholderList.getKeyholderListId(), typeDetails);
					new_keyholder.setIdWithContactType(contactMap);
					
					keyholders
							.put(result.getInt("keyholder_id"), new_keyholder);

				}

			}
			for (Integer key : keyholders.keySet()) {
				branchKeyholders.add(keyholders.get(key));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		return branchKeyholders;
	}

	@Override
	public List<KeyholderList> getKeyholdersTypes(String branch_code,
			int deptId, boolean isSpecial) {
		// System.out.println("branch code:"+branch_code);
		// System.out.println("dept id: "+deptId);
		List<KeyholderList> displayNames = new ArrayList<KeyholderList>();
		String query = "SELECT DISTINCT kk.display_name,kk.keyholder_list_id FROM `keyholders_sitekeyholder` as ksk "
				+ "inner join `keyholders_department_keyholderlist` as kdk "
				+ "inner join `keyholders_keyholderlist` as kk "
				+ "inner join `keyholders_keyholderlist_keyholders` as kkk "
				+ "WHERE ksk.branch_code = '"
				+ branch_code
				+ "' "
				+ "AND kdk.dep_id = "
				+ deptId
				+ " "
				+ "AND kk.keyholder_list_id = kdk.keyholder_list_id "
				+ "AND kkk.keyholder_list_id=kk.keyholder_list_id "
				+ "AND ksk.keyholder_id = kkk.keyholder_id ";
		if (isSpecial)
			query += "AND (kk.list_type NOT LIKE '%Weekday Occupancy%'"
					+ " AND kk.list_type NOT LIKE '%Weekday OutOfHours%'"
					+ " AND kk.list_type NOT LIKE '%Saturday Occupancy%'"
					+ " AND kk.list_type NOT LIKE '%Saturday OutOfHours%'"
					+ " AND kk.list_type NOT LIKE '%Sunday Occupancy%'"
					+ " AND kk.list_type NOT LIKE '%Sunday OutOfHours%'"
					+ " AND kk.list_type NOT LIKE '%Default%')";
		else
			query += "AND (kk.list_type LIKE '%Weekday Occupancy%'"
					+ " OR kk.list_type LIKE '%Weekday OutOfHours%'"
					+ " OR kk.list_type LIKE '%Saturday Occupancy%'"
					+ " OR kk.list_type LIKE '%Saturday OutOfHours%'"
					+ " OR kk.list_type LIKE '%Sunday Occupancy%'"
					+ " OR kk.list_type LIKE '%Sunday OutOfHours%'"
					+ " OR kk.list_type LIKE '%Default%')";
//		 System.out.println("query: "+query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				KeyholderList keyholderList = new KeyholderList();
				keyholderList.setKeyholderListId(result
						.getInt("keyholder_list_id"));
				keyholderList.setDisplayName(result.getString("display_name"));
				displayNames.add(keyholderList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		return displayNames;
	}

	@Override
	public List<Keyholder> getSpecialKeyholders(String branch_code, int deptId) {
		// System.out.println("branch code:"+branch_code);
		// System.out.println("dept id: "+deptId);
		Map<Integer, Keyholder> keyholders = new TreeMap<Integer, Keyholder>();
		List<Keyholder> branchKeyholders = new ArrayList<Keyholder>();
		String query = "SELECT * from `keyholders_sitekeyholder` where branch_code='"
				+ branch_code + "'";
		// System.out.println("query: "+query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Keyholder new_keyholder = new Keyholder(
						result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				new_keyholder.setKeyholderList(new ArrayList<KeyholderList>());
				new_keyholder.setKeyholderListIds(new ArrayList<Integer>());
				keyholders.put(result.getInt("keyholder_id"), new_keyholder);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		query = "SELECT * FROM `keyholders_sitekeyholder` as ksk "
				+ "inner join `keyholders_department_keyholderlist` as kdk "
				+ "inner join `keyholders_keyholderlist` as kk "
				+ "inner join `keyholders_keyholderlist_keyholders` as kkk "
				+ "WHERE ksk.branch_code = '" + branch_code + "' "
				+ "AND kdk.dep_id = " + deptId + " "
				+ "AND kk.keyholder_list_id = kdk.keyholder_list_id "
				+ "AND kkk.keyholder_list_id=kk.keyholder_list_id "
				+ "AND ksk.keyholder_id = kkk.keyholder_id "
				+ "AND (kk.list_type NOT LIKE '%Weekday Occupancy%'"
				+ " AND kk.list_type NOT LIKE '%Weekday OutOfHours%'"
				+ " AND kk.list_type NOT LIKE '%Saturday Occupancy%'"
				+ " AND kk.list_type NOT LIKE '%Saturday OutOfHours%'"
				+ " AND kk.list_type NOT LIKE '%Sunday Occupancy%'"
				+ " AND kk.list_type NOT LIKE '%Sunday OutOfHours%'"
				+ " AND kk.list_type NOT LIKE '%Default%')";
//		 System.out.println("query: "+query);

		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Keyholder new_keyholder = new Keyholder(
						result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				// new_keyholder.setListType(result.getString("list_type"));
				new_keyholder.setDisplayName(result.getString("display_name"));

				KeyholderList keyholderList = new KeyholderList(
						result.getInt("keyholder_list_id"),
						result.getString("list_name"),
						result.getString("list_type"),
						result.getTimestamp("start_date"),
						result.getTimestamp("end_date"),
						result.getString("occupancy_start"),
						result.getString("occupancy_end"),
						result.getString("comments"),
						result.getString("weekdays_active"));
				keyholderList.setDescription(result.getString("description"));
				keyholderList.setDisplayName(result.getString("display_name"));

				if (keyholders.get(result.getInt("keyholder_id")) != null) {
					Keyholder holder = keyholders.get(result
							.getInt("keyholder_id"));
					List<KeyholderList> list = holder.getKeyholderList();
					list.add(keyholderList);
					holder.setKeyholderList(list);

					List<Integer> listNames = holder.getKeyholderListIds();
					listNames.add(keyholderList.getKeyholderListId());
					holder.setKeyholderListIds(listNames);
					holder.setContact_type(result.getInt("contact_type"));
					ContactTypeDetails typeDetails=new ContactTypeDetails();
					typeDetails.setContactType(result.getInt("contact_type"));
					typeDetails.setContactString(KeyholderUtility.contactTypeText[result.getInt("contact_type")]);
					typeDetails.setIconName(KeyholderUtility.contactTypeIcon[result.getInt("contact_type")]);
					Map<Integer, ContactTypeDetails> contactMap=holder.getIdWithContactType();
					contactMap.put(keyholderList.getKeyholderListId(), typeDetails);
					holder.setIdWithContactType(contactMap);
//					for(Integer id: holder.getIdWithContactType().keySet()){
//						System.out.println("--id: "+id+" - contact type: "+holder.getIdWithContactType().get(id).getIconName());
//						}
					
					keyholders.put(result.getInt("keyholder_id"), holder);
				} else {
					List<KeyholderList> keyholdersList = new ArrayList<KeyholderList>();
					keyholdersList.add(keyholderList);
					new_keyholder.setKeyholderList(keyholdersList);

					List<Integer> listNames = new ArrayList<Integer>();
					listNames.add(keyholderList.getKeyholderListId());
					new_keyholder.setKeyholderListIds(listNames);
					
					new_keyholder.setContact_type(result.getInt("contact_type"));
					ContactTypeDetails typeDetails=new ContactTypeDetails();
					typeDetails.setContactType(result.getInt("contact_type"));
					typeDetails.setContactString(KeyholderUtility.contactTypeText[result.getInt("contact_type")]);
					typeDetails.setIconName(KeyholderUtility.contactTypeIcon[result.getInt("contact_type")]);
					Map<Integer, ContactTypeDetails> contactMap=new_keyholder.getIdWithContactType();
					contactMap.put(keyholderList.getKeyholderListId(), typeDetails);
					new_keyholder.setIdWithContactType(contactMap);
					
//					for(Integer id: new_keyholder.getIdWithContactType().keySet()){
//					System.out.println("--id: "+id+" - contact type: "+new_keyholder.getIdWithContactType().get(id).getIconName());
//					}
					
					keyholders.put(result.getInt("keyholder_id"), new_keyholder);

				}

			}
			for (Integer key : keyholders.keySet()) {
				branchKeyholders.add(keyholders.get(key));
//				for(Integer id: keyholders.get(key).getIdWithContactType().keySet()){
//					System.out.println("id: "+id+" - contact type: "+keyholders.get(key).getIdWithContactType().get(id).getIconName());
//					}
				// System.out.println("keyholder id: "
				// +key);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		return branchKeyholders;
	}

//	@Override
//	public KeyholderList getKeyholdersList(String branch_code, int listId) {
//		KeyholderList branchKeyholders = null;
//		String query = "SELECT * FROM keyholders_sitekeyholder ksk"
//				+ " inner join keyholders_keyholderlist_keyholders kkk on ksk.keyholder_id= kkk.keyholder_id"
//				+ " inner join keyholders_keyholderlist kk on kkk.keyholder_list_id=kk.keyholder_list_id"
//				+ " where ksk.branch_code='" + branch_code + "'"
//				+ " AND kk.keyholder_list_id LIKE '" + listId + "'"
//				+ " order by list_priority;";
//		// System.out.println("query: "+query);
//		Statement stmnt = null;
//		ResultSet result = null;
//		try {
//			dbBureauConnect();
//			stmnt = dbBureauConn.createStatement();
//			result = stmnt.executeQuery(query);
//			while (result.next()) {
//				branchKeyholders = new KeyholderList(
//						result.getInt("keyholder_list_id"),
//						result.getString("list_name"),
//						result.getString("list_type"),
//						result.getTimestamp("start_date"),
//						result.getTimestamp("end_date"),
//						result.getString("occupancy_start"),
//						result.getString("occupancy_end"),
//						result.getString("comments"),
//						result.getString("weekdays_active"));
//
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				result.close();
//				stmnt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			dbBureauConn = null;
//			stmnt = null;
//			result = null;
//		}
//		// System.out.println("list key holders size: "+branchKeyholders.size());
//		return branchKeyholders;
//	}

	@Override
	public List<Keyholder> getListOfKeyholders(String branch_code, int listId) {
		List<Keyholder> branchKeyholders = new ArrayList<Keyholder>();
		String query = "SELECT * FROM keyholders_sitekeyholder ksk"
				+ " inner join keyholders_keyholderlist_keyholders kkk on ksk.keyholder_id= kkk.keyholder_id"
				+ " inner join keyholders_keyholderlist kk on kkk.keyholder_list_id=kk.keyholder_list_id"
				+ " where ksk.branch_code='" + branch_code + "'"
				+ " AND kk.keyholder_list_id LIKE '" + listId + "'"
				+ " order by list_priority;";
		// System.out.println("query: "+query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Keyholder new_keyholder = new Keyholder(
						result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				new_keyholder.setListType(result.getString("list_type"));
				new_keyholder.setDisplayName(result.getString("display_name"));
				new_keyholder.setPriority(result.getInt("list_priority"));

				// new_keyholder.setKeyholderListId(result.getInt("keyholder_list_id"));
				branchKeyholders.add(new_keyholder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		// System.out.println("list key holders size: "+branchKeyholders.size());
		return branchKeyholders;
	}

	@Override
	public Keyholder getKeyholderById(String branch_code, int listId,
			int keyholderId) {
		// List<Keyholder> branchKeyholders = new ArrayList<Keyholder> ();
		Keyholder new_keyholder = null;
		String query = "SELECT * FROM keyholders_sitekeyholder ksk"
				+ " inner join keyholders_keyholderlist_keyholders kkk on ksk.keyholder_id= kkk.keyholder_id"
				+ " inner join keyholders_keyholderlist kk on kkk.keyholder_list_id=kk.keyholder_list_id"
				+ " where ksk.branch_code='" + branch_code + "'"
				+ " AND kk.keyholder_list_id LIKE '" + listId + "'"
				+ " AND ksk.keyholder_id=" + keyholderId
				+ " order by list_priority;";
		// System.out.println("query: "+query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				new_keyholder = new Keyholder(result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				new_keyholder.setListType(result.getString("list_type"));
				new_keyholder.setDisplayName(result.getString("display_name"));
				new_keyholder.setPriority(result.getInt("list_priority"));
				// new_keyholder.setKeyholderListId(result.getInt("keyholder_list_id"));
				// branchKeyholders.add(new_keyholder);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		// System.out.println("list key holders size: "+branchKeyholders.size());
		return new_keyholder;
	}

	@Override
	public Keyholder getKeyholderById(String branch_code, int keyholderId) {
		// List<Keyholder> branchKeyholders = new ArrayList<Keyholder> ();

		Map<Integer, Keyholder> keyholders = new HashMap<Integer, Keyholder>();
		Keyholder new_keyholder = null;
		String query = "SELECT * from `keyholders_sitekeyholder` where branch_code='"
				+ branch_code + "' AND keyholder_id=" + keyholderId;
		// System.out.println("query: "+query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				new_keyholder = new Keyholder(result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				new_keyholder.setKeyholderList(new ArrayList<KeyholderList>());
				new_keyholder.setKeyholderListIds(new ArrayList<Integer>());
				keyholders.put(result.getInt("keyholder_id"), new_keyholder);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		query = "SELECT * FROM keyholders_sitekeyholder ksk"
				+ " inner join keyholders_keyholderlist_keyholders kkk on ksk.keyholder_id= kkk.keyholder_id"
				+ " inner join keyholders_keyholderlist kk on kkk.keyholder_list_id=kk.keyholder_list_id"
				+ " where ksk.branch_code='" + branch_code + "'"
				+ " AND ksk.keyholder_id=" + keyholderId;

		// System.out.println("query: " + query);
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				new_keyholder = new Keyholder(result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"), branch_code, 0, false);
				new_keyholder.setListType(result.getString("list_type"));
				new_keyholder.setDisplayName(result.getString("display_name"));
				new_keyholder.setPriority(result.getInt("list_priority"));

				KeyholderList keyholderList = new KeyholderList(
						result.getInt("keyholder_list_id"),
						result.getString("list_name"),
						result.getString("list_type"),
						result.getTimestamp("start_date"),
						result.getTimestamp("end_date"),
						result.getString("occupancy_start"),
						result.getString("occupancy_end"),
						result.getString("comments"),
						result.getString("weekdays_active"));
				keyholderList.setDescription(result.getString("description"));
				keyholderList.setDisplayName(result.getString("display_name"));

				if (keyholders.get(result.getInt("keyholder_id")) != null) {
					Keyholder holder = keyholders.get(result
							.getInt("keyholder_id"));
					List<KeyholderList> list = holder.getKeyholderList();
					list.add(keyholderList);
					holder.setKeyholderList(list);

					List<Integer> listNames = holder.getKeyholderListIds();
					listNames.add(keyholderList.getKeyholderListId());
					holder.setKeyholderListIds(listNames);

					keyholders.put(result.getInt("keyholder_id"), holder);
				} else {
					List<KeyholderList> keyholdersList = new ArrayList<KeyholderList>();
					keyholdersList.add(keyholderList);
					new_keyholder.setKeyholderList(keyholdersList);

					List<Integer> listNames = new ArrayList<Integer>();
					listNames.add(keyholderList.getKeyholderListId());
					new_keyholder.setKeyholderListIds(listNames);
					keyholders
							.put(result.getInt("keyholder_id"), new_keyholder);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		for (int key : keyholders.keySet()) {
			new_keyholder = keyholders.get(key);
		}

		return new_keyholder;
	}

	@Override
	public void updateKeyholdersList(List<Keyholder> listKeyholders,
			int keyholderListId) {
		if(listKeyholders==null)
		{
			return;
		}
		Statement stmnt = null;
		String query = "update keyholders_keyholderlist_keyholders"
				+ " set list_priority =" + " case";
		for (Keyholder keyholder : listKeyholders) {
			query += " when (keyholder_id =" + keyholder.getKeyholderId()
					+ " and keyholder_list_id=" + keyholderListId + ")  then "
					+ keyholder.getPriority();
		}
		query += " else list_priority";
		query += " end";

		// System.out.println(query);
		/*
		 * String query=
		 * "INSERT INTO `keyholders_keyholderlist_keyholders`(keyholder_list_id,keyholder_id,list_priority,active,contact_type,redial)"
		 * + "VALUES ("+ keyholder_list_id +","+ keyholder_id + "," +
		 * listPriority + "," + active + "," + contact_type + "," + (redial?1:0)
		 * + ");";
		 */
		try {
			if (!listKeyholders.isEmpty()) {
				dbMySQLConnect();
				stmnt = dbMySQLConn.createStatement();
				stmnt.executeUpdate(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
		}
	}

	@Override
	public List<KeyholderList> getAllNormalLists(int deptId) {
		// List<Keyholder> branchKeyholders = new ArrayList<Keyholder> ();
		List<KeyholderList> keyholderLists = new ArrayList<KeyholderList>();
		String query = "SELECT distinct kk.display_name,kk.list_type,kk.keyholder_list_id"
				+ " FROM keyholders_keyholderlist as kk join keyholders_department_keyholderlist as kdk on kk.keyholder_list_id=kdk.keyholder_list_id"
				+ " where (kk.list_type LIKE '%Weekday Occupancy%'"
				+ " OR kk.list_type LIKE '%Weekday OutOfHours%'"
				+ " OR kk.list_type LIKE '%Saturday Occupancy%'"
				+ " OR kk.list_type LIKE '%Saturday OutOfHours%'"
				+ " OR kk.list_type LIKE '%Sunday Occupancy%'"
				+ " OR kk.list_type LIKE '%Sunday OutOfHours%'"
				+ " OR kk.list_type LIKE '%Default%') AND kdk.dep_id="
				+ deptId;
		// System.out.println("query: " + query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				KeyholderList keyholderList = new KeyholderList();
				keyholderList.setListType(result.getString("list_type"));
				// keyholderList.setDescription(result.getString("description"));
				keyholderList.setDisplayName(result.getString("display_name"));
				keyholderList.setKeyholderListId(result
						.getInt("keyholder_list_id"));

				keyholderLists.add(keyholderList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		// System.out.println("list key holders size: "+branchKeyholders.size());
		return keyholderLists;
	}

	@Override
	public List<KeyholderList> getAllHolidayLists(int deptId) {
		// List<Keyholder> branchKeyholders = new ArrayList<Keyholder> ();
		List<KeyholderList> keyholderLists = new ArrayList<KeyholderList>();
		String query = "SELECT distinct kk.display_name,kk.list_type,kk.list_name,kk.keyholder_list_id"
				+ " FROM keyholders_keyholderlist as kk join keyholders_department_keyholderlist as kdk on kk.keyholder_list_id=kdk.keyholder_list_id"
				+ " where (kk.list_type NOT LIKE '%Weekday Occupancy%'"
				+ " AND kk.list_type NOT LIKE '%Weekday OutOfHours%'"
				+ " AND kk.list_type NOT LIKE '%Saturday Occupancy%'"
				+ " AND kk.list_type NOT LIKE '%Saturday OutOfHours%'"
				+ " AND kk.list_type NOT LIKE '%Sunday Occupancy%'"
				+ " AND kk.list_type NOT LIKE '%Sunday OutOfHours%'"
				+ " AND kk.list_type NOT LIKE '%Default%') AND kdk.dep_id="
				+ deptId;
		// System.out.println("get all holiday query: " + query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				KeyholderList keyholderList = new KeyholderList();
				keyholderList.setListType(result.getString("list_type"));
				keyholderList.setListName(result.getString("list_name"));
				keyholderList.setDisplayName(result.getString("display_name"));
				keyholderList.setKeyholderListId(result
						.getInt("keyholder_list_id"));

				keyholderLists.add(keyholderList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		// System.out.println("list key holders size: "+branchKeyholders.size());
		return keyholderLists;
	}

	@Override
	public List<Keyholder> getContactTypes(int keyholderId) {
		// Map<Integer, Keyholder> keyholders = new HashMap<Integer,
		// Keyholder>();
		// List<Keyholder> branchKeyholders = new ArrayList<Keyholder> ();
		List<Keyholder> keyholderLists = new ArrayList<Keyholder>();
		String query = "SELECT * FROM keyholders_keyholderlist_keyholders as kkk join keyholders_sitekeyholder as ksk"
				+ " on  kkk.keyholder_id=ksk.keyholder_id join keyholders_keyholderlist as kk on kkk.keyholder_list_id=kk.keyholder_list_id"
				+ " where ksk.keyholder_id=" + keyholderId;
		// System.out.println("query: " + query);
		Statement stmnt = null;
		ResultSet result = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				Keyholder new_keyholder = new Keyholder(
						result.getInt("keyholder_id"),
						result.getString("contactName"),
						result.getString("position"),
						result.getBoolean("active"), result.getString("phone"),
						result.getString("mobile"), result.getString("fax"),
						result.getString("email"),
						result.getString("branch_code"),
						result.getInt("contact_type"),
						result.getBoolean("redial"));
				new_keyholder.setDisplayName(result.getString("display_name"));
				new_keyholder.setKeyholderListId(result
						.getInt("keyholder_list_id"));
				keyholderLists.add(new_keyholder);
				// KeyholderList keyholderList = new KeyholderList(
				// result.getInt("keyholder_list_id"),
				// result.getString("list_name"),
				// result.getString("list_type"),
				// result.getTimestamp("start_date"),
				// result.getTimestamp("end_date"),
				// result.getString("occupancy_start"),
				// result.getString("occupancy_end"),
				// result.getString("comments"),
				// result.getString("weekdays_active"));
				// keyholderList.setDisplayName(result.getString("display_name"));
				//
				// if (keyholders.get(result.getInt("keyholder_id")) != null) {
				// Keyholder holder = keyholders.get(result
				// .getInt("keyholder_id"));
				// List<KeyholderList> list = holder.getKeyholderList();
				// list.add(keyholderList);
				// holder.setKeyholderList(list);
				//
				// List<String> listNames = holder.getKeyholderListNames();
				// listNames.add(keyholderList.getDisplayName());
				// holder.setKeyholderListNames(listNames);
				//
				// keyholders.put(result.getInt("keyholder_id"), holder);
				// } else {
				// List<KeyholderList> keyholdersList = new
				// ArrayList<KeyholderList>();
				// keyholdersList.add(keyholderList);
				// new_keyholder.setKeyholderList(keyholdersList);
				//
				// List<String> listNames = new ArrayList<String>();
				// listNames.add(keyholderList.getDisplayName());
				// new_keyholder.setKeyholderListNames(listNames);
				// keyholders
				// .put(result.getInt("keyholder_id"), new_keyholder);
				//
				// }

			}
			// for (Integer key : keyholders.keySet()) {
			// keyholderLists.add(keyholders.get(key));
			// // System.out.println("keyholder id: "
			// // +key);
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}

		// System.out.println("list key holders size: "+branchKeyholders.size());
		return keyholderLists;

	}

	@Override
	public void saveKeyholderList_Keyholder(Keyholder keyholder,
			KeyholderListEnablingDetails details) {

		String query = "UPDATE keyholders_keyholderlist_keyholders SET contact_type=? "
				+ " where keyholder_id=?" + " AND keyholder_list_id=?";
		// System.out.println("update keyholder list query: " + query);
		PreparedStatement stmnt = null;
		PreparedStatement stmnt2 = null;

		Statement statement = null;
		ResultSet selectResults = null;
		try {
			// System.out.println("contact type: " + details.getContact_type());
			dbMySQLConnect();
			stmnt = dbMySQLConn.prepareStatement(query);
			stmnt.setInt(1, details.getContact_type());
			stmnt.setInt(2, keyholder.getKeyholderId());
			stmnt.setInt(3, details.getKeyholderListId());
			int row = stmnt.executeUpdate();

			if (row <= 0) {
				query = "SELECT list_priority FROM keyholders_keyholderlist_keyholders WHERE keyholder_list_id="
						+ details.getKeyholderListId()
						+ " ORDER BY list_priority";
				// System.out.println("priority query: " + query);
				statement = dbMySQLConn.createStatement();
				selectResults = statement.executeQuery(query);
				int priority = 1;
				if (selectResults.next()) {
					selectResults.last();
					priority = selectResults.getInt("list_priority");
				}

				query = "INSERT INTO keyholders_keyholderlist_keyholders (keyholder_list_id,keyholder_id,list_priority,active,contact_type,redial)"
						+ "VALUES(?,?,?,?,?,?)";
				// System.out.println("insert query:"+query);
				// System.out.println("keyholder list id: "+details.getKeyholderListId());
				// System.out.println("keyholder id: "+keyholder.getKeyholderId());
				stmnt2 = dbMySQLConn.prepareStatement(query);
				stmnt2.setInt(1, details.getKeyholderListId());
				stmnt2.setInt(2, keyholder.getKeyholderId());
				stmnt2.setInt(3, (priority + 1));
				stmnt2.setBoolean(4, true);
				stmnt2.setInt(5, details.getContact_type());
				stmnt2.setBoolean(6, false);
				stmnt2.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmnt2 != null)
					stmnt2.close();
				stmnt.close();
				if (statement != null)
					statement.close();
				if (selectResults != null)
					selectResults.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			stmnt2 = null;
			statement = null;
			selectResults = null;
		}
		// System.out.println("list key holders size: "+branchKeyholders.size());
	}

	@Override
	public void deleteKeyholderList_Keyholder(Keyholder keyholder,
			KeyholderListEnablingDetails details) {
		String query = "DELETE FROM keyholders_keyholderlist_keyholders"
				+ " where keyholder_id=?" + " AND keyholder_list_id=?";
		// System.out.println("query: "+query);
		PreparedStatement stmnt = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.prepareStatement(query);

			stmnt.setInt(1, keyholder.getKeyholderId());
			stmnt.setInt(2, details.getKeyholderListId());
			int row = stmnt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;

		}
	}

	@Override
	public void updateSiteKeyholder(Keyholder keyholder) {

		String query = "UPDATE keyholders_sitekeyholder SET contactName=?, phone=?, mobile=?, email=?, position=? "
				+ " where keyholder_id=?" + " AND branch_code=?";
		// System.out.println("query: " + query);
		PreparedStatement stmnt = null;
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.prepareStatement(query);
			stmnt.setString(1, keyholder.getContactName());
			stmnt.setString(2, keyholder.getPhone());
			stmnt.setString(3, keyholder.getMobile());
			stmnt.setString(4, keyholder.getEmail());
			stmnt.setString(5, keyholder.getPosition());
			stmnt.setInt(6, keyholder.getKeyholderId());
			stmnt.setString(7, keyholder.getBranchCode());
			int row = stmnt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;

		}
	}

	public int addBranchKeyholder(String contactName, String position,
			boolean active, String phone, String mobile, String fax,
			String email, String branchCode) {
		int keyholderId = -1;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		String query = "INSERT INTO `keyholders_sitekeyholder` (contactName,position,active,phone,mobile,fax,email,branch_code) "
				+ "VALUES (?,?,?,?,?,?,?,?);";
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.prepareStatement(query,
					PreparedStatement.RETURN_GENERATED_KEYS);
			if (contactName != null) {
				stmnt.setString(1, contactName);
			} else {
				stmnt.setNull(1, java.sql.Types.VARCHAR);
			}
			if (position != null) {
				stmnt.setString(2, position);
			} else {
				stmnt.setNull(2, java.sql.Types.VARCHAR);
			}
			stmnt.setByte(3, (byte) (active ? 1 : 0));
			if (phone != null) {
				stmnt.setString(4, phone);
			} else {
				stmnt.setNull(4, java.sql.Types.VARCHAR);
			}
			if (mobile != null) {
				stmnt.setString(5, mobile);
			} else {
				stmnt.setNull(5, java.sql.Types.VARCHAR);
			}
			if (fax != null) {
				stmnt.setString(6, fax);
			} else {
				stmnt.setNull(6, java.sql.Types.VARCHAR);
			}
			if (email != null) {
				stmnt.setString(7, email);
			} else {
				stmnt.setNull(7, java.sql.Types.VARCHAR);
			}
			if (branchCode != null) {
				stmnt.setString(8, branchCode);
			} else {
				stmnt.setNull(8, java.sql.Types.VARCHAR);
			}
			stmnt.executeUpdate();
			rs = stmnt.getGeneratedKeys();
			while (rs.next()) {
				keyholderId = rs.getInt(1);
			}
			// System.out.println("saved site keyholder to db");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
		}
		// query = "SELECT LAST_INSERT_ID();";
		// // System.out.println("get all holiday query: " + query);
		// Statement stment = null;
		// ResultSet result = null;

		// try {
		// dbBureauConnect();
		// stment = dbBureauConn.createStatement();
		// result = stment.executeQuery(query);
		// while (result.next()) {
		// keyholderId=result.getInt("LAST_INSERT_ID()");
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// result.close();
		// stment.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// dbBureauConn = null;
		// stment = null;
		// result = null;
		// }
		return keyholderId;
	}

	@Override
	public void deleteBranchKeyholder(int keyholderId) {
		Statement stmnt = null;
		String query = "DELETE FROM `keyholders_sitekeyholder` WHERE keyholder_id="
				+ keyholderId + ";";
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			stmnt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
		}
	}

	public List<KeyholderList> retrieveDepartmentKeyholderListNormal(int dep_id) {
		List<KeyholderList> keyholderList = new ArrayList<KeyholderList>();
		Statement stmnt = null;
		ResultSet result = null;
		String query = "SELECT * FROM `keyholders_keyholderlist` INNER JOIN `keyholders_department_keyholderlist`"
				+ "ON `keyholders_keyholderlist`.keyholder_list_id=`keyholders_department_keyholderlist`.keyholder_list_id WHERE dep_id="
				+ dep_id + " AND (list_type LIKE '%Weekday Occupancy%'"
				+ " OR list_type LIKE '%Weekday OutOfHours%'"
				+ " OR list_type LIKE '%Saturday Occupancy%'"
				+ " OR list_type LIKE '%Saturday OutOfHours%'"
				+ " OR list_type LIKE '%Sunday Occupancy%'"
				+ " OR list_type LIKE '%Sunday OutOfHours%'"
				+ " OR list_type LIKE '%Default%')";
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				KeyholderList new_keyholderList = new KeyholderList(
						result.getInt("keyholder_list_id"),
						result.getString("list_name"),
						result.getString("list_type"),
						result.getTimestamp("start_date"),
						result.getTimestamp("end_date"),
						result.getString("occupancy_start"),
						result.getString("occupancy_end"),
						result.getString("comments"),
						result.getString("weekdays_active"));
				new_keyholderList.setDisplayName(result
						.getString("display_name"));
				new_keyholderList.setDescription(result
						.getString("description"));
				keyholderList.add(new_keyholderList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		return keyholderList;
	}

	public List<KeyholderList> retrieveDepartmentKeyholderListHoliday(int dep_id) {
		List<KeyholderList> keyholderList = new ArrayList<KeyholderList>();
		Statement stmnt = null;
		ResultSet result = null;
		String query = "SELECT * FROM `keyholders_keyholderlist` INNER JOIN `keyholders_department_keyholderlist`"
				+ "ON `keyholders_keyholderlist`.keyholder_list_id=`keyholders_department_keyholderlist`.keyholder_list_id WHERE dep_id="
				+ dep_id + " AND (list_type NOT LIKE '%Weekday Occupancy%'"
				+ " AND list_type NOT LIKE '%Weekday OutOfHours%'"
				+ " AND list_type NOT LIKE '%Saturday Occupancy%'"
				+ " AND list_type NOT LIKE '%Saturday OutOfHours%'"
				+ " AND list_type NOT LIKE '%Sunday Occupancy%'"
				+ " AND list_type NOT LIKE '%Sunday OutOfHours%'"
				+ " AND list_type NOT LIKE '%Default%')";
		try {
//			System.out.println("query: " + query);
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				KeyholderList new_keyholderList = new KeyholderList(
						result.getInt("keyholder_list_id"),
						result.getString("list_name"),
						result.getString("list_type"),
						result.getTimestamp("start_date"),
						result.getTimestamp("end_date"),
						result.getString("occupancy_start"),
						result.getString("occupancy_end"),
						result.getString("comments"),
						result.getString("weekdays_active"));
				new_keyholderList.setDisplayName(result
						.getString("display_name"));
				new_keyholderList.setDescription(result
						.getString("description"));
				keyholderList.add(new_keyholderList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		return keyholderList;
	}

	// @Override
	// public void saveNewSiteKeyholder(Keyholder keyholder) {
	//
	// String query =
	// "INSERT INTO keyholders_sitekeyholder (contactName, phone, mobile, email,branch_code) "
	// + " VALUES ("")";
	// // System.out.println("query: " + query);
	// PreparedStatement stmnt = null;
	// try {
	// dbBureauConnect();
	// stmnt = dbBureauConn.prepareStatement(query);
	// stmnt.setString(1, keyholder.getContactName());
	// stmnt.setString(2, keyholder.getPhone());
	// stmnt.setString(3, keyholder.getMobile());
	// stmnt.setString(4, keyholder.getEmail());
	// stmnt.setInt(5, keyholder.getKeyholderId());
	// stmnt.setString(6, keyholder.getBranchCode());
	// int row = stmnt.executeUpdate();
	//
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// stmnt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// dbBureauConn = null;
	// stmnt = null;
	//
	// }
	// }
	public void addDeptKeyholderList(String displayName, String description,
			String listName, String listType, Date startDate, Date endDate,
			String occupancyStart, String occupancyEnd, String comments,
			int dep_id, String weekdaysActive) {
		dbMySQLConnect();
		int keyholder_list_id = 0;
		String query = "INSERT INTO `keyholders_keyholderlist` (list_name,list_type,start_date,end_date"
				+ ",occupancy_start,occupancy_end,comments,weekdays_active,description,display_name) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?);";
		PreparedStatement stmnt = null;
		Statement stmnt2 = null;
		ResultSet result = null;
		try {
			stmnt = dbMySQLConn.prepareStatement(query,
					PreparedStatement.RETURN_GENERATED_KEYS);
			if (listName == null) {
				stmnt.setNull(1, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(1, listName);
			}
			stmnt.setString(2, listType);
			if (startDate == null) {
				stmnt.setNull(3, java.sql.Types.DATE);
			} else {
				int hour = Integer.parseInt(occupancyStart.split(":")[0]);
				int min = Integer.parseInt(occupancyStart.split(":")[1]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, min);
				stmnt.setTimestamp(3,
						new java.sql.Timestamp(cal.getTimeInMillis()));
			}
			if (endDate == null) {
				stmnt.setNull(4, java.sql.Types.DATE);
			} else {
				int hour = Integer.parseInt(occupancyEnd.split(":")[0]);
				int min = Integer.parseInt(occupancyEnd.split(":")[1]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, min);
				stmnt.setTimestamp(4,
						new java.sql.Timestamp(cal.getTimeInMillis()));
			}
			stmnt.setString(5, occupancyStart);
			stmnt.setString(6, occupancyEnd);
			if (comments == null) {
				stmnt.setNull(7, java.sql.Types.VARCHAR);
			} else if (comments.equals("null")) {
				stmnt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(7, comments);
			}
			if (weekdaysActive == null) {
				stmnt.setNull(8, java.sql.Types.VARCHAR);
			} else if (weekdaysActive.equals("null")) {
				stmnt.setNull(8, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(8, weekdaysActive);
			}
			stmnt.setString(9, description);
			stmnt.setString(10, displayName);
			int row = stmnt.executeUpdate();
			// System.out.println("rows inserted: "+row);
			result = stmnt.getGeneratedKeys();
			while (result.next()) {
				keyholder_list_id = result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmnt = null;
			result = null;
			stmnt2 = null;
		}
		// query="SELECT keyholder_list_id FROM `keyholders_keyholderlist` WHERE list_name='"
		// +listName + "';";
		// try{
		// stmnt2=dbBureauConn.createStatement();
		// result=stmnt2.executeQuery(query);
		// while (result.next()){
		// keyholder_list_id=result.getInt(1);
		// }
		// }catch (SQLException e) {
		// e.printStackTrace();
		// }finally{
		// try {
		// result.close();
		// stmnt2.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// result = null;
		// stmnt2=null;
		// }
		// System.out.println("keyholder_list_id: "+keyholder_list_id);
		query = "INSERT INTO `keyholders_department_keyholderlist` (keyholder_list_id,dep_id) VALUES "
				+ "(" + keyholder_list_id + ",'" + dep_id + "');";
		try {
			if (keyholder_list_id != 0) {
				stmnt2 = dbMySQLConn.createStatement();
				stmnt2.executeUpdate(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt2.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt2 = null;
		}
	}
	//used in testing
	public KeyholderList getKeyholderListById(int keyholderListId) {
		KeyholderList new_keyholderList=null;
		Statement stmnt = null;
		ResultSet result = null;
		String query = "SELECT * FROM `keyholders_keyholderlist`"
				+ " where keyholder_list_id="+keyholderListId;
		try {
//			System.out.println("query: " + query);
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			result = stmnt.executeQuery(query);
			while (result.next()) {
				new_keyholderList = new KeyholderList(
						result.getInt("keyholder_list_id"),
						result.getString("list_name"),
						result.getString("list_type"),
						result.getTimestamp("start_date"),
						result.getTimestamp("end_date"),
						result.getString("occupancy_start"),
						result.getString("occupancy_end"),
						result.getString("comments"),
						result.getString("weekdays_active"));
				new_keyholderList.setDisplayName(result
						.getString("display_name"));
				new_keyholderList.setDescription(result
						.getString("description"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
			result = null;
		}
		return new_keyholderList;
	}
	public void delDeptKeyholderList(int keyholder_list_id) {
		Statement stmnt = null;
		String query = "DELETE FROM `keyholders_keyholderlist` WHERE keyholder_list_id="
				+ keyholder_list_id + ";";
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.createStatement();
			stmnt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
		}
	}

	public void modifyKeyholderList(String listName, String listType,
			Date startDate, Date endDate, String occupancyStart,
			String occupancyEnd, String comments, int keyholder_list_id,
			String weekdaysActive, String description, String displayName) {
		PreparedStatement stmnt = null;
		String query = "UPDATE `keyholders_keyholderlist`"
				+ "SET list_name = ?,list_type = ?,start_date = ?,end_date = ?,occupancy_start = ?,occupancy_end = ?,"
				+ "comments = ?,weekdays_active = ?,description=?,display_name=? WHERE keyholder_list_id=?;";
		try {
			dbMySQLConnect();
			stmnt = dbMySQLConn.prepareStatement(query);
			if (listName == null) {
				stmnt.setNull(1, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(1, listName);
			}
			stmnt.setString(2, listType);
			if (startDate == null) {
				stmnt.setNull(3, java.sql.Types.DATE);
			} else {
				int hour = Integer.parseInt(occupancyStart.split(":")[0]);
				int min = Integer.parseInt(occupancyStart.split(":")[1]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, min);
				stmnt.setTimestamp(3,
						new java.sql.Timestamp(cal.getTimeInMillis()));
				// System.out.println(new
				// SimpleDateFormat("dd/MM/yyyy HH:mm").format(cal.getTime()));
			}
			if (endDate == null) {
				stmnt.setNull(4, java.sql.Types.DATE);
			} else {
				int hour = Integer.parseInt(occupancyEnd.split(":")[0]);
				int min = Integer.parseInt(occupancyEnd.split(":")[1]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, min);
				stmnt.setTimestamp(4,
						new java.sql.Timestamp(cal.getTimeInMillis()));
				// System.out.println(new
				// SimpleDateFormat("dd/MM/yyyy HH:mm").format(cal.getTime()));

			}
			stmnt.setString(5, occupancyStart);
			stmnt.setString(6, occupancyEnd);
			if (comments == null) {
				stmnt.setNull(7, java.sql.Types.VARCHAR);
			} else if (comments.equals("null")) {
				stmnt.setNull(7, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(7, comments);
			}
			if (weekdaysActive == null) {
				stmnt.setNull(8, java.sql.Types.VARCHAR);
			} else if (weekdaysActive.equals("null")) {
				stmnt.setNull(8, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(8, weekdaysActive);
			}
			if (description == null) {
				stmnt.setNull(9, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(9, description);
			}
			if (displayName == null) {
				stmnt.setNull(10, java.sql.Types.VARCHAR);
			} else {
				stmnt.setString(10, displayName);
			}
			stmnt.setInt(11, keyholder_list_id);
			stmnt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbMySQLConn = null;
			stmnt = null;
		}
	}

}

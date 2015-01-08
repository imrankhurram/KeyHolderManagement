package com.nextcontrols.dao;

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

public interface IAlarmingIsolationDAO {
	// public List<Isolation> getAllDeptIsolations(int depId);
	public List<Isolation> getGlobalIsolations(int depId, String siteCode,
			String isolationType);

	// added new field of name_more by Nayyab 09 sep 2011
	public void addDeptIsolation(String siteCode, String isolationType,
			String dayOfWeek, Date startTime, Date endTime, boolean enabled,
			int userId, Date createdOn, ArrayList<Integer> alarmTrapId,
			ArrayList<IsolationAssetSubfixtures> alarmAssetSubfixtures,
			String startHour, String endHour, int depId, String name,
			String more_name);

	public void modDeptIsolation(Isolation modIsolation, String startHour,
			String endHour, ArrayList<Integer> alarmTrapId,
			ArrayList<IsolationAssetSubfixtures> alarmAssetSubfixtures);

	public void changeIsolationActivity(int isolationId, boolean enabled);

	// public ArrayList<Integer> getIsolationAlarmTraps(int isolationId);
	// public ArrayList<Integer> getIsolationAlarmAssets(int isolationId);
	public List<AlarmAssetIsolation> getAlarmAssetsIsolationAdd(int departmentId);

	public AlarmAssetIsolation getAlarmAssetsIsolationByAlarmAssetId(
			int alarmAssetId);

	public List<Integer> getActiveIsolatedAssetIds(List<Asset> assetList);

	public List<Integer> getIsolatedAssetIds(List<Asset> assetList);

	public List<AssetSearchStringIsolation> getSubfixtureIsolation(
			int alarmAssetId);

	// public Isolation getSpecificIsolation(int isolationId);
	public List<AlarmTrap> getSpecificIsolationAlmTraps(int isolationId,
			int depId);

	public List<AlarmAssetIsolation> getSpecificIsolationAlmAssets(
			int isolationId, int depId);

	public List<AlarmTrap> getIsolatedAlmTraps(int isolationId, int depId);

	public List<IsolatedAsset> getIsolatedAlmAssets(int isolationId, int depId);

	public void delIsolation(int isolationId, int depId);

	public List<Isolation> getActiveIsolations(String siteCode, String type,
			int deptId);

	public List<Isolation> getInactiveIsolations(String siteCode, String type,
			int deptId);

	public List<Isolation> getDisabledIsolations(String siteCode, String type,
			int deptId);

	public List<Isolation> getExpiredIsolations(String siteCode, String type,
			int deptId);

	// public List<AlarmTrap> getIsolationAlmTrapsAdd(int depId);
	public List<Isolation> getIsolationsByAlarmAssetId(int id);
}

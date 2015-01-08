package com.nextcontrols.dao;

import java.io.Serializable;
import java.util.List;

import com.nextcontrols.domain.Asset;


public interface IAssetGroupDAO extends Serializable{
	public List<Asset> listAssets(int pAssetGroupID);
//	public List<Integer> listOfFlashingAlarmAssets(String pSiteCode, String pField);
}

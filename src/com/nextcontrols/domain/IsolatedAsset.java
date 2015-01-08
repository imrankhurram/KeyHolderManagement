package com.nextcontrols.domain;

import java.io.Serializable;

public class IsolatedAsset  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fixtureName;
	private String fixtureType;
	private String subfixture;
	
	public IsolatedAsset(){}
	
	public IsolatedAsset(String fixtureName,String fixtureType,String subfixture){
		this.fixtureName=fixtureName;
		this.fixtureType=fixtureType;
		this.subfixture=subfixture;
	}
	
	public void setFixtureName(String fixtureName) {
		this.fixtureName = fixtureName;
	}
	public String getFixtureName() {
		return fixtureName;
	}
	public void setSubfixture(String subfixture) {
		this.subfixture = subfixture;
	}
	public String getSubfixture() {
		return subfixture;
	}

	public void setFixtureType(String fixtureType) {
		this.fixtureType = fixtureType;
	}

	public String getFixtureType() {
		return fixtureType;
	}
}

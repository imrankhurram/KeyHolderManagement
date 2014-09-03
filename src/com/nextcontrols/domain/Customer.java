package com.nextcontrols.domain;

import java.io.Serializable;

public class Customer implements Serializable{
    
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int id;
    private int version;
    private String name;
    private String businesstype;
    
   public Customer (){}
    
    public Customer(int id,int version, String name, String businesstype){
    	this.id=id;
    	this.version=version;
    	this.name=name;
    	this.businesstype=businesstype;;
    	}
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
    
    
}


package com.nextcontrols.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;

public class Site implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /**************** Persistent Properties ****************/
    
    private String branchCode ;
    private String name ;
    private String phone ;
    private String email ;
//	private MailAddress address; 
//    private SiteContract sitecontract;
    private Customer customer;
    private TimeZone timeZone;
    private Set <Department> departments ;
//    private Set <AuditComments> auditcomments ;
    private Set <SiteView> brancheviews ;
    private int logsDatabase;
  /*********** Constructors ********************/
    public Site(){}
//    public Site(String pbranchCode, String pname, String pphone, String pemail, MailAddress paddress, SiteContract psitecontract,
//    			  Customer pcustomer, TimeZone ptimezone, LinkedHashSet <Department> pdepartments, LinkedHashSet <AuditComments> pauditcomments, 
//    			  LinkedHashSet <SiteView> pbranchviews,int pLogsDatabase) {
//    	this.branchCode = pbranchCode;
//        this.name = pname;
//        this.phone = pphone;
//        this.email = pemail;
//        this.address = paddress;
//        this.sitecontract = psitecontract;
//        this.customer = pcustomer;
//    	this.timeZone = ptimezone;//TimeZone.getTimeZone("Etc.UTC");
//    	this.departments = pdepartments;
//        this.auditcomments = pauditcomments;
//        this.brancheviews = pbranchviews;
//        this.logsDatabase = pLogsDatabase;
//     
//    }
    
    public Site(String pbranchCode) {
    	this.branchCode = pbranchCode;
  
    }

	public Site(String branchCode, String name, String phone, String email,
			Customer customer, TimeZone timeZone, Set<Department> departments,
			Set<SiteView> brancheviews, int logsDatabase) {
		super();
		this.branchCode = branchCode;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.customer = customer;
		this.timeZone = timeZone;
		this.departments = departments;
		this.brancheviews = brancheviews;
		this.logsDatabase = logsDatabase;
	}

	/************* Accessor methods ********************/
    
    public String getBranchCode() {return branchCode;}
    public void setBranchCode(String branchCode) {this.branchCode = branchCode.toUpperCase();}

 	public String getName() {return name;}
    public void setName(String name) {this.name = name; }

//    public MailAddress getAddress() {return address;}
//    public void setAddress(MailAddress address) {this.address = address; }

    public Customer getCustomer() {return customer;}
    void setCustomer(Customer customer) {this.customer = customer; }
          
    public Set<Department> getDepartments() {return departments;}
	public void setDepartments(Set<Department> departments) {this.departments = departments;}
	
//	public Set<AuditComments> getAuditcomments() {return auditcomments;}
//	public void setAuditcomments(Set<AuditComments> auditcomments) {this.auditcomments = auditcomments;}

	public TimeZone getTimeZone() { return timeZone; }
    public void setTimeZone(TimeZone timeZone) { this.timeZone = timeZone; }
    
    public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public String getPhone() {return phone;}
	public void setPhone(String phone) {this.phone = phone;}
	
	public Set<SiteView> getBrancheviews() {return brancheviews;}
	public void setBrancheviews(Set<SiteView> brancheviews) {this.brancheviews = brancheviews;}

//	public SiteContract getSitecontract() {return sitecontract;}
//	public void setSitecontract(SiteContract sitecontract) {this.sitecontract = sitecontract;}

	public int getLogsDatabase() {return logsDatabase;}
	public void setLogsDatabase(int logsDatabase) {this.logsDatabase = logsDatabase;}
	/**************** Builder Methods ***********************/
    public Set <Department> listDepartments() {
        return Collections.unmodifiableSet(departments);
    }
    
//    public Set <AuditComments> listAuditComments() {
//        return Collections.unmodifiableSet(auditcomments);
//    }  
        
    public boolean addDepartment(Department department) {
        if (department.getBranch() != null) {
            throw new IllegalArgumentException(
                    "Department already bound to a branch");
        }
        department.setBranch(this);
        return departments.add(department);
    }

    public boolean removeDepartment(Department department) {
        if (department.getBranch() != this) {
            throw new IllegalArgumentException(
                    "Department is not bound to this branch");
        }
        department.setBranch(null);
        return departments.remove(department);
    }
//    public boolean addAuditComments(AuditComments auditcomment) {
//        if (auditcomment.getBranch() != null) {
//            throw new IllegalArgumentException(
//                    "AuditComment already bound to a branch");
//        }
//        auditcomment.setBranch(this);
//        return auditcomments.add(auditcomment);
//    }
//
//    public boolean removeAuditComment(AuditComments auditcomment) {
//        if (auditcomment.getBranch() != this) {
//            throw new IllegalArgumentException(
//                    "AuditComment is not bound to this branch");
//        }
//        auditcomment.setBranch(null);
//        return auditcomments.remove(auditcomment);
//    }
    
    /**************** Common Methods ***********************/
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        Site t = (Site) o;
        return t.getBranchCode().equals(getBranchCode());
    }
        
    public int hashCode() {
        return getBranchCode().hashCode();
    }
    
    public String toString() {
        return getBranchCode() + " - " + getName();
    }
    
    /************** Business Methods *******************/
    
}

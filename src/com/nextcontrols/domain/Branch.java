package com.nextcontrols.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Branch implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String branchCode;
	private String name;
	private int customer_id;
	private String address1;
	private String address2;
	private String city;
	private String zip;
	private String country;
	private String timezone;
	private String fax;
	private String phone;
	private String email;
	private String siteContact;
	private Date orderDate;
	private String agreementNumber;
	private int agreementTerm;
	private Date commencementDate;
	private String formattedCommencementDate;
	private SimpleDateFormat format ;
	private Date installationDate;
	private int repeatalarminterval;
	private String nextEngineer;
	private String refrigerationContractor;
	private String hvacContractor;
	private String maintenanceManager;
	private String siteGroup;
	private byte transferedToNewBureau;
	private int alarmPriority;
	private String priority;
	private boolean outOfContract;
	private String customerName;
	private boolean selected;
	private String refrigeratedSystem;


	public Branch(String branchCode,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
			String fax,String phone,String email,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup,
			byte transferedToNewBureau,int alarmPriority,boolean outOfContract, String refSystem){
		format = new SimpleDateFormat("dd-MM-yyyy");
		this.branchCode=branchCode;
		this.name=name;
		this.customer_id=customer_id;
		this.address1=address1;
		this.address2=address2;
		this.city=city;
		this.zip=zip;
		this.country=country;
		this.timezone=timezone;
		this.fax=fax;
		this.phone=phone;
		this.email=email;
		this.siteContact=siteContact;
		this.orderDate=orderDate;
		this.agreementNumber=agreementNumber;
		this.agreementTerm=agreementTerm;
		this.commencementDate=commencementDate;
		setFormattedCommencementDate(commencementDate);
		this.installationDate=installationDate;
		this.repeatalarminterval=repeatalarminterval;
		this.nextEngineer=nextEngineer;
		this.refrigerationContractor=refrigerationContractor;
		this.hvacContractor=hvacContractor;
		this.maintenanceManager=maintenanceManager;
		this.siteGroup=siteGroup;
		this.transferedToNewBureau=transferedToNewBureau;
		this.alarmPriority = alarmPriority;
		this.outOfContract = outOfContract;
		setPriority(alarmPriority);
		this.refrigeratedSystem = refSystem;
	}

	public Branch(String branchCode,String name,int customer_id,String address1,String address2,String city,String zip,String country,String timezone,
			String fax,String phone,String email,Date orderDate,String agreementNumber,int agreementTerm,Date commencementDate,Date installationDate,
			int repeatalarminterval,String nextEngineer,String siteContact,String refrigerationContractor,String hvacContractor,String maintenanceManager,String siteGroup,
			byte transferedToNewBureau,int alarmPriority,boolean outOfContract, boolean selected, String refSystem){
		format = new SimpleDateFormat("dd-MM-yyyy");
		this.branchCode=branchCode;
		this.name=name;
		this.customer_id=customer_id;
		this.address1=address1;
		this.address2=address2;
		this.city=city;
		this.zip=zip;
		this.country=country;
		this.timezone=timezone;
		this.fax=fax;
		this.phone=phone;
		this.email=email;
		this.siteContact=siteContact;
		this.orderDate=orderDate;
		this.agreementNumber=agreementNumber;
		this.agreementTerm=agreementTerm;
		this.commencementDate=commencementDate;
		setFormattedCommencementDate(commencementDate);
		this.installationDate=installationDate;
		this.repeatalarminterval=repeatalarminterval;
		this.nextEngineer=nextEngineer;
		this.refrigerationContractor=refrigerationContractor;
		this.hvacContractor=hvacContractor;
		this.maintenanceManager=maintenanceManager;
		this.siteGroup=siteGroup;
		this.transferedToNewBureau=transferedToNewBureau;
		this.alarmPriority = alarmPriority;
		this.outOfContract = outOfContract;
		setPriority(alarmPriority);
		this.selected = selected;
		this.refrigeratedSystem= refSystem;
	}


	public Branch() {
		format = new SimpleDateFormat("dd-MM-yyyy");
	}

	public void setBranchCode(String branch_code){
		this.branchCode=branch_code;
	}
	
	public String getBranchCode(){
		return branchCode;
	}
		
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setCustomerId(int customerId){
		this.customer_id=customerId;
	}
	
	public int getCustomerId(){
		return customer_id;
	}
	
	public void setAddress1(String address1){
		this.address1=address1;
	}
	
	public String getAddress1(){
		return address1;
	}
	
	public void setAddress2(String address2){
		this.address2=address2;
	}
	
	public String getAddress2(){
		return address2;
	}
	
	public void setCity(String city){
		this.city=city;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setZip(String zip){
		this.zip=zip;
	}
	
	public String getZip(){
		return zip;
	}
	
	public void setCountry(String country){
		this.country=country;
	}
	
	public String getCountry(){
		return country;
	}
	
	public void setTimezone(String timezone){
		this.timezone=timezone;
	}
	
	public String getTimezone(){
		return timezone;
	}
		
	public void setFax(String fax){
		this.fax=fax;
	}
	
	public String getFax(){
		return fax;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}

	public String getAgreementNumber() {
		return agreementNumber;
	}

	public void setAgreementTerm(int agreementTerm) {
		this.agreementTerm = agreementTerm;
	}

	public int getAgreementTerm() {
		return agreementTerm;
	}

	public void setCommencementDate(Date commencementDate) {
		this.commencementDate = commencementDate;
		setFormattedCommencementDate(commencementDate);
	}

	public Date getCommencementDate() {
		return commencementDate;
	}

	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}

	public Date getInstallationDate() {
		return installationDate;
	}

	public void setHvacContractor(String hvacContractor) {
		this.hvacContractor = hvacContractor;
	}

	public String getHvacContractor() {
		return hvacContractor;
	}

	public void setSiteGroup(String siteGroup) {
		this.siteGroup = siteGroup;
	}

	public String getSiteGroup() {
		return siteGroup;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setRepeatalarminterval(int repeatalarminterval) {
		this.repeatalarminterval = repeatalarminterval;
	}

	public int getRepeatalarminterval() {
		return repeatalarminterval;
	}

	public void setRefrigerationContractor(String refrigerationContractor) {
		this.refrigerationContractor = refrigerationContractor;
	}

	public String getRefrigerationContractor() {
		return refrigerationContractor;
	}

	public void setMaintenanceManager(String maintenanceManager) {
		this.maintenanceManager = maintenanceManager;
	}

	public String getMaintenanceManager() {
		return maintenanceManager;
	}

	public void setNextEngineer(String nextEngineer) {
		this.nextEngineer = nextEngineer;
	}

	public String getNextEngineer() {
		return nextEngineer;
	}

	public void setTransferedToNewBureau(byte transferedToNewBureau) {
		this.transferedToNewBureau = transferedToNewBureau;
	}

	public byte getTransferedToNewBureau() {
		return transferedToNewBureau;
	}

	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}

	public String getSiteContact() {
		return siteContact;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setFormattedCommencementDate(Date formattedCommencementDate) {
		this.formattedCommencementDate = format.format(formattedCommencementDate);
	}

	public String getFormattedCommencementDate() {
		return formattedCommencementDate;
	}

	public void setAlarmPriority(int alarmPriority) {
		this.alarmPriority = alarmPriority;
		setPriority(alarmPriority);
	}

	public int getAlarmPriority() {
		return alarmPriority;
	}

	public void setPriority(int priority) {
		if (priority==0){
			this.priority = "Not Configured";
		}else if (priority==1){
			this.priority = "High";
		}else if (priority==2){
			this.priority = "Normal";
		}
	}

	public String getPriority() {
		return priority;
	}

	public void setOutOfContract(boolean outOfContract) {
		this.outOfContract = outOfContract;
	}

	public boolean isOutOfContract() {
		return outOfContract;
	}
	public void setCustomerName(String customerName) {
		this.customerName=customerName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getRefrigeratedSystem() {
		return refrigeratedSystem;
	}

	public void setRefrigeratedSystem(String refrigeratedSystem) {
		this.refrigeratedSystem = refrigeratedSystem;
	}
	
}

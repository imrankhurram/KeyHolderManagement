package com.nextcontrols.dao;

import java.util.List;

import com.nextcontrols.domain.Customer;

public interface ICustomerDAO {
	public List<Customer> getCustomers();
//	public List<Customer> getCustomers(long businessType);
//	public void addCustomer(int version,String name,String businesstype);
//	public void deleteCustomer(int customer_id);
//	public void modifyCustomer(int customer_id,int version,String name,String businesstype);
//	public Customer getSpecificCustomer(int customer_id);
//	public boolean checkIfWaitrose(int customer_id);
//	public List<CustomerBusinessType> getBusinessTypes();
//	public void addBusinessType(CustomerBusinessType newType);
//	public void deleteBusinessType(int typeId);
//	public void modifyBusinessType(CustomerBusinessType type);
	public String getCustomerNameFromSite(String branchCode);
//	public int getCustomerId(String branchCode);
//	public String getBusinessType(int customer_id);
//	public int checkCustomer(String alarm_id);
//	public String getTimeZonesForUsSites(String branchCode);
//	public String getBusinessTypeName(int businessTypeId);
}

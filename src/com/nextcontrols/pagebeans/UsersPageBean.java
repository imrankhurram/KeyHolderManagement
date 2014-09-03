package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.dao.UserDAO;
import com.nextcontrols.domain.Customer;
import com.nextcontrols.domain.User;

@ManagedBean(name = "users")
@SessionScoped
public class UsersPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> usersList;
	private List<Customer> customersList;
	private int selectedCustomerId;

	public UsersPageBean() {
		usersList = new ArrayList<User>();

	}

	public void intitializeCustomers() {
		customersList = UserDAO.getInstance().getCustomerList();
	}
	public String viewCustomerUsers(int customerId) {
		selectedCustomerId=customerId;
		initializeUsers();
		return "UsersPage.xhtml?faces-redirect=true";
	}

	public void initializeUsers() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		String userType = (String) session.getAttribute("userType");
		List<Integer> customerIds = new ArrayList<Integer>();
		if (userType.equals("WebAdmin") || userType.equals("WebReadOnly")) {
//			System.out.println("customerid: "+this.selectedCustomerId);
			customerIds.add(this.selectedCustomerId);
		} else {
			customerIds = UserDAO.getInstance().getCustomerIds(
					(int) session.getAttribute("userId"));
			if (customerIds.isEmpty()) {
				customerIds.add((int) session.getAttribute("customerId"));
			}
		}
		this.usersList = UserDAO.getInstance().getUserList(customerIds);
//		System.out.println("user size: " + this.usersList.size());
//		Map<Integer, String> customerIdsNdNames = UserDAO.getInstance()
//				.getDivisionNames(customerIds);
		Date today = new Date();
		// int i = 0;
		for (User user : this.usersList) {
//			user.setDivisionName(customerIdsNdNames.get(user.getCustomer_id()));
			long pinDiff = user.getPincodeExpires().getTime() - today.getTime();
			user.setPinCodeTimeout((pinDiff / (1000 * 60 * 60 * 24)));
			// if (i < 3) {
			// System.out.println("expiry date:" + user.getPincodeExpires());
			// System.out.println("expiry time utils:"
			// + TimeUnit.MILLISECONDS.toDays(pinDiff));
			// System.out.println("expiry pincode: "
			// + user.getPincodeExpires().getTime());
			// System.out.println("timout: " + user.getPinCodeTimeout());
			// }

			long passDiff = user.getPasswordExpires().getTime()
					- today.getTime();
			user.setPasswordTimeout((passDiff / (1000 * 60 * 60 * 24)));
			// i++;
		}
	}

	public void saveChanges() {
		FacesMessage message = null;
		Calendar pincodeDate = Calendar.getInstance();

		Calendar passwordDate = Calendar.getInstance();
		for (User user : this.usersList) {
			pincodeDate.setTime(new Date());
			passwordDate.setTime(new Date());
			if (user.getPinCodeTimeout().intValue() > 365) {
				// System.out.println("entered");
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Pin code timeout value should be less than 365!",
						"Pin code timeout value should be less than 365!");
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update("userMsg");
				return;
			}
			if (user.getPasswordTimeout().intValue() > 365) {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Password timeout value should be less than 365!",
						"Password timeout value should be less than 365!");
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update("userMsg");
				return;
			}
			if (user.getPinCodeTimeout().intValue() >= 0) {
				// System.out.println("timout:"
				// + user.getPinCodeTimeout().intValue());
				pincodeDate.add(Calendar.DATE, user.getPinCodeTimeout()
						.intValue());
				// System.out.println("date expiry:" + pincodeDate.getTime());
				user.setPincodeExpires(pincodeDate.getTime());
			}

			if (user.getPasswordTimeout().intValue() >= 0) {
				passwordDate.add(Calendar.DATE, user.getPasswordTimeout()
						.intValue());
				// System.out.println("pass expiry0: " +
				// passwordDate.getTime());
				user.setPasswordExpires(passwordDate.getTime());
				// System.out.println("pass expiry: " +
				// user.getPasswordExpires());
			}
		}
		UserDAO.getInstance().modifyUsers(this.usersList);
	}

	public String cancelChanges() {
		initializeUsers();
		return "UsersPage.xhtml?faces-redirect=true";
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}

	public List<Customer> getCustomersList() {
		return customersList;
	}

	public void setCustomersList(List<Customer> customersList) {
		this.customersList = customersList;
	}

}

package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.dao.DepartmentDAO;
import com.nextcontrols.domain.Department;



@ManagedBean(name = "departments")
@SessionScoped
public class DepartmentsPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Department> departmentsList;
	private Department selectedDepartment;
	public DepartmentsPageBean() {
		this.departmentsList=new ArrayList<Department>();
		
	}
	public void initializeDepartments(List<String> branchCodes){
//		departmentsList=DepartmentDAO.getInstance().getDepartmentList("NHSRGBQE01");//hard coded for the time being
		this.departmentsList=DepartmentDAO.getInstance().getDepartmentList(branchCodes);
	}
	public List<Department> getDepartmentsList() {
		return departmentsList;
	}
	public void setDepartmentsList(List<Department> departmentsList) {
		this.departmentsList = departmentsList;
	}
	public String showKeyholders(Department selectedDepartment) {
		this.selectedDepartment=selectedDepartment;
		initializeKeyholders();
		return "KeyholdersNormalCallListPage.xhtml?faces-redirect=true";
		
	}
	public void initializeKeyholders (){
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		KeyholdersPageBean keyholdersPageBean = (KeyholdersPageBean) session
				.getAttribute("keyholders");
		if(keyholdersPageBean!=null){
			session.removeAttribute("keyholders");
			keyholdersPageBean.initializeKeyholders(this.selectedDepartment);
			session.setAttribute("keyholders", keyholdersPageBean);
		}else{
			keyholdersPageBean=new KeyholdersPageBean();
			keyholdersPageBean.initializeKeyholders(this.selectedDepartment);
			session.setAttribute("keyholders", keyholdersPageBean);
		}
	}
	public Department getSelectedDepartment() {
		return selectedDepartment;
	}
	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}
	
}

package com.nextcontrols.utility;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

import com.nextcontrols.dao.KeyholderDAO;
import com.nextcontrols.domain.Keyholder;
import com.nextcontrols.pagebeans.KeyholderCallListPageBean;

@FacesConverter(value="keyholderconverter4calllist")
public class KeyholderConverter4CallList implements javax.faces.convert.Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		FacesContext ctx= FacesContext.getCurrentInstance();
		Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
		KeyholderCallListPageBean keyholderCallBean = (KeyholderCallListPageBean) sessionMap.get("keyholdercalllist");
//		System.out.println("converter get as object called");
		int number=Integer.parseInt(arg2);
		Keyholder source=KeyholderDAO.getInstance().getKeyholderById(
				keyholderCallBean.getSelectedDepartment().getBranch_code(), keyholderCallBean.getSelectedCallList().getKeyholderListId(),number);
//		for (int i=0;i<=source.size()-1;i++){
//			if (source.get(i).getKeyholderId()==number){
//				return source.get(i);
//			}
//		}
//		return null;
		return source;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
//		System.out.println("converter get as string called: "+((Keyholder) arg2).getContactName());
		return String.valueOf(((Keyholder) arg2).getKeyholderId());
	}
	
}

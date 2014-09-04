package com.nextcontrols.utility;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "dateValidator")
public class DateValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
//        	System.out.println("value null");
            return;
        }
         
        //Leave the null handling of startDate to required="true"
        Object startDateValue = component.getAttributes().get("startDate");
        if (startDateValue==null) {
//        	System.out.println("start value null");
            return;
        }
         
        Date startDate = (Date)startDateValue;
        Date endDate = (Date)value;
        java.util.Calendar startDateCalendar=java.util.Calendar.getInstance();
        startDateCalendar.setTime(startDate);
        startDateCalendar.add(java.util.Calendar.MONTH, 6);
//        System.out.println("start date: "+startDate);
//        System.out.println("end date: "+endDate);
        if (endDate.after(startDateCalendar.getTime())) {
//        	System.out.println("date exceeds");
            throw new ValidatorException(new FacesMessage("Please select six months date range!"));
        }
    }
}
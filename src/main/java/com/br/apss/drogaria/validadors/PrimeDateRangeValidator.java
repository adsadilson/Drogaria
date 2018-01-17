package com.br.apss.drogaria.validadors;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.Date;

@FacesValidator("primeDateRangeValidator")
public class PrimeDateRangeValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		// Leave the null handling of startDate to required="true"
		Object startDateValue = component.getAttributes().get("startDate");
		if (startDateValue == null) {
			return;
		}

		Date startDate = (Date) startDateValue;
		Date endDate = (Date) value;
		if (endDate.before(startDate)) {
			FacesMessage msg = new FacesMessage("A data final esta maior que a data inicial!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

}

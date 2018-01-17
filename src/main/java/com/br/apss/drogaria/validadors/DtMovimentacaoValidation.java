package com.br.apss.drogaria.validadors;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class DtMovimentacaoValidation implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		// Leave the null handling of startDate to required="true"
		Object startDateValue = component.getAttributes().get("dtMovimentacao");
		if (startDateValue == null) {
			return;
		}

		Date startDate = (Date) startDateValue;
		Date endDate = (Date) value;
		if (endDate.before(startDate)) {
			FacesMessage msg = new FacesMessage("Informe uma data final maior que a inicial.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}

}

package com.br.apss.drogaria.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.br.apss.drogaria.model.ConfigEmail;
import com.br.apss.drogaria.service.ConfigEmailService;

@FacesConverter(forClass = ConfigEmail.class)
public class ConfigEmailConverter implements Converter {

	@Inject
	private ConfigEmailService dao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ConfigEmail retorno = null;

		if (value != null && value != "") {
			retorno = this.dao.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((ConfigEmail) value).getId();
			String retorno = (codigo == null ? null : codigo.toString());

			return retorno;
		}

		return "";
	}

}
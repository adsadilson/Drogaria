package com.br.apss.drogaria.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.service.PlanoContaService;


@FacesConverter(forClass = PlanoConta.class)
public class PlanoContaConverter implements Converter {

	@Inject
	private PlanoContaService dao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		PlanoConta retorno = null;

		if (value != null && value != "") {
			retorno = this.dao.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && value instanceof PlanoConta) {
			Long codigo = ((PlanoConta) value).getId();
			String retorno = (codigo == null ? null : codigo.toString());

			return retorno;
		}

		return "";
	}

}
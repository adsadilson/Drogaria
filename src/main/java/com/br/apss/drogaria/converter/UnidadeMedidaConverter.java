package com.br.apss.drogaria.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.br.apss.drogaria.model.UnidadeMedida;
import com.br.apss.drogaria.service.UnidadeMedidaService;

@FacesConverter(forClass = UnidadeMedida.class)
public class UnidadeMedidaConverter implements Converter {

	@Inject
	private UnidadeMedidaService dao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		UnidadeMedida retorno = null;

		if (value != null && value != "") {
			retorno = this.dao.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((UnidadeMedida) value).getId();
			String retorno = (codigo == null ? null : codigo.toString());

			return retorno;
		}

		return "";
	}

}
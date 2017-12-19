package com.br.apss.drogaria.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.br.apss.drogaria.model.GrupoUsuario;
import com.br.apss.drogaria.service.GrupoUsuarioService;

@FacesConverter(forClass = GrupoUsuario.class)
public class GrupoUsuarioConverter implements Converter {

	@Inject
	private GrupoUsuarioService dao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		GrupoUsuario retorno = null;

		if (value != null && value != "") {
			retorno = this.dao.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && value instanceof GrupoUsuario) {
			Long codigo = ((GrupoUsuario) value).getId();
			String retorno = (codigo == null ? null : codigo.toString());

			return retorno;
		}

		return "";
	}

}
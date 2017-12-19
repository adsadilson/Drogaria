package com.br.apss.drogaria.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.service.ProdutoService;


@FacesConverter(forClass = Produto.class)
public class ProdutoConverter implements Converter {

	@Inject
	private ProdutoService dao;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Produto retorno = null;

		if (value != null && value != "") {
			retorno = this.dao.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null && value instanceof Produto) {
			Long codigo = ((Produto) value).getId();
			String retorno = (codigo == null ? null : codigo.toString());

			return retorno;
		}

		return "";
	}

}
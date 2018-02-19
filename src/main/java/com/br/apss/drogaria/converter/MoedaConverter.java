package com.br.apss.drogaria.converter;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("moedaConverter")
public class MoedaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		if(!valor.isEmpty()){
			valor = valor.replace("R$ ", "").replace(".", "").replace(",", ".");
			BigDecimal numero = new BigDecimal(valor);
			
			return numero;
		}
		return valor;		
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object total) {
		return total.toString().replace(".", ",");
	}

}


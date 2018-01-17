package com.br.apss.drogaria.validadors;

import java.util.Date;

public class ValidadorBean {

	public static boolean DataInicialFinal(Date dataInicio, Date dataFim) {
		boolean data;
		if (dataInicio.before(dataFim)){
			data = false;
		}
		else if (dataInicio.after(dataFim))
			data = true;
		else
			data = false;
		return data;
	}
}

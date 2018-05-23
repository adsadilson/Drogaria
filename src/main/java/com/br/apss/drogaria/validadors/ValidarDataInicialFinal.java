package com.br.apss.drogaria.validadors;

import java.util.Date;

public class ValidarDataInicialFinal {

	public static boolean validateBeginEndDate(Date dataInicio, Date dataFim) {
		boolean result = false;
		if (!dataFim.after(dataInicio)) {
			result = true;
		}
		return result;
	}
}

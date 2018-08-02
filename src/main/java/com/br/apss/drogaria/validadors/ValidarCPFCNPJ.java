package com.br.apss.drogaria.validadors;

public class ValidarCPFCNPJ {

	public static boolean validar(String campo) {

		String str = campo.replaceAll("[^0-9]", "");

		if (str.length() == 14) {

			int iSoma = 0, iDigito;
			char[] chCaracteresCNPJ;
			String strCNPJ_Calculado;

			if (!str.substring(0, 1).equals("")) {
				try {
					str = str.replace('.', ' ');
					str = str.replace('/', ' ');
					str = str.replace('-', ' ');
					str = str.replaceAll(" ", "");
					strCNPJ_Calculado = str.substring(0, 12);
					if (str.length() != 14) {
						return false;
					}
					chCaracteresCNPJ = str.toCharArray();
					for (int i = 0; i < 4; i++) {
						if ((chCaracteresCNPJ[i] - 48 >= 0) && (chCaracteresCNPJ[i] - 48 <= 9)) {
							iSoma += (chCaracteresCNPJ[i] - 48) * (6 - (i + 1));
						}
					}
					for (int i = 0; i < 8; i++) {
						if ((chCaracteresCNPJ[i + 4] - 48 >= 0) && (chCaracteresCNPJ[i + 4] - 48 <= 9)) {
							iSoma += (chCaracteresCNPJ[i + 4] - 48) * (10 - (i + 1));
						}
					}
					iDigito = 11 - (iSoma % 11);
					strCNPJ_Calculado += ((iDigito == 10) || (iDigito == 11)) ? "0" : Integer.toString(iDigito);
					/*
					 * Segunda parte
					 */
					iSoma = 0;
					for (int i = 0; i < 5; i++) {
						if ((chCaracteresCNPJ[i] - 48 >= 0) && (chCaracteresCNPJ[i] - 48 <= 9)) {
							iSoma += (chCaracteresCNPJ[i] - 48) * (7 - (i + 1));
						}
					}
					for (int i = 0; i < 8; i++) {
						if ((chCaracteresCNPJ[i + 5] - 48 >= 0) && (chCaracteresCNPJ[i + 5] - 48 <= 9)) {
							iSoma += (chCaracteresCNPJ[i + 5] - 48) * (10 - (i + 1));
						}
					}
					iDigito = 11 - (iSoma % 11);
					strCNPJ_Calculado += ((iDigito == 10) || (iDigito == 11)) ? "0" : Integer.toString(iDigito);
					return str.equals(strCNPJ_Calculado);
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}

		} else {

			int i, soma1, soma2, digito1, digito2;
			if (str.length() != 11) {
				return false;
			}

			if ((str.equals("00000000000")) || (str.equals("11111111111")) || (str.equals("22222222222"))
					|| (str.equals("33333333333")) || (str.equals("44444444444")) || (str.equals("55555555555"))
					|| (str.equals("66666666666")) || (str.equals("77777777777")) || (str.equals("88888888888"))
					|| (str.equals("99999999999"))) {
				return false;
			}

			// Calcula o primeiro dígito
			soma1 = 0;
			for (i = 0; i <= 8; i++) {
				soma1 = soma1 + Integer.parseInt(str.substring(i, i + 1)) * (10 - i);
			}

			if (soma1 % 11 < 2) {
				digito1 = 0;
			} else {
				digito1 = 11 - (soma1 % 11);
			}

			// Calcula o segundo dígito
			soma2 = 0;
			for (i = 0; i <= 9; i++) {
				soma2 = soma2 + Integer.parseInt(str.substring(i, i + 1)) * (11 - i);
			}

			if (soma2 % 11 < 2) {
				digito2 = 0;
			} else {
				digito2 = 11 - (soma2 % 11);
			}

			if ((digito1 == Integer.parseInt(str.substring(9, 10)))
					&& (digito2 == Integer.parseInt(str.substring(10)))) {
				return true;
			}

			return false;

		}
	}

}

package com.br.apss.drogaria.enums;

public enum FormaPagamento {

	DINHEIRO("DINHEIRO"), CARTAO_CREDITO("CARTAO DE CREDITO"), CARTAO_DEBITO("CARTAO DE DEBITO"), CHEQUE(
			"CHEQUE"), BOLETO_BANCARIO("BOLETO BANCARIO"), DEPOSITO_BANCARIO("DEPOSITO BANCARIO");

	private String descricao;

	FormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
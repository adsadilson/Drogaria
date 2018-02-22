package com.br.apss.drogaria.enums;

public enum TipoBaixa {

	AP("Apenas uma conta"),
	MC("Multiplas contas");
	
	private String descricao;

	TipoBaixa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
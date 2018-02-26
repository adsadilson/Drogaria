package com.br.apss.drogaria.enums;

public enum TipoBaixa {

	TOTAL("TOTAL"),
	PARCIAL("PARCIAL");
	
	private String descricao;

	TipoBaixa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
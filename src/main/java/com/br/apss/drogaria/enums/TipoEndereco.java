package com.br.apss.drogaria.enums;

public enum TipoEndereco {

	RESIDENCIAL("RESIDENCIAL"),
	COMERCIAL("COMERCIAL"),
	COBRANCA("COBRANÇA");
	
	private String descricao;

	TipoEndereco(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
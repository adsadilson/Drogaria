package com.br.apss.drogaria.enums;

public enum TipoTelefone {

	RESIDENCIAL("RESIDENCIAL"),
	COMERCIAL("COMERCIAL"),
	FIXO("FIXO"),
	CELULAR("CELULAR");
	
	private String descricao;

	TipoTelefone(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
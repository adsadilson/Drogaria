package com.br.apss.drogaria.enums;

public enum TipoCartao {

	CREDITO("CREDITO"), DEBITO("DEBITO");

	private String descricao;

	TipoCartao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
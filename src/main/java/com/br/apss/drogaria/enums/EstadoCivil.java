package com.br.apss.drogaria.enums;

public enum EstadoCivil {

	SOLTEIRO("SOLTEIRO(A)"),
	CASADO("CASADO(A)"),
	DESQUITADO("DESQUITADO(A)"),
	DIVORCIADO("DIVORCIADO(A)"),
	VIUVO("VIUVO(A)"),
	UNIAO_ESTAVEL("UNIÃO ESTAVEL");
	
	private String descricao;
	
	private EstadoCivil(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}
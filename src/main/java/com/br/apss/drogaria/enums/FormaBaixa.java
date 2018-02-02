package com.br.apss.drogaria.enums;

public enum FormaBaixa {

	BI("Baixa individual de lançamentos"),
	AG("Baixa agrupada");
	
	private String descricao;

	FormaBaixa(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
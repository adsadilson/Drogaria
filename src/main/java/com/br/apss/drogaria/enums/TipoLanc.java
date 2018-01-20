package com.br.apss.drogaria.enums;

public enum TipoLanc {

	CC("C/CORRENTES", "CC"), 
	CR("C/CORRENTE X RECEITAS", "CR"), 
	CD("C/CORRENTE X DESPESAS", "CD");

	private String descricao;
	private String sigla;

	private TipoLanc(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
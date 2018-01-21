package com.br.apss.drogaria.enums;

public enum TipoCobranca {

	BOLETO_BANCARIO("BOLETO BANCARIO","BB"),
	CHEQUE("CHEQUE","CHQ"),
	DINHEIRO("DINHEIRO","DH");
	
	private String descricao;
	private String sigla;
	
	private TipoCobranca(String descricao, String sigla) {
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
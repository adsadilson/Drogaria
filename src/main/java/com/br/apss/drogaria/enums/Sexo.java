package com.br.apss.drogaria.enums;

public enum Sexo {

	M("MASCULINO","M"),
	F("FEMININO","F"),
	O("OUTROS","O");
	
	private String descricao;
	private String sigla;
	
	private Sexo(String descricao, String sigla) {
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
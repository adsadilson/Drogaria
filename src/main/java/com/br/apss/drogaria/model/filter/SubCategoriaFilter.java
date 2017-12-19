package com.br.apss.drogaria.model.filter;

import java.io.Serializable;

public class SubCategoriaFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String origem;
	private Boolean status;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOrigem(String origem) {
		if (origem.equals("principal")) {
			this.status = null;
		}
		this.origem = origem;
	}

	public String getOrigem() {
		return origem;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}

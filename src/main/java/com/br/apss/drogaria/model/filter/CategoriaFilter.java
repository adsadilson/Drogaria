package com.br.apss.drogaria.model.filter;

import java.io.Serializable;

public class CategoriaFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private Boolean status;
	private String origem;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getStatus() {
		return status;
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

}

package com.br.apss.drogaria.model.filter;

import java.io.Serializable;

public class CategoriaFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private Boolean status;
	private String origem;
	private String campoOrdenacao = "nome";
	private boolean ascendente = true;

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

	public String getCampoOrdenacao() {
		return campoOrdenacao;
	}

	public void setCampoOrdenacao(String campoOrdenacao) {
		this.campoOrdenacao = campoOrdenacao;
	}

	public boolean isAscendente() {
		return ascendente;
	}

	public void setAscendente(boolean ascendente) {
		this.ascendente = ascendente;
	}

}

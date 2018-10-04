package com.br.apss.drogaria.model.filter;

import java.io.Serializable;

public class UnidadeMedidaFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String descricao;
	private String origem;
	private Boolean status;
	private Boolean tipoAnalitico = false;
	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String campoOrdenacao;
	private boolean ascendente;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
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

	public Boolean getTipoAnalitico() {
		return tipoAnalitico;
	}

	public void setTipoAnalitico(Boolean tipoAnalitico) {
		this.tipoAnalitico = tipoAnalitico;
	}

}

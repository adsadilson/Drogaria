package com.br.apss.drogaria.model.filter;

import com.br.apss.drogaria.enums.Estado;

public class ContaAPagarFilter {

	private String nome;
	private Estado uf;
	private Boolean status;
	private String origem;
	private int priRegistro;
	private int qtdeRegistros;
	private String campoOrder;
	private boolean asc;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getUf() {
		return uf;
	}

	public void setUf(Estado uf) {
		this.uf = uf;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public int getPriRegistro() {
		return priRegistro;
	}

	public void setPriRegistro(int priRegistro) {
		this.priRegistro = priRegistro;
	}

	public int getQtdeRegistros() {
		return qtdeRegistros;
	}

	public void setQtdeRegistros(int qtdeRegistros) {
		this.qtdeRegistros = qtdeRegistros;
	}

	public String getCampoOrder() {
		return campoOrder;
	}

	public void setCampoOrder(String campoOrder) {
		this.campoOrder = campoOrder;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

}

package com.br.apss.drogaria.model.filter;

import com.br.apss.drogaria.enums.TipoCartao;
import com.br.apss.drogaria.enums.TipoConta;

public class AdmCartaoFilter {

	private String nome;
	private String operadora;
	private TipoConta tipo;
	private TipoCartao tipoCartao;
	private Boolean status;
	private String origem;

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

	public void setOrigem(String origem) {
		if (origem.equals("principal")) {
			this.status = null;
			this.tipo = null;
		}
		this.origem = origem;
	}

	public String getOrigem() {
		return origem;
	}

	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	public TipoCartao getTipoCartao() {
		return tipoCartao;
	}

	public void setTipoCartao(TipoCartao tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

}

package com.br.apss.drogaria.model.filter;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.PlanoConta;

public class PlanoContaFilter {

	private String nome;
	private TipoConta tipo;
	private Boolean status;
	private String origem;
	private PlanoConta planoContaPai;
	private PlanoConta planoConta;
	private TipoRelatorio categoria;
	private String campoOrdenacao = "mascara";
	private boolean ascendente = true;

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

	public PlanoConta getPlanoContaPai() {
		return planoContaPai;
	}

	public void setPlanoContaPai(PlanoConta planoContaPai) {
		this.planoContaPai = planoContaPai;
	}

	public TipoRelatorio getCategoria() {
		return categoria;
	}

	public void setCategoria(TipoRelatorio categoria) {
		this.categoria = categoria;
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

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
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

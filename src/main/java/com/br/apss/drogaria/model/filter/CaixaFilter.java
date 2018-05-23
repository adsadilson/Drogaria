package com.br.apss.drogaria.model.filter;

import java.util.Date;

import com.br.apss.drogaria.model.Pessoa;


public class CaixaFilter {

	private Date inicial;
	private Date fim;
	private Pessoa responsavel;
	private Date data;
	private boolean status = true;

	public Date getData() {
		return data;
	}

	public Date getFim() {
		return fim;
	}

	public Date getInicial() {
		return inicial;
	}

	public Pessoa getResponsavel() {
		return responsavel;
	}

	public boolean isStatus() {
		return status;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public void setInicial(Date inicial) {
		this.inicial = inicial;
	}

	public void setResponsavel(Pessoa responsavel) {
		this.responsavel = responsavel;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}

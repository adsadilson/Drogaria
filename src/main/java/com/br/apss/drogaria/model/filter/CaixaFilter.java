package com.br.apss.drogaria.model.filter;

import java.util.Date;

import com.br.apss.drogaria.model.Usuario;

public class CaixaFilter {

	private Date inicial;
	private Date fim;
	private Usuario responsavel;
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

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

}

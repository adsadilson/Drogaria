package com.br.apss.drogaria.model.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.br.apss.drogaria.model.Pessoa;

public class PagamentoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dtIni = new Date();
	private Date dtFim = new Date();
	private Date dataLimite = new Date();
	private Pessoa fornecedor;
	private BigDecimal valor1;
	private BigDecimal valor2;
	private int primeiroRegistro;
	private int qtdeRegistro;
	private String ordenacao;
	private Boolean ascendente = true;

	public void getDataFimMin() {
		this.dataLimite = this.dtIni;
	}

	public Date getDtIni() {
		return dtIni;
	}

	public void setDtIni(Date dtIni) {
		this.dtIni = dtIni;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValor1() {
		return valor1;
	}

	public void setValor1(BigDecimal valor1) {
		this.valor1 = valor1;
	}

	public BigDecimal getValor2() {
		return valor2;
	}

	public void setValor2(BigDecimal valor2) {
		this.valor2 = valor2;
	}

	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public int getQtdeRegistro() {
		return qtdeRegistro;
	}

	public void setQtdeRegistro(int qtdeRegistro) {
		this.qtdeRegistro = qtdeRegistro;
	}

	public String getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Boolean getAscendente() {
		return ascendente;
	}

	public void setAscendente(Boolean ascendente) {
		this.ascendente = ascendente;
	}

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
	}

}

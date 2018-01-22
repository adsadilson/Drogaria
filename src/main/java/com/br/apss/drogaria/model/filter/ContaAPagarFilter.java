package com.br.apss.drogaria.model.filter;

import java.math.BigDecimal;
import java.util.Date;

import com.br.apss.drogaria.enums.Estado;

public class ContaAPagarFilter {

	private String nome;
	private Estado uf;
	private Boolean status;
	private String origem;
	private int primerioRegistro;
	private int quantidadeRegistros;
	private String campoOrdernacao;
	private boolean asc;
	private Date dataIni = new Date();
	private Date dataFim = new Date();
	private BigDecimal entrada1;
	private BigDecimal entrada2;
	private BigDecimal saida1;
	private BigDecimal saida2;

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

	

	public int getPrimerioRegistro() {
		return primerioRegistro;
	}

	public void setPrimerioRegistro(int primerioRegistro) {
		this.primerioRegistro = primerioRegistro;
	}

	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public String getCampoOrdernacao() {
		return campoOrdernacao;
	}

	public void setCampoOrdernacao(String campoOrdernacao) {
		this.campoOrdernacao = campoOrdernacao;
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

	public Date getDataIni() {
		return dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public BigDecimal getEntrada1() {
		return entrada1;
	}

	public void setEntrada1(BigDecimal entrada1) {
		this.entrada1 = entrada1;
	}

	public BigDecimal getEntrada2() {
		return entrada2;
	}

	public void setEntrada2(BigDecimal entrada2) {
		this.entrada2 = entrada2;
	}

	public BigDecimal getSaida1() {
		return saida1;
	}

	public void setSaida1(BigDecimal saida1) {
		this.saida1 = saida1;
	}

	public BigDecimal getSaida2() {
		return saida2;
	}

	public void setSaida2(BigDecimal saida2) {
		this.saida2 = saida2;
	}

}

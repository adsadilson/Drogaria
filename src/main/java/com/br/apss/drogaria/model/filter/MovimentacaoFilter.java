package com.br.apss.drogaria.model.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MovimentacaoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long contaID;
	private String nomeConta;
	private Date dataIni = new Date();
	private Date dataFim = new Date();
	private Date dataLimite = new Date();
	private String doc;
	private String descricao;
	private BigDecimal entrada1;
	private BigDecimal entrada2;
	private BigDecimal saida1;
	private BigDecimal saida2;
	private int primeiroRegistro;
	private int qtdeRegistro;
	private String ordenacao;
	private Boolean ascendente = true;

	public void getDataFimMin() {
		this.dataLimite = this.dataIni;
	}

	public Long getContaID() {
		return contaID;
	}

	public void setContaID(Long contaID) {
		this.contaID = contaID;
	}

	public String getNomeConta() {
		return nomeConta;
	}

	public void setNomeConta(String nomeConta) {
		this.nomeConta = nomeConta;
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

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Date getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(Date dataLimite) {
		this.dataLimite = dataLimite;
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

}

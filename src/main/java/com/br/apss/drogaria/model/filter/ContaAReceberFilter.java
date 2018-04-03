package com.br.apss.drogaria.model.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;

public class ContaAReceberFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlanoConta planoConta;
	private Pessoa cliente;
	private String nomeConta;
	private Date dataIni = new Date();
	private Date dataFim = new Date();
	private Date dataLimite = new Date();
	private String doc;
	private String descricao;
	private Date dataEmissaoIni;
	private Date dataEmissaoFim;
	private Date dataVenctoIni;
	private Date dataVenctoFim;
	private BigDecimal valor1;
	private BigDecimal valor2;
	private int primeiroRegistro;
	private int qtdeRegistro;
	private String ordenacao;
	private Boolean ascendente = true;
	private String status;

	public void getDataFimMin() {
		this.dataLimite = this.dataIni;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
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

	public Date getDataEmissaoIni() {
		return dataEmissaoIni;
	}

	public void setDataEmissaoIni(Date dataEmissaoIni) {
		this.dataEmissaoIni = dataEmissaoIni;
	}

	public Date getDataEmissaoFim() {
		return dataEmissaoFim;
	}

	public void setDataEmissaoFim(Date dataEmissaoFim) {
		this.dataEmissaoFim = dataEmissaoFim;
	}

	public Date getDataVenctoIni() {
		return dataVenctoIni;
	}

	public void setDataVenctoIni(Date dataVenctoIni) {
		this.dataVenctoIni = dataVenctoIni;
	}

	public Date getDataVenctoFim() {
		return dataVenctoFim;
	}

	public void setDataVenctoFim(Date dataVenctoFim) {
		this.dataVenctoFim = dataVenctoFim;
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

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

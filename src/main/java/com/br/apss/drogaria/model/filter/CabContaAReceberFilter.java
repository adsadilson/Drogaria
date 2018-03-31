package com.br.apss.drogaria.model.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.br.apss.drogaria.model.Pessoa;

public class CabContaAReceberFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String descricao;
	private String origem;
	private Boolean status;
	private Date dataEmissaoIni;
	private Date dataEmissaoFim;
	private Date dataVenctoIni;
	private Date dataVenctoFim;
	private BigDecimal valor1;
	private BigDecimal valor2;
	private String doc;
	private Pessoa fornecedor;
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

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

}

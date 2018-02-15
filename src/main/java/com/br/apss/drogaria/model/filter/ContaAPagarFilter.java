package com.br.apss.drogaria.model.filter;

import java.math.BigDecimal;
import java.util.Date;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.model.Pessoa;

public class ContaAPagarFilter {

	private String nome;
	private Estado uf;
	private Boolean status;
	private String origem;
	private int primerioRegistro;
	private int quantidadeRegistros;
	private String campoOrdernacao;
	private boolean asc;
	private Date dataEmissaoIni;
	private Date dataEmissaoFim;
	private Date dataVenctoIni;
	private Date dataVenctoFim;
	private BigDecimal valor1;
	private BigDecimal valor2;
	private String doc;
	private Pessoa fornecedor;

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

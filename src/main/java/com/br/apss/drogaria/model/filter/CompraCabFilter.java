package com.br.apss.drogaria.model.filter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.br.apss.drogaria.enums.StatusPedido;
import com.br.apss.drogaria.model.Pessoa;

public class CompraCabFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private StatusPedido[] statuses;
	private String origem;
	private int priRegistro;
	private int qtdeRegistros;
	private String campoOrder;
	private boolean asc;
	private Date emissaoIni;
	private Date emissaoFim;
	private Date entradaIni;
	private Date entradaFim;
	private BigDecimal valorNT1;
	private BigDecimal valorNT2;
	private BigDecimal valorP1;
	private BigDecimal valorP2;
	private String doc;
	private Pessoa fornecedor;

	public StatusPedido[] getStatuses() {
		return statuses;
	}

	public void setStatuses(StatusPedido[] statuses) {
		this.statuses = statuses;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public int getPriRegistro() {
		return priRegistro;
	}

	public void setPriRegistro(int priRegistro) {
		this.priRegistro = priRegistro;
	}

	public int getQtdeRegistros() {
		return qtdeRegistros;
	}

	public void setQtdeRegistros(int qtdeRegistros) {
		this.qtdeRegistros = qtdeRegistros;
	}

	public String getCampoOrder() {
		return campoOrder;
	}

	public void setCampoOrder(String campoOrder) {
		this.campoOrder = campoOrder;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public Date getEmissaoIni() {
		return emissaoIni;
	}

	public void setEmissaoIni(Date emissaoIni) {
		this.emissaoIni = emissaoIni;
	}

	public Date getEmissaoFim() {
		return emissaoFim;
	}

	public void setEmissaoFim(Date emissaoFim) {
		this.emissaoFim = emissaoFim;
	}

	public Date getEntradaIni() {
		return entradaIni;
	}

	public void setEntradaIni(Date entradaIni) {
		this.entradaIni = entradaIni;
	}

	public Date getEntradaFim() {
		return entradaFim;
	}

	public void setEntradaFim(Date entradaFim) {
		this.entradaFim = entradaFim;
	}

	public BigDecimal getValorNT1() {
		return valorNT1;
	}

	public void setValorNT1(BigDecimal valorNT1) {
		this.valorNT1 = valorNT1;
	}

	public BigDecimal getValorNT2() {
		return valorNT2;
	}

	public void setValorNT2(BigDecimal valorNT2) {
		this.valorNT2 = valorNT2;
	}

	public BigDecimal getValorP1() {
		return valorP1;
	}

	public void setValorP1(BigDecimal valorP1) {
		this.valorP1 = valorP1;
	}

	public BigDecimal getValorP2() {
		return valorP2;
	}

	public void setValorP2(BigDecimal valorP2) {
		this.valorP2 = valorP2;
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

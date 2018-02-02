package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.enums.TipoConta;

@Entity
@Table(name = "conta_apagar")
@SequenceGenerator(name = "CONTA_APAGAR_ID", sequenceName = "CONTA_APAGAR_SEQ", allocationSize = 1, initialValue = 1)
public class ContaAPagar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CONTA_APAGAR_ID")
	private Long id;

	@Column(name = "origem_id")
	private Long origemId;

	@Column(name = "vinculo")
	private Long vinculo;

	@Column(name = "descricao", length = 255)
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_lanc")
	private Date dataLanc;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_doc")
	private Date dataDoc;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencto")
	private Date dataVencto;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_pagto")
	private Date dataPagto;

	@Column(name = "num_doc", length = 15)
	private String numDoc;

	@Column(name = "parcela", length = 5)
	private String parcela;

	@ManyToOne
	@JoinColumn(name = "fornecedor_id")
	private Pessoa fornecedor;

	@Column(name = "valor", precision = 12, scale = 2)
	private BigDecimal valor = BigDecimal.ZERO;

	@Column(name = "vlr_apagar", precision = 12, scale = 2)
	private BigDecimal vlrApagar = BigDecimal.ZERO;

	@Column(name = "vlr_pago", precision = 12, scale = 2)
	private BigDecimal valorPago = BigDecimal.ZERO;

	@Column(name = "vlr_multa", precision = 12, scale = 2)
	private BigDecimal valorMulta = BigDecimal.ZERO;

	@Column(name = "vlr_juro", precision = 12, scale = 2)
	private BigDecimal valorJuro = BigDecimal.ZERO;

	@Column(name = "vlr_desc", precision = 12, scale = 2)
	private BigDecimal valorDesc = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "conta_apagar_movimentacao", joinColumns = @JoinColumn(name = "conta_apagar_id"), inverseJoinColumns = @JoinColumn(name = "movimentacao_id"))
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

	@Enumerated(EnumType.STRING)
	@Column(name = "tipoConta", length = 20)
	private TipoConta tipoConta;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cobranca", length = 40)
	private TipoCobranca tipoCobranca;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_baixa", length = 45)
	private FormaBaixa formaBaixa;

	@Column(length = 20)
	private String status;

	@Transient
	private int dias;

	@Transient
	private int numVezes = 1;

	@Transient
	private int periodo = 30;

	@Transient
	private BigDecimal totalRateio = BigDecimal.ZERO;

	@Transient
	private BigDecimal totalFormaPg = BigDecimal.ZERO;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrigemId() {
		return origemId;
	}

	public void setOrigemId(Long origemId) {
		this.origemId = origemId;
	}

	public Long getVinculo() {
		return vinculo;
	}

	public void setVinculo(Long vinculo) {
		this.vinculo = vinculo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataLanc() {
		return dataLanc;
	}

	public void setDataLanc(Date dataLanc) {
		this.dataLanc = dataLanc;
	}

	public Date getDataDoc() {
		return dataDoc;
	}

	public void setDataDoc(Date dataDoc) {
		this.dataDoc = dataDoc;
	}

	public Date getDataVencto() {
		return dataVencto;
	}

	public void setDataVencto(Date dataVencto) {
		this.dataVencto = dataVencto;
	}

	public Date getDataPagto() {
		return dataPagto;
	}

	public void setDataPagto(Date dataPagto) {
		this.dataPagto = dataPagto;
	}

	public String getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getVlrApagar() {
		return vlrApagar;
	}

	public void setVlrApagar(BigDecimal vlrApagar) {
		this.vlrApagar = vlrApagar;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

	public BigDecimal getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}

	public BigDecimal getValorJuro() {
		return valorJuro;
	}

	public void setValorJuro(BigDecimal valorJuro) {
		this.valorJuro = valorJuro;
	}

	public BigDecimal getValorDesc() {
		return valorDesc;
	}

	public void setValorDesc(BigDecimal valorDesc) {
		this.valorDesc = valorDesc;
	}

	public FormaBaixa getFormaBaixa() {
		return formaBaixa;
	}

	public void setFormaBaixa(FormaBaixa formaBaixa) {
		this.formaBaixa = formaBaixa;
	}

	public int getNumVezes() {
		return numVezes;
	}

	public void setNumVezes(int numVezes) {
		this.numVezes = numVezes;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public BigDecimal getTotalRateio() {
		return totalRateio;
	}

	public void setTotalRateio(BigDecimal totalRateio) {
		this.totalRateio = totalRateio;
	}

	public BigDecimal getTotalFormaPg() {
		return totalFormaPg;
	}

	public void setTotalFormaPg(BigDecimal totalFormaPg) {
		this.totalFormaPg = totalFormaPg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContaAPagar other = (ContaAPagar) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

}

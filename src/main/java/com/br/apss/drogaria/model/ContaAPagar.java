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

import com.br.apss.drogaria.enums.TipoCobranca;

@Entity
@Table(name = "conta_apagar")
@SequenceGenerator(name = "CONTA_APAGAR_ID", sequenceName = "CONTA_APAGAR_SEQ", allocationSize = 1, initialValue = 1)
public class ContaAPagar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CONTA_APAGAR_ID")
	private Long id;

	@Column(name = "vinculo")
	private Long vinculo;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_vencto")
	private Date dataVencto;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_doc")
	private Date dataDoc;

	@Column(name = "num_doc", length = 15)
	private String numDoc;

	@Column(name = "parcela", length = 5)
	private String parcela;

	@ManyToOne
	@JoinColumn(name = "fornecedor_id")
	private Pessoa fornecedor;

	@Column(name = "valor", precision = 12, scale = 2)
	private BigDecimal valor = BigDecimal.ZERO;

	@Column(name = "valor_apagar", precision = 12, scale = 2)
	private BigDecimal valorApagar = BigDecimal.ZERO;

	@Column(name = "valor_pago", precision = 12, scale = 2)
	private BigDecimal valorPago = BigDecimal.ZERO;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "conta_apagar_movimentacao", joinColumns = @JoinColumn(name = "conta_apagar_id"), inverseJoinColumns = @JoinColumn(name = "movimentacao_id"))
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cobranca", length = 40)
	private TipoCobranca tipoCobranca;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(length = 20)
	private String status;

	@Column(name = "movimentancao_vinculo")
	private Long agrupadorMovimentacao;

	@Transient
	private int dias;

	@Transient
	private int numVezes = 1;

	@Transient
	private int periodo = 30;

	@Transient
	private BigDecimal saldoDevedor = BigDecimal.ZERO;

	@Transient
	private BigDecimal multaTB = BigDecimal.ZERO;

	@Transient
	private BigDecimal descTB = BigDecimal.ZERO;

	@Transient
	private BigDecimal pagoTB = BigDecimal.ZERO;

	@Transient
	BigDecimal totalGeralDeParcelas = BigDecimal.ZERO;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVinculo() {
		return vinculo;
	}

	public void setVinculo(Long vinculo) {
		this.vinculo = vinculo;
	}

	public Date getDataVencto() {
		return dataVencto;
	}

	public void setDataVencto(Date dataVencto) {
		this.dataVencto = dataVencto;
	}

	public String getNumDoc() {
		return numDoc;
	}

	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc == null ? null : numDoc.toUpperCase();
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

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
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

	public BigDecimal getValorApagar() {
		return valorApagar;
	}

	public void setValorApagar(BigDecimal valorApagar) {
		this.valorApagar = valorApagar;
	}

	public Date getDataDoc() {
		return dataDoc;
	}

	public void setDataDoc(Date dataDoc) {
		this.dataDoc = dataDoc;
	}

	public BigDecimal getSaldoDevedor() {
		return saldoDevedor;
	}

	public void setSaldoDevedor(BigDecimal saldoDevedor) {
		this.saldoDevedor = saldoDevedor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getMultaTB() {
		return multaTB;
	}

	public void setMultaTB(BigDecimal multaTB) {
		this.multaTB = multaTB;
	}

	public BigDecimal getDescTB() {
		return descTB;
	}

	public void setDescTB(BigDecimal descTB) {
		this.descTB = descTB;
	}

	public BigDecimal getPagoTB() {
		return pagoTB;
	}

	public void setPagoTB(BigDecimal pagoTB) {
		this.pagoTB = pagoTB;
	}

	public Long getAgrupadorMovimentacao() {
		return agrupadorMovimentacao;
	}

	public void setAgrupadorMovimentacao(Long agrupadorMovimentacao) {
		this.agrupadorMovimentacao = agrupadorMovimentacao;
	}

	public BigDecimal getTotalGeralDeParcelas() {
		return totalGeralDeParcelas;
	}

	public void setTotalGeralDeParcelas(BigDecimal totalGeralDeParcelas) {
		this.totalGeralDeParcelas = totalGeralDeParcelas;
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
		} else if (!id.equals(other.id)) {
			return false;
		} else if (id.equals(null) && other.id.equals(null)) {
			if (!this.getDataVencto().equals(other.getDataVencto()) || !this.getNumDoc().equals(other.getNumDoc())
					|| !this.getValor().equals(other.getValor())) {
				return false;
			}
			return true;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

}

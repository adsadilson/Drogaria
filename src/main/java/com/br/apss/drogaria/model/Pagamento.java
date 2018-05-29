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
import com.br.apss.drogaria.enums.TipoBaixa;

@Entity
@Table(name = "pagamento")
@SequenceGenerator(name = "PAGAMENTO_ID", sequenceName = "PAGAMENTO_SEQ", allocationSize = 1, initialValue = 1)
public class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PAGAMENTO_ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_lanc", length = 10)
	private Date dataLanc;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_pago", length = 10)
	private Date dataPago;

	@Column(name = "valor", precision = 12, scale = 2)
	private BigDecimal valor = BigDecimal.ZERO;

	@Column(name = "valor_multa_juros", precision = 12, scale = 2)
	private BigDecimal valorMultaJuros = BigDecimal.ZERO;

	@Column(name = "valor_desc", precision = 12, scale = 2)
	private BigDecimal valorDesc = BigDecimal.ZERO;

	@Column(name = "valor_apagar", precision = 12, scale = 2)
	private BigDecimal valorAPagar = BigDecimal.ZERO;

	@Column(name = "valor_pago", precision = 12, scale = 2)
	private BigDecimal valorPago = BigDecimal.ZERO;

	@ManyToMany
	@JoinTable(name = "pagamento_conta_apagar", joinColumns = @JoinColumn(name = "pagamento_id"), inverseJoinColumns = @JoinColumn(name = "conta_apagar_id"))
	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "pagamento_movimentacao", joinColumns = @JoinColumn(name = "pagamento_id"), inverseJoinColumns = @JoinColumn(name = "movimentacao_id"))
	private List<Movimentacao> listaMovimentacoes = new ArrayList<Movimentacao>();

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_baixa", length = 25)
	private TipoBaixa tipoBaixa;

	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_baixa", length = 35)
	private FormaBaixa formaBaixa;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Transient
	private PlanoConta conta;

	private Long vinculo;

	@Column(name = "conta_apagar_vinculo")
	private Long agrupadorContaApagar;

	@ManyToOne
	@JoinColumn(name = "origem")
	private Pagamento origem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataLanc() {
		return dataLanc;
	}

	public void setDataLanc(Date dataLanc) {
		this.dataLanc = dataLanc;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorMultaJuros() {
		return valorMultaJuros;
	}

	public void setValorMultaJuros(BigDecimal valorMultaJuros) {
		this.valorMultaJuros = valorMultaJuros;
	}

	public BigDecimal getValorDesc() {
		return valorDesc;
	}

	public void setValorDesc(BigDecimal valorDesc) {
		this.valorDesc = valorDesc;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public List<ContaAPagar> getListaContaAPagars() {
		return listaContaAPagars;
	}

	public void setListaContaAPagars(List<ContaAPagar> listaContaAPagars) {
		this.listaContaAPagars = listaContaAPagars;
	}

	public List<Movimentacao> getListaMovimentacoes() {
		return listaMovimentacoes;
	}

	public void setListaMovimentacoes(List<Movimentacao> listaMovimentacoes) {
		this.listaMovimentacoes = listaMovimentacoes;
	}

	public Date getDataPago() {
		return dataPago;
	}

	public void setDataPago(Date dataPago) {
		this.dataPago = dataPago;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoBaixa getTipoBaixa() {
		return tipoBaixa;
	}

	public void setTipoBaixa(TipoBaixa tipoBaixa) {
		this.tipoBaixa = tipoBaixa;
	}

	public FormaBaixa getFormaBaixa() {
		return formaBaixa;
	}

	public void setFormaBaixa(FormaBaixa formaBaixa) {
		this.formaBaixa = formaBaixa;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getValorAPagar() {
		return valorAPagar;
	}

	public void setValorAPagar(BigDecimal valorAPagar) {
		this.valorAPagar = valorAPagar;
	}

	public boolean isInclusao() {
		return this.getId() == null;
	}

	public PlanoConta getConta() {
		return conta;
	}

	public void setConta(PlanoConta conta) {
		this.conta = conta;
	}

	public Long getVinculo() {
		return vinculo;
	}

	public void setVinculo(Long vinculo) {
		this.vinculo = vinculo;
	}

	public Long getAgrupadorContaApagar() {
		return agrupadorContaApagar;
	}

	public void setAgrupadorContaApagar(Long agrupadorContaApagar) {
		this.agrupadorContaApagar = agrupadorContaApagar;
	}

	public Pagamento getOrigem() {
		return origem;
	}

	public void setOrigem(Pagamento origem) {
		this.origem = origem;
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
		Pagamento other = (Pagamento) obj;
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

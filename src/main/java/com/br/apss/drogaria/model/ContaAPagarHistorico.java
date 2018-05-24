package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "conta_apagar_historico")
@SequenceGenerator(name = "CONTA_APAGAR_HISTORICO_ID", sequenceName = "CONTA_APAGAR_HISTORICO_SEQ", allocationSize = 1, initialValue = 1)
public class ContaAPagarHistorico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CONTA_APAGAR_HISTORICO_ID")
	private Long id;

	@Column(name = "valor_anterio", precision = 12, scale = 2)
	private BigDecimal valorAnterio = BigDecimal.ZERO;

	@Column(name = "valor_atual", precision = 12, scale = 2)
	private BigDecimal valorAtual = BigDecimal.ZERO;

	@Column(name = "valor_pago", precision = 12, scale = 2)
	private BigDecimal valorPago = BigDecimal.ZERO;

	@Column(name = "valor_multa_juros", precision = 12, scale = 2)
	private BigDecimal valorMultaJuros = BigDecimal.ZERO;

	@Column(name = "valor_desc", precision = 12, scale = 2)
	private BigDecimal valorDesc = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "conta_apagar_id")
	private ContaAPagar contaApagar;

	@ManyToOne
	@JoinColumn(name = "pagamento_id")
	private Pagamento pagamento;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(name = "pagamento_vinculo")
	private Long agrupadorPagamento;

	@Column(name = "pagamento_vinculo_anterior")
	private Long vinculoAnterio;

	@Column(length = 20)
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAgrupadorPagamento() {
		return agrupadorPagamento;
	}

	public void setAgrupadorPagamento(Long agrupadorPagamento) {
		this.agrupadorPagamento = agrupadorPagamento;
	}

	public BigDecimal getValorAnterio() {
		return valorAnterio;
	}

	public void setValorAnterio(BigDecimal valorAnterio) {
		this.valorAnterio = valorAnterio;
	}

	public BigDecimal getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}

	public ContaAPagar getContaApagar() {
		return contaApagar;
	}

	public void setContaApagar(ContaAPagar contaApagar) {
		this.contaApagar = contaApagar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getVinculoAnterio() {
		return vinculoAnterio;
	}

	public void setVinculoAnterio(Long vinculoAnterio) {
		this.vinculoAnterio = vinculoAnterio;
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

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		ContaAPagarHistorico other = (ContaAPagarHistorico) obj;
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

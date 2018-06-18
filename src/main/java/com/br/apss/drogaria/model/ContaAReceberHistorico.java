package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conta_areceber_historico")
@SequenceGenerator(name = "CONTA_ARECEBER_HISTORICO_ID", sequenceName = "CONTA_ARECEBER_HISTORICO_SEQ", allocationSize = 1, initialValue = 1)
public class ContaAReceberHistorico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CONTA_ARECEBER_HISTORICO_ID")
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	@ManyToOne
	@JoinColumn(name = "conta_areceber_id")
	private ContaAReceber contaAReceber;

	@Column(name = "recebimento_id")
	private Long recebimento;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(name = "recebimento_vinculo")
	private Long agrupadorRecebimento;

	@Column(name = "recebimento_vinculo_anterior")
	private Long vinculoAnterio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public ContaAReceber getContaAReceber() {
		return contaAReceber;
	}

	public void setContaAReceber(ContaAReceber contaAReceber) {
		this.contaAReceber = contaAReceber;
	}

	public Long getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Long recebimento) {
		this.recebimento = recebimento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getAgrupadorRecebimento() {
		return agrupadorRecebimento;
	}

	public void setAgrupadorRecebimento(Long agrupadorRecebimento) {
		this.agrupadorRecebimento = agrupadorRecebimento;
	}

	public Long getVinculoAnterio() {
		return vinculoAnterio;
	}

	public void setVinculoAnterio(Long vinculoAnterio) {
		this.vinculoAnterio = vinculoAnterio;
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
		ContaAReceberHistorico other = (ContaAReceberHistorico) obj;
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

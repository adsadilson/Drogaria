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

	@ManyToOne
	@JoinColumn(name = "conta_arecebr_id")
	private ContaAReceber contaAReceber;

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

	public Long getAgrupadorRecebimento() {
		return agrupadorRecebimento;
	}

	public void setAgrupadorRecebimento(Long agrupadorRecebimento) {
		this.agrupadorRecebimento = agrupadorRecebimento;
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

	public ContaAReceber getContaAReceber() {
		return contaAReceber;
	}

	public void setContaAReceber(ContaAReceber contaAReceber) {
		this.contaAReceber = contaAReceber;
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

package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.br.apss.drogaria.enums.TipoCartao;

@Entity
@Table(name = "adm_cartao")
@SequenceGenerator(name = "ADM_CARTAO_ID", sequenceName = "ADM_CARTAO_SEQ", allocationSize = 1, initialValue = 11)
public class AdmCartao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ADM_CARTAO_ID")
	private Long id;

	@Column(nullable = false, length = 155)
	private String nome;

	@Column(nullable = false, length = 155)
	private String operadora;

	@Column(name = "tipo_cartao", length = 10)
	@Enumerated(EnumType.STRING)
	private TipoCartao tipoCartao;

	@Column(name = "taxa", precision = 12, scale = 2)
	private BigDecimal taxa = BigDecimal.ZERO;

	@Column(name = "prazo")
	private Integer prazo = 1;

	@Column(name = "parcela")
	private Integer parcela = 1;

	@ManyToOne
	@JoinColumn(name = "conta_creditar", nullable = false)
	private PlanoConta contaCreditar;

	private Boolean status = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	public TipoCartao getTipoCartao() {
		return tipoCartao;
	}

	public void setTipoCartao(TipoCartao tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public BigDecimal getTaxa() {
		return taxa;
	}

	public void setTaxa(BigDecimal taxa) {
		this.taxa = taxa;
	}

	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public PlanoConta getContaCreditar() {
		return contaCreditar;
	}

	public void setContaCreditar(PlanoConta contaCreditar) {
		this.contaCreditar = contaCreditar;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public boolean isInclusao() {
		return this.getId() == null;
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
		AdmCartao other = (AdmCartao) obj;
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

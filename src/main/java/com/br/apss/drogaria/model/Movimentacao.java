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
import javax.persistence.Transient;

@Entity
@Table(name = "movimentacao")
@SequenceGenerator(name = "MOVIMENTACAO_ID", sequenceName = "MOVIMENTACAO_SEQ", allocationSize = 1)
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "MOVIMENTACAO_ID")
	private Long id;

	@Column(name = "descricao", nullable = true, length = 80)
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_lanc", length = 10)
	private Date dataLanc;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_doc", length = 10)
	private Date dataDoc;

	@ManyToOne
	@JoinColumn(name = "conta_id")
	private PlanoConta planoConta = new PlanoConta();

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Column(name = "documento", length = 15)
	private String documento;

	@Column(name = "vlr_entrada", precision = 12, scale = 2)
	private BigDecimal vlrEntrada = BigDecimal.ZERO;

	@Transient
	private BigDecimal vlrSaldo = BigDecimal.ZERO;

	@Column(name = "vlr_saida", precision = 12, scale = 2)
	private BigDecimal vlrSaida;

	private Long vinculo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento == null ? null : documento.toUpperCase();
	}

	public BigDecimal getVlrEntrada() {
		return vlrEntrada;
	}

	public void setVlrEntrada(BigDecimal vlrEntrada) {
		this.vlrEntrada = vlrEntrada;
	}

	public BigDecimal getVlrSaida() {
		return vlrSaida;
	}

	public void setVlrSaida(BigDecimal vlrSaida) {
		this.vlrSaida = vlrSaida;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public Long getVinculo() {
		return vinculo;
	}

	public void setVinculo(Long vinculo) {
		this.vinculo = vinculo;
	}

	public BigDecimal getVlrSaldo() {
		return vlrSaldo;
	}

	public void setVlrSaldo(BigDecimal vlrSaldo) {
		this.vlrSaldo = vlrSaldo;
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
		Movimentacao other = (Movimentacao) obj;
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

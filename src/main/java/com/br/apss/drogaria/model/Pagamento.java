package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	@Column(name = "data_pagto", length = 10)
	private Date dataPagto;
	
	@Column(name = "valor", precision = 12, scale = 2)
	private BigDecimal valor = BigDecimal.ZERO;
	
	@Column(name = "valor_multa_juros", precision = 12, scale = 2)
	private BigDecimal valorMultaJuros = BigDecimal.ZERO;
	
	@Column(name = "valor_desc", precision = 12, scale = 2)
	private BigDecimal valorDesc = BigDecimal.ZERO;
	
	@Column(name = "valor_pago", precision = 12, scale = 2)
	private BigDecimal valorPago = BigDecimal.ZERO;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

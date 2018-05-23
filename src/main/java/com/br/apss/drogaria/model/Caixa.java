package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "caixa")
@SequenceGenerator(name = "CAIXA_ID", sequenceName = "CAIXA_SEQ", allocationSize = 1, initialValue = 1)
public class Caixa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CAIXA_ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Date data;

	@Column(name = "turno")
	private int turno;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "abertura")
	private Date abertura;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fechamento")
	private Date fechamento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "conferencia")
	private Date conferencia;

	@OneToOne
	@JoinColumn(name = "responsavel_id")
	private Pessoa responsavel;

	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Pessoa usuario;

	@OneToOne
	@JoinColumn(name = "conferente_id")
	private Pessoa conferente;

	private String status;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Caixa other = (Caixa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getAbertura() {
		return abertura;
	}

	public Date getConferencia() {
		return conferencia;
	}

	public Pessoa getConferente() {
		return conferente;
	}

	public Date getData() {
		return data;
	}

	public Date getFechamento() {
		return fechamento;
	}

	public Long getId() {
		return id;
	}

	public Pessoa getResponsavel() {
		return responsavel;
	}

	public String getStatus() {
		return status;
	}

	public int getTurno() {
		return turno;
	}

	public Pessoa getUsuario() {
		return usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setAbertura(Date abertura) {
		this.abertura = abertura;
	}

	public void setConferencia(Date conferencia) {
		this.conferencia = conferencia;
	}

	public void setConferente(Pessoa conferente) {
		this.conferente = conferente;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setFechamento(Date fechamento) {
		this.fechamento = fechamento;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setResponsavel(Pessoa responsavel) {
		this.responsavel = responsavel;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public void setUsuario(Pessoa usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

}

package com.br.apss.drogaria.model;

import java.io.Serializable;

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

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;

@Entity
@Table(name = "plano_conta")
@SequenceGenerator(name = "PLANO_CONTA_ID", sequenceName = "PLANO_CONTA_SEQ", allocationSize = 1, initialValue = 26)
public class PlanoConta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PLANO_CONTA_ID")
	private Long id;

	@Column(name = "nome", length = 80)
	private String nome;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private TipoConta tipo;

	@Column(name = "status", length = 1)
	private boolean status = true;

	@Column(name = "mascara", length = 30)
	private String mascara;

	@ManyToOne
	@JoinColumn(name = "conta_pai_id")
	private PlanoConta contaPai;

	@Enumerated(EnumType.STRING)
	@Column(name = "categoria", length = 30)
	private TipoRelatorio categoria;

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
		this.nome = nome.toUpperCase();
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public TipoConta getTipo() {
		return tipo;
	}

	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public PlanoConta getContaPai() {
		return contaPai;
	}

	public void setContaPai(PlanoConta contaPai) {
		this.contaPai = contaPai;
	}

	public TipoRelatorio getCategoria() {
		return categoria;
	}

	public void setCategoria(TipoRelatorio categoria) {
		this.categoria = categoria;
	}

	public boolean isInclusao() {
		return getId() == null ? true : false;
	}

	public boolean isEditando() {
		return !isInclusao();
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
		PlanoConta other = (PlanoConta) obj;
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

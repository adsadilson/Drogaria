package com.br.apss.drogaria.model;

import java.io.Serializable;

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
@Table(name = "permissao")
@SequenceGenerator(name = "PERMISSAO_ID", sequenceName = "PERMISSAO_SEQ", allocationSize = 1)
public class Permissao implements Serializable, Comparable<Permissao> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PERMISSAO_ID")
	private Long id;

	@Column(nullable = false, length = 1)
	private Boolean incluir = false;

	@Column(nullable = false, length = 1)
	private Boolean alterar = false;

	@Column(nullable = false, length = 1)
	private Boolean excluir = false;

	@Column(nullable = false, length = 1)
	private Boolean visualizar = false;

	@Column(nullable = false, length = 1)
	private Boolean imprimir = false;

	@ManyToOne
	@JoinColumn(name = "grupo_usuario_id", nullable = false)
	private GrupoUsuario grupoUsuario;

	@ManyToOne
	@JoinColumn(name = "controle_menu_id", nullable = false)
	private ControleMenu controleMenu;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIncluir() {
		return incluir;
	}

	public void setIncluir(Boolean incluir) {
		this.incluir = incluir;
	}

	public Boolean getAlterar() {
		return alterar;
	}

	public void setAlterar(Boolean alterar) {
		this.alterar = alterar;
	}

	public Boolean getExcluir() {
		return excluir;
	}

	public void setExcluir(Boolean excluir) {
		this.excluir = excluir;
	}

	public Boolean getVisualizar() {
		return visualizar;
	}

	public void setVisualizar(Boolean visualizar) {
		this.visualizar = visualizar;
	}

	public Boolean getImprimir() {
		return imprimir;
	}

	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public ControleMenu getControleMenu() {
		return controleMenu;
	}

	public void setControleMenu(ControleMenu controleMenu) {
		this.controleMenu = controleMenu;
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
		Permissao other = (Permissao) obj;
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

	@Override
	public int compareTo(Permissao o) {
		 if (this.getControleMenu().getId() < o.id) {
	            return -1;
	        }
	        if (this.getControleMenu().getId() > o.id) {
	            return 1;
	        }
	        return 0;
	}

}

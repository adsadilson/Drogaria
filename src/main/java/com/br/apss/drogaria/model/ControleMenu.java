package com.br.apss.drogaria.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "controle_menu")
@SequenceGenerator(name = "CONTROLE_MENU_ID", sequenceName = "CONTROLE_MENU_SEQ", allocationSize = 1, initialValue = 1)
public class ControleMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CONTROLE_MENU_ID")
	private Long id;

	@Column(length = 250)
	private String url;

	@Column(length = 150)
	private String formulario;

	@Column(length = 150)
	private String funcao;

	@Column(length = 150)
	private String menu;

	@Column(length = 150)
	private String submenu;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFormulario() {
		return formulario;
	}

	public void setFormulario(String formulario) {
		this.formulario = formulario;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao.toUpperCase();
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getSubmenu() {
		return submenu;
	}

	public void setSubmenu(String submenu) {
		this.submenu = submenu;
	}

	public boolean isInclusao() {
		return this.getId() == null;
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
		ControleMenu other = (ControleMenu) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

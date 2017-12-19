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
@Table(name = "permissao_grupo")
@SequenceGenerator(name = "PERMISSAO_GRUPO_ID", sequenceName = "PERMISSAO_GRUPO_SEQ", allocationSize = 1)
public class PermissaoGrupo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PERMISSAO_GRUPO_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "grupo_usuario_id")
	private GrupoUsuario grupoUsuario;

	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;

	@Column(nullable = false, length = 1)
	private Boolean incluir = false;

	@Column(nullable = false, length = 1)
	private Boolean alterar = false;

	@Column(nullable = false, length = 1)
	private Boolean salvar = false;

	@Column(nullable = false, length = 1)
	private Boolean excluir = false;

	@Column(nullable = false, length = 1)
	private Boolean visualizar = false;

	@Column(nullable = false, length = 1)
	private Boolean imprimir = false;

	@Column(nullable = false, length = 1)
	private Boolean exportar = false;

	@Column(nullable = false, length = 1)
	private Boolean importar = false;

	@Column(nullable = false, length = 1)
	private Boolean pesquisar = false;

	@Column(nullable = false, length = 1)
	private Boolean acessar = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
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

	public Boolean getSalvar() {
		return salvar;
	}

	public void setSalvar(Boolean salvar) {
		this.salvar = salvar;
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

	public Boolean getExportar() {
		return exportar;
	}

	public void setExportar(Boolean exportar) {
		this.exportar = exportar;
	}

	public Boolean getImportar() {
		return importar;
	}

	public void setImportar(Boolean importar) {
		this.importar = importar;
	}

	public Boolean getPesquisar() {
		return pesquisar;
	}

	public void setPesquisar(Boolean pesquisar) {
		this.pesquisar = pesquisar;
	}

	public Boolean getAcessar() {
		return acessar;
	}

	public void setAcessar(Boolean acessar) {
		this.acessar = acessar;
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
		PermissaoGrupo other = (PermissaoGrupo) obj;
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

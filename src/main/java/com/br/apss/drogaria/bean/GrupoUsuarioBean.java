package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.GrupoUsuario;
import com.br.apss.drogaria.model.filter.GrupoUsuarioFilter;
import com.br.apss.drogaria.service.GrupoUsuarioService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class GrupoUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private GrupoUsuario grupoUsuario = new GrupoUsuario();

	private GrupoUsuario grupoUsuarioSelecionado;

	private GrupoUsuarioFilter filtro = new GrupoUsuarioFilter();

	private List<GrupoUsuario> grupoUsuarios = new ArrayList<GrupoUsuario>();

	@Inject
	private GrupoUsuarioService grupoUsuarioService;

	public void inicializar() {
		if (this.grupoUsuario == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		GrupoUsuario grupoUsuarioExistente = grupoUsuarioService.porNome(grupoUsuario.getNome());
		if (grupoUsuarioExistente != null && !grupoUsuarioExistente.equals(grupoUsuario)) {
			throw new NegocioException("Já existe um Grupo de Usuário com esse nome informado.");
		}

		grupoUsuarioService.salvar(grupoUsuario);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		novo();
		pesquisar();
	}

	public void novo() {
		this.grupoUsuario = new GrupoUsuario();
	}

	public void novoFiltro() {
		this.filtro = new GrupoUsuarioFilter();
	}

	public void pesquisar() {
		this.grupoUsuarios = grupoUsuarioService.filtrados(filtro);
	}

	public void preparEdicao() {
		this.grupoUsuario = this.grupoUsuarioSelecionado;
	}

	public void excluir() {
		grupoUsuarioService.excluir(grupoUsuarioSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public GrupoUsuarioFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(GrupoUsuarioFilter filtro) {
		this.filtro = filtro;
	}

	public List<GrupoUsuario> getGrupoUsuarios() {
		return grupoUsuarios;
	}

	public void setGrupoUsuarios(List<GrupoUsuario> grupoUsuarios) {
		this.grupoUsuarios = grupoUsuarios;
	}

	public GrupoUsuario getGrupoUsuarioSelecionado() {
		return grupoUsuarioSelecionado;
	}

	public void setGrupoUsuarioSelecionado(GrupoUsuario grupoUsuarioSelecionado) {
		this.grupoUsuarioSelecionado = grupoUsuarioSelecionado;
	}

}

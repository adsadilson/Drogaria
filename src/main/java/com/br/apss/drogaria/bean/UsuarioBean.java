package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.UsuarioFilter;
import com.br.apss.drogaria.service.UsuarioService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();

	private Usuario usuarioSelecionado;

	private UsuarioFilter filtro = new UsuarioFilter();

	private List<Usuario> usuarios = new ArrayList<Usuario>();

	@Inject
	private UsuarioService usuarioService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.usuario == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Usuario usuarioExistente = usuarioService.porEmail(usuario.getEmail());
		if (usuarioExistente != null && !usuarioExistente.equals(usuario)) {
			throw new NegocioException("J� existe um Usu�rio com esse e-mail informado.");
		}

		usuarioService.salvar(usuario);
		Messages.addGlobalInfo("Registro salvor com sucesso.");

		novo();
		pesquisar();
	}

	public void novo() {
		usuario = new Usuario();
	}

	public void novoFiltro() {
		this.filtro = new UsuarioFilter();
	}

	public void pesquisar() {
		this.usuarios = usuarioService.filtrados(filtro);
	}

	public void preparEdicao() {
		this.usuario = usuarioService.porId(this.usuarioSelecionado.getId());
	}

	public void excluir() {
		usuarioService.excluir(usuarioSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	/******************** Getters e Setters ***************************/

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(UsuarioFilter filtro) {
		this.filtro = filtro;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

}

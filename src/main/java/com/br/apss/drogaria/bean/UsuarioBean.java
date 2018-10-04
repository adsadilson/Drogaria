package com.br.apss.drogaria.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import com.br.apss.drogaria.model.GrupoUsuario;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.UsuarioFilter;
import com.br.apss.drogaria.relatorio.Relatorio;
import com.br.apss.drogaria.service.GrupoUsuarioService;
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

	private List<GrupoUsuario> listaGrupos = new ArrayList<GrupoUsuario>();

	private GrupoUsuario grupoUsuario = new GrupoUsuario();

	private GrupoUsuario grupoSelecionado = new GrupoUsuario();

	@Inject
	private Relatorio relat;

	private List<Usuario> list = null;

	@Inject
	private GrupoUsuarioService grupoUsuarioService;

	@Inject
	private UsuarioService usuarioService;

	private boolean aprovado = true;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.usuario == null) {
			novo();
		}
		pesquisar();
		this.listaGrupos = grupoUsuarioService.listarTodos();
	}

	public void gerarRelatUser() throws IOException {
		list = usuarioService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Usuários");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_ordenacao", filtro.getCampoOrdenacao());
		String caminho = "/relatorios/reportUsuario.jrxml";
		relat.gerarRelatorio(caminho, "Lista De Usuários", par, list);
	}

	public void salvar() {

		Usuario usuarioExistente = usuarioService.porEmail(usuario.getEmail());
		if (usuarioExistente != null && !usuarioExistente.equals(usuario)) {
			this.aprovado = false;
			throw new NegocioException("Já existe um Usuário com esse e-mail informado.");
		}

		if (this.usuario.getGrupos().size() < 1) {
			this.aprovado = false;
			throw new NegocioException("Selecione pelo menos um grupo de usuário.");

		}

		if (aprovado) {
			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
			usuarioService.salvar(usuario);
			Messages.addGlobalInfo("Registro salvor com sucesso.");
			novo();
			pesquisar();
		}
	}

	public void trocaSenha() {
		SimpleHash hash = new SimpleHash("md5", this.usuario.getSenha());
		Usuario usuarioExistente = usuarioService.porEmail(this.usuario.getEmail());
		if (!hash.toHex().equals(usuarioExistente.getSenha())) {
			this.aprovado = false;
			throw new NegocioException("Senha atual invalida.");
		} else {
			this.aprovado = true;
		}

		if (aprovado) {
			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
			this.usuario.setSenha(this.usuario.getNovaSenha());
			usuarioService.salvar(usuario);
			Messages.addGlobalInfo("Senha alterada com sucesso.");
			novo();
			pesquisar();
		}
	}

	public void editar() {
		this.usuario = usuarioService.porId(this.usuarioSelecionado.getId());
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

	public void adicionarGrupo() {
		if (this.grupoUsuario != null) {
			if (this.usuario.getGrupos().contains(this.grupoUsuario)) {
				throw new NegocioException("Grupo já cadastrado.");
			}
			this.usuario.getGrupos().add(this.grupoUsuario);
			this.grupoUsuario = null;
		} else {
			Messages.addGlobalWarn("Selecione um grupo primeiro.");
		}
	}

	public void removeGrupo() {
		this.usuario.getGrupos().remove(this.grupoSelecionado);
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

	public List<GrupoUsuario> getListaGrupos() {
		return listaGrupos;
	}

	public void setListaGrupos(List<GrupoUsuario> listaGrupos) {
		this.listaGrupos = listaGrupos;
	}

	public GrupoUsuario getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(GrupoUsuario grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public GrupoUsuario getGrupoUsuario() {
		return grupoUsuario;
	}

	public void setGrupoUsuario(GrupoUsuario grupoUsuario) {
		this.grupoUsuario = grupoUsuario;
	}

	public List<Usuario> getList() {
		return list;
	}

	public void setList(List<Usuario> list) {
		this.list = list;
	}

}

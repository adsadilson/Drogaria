package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.PermissaoGrupo;
import com.br.apss.drogaria.model.filter.PermissaoGrupoUserFilter;
import com.br.apss.drogaria.service.PermissaoGrupoService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class PermissaoGrupoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PermissaoGrupo permissaoGrupo = new PermissaoGrupo();

	private PermissaoGrupo permissaoGrupoSelecionado;

	private PermissaoGrupoUserFilter filtro = new PermissaoGrupoUserFilter();

	private List<PermissaoGrupo> permissaoGrupos = new ArrayList<PermissaoGrupo>();

	@Inject
	private PermissaoGrupoService permissaoGrupoService;
	
	
	/**************** Metodos ****************/

	public void inicializar() {
		if (this.permissaoGrupo == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {


		permissaoGrupoService.salvar(permissaoGrupo);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		novo();
		pesquisar();
	}

	public void novo() {
		this.permissaoGrupo = new PermissaoGrupo();
	}

	public void novoFiltro() {
		this.filtro = new PermissaoGrupoUserFilter();
	}

	public void pesquisar() {
		this.permissaoGrupos = permissaoGrupoService.filtrados(filtro);
	}

	public void preparEdicao() {
		this.permissaoGrupo = this.permissaoGrupoSelecionado;
	}

	public void excluir() {
		permissaoGrupoService.excluir(permissaoGrupoSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	/**************** Getters e Setters ****************/

	public PermissaoGrupo getPermissaoGrupo() {
		return permissaoGrupo;
	}

	public void setPermissaoGrupo(PermissaoGrupo permissaoGrupo) {
		this.permissaoGrupo = permissaoGrupo;
	}

	public PermissaoGrupo getPermissaoGrupoSelecionado() {
		return permissaoGrupoSelecionado;
	}

	public void setPermissaoGrupoSelecionado(PermissaoGrupo permissaoGrupoSelecionado) {
		this.permissaoGrupoSelecionado = permissaoGrupoSelecionado;
	}

	public PermissaoGrupoUserFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(PermissaoGrupoUserFilter filtro) {
		this.filtro = filtro;
	}

	public List<PermissaoGrupo> getPermissaoGrupos() {
		return permissaoGrupos;
	}

	public void setPermissaoGrupos(List<PermissaoGrupo> permissaoGrupos) {
		this.permissaoGrupos = permissaoGrupos;
	}

}

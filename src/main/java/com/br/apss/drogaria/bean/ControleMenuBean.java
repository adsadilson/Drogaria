package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.ControleMenu;
import com.br.apss.drogaria.model.filter.ControleMenuFilter;
import com.br.apss.drogaria.service.ControleMenuService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class ControleMenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ControleMenu menu = new ControleMenu();

	private ControleMenu menuSelecionado;

	private ControleMenuFilter filtro = new ControleMenuFilter();

	private List<ControleMenu> menus = new ArrayList<ControleMenu>();

	@Inject
	private ControleMenuService menuService;

	public void inicializar() {
		if (this.menu == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		ControleMenu menuExistente = menuService.porNome(menu.getFormulario());
		if (menuExistente != null && !menuExistente.equals(menu)) {
			throw new NegocioException("J� existe um Menu com esse nome informado.");
		}

		menuService.salvar(menu);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		novo();
		pesquisar();
	}

	public void novo() {
		this.menu = new ControleMenu();
	}

	public void novoFiltro() {
		this.filtro = new ControleMenuFilter();
	}

	public void pesquisar() {
		this.menus = menuService.filtrados(filtro);
	}

	public void preparEdicao() {
		this.menu = this.menuSelecionado;
	}

	public void excluir() {
		menuService.excluir(menuSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public ControleMenu getControleMenu() {
		return menu;
	}

	public void setControleMenu(ControleMenu menu) {
		this.menu = menu;
	}

	public ControleMenuFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(ControleMenuFilter filtro) {
		this.filtro = filtro;
	}

	public List<ControleMenu> getControleMenus() {
		return menus;
	}

	public void setControleMenus(List<ControleMenu> menus) {
		this.menus = menus;
	}

	public ControleMenu getControleMenuSelecionado() {
		return menuSelecionado;
	}

	public void setControleMenuSelecionado(ControleMenu menuSelecionado) {
		this.menuSelecionado = menuSelecionado;
	}

}

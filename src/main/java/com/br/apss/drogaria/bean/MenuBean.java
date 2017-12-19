package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.Menu;
import com.br.apss.drogaria.model.filter.MenuFilter;
import com.br.apss.drogaria.service.MenuService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Menu menu = new Menu();

	private Menu menuSelecionado;

	private MenuFilter filtro = new MenuFilter();

	private List<Menu> menus = new ArrayList<Menu>();

	@Inject
	private MenuService menuService;

	public void inicializar() {
		if (this.menu == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Menu menuExistente = menuService.porNome(menu.getFormulario());
		if (menuExistente != null && !menuExistente.equals(menu)) {
			throw new NegocioException("Já existe um Menu com esse nome informado.");
		}

		menuService.salvar(menu);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		novo();
		pesquisar();
	}

	public void novo() {
		this.menu = new Menu();
	}

	public void novoFiltro() {
		this.filtro = new MenuFilter();
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public MenuFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(MenuFilter filtro) {
		this.filtro = filtro;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Menu getMenuSelecionado() {
		return menuSelecionado;
	}

	public void setMenuSelecionado(Menu menuSelecionado) {
		this.menuSelecionado = menuSelecionado;
	}

}

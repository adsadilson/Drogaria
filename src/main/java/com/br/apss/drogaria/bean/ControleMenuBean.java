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

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.ControleMenu;
import com.br.apss.drogaria.model.filter.ControleMenuFilter;
import com.br.apss.drogaria.relatorio.Relatorio;
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
	private Relatorio relat;

	private List<ControleMenu> list = null;

	@Inject
	private ControleMenuService menuService;

	public void inicializar() {
		if (this.menu == null) {
			novo();
		}
		pesquisar();
	}

	public void gerarRelatControleMenu() throws IOException {
		list = menuService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Controle de Menu");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_tipo", false);
		par.put("par_ordenacao", filtro.getCampoOrdenacao());
		String caminho = "/relatorios/reportControleMenu.jrxml";
		relat.gerarRelatorio(caminho, "Lista de Controle de Menu", par, list);
	}

	public void salvar() {

		ControleMenu menuExistente = menuService.porNome(menu.getFormulario());
		if (menuExistente != null && !menuExistente.equals(menu)) {
			throw new NegocioException("Já existe um Menu com esse nome informado.");
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

	public List<ControleMenu> getList() {
		return list;
	}

	public void setList(List<ControleMenu> list) {
		this.list = list;
	}

}

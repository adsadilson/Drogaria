package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Menu;
import com.br.apss.drogaria.model.filter.MenuFilter;
import com.br.apss.drogaria.repository.MenuRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class MenuService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MenuRepository dao;

	@Transacional
	public void salvar(Menu obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Menu obj) {
		dao.excluir(obj);
	}

	public List<Menu> filtrados(MenuFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Menu> listarTodos() {
		return dao.listarTodos();
	}

	public Menu porId(Long id) {
		return dao.porId(id);
	}

	public Menu porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(MenuFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

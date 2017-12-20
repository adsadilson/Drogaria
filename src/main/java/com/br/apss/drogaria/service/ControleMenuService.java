package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ControleMenu;
import com.br.apss.drogaria.model.filter.ControleMenuFilter;
import com.br.apss.drogaria.repository.ControleMenuRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ControleMenuService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ControleMenuRepository dao;

	@Transacional
	public void salvar(ControleMenu obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(ControleMenu obj) {
		dao.excluir(obj);
	}

	public List<ControleMenu> filtrados(ControleMenuFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<ControleMenu> listarTodos() {
		return dao.listarTodos();
	}

	public ControleMenu porId(Long id) {
		return dao.porId(id);
	}

	public ControleMenu porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(ControleMenuFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

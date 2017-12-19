package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Categoria;
import com.br.apss.drogaria.model.filter.CategoriaFilter;
import com.br.apss.drogaria.repository.CategoriaRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class CategoriaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaRepository dao;

	@Transacional
	public void salvar(Categoria obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Categoria obj) {
		dao.excluir(obj);
	}

	public List<Categoria> filtrados(CategoriaFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Categoria> listarTodos() {
		return dao.listarTodos();
	}

	public Categoria porId(Long id) {
		return dao.porId(id);
	}

	public Categoria porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(CategoriaFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

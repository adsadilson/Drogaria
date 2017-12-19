package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Categoria;
import com.br.apss.drogaria.model.SubCategoria;
import com.br.apss.drogaria.model.filter.SubCategoriaFilter;
import com.br.apss.drogaria.repository.SubCategoriaRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class SubCategoriaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SubCategoriaRepository dao;

	@Transacional
	public void salvar(SubCategoria tarefa) {
		dao.salvar(tarefa);
	}

	@Transacional
	public void excluir(SubCategoria tarefa) {
		dao.excluir(tarefa);
	}

	public List<SubCategoria> filtrados(SubCategoriaFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<SubCategoria> listarTodos() {
		return dao.listarTodos();
	}

	public SubCategoria porId(Long id) {
		return dao.porId(id);
	}

	public SubCategoria porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(SubCategoriaFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
	
	public List<SubCategoria> porCategoria(Categoria categoria) {
		return dao.porCategoria(categoria);
	}
}

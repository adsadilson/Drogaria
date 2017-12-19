package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.filter.ProdutoFilter;
import com.br.apss.drogaria.repository.ProdutoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoRepository dao;

	@Transacional
	public void salvar(Produto tarefa) {
		dao.salvar(tarefa);
	}

	@Transacional
	public void excluir(Produto tarefa) {
		dao.excluir(tarefa);
	}

	public List<Produto> filtrados(ProdutoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Produto> listarTodos() {
		return dao.listarTodos();
	}

	public Produto porId(Long id) {
		return dao.porId(id);
	}

	public Produto porNome(String nome) {
		return dao.porNome(nome);
	}

	public List<Produto> porListaProdutoPorNome(String nome) {
		return dao.porListaProdutoPorNome(nome);
	}

	public int quantidadeFiltrados(ProdutoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}

	public Produto porCodigoBarra(String codigoBarra) {
		return dao.porCodigoBarra(codigoBarra);
	}
}

package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.CabContaApagar;
import com.br.apss.drogaria.model.filter.CabContaApagarFilter;
import com.br.apss.drogaria.repository.CabContaAPagarRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class CabContaApagarService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CabContaAPagarRepository dao;

	@Transacional
	public void salvar(CabContaApagar tarefa) {
		dao.salvar(tarefa);
	}

	@Transacional
	public void excluir(CabContaApagar tarefa) {
		dao.excluir(tarefa);
	}

	public List<CabContaApagar> filtrados(CabContaApagarFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<CabContaApagar> listarTodos() {
		return dao.listarTodos();
	}

	public CabContaApagar porId(Long id) {
		return dao.porId(id);
	}

	public CabContaApagar porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(CabContaApagarFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

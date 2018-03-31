package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.CabContaAReceber;
import com.br.apss.drogaria.model.filter.CabContaAReceberFilter;
import com.br.apss.drogaria.repository.CabContaAReceberRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class CabContaAReceberService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CabContaAReceberRepository dao;

	@Transacional
	public void salvar(CabContaAReceber tarefa) {
		dao.salvar(tarefa);
	}

	@Transacional
	public void excluir(CabContaAReceber obj) {
		dao.excluir(obj);
	}

	public List<CabContaAReceber> filtrados(CabContaAReceberFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<CabContaAReceber> listarTodos() {
		return dao.listarTodos();
	}

	public CabContaAReceber porId(Long id) {
		return dao.porId(id);
	}

	public CabContaAReceber porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	public CabContaAReceber porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(CabContaAReceberFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

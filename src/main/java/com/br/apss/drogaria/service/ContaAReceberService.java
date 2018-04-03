package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
import com.br.apss.drogaria.repository.ContaAReceberRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAReceberService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAReceberRepository dao;

	@Transacional
	public void salvar(ContaAReceber obj) {
		dao.save(obj);
	}

	@Transacional
	public void excluir(ContaAReceber obj) {
		dao.excluirPorVinculo(obj.getId());
	}

	public List<ContaAReceber> filtrados(ContaAReceberFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<ContaAReceber> listarTodos() {
		return dao.listarTodos();
	}

	public ContaAReceber porId(Long id) {
		return dao.porId(id);
	}

	public ContaAReceber porVinculo(Long vinculo, Long id) {
		return dao.porVinculo(vinculo, id);
	}

	public List<ContaAReceber> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	@Transacional
	public void excluirPorVinculo(Long id) {
		dao.excluirPorVinculo(id);
	}

	public int quantidadeFiltrados(ContaAReceberFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}

	@Transacional
	public void excluirContas(List<ContaAReceber> contas) throws Exception {

	}

}

package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAPagarHistorico;
import com.br.apss.drogaria.repository.ContaAPagarHistoricoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAPagarHistoricoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAPagarHistoricoRepository dao;

	@Transacional
	public void salvar(ContaAPagarHistorico ContaAPagarHistorico) {
		dao.salvar(ContaAPagarHistorico);
	}

	@Transacional
	public List<ContaAPagarHistorico> salvar(List<ContaAPagarHistorico> list) {
		return dao.salvar(list);
	}

	@Transacional
	public void excluir(ContaAPagarHistorico obj) {
		dao.excluir(obj);
	}

	public ContaAPagarHistorico porId(Long id) {
		return dao.porId(id);
	}

	public ContaAPagarHistorico porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	public List<ContaAPagarHistorico> listaVinculo(Long vinculo) {
		return dao.listaVinculo(vinculo);
	}

	public Long maxId(ContaAPagarHistorico cph) {
		return dao.maxID(cph);
	}

}

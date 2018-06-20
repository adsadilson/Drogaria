package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAReceberHistorico;
import com.br.apss.drogaria.repository.ContaAReceberHistoricoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAReceberHistoricoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAReceberHistoricoRepository dao;

	@Transacional
	public void salvar(ContaAReceberHistorico ContaAReceberHistorico) {
		dao.salvar(ContaAReceberHistorico);
	}

	@Transacional
	public List<ContaAReceberHistorico> salvar(List<ContaAReceberHistorico> list) {
		return dao.salvar(list);
	}

	@Transacional
	public void excluir(ContaAReceberHistorico obj) {
		dao.excluir(obj);
	}

	public ContaAReceberHistorico porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	public List<ContaAReceberHistorico> listaVinculo(Long vinculo) {
		return dao.listaVinculo(vinculo);
	}

	public Long maxId(ContaAReceberHistorico cp) {
		return dao.maxID(cp);
	}

	public ContaAReceberHistorico porId(Long cph) {
		return dao.porId(cph);
	}

}

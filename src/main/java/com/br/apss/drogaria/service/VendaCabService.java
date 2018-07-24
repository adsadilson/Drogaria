package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.VendaCab;
import com.br.apss.drogaria.model.filter.VendaCabFilter;
import com.br.apss.drogaria.repository.VendaCabRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class VendaCabService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendaCabRepository dao;

	@Inject
	private ContaAPagarService capService;

	@Transacional
	public VendaCab salvar(VendaCab obj) {
		return dao.salvar(obj);
	}

	@Transacional
	public void excluir(VendaCab obj) {
		dao.excluir(obj);
	}

	public VendaCab porId(Long id) {
		return dao.porId(id);
	}

	public VendaCab porDocumento(String documento) {
		return dao.porDocumento(documento);
	}

	public List<VendaCab> filtrados(VendaCabFilter filtro) {
		return dao.filtrados(filtro);
	}

	public int qtdeFiltrados(VendaCabFilter filtro) {
		return dao.qtdeFiltrados(filtro);
	}

	public VendaCab buscarVendaItens(Long id) {
		return dao.buscarVendaComItens(id);
	}

}

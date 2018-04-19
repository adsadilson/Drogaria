package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.CompraCab;
import com.br.apss.drogaria.model.filter.CompraCabFilter;
import com.br.apss.drogaria.repository.CompraCabRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class CompraCabService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CompraCabRepository dao;

	@Transacional
	public CompraCab salvar(CompraCab obj) {
		return dao.salvar(obj);
	}

	@Transacional
	public void excluir(CompraCab obj) {
		dao.excluir(obj);
	}


	public CompraCab porId(Long id) {
		return dao.porId(id);
	}

	public CompraCab porDocumento(String documento) {
		return dao.porDocumento(documento);
	}


	public List<CompraCab> filtrados(CompraCabFilter filtro) {
		return dao.filtrados(filtro);
	}

	public int qtdeFiltrados(CompraCabFilter filtro) {
		return dao.qtdeFiltrados(filtro);
	}
	
	public CompraCab buscarCompraComItens(Long id) {
		return dao.buscarCompraComItens(id);
	}

}

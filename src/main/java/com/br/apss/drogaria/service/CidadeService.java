package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.model.Cidade;
import com.br.apss.drogaria.model.filter.CidadeFilter;
import com.br.apss.drogaria.repository.CidadeRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class CidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CidadeRepository dao;

	@Transacional
	public void salvar(Cidade obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Cidade obj) {
		dao.excluir(obj);
	}

	public List<Cidade> listAll() {
		return dao.listAll();
	}

	public Cidade porId(Long id) {
		return dao.porId(id);
	}

	public Cidade porNome(String nome) {
		return dao.porNome(nome);
	}

	public Cidade porNomeEstado(String nome, Estado uf) {
		return dao.porNomeEstado(nome, uf);
	}

	public List<Cidade> buscarPorEstado(Estado estado) {
		return dao.buscarPorEstado(estado);
	}

	public List<Cidade> filtrados(CidadeFilter filtro) {
		return dao.filtrados(filtro);
	}
	
	public int qtdeFiltrados(CidadeFilter filtro) {
		return dao.qtdeFiltrados(filtro);
	}

}

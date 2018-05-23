package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Caixa;
import com.br.apss.drogaria.model.filter.CaixaFilter;
import com.br.apss.drogaria.repository.CaixaRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class CaixaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CaixaRepository dao;

	public Caixa consultarCaixa(Caixa cx) {
		return dao.consultarCaixa(cx);

	}

	@Transacional
	public void excluir(Caixa obj) {
		dao.remover(obj);
	}

	public List<Caixa> filtrados(CaixaFilter filtro) {
		return dao.filtrados(filtro);
	}

	public Caixa porId(Long id) {
		return dao.porId(id);
	}

	@Transacional
	public void salvar(Caixa obj) {
		dao.save(obj);
	}

	public List<Caixa> todas() {
		return dao.todas();
	}

}

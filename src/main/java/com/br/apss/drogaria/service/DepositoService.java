package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Deposito;
import com.br.apss.drogaria.model.filter.DepositoFilter;
import com.br.apss.drogaria.repository.DepositoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class DepositoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DepositoRepository dao;

	@Transacional
	public void salvar(Deposito obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Deposito obj) {
		dao.excluir(obj);
	}

	public List<Deposito> filtrados(DepositoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Deposito> listarTodos() {
		return dao.listarTodos();
	}

	public Deposito porId(Long id) {
		return dao.porId(id);
	}

	public Deposito porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(DepositoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

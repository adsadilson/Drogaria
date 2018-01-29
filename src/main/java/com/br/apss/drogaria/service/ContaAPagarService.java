package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAPagarService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAPagarRepository dao;

	@Transacional
	public void salvar(ContaAPagar obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(ContaAPagar obj) {
		dao.excluir(obj);
	}
	
	@Transacional
	public void excluirContas(List<ContaAPagar> contas) throws Exception{
		dao.excluirContas(contas);
	}

	public List<ContaAPagar> listAll() {
		return dao.listarTodos();
	}

	public ContaAPagar porId(Long id) {
		return dao.porId(id);
	}

	public ContaAPagar porNome(String nome) {
		return dao.porNome(nome);
	}

	public List<ContaAPagar> filtrados(ContaAPagarFilter filtro) {
		return dao.filtrados(filtro);
	}

	public int qtdeFiltrados(ContaAPagarFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}

}

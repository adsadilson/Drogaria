package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.filter.PagamentoFilter;
import com.br.apss.drogaria.repository.PagamentoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PagamentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoRepository dao;

	@Transacional
	public void salvar(Pagamento obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Pagamento obj) {
		dao.excluir(obj);
	}

	public List<Pagamento> filtrados(PagamentoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Pagamento> listarTodos() {
		return dao.listarTodos();
	}

	public Pagamento porId(Long id) {
		return dao.porId(id);
	}

	public Pagamento porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(PagamentoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

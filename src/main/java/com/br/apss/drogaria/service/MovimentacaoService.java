package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.filter.MovimentacaoFilter;
import com.br.apss.drogaria.repository.MovimentacaoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class MovimentacaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MovimentacaoRepository dao;

	@Transacional
	public void salvar(Movimentacao obj) {
		dao.save(obj);
	}

	@Transacional
	public List<Movimentacao> salvar(List<Movimentacao> list) {
		return dao.save(list);
	}

	@Transacional
	public void excluir(Movimentacao obj) {
		dao.excluirPorVinculo(obj.getId());
	}

	public List<Movimentacao> filtrados(MovimentacaoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Movimentacao> listarTodos() {
		return dao.listarTodos();
	}

	public Movimentacao porId(Long id) {
		return dao.porId(id);
	}

	public Movimentacao porNome(String nome) {
		return dao.porNome(nome);
	}

	public BigDecimal pesquisaSaldo(MovimentacaoFilter filtro) {
		return dao.pesquisaSaldo(filtro);
	}

	public Movimentacao porVinculo(Long vinculo, Long id) {
		return dao.porVinculo(vinculo, id);
	}

	public List<Movimentacao> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	@Transacional
	public void excluirPorVinculo(Long id) {
		dao.excluirPorVinculo(id);
	}

	public int quantidadeFiltrados(MovimentacaoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

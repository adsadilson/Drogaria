package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.Recebimento;
import com.br.apss.drogaria.model.filter.RecebimentoFilter;
import com.br.apss.drogaria.repository.ContaAReceberRepository;
import com.br.apss.drogaria.repository.RecebimentoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class RecebimentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RecebimentoRepository dao;

	@Inject
	private ContaAReceberRepository contaAReceberRepository;

	@Transacional
	public void salvar(Recebimento obj) {
		dao.salvar(obj);
	}

	@Transacional
	public List<Recebimento> salvar(List<Recebimento> list) {
		return dao.save(list);
	}

	@Transacional
	public void excluir(Recebimento obj) {
		dao.excluir(obj);
	}

	@Transacional
	public void excluirListaPagto(List<Recebimento> obj) {
		dao.excluirListaPagto(obj);
	}

	@Transacional
	public void excluirPagtoEstornaCP(Recebimento obj) {
		dao.excluir(obj);
		for (ContaAReceber c : obj.getListaContaARecebers()) {
			ContaAReceber cp = new ContaAReceber();
			cp.setId(c.getId());
			cp.setValorPago(cp.getValorApagar());
			// ContaAReceberRepository.baixaSimples(cp);
		}
	}

	public List<Recebimento> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	public List<Recebimento> filtrados(RecebimentoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Recebimento> listarTodos() {
		return dao.listarTodos();
	}

	public Recebimento porId(Long id) {
		return dao.porId(id);
	}

	public Recebimento buscarRecebimentoPorVinculo(Long vinculo) {
		return dao.buscarRecebimentoPorVinculo(vinculo);
	}

	public Recebimento porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(RecebimentoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

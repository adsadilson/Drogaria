package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.ContaAPagarHistorico;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.filter.PagamentoFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
import com.br.apss.drogaria.repository.PagamentoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PagamentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoRepository dao;

	@Inject
	private ContaAPagarRepository contaAPagarRepository;

	@Inject
	private ContaAPagarHistoricoService cpHistoricoService;

	@Transacional
	public void salvar(Pagamento obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void salvar(List<Pagamento> list) {
		dao.save(list);
	}

	@Transacional
	public void excluir(Pagamento obj) {
		dao.excluir(obj);
	}

	@Transacional
	public void excluirListaPagto(List<Pagamento> obj) {
		dao.excluirListaPagto(obj);
	}

	@Transacional
	public void cancelarPagamento(List<Pagamento> listaPagto) {

		for (Pagamento p : listaPagto) {
			// Excluir os registros da tabela pagamento_conta_apagar
			dao.excluirPagamentoContaApagar(p.getId());
			// Excluir os registros da tabela pagamento_movimentacao
			dao.excluirPagtoMovimentacao(p.getId());
		}

		List<Movimentacao> movto = listaPagto.get(0).getListaMovimentacoes();
		for (Movimentacao m : movto) {
			// Excluir os registros da tabela movimentacao
			dao.excluirMovimentacao(m.getId());
		}

		// Excluir os registros da tabela pagamento
		dao.excluirPorVinculo(listaPagto.get(0).getAgrupadorContaApagar());

		List<ContaAPagarHistorico> listaCPHistorico = cpHistoricoService
				.listaVinculo(listaPagto.get(0).getAgrupadorContaApagar());
		
		for (ContaAPagarHistorico cph : listaCPHistorico) {
			ContaAPagar cp = new ContaAPagar();
			cp.setId(cph.getContaApagar().getId());
			cp.setValorApagar(cph.getValorAnterio());
			cp.setVinculo(cph.getVinculoAnterio());
			// Atualiza os registros da tabela conta_apagar
			contaAPagarRepository.cancelarPagto(cp);
		}
	}

	public List<Pagamento> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
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

	public Pagamento buscarPagamentoPorVinculo(Long vinculo) {
		return dao.buscarPagamentoPorVinculo(vinculo);
	}

	public Pagamento porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(PagamentoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

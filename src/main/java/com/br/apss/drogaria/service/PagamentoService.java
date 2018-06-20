package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.ContaAPagarHistorico;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.filter.PagamentoFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
import com.br.apss.drogaria.repository.PagamentoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;
import com.br.apss.drogaria.util.jsf.NegocioException;

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
	public List<Pagamento> salvar(List<Pagamento> list) {
		return dao.save(list);
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

		List<ContaAPagarHistorico> listaContaApagarHistorico = cpHistoricoService
				.listaVinculo(listaPagto.get(0).getAgrupadorContaApagar());

		ContaAPagarHistorico cp = listaContaApagarHistorico.get(listaContaApagarHistorico.size() - 1);

		Long cph = cpHistoricoService.maxId(cp);
		ContaAPagarHistorico cp2 = cpHistoricoService.porId(cph);

		Format f = new SimpleDateFormat("dd/MM/yyyy");
		Pagamento p = dao.porId(cp2.getPagamento());

		if (!cp.equals(cp2)) {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("É necessário cancelar primeiro o pagamento de código: " + cp2.getPagamento()
					+ "\n" + "pago em " + f.format(p.getDataPago()));
		}

		for (Pagamento p2 : listaPagto) {
			// Excluir os registros da tabela pagamento_conta_apagar
			dao.excluirPagamentoContaApagar(p2.getId());
			// Excluir os registros da tabela pagamento_movimentacao
			dao.excluirPagtoMovimentacao(p2.getId());
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

		for (ContaAPagarHistorico cph1 : listaCPHistorico) {
			ContaAPagar cp3 = new ContaAPagar();
			BigDecimal vlr = (cph1.getValorAtual().add(cph1.getValorPago().add(cph1.getValorDesc())))
					.subtract(cph1.getValorMultaJuros());

			cp3.setId(cph1.getContaApagar().getId());
			cp3.setValorApagar(vlr);
			cp3.setVinculo(cph1.getVinculoAnterio());
			// Atualiza os registros da tabela conta_apagar
			contaAPagarRepository.cancelarPagto(cp3);
		}

		// Excluir os registros da tabela historico_conta_apagar
		for (ContaAPagarHistorico cph2 : listaCPHistorico) {
			cpHistoricoService.excluir(cph2);
		}

	}

	public List<Pagamento> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	public List<Pagamento> filtrados(PagamentoFilter filtro) {
		List<Pagamento> list = dao.filtrados(filtro);
		return list;
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

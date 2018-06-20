package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.TipoBaixa;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoLanc;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.ContaAPagarHistorico;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAPagarService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAPagarRepository dao;

	@Inject
	private MovimentacaoService movtoService;

	@Inject
	private PlanoContaService contaService;

	@Inject
	private PagamentoService pagamentoService;

	@Inject
	private ContaAPagarHistoricoService cpHistoricoService;

	@Inject
	private GeradorVinculo gerarVinculo;

	@Transacional
	public void salvar(ContaAPagar contaAPagar) {
		dao.salvar(contaAPagar);
	}

	@Transacional
	public void baixaSimples(ContaAPagar contaAPagar, Pagamento pagamento) {

		List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

		// Gerador do codigo de vinculo
		Long idAgrupador = gerarVinculo.gerar(Pagamento.class);
		Long idAgrupadorAnterio = contaAPagar.getVinculo() == null ? idAgrupador : contaAPagar.getVinculo();

		BigDecimal vlrAnterio = contaAPagar.getSaldoDevedor();

		pagamento.setTipoBaixa(TipoBaixa.TOTAL);
		contaAPagar.setUsuario(pagamento.getUsuario());
		contaAPagar.setSaldoDevedor(contaAPagar.getValorApagar());
		contaAPagar.setVinculo(idAgrupador);
		if (contaAPagar.getValorApagar().compareTo(contaAPagar.getPagoTB()) > 0) {
			pagamento.setTipoBaixa(TipoBaixa.PARCIAL);
			pagamento.setDescricao(pagamento.getDescricao() + " (P)");
		}
		contaAPagar.setValorApagar(contaAPagar.getValorApagar().subtract(contaAPagar.getPagoTB()));

		// Seta o objeto na lista de contas a pagar para vincular ao pagamento
		listaContaAPagars.add(contaAPagar);

		// Update na tabela conta a pagar
		dao.updateNasContasApagar(contaAPagar);

		// Lista de Movimentação
		List<Movimentacao> listaMovimentacoes = new ArrayList<Movimentacao>();

		Movimentacao movto = new Movimentacao();

		PlanoConta pl1 = new PlanoConta();
		pl1 = contaService.porId(pagamento.getConta().getId());

		PlanoConta pl2 = new PlanoConta();
		pl2 = contaService.porId(pl1.getContaPai().getId());

		movto.setDataDoc(pagamento.getDataPago());
		movto.setDataLanc(pagamento.getDataPago());
		movto.setUsuario(pagamento.getUsuario());
		movto.setDescricao(pagamento.getDescricao());
		movto.setVinculo(pagamento.getVinculo());
		movto.setDocumento(contaAPagar.getNumDoc());
		movto.setPessoa(contaAPagar.getFornecedor());
		movto.setVlrEntrada(null);
		movto.setVlrSaida(contaAPagar.getPagoTB());
		movto.setTipoLanc(TipoLanc.PC);
		movto.setTipoConta(TipoConta.CC);
		movto.setPlanoConta(pl1);
		movto.setPlanoContaPai(pl2);

		// Add objeto movimentação na lista para salvar posterior
		listaMovimentacoes.add(movto);

		// Criação do objeto movimentação caso aja multa
		if (contaAPagar.getMultaTB().compareTo(BigDecimal.ZERO) > 0) {

			Movimentacao movtoMulta = new Movimentacao();

			PlanoConta pl1Multa = new PlanoConta();
			pl1Multa = contaService.porNome("JUROS/MULTA CP");

			PlanoConta pl2Multa = new PlanoConta();
			pl2Multa = contaService.porId(pl1Multa.getContaPai().getId());

			movtoMulta.setDataDoc(pagamento.getDataPago());
			movtoMulta.setDataLanc(pagamento.getDataPago());
			movtoMulta.setUsuario(pagamento.getUsuario());
			movtoMulta.setDescricao("PG JURUOS/MULTA " + pagamento.getDescricao());
			movtoMulta.setVinculo(pagamento.getVinculo());
			movtoMulta.setDocumento(contaAPagar.getNumDoc());
			movtoMulta.setPessoa(contaAPagar.getFornecedor());
			movtoMulta.setVlrEntrada(null);
			movtoMulta.setVlrSaida(contaAPagar.getMultaTB());
			movtoMulta.setTipoLanc(TipoLanc.PC);
			movtoMulta.setTipoConta(TipoConta.D);
			movtoMulta.setPlanoConta(pl1Multa);
			movtoMulta.setPlanoContaPai(pl2Multa);
			// Add na lista de movimentação
			listaMovimentacoes.add(movtoMulta);
		}

		// Criação do objeto movimentação caso aja desconto
		if (contaAPagar.getDescTB().compareTo(BigDecimal.ZERO) > 0) {

			Movimentacao movtoDesc = new Movimentacao();

			PlanoConta pl1Desc = new PlanoConta();
			pl1Desc = contaService.porNome("RECEITAS C/DESC./JUROS E MULTA");

			PlanoConta pl2Desc = new PlanoConta();
			pl2Desc = contaService.porId(pl1Desc.getContaPai().getId());

			movtoDesc.setDataDoc(pagamento.getDataPago());
			movtoDesc.setDataLanc(pagamento.getDataPago());
			movtoDesc.setUsuario(pagamento.getUsuario());
			movtoDesc.setDescricao("REC DESCONTO " + pagamento.getDescricao());
			movtoDesc.setVinculo(pagamento.getVinculo());
			movtoDesc.setDocumento(contaAPagar.getNumDoc());
			movtoDesc.setPessoa(contaAPagar.getFornecedor());
			movtoDesc.setVlrEntrada(contaAPagar.getDescTB());
			movtoDesc.setVlrSaida(null);
			movtoDesc.setTipoLanc(TipoLanc.PC);
			movtoDesc.setTipoConta(TipoConta.R);
			movtoDesc.setPlanoConta(pl1Desc);
			movtoDesc.setPlanoContaPai(pl2Desc);
			// Add na lista de movimentação
			listaMovimentacoes.add(movtoDesc);
		}

		// Salvar a lista de movimentação na tabela e recuperando a mesma
		listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

		// Criação da lista de pagamento para receber os objetos
		List<Pagamento> list = new ArrayList<Pagamento>();

		Pagamento pagto = new Pagamento();
		pagto.setDataLanc(new Date());
		pagto.setDataPago(pagamento.getDataPago());
		pagto.setDescricao(pagamento.getDescricao());
		pagto.setFormaBaixa(pagamento.getFormaBaixa());
		pagto.setValor(contaAPagar.getValor());
		pagto.setValorAPagar(contaAPagar.getSaldoDevedor());
		pagto.setValorDesc(contaAPagar.getDescTB());
		pagto.setValorMultaJuros(contaAPagar.getMultaTB());
		pagto.setValorPago(contaAPagar.getPagoTB());
		pagto.setUsuario(pagamento.getUsuario());
		pagto.setListaContaAPagars(listaContaAPagars);
		pagto.setAgrupadorContaApagar(idAgrupador);
		pagto.setTipoBaixa(pagamento.getTipoBaixa());
		pagto.setListaMovimentacoes(listaMovimentacoes);
		// Add na lista
		list.add(pagto);

		// Salvando a lista de recebimento preenchida e recuperando a mesma
		List<Pagamento> listaPagto = pagamentoService.salvar(list);

		// Criação do objeto conta a pagar historico
		ContaAPagarHistorico cpHistorico = new ContaAPagarHistorico();

		cpHistorico.setContaApagar(contaAPagar);
		cpHistorico.setValorAnterio(vlrAnterio);
		cpHistorico.setValorAtual(contaAPagar.getValorApagar());
		cpHistorico.setValorPago(contaAPagar.getPagoTB());
		cpHistorico.setUsuario(pagamento.getUsuario());
		cpHistorico.setData(new Date());
		cpHistorico.setPagamento(listaPagto.get(0).getId());
		cpHistorico.setValorDesc(contaAPagar.getDescTB());
		cpHistorico.setValorMultaJuros(contaAPagar.getMultaTB());
		cpHistorico.setAgrupadorPagamento(idAgrupador);
		cpHistorico.setVinculoAnterio(idAgrupadorAnterio);

		// Salvando o objeto historico preenchindo
		cpHistoricoService.salvar(cpHistorico);

	}

	@Transacional // Baixar varias contas de uma só vez
	public void baixaMultiplas(List<ContaAPagar> listaContaAPagars, List<Movimentacao> listaMovimentacoes,
			List<Pagamento> listaPagamentos, Pagamento pagamento) {

		// Verificar a condicao se é baixa individual (BI) ou baixa agrupada (BA)
		if (pagamento.getFormaBaixa() == FormaBaixa.BI) {
			// Baixa individual
			for (int i = 0; i < listaContaAPagars.size(); i++) {
				/*
				 * Percorrer o for para carregar os objetos para fazer a baixa ultilizado o
				 * metodo da baixa simples
				 */
				ContaAPagar contaAPagar = listaContaAPagars.get(i);
				Pagamento pagto = listaPagamentos.get(i);
				baixaSimples(contaAPagar, pagto);
			}

		} else {
			// Baixa agrupada
			BigDecimal valorMulta = BigDecimal.ZERO;
			BigDecimal valorDesc = BigDecimal.ZERO;

			Long idAgrupador = gerarVinculo.gerar(Pagamento.class);

			for (ContaAPagar cp : listaContaAPagars) {

				ContaAPagar contaAPagar = new ContaAPagar();
				contaAPagar.setId(cp.getId());
				contaAPagar.setValorApagar(cp.getValorApagar().subtract(cp.getPagoTB()));
				contaAPagar.setVinculo(idAgrupador);
				// Altera o valor de cada titulo a pagar
				dao.updateNasContasApagar(contaAPagar);

			}

			// Unifica os valores de multas e descontos
			for (int j = 0; j < listaContaAPagars.size(); j++) {
				if (listaContaAPagars.get(j).getMultaTB().compareTo(BigDecimal.ZERO) > 0) {
					valorMulta = valorMulta.add(listaContaAPagars.get(j).getMultaTB());
				}
				if (listaContaAPagars.get(j).getDescTB().compareTo(BigDecimal.ZERO) > 0) {
					valorDesc = valorDesc.add(listaContaAPagars.get(j).getDescTB());
				}
			}

			// Se existir valor de multar add na lista de movimentacao
			if (valorMulta.compareTo(BigDecimal.ZERO) > 0) {

				Movimentacao movtoMulta = new Movimentacao();

				PlanoConta pl1Multa = new PlanoConta();
				pl1Multa = contaService.porNome("JUROS/MULTA CP");

				PlanoConta pl2Multa = new PlanoConta();
				pl2Multa = contaService.porId(pl1Multa.getContaPai().getId());

				movtoMulta.setDataDoc(pagamento.getDataPago());
				movtoMulta.setDataLanc(pagamento.getDataPago());
				movtoMulta.setUsuario(pagamento.getUsuario());
				movtoMulta.setDescricao("PG JUROS/MULTA ");
				movtoMulta.setVinculo(pagamento.getVinculo());
				movtoMulta.setDocumento(null);
				movtoMulta.setPessoa(null);
				movtoMulta.setVlrEntrada(null);
				movtoMulta.setVlrSaida(valorMulta);
				movtoMulta.setTipoLanc(TipoLanc.PC);
				movtoMulta.setTipoConta(TipoConta.D);
				movtoMulta.setPlanoConta(pl1Multa);
				movtoMulta.setPlanoContaPai(pl2Multa);
				listaMovimentacoes.add(movtoMulta);
			}

			// Se existir valor de desconto add na lista de movimentacao
			if (valorDesc.compareTo(BigDecimal.ZERO) > 0) {

				Movimentacao movtoDesc = new Movimentacao();

				PlanoConta pl1Desc = new PlanoConta();
				pl1Desc = contaService.porNome("RECEITAS C/DESC./JUROS E MULTA");

				PlanoConta pl2Desc = new PlanoConta();
				pl2Desc = contaService.porId(pl1Desc.getContaPai().getId());

				movtoDesc.setDataDoc(pagamento.getDataPago());
				movtoDesc.setDataLanc(pagamento.getDataPago());
				movtoDesc.setUsuario(pagamento.getUsuario());
				movtoDesc.setDescricao("RECEBIMENTO DE DESCONTOS ");
				movtoDesc.setVinculo(pagamento.getVinculo());
				movtoDesc.setDocumento(null);
				movtoDesc.setPessoa(null);
				movtoDesc.setVlrEntrada(valorDesc);
				movtoDesc.setVlrSaida(null);
				movtoDesc.setTipoLanc(TipoLanc.PC);
				movtoDesc.setTipoConta(TipoConta.R);
				movtoDesc.setPlanoConta(pl1Desc);
				movtoDesc.setPlanoContaPai(pl2Desc);
				listaMovimentacoes.add(movtoDesc);

			}

			// Salvar toda movimentação na tabela e retorna a lista salvar
			listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

			// Criar uma nova lista do tipo pagamento para receber novas atualizações
			List<Pagamento> list = new ArrayList<>();

			for (Pagamento pagto : listaPagamentos) {

				Pagamento p = new Pagamento();

				p.setDataLanc(new Date());
				p.setDataPago(pagamento.getDataPago());
				p.setDescricao(pagto.getDescricao());
				p.setFormaBaixa(pagto.getFormaBaixa());
				p.setValor(pagto.getValor());
				p.setValorAPagar(pagto.getValorAPagar());
				p.setValorDesc(pagto.getValorDesc());
				p.setValorMultaJuros(pagto.getValorMultaJuros());
				p.setValorPago(pagto.getValorPago());
				p.setUsuario(pagto.getUsuario());
				p.setListaContaAPagars(listaContaAPagars); //
				p.setAgrupadorContaApagar(idAgrupador);
				p.setListaMovimentacoes(listaMovimentacoes);
				// Carregar a nova lista
				list.add(p);
			}

			// Salvando a nova lista no banco e recuperar a mesma
			List<Pagamento> listaPagto = pagamentoService.salvar(list);

			for (Pagamento pagto : listaPagto) {

				for (ContaAPagar cp : listaContaAPagars) {
					ContaAPagarHistorico cpHistorico = new ContaAPagarHistorico();
					cpHistorico.setContaApagar(cp);
					cpHistorico.setValorAnterio(cp.getSaldoDevedor());
					cpHistorico.setValorAtual(cp.getValorApagar().subtract(cp.getPagoTB()));
					cpHistorico.setValorPago(cp.getPagoTB());
					cpHistorico.setData(new Date());
					cpHistorico.setUsuario(pagamento.getUsuario());
					cpHistorico.setValorDesc(cp.getDescTB());
					cpHistorico.setPagamento(pagto.getId());
					cpHistorico.setValorMultaJuros(cp.getMultaTB());
					cpHistorico.setAgrupadorPagamento(idAgrupador);
					cpHistorico.setVinculoAnterio(cp.getVinculo() == null ? idAgrupador : cp.getVinculo());
					// Salvando o historico do pagamento do titulo
					cpHistoricoService.salvar(cpHistorico);
				}
			}

			Messages.addGlobalInfo("Titulo(s) baixado(s) com sucesso.");
		}

	}

	@Transacional
	public List<ContaAPagar> salvar(List<ContaAPagar> list) {
		return dao.salvar(list);
	}

	@Transacional
	public void excluir(ContaAPagar contaAPagar) {
		dao.excluir(contaAPagar);
	}

	@Transacional
	public void excluirContas(List<ContaAPagar> contas) {
		try {
			dao.excluirContas(contas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ContaAPagar> listAll() {
		return dao.listarTodos();
	}

	public ContaAPagar porId(Long id) {
		return dao.porId(id);
	}

	public List<ContaAPagar> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
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

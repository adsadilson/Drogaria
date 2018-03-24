package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.TipoBaixa;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoLanc;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
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

	@Transacional
	public void salvar(ContaAPagar contaAPagar) {
		dao.salvar(contaAPagar);
	}

	@Transacional
	public void baixaSimples(ContaAPagar contaAPagar, Pagamento pagamento) {

		List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

		contaAPagar.setStatus("PAGO");
		pagamento.setTipoBaixa(TipoBaixa.TOTAL);
		contaAPagar.setUsuario(pagamento.getUsuario());
		contaAPagar.setValorPago(contaAPagar.getValorPago().add(contaAPagar.getPagoTB()));
		contaAPagar.setSaldoDevedor(contaAPagar.getValorApagar());
		if (contaAPagar.getValorApagar().compareTo(contaAPagar.getPagoTB()) > 0) {
			contaAPagar.setStatus("PAGO PARCIAL");
			pagamento.setTipoBaixa(TipoBaixa.PARCIAL);
			pagamento.setDescricao(pagamento.getDescricao() + " (P)");
		}
		contaAPagar.setValorApagar(contaAPagar.getValorApagar().subtract(contaAPagar.getPagoTB()));

		listaContaAPagars.add(contaAPagar);

		dao.baixaSimples(contaAPagar);

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

		listaMovimentacoes.add(movto);

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
			listaMovimentacoes.add(movtoMulta);
		}

		if (contaAPagar.getDescTB().compareTo(BigDecimal.ZERO) > 0) {

			Movimentacao movtoDesc = new Movimentacao();

			PlanoConta pl1Desc = new PlanoConta();
			pl1Desc = contaService.porNome("RECEITAS COM DESCONTOS");

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
			listaMovimentacoes.add(movtoDesc);
		}

		listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

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
		pagto.setVinculo(contaAPagar.getId());
		pagto.setTipoBaixa(pagamento.getTipoBaixa());
		pagto.setListaMovimentacoes(listaMovimentacoes);
		list.add(pagto);

		pagamentoService.salvar(list);

	}

	@Transacional
	public void baixaMultiplas(List<ContaAPagar> listaContaAPagars, List<Movimentacao> listaMovimentacoes,
			List<Pagamento> listaPagamentos, Pagamento pagamento) {

		for (ContaAPagar cp : listaContaAPagars) {

			ContaAPagar contaAPagar = new ContaAPagar();
			contaAPagar.setId(cp.getId());
			contaAPagar.setStatus("PAGO");
			contaAPagar.setValorPago(cp.getValorPago().add(cp.getPagoTB()));
			contaAPagar.setValorApagar(cp.getValorApagar().subtract(cp.getPagoTB()));

			if (cp.getValorApagar().compareTo(cp.getPagoTB()) > 0) {
				contaAPagar.setStatus("PAGO PARCIAL");
			}

			dao.baixaSimples(contaAPagar);
		}

		if (pagamento.getFormaBaixa() == FormaBaixa.BI) {

			for (int i = 0; i < listaMovimentacoes.size(); i++) {

				Movimentacao movto = new Movimentacao();
				Pagamento pagto = new Pagamento();
				ContaAPagar cp = new ContaAPagar();

				List<Pagamento> list = new ArrayList<Pagamento>();
				List<Movimentacao> listM = new ArrayList<Movimentacao>();
				List<ContaAPagar> listCP = new ArrayList<ContaAPagar>();

				cp.setId(listaContaAPagars.get(i).getId());
				listCP.add(cp);

				movto.setDataDoc(listaMovimentacoes.get(i).getDataDoc());
				movto.setDataLanc(listaMovimentacoes.get(i).getDataLanc());
				movto.setUsuario(listaMovimentacoes.get(i).getUsuario());
				movto.setDescricao(listaMovimentacoes.get(i).getDescricao());
				movto.setVinculo(listaMovimentacoes.get(i).getVinculo());
				movto.setDocumento(listaMovimentacoes.get(i).getDocumento());
				movto.setPessoa(listaMovimentacoes.get(i).getPessoa());
				movto.setVlrEntrada(null);
				movto.setVlrSaida(listaMovimentacoes.get(i).getVlrSaida());
				movto.setTipoLanc(TipoLanc.PC);
				movto.setTipoConta(TipoConta.CC);
				movto.setPlanoConta(listaMovimentacoes.get(i).getPlanoConta());
				movto.setPlanoContaPai(listaMovimentacoes.get(i).getPlanoContaPai());

				listM.add(movto);

				if (listaContaAPagars.get(i).getMultaTB().compareTo(BigDecimal.ZERO) > 0) {

					Movimentacao movtoMulta = new Movimentacao();

					PlanoConta pl1Multa = new PlanoConta();
					pl1Multa = contaService.porNome("JUROS/MULTA CP");

					PlanoConta pl2Multa = new PlanoConta();
					pl2Multa = contaService.porId(pl1Multa.getContaPai().getId());

					movtoMulta.setDataDoc(listaMovimentacoes.get(i).getDataDoc());
					movtoMulta.setDataLanc(listaMovimentacoes.get(i).getDataLanc());
					movtoMulta.setUsuario(listaMovimentacoes.get(i).getUsuario());
					movtoMulta.setDescricao("PG JURUOS/MULTA " + listaMovimentacoes.get(i).getDescricao());
					movtoMulta.setVinculo(listaMovimentacoes.get(i).getVinculo());
					movtoMulta.setDocumento(listaMovimentacoes.get(i).getDocumento());
					movtoMulta.setPessoa(listaMovimentacoes.get(i).getPessoa());
					movtoMulta.setVlrEntrada(null);
					movtoMulta.setVlrSaida(listaContaAPagars.get(i).getMultaTB());
					movtoMulta.setTipoLanc(TipoLanc.PC);
					movtoMulta.setTipoConta(TipoConta.D);
					movtoMulta.setPlanoConta(pl1Multa);
					movtoMulta.setPlanoContaPai(pl2Multa);
					listM.add(movtoMulta);
				}

				if (listaContaAPagars.get(i).getDescTB().compareTo(BigDecimal.ZERO) > 0) {

					Movimentacao movtoDesc = new Movimentacao();

					PlanoConta pl1Desc = new PlanoConta();
					pl1Desc = contaService.porNome("RECEITAS COM DESCONTOS");

					PlanoConta pl2Desc = new PlanoConta();
					pl2Desc = contaService.porId(pl1Desc.getContaPai().getId());

					movtoDesc.setDataDoc(listaMovimentacoes.get(i).getDataDoc());
					movtoDesc.setDataLanc(listaMovimentacoes.get(i).getDataLanc());
					movtoDesc.setUsuario(listaMovimentacoes.get(i).getUsuario());
					movtoDesc.setDescricao("REC DESCONTO " + listaMovimentacoes.get(i).getDescricao());
					movtoDesc.setVinculo(listaMovimentacoes.get(i).getVinculo());
					movtoDesc.setDocumento(listaMovimentacoes.get(i).getDocumento());
					movtoDesc.setPessoa(listaMovimentacoes.get(i).getPessoa());
					movtoDesc.setVlrEntrada(listaContaAPagars.get(i).getDescTB());
					movtoDesc.setVlrSaida(null);
					movtoDesc.setTipoLanc(TipoLanc.PC);
					movtoDesc.setTipoConta(TipoConta.R);
					movtoDesc.setPlanoConta(pl1Desc);
					movtoDesc.setPlanoContaPai(pl2Desc);
					listM.add(movtoDesc);
				}

				listM = movtoService.salvar(listM);

				pagto.setDataLanc(new Date());
				pagto.setDataPago(listaPagamentos.get(i).getDataPago());
				pagto.setDescricao(listaPagamentos.get(i).getDescricao());
				pagto.setFormaBaixa(FormaBaixa.BI);
				pagto.setValor(listaPagamentos.get(i).getValor());
				pagto.setValorDesc(listaContaAPagars.get(i).getDescTB());
				pagto.setValorMultaJuros(listaContaAPagars.get(i).getMultaTB());
				pagto.setValorAPagar(listaContaAPagars.get(i).getSaldoDevedor()
						.add(listaContaAPagars.get(i).getMultaTB().subtract(listaContaAPagars.get(i).getDescTB())));
				pagto.setValorPago(listaPagamentos.get(i).getValorPago());
				pagto.setUsuario(listaPagamentos.get(i).getUsuario());
				pagto.setListaContaAPagars(listCP);
				pagto.setVinculo(listaContaAPagars.get(i).getId());
				pagto.setTipoBaixa(listaPagamentos.get(i).getTipoBaixa());
				pagto.setListaMovimentacoes(listM);
				list.add(pagto);

				pagamentoService.salvar(list);
			}
		} else {

			BigDecimal valorMutla = BigDecimal.ZERO;
			BigDecimal valorDesc = BigDecimal.ZERO;

			for (int j = 0; j < listaContaAPagars.size(); j++) {
				if (listaContaAPagars.get(j).getMultaTB().compareTo(BigDecimal.ZERO) > 0) {
					valorMutla = valorMutla.add(listaContaAPagars.get(j).getMultaTB());
				}
				if (listaContaAPagars.get(j).getDescTB().compareTo(BigDecimal.ZERO) > 0) {
					valorDesc = valorDesc.add(listaContaAPagars.get(j).getDescTB());
				}
			}

			if (valorMutla.compareTo(BigDecimal.ZERO) > 0) {

				Movimentacao movtoMulta = new Movimentacao();

				PlanoConta pl1Multa = new PlanoConta();
				pl1Multa = contaService.porNome("JUROS/MULTA CP");

				PlanoConta pl2Multa = new PlanoConta();
				pl2Multa = contaService.porId(pl1Multa.getContaPai().getId());

				movtoMulta.setDataDoc(pagamento.getDataPago());
				movtoMulta.setDataLanc(pagamento.getDataPago());
				movtoMulta.setUsuario(pagamento.getUsuario());
				movtoMulta.setDescricao("PG JURUOS/MULTA ");
				movtoMulta.setVinculo(pagamento.getVinculo());
				movtoMulta.setDocumento(null);
				movtoMulta.setPessoa(null);
				movtoMulta.setVlrEntrada(null);
				movtoMulta.setVlrSaida(valorMutla);
				movtoMulta.setTipoLanc(TipoLanc.PC);
				movtoMulta.setTipoConta(TipoConta.D);
				movtoMulta.setPlanoConta(pl1Multa);
				movtoMulta.setPlanoContaPai(pl2Multa);
				listaMovimentacoes.add(movtoMulta);
			}

			if (valorDesc.compareTo(BigDecimal.ZERO) > 0) {

				Movimentacao movtoDesc = new Movimentacao();

				PlanoConta pl1Desc = new PlanoConta();
				pl1Desc = contaService.porNome("RECEITAS COM DESCONTOS");

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

			listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

			List<Pagamento> list = new ArrayList<>();

			for (Pagamento pagto : listaPagamentos) {

				Pagamento p = new Pagamento();

				p.setDataLanc(new Date());
				p.setDataPago(pagto.getDataPago());
				p.setDescricao(pagto.getDescricao());
				p.setFormaBaixa(pagto.getFormaBaixa());
				p.setValor(pagto.getValor());
				p.setValorAPagar(pagto.getValorAPagar());
				p.setValorDesc(pagto.getValorDesc());
				p.setValorMultaJuros(pagto.getValorMultaJuros());
				p.setValorPago(pagto.getValorPago());
				p.setUsuario(pagto.getUsuario());
				p.setListaContaAPagars(listaContaAPagars);
				p.setVinculo(pagto.getVinculo());
				p.setListaMovimentacoes(listaMovimentacoes);
				list.add(p);
			}

			pagamentoService.salvar(list);
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
	public void excluirContas(List<ContaAPagar> contas) throws Exception {
		dao.excluirContas(contas);
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

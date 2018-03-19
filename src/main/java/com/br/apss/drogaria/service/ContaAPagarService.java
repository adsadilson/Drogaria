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
		if (contaAPagar.getValorApagar().compareTo(contaAPagar.getValorPago()) > 0) {
			pagamento.setTipoBaixa(TipoBaixa.PARCIAL);
			pagamento.setDescricao(pagamento.getDescricao() + " (P)");
		}

		listaContaAPagars.add(contaAPagar);

		dao.baixaSimples(contaAPagar);

		if (contaAPagar.getValorApagar().compareTo(contaAPagar.getValorPago()) > 0) {

			ContaAPagar cp = new ContaAPagar();
			cp.setDataDoc(contaAPagar.getDataDoc());
			cp.setDataVencto(contaAPagar.getDataVencto());
			cp.setNumDoc(contaAPagar.getNumDoc());
			cp.setParcela(contaAPagar.getParcela());
			cp.setStatus("ABERTO");
			cp.setTipoCobranca(contaAPagar.getTipoCobranca());

			cp.setValor(contaAPagar.getValorApagar().subtract(contaAPagar.getValorPago()));
			cp.setValorPago(BigDecimal.ZERO);
			cp.setVinculo(contaAPagar.getVinculo());
			cp.setFornecedor(contaAPagar.getFornecedor());
			cp.setUsuario(contaAPagar.getUsuario());
			cp.setOrigemId(contaAPagar);

			dao.salvar(cp);
		}

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
		movto.setVlrSaida(contaAPagar.getValorPago());
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
			movtoMulta.setDescricao("PG JURUOS/MULTA "+pagamento.getDescricao());
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

		listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

		List<Pagamento> list = new ArrayList<Pagamento>();

		Pagamento pagto = new Pagamento();

		pagto.setDataLanc(new Date());
		pagto.setDataPago(pagamento.getDataPago());
		pagto.setDescricao(pagamento.getDescricao());
		pagto.setFormaBaixa(pagamento.getFormaBaixa());
		pagto.setValor(contaAPagar.getValor());
		pagto.setValorAPagar(contaAPagar.getValor().add(contaAPagar.getMultaTB()).subtract(contaAPagar.getDescTB()));
		pagto.setValorDesc(contaAPagar.getDescTB());
		pagto.setValorMultaJuros(contaAPagar.getMultaTB());
		pagto.setValorPago(contaAPagar.getValorPago());
		pagto.setUsuario(pagamento.getUsuario());
		pagto.setListaContaAPagars(listaContaAPagars);
		pagto.setVinculo(pagamento.getVinculo());
		pagto.setTipoBaixa(pagamento.getTipoBaixa());
		pagto.setListaMovimentacoes(listaMovimentacoes);
		list.add(pagto);

		pagamentoService.salvar(list);

	}

	@Transacional
	public void baixaMultiplas(List<ContaAPagar> listaContaAPagars, List<Movimentacao> listaMovimentacoes,
			List<Pagamento> listaPagamentos, FormaBaixa formaBaixa) {

		for (ContaAPagar cp : listaContaAPagars) {

			ContaAPagar contaAPagar = new ContaAPagar();
			contaAPagar.setId(cp.getId());
			contaAPagar.setStatus("PAGO");
			contaAPagar.setValorPago(cp.getValorPago());

			dao.baixaSimples(contaAPagar);
		}

		for (ContaAPagar cp : listaContaAPagars) {

			if (cp.getValorApagar().compareTo(cp.getValorPago()) > 0) {

				ContaAPagar contaAPagar = new ContaAPagar();
				contaAPagar.setDataDoc(cp.getDataDoc());
				contaAPagar.setDataVencto(cp.getDataVencto());
				contaAPagar.setNumDoc(cp.getNumDoc());
				contaAPagar.setParcela(cp.getParcela());
				contaAPagar.setStatus("ABERTO");
				contaAPagar.setTipoCobranca(cp.getTipoCobranca());

				contaAPagar.setValor(cp.getValorApagar().subtract(cp.getValorPago()));
				contaAPagar.setValorPago(BigDecimal.ZERO);
				contaAPagar.setVinculo(cp.getVinculo());
				contaAPagar.setFornecedor(cp.getFornecedor());
				contaAPagar.setUsuario(cp.getUsuario());
				contaAPagar.setOrigemId(cp);

				dao.salvar(contaAPagar);
			}
		}

		if (formaBaixa == FormaBaixa.BI) {

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

				listM = movtoService.salvar(listM);

				pagto.setDataLanc(new Date());
				pagto.setDataPago(listaPagamentos.get(i).getDataPago());
				pagto.setDescricao(listaPagamentos.get(i).getDescricao());
				pagto.setFormaBaixa(FormaBaixa.BI);
				pagto.setValor(listaPagamentos.get(i).getValor());
				pagto.setValorDesc(listaContaAPagars.get(i).getDescTB());
				pagto.setValorMultaJuros(listaContaAPagars.get(i).getMultaTB());
				pagto.setValorPago(listaPagamentos.get(i).getValorPago());
				pagto.setUsuario(listaPagamentos.get(i).getUsuario());
				pagto.setListaContaAPagars(listCP);
				pagto.setVinculo(listaPagamentos.get(i).getVinculo());
				pagto.setTipoBaixa(listaPagamentos.get(i).getTipoBaixa());
				pagto.setListaMovimentacoes(listM);
				list.add(pagto);

				pagamentoService.salvar(list);
			}
		} else {

			listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

			List<Pagamento> list = new ArrayList<>();

			for (Pagamento pagamento : listaPagamentos) {

				Pagamento p = new Pagamento();

				p.setDataLanc(new Date());
				p.setDataPago(pagamento.getDataPago());
				p.setDescricao(pagamento.getDescricao());
				p.setFormaBaixa(pagamento.getFormaBaixa());
				p.setValor(pagamento.getValor());
				p.setValorAPagar(pagamento.getValorAPagar());
				p.setValorDesc(pagamento.getValorDesc());
				p.setValorMultaJuros(pagamento.getValorMultaJuros());
				p.setValorPago(pagamento.getValorPago());
				p.setUsuario(pagamento.getUsuario());
				p.setListaContaAPagars(listaContaAPagars);
				p.setVinculo(pagamento.getVinculo());
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

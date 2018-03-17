package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

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

		BigDecimal m = BigDecimal.ZERO;
		BigDecimal d = BigDecimal.ZERO;
		BigDecimal p = BigDecimal.ZERO;
		BigDecimal vlrApagar = contaAPagar.getValorApagar();

		m = m.add(contaAPagar.getValorMultaJuros().add(contaAPagar.getMulta()));
		d = d.add(contaAPagar.getValorDesc().add(contaAPagar.getDesc()));
		p = p.add(contaAPagar.getValorPago().add(contaAPagar.getPago()));

		contaAPagar.setValorApagar((contaAPagar.getValor().add(m).subtract(d)).subtract(p));
		contaAPagar.setStatus("PAGO");
		pagamento.setTipoBaixa(TipoBaixa.TOTAL);
		contaAPagar.setValorMultaJuros(m);
		contaAPagar.setValorDesc(d);
		contaAPagar.setValorPago(p);
		contaAPagar.setUsuario(pagamento.getUsuario());
		if (contaAPagar.getValorApagar().compareTo(BigDecimal.ZERO) > 0) {
			contaAPagar.setStatus("PENDENTE");
			pagamento.setTipoBaixa(TipoBaixa.PARCIAL);
			pagamento.setDescricao(pagamento.getDescricao() + " (P)");
		}

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
		movto.setVlrSaida(contaAPagar.getPago());
		movto.setTipoLanc(TipoLanc.PC);
		movto.setTipoConta(TipoConta.CC);
		movto.setPlanoConta(pl1);
		movto.setPlanoContaPai(pl2);

		listaMovimentacoes.add(movto);

		listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

		List<Pagamento> list = new ArrayList<Pagamento>();

		Pagamento pagto = new Pagamento();

		pagto.setDataLanc(new Date());
		pagto.setDataPago(pagamento.getDataPago());
		pagto.setDescricao(pagamento.getDescricao());
		pagto.setFormaBaixa(pagamento.getFormaBaixa());
		pagto.setValor(contaAPagar.getValor());
		pagto.setValorAPagar(vlrApagar);
		pagto.setValorDesc(contaAPagar.getDesc());
		pagto.setValorMultaJuros(contaAPagar.getMulta());
		pagto.setValorPago(contaAPagar.getPago());
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
			List<Pagamento> listaPagamentos) {

		for (ContaAPagar cp : listaContaAPagars) {

			BigDecimal m = BigDecimal.ZERO;
			BigDecimal d = BigDecimal.ZERO;
			BigDecimal p = BigDecimal.ZERO;

			m = m.add(cp.getValorMultaJuros().add(cp.getMultaTB()));
			d = d.add(cp.getValorDesc().add(cp.getDescTB()));
			p = p.add(cp.getValorPago().add(cp.getPagoTB()));

			ContaAPagar contaAPagar = new ContaAPagar();
			contaAPagar.setId(cp.getId());
			contaAPagar.setStatus("PENDENTE");
			contaAPagar.setValorMultaJuros(m);
			contaAPagar.setValorDesc(d);
			contaAPagar.setValorPago(p);
			contaAPagar.setValorApagar((cp.getValor().add(m).subtract(d)).subtract(p));

			if (cp.getValorApagar().compareTo(cp.getPagoTB()) == 0) {
				contaAPagar.setStatus("PAGO");
			}
			dao.baixaSimples(contaAPagar);
		}

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

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
import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.ContaAReceberHistorico;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Recebimento;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
import com.br.apss.drogaria.repository.ContaAReceberRepository;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAReceberService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAReceberRepository dao;
	
	@Inject
	private PlanoContaService contaService;
	
	@Inject
	private RecebimentoService recebimentoService;
	
	@Inject
	private MovimentacaoService movtoService;
	
	@Inject
	private ContaAReceberHistoricoService cpHistoricoService;
	
	@Inject
	private GeradorVinculo gerarVinculo;

	@Transacional
	public void salvar(ContaAReceber obj) {
		dao.save(obj);
	}

	@Transacional
	public void excluir(ContaAReceber obj) {
		dao.excluirPorVinculo(obj.getId());
	}

	public List<ContaAReceber> filtrados(ContaAReceberFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<ContaAReceber> listarTodos() {
		return dao.listarTodos();
	}

	public ContaAReceber porId(Long id) {
		return dao.porId(id);
	}

	public ContaAReceber porVinculo(Long vinculo, Long id) {
		return dao.porVinculo(vinculo, id);
	}

	public List<ContaAReceber> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	@Transacional
	public void excluirPorVinculo(Long id) {
		dao.excluirPorVinculo(id);
	}

	public int quantidadeFiltrados(ContaAReceberFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}

	@Transacional
	public void excluirContas(List<ContaAReceber> contas) throws Exception {

	}
	
	@Transacional
	public void baixaSimples(ContaAReceber contaAReceber, Recebimento recebimento) {

		List<ContaAReceber> listaContaARecebers = new ArrayList<ContaAReceber>();

		Long idAgrupador = gerarVinculo.gerar(Recebimento.class);
		Long idAgrupadorAnterio = contaAReceber.getVinculo() == null ? idAgrupador : contaAReceber.getVinculo();

		BigDecimal vlrAnterio = contaAReceber.getSaldoDevedor();

		contaAReceber.setStatus("RECEBIMENTO TOTAL");
		recebimento.setTipoBaixa(TipoBaixa.TOTAL);
		contaAReceber.setUsuario(recebimento.getUsuario());
		contaAReceber.setValorPago(contaAReceber.getValorPago().add(contaAReceber.getPagoTB()));
		contaAReceber.setSaldoDevedor(contaAReceber.getValorApagar());
		contaAReceber.setVinculo(idAgrupador);
		if (contaAReceber.getValorApagar().compareTo(contaAReceber.getPagoTB()) > 0) {
			contaAReceber.setStatus("RECEBIMENTO PARCIAL");
			recebimento.setTipoBaixa(TipoBaixa.PARCIAL);
			recebimento.setDescricao(recebimento.getDescricao() + " (P)");
		}
		contaAReceber.setValorApagar(contaAReceber.getValorApagar().subtract(contaAReceber.getPagoTB()));

		listaContaARecebers.add(contaAReceber);

		dao.baixaSimples(contaAReceber);

		List<Movimentacao> listaMovimentacoes = new ArrayList<Movimentacao>();

		Movimentacao movto = new Movimentacao();

		PlanoConta pl1 = new PlanoConta();
		pl1 = contaService.porId(recebimento.getConta().getId());

		PlanoConta pl2 = new PlanoConta();
		pl2 = contaService.porId(pl1.getContaPai().getId());

		movto.setDataDoc(recebimento.getDataPago());
		movto.setDataLanc(recebimento.getDataPago());
		movto.setUsuario(recebimento.getUsuario());
		movto.setDescricao(recebimento.getDescricao());
		movto.setVinculo(recebimento.getVinculo());
		movto.setDocumento(contaAReceber.getDocumento());
		movto.setPessoa(contaAReceber.getCliente());
		movto.setVlrEntrada(contaAReceber.getPagoTB());
		movto.setVlrSaida(null);
		movto.setTipoLanc(TipoLanc.RR);
		movto.setTipoConta(TipoConta.CC);
		movto.setPlanoConta(pl1);
		movto.setPlanoContaPai(pl2);

		listaMovimentacoes.add(movto);

		if (contaAReceber.getMultaTB().compareTo(BigDecimal.ZERO) > 0) {

			Movimentacao movtoMulta = new Movimentacao();

			PlanoConta pl1Multa = new PlanoConta();
			pl1Multa = contaService.porNome("RECEITAS C/DESC./JUROS E MULTA");

			PlanoConta pl2Multa = new PlanoConta();
			pl2Multa = contaService.porId(pl1Multa.getContaPai().getId());

			movtoMulta.setDataDoc(recebimento.getDataPago());
			movtoMulta.setDataLanc(recebimento.getDataPago());
			movtoMulta.setUsuario(recebimento.getUsuario());
			movtoMulta.setDescricao("REC JUROS/MULTA " + recebimento.getDescricao());
			movtoMulta.setVinculo(recebimento.getVinculo());
			movtoMulta.setDocumento(contaAReceber.getDocumento());
			movtoMulta.setPessoa(contaAReceber.getCliente());
			movtoMulta.setVlrEntrada(contaAReceber.getMultaTB());
			movtoMulta.setVlrSaida(null);
			movtoMulta.setTipoLanc(TipoLanc.RR);
			movtoMulta.setTipoConta(TipoConta.R);
			movtoMulta.setPlanoConta(pl1Multa);
			movtoMulta.setPlanoContaPai(pl2Multa);
			listaMovimentacoes.add(movtoMulta);
		}

		if (contaAReceber.getDescTB().compareTo(BigDecimal.ZERO) > 0) {

			Movimentacao movtoDesc = new Movimentacao();

			PlanoConta pl1Desc = new PlanoConta();
			pl1Desc = contaService.porNome("DESCONTOS CONCEDIDOS");

			PlanoConta pl2Desc = new PlanoConta();
			pl2Desc = contaService.porId(pl1Desc.getContaPai().getId());

			movtoDesc.setDataDoc(recebimento.getDataPago());
			movtoDesc.setDataLanc(recebimento.getDataPago());
			movtoDesc.setUsuario(recebimento.getUsuario());
			movtoDesc.setDescricao("DESCONTO " + recebimento.getDescricao());
			movtoDesc.setVinculo(recebimento.getVinculo());
			movtoDesc.setDocumento(contaAReceber.getDocumento());
			movtoDesc.setPessoa(contaAReceber.getCliente());
			movtoDesc.setVlrEntrada(null);
			movtoDesc.setVlrSaida(contaAReceber.getDescTB());
			movtoDesc.setTipoLanc(TipoLanc.RR);
			movtoDesc.setTipoConta(TipoConta.D);
			movtoDesc.setPlanoConta(pl1Desc);
			movtoDesc.setPlanoContaPai(pl2Desc);
			listaMovimentacoes.add(movtoDesc);
		}

		listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

		List<Recebimento> list = new ArrayList<Recebimento>();

		ContaAReceberHistorico cpHistorico = new ContaAReceberHistorico();

		cpHistorico.setContaAReceber(contaAReceber);
		cpHistorico.setValorAnterio(vlrAnterio);
		cpHistorico.setValorAtual(contaAReceber.getPagoTB());
		cpHistorico.setUsuario(recebimento.getUsuario());
		cpHistorico.setAgrupadorRecebimento(idAgrupador);
		cpHistorico.setVinculoAnterio(idAgrupadorAnterio);

		cpHistoricoService.salvar(cpHistorico);

		Recebimento pagto = new Recebimento();

		pagto.setDataLanc(new Date());
		pagto.setDataPago(recebimento.getDataPago());
		pagto.setDescricao(recebimento.getDescricao());
		pagto.setFormaBaixa(recebimento.getFormaBaixa());
		pagto.setValor(contaAReceber.getValor());
		pagto.setValorAPagar(contaAReceber.getSaldoDevedor());
		pagto.setValorDesc(contaAReceber.getDescTB());
		pagto.setValorMultaJuros(contaAReceber.getMultaTB());
		pagto.setValorPago(contaAReceber.getPagoTB());
		pagto.setUsuario(recebimento.getUsuario());
		pagto.setListaContaARecebers(listaContaARecebers);
		pagto.setAgrupadorContaAReceber(idAgrupador);
		pagto.setTipoBaixa(recebimento.getTipoBaixa());
		pagto.setListaMovimentacoes(listaMovimentacoes);
		list.add(pagto);

		recebimentoService.salvar(list);

	}

	@Transacional
	public void baixaMultiplas(List<ContaAReceber> listaContaARecebers, List<Movimentacao> listaMovimentacoes,
			List<Recebimento> listaRecebimentos, Recebimento recebimento) {

		if (recebimento.getFormaBaixa() == FormaBaixa.BI) {

			List<ContaAReceber> listaCP = new ArrayList<ContaAReceber>();

			for (ContaAReceber cp : listaContaARecebers) {

				ContaAReceber contaAReceber = new ContaAReceber();
				Long idAgrupador = gerarVinculo.gerar(Recebimento.class);
				Long idAgrupadorAnterio = cp.getVinculo() == null ? idAgrupador : cp.getVinculo();
				BigDecimal vlrAnterio = cp.getSaldoDevedor();

				contaAReceber.setId(cp.getId());
				contaAReceber.setAgrupadorMovimentacao(cp.getAgrupadorMovimentacao());
				contaAReceber.setDataDoc(cp.getDataDoc());
				contaAReceber.setDataVencto(cp.getDataVencto());
				contaAReceber.setDocumento(cp.getDocumento());
				contaAReceber.setParcela(cp.getParcela());
				contaAReceber.setStatus("PAGAMENTO TOTAL");
				contaAReceber.setTipoRecebimento(cp.getTipoRecebimento());
				contaAReceber.setValor(cp.getValor());
				contaAReceber.setValorApagar(cp.getValorApagar().subtract(cp.getPagoTB()));
				contaAReceber.setValorPago(cp.getValorPago().add(cp.getPagoTB()));
				contaAReceber.setVinculo(idAgrupador);
				contaAReceber.setCliente(cp.getCliente());
				contaAReceber.setUsuario(cp.getUsuario());

				if (cp.getValorApagar().compareTo(cp.getPagoTB()) > 0) {
					contaAReceber.setStatus("RECEBIMENTO PARCIAL");
				}

				listaCP.add(contaAReceber);
				dao.baixaSimples(contaAReceber);

				ContaAReceberHistorico cpHistorico = new ContaAReceberHistorico();

				cpHistorico.setContaAReceber(contaAReceber);
				cpHistorico.setValorAnterio(vlrAnterio);
				cpHistorico.setValorAtual(cp.getPagoTB());
				cpHistorico.setUsuario(recebimento.getUsuario());
				cpHistorico.setAgrupadorRecebimento(idAgrupador);
				cpHistorico.setVinculoAnterio(idAgrupadorAnterio);

				cpHistoricoService.salvar(cpHistorico);

			}

			for (int i = 0; i < listaMovimentacoes.size(); i++) {

				Movimentacao movto = new Movimentacao();
				Recebimento pagto = new Recebimento();
				ContaAReceber cp = new ContaAReceber();

				List<Recebimento> list = new ArrayList<Recebimento>();
				List<Movimentacao> listM = new ArrayList<Movimentacao>();
				List<ContaAReceber> listCP = new ArrayList<ContaAReceber>();

				cp.setId(listaCP.get(i).getId());
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

				if (listaContaARecebers.get(i).getMultaTB().compareTo(BigDecimal.ZERO) > 0) {

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
					movtoMulta.setVlrSaida(listaContaARecebers.get(i).getMultaTB());
					movtoMulta.setTipoLanc(TipoLanc.PC);
					movtoMulta.setTipoConta(TipoConta.D);
					movtoMulta.setPlanoConta(pl1Multa);
					movtoMulta.setPlanoContaPai(pl2Multa);
					listM.add(movtoMulta);
				}

				if (listaContaARecebers.get(i).getDescTB().compareTo(BigDecimal.ZERO) > 0) {

					Movimentacao movtoDesc = new Movimentacao();

					PlanoConta pl1Desc = new PlanoConta();
					pl1Desc = contaService.porNome("RECEITAS C/DESC./JUROS E MULTA");

					PlanoConta pl2Desc = new PlanoConta();
					pl2Desc = contaService.porId(pl1Desc.getContaPai().getId());

					movtoDesc.setDataDoc(listaMovimentacoes.get(i).getDataDoc());
					movtoDesc.setDataLanc(listaMovimentacoes.get(i).getDataLanc());
					movtoDesc.setUsuario(listaMovimentacoes.get(i).getUsuario());
					movtoDesc.setDescricao("REC DESCONTO " + listaMovimentacoes.get(i).getDescricao());
					movtoDesc.setVinculo(listaMovimentacoes.get(i).getVinculo());
					movtoDesc.setDocumento(listaMovimentacoes.get(i).getDocumento());
					movtoDesc.setPessoa(listaMovimentacoes.get(i).getPessoa());
					movtoDesc.setVlrEntrada(listaContaARecebers.get(i).getDescTB());
					movtoDesc.setVlrSaida(null);
					movtoDesc.setTipoLanc(TipoLanc.PC);
					movtoDesc.setTipoConta(TipoConta.R);
					movtoDesc.setPlanoConta(pl1Desc);
					movtoDesc.setPlanoContaPai(pl2Desc);
					listM.add(movtoDesc);
				}

				listM = movtoService.salvar(listM);

				pagto.setDataLanc(new Date());
				pagto.setDataPago(listaRecebimentos.get(i).getDataPago());
				pagto.setDescricao(listaRecebimentos.get(i).getDescricao());
				pagto.setFormaBaixa(FormaBaixa.BI);
				pagto.setValor(listaRecebimentos.get(i).getValor());
				pagto.setValorDesc(listaCP.get(i).getDescTB());
				pagto.setValorMultaJuros(listaCP.get(i).getMultaTB());
				pagto.setValorAPagar(listaCP.get(i).getSaldoDevedor()
						.add(listaCP.get(i).getMultaTB().subtract(listaCP.get(i).getDescTB())));
				pagto.setValorPago(listaRecebimentos.get(i).getValorPago());
				pagto.setUsuario(listaRecebimentos.get(i).getUsuario());
				pagto.setListaContaARecebers(listaCP);
				pagto.setAgrupadorContaAReceber(listaCP.get(i).getVinculo());
				pagto.setTipoBaixa(listaRecebimentos.get(i).getTipoBaixa());
				pagto.setListaMovimentacoes(listM);
				list.add(pagto);

				recebimentoService.salvar(list);
			}
		} else {

			BigDecimal valorMutla = BigDecimal.ZERO;
			BigDecimal valorDesc = BigDecimal.ZERO;

			Long idAgrupador = gerarVinculo.gerar(Recebimento.class);

			for (ContaAReceber cp : listaContaARecebers) {
				
				BigDecimal valorAnterio = cp.getValorApagar();

				ContaAReceber contaAReceber = new ContaAReceber();
				contaAReceber.setId(cp.getId());
				contaAReceber.setStatus("PAGAMENTO TOTAL");
				contaAReceber.setValorPago(cp.getValorPago().add(cp.getPagoTB()));
				contaAReceber.setValorApagar(cp.getValorApagar().subtract(cp.getPagoTB()));
				contaAReceber.setVinculo(idAgrupador);
				if (cp.getValorApagar().compareTo(cp.getPagoTB()) > 0) {
					contaAReceber.setStatus("PAGAMENTO PARCIAL");
				}

				dao.baixaSimples(contaAReceber);

				ContaAReceberHistorico cpHistorico = new ContaAReceberHistorico();

				cpHistorico.setContaAReceber(contaAReceber);
				cpHistorico.setValorAnterio(valorAnterio);
				cpHistorico.setValorAtual(cp.getPagoTB());
				cpHistorico.setUsuario(recebimento.getUsuario());
				cpHistorico.setAgrupadorRecebimento(idAgrupador);
				cpHistorico.setVinculoAnterio(cp.getVinculo() == null ? idAgrupador : cp.getVinculo());

				cpHistoricoService.salvar(cpHistorico);

			}

			for (int j = 0; j < listaContaARecebers.size(); j++) {
				if (listaContaARecebers.get(j).getMultaTB().compareTo(BigDecimal.ZERO) > 0) {
					valorMutla = valorMutla.add(listaContaARecebers.get(j).getMultaTB());
				}
				if (listaContaARecebers.get(j).getDescTB().compareTo(BigDecimal.ZERO) > 0) {
					valorDesc = valorDesc.add(listaContaARecebers.get(j).getDescTB());
				}
			}

			if (valorMutla.compareTo(BigDecimal.ZERO) > 0) {

				Movimentacao movtoMulta = new Movimentacao();

				PlanoConta pl1Multa = new PlanoConta();
				pl1Multa = contaService.porNome("JUROS/MULTA CP");

				PlanoConta pl2Multa = new PlanoConta();
				pl2Multa = contaService.porId(pl1Multa.getContaPai().getId());

				movtoMulta.setDataDoc(recebimento.getDataPago());
				movtoMulta.setDataLanc(recebimento.getDataPago());
				movtoMulta.setUsuario(recebimento.getUsuario());
				movtoMulta.setDescricao("PG JURUOS/MULTA ");
				movtoMulta.setVinculo(recebimento.getVinculo());
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

				movtoDesc.setDataDoc(recebimento.getDataPago());
				movtoDesc.setDataLanc(recebimento.getDataPago());
				movtoDesc.setUsuario(recebimento.getUsuario());
				movtoDesc.setDescricao("RECEBIMENTO DE DESCONTOS ");
				movtoDesc.setVinculo(recebimento.getVinculo());
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

			List<Recebimento> list = new ArrayList<>();

			for (Recebimento pagto : listaRecebimentos) {

				Recebimento p = new Recebimento();

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
				p.setListaContaARecebers(listaContaARecebers); //
				p.setAgrupadorContaAReceber(idAgrupador);
				p.setListaMovimentacoes(listaMovimentacoes);
				list.add(p);
			}

			recebimentoService.salvar(list);

		}

	}

}

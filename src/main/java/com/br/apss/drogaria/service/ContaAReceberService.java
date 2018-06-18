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
	private ContaAReceberHistoricoService rcHistoricoService;

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

		dao.updateNasContasAReceber(contaAReceber);

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

		rcHistoricoService.salvar(cpHistorico);

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

	@Transacional // Baixar varias contas de uma so vez
	public void baixaMultiplas(List<ContaAReceber> listaContaARecebers, List<Movimentacao> listaMovimentacoes,
			List<Recebimento> listaRecebimentos, Recebimento recebimento) {

		// Verificar a condicao se é baixa individual (BI) ou baixa agrupada (BA)
		if (recebimento.getFormaBaixa() == FormaBaixa.BI) {
			// Baixa individual
			for (int i = 0; i < listaContaARecebers.size(); i++) {
				ContaAReceber contaAReceber = listaContaARecebers.get(i);
				Recebimento rec = listaRecebimentos.get(i);
				baixaSimples(contaAReceber, rec);
			}

		} else {
			// Baixa agrupada
			BigDecimal valorMulta = BigDecimal.ZERO;
			BigDecimal valorDesc = BigDecimal.ZERO;

			Long idAgrupador = gerarVinculo.gerar(Recebimento.class);

			for (ContaAReceber cp : listaContaARecebers) {

				ContaAReceber contaAReceber = new ContaAReceber();
				contaAReceber.setId(cp.getId());
				contaAReceber.setValorApagar(cp.getValorApagar().subtract(cp.getPagoTB()));
				contaAReceber.setVinculo(idAgrupador);
				// Altera o valor de cada titulo a receber
				dao.updateNasContasAReceber(contaAReceber);

			}

			// Unifica os valores de multas e descontos
			for (int j = 0; j < listaContaARecebers.size(); j++) {
				if (listaContaARecebers.get(j).getMultaTB().compareTo(BigDecimal.ZERO) > 0) {
					valorMulta = valorMulta.add(listaContaARecebers.get(j).getMultaTB());
				}
				if (listaContaARecebers.get(j).getDescTB().compareTo(BigDecimal.ZERO) > 0) {
					valorDesc = valorDesc.add(listaContaARecebers.get(j).getDescTB());
				}
			}

			// Se existir valor de multar add na lista de movimentacao
			if (valorMulta.compareTo(BigDecimal.ZERO) > 0) {

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
				movtoMulta.setVlrEntrada(valorMulta);
				movtoMulta.setVlrSaida(null);
				movtoMulta.setTipoLanc(TipoLanc.RR);
				movtoMulta.setTipoConta(TipoConta.R);
				movtoMulta.setPlanoConta(pl1Multa);
				movtoMulta.setPlanoContaPai(pl2Multa);
				listaMovimentacoes.add(movtoMulta);
			}

			// Se existir valor de desconto add na lista de movimentacao
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
				movtoDesc.setVlrEntrada(null);
				movtoDesc.setVlrSaida(valorDesc);
				movtoDesc.setTipoLanc(TipoLanc.RR);
				movtoDesc.setTipoConta(TipoConta.D);
				movtoDesc.setPlanoConta(pl1Desc);
				movtoDesc.setPlanoContaPai(pl2Desc);
				listaMovimentacoes.add(movtoDesc);

			}

			// Salvar toda movimentação na tabela e retorna a lista salvar
			listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

			// Criar uma nova lista do tipo recebimento para receber novas atualizações
			List<Recebimento> list = new ArrayList<Recebimento>();

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
				// Carregar a nova lista
				list.add(p);
			}

			// Salvando a nova lista no banco e recuperar a mesma
			List<Recebimento> listaRec = recebimentoService.salvar(list);

			for (Recebimento pagto : listaRec) {

				for (ContaAReceber rc : listaContaARecebers) {
					ContaAReceberHistorico rcHistorico = new ContaAReceberHistorico();
					rcHistorico.setContaAReceber(rc);
					rcHistorico.setValorAnterio(rc.getSaldoDevedor());
					rcHistorico.setValorAtual(rc.getValorApagar().subtract(rc.getPagoTB()));
					rcHistorico.setValorPago(rc.getPagoTB());
					rcHistorico.setData(new Date());
					rcHistorico.setUsuario(recebimento.getUsuario());
					rcHistorico.setValorDesc(rc.getDescTB());
					rcHistorico.setRecebimento(pagto.getId());
					rcHistorico.setValorMultaJuros(rc.getMultaTB());
					rcHistorico.setAgrupadorRecebimento(idAgrupador);
					rcHistorico.setVinculoAnterio(rc.getVinculo() == null ? idAgrupador : rc.getVinculo());
					// Salvando o historico do recebimento do titulo
					rcHistoricoService.salvar(rcHistorico);
				}
			}

			Messages.addGlobalInfo("Titulo(s) recebido(s) com sucesso.");

		}

	}

}

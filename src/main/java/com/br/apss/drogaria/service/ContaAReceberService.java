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
	public void excluirContas(List<ContaAReceber> contas) {
		try {
			dao.excluirContas(contas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transacional
	public void baixaSimples(ContaAReceber contaAReceber, Recebimento recebimento) {

		List<ContaAReceber> listaContaARecebers = new ArrayList<ContaAReceber>();

		// Gerador do codigo de vinculo
		Long idAgrupador = gerarVinculo.gerar(Recebimento.class);
		Long idAgrupadorAnterio = contaAReceber.getVinculo() == null ? idAgrupador : contaAReceber.getVinculo();

		BigDecimal vlrAnterio = contaAReceber.getSaldoDevedor();

		recebimento.setTipoBaixa(TipoBaixa.TOTAL);
		contaAReceber.setUsuario(recebimento.getUsuario());
		contaAReceber.setSaldoDevedor(contaAReceber.getValorApagar());
		contaAReceber.setVinculo(idAgrupador);
		if (contaAReceber.getValorApagar().compareTo(contaAReceber.getPagoTB()) > 0) {
			recebimento.setTipoBaixa(TipoBaixa.PARCIAL);
			recebimento.setDescricao(recebimento.getDescricao() + " (R)");
		}
		contaAReceber.setValorApagar(contaAReceber.getValorApagar().subtract(contaAReceber.getPagoTB()));

		// Seta o objeto na lista de contas a receber para vincular ao recebimento
		listaContaARecebers.add(contaAReceber);

		// Update na tabela conta a receber
		dao.updateNasContasAReceber(contaAReceber);

		// Lista de Movimentação
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
		movto.setDocumento(contaAReceber.getNumDoc());
		movto.setPessoa(contaAReceber.getCliente());
		movto.setVlrEntrada(contaAReceber.getPagoTB());
		movto.setVlrSaida(null);
		movto.setTipoLanc(TipoLanc.RR);
		movto.setTipoConta(TipoConta.CC);
		movto.setPlanoConta(pl1);
		movto.setPlanoContaPai(pl2);

		// Add objeto movimentação na lista para salvar posterior
		listaMovimentacoes.add(movto);

		// Criação do objeto movimentação caso aja multa
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
			movtoMulta.setDocumento(contaAReceber.getNumDoc());
			movtoMulta.setPessoa(contaAReceber.getCliente());
			movtoMulta.setVlrEntrada(contaAReceber.getMultaTB());
			movtoMulta.setVlrSaida(null);
			movtoMulta.setTipoLanc(TipoLanc.RR);
			movtoMulta.setTipoConta(TipoConta.R);
			movtoMulta.setPlanoConta(pl1Multa);
			movtoMulta.setPlanoContaPai(pl2Multa);
			// Add na lista de movimentação
			listaMovimentacoes.add(movtoMulta);
		}

		// Criação do objeto movimentação caso aja desconto
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
			movtoDesc.setDocumento(contaAReceber.getNumDoc());
			movtoDesc.setPessoa(contaAReceber.getCliente());
			movtoDesc.setVlrEntrada(null);
			movtoDesc.setVlrSaida(contaAReceber.getDescTB());
			movtoDesc.setTipoLanc(TipoLanc.RR);
			movtoDesc.setTipoConta(TipoConta.D);
			movtoDesc.setPlanoConta(pl1Desc);
			movtoDesc.setPlanoContaPai(pl2Desc);
			// Add na lista de movimentação
			listaMovimentacoes.add(movtoDesc);
		}

		// Salvar a lista de movimentação na tabela e recuperando a mesma
		listaMovimentacoes = movtoService.salvar(listaMovimentacoes);

		// Criação da lista de recebimento para receber os objetos
		List<Recebimento> list = new ArrayList<Recebimento>();

		Recebimento recto = new Recebimento();
		recto.setDataLanc(new Date());
		recto.setDataPago(recebimento.getDataPago());
		recto.setDescricao(recebimento.getDescricao());
		recto.setFormaBaixa(recebimento.getFormaBaixa());
		recto.setValor(contaAReceber.getValor());
		recto.setValorAPagar(contaAReceber.getSaldoDevedor());
		recto.setValorDesc(contaAReceber.getDescTB());
		recto.setValorMultaJuros(contaAReceber.getMultaTB());
		recto.setValorPago(contaAReceber.getPagoTB());
		recto.setUsuario(recebimento.getUsuario());
		recto.setListaContaARecebers(listaContaARecebers);
		recto.setAgrupadorContaAReceber(idAgrupador);
		recto.setTipoBaixa(recebimento.getTipoBaixa());
		recto.setListaMovimentacoes(listaMovimentacoes);
		recto.setCliente(listaContaARecebers.get(0).getCliente());
		// Add na lista
		list.add(recto);

		// Salvando a lista de recebimento preenchida e recuperando a mesma
		List<Recebimento> listaRec = recebimentoService.salvar(list);

		// Criação do objeto conta a receber historico
		ContaAReceberHistorico rcHistorico = new ContaAReceberHistorico();

		rcHistorico.setVinculoAnterio(idAgrupadorAnterio);

		rcHistorico.setContaAReceber(contaAReceber);
		rcHistorico.setValorAnterio(vlrAnterio);
		rcHistorico.setValorAtual(contaAReceber.getValorApagar());
		rcHistorico.setValorPago(contaAReceber.getPagoTB());
		rcHistorico.setUsuario(recebimento.getUsuario());
		rcHistorico.setData(new Date());
		rcHistorico.setRecebimento(listaRec.get(0).getId());
		rcHistorico.setValorDesc(contaAReceber.getDescTB());
		rcHistorico.setValorMultaJuros(contaAReceber.getMultaTB());
		rcHistorico.setAgrupadorRecebimento(idAgrupador);
		rcHistorico.setVinculoAnterio(idAgrupadorAnterio);

		// Salvando o objeto historico preenchindo
		rcHistoricoService.salvar(rcHistorico);

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
				// Alterar o valor de cada titulo a receber
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
				movtoMulta.setPessoa(listaContaARecebers.get(0).getCliente());
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
				pl1Desc = contaService.porNome("RECEITAS C/DESC./JUROS E MULTA");

				PlanoConta pl2Desc = new PlanoConta();
				pl2Desc = contaService.porId(pl1Desc.getContaPai().getId());

				movtoDesc.setDataDoc(recebimento.getDataPago());
				movtoDesc.setDataLanc(recebimento.getDataPago());
				movtoDesc.setUsuario(recebimento.getUsuario());
				movtoDesc.setDescricao("RECEBIMENTO DE DESCONTOS ");
				movtoDesc.setVinculo(recebimento.getVinculo());
				movtoDesc.setDocumento(null);
				movtoDesc.setPessoa(listaContaARecebers.get(0).getCliente());
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

				Recebimento recto = new Recebimento();

				recto.setDataLanc(new Date());
				recto.setDataPago(pagto.getDataPago());
				recto.setDescricao(pagto.getDescricao());
				recto.setFormaBaixa(pagto.getFormaBaixa());
				recto.setValor(pagto.getValor());
				recto.setValorAPagar(pagto.getValorAPagar());
				recto.setValorDesc(pagto.getValorDesc());
				recto.setValorMultaJuros(pagto.getValorMultaJuros());
				recto.setValorPago(pagto.getValorPago());
				recto.setUsuario(pagto.getUsuario());
				recto.setListaContaARecebers(listaContaARecebers); //
				recto.setAgrupadorContaAReceber(idAgrupador);
				recto.setListaMovimentacoes(listaMovimentacoes);
				recto.setCliente(listaContaARecebers.get(0).getCliente());
				// Carregar a nova lista
				list.add(recto);
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

package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.ContaAReceberHistorico;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Recebimento;
import com.br.apss.drogaria.model.filter.RecebimentoFilter;
import com.br.apss.drogaria.repository.ContaAReceberRepository;
import com.br.apss.drogaria.repository.RecebimentoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class RecebimentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private RecebimentoRepository dao;

	@Inject
	private ContaAReceberRepository contaAReceberRepository;

	@Inject
	private ContaAReceberHistoricoService rcHistoricoService;

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
			// ContaAReceberRepository.baixaSimples(cp);
		}
	}

	@Transacional
	public void cancelarRecebimento(List<Recebimento> listaRecebimento) {

		List<ContaAReceberHistorico> listaContaAReceberHistorico = rcHistoricoService
				.listaVinculo(listaRecebimento.get(0).getAgrupadorContaAReceber());

		ContaAReceberHistorico cp = listaContaAReceberHistorico.get(listaContaAReceberHistorico.size() - 1);

		Long cph = rcHistoricoService.maxId(cp);
		ContaAReceberHistorico cp2 = rcHistoricoService.porId(cph);

		Format f = new SimpleDateFormat("dd/MM/yyyy");
		Recebimento p = dao.porId(cp2.getRecebimento());

		if (!cp.equals(cp2)) {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("É necessário cancelar primeiro o recebimento de código: " + cp2.getRecebimento()
					+ "\n" + "recebido em " + f.format(p.getDataPago()));
		}

		for (Recebimento p2 : listaRecebimento) {
			// Excluir os registros da tabela recebimento_conta_arecebe
			dao.excluirRecebimentoContaAReceber(p2.getId());
			// Excluir os registros da tabela recebimento_movimentacao
			dao.excluirRecebimentoMovimentacao(p2.getId());
		}

		List<Movimentacao> movto = listaRecebimento.get(0).getListaMovimentacoes();
		for (Movimentacao m : movto) {
			// Excluir os registros da tabela movimentacao
			dao.excluirMovimentacao(m.getId());
		}

		// Excluir os registros da tabela recebimento
		dao.excluirPorVinculo(listaRecebimento.get(0).getAgrupadorContaAReceber());

		List<ContaAReceberHistorico> listaCPHistorico = rcHistoricoService
				.listaVinculo(listaRecebimento.get(0).getAgrupadorContaAReceber());

		for (ContaAReceberHistorico cph1 : listaCPHistorico) {
			ContaAReceber cp3 = new ContaAReceber();
			BigDecimal vlr = (cph1.getValorAtual().add(cph1.getValorPago().add(cph1.getValorDesc())))
					.subtract(cph1.getValorMultaJuros());

			cp3.setId(cph1.getContaAReceber().getId());
			cp3.setValorApagar(vlr);
			cp3.setVinculo(cph1.getVinculoAnterio());
			// Atualizar os registros da tabela conta_areceber
			contaAReceberRepository.cancelarRecebimento(cp3);
		}

		// Excluir os registros da tabela historico_conta_areceber
		for (ContaAReceberHistorico cph2 : listaCPHistorico) {
			rcHistoricoService.excluir(cph2);
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

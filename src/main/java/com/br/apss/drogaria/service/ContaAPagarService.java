package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.TipoBaixa;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoLanc;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class ContaAPagarService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ContaAPagarRepository dao;

	@Inject
	private MovimentacaoService movtoDao;

	@Inject
	private PagamentoService pagamentoService;

	@Transacional
	public void salvar(ContaAPagar contaAPagar) {
		dao.salvar(contaAPagar);
	}

	@Transacional
	public void baixaSimples(ContaAPagar contaAPagar, Pagamento pagamento) {
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal d = BigDecimal.ZERO;
		BigDecimal p = BigDecimal.ZERO;
		m = m.add(contaAPagar.getValorMultaJuros().add(contaAPagar.getMulta()));
		d = d.add(contaAPagar.getValorDesc().add(contaAPagar.getDesc()));
		p = p.add(contaAPagar.getValorPago().add(contaAPagar.getPago()));

		contaAPagar.setValorApagar((contaAPagar.getValor().add(m).subtract(d)).subtract(p));
		contaAPagar.setStatus("PENDENTE");
		pagamento.setTipoBaixa(TipoBaixa.PARCIAL);
		contaAPagar.setValorMultaJuros(m);
		contaAPagar.setValorDesc(d);
		contaAPagar.setValorPago(p);
		if (contaAPagar.getValorApagar().compareTo(BigDecimal.ZERO) == 0) {
			contaAPagar.setStatus("PAGO");
			pagamento.setTipoBaixa(TipoBaixa.TOTAL);
		}

		dao.baixaSimples(contaAPagar);

		Movimentacao movto = new Movimentacao();
		movto.setDataDoc(pagamento.getDataPago());
		movto.setDataLanc(pagamento.getDataPago());
		movto.setUsuario(pagamento.getUsuario());
		movto.setDescricao(pagamento.getDescricao());
		movto.setDocumento(contaAPagar.getNumDoc());
		movto.setVinculo(contaAPagar.getVinculo());
		movto.setPessoa(contaAPagar.getFornecedor());
		movto.setVlrEntrada(contaAPagar.getPago());
		movto.setVlrSaida(null);
		movto.setTipoLanc(TipoLanc.PC);
		movto.setTipoConta(TipoConta.CC);
		movto.setPlanoConta(pagamento.getMovimentacao().getPlanoConta());
		movto.setPlanoContaPai(pagamento.getMovimentacao().getPlanoConta().getContaPai());
		movto = movtoDao.salvar(movto);

		pagamento.setValor(contaAPagar.getValor());
		pagamento.setValorDesc(d);
		pagamento.setValorMultaJuros(m);
		pagamento.setValorPago(contaAPagar.getPago());
		pagamento.setDataLanc(pagamento.getDataPago());
		pagamento.setContaAPagar(contaAPagar);
		pagamento.setUsuario(movto.getUsuario());
		pagamento.setMovimentacao(movto);

		pagamentoService.salvar(pagamento);

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

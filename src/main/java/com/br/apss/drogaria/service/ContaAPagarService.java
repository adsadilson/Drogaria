package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAPagar;
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
	public void baixaSimples(List<ContaAPagar> listaContaAPagars, List<Pagamento> listaPagamentos) {

//		BigDecimal m = BigDecimal.ZERO;
//		BigDecimal d = BigDecimal.ZERO;
//		BigDecimal p = BigDecimal.ZERO;
//
//		BigDecimal vApagar = contaAPagar.getValorApagar();
//		String descricao = pagamento.getDescricao();
//
//		m = m.add(contaAPagar.getValorMultaJuros().add(contaAPagar.getMulta()));
//		d = d.add(contaAPagar.getValorDesc().add(contaAPagar.getDesc()));
//		p = p.add(contaAPagar.getValorPago().add(contaAPagar.getPago()));
//
//		contaAPagar.setValorApagar((contaAPagar.getValor().add(m).subtract(d)).subtract(p));
//		contaAPagar.setStatus("PENDENTE");
//		pagamento.setDescricao(pagamento.getDescricao() + " (P)");
//		pagamento.setTipoBaixa(TipoBaixa.PARCIAL);
//		contaAPagar.setValorMultaJuros(m);
//		contaAPagar.setValorDesc(d);
//		contaAPagar.setValorPago(p);
//		if (contaAPagar.getValorApagar().compareTo(BigDecimal.ZERO) == 0) {
//			contaAPagar.setStatus("PAGO");
//			pagamento.setTipoBaixa(TipoBaixa.TOTAL);
//			pagamento.setDescricao(descricao);
//		}
//
//		dao.baixaSimples(contaAPagar);
//
//		Movimentacao movto = new Movimentacao();
//		movto.setDataDoc(pagamento.getDataPago());
//		movto.setDataLanc(pagamento.getDataPago());
//		movto.setUsuario(pagamento.getUsuario());
//		movto.setDescricao(pagamento.getDescricao());
//		movto.setDocumento(contaAPagar.getNumDoc());
//		movto.setVinculo(null);
//		movto.setPessoa(contaAPagar.getFornecedor());
//		movto.setVlrEntrada(null);
//		movto.setVlrSaida(contaAPagar.getPago());
//		movto.setTipoLanc(TipoLanc.PC);
//		movto.setTipoConta(TipoConta.CC);
//		movto.setPlanoConta(pagamento.getMovimentacao().getPlanoConta());
//		movto.setPlanoContaPai(pagamento.getMovimentacao().getPlanoConta().getContaPai());
//		movto = movtoDao.salvar(movto);
//
//		pagamento.setValor(contaAPagar.getValor());
//		pagamento.setValorDesc(contaAPagar.getDesc());
//		pagamento.setValorMultaJuros(contaAPagar.getMulta());
//		pagamento.setValorPago(contaAPagar.getPago());
//		pagamento.setDataLanc(pagamento.getDataPago());
//		pagamento.setUsuario(movto.getUsuario());
//		pagamento.setValorAPagar(vApagar);
//		pagamento.setContaAPagar(contaAPagar);
//		pagamento.setMovimentacao(movto);
//
//		pagamentoService.salvar(pagamento);

	}

	@Transacional
	public void baixaMultiplas(List<ContaAPagar> listaContaAPagars, List<Pagamento> listaPagamentos) {

		for (ContaAPagar cp : listaContaAPagars) {

			BigDecimal m = BigDecimal.ZERO;
			BigDecimal d = BigDecimal.ZERO;
			BigDecimal p = BigDecimal.ZERO;

			m = m.add(cp.getValorMultaJuros().add(cp.getMultaTB()));
			d = d.add(cp.getValorDesc().add(cp.getDescTB()));
			p = p.add(cp.getValorPago().add(cp.getPagoTB()));

			ContaAPagar c = new ContaAPagar();
			c.setId(cp.getId());
			c.setStatus("PENDENTE");
			c.setValorMultaJuros(m);
			c.setValorDesc(d);
			c.setValorPago(p);
			c.setValorApagar((cp.getValor().add(m).subtract(d)).subtract(p));

			if (cp.getValorApagar().compareTo(cp.getPagoTB()) == 0) {
				c.setStatus("PAGO");
			}
			dao.baixaSimples(c);
		}


			pagamentoService.salvar(listaPagamentos);

			
			
			/*pagamento.setDataLanc(pagto.getDataLanc());
			pagamento.setDataPago(pagto.getDataPago());
			pagamento.setDescricao(pagto.getDescricao());
			pagamento.setFormaBaixa(pagto.getFormaBaixa());
			pagamento.setValor(pagto.getContaAPagar().getValor());
			pagamento.setValorAPagar(pagto.getContaAPagar().getValorApagar());
			pagamento.setValorDesc(pagto.getContaAPagar().getDescTB());
			pagamento.setValorMultaJuros(pagto.getContaAPagar().getMultaTB());
			pagamento.setValorPago(pagto.getValorPago());
			pagamento.setUsuario(pagto.getUsuario());
			pagamento.setContaAPagar(pagto.getContaAPagar());
			pagamento.setMovimentacao(movto);

			*/

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

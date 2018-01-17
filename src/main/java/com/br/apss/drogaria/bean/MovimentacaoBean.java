package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.MovimentacaoFilter;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.security.LoginBean;
import com.br.apss.drogaria.service.MovimentacaoService;
import com.br.apss.drogaria.service.PlanoContaService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;

@Named
@ViewScoped
public class MovimentacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Movimentacao movto = new Movimentacao();
	private Movimentacao movtoSelecionado;
	private MovimentacaoFilter filtro;
	private List<Movimentacao> listaMovimentacoes = new ArrayList<Movimentacao>();
	private List<PlanoConta> listaPlanoContas = new ArrayList<PlanoConta>();
	private BigDecimal valor = BigDecimal.ZERO;
	private LazyDataModel<Movimentacao> model;
	private BigDecimal saldo;

	private Long contaDestino;

	@Inject
	private PlanoContaService contaService;

	@Inject
	MovimentacaoService movimentacaoService;

	@Inject
	GeradorVinculo idVinculo;

	@Inject
	LoginBean user;

	@PostConstruct
	public void Inicializar() {
		filtro = new MovimentacaoFilter();
		filtro.setDataIni(new Date());
		filtro.setDataFim(new Date());
		movto = new Movimentacao();
	}

	public List<PlanoConta> getContas() {
		PlanoContaFilter cc = new PlanoContaFilter();
		cc.setTipo(TipoConta.CC);
		return contaService.filtrados(cc);
	}

	public boolean desabilitarInfo(Movimentacao m) {
		if (m.equals(this.movtoSelecionado)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean desabilitar(Movimentacao m) {
		if (m.equals(this.movtoSelecionado)) {
			return true;
		}
		return false;
	}

	public Boolean validarDatas(Date ini, Date fim) {
		if (ini != null && fim != null) {
			if (fim.before(ini)) {
				return true;
			}
		}
		return false;
	}

	public void pesquisar() {
		if (!validarDatas(filtro.getDataIni(), filtro.getDataFim())) {
			model = new LazyDataModel<Movimentacao>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Movimentacao> load(int first, int pageSize, String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {

					setRowCount(movimentacaoService.quantidadeFiltrados(filtro));

					filtro.setPrimeiroRegistro(first);
					filtro.setQtdeRegistro(pageSize);
					filtro.setOrdenacao(sortField);
					filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));

					listaMovimentacoes = movimentacaoService.filtrados(filtro);

					if (listaMovimentacoes != null) {
						saldo = movimentacaoService.pesquisaSaldo(filtro);
						BigDecimal saldoIni = saldo;
						if (null == saldo) {
							saldo = BigDecimal.ZERO;
						}

						for (Movimentacao m : listaMovimentacoes) {
							if (null != m.getVlrSaida()) {
								saldo = saldo.subtract(m.getVlrSaida());
								m.setVlrSaldo(saldo);
							} else if (null != m.getVlrEntrada()) {
								saldo = saldo.add(m.getVlrEntrada());
								m.setVlrSaldo(saldo);
							}
						}
						saldo = saldoIni;
					}
					return listaMovimentacoes;
				}

				@Override
				public Movimentacao getRowData(String rowKey) {
					movtoSelecionado = movimentacaoService.porId(Long.valueOf(rowKey));
					return movtoSelecionado;
				}

				@Override
				public String getRowKey(Movimentacao objeto) {
					return movtoSelecionado.getId().toString();
				}

			};

			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
		} else {
			Messages.addGlobalError("Data inicio maior do que data final!");
		}

	}

	public void novoCadastro() {
		this.movto = new Movimentacao();
		this.movto.setVlrEntrada(null);
	}

	public void editar() {
		if (this.movtoSelecionado.getVlrEntrada() == null && this.movtoSelecionado.getVinculo() != null) {
			this.movto = movimentacaoService.porVinculo(movtoSelecionado.getVinculo(), movtoSelecionado.getId());
		} else {
			this.movto = movtoSelecionado;
		}

		// carregarContasLanc();

	}

	public void excluir() {
		if (this.movtoSelecionado.getVinculo() != null) {
			movimentacaoService.excluirPorVinculo(this.movtoSelecionado.getVinculo());
		} else {
			movimentacaoService.excluir(this.movtoSelecionado);
		}
		this.movtoSelecionado = null;
		pesquisar();
		Messages.addGlobalInfo("Registro excluido com sucesso");
	}

	/*
	 * public String getContaDestino() { if (null != movtoSelecionado) { if
	 * (null == movtoSelecionado.getSubConta() &&
	 * movtoSelecionado.getContaTipo().getId() == 1) { return
	 * movimentacaoService.porVinculo(movtoSelecionado.getVinculo(),
	 * movtoSelecionado.getId()) .getConta().getNome(); } else { return
	 * contaService.porId(movtoSelecionado.getSubConta().getId()).getNome(); } }
	 * return null; }
	 */

	@SuppressWarnings("unused")
	private Usuario obterUsuario() {
		Usuario usuario = user.getUsuario();
		return usuario;
	}

	public void salvar() {
		int contador = 0;
		this.movto.setVinculo(idVinculo.gerar(Movimentacao.class));
		while (contador <= 1) {
			if (contador == 0) {
				if (this.movto.getPlanoConta().getTipo().getSigla().contains("R")) {
					this.movto.setVlrEntrada(valor);
					this.movto.setVlrSaida(null);
				} else {
					this.movto.setVlrSaida(valor);
					this.movto.setVlrEntrada(null);
				}
				this.movto.getPlanoConta().setId(filtro.getContaID());

			} else {
				if (this.movto.getPlanoConta().getTipo().getSigla().contains("R")) {
					this.movto.setVlrSaida(valor);
					this.movto.setVlrEntrada(null);
				} else {
					this.movto.setVlrEntrada(valor);
					this.movto.setVlrSaida(null);
				}
				this.movto.getPlanoConta().setId(this.contaDestino);
			}
			this.movto.setDataLanc(new Date());
			// this.movto.setUsuario(obterUsuario());
			movimentacaoService.salvar(this.movto);
			contador++;
		}
		this.movtoSelecionado = null;
		this.movto = new Movimentacao();
		this.valor = BigDecimal.ZERO;
		pesquisar();
		carregarContasLanctos();
		Messages.addGlobalInfo("Registro salvo com sucesso");
	}

	public void transferecia() {
		if (!verificarContaTransf()) {
			if (this.movto.getId() == null) {
				this.movto.setDataLanc(new Date());
				// this.movto.setUsuario(obterUsuario());
				this.movto.setVinculo(idVinculo.gerar(Movimentacao.class));
				movimentacaoService.salvar(this.movto);

				PlanoConta c = new PlanoConta();
				c.setId(filtro.getContaID());
				// this.movto.setConta(c);
				if (this.movto.getVlrSaida() != null) {
					this.movto.setVlrEntrada(this.movto.getVlrSaida());
					this.movto.setVlrSaida(null);
				} else {
					this.movto.setVlrSaida(this.movto.getVlrEntrada());
					this.movto.setVlrEntrada(null);
				}

				movimentacaoService.salvar(this.movto);
			} else {

				movimentacaoService.salvar(this.movto);
				Movimentacao movtoAlt = movimentacaoService.porVinculo(this.movto.getVinculo(), this.movto.getId());

				movtoAlt.setDescricao(this.movto.getDescricao());
				movtoAlt.setDataDoc(this.movto.getDataDoc());
				movtoAlt.setDocumento(this.movto.getDocumento());
				movtoAlt.setVlrEntrada(movto.getVlrSaida());
				movtoAlt.setVlrSaida(movto.getVlrEntrada());

				movimentacaoService.salvar(movtoAlt);
			}
			this.movtoSelecionado = null;
			this.movto = new Movimentacao();
			this.valor = BigDecimal.ZERO;
			pesquisar();
			Messages.addGlobalInfo("Registro salvo com sucesso");
		}
	}

	public Boolean verificarContaTransf() {
		if (this.movto.getPlanoConta().getId() == filtro.getContaID()) {
			Messages.addGlobalError("Selecionar outra conta diferente da: " + this.movto.getPlanoConta().getNome());
			return true;
		}
		return false;
	}

	public String getSaldoMoeda() {
		if (null != saldo) {
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			String formatado = nf.format(saldo);
			return formatado;
		} else {
			return "";
		}
	}

	public List<TipoConta> getTipoConta() {
		return Arrays.asList(TipoConta.D, TipoConta.R);
	}

	public void carregarContasLanctos() {
		PlanoContaFilter cl = new PlanoContaFilter();
		if (null != movto.getPlanoConta().getTipo()) {
			cl.setTipo(movto.getPlanoConta().getTipo());
			cl.setStatus(true);
			listaPlanoContas = contaService.filtrados(cl);
		} else {
			listaPlanoContas = new ArrayList<>();
		}
	}

	/********* Gett e Sett ************/

	public Movimentacao getMovto() {
		return movto;
	}

	public void setMovto(Movimentacao movto) {
		this.movto = movto;
	}

	public Movimentacao getMovtoSelecionado() {
		return movtoSelecionado;
	}

	public void setMovtoSelecionado(Movimentacao movtoSelecionado) {
		this.movtoSelecionado = movtoSelecionado;
	}

	public MovimentacaoFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(MovimentacaoFilter filtro) {
		this.filtro = filtro;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public LazyDataModel<Movimentacao> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Movimentacao> model) {
		this.model = model;
	}

	public List<Movimentacao> getListaMovimentacoes() {
		return listaMovimentacoes;
	}

	public void setListaMovimentacoes(List<Movimentacao> listaMovimentacoes) {
		this.listaMovimentacoes = listaMovimentacoes;
	}

	public List<PlanoConta> getListaPlanoContas() {
		return listaPlanoContas;
	}

	public void setListaPlanoContas(List<PlanoConta> listaPlanoContas) {
		this.listaPlanoContas = listaPlanoContas;
	}

	public GeradorVinculo getIdVinculo() {
		return idVinculo;
	}

	public void setIdVinculo(GeradorVinculo idVinculo) {
		this.idVinculo = idVinculo;
	}

	public Long getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(Long contaDestino) {
		this.contaDestino = contaDestino;
	}

}

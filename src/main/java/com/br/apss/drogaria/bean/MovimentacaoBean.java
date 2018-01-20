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
import javax.validation.constraints.DecimalMin;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoLanc;
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
	@DecimalMin(value = "0.01", message = "O 'VALOR' tem quer ser maior que 0,00")
	private BigDecimal valor = BigDecimal.ZERO;
	private LazyDataModel<Movimentacao> model;
	private BigDecimal saldo;

	private TipoConta tipoConta;

	private List<TipoConta> listaTiposContas;

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
		this.valor = BigDecimal.ZERO;
		this.movto.setDataDoc(new Date());
		carregarListaTipoContas();
	}

	public void atualizar() {
		List<Movimentacao> m = this.movimentacaoService.porVinculo(movtoSelecionado.getVinculo());
		for (int i = 0; i < m.size(); i++) {
			this.movtoSelecionado = m.get(i);

			this.movtoSelecionado.setDataDoc(this.movto.getDataDoc());
			this.movtoSelecionado.setDescricao(this.movto.getDescricao());
			this.movtoSelecionado.setDocumento(this.movto.getDocumento());
			this.movtoSelecionado.setVlrEntrada(this.movto.getVlrEntrada());
			this.movtoSelecionado.setVlrSaida(this.movto.getVlrSaida());

			if (this.movtoSelecionado.getPlanoConta().getTipo().getSigla().contains("R")) {
				this.movtoSelecionado.setVlrSaida(this.movto.getVlrEntrada());
				this.movtoSelecionado.setVlrEntrada(null);
				this.movtoSelecionado.setPlanoConta(this.movto.getPlanoConta());
			}

			if (this.movtoSelecionado.getPlanoConta().getTipo().getSigla().contains("D")) {
				this.movtoSelecionado.setVlrEntrada(this.movto.getVlrSaida());
				this.valor = this.movto.getVlrSaida();
				this.movtoSelecionado.setVlrSaida(null);
				this.movtoSelecionado.setPlanoConta(this.movto.getPlanoConta());
			}

			if (this.movtoSelecionado.getPlanoConta().getTipo().getSigla().contains("CC")) {
				if (i == 0) {
					this.movtoSelecionado.setPlanoConta(this.movto.getPlanoConta());
				} else {
					this.movtoSelecionado.setVlrSaida(valor);
					this.movtoSelecionado.setVlrEntrada(null);
				}
			}

			movimentacaoService.salvar(this.movtoSelecionado);

		}
		this.movto = new Movimentacao();
		pesquisar();
		carregarContasLanctos();
		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		Messages.addGlobalInfo("Registro salvo com sucesso");

	}

	public void editar() {
		List<Movimentacao> m = this.movimentacaoService.porVinculo(movtoSelecionado.getVinculo());
		this.movto = m.get(0);
		if (movto.getTipoLanc().getSigla().contains("CC")) {
			this.tipoConta = movto.getPlanoConta().getTipo();
			if (movtoSelecionado.getId() == this.movto.getId()) {
				Movimentacao mm = this.movimentacaoService.porVinculo(movtoSelecionado.getVinculo(),
						this.movto.getId());
				this.filtro.setPlanoConta(mm.getPlanoConta());
			}
			// System.out.println("Entrou aqui...");
		} else {
			this.tipoConta = this.movto.getPlanoConta().getTipo();
			if (this.tipoConta.getSigla().contains("R")) {
				this.movto.setVlrEntrada(this.movto.getVlrSaida());
				this.movto.setVlrSaida(null);
			} else if (this.tipoConta.getSigla().contains("D")) {
				this.movto.setVlrSaida(this.movto.getVlrEntrada());
				this.movto.setVlrEntrada(null);
			}
		}
		carregarListaTipoContas();
		carregarContasLanctos();

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

		if (this.movto.isInclusao()) {
			int contador = 0;
			this.movto.setVinculo(idVinculo.gerar(Movimentacao.class));
			while (contador <= 1) {
				if (contador == 0) {
					if (this.tipoConta.getSigla().contains("R")) {
						this.movto.setVlrSaida(this.movto.getVlrEntrada());
						this.valor = this.movto.getVlrEntrada();
						this.movto.setVlrEntrada(null);
						this.movto.setTipoLanc(TipoLanc.CR);
					} else if (this.tipoConta.getSigla().contains("D")) {
						this.movto.setVlrEntrada(this.movto.getVlrSaida());
						this.valor = this.movto.getVlrSaida();
						this.movto.setVlrSaida(null);
						this.movto.setTipoLanc(TipoLanc.CD);
					} else {
						this.valor = this.movto.getVlrEntrada();
						this.movto.setVlrSaida(null);
						this.movto.setTipoLanc(TipoLanc.CC);
					}
				} else {
					if (this.tipoConta.getSigla().contains("R")) {
						this.movto.setVlrEntrada(this.valor);
						this.movto.setVlrSaida(null);
						this.movto.setTipoLanc(TipoLanc.CR);
					} else if (this.tipoConta.getSigla().contains("D")) {
						this.movto.setVlrSaida(this.valor);
						this.movto.setVlrEntrada(null);
						this.movto.setTipoLanc(TipoLanc.CD);
					} else {
						this.movto.setVlrSaida(this.valor);
						this.movto.setVlrEntrada(null);
						this.movto.setTipoLanc(TipoLanc.CC);
					}
					this.movto.setPlanoConta(this.filtro.getPlanoConta());
				}
				this.movto.setDataLanc(new Date());
				// this.movto.setUsuario(obterUsuario());
				movimentacaoService.salvar(this.movto);
				contador++;
			}
			this.movtoSelecionado = null;
			novoCadastro();
			pesquisar();
			carregarContasLanctos();
			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("nao fechar", true);
			Messages.addGlobalInfo("Registro salvo com sucesso");
		} else {
			atualizar();
		}
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

	public void carregarListaTipoContas() {
		this.listaTiposContas = Arrays.asList(TipoConta.values());
	}

	public void carregarContasLanctos() {
		PlanoContaFilter cl = new PlanoContaFilter();
		if (null != this.tipoConta) {
			cl.setTipo(this.tipoConta);
			cl.setStatus(true);
			listaPlanoContas = contaService.filtrados(cl);
			if (cl.getTipo().getSigla().contains("CC")) {
				listaPlanoContas = getContaCorrentes();
			}
		} else {
			listaPlanoContas = new ArrayList<>();
		}
	}

	/* Remover a propria conta para lançamento */
	public List<PlanoConta> getContaCorrentes() {
		List<PlanoConta> cts = new ArrayList<PlanoConta>();
		List<PlanoConta> pc = getContas();
		if (null != this.filtro.getPlanoConta()) {
			for (int i = 0; i < pc.size(); i++) {
				if (pc.get(i).getId() == this.filtro.getPlanoConta().getId()) {
					pc.remove(i);
				}
			}
			cts = pc;
		}
		return cts;
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

	public List<TipoConta> getListaTiposContas() {
		return listaTiposContas;
	}

	public void setListaTiposContas(List<TipoConta> listaTiposContas) {
		this.listaTiposContas = listaTiposContas;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

}
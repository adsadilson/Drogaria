package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.enums.TipoBaixa;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoLanc;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.CabContaApagar;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.model.filter.PagamentoFilter;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.service.CabContaApagarService;
import com.br.apss.drogaria.service.ContaAPagarService;
import com.br.apss.drogaria.service.MovimentacaoService;
import com.br.apss.drogaria.service.PagamentoService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.PlanoContaService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class ContaAPagarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAPagar contaAPagar;

	private Pagamento pagamento = new Pagamento();

	private Pagamento pagamentoSelecionado = new Pagamento();

	private ContaAPagarFilter filtro;

	private PagamentoFilter filtroPagamento;

	private BigDecimal saldo;

	private BigDecimal totalSelecionado = BigDecimal.ZERO;

	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	private List<ContaAPagar> contaApagarSelecionadas = new ArrayList<ContaAPagar>();

	private List<ContaAPagar> listaContasApagar = new ArrayList<ContaAPagar>();

	private List<PlanoConta> listaContas = new ArrayList<PlanoConta>();

	private List<Pagamento> listaPagamentos;

	private LazyDataModel<Pagamento> model;

	private ContaAPagar parcela;

	private ContaAPagar parcelaEditar;

	private List<ContaAPagar> listaParcelas;

	private Movimentacao movimentacao;

	private List<Movimentacao> listaMovimentacoes = new ArrayList<Movimentacao>();

	@Inject
	private ContaAPagarService contaAPagarService;

	@Inject
	private PlanoContaService contaService;

	@Inject
	private PagamentoService pagamentoService;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private MovimentacaoService movtoServico;

	@Inject
	private GeradorVinculo gerarVinculo;

	private Long idVinculo = null;

	@Inject
	private CabContaApagarService cabContaApagarService;

	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

	private BigDecimal totalDasParcelas = BigDecimal.ZERO;

	private BigDecimal totalDaNotaMovimentacao = BigDecimal.ZERO;

	private TipoConta tipoConta;

	private CabContaApagar cabContaApagar;

	private BigDecimal totalAVencer = BigDecimal.ZERO;

	private BigDecimal totalAVencido = BigDecimal.ZERO;

	private BigDecimal totalGeral = BigDecimal.ZERO;

	private BigDecimal totalMultaJuros = BigDecimal.ZERO;

	private BigDecimal totalDesc = BigDecimal.ZERO;

	private BigDecimal totalApagar = BigDecimal.ZERO;

	private BigDecimal totalPago = BigDecimal.ZERO;

	private BigDecimal totalAParcelar = BigDecimal.ZERO;

	private boolean isToggle = false;

	private String permitirEditar;

	private String descricao;

	private String labelInfo = "sim";

	@Inject
	CabContaApagarService service;

	/******************** Metodos ***********************/

	public void pesquisarPagamento() {

		if (!validarDatas(filtroPagamento.getDtIni(), filtroPagamento.getDtFim())) {

			model = new LazyDataModel<Pagamento>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Pagamento> load(int first, int pageSize, String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {

					setRowCount(pagamentoService.quantidadeFiltrados(filtroPagamento));

					filtroPagamento.setPrimeiroRegistro(first);
					filtroPagamento.setQtdeRegistro(pageSize);
					filtroPagamento.setOrdenacao(sortField);
					filtroPagamento.setAscendente(SortOrder.ASCENDING.equals(sortOrder));

					return pagamentoService.filtrados(filtroPagamento);

				}

				@Override
				public Pagamento getRowData(String rowKey) {
					pagamentoSelecionado = pagamentoService.porId(Long.valueOf(rowKey));
					return pagamentoSelecionado;
				}

				@Override
				public String getRowKey(Pagamento objeto) {
					return pagamentoSelecionado.getId().toString();
				}

			};

			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
		} else {
			Messages.addGlobalError("Data inicio maior do que data final!");
		}

	}

	public void cancelarPagamentos() {

		if (this.pagamentoSelecionado.getFormaBaixa() == FormaBaixa.BI) {
			pagamentoService.cancelarPagamento(pagamentoSelecionado);
		} else {
			this.listaMovimentacoes.clear();
			this.listaMovimentacoes = pagamentoSelecionado.getListaMovimentacoes();
			if (this.listaMovimentacoes.size() > 1) {
				pagamentoService.cancelarPagamento(pagamentoSelecionado);
			} else {

				pagamentoService.cancelarPagamento(pagamentoSelecionado);
			}
		}

		pesquisarPagamento();
	}

	public void inicializar() {
		contaAPagar = new ContaAPagar();
		filtro = new ContaAPagarFilter();
		filtroPagamento = new PagamentoFilter();
		movimentacao = new Movimentacao();
		pesquisar();
	}

	public void novo() {
		this.cabContaApagar = new CabContaApagar();
		this.cabContaApagar.setDataDoc(new Date());
		this.cabContaApagar.setDataLanc(new Date());
		this.cabContaApagar.setUsuario(obterUsuario());
		this.parcela = new ContaAPagar();
		this.listaMovimentacoes = new ArrayList<Movimentacao>();
		this.listaParcelas = new ArrayList<ContaAPagar>();
		this.totalDasParcelas = BigDecimal.ZERO;
		this.totalDaNotaMovimentacao = BigDecimal.ZERO;
	}

	public void excluirSelecionados() {
		try {
			contaAPagarService.excluirContas(this.contaApagarSelecionadas);
			this.contaApagarSelecionadas = new ArrayList<>();
			pesquisar();
			this.setTotalSelecionado(BigDecimal.ZERO);
			Messages.addGlobalInfo("Parcela(s) excluida(s) com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Esse registro possui vinculo com outras tabelas!");
		}
	}

	public void excluirPagamentoAbaixar() {
		if (this.pagamento.getFormaBaixa().equals(FormaBaixa.BI)) {
			this.listaMovimentacoes.clear();
			this.listaPagamentos.clear();
			this.pagamento.setValorPago(BigDecimal.ZERO);
			calcularValorApagar();
		} else {
			int achou = -1;
			for (int i = 0; i < this.listaMovimentacoes.size(); i++) {
				if (this.listaMovimentacoes.get(i).getPlanoConta().getNome()
						.equals(pagamento.getListaMovimentacoes().get(i).getPlanoConta().getNome())) {
					achou = i;
					break;
				}
			}
			if (achou > -1) {
				this.listaMovimentacoes.remove(achou);
				this.listaPagamentos.remove(achou);
			}
		}
	}

	public void abrirEdicao() {
		this.parcelaEditar = new ContaAPagar();
		this.parcelaEditar.setParcela(this.parcela.getParcela());
		this.parcelaEditar.setDataVencto(this.parcela.getDataVencto());
		this.parcelaEditar.setNumDoc(this.parcela.getNumDoc());
		this.parcelaEditar.setValor(this.parcela.getValor());
	}

	public void salvarEdicaoParcela() {

		BigDecimal recalculo = BigDecimal.ZERO;

		if (!validarDatas(this.cabContaApagar.getDataDoc(), this.parcelaEditar.getDataVencto())) {

			for (ContaAPagar pp : this.listaParcelas) {
				if (pp.getParcela().equals(this.parcelaEditar.getParcela())) {
					pp.setDataVencto(this.parcelaEditar.getDataVencto());
					pp.setNumDoc(this.parcelaEditar.getNumDoc());
					pp.setValor(this.parcelaEditar.getValor());
				}
				recalculo = recalculo.add(pp.getValor());
			}
			this.setTotalDasParcelas(recalculo);
		} else {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("A data de vencimento dever ser maior que a data de entrada!");
		}

	}

	public void editar() {

		this.permitirEditar = "true";

		this.parcela = new ContaAPagar();
		this.movimentacao = new Movimentacao();
		BigDecimal t = BigDecimal.ZERO, t2 = BigDecimal.ZERO;

		for (ContaAPagar cp : this.contaApagarSelecionadas) {

			this.listaParcelas = contaAPagarService.porVinculo(cp.getAgrupadorMovimentacao());

			for (ContaAPagar par : this.listaParcelas) {
				if (!par.getStatus().contains("ABERTO")) {
					this.permitirEditar = "false";
					break;
				}
				this.permitirEditar = "true";
				t = t.add(par.getValor());
			}

			if (this.permitirEditar == "true") {

				this.listaParcelas = contaAPagarService.porVinculo(cp.getAgrupadorMovimentacao());
				this.listaMovimentacoes = cp.getMovimentacoes();
				this.cabContaApagar = cabContaApagarService.porVinculo(cp.getAgrupadorMovimentacao());

				for (Movimentacao m : this.listaMovimentacoes) {
					t2 = t2.add(m.getVlrSaida());
				}

				this.parcela.setValor(t);
				this.setTotalDasParcelas(t);
				this.movimentacao.setTotalRateio(t2);
			}
		}
	}

	public void salvar() throws Exception {
		if (!validarDatas(this.cabContaApagar.getDataDoc(), this.cabContaApagar.getDataVencto())) {

			if (!this.getTotalDasParcelas().equals(BigDecimal.ZERO)) {

				if (this.movimentacao.getTotalRateio().equals(this.getTotalDasParcelas())) {

					if (this.cabContaApagar.getId() == null) {
						this.cabContaApagar.setVinculo(gerarVinculo.gerar(Movimentacao.class));
					}

					for (int i = 0; i < this.listaMovimentacoes.size(); i++) {
						this.listaMovimentacoes.get(i)
								.setPlanoContaPai(this.listaMovimentacoes.get(i).getPlanoConta().getContaPai());
						this.listaMovimentacoes.get(i).setDataDoc(this.cabContaApagar.getDataDoc());
						this.listaMovimentacoes.get(i).setDataLanc(new Date());
						this.listaMovimentacoes.get(i).setDocumento(this.cabContaApagar.getDocumento());
						this.listaMovimentacoes.get(i).setVlrEntrada(null);
						this.listaMovimentacoes.get(i).setTipoLanc(TipoLanc.CA);
						this.listaMovimentacoes.get(i).setTipoConta(TipoConta.D);
						this.listaMovimentacoes.get(i).setVinculo(cabContaApagar.getVinculo());
						this.listaMovimentacoes.get(i).setPessoa(this.cabContaApagar.getFornecedor());
						this.listaMovimentacoes.get(i).setUsuario(obterUsuario());
					}

					this.listaMovimentacoes = this.movtoServico.salvar(this.listaMovimentacoes);

					for (int i = 0; i < this.listaParcelas.size(); i++) {
						this.listaParcelas.get(i).setDataDoc(this.cabContaApagar.getDataDoc());
						this.listaParcelas.get(i).setStatus("ABERTO");
						this.listaParcelas.get(i).setUsuario(obterUsuario());
						this.listaParcelas.get(i).setFornecedor(this.cabContaApagar.getFornecedor());
						this.listaParcelas.get(i).setAgrupadorMovimentacao(this.cabContaApagar.getVinculo());
						this.listaParcelas.get(i).setValorApagar(this.listaParcelas.get(i).getValor());
					}

					for (int i = 0; i < this.listaParcelas.size(); i++) {
						this.listaParcelas.get(i).setMovimentacoes(this.listaMovimentacoes);
					}

					this.cabContaApagar.setListaContaAPagars(this.listaParcelas);
					this.cabContaApagar.setVinculo(this.cabContaApagar.getVinculo());
					cabContaApagarService.salvar(this.cabContaApagar);

					RequestContext request = RequestContext.getCurrentInstance();
					request.addCallbackParam("sucesso", true);
					Messages.addGlobalInfo("Registro salvo com sucesso.");

					pesquisar();

				} else {
					Messages.addGlobalError("Total do rateio diferente do total de pagamento");
				}
			} else {
				Messages.addGlobalError("Não foi informado nenhum valor para forma de pagamento");
			}
		} else {
			Messages.addGlobalInfo("Data da entrada está maior que a data de vencimento.");
		}
	}

	public void salvarBaixaSimples() throws Exception {
		if (!validarDatas(this.contaAPagar.getDataDoc(), this.pagamento.getDataPago())) {
			this.pagamento.setUsuario(obterUsuario());

			if (this.contaAPagar.getPagoTB().compareTo(this.contaAPagar.getValorApagar()) > 0) {
				FacesContext.getCurrentInstance().validationFailed();
				throw new NegocioException("O valor do pagamento não dever ser maior que o valor à pagar!");
			}

			contaAPagarService.baixaSimples(this.contaAPagar, this.pagamento);
			Messages.addGlobalInfo("Titulo baixado com sucesso!");
		} else {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("A data de pagamento dever ser maior ou igual a data de lançamento ("
					+ formato.format(this.contaAPagar.getDataDoc()) + ") !");
		}
	}

	public void salvarBaixaMultiplas() {

		BigDecimal c = BigDecimal.ZERO;
		BigDecimal p = BigDecimal.ZERO;

		for (ContaAPagar cp : this.listaContasApagar) {
			if (cp.getDataDoc().compareTo(this.pagamento.getDataPago()) > 0) {
				throw new NegocioException("A data de pagamento dever ser maior ou igual a data de lançamento ("
						+ formato.format(cp.getDataDoc()) + ") !");
			}
			c = c.add(cp.getPagoTB());
		}

		for (Pagamento pg : this.listaPagamentos) {
			p = p.add(pg.getValorPago());
		}

		if (p.compareTo(c) < 0) {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("Valor de pagamento a menor!");
		}

		contaAPagarService.baixaMultiplas(this.listaContasApagar, this.listaMovimentacoes, this.listaPagamentos,
				this.pagamento);
	}

	public void formaBaixa() {

		if (this.pagamento.getFormaBaixa().equals(FormaBaixa.BI)) {
			this.pagamento.setDescricao(null);
		} else {
			this.listaMovimentacoes.clear();
			this.listaPagamentos.clear();

		}
	}

	public void addPagamento() {

		if (this.pagamento.getFormaBaixa().equals(FormaBaixa.BI)) {

			this.listaPagamentos.clear();

			for (ContaAPagar c : this.listaContasApagar) {

				idVinculo = gerarVinculo.gerar(ContaAPagar.class);

				Movimentacao movto = new Movimentacao();
				PlanoConta pl1 = new PlanoConta();
				pl1 = contaService.porId(this.movimentacao.getPlanoConta().getId());

				PlanoConta pl2 = new PlanoConta();
				pl2 = contaService.porId(pl1.getContaPai().getId());

				movto.setDescricao(
						"PG. NT." + c.getNumDoc() + " Parc." + c.getParcela() + " - " + c.getFornecedor().getNome());

				if (c.getValorApagar().compareTo(c.getValorPago()) > 0) {
					movto.setDescricao("PG. NT." + c.getNumDoc() + " Parc." + c.getParcela() + " - "
							+ c.getFornecedor().getNome() + " (P)");
				}

				this.pagamento.setUsuario(obterUsuario());

				movto.setDataDoc(this.pagamento.getDataPago());
				movto.setDataLanc(this.pagamento.getDataPago());
				movto.setUsuario(this.pagamento.getUsuario());
				movto.setVinculo(idVinculo);
				movto.setVlrEntrada(null);
				movto.setVlrSaida(c.getPagoTB());
				movto.setDocumento(c.getNumDoc());
				movto.setPessoa(c.getFornecedor());
				movto.setTipoLanc(TipoLanc.PC);
				movto.setTipoConta(TipoConta.CC);
				movto.setPlanoConta(pl1);
				movto.setPlanoContaPai(pl2);

				listaMovimentacoes.add(movto);

				Pagamento p = new Pagamento();

				p.setDataLanc(new Date());
				p.setDataPago(this.pagamento.getDataPago());
				p.setDescricao(movto.getDescricao());
				p.setFormaBaixa(FormaBaixa.BI);
				p.setValor(c.getValor());
				p.setValorAPagar(c.getValorApagar());
				p.setValorDesc(c.getDescTB());
				p.setValorMultaJuros(c.getMultaTB());
				p.setVinculo(idVinculo);
				p.setValorPago(c.getPagoTB());
				p.setUsuario(movto.getUsuario());
				p.setConta(movto.getPlanoConta());

				p.setListaContaAPagars(listaContaAPagars);
				p.setListaMovimentacoes(listaMovimentacoes);

				this.listaPagamentos.add(p);

				calcularValorApagar();
			}
		} else {

			calcularValorApagar();

			Movimentacao movto = new Movimentacao();

			if (listaMovimentacoes.isEmpty()) {
				idVinculo = gerarVinculo.gerar(ContaAPagar.class);
			}

			this.pagamento.setUsuario(obterUsuario());
			this.pagamento.setVinculo(idVinculo);

			PlanoConta pl1 = new PlanoConta();
			pl1 = contaService.porId(this.movimentacao.getPlanoConta().getId());

			PlanoConta pl2 = new PlanoConta();
			pl2 = contaService.porId(pl1.getContaPai().getId());

			movto.setDataDoc(this.pagamento.getDataPago());
			movto.setDataLanc(this.pagamento.getDataPago());
			movto.setUsuario(this.pagamento.getUsuario());
			movto.setVlrSaida(this.pagamento.getValor());
			movto.setDescricao(descricao);
			movto.setVlrEntrada(null);
			movto.setVinculo(idVinculo);
			movto.setVlrSaida(this.pagamento.getValorPago());
			movto.setTipoLanc(TipoLanc.PC);
			movto.setTipoConta(TipoConta.CC);
			movto.setPlanoConta(pl1);
			movto.setPlanoContaPai(pl2);

			listaMovimentacoes.add(movto);

			Pagamento p = new Pagamento();

			p.setDataLanc(new Date());
			p.setDataPago(this.pagamento.getDataPago());
			p.setDescricao(movto.getDescricao());
			p.setFormaBaixa(FormaBaixa.BA);
			p.setValor(totalSelecionado);
			p.setValorAPagar(totalApagar);
			p.setValorDesc(totalDesc);
			p.setValorMultaJuros(totalMultaJuros);
			p.setValorPago(this.pagamento.getValorPago());
			p.setUsuario(this.pagamento.getUsuario());
			p.setConta(movto.getPlanoConta());
			p.setVinculo(idVinculo);

			p.setListaContaAPagars(listaContaAPagars);
			p.setListaMovimentacoes(listaMovimentacoes);

			this.listaPagamentos.add(p);

		}
	}

	public void calcularValorApagar() {
		BigDecimal c = BigDecimal.ZERO;
		BigDecimal p = BigDecimal.ZERO;

		for (ContaAPagar cp : this.listaContasApagar) {
			c = c.add(cp.getPagoTB());
		}

		for (Pagamento pg : this.listaPagamentos) {
			p = p.add(pg.getValorPago());
		}

		if (this.pagamento.getFormaBaixa().equals(FormaBaixa.BA)) {
			p = p.add(this.pagamento.getValorPago());
			if (p.compareTo(c) > 0) {
				FacesContext.getCurrentInstance().validationFailed();
				throw new NegocioException("O valor do pagamento não dever ser maior que o valor à pagar!");
			}
		}

		saldo = p.subtract(c);
	}

	public void rowSelect(SelectEvent event) {
		editar();
		this.setTotalSelecionado(BigDecimal.ZERO);
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAPagar) event.getObject()).getValorApagar()));
		if (this.contaApagarSelecionadas.size() > 1) {
			BigDecimal t = BigDecimal.ZERO;
			for (ContaAPagar cp : contaApagarSelecionadas) {
				t = t.add(cp.getValorApagar());
				this.setTotalSelecionado(t);
			}
		}
	}

	public void rowSelectCheckBox(SelectEvent event) {
		editar();
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAPagar) event.getObject()).getValorApagar()));
	}

	public void rowUnSelect(UnselectEvent event) {
		editar();
		this.setTotalSelecionado(
				this.getTotalSelecionado().subtract(((ContaAPagar) event.getObject()).getValorApagar()));
	}

	@SuppressWarnings("unchecked")
	public void onRowSelectAll(ToggleSelectEvent event) {
		DataTable listTemp = (DataTable) event.getSource();
		List<ContaAPagar> list = (List<ContaAPagar>) listTemp.getValue();
		if (event.isSelected()) {
			BigDecimal t = BigDecimal.ZERO;
			for (ContaAPagar p : list) {
				t = t.add(p.getValorApagar());
				this.setTotalSelecionado(t);
			}
			return;
		}
		this.setTotalSelecionado(BigDecimal.ZERO);
	}

	public void rowToggleSelect() {
		if (!isToggle) {
			BigDecimal t = BigDecimal.ZERO;
			if (contaApagarSelecionadas.size() > 0) {
				for (ContaAPagar cp : contaApagarSelecionadas) {
					t = t.add(cp.getValorApagar());
					this.setTotalSelecionado(t);
				}
				isToggle = true;
			}
		} else {
			isToggle = false;
			this.setTotalSelecionado(BigDecimal.ZERO);
		}
	}

	public void carregarContasPorTipoCategorias() {
		if (null != this.getTipoConta()) {
			this.listaContas = contaService.listarContasPorTipoCategorias(this.getTipoConta(), TipoRelatorio.A);
		}
	}

	public List<PlanoConta> getCarregarContasADebitar() {
		PlanoContaFilter cl = new PlanoContaFilter();
		cl.setTipo(TipoConta.CC);
		cl.setCategoria(TipoRelatorio.A);
		cl.setStatus(true);
		return contaService.filtrados(cl);
	}

	public void iniciarBaixaTitulo() {

		this.listaPagamentos = new ArrayList<Pagamento>();
		this.pagamento = new Pagamento();
		this.pagamento.setDataPago(new Date());
		this.pagamento.setFormaBaixa(FormaBaixa.BI);
		this.movimentacao = new Movimentacao();
		this.listaMovimentacoes.clear();
		this.setLabelInfo("nao");

		this.listaContasApagar.clear();
		BigDecimal vlr = BigDecimal.ZERO;

		this.setTotalApagar(vlr);
		this.setTotalPago(vlr);
		this.setTotalMultaJuros(vlr);
		this.setTotalDesc(vlr);

		for (ContaAPagar cp : this.contaApagarSelecionadas) {
			contaAPagar = new ContaAPagar();
			contaAPagar.setId(cp.getId());
			contaAPagar.setDataVencto(cp.getDataVencto());
			contaAPagar.setNumDoc(cp.getNumDoc());
			contaAPagar.setParcela(cp.getParcela());
			contaAPagar.setStatus(cp.getStatus());
			contaAPagar.setTipoCobranca(cp.getTipoCobranca());
			contaAPagar.setValor(cp.getValor());
			contaAPagar.setValorApagar(cp.getValorApagar());
			contaAPagar.setValorPago(cp.getValorPago());
			contaAPagar.setPagoTB(cp.getValorApagar());
			contaAPagar.setVinculo(cp.getVinculo());
			contaAPagar.setFornecedor(cp.getFornecedor());
			contaAPagar.setDataDoc(cp.getDataDoc());

			this.pagamento.setDescricao(
					"PG. NT." + cp.getNumDoc() + " Parc." + cp.getParcela() + " - " + cp.getFornecedor().getNome());

			contaAPagar.setSaldoDevedor(cp.getValorApagar());

			this.listaContasApagar.add(contaAPagar);

			vlr = vlr.add(contaAPagar.getSaldoDevedor());

			calcularValorApagar();
		}

		this.pagamento.setValorPago(vlr);
		this.setTotalApagar(vlr);
		this.setTotalPago(vlr);
		this.setTotalSelecionado(vlr);

	}

	public void closeBaixaTitulo() {
		this.contaApagarSelecionadas.clear();
		pesquisar();
		rowToggleSelect();
	}

	public List<Pessoa> getCarregarFornecedores() {
		return pessoaService.listarFornecedore();
	}

	public void iniciarLancRateio() {
		movimentacao = new Movimentacao();
	}

	private Usuario obterUsuario() {
		HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false));
		Usuario usuario = null;
		if (session != null) {
			usuario = (Usuario) session.getAttribute("usuarioAutenticado");
		}
		return usuario;
	}

	public void novoFiltro() {
		this.filtro = new ContaAPagarFilter();
	}

	public List<TipoConta> getListaTipoContas() {
		return Arrays.asList(TipoConta.D, TipoConta.CC);
	}

	public List<TipoCobranca> getListaTipoCobrancas() {
		return Arrays.asList(TipoCobranca.values());
	}

	public List<Status> getStatus() {
		return Arrays.asList(Status.values());
	}

	public List<FormaBaixa> getListaFormaBaixa() {
		return Arrays.asList(FormaBaixa.values());
	}

	public List<TipoBaixa> getListaTipoBaixa() {
		return Arrays.asList(TipoBaixa.values());
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

		listaContaAPagars.clear();
		listaContaAPagars = contaAPagarService.filtrados(filtro);

		BigDecimal t1 = BigDecimal.ZERO;
		BigDecimal t2 = BigDecimal.ZERO;
		BigDecimal t3 = BigDecimal.ZERO;
		this.setTotalAVencido(t1);
		this.setTotalAVencer(t2);
		this.setTotalGeral(t3);

		Date dataAtual = getDataAtualFormatada();

		for (ContaAPagar c : listaContaAPagars) {
			c.setDias(intervaloDias(c.getDataVencto(), new Date()));
			if (c.getDataVencto().before(dataAtual)) {
				// System.out.println("Data é inferior à ");
				t1 = t1.add(c.getValorApagar());
				this.setTotalAVencido(t1);
			} else if (c.getDataVencto().after(dataAtual)) {
				// System.out.println("Data é posterior à ");
				t2 = t2.add(c.getValorApagar());
				this.setTotalAVencer(t2);
			} else {
				// System.out.println("Data é igual à ");
				t3 = t3.add(c.getValorApagar());
				this.setTotalGeral(t3);
			}
		}

		this.setTotalSelecionado(BigDecimal.ZERO);
		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
	}

	public Date getDataAtualFormatada() {

		Date dt = new Date();
		String hoje = formato.format(dt);
		Date data = null;
		try {
			data = formato.parse(hoje);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	public void gerarParcelas() {

		BigDecimal qtde_parcela = new BigDecimal(this.parcela.getNumVezes());
		BigDecimal valorParcela = this.getTotalAParcelar().divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = this.totalAParcelar.subtract(valorParcial);

		this.listaParcelas.clear();
		for (int i = 0; i < this.parcela.getNumVezes(); i++) {
			ContaAPagar ap = new ContaAPagar();
			ap.setTipoCobranca(this.parcela.getTipoCobranca());
			ap.setParcela((i + 1) + "/" + this.parcela.getNumVezes());
			ap.setNumDoc(this.cabContaApagar.getDocumento());
			ap.setDataVencto(i == 0 ? this.cabContaApagar.getDataVencto()
					: somaDias(this.cabContaApagar.getDataVencto(), this.parcela.getPeriodo() * i));
			ap.setValor(i == 0 ? primeiraParcela : valorParcela);
			this.listaParcelas.add(ap);
		}
		this.setTotalDasParcelas(this.getTotalAParcelar());
	}

	public void removerConta() {
		int achou = -1;
		for (int i = 0; i < this.listaMovimentacoes.size(); i++) {
			if (this.listaMovimentacoes.get(i).getPlanoConta().getNome()
					.equals(movimentacao.getPlanoConta().getNome())) {
				achou = i;
				break;
			}
		}
		if (achou > -1) {
			this.listaMovimentacoes.remove(achou);
			BigDecimal t = BigDecimal.ZERO;
			for (Movimentacao m : this.listaMovimentacoes) {
				t = t.add(m.getVlrSaida());
			}

			this.movimentacao.setTotalRateio(t);
			this.parcela.setValor(t);
			this.cabContaApagar.setValor(t);
			this.setTotalDaNotaMovimentacao(t);

			if (this.listaMovimentacoes.size() == 0) {
				this.listaParcelas.clear();
				this.cabContaApagar.setValor(BigDecimal.ZERO);
				this.setTotalDasParcelas(BigDecimal.ZERO);
			}
		}
	}

	public void informativo() {
		labelInfo = "sim";
		if (contaAPagar.getValorApagar().compareTo(contaAPagar.getValorPago()) <= 0) {
			labelInfo = "nao";
		}
	}

	public void addConta() {
		if (!validarDatas(this.cabContaApagar.getDataDoc(), this.cabContaApagar.getDataVencto())) {
			int achou = -1;
			for (int i = 0; i < this.listaMovimentacoes.size(); i++) {
				if (this.listaMovimentacoes.get(i).getPlanoConta().getNome()
						.equals(this.movimentacao.getPlanoConta().getNome())) {
					achou = i;
				}
			}
			if (achou < 0) {
				this.listaMovimentacoes.add(this.movimentacao);
				this.movimentacao = new Movimentacao();
				BigDecimal t = BigDecimal.ZERO;
				for (Movimentacao m : this.listaMovimentacoes) {
					t = t.add(m.getVlrSaida());
				}
				this.movimentacao.setTotalRateio(t);
				this.setTotalAParcelar(t);
				this.cabContaApagar.setValor(t);
			} else {
				Messages.addGlobalError("Conta já cadastrada!");
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.addCallbackParam("sucesso", true);
			}
		} else {
			Messages.addGlobalError("A data de entrada esta maior que a data de vencimento.");
		}
	}

	public void onCellEdit(CellEditEvent event) {
		String coluna = event.getColumn().getClientId();

		BigDecimal oldValue = (BigDecimal) event.getOldValue();
		BigDecimal newValue = (BigDecimal) event.getNewValue();

		DataTable dataModel = (DataTable) event.getSource();
		ContaAPagar parcela = (ContaAPagar) dataModel.getRowData();

		if (coluna.indexOf("pago") > 0) {
			if (newValue.compareTo(parcela.getValorApagar()) > 0) {
				parcela.setPagoTB(oldValue);

				Integer row = event.getRowIndex();
				dataModel.setRowIndex(row);

				throw new NegocioException("Valor informando a pagar maior que o titulo!");
			}
		}

		if (newValue != null && !newValue.equals(oldValue)) {
			for (ContaAPagar c : this.listaContasApagar) {
				if (coluna.indexOf("pago") < 0) {
					BigDecimal t5 = BigDecimal.ZERO;
					t5 = t5.add(c.getSaldoDevedor().add(c.getMultaTB().subtract(c.getDescTB())));
					c.setValorApagar(t5);
					c.setPagoTB(t5);
				}
			}
			calcularTotais();
		}
		calcularValorApagar();
	}

	public void calcularTotais() {
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal d = BigDecimal.ZERO;
		BigDecimal t3 = BigDecimal.ZERO;
		BigDecimal t4 = BigDecimal.ZERO;

		for (ContaAPagar c : this.listaContasApagar) {
			m = m.add(c.getMultaTB());
			d = d.add(c.getDescTB());
			t3 = t3.add(c.getValorApagar());
			t4 = t4.add(c.getPagoTB());
		}
		this.setTotalMultaJuros(m);
		this.setTotalDesc(d);
		this.setTotalApagar(t3);
		this.setTotalPago(t4);
		this.pagamento.setValorPago(t4);
	}

	public void calcularValores() {
		for (ContaAPagar c : this.listaContasApagar) {
			c.setValorApagar(c.getSaldoDevedor().add(c.getMultaTB().subtract(c.getDescTB())));
			c.setPagoTB(c.getSaldoDevedor().add(c.getMultaTB().subtract(c.getDescTB())));
		}
	}

	public String calcularParcelas() {
		BigDecimal totalParcelas = BigDecimal.ZERO;
		BigDecimal dif = contaAPagar.getValor();
		for (ContaAPagar cp : this.contaApagarSelecionadas) {
			totalParcelas = totalParcelas.add(cp.getValor());
		}
		BigDecimal reslt = totalParcelas.subtract(dif);

		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String formatado = nf.format(reslt);
		return formatado;
	}

	public Date somaDias(Date data, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}

	public static int intervaloDias(Date d1, Date d2) {
		int result = (int) ((d1.getTime() - d2.getTime()) / 86400000L);
		return result < 0 ? result * -1 : 0;
	}

	public String getCalculaDif() {
		BigDecimal dif = contaAPagar.getValor();
		for (ContaAPagar cp : listaParcelas) {
			dif.subtract(cp.getValor());
		}
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String formatado = nf.format(dif);
		return formatado;
	}

	/*
	 * public void duplicarLancamento() { for (ContaAPagar cp :
	 * contaApagarSelecionadas) { for (int i = 0; i < numVezes; i++) {
	 * ContaAPagar c = new ContaAPagar(); c.setDataDoc(somaDias(cp.getDataDoc(),
	 * 30 * (i + 1))); c.setDataLanc(cp.getDataLanc());
	 * c.setValor(cp.getValor()); c.setValorPago(cp.getValorPago());
	 * c.setVlrApagar(cp.getVlrApagar()); c.setFornecedor(cp.getFornecedor());
	 * c.setUsuario(cp.getUsuario()); c.setTipoCobranca(cp.getTipoCobranca());
	 * c.setStatus(cp.getStatus()); c.setNumDoc(cp.getNumDoc());
	 * 
	 * if (null != cp.getParcela()) { // pegar só numero converter em int e soma
	 * com i depois // converter em string int p =
	 * Integer.parseInt(cp.getParcela().replaceAll("\\D", "")); p = p + (i + 1);
	 * c.setParcela("D/" + String.valueOf(p)); } else { c.setParcela("D/" + (i +
	 * 1)); }
	 * 
	 * c.setDataVencto(somaDias(cp.getDataVencto(), 30 * (i + 1)));
	 * contaAPagarService.salvar(c); } pesquisar(); } }
	 */

	/******************** Getters e Setters ***************************/

	public ContaAPagarFilter getFiltro() {
		return filtro;
	}

	public ContaAPagar getContaAPagar() {
		return contaAPagar;
	}

	public void setContaAPagar(ContaAPagar contaAPagar) {
		this.contaAPagar = contaAPagar;
	}

	public void setFiltro(ContaAPagarFilter filtro) {
		this.filtro = filtro;
	}

	public List<ContaAPagar> getListaContaAPagars() {
		return listaContaAPagars;
	}

	public void setListaContaAPagars(List<ContaAPagar> listaContaAPagars) {
		this.listaContaAPagars = listaContaAPagars;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<PlanoConta> getListaContas() {
		return listaContas;
	}

	public void setListaContas(List<PlanoConta> listaContas) {
		this.listaContas = listaContas;
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public List<ContaAPagar> getListaParcelas() {
		return listaParcelas;
	}

	public void setListaParcelas(List<ContaAPagar> listaParcelas) {
		this.listaParcelas = listaParcelas;
	}

	public List<Movimentacao> getListaMovimentacoes() {
		return listaMovimentacoes;
	}

	public void setListaMovimentacoes(List<Movimentacao> listaMovimentacoes) {
		this.listaMovimentacoes = listaMovimentacoes;
	}

	public List<ContaAPagar> getContaApagarSelecionadas() {
		return contaApagarSelecionadas;
	}

	public void setContaApagarSelecionadas(List<ContaAPagar> contaApagarSelecionadas) {
		this.contaApagarSelecionadas = contaApagarSelecionadas;
	}

	public BigDecimal getTotalAVencer() {
		return totalAVencer;
	}

	public void setTotalAVencer(BigDecimal totalAVencer) {
		this.totalAVencer = totalAVencer;
	}

	public BigDecimal getTotalAVencido() {
		return totalAVencido;
	}

	public void setTotalAVencido(BigDecimal totalAVencido) {
		this.totalAVencido = totalAVencido;
	}

	public BigDecimal getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(BigDecimal totalGeral) {
		this.totalGeral = totalGeral;
	}

	public BigDecimal getTotalSelecionado() {
		return totalSelecionado;
	}

	public void setTotalSelecionado(BigDecimal totalSelecionado) {
		this.totalSelecionado = totalSelecionado;
	}

	public ContaAPagar getParcela() {
		return parcela;
	}

	public void setParcela(ContaAPagar parcela) {
		this.parcela = parcela;
	}

	public ContaAPagar getParcelaEditar() {
		return parcelaEditar;
	}

	public void setParcelaEditar(ContaAPagar parcelaEditar) {
		this.parcelaEditar = parcelaEditar;
	}

	public CabContaApagar getCabContaApagar() {
		return cabContaApagar;
	}

	public void setCabContaApagar(CabContaApagar cabContaApagar) {
		this.cabContaApagar = cabContaApagar;
	}

	public BigDecimal getTotalDasParcelas() {
		return totalDasParcelas;
	}

	public void setTotalDasParcelas(BigDecimal totalDasParcelas) {
		this.totalDasParcelas = totalDasParcelas;
	}

	public BigDecimal getTotalDaNotaMovimentacao() {
		return totalDaNotaMovimentacao;
	}

	public void setTotalDaNotaMovimentacao(BigDecimal totalDaNotaMovimentacao) {
		this.totalDaNotaMovimentacao = totalDaNotaMovimentacao;
	}

	public TipoConta getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getPermitirEditar() {
		return permitirEditar;
	}

	public void setPermitirEditar(String permitirEditar) {
		this.permitirEditar = permitirEditar;
	}

	public BigDecimal getTotalMultaJuros() {
		return totalMultaJuros;
	}

	public void setTotalMultaJuros(BigDecimal totalMultaJuros) {
		this.totalMultaJuros = totalMultaJuros;
	}

	public BigDecimal getTotalDesc() {
		return totalDesc;
	}

	public void setTotalDesc(BigDecimal totalDesc) {
		this.totalDesc = totalDesc;
	}

	public BigDecimal getTotalApagar() {
		return totalApagar;
	}

	public void setTotalApagar(BigDecimal totalApagar) {
		this.totalApagar = totalApagar;
	}

	public List<ContaAPagar> getListaContasApagar() {
		return listaContasApagar;
	}

	public void setListaContasApagar(List<ContaAPagar> listaContasApagar) {
		this.listaContasApagar = listaContasApagar;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public List<Pagamento> getListaPagamentos() {
		return listaPagamentos;
	}

	public void setListaPagamentos(List<Pagamento> listaPagamentos) {
		this.listaPagamentos = listaPagamentos;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public PagamentoFilter getFiltroPagamento() {
		return filtroPagamento;
	}

	public void setFiltroPagamento(PagamentoFilter filtroPagamento) {
		this.filtroPagamento = filtroPagamento;
	}

	public LazyDataModel<Pagamento> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Pagamento> model) {
		this.model = model;
	}

	public Pagamento getPagamentoSelecionado() {
		return pagamentoSelecionado;
	}

	public void setPagamentoSelecionado(Pagamento pagamentoSelecionado) {
		this.pagamentoSelecionado = pagamentoSelecionado;
	}

	public String getLabelInfo() {
		return labelInfo;
	}

	public void setLabelInfo(String labelInfo) {
		this.labelInfo = labelInfo;
	}

	public BigDecimal getTotalAParcelar() {
		return totalAParcelar;
	}

	public void setTotalAParcelar(BigDecimal totalAParcelar) {
		this.totalAParcelar = totalAParcelar;
	}

}

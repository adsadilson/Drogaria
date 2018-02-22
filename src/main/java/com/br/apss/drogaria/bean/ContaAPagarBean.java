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

import javax.faces.application.FacesMessage;
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

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.enums.TipoBaixa;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.CabContaApagar;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.service.CabContaApagarService;
import com.br.apss.drogaria.service.ContaAPagarService;
import com.br.apss.drogaria.service.MovimentacaoService;
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

	private ContaAPagarFilter filtro;

	private BigDecimal saldo;

	private BigDecimal totalSelecionado = BigDecimal.ZERO;

	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	private List<ContaAPagar> contaApagarSelecionadas = new ArrayList<ContaAPagar>();

	private List<ContaAPagar> listaContasApagar = new ArrayList<ContaAPagar>();

	private List<PlanoConta> listaContas = new ArrayList<PlanoConta>();

	private List<Pagamento> listaPagamentos;

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
	private PessoaService pessoaService;

	@Inject
	private MovimentacaoService movtoServico;

	@Inject
	private GeradorVinculo gerarVinculo;

	@Inject
	private CabContaApagarService cabContaApagarService;

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

	private boolean isToggle = false;

	private String permitirEditar;

	@Inject
	CabContaApagarService service;

	/******************** Metodos ***********************/

	public void inicializar() {
		contaAPagar = new ContaAPagar();
		filtro = new ContaAPagarFilter();
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
			Messages.addGlobalError("Não é possivel excluir uma conta a pagar se existe outras Parcelas, "
					+ "seleciona todas para excluir!");
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

			this.listaParcelas = contaAPagarService.porVinculo(cp.getVinculo());

			for (ContaAPagar par : this.listaParcelas) {
				if (par.getDataPagto() != null) {
					this.permitirEditar = "false";
					break;
				}
				this.permitirEditar = "true";
				t = t.add(par.getValor());
			}

			if (this.permitirEditar == "true") {

				this.listaParcelas = contaAPagarService.porVinculo(cp.getVinculo());
				this.listaMovimentacoes = cp.getMovimentacoes();
				this.cabContaApagar = cabContaApagarService.porVinculo(cp.getVinculo());

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

					if (this.contaAPagar.getId() == null) {
						this.contaAPagar.setVinculo(gerarVinculo.gerar(ContaAPagar.class));
					} else {
						contaAPagarService.excluirContas(this.contaApagarSelecionadas);
					}

					for (int i = 0; i < this.listaMovimentacoes.size(); i++) {
						if (this.contaAPagar.getId() != null) {
							this.listaMovimentacoes.get(i).setId(null);
						}
						this.listaMovimentacoes.get(i)
								.setPlanoContaPai(this.listaMovimentacoes.get(i).getPlanoConta().getContaPai());
						this.listaMovimentacoes.get(i).setDataDoc(this.cabContaApagar.getDataDoc());
						this.listaMovimentacoes.get(i).setDataLanc(new Date());
						this.listaMovimentacoes.get(i).setDocumento(this.cabContaApagar.getDocumento());
						this.listaMovimentacoes.get(i).setVlrEntrada(null);
						this.listaMovimentacoes.get(i).setPessoa(this.cabContaApagar.getFornecedor());
						this.listaMovimentacoes.get(i).setUsuario(obterUsuario());
					}

					this.listaMovimentacoes = this.movtoServico.salvar(this.listaMovimentacoes);

					for (int i = 0; i < this.listaParcelas.size(); i++) {
						this.listaParcelas.get(i).setDataDoc(this.cabContaApagar.getDataDoc());
						this.listaParcelas.get(i).setStatus("ABERTO");
						this.listaParcelas.get(i).setFornecedor(this.cabContaApagar.getFornecedor());
						this.listaParcelas.get(i).setVinculo(this.contaAPagar.getVinculo());
						this.listaParcelas.get(i).setValorApagar(this.listaParcelas.get(i).getValor());
					}

					for (int i = 0; i < this.listaParcelas.size(); i++) {
						this.listaParcelas.get(i).setMovimentacoes(this.listaMovimentacoes);
					}

					this.cabContaApagar.setListaContaAPagars(this.listaParcelas);
					this.cabContaApagar.setVinculo(this.contaAPagar.getVinculo());
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

	public void rowSelect(SelectEvent event) {
		editar();
		this.setTotalSelecionado(BigDecimal.ZERO);
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAPagar) event.getObject()).getValor()));
		if (this.contaApagarSelecionadas.size() > 1) {
			BigDecimal t = BigDecimal.ZERO;
			for (ContaAPagar cp : contaApagarSelecionadas) {
				t = t.add(cp.getValor());
				this.setTotalSelecionado(t);
			}
		}
	}

	public void rowSelectCheckBox(SelectEvent event) {
		editar();
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAPagar) event.getObject()).getValor()));
	}

	public void rowUnSelect(UnselectEvent event) {
		editar();
		this.setTotalSelecionado(this.getTotalSelecionado().subtract(((ContaAPagar) event.getObject()).getValor()));
	}

	@SuppressWarnings("unchecked")
	public void onRowSelectAll(ToggleSelectEvent event) {
		DataTable listTemp = (DataTable) event.getSource();
		List<ContaAPagar> list = (List<ContaAPagar>) listTemp.getValue();
		if (event.isSelected()) {
			BigDecimal t = BigDecimal.ZERO;
			for (ContaAPagar p : list) {
				t = t.add(p.getValor());
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
					t = t.add(cp.getValor());
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
		this.pagamento.setDataPagto(new Date());

		this.listaContasApagar.clear();
		BigDecimal vlr = BigDecimal.ZERO;
		this.setTotalApagar(vlr);
		this.setTotalPago(vlr);

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
			contaAPagar.setValorPago(cp.getValorApagar());
			contaAPagar.setVinculo(cp.getVinculo());
			contaAPagar.setFornecedor(cp.getFornecedor());
			contaAPagar.setDataDoc(cp.getDataDoc());
			this.listaContasApagar.add(contaAPagar);
			vlr = vlr.add(cp.getValor());
		}

		this.pagamento.setValorPago(vlr);
		this.setTotalApagar(vlr);
		this.setTotalPago(vlr);
		this.setTotalSelecionado(vlr);

	}

	public void closeBaixaTitulo() {
		this.contaApagarSelecionadas.clear();
		rowToggleSelect();
	}

	public List<Pessoa> getCarregarFornecedores() {
		return pessoaService.listarTodos();
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
				t1 = t1.add(c.getValor());
				this.setTotalAVencido(t1);
			} else if (c.getDataVencto().after(dataAtual)) {
				// System.out.println("Data é posterior à ");
				t2 = t2.add(c.getValor());
				this.setTotalAVencer(t2);
			} else {
				// System.out.println("Data é igual à ");
				t3 = t3.add(c.getValor());
				this.setTotalGeral(t3);
			}
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
	}

	public Date getDataAtualFormatada() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
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
		BigDecimal valorParcela = this.parcela.getValor().divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = this.parcela.getValor().subtract(valorParcial);

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
		this.setTotalDasParcelas(this.parcela.getValor());
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
			this.setTotalDaNotaMovimentacao(t);
			if (this.listaMovimentacoes.size() == 00) {
				this.listaParcelas.clear();
				this.cabContaApagar.setValor(BigDecimal.ZERO);
				this.setTotalDasParcelas(BigDecimal.ZERO);
			}
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
				this.parcela.setValor(t);
			} else {
				Messages.addGlobalError("Conta já cadastrada!");
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.addCallbackParam("sucesso", true);
			}
		} else {
			Messages.addGlobalError("A data de entrada esta maior que a data de vencimento.");
		}
	}

	public void addPagamento() {

		if (this.pagamento.getTipoBaixa().equals(TipoBaixa.AP)) {
			if (this.pagamento.getFormaBaixa().equals(FormaBaixa.BI)) {

				for (ContaAPagar c : this.listaContasApagar) {
					this.pagamento = new Pagamento();
					this.pagamento
							.setHistorico("PAGTO REF. NT de Nr. " + c.getNumDoc() + " - " + c.getFornecedor().getNome());
					this.pagamento.setValorPago(c.getValorPago());
					this.listaPagamentos.add(this.pagamento);
				}
			}
		}

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			// calcularParcelas();

			DataTable dataModel = (DataTable) event.getSource();
			ContaAPagar parcela = (ContaAPagar) dataModel.getRowData();

			BigDecimal t1 = BigDecimal.ZERO;
			BigDecimal t2 = BigDecimal.ZERO;
			BigDecimal t3 = BigDecimal.ZERO;
			BigDecimal t4 = BigDecimal.ZERO;

			for (ContaAPagar c : this.listaContasApagar) {

				if (parcela.getParcela().equals(c.getParcela())) {
					if (c.getValorApagar().intValue() == parcela.getValorPago().intValue()) {
						c.setValorPago(c.getValor().add(c.getValorMultaJuros().subtract(c.getValorDesc())));
					}
				}

				c.setValorApagar(c.getValor().add(c.getValorMultaJuros().subtract(c.getValorDesc())));

				t1 = t1.add(c.getValorMultaJuros());
				t2 = t2.add(c.getValorDesc());
				t3 = t3.add(c.getValorApagar());
				t4 = t4.add(c.getValorPago());
			}

			this.setTotalMultaJuros(t1);
			this.setTotalDesc(t2);
			this.setTotalApagar(t3);
			this.setTotalPago(t4);
			this.pagamento.setValorPago(t4);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Celula editada",
					"Antigo: " + oldValue + ", Novo:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
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
	 * contaApagarSelecionadas) { for (int i = 0; i < numVezes; i++) { ContaAPagar c
	 * = new ContaAPagar(); c.setDataDoc(somaDias(cp.getDataDoc(), 30 * (i + 1)));
	 * c.setDataLanc(cp.getDataLanc()); c.setValor(cp.getValor());
	 * c.setValorPago(cp.getValorPago()); c.setVlrApagar(cp.getVlrApagar());
	 * c.setFornecedor(cp.getFornecedor()); c.setUsuario(cp.getUsuario());
	 * c.setTipoCobranca(cp.getTipoCobranca()); c.setStatus(cp.getStatus());
	 * c.setNumDoc(cp.getNumDoc());
	 * 
	 * if (null != cp.getParcela()) { // pegar só numero converter em int e soma com
	 * i depois // converter em string int p =
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

}

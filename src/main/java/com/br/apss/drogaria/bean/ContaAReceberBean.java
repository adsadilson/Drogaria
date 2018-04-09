package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
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

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoLanc;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.CabContaAReceber;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Recebimento;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.service.CabContaAReceberService;
import com.br.apss.drogaria.service.ContaAReceberService;
import com.br.apss.drogaria.service.MovimentacaoService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.PlanoContaService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class ContaAReceberBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAReceberFilter filtro;

	private ContaAReceber contaAReceberSelecionado;

	private ContaAReceber contaAReceber;

	private List<ContaAReceber> listaContaARecebers;

	private LazyDataModel<ContaAReceber> model;

	private Pessoa clienteSelecionado;

	private List<Pessoa> listaDeClientes = new ArrayList<Pessoa>();

	private List<ContaAReceber> listaContasSelecionadas = new ArrayList<ContaAReceber>();

	private List<ContaAReceber> listaContasAReceber = new ArrayList<ContaAReceber>();

	private List<PlanoConta> listaDePlanoContas;

	private CabContaAReceber cabContaAReceber;

	private Movimentacao movto = new Movimentacao();

	private List<Movimentacao> listaDeMovtos;

	private ContaAReceber parcela;

	private ContaAReceber parcelaEdicao;

	private List<ContaAReceber> listaDeParcelas;

	private List<Recebimento> listaDeRecebimentos;

	private Recebimento recebimento;

	@Inject
	private MovimentacaoService movtoService;

	@Inject
	private ContaAReceberService contaAReceberService;

	@Inject
	private CabContaAReceberService cabContaAReceberService;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private PlanoContaService contaService;

	@Inject
	private GeradorVinculo gerarVinculo;

	private Long idVinculo = null;

	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

	private BigDecimal totalAVencer = BigDecimal.ZERO;

	private BigDecimal totalAVencido = BigDecimal.ZERO;

	private BigDecimal totalGeral = BigDecimal.ZERO;

	private BigDecimal totalSelecionado = BigDecimal.ZERO;

	private BigDecimal totalMultaJuros = BigDecimal.ZERO;

	private BigDecimal totalDesc = BigDecimal.ZERO;

	private BigDecimal totalApagar = BigDecimal.ZERO;

	private BigDecimal totalPago = BigDecimal.ZERO;

	private String permitirEditar;

	private String labelInfo = "sim";

	private String descricao;

	private BigDecimal saldo;

	@PostConstruct
	public void inicializar() {
		filtro = new ContaAReceberFilter();
		this.listaContaARecebers = new ArrayList<ContaAReceber>();
		carregarListaDeClientes();
	}

	public void novo() {
		this.cabContaAReceber = new CabContaAReceber();
		this.cabContaAReceber.setDataDoc(new Date());
		this.cabContaAReceber.setDataLanc(new Date());
		this.cabContaAReceber.setCliente(filtro.getCliente());
		this.cabContaAReceber.setUsuario(obterUsuario());
		this.movto = new Movimentacao();
		this.listaDeMovtos = new ArrayList<Movimentacao>();
		this.listaDePlanoContas = new ArrayList<PlanoConta>();
		this.parcela = new ContaAReceber();
		this.listaDeParcelas = new ArrayList<ContaAReceber>();

	}

	public void salvar() {
		if (!validarDatas(this.cabContaAReceber.getDataDoc(), this.cabContaAReceber.getDataVencto())) {

			if (!this.parcela.getTotalPagamento().equals(BigDecimal.ZERO)) {

				if (this.movto.getTotalRateio().equals(this.parcela.getTotalPagamento())) {

					if (this.cabContaAReceber.getId() == null) {
						this.cabContaAReceber.setVinculo(gerarVinculo.gerar(Movimentacao.class));
					}

					for (int i = 0; i < this.listaDeMovtos.size(); i++) {
						this.listaDeMovtos.get(i)
								.setPlanoContaPai(this.listaDeMovtos.get(i).getPlanoConta().getContaPai());
						this.listaDeMovtos.get(i).setDataDoc(this.cabContaAReceber.getDataDoc());
						this.listaDeMovtos.get(i).setDataLanc(new Date());
						this.listaDeMovtos.get(i).setDocumento(this.cabContaAReceber.getDocumento());
						this.listaDeMovtos.get(i).setVlrSaida(null);
						this.listaDeMovtos.get(i).setTipoLanc(TipoLanc.CR);
						this.listaDeMovtos.get(i).setTipoConta(TipoConta.R);
						this.listaDeMovtos.get(i).setVinculo(cabContaAReceber.getVinculo());
						this.listaDeMovtos.get(i).setPessoa(this.cabContaAReceber.getCliente());
						this.listaDeMovtos.get(i).setUsuario(obterUsuario());
					}

					this.listaDeMovtos = this.movtoService.salvar(this.listaDeMovtos);

					for (int i = 0; i < this.listaDeParcelas.size(); i++) {
						this.listaDeParcelas.get(i).setDataDoc(this.cabContaAReceber.getDataDoc());
						this.listaDeParcelas.get(i).setStatus("ABERTO");
						this.listaDeParcelas.get(i).setUsuario(obterUsuario());
						this.listaDeParcelas.get(i).setCliente(this.cabContaAReceber.getCliente());
						this.listaDeParcelas.get(i).setAgrupadorMovimentacao(this.cabContaAReceber.getVinculo());
						this.listaDeParcelas.get(i).setValorApagar(this.listaDeParcelas.get(i).getValor());
					}

					for (int i = 0; i < this.listaDeParcelas.size(); i++) {
						this.listaDeParcelas.get(i).setMovimentacoes(this.listaDeMovtos);
					}

					this.cabContaAReceber.setListaContaARecebers(this.listaDeParcelas);
					this.cabContaAReceber.setVinculo(this.cabContaAReceber.getVinculo());
					cabContaAReceberService.salvar(this.cabContaAReceber);

					RequestContext request = RequestContext.getCurrentInstance();
					request.addCallbackParam("sucesso", true);
					Messages.addGlobalInfo("Registro salvo com sucesso.");

					pesquisar();

				} else {
					Messages.addGlobalError("Total do rateio diferente do total de recebimento");
				}
			} else {
				Messages.addGlobalError("Não foi informado nenhum valor para forma de recebimento");
			}
		} else {
			Messages.addGlobalInfo("Data da entrada está maior que a data de vencimento.");
		}
	}

	public void salvarBaixaSimples() throws Exception {
		if (!validarDatas(this.contaAReceber.getDataDoc(), this.recebimento.getDataPago())) {
			this.recebimento.setUsuario(obterUsuario());

			if (this.contaAReceber.getPagoTB().compareTo(this.contaAReceber.getValorApagar()) > 0) {
				FacesContext.getCurrentInstance().validationFailed();
				throw new NegocioException("O valor do recebimento não dever ser maior que o valor à pagar!");
			}

			contaAReceberService.baixaSimples(this.contaAReceber, this.recebimento);
			Messages.addGlobalInfo("Titulo baixado com sucesso!");
		} else {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("A data de recebimento dever ser maior ou igual a data de lançamento ("
					+ formato.format(this.contaAReceber.getDataDoc()) + ") !");
		}
	}

	public void excluir() {
		Messages.addGlobalInfo("Registro excluido com sucesso");
	}

	public void pesquisar() {

		listaContaARecebers = contaAReceberService.filtrados(filtro);

		BigDecimal t1 = BigDecimal.ZERO;
		BigDecimal t2 = BigDecimal.ZERO;
		BigDecimal t3 = BigDecimal.ZERO;
		setTotalAVencido(t1);
		setTotalAVencer(t2);
		setTotalGeral(t3);

		Date dataAtual = getDataAtualFormatada();

		for (ContaAReceber c : listaContaARecebers) {
			c.setDias(intervaloDias(c.getDataVencto(), new Date()));
			if (c.getDataVencto().before(dataAtual)) {
				// System.out.println("Data é inferior à ");
				t1 = t1.add(c.getValorApagar());
				setTotalAVencido(t1);
			} else if (c.getDataVencto().after(dataAtual)) {
				// System.out.println("Data é posterior à ");
				t2 = t2.add(c.getValorApagar());
				setTotalAVencer(t2);
			} else {
				// System.out.println("Data é igual à ");
				t3 = t3.add(c.getValorApagar());
				setTotalGeral(t3);
			}
		}

		setTotalSelecionado(BigDecimal.ZERO);

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

	/* Carregar lista de clientes */
	public void carregarListaDeClientes() {
		this.listaDeClientes = pessoaService.listarClientes();
	}

	/* Validar data final maior q a data inicio */
	public Boolean validarDatas(Date ini, Date fim) {
		if (ini != null && fim != null) {
			if (fim.before(ini)) {
				return true;
			}
		}
		return false;
	}

	/* Carregar os tipos de lançamentos */
	public List<TipoConta> getListaTipoLanc() {
		return Arrays.asList(TipoConta.R, TipoConta.CC);
	}

	/* Carregar as contas por tipo de Lançamento */
	public void carregarContasPorTipoCategorias() {
		if (null != this.movto.getTipoConta()) {
			this.listaDePlanoContas = contaService.listarContasPorTipoCategorias(this.movto.getTipoConta(),
					TipoRelatorio.A);
		}
	}

	/* Carregar lista de conta para lançamento de acordo o tipo selecionado */
	public void listaContaLancamento() {
		if (null != this.movto.getTipoConta()) {
			this.listaDePlanoContas = contaService.listarContasPorTipoCategorias(this.movto.getTipoConta(),
					TipoRelatorio.A);
		}
	}

	/* Adicionar o rateio da nota */
	public void addConta() {
		if (!validarDatas(this.cabContaAReceber.getDataDoc(), this.cabContaAReceber.getDataVencto())) {
			int achou = -1;
			for (int i = 0; i < this.listaDeMovtos.size(); i++) {
				if (this.listaDeMovtos.get(i).getPlanoConta().getNome().equals(this.movto.getPlanoConta().getNome())) {
					achou = i;
				}
			}
			if (achou < 0) {
				this.listaDeMovtos.add(this.movto);
				this.movto = new Movimentacao();
				BigDecimal t = BigDecimal.ZERO;
				for (Movimentacao m : this.listaDeMovtos) {
					t = t.add(m.getVlrEntrada());
				}
				this.movto.setTotalRateio(t);
				this.listaDePlanoContas.clear();

				this.parcela.setValor(t);
				this.cabContaAReceber.setValor(t);

			} else {
				Messages.addGlobalError("Conta já cadastrada!");
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.addCallbackParam("sucesso", true);
			}
		} else {
			Messages.addGlobalError("A data de entrada esta maior que a data de vencimento.");
		}
	}

	/* Excluir o rateio da nota */
	public void removerConta() {
		int achou = -1;
		for (int i = 0; i < this.listaDeMovtos.size(); i++) {
			if (this.listaDeMovtos.get(i).getPlanoConta().getNome().equals(movto.getPlanoConta().getNome())) {
				achou = i;
				break;
			}
		}
		if (achou > -1) {
			this.listaDeMovtos.remove(achou);
			BigDecimal t = BigDecimal.ZERO;
			for (Movimentacao m : this.listaDeMovtos) {
				t = t.add(m.getVlrEntrada());
			}

			this.movto.setTotalRateio(t);
			this.parcela.setValor(t);
			this.cabContaAReceber.setValor(t);

			if (this.listaDeMovtos.size() == 0) {

				this.listaDeParcelas.clear();
				this.cabContaAReceber.setValor(BigDecimal.ZERO);
				this.parcela.setTotalPagamento(BigDecimal.ZERO);
				this.parcela.setValor(BigDecimal.ZERO);

			}
		}
	}

	private Usuario obterUsuario() {
		HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false));
		Usuario usuario = null;
		if (session != null) {
			usuario = (Usuario) session.getAttribute("usuarioAutenticado");
		}
		return usuario;
	}

	public List<TipoCobranca> getListaTipoRecebimentos() {
		return Arrays.asList(TipoCobranca.values());
	}

	public void gerarParcelas() {

		BigDecimal qtde_parcela = new BigDecimal(this.parcela.getNumVezes());
		BigDecimal valorParcela = this.parcela.getValor().divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = this.parcela.getValor().subtract(valorParcial);

		this.listaDeParcelas.clear();
		for (int i = 0; i < this.parcela.getNumVezes(); i++) {
			ContaAReceber cr = new ContaAReceber();
			cr.setTipoRecebimento(this.parcela.getTipoRecebimento());
			cr.setParcela((i + 1) + "/" + this.parcela.getNumVezes());
			cr.setDocumento(this.cabContaAReceber.getDocumento());
			cr.setDataVencto(i == 0 ? this.cabContaAReceber.getDataVencto()
					: somaDias(this.cabContaAReceber.getDataVencto(), this.parcela.getPeriodo() * i));
			cr.setValor(i == 0 ? primeiraParcela : valorParcela);
			this.listaDeParcelas.add(cr);
		}
		this.parcela.setTotalPagamento(this.parcela.getValor());
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

	public void salvarEdicaoParcela() {

		BigDecimal recalculo = BigDecimal.ZERO;

		if (!validarDatas(this.cabContaAReceber.getDataDoc(), this.parcelaEdicao.getDataVencto())) {

			for (ContaAReceber cr : this.listaDeParcelas) {
				if (cr.getParcela().equals(this.parcelaEdicao.getParcela())) {
					cr.setDataVencto(this.parcelaEdicao.getDataVencto());
					cr.setValor(this.parcelaEdicao.getValor());
				}
				recalculo = recalculo.add(cr.getValor());
			}
			this.parcela.setTotalPagamento(recalculo);
		} else {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("A data de vencimento dever ser maior que a data de entrada!");
		}

	}

	public void editar() {

		this.permitirEditar = "true";

		this.parcela = new ContaAReceber();
		this.movto = new Movimentacao();
		BigDecimal t = BigDecimal.ZERO, t2 = BigDecimal.ZERO;

		for (ContaAReceber cr : this.listaContasSelecionadas) {

			this.listaDeParcelas = contaAReceberService.porVinculo(cr.getAgrupadorMovimentacao());

			for (ContaAReceber par : this.listaDeParcelas) {
				if (!par.getStatus().contains("ABERTO")) {
					this.permitirEditar = "false";
					break;
				}
				this.permitirEditar = "true";
				t = t.add(par.getValor());
			}

			if (this.permitirEditar == "true") {

				this.listaDeParcelas = contaAReceberService.porVinculo(cr.getAgrupadorMovimentacao());
				this.listaDeMovtos = cr.getMovimentacoes();
				this.cabContaAReceber = cabContaAReceberService.porVinculo(cr.getAgrupadorMovimentacao());

				for (Movimentacao m : this.listaDeMovtos) {
					t2 = t2.add(m.getVlrEntrada());
				}

				this.parcela.setValor(t);
				this.parcela.setTotalPagamento(t);
				this.movto.setTotalRateio(t2);
			}
		}
	}

	public void abrirEdicao() {
		this.parcelaEdicao = new ContaAReceber();
		this.parcelaEdicao.setParcela(this.parcela.getParcela());
		this.parcelaEdicao.setDataVencto(this.parcela.getDataVencto());
		this.parcelaEdicao.setValor(this.parcela.getValor());
	}

	public void novoFiltro() {
		this.filtro = new ContaAReceberFilter();
	}

	public void iniciarBaixaTitulo() {

		this.listaDeRecebimentos = new ArrayList<Recebimento>();

		this.recebimento = new Recebimento();
		this.recebimento.setDataPago(new Date());
		this.recebimento.setFormaBaixa(FormaBaixa.BI);

		this.movto = new Movimentacao();

		this.listaDeMovtos = new ArrayList<Movimentacao>();
		this.listaContasAReceber.clear();

		this.setLabelInfo("nao");

		BigDecimal vlr = BigDecimal.ZERO;

		this.setTotalApagar(vlr);
		this.setTotalPago(vlr);
		this.setTotalMultaJuros(vlr);
		this.setTotalDesc(vlr);

		for (ContaAReceber cr : this.listaContasSelecionadas) {
			contaAReceber = new ContaAReceber();
			contaAReceber.setId(cr.getId());
			contaAReceber.setDataVencto(cr.getDataVencto());
			contaAReceber.setDocumento(cr.getDocumento());
			contaAReceber.setParcela(cr.getParcela());
			contaAReceber.setStatus(cr.getStatus());
			contaAReceber.setTipoRecebimento(cr.getTipoRecebimento());
			contaAReceber.setValor(cr.getValor());
			contaAReceber.setValorApagar(cr.getValorApagar());
			contaAReceber.setValorPago(cr.getValorPago());
			contaAReceber.setPagoTB(cr.getValorApagar());
			contaAReceber.setVinculo(cr.getVinculo());
			contaAReceber.setCliente(cr.getCliente());
			contaAReceber.setDataDoc(cr.getDataDoc());

			this.recebimento.setDescricao(
					"PG. NT." + cr.getDocumento() + " Parc." + cr.getParcela() + " - " + cr.getCliente().getNome());

			contaAReceber.setSaldoDevedor(cr.getValorApagar());

			this.listaContasAReceber.add(contaAReceber);

			vlr = vlr.add(contaAReceber.getSaldoDevedor());

			calcularValorApagar();
		}

		this.recebimento.setValorPago(vlr);
		this.setTotalApagar(vlr);
		this.setTotalPago(vlr);

		this.setTotalSelecionado(vlr);

	}

	public void addRecebimento() {

		if (this.recebimento.getFormaBaixa().equals(FormaBaixa.BI)) {

			this.listaDeRecebimentos.clear();

			for (ContaAReceber c : this.listaContasAReceber) {

				idVinculo = gerarVinculo.gerar(ContaAReceber.class);

				Movimentacao movto = new Movimentacao();
				PlanoConta pl1 = new PlanoConta();
				pl1 = contaService.porId(this.movto.getPlanoConta().getId());

				PlanoConta pl2 = new PlanoConta();
				pl2 = contaService.porId(pl1.getContaPai().getId());

				movto.setDescricao(
						"REC. NT." + c.getDocumento() + " Parc." + c.getParcela() + " - " + c.getCliente().getNome());

				if (c.getValorApagar().compareTo(c.getValorPago()) > 0) {
					movto.setDescricao("REC. NT." + c.getDocumento() + " Parc." + c.getParcela() + " - "
							+ c.getCliente().getNome() + " (P)");
				}

				this.recebimento.setUsuario(obterUsuario());

				movto.setDataDoc(this.recebimento.getDataPago());
				movto.setDataLanc(this.recebimento.getDataPago());
				movto.setUsuario(this.recebimento.getUsuario());
				movto.setVinculo(idVinculo);
				movto.setVlrEntrada(null);
				movto.setVlrSaida(c.getPagoTB());
				movto.setDocumento(c.getDocumento());
				movto.setPessoa(c.getCliente());
				movto.setTipoLanc(TipoLanc.PC);
				movto.setTipoConta(TipoConta.CC);
				movto.setPlanoConta(pl1);
				movto.setPlanoContaPai(pl2);

				listaDeMovtos.add(movto);

				Recebimento r = new Recebimento();

				r.setDataLanc(new Date());
				r.setDataPago(this.recebimento.getDataPago());
				r.setDescricao(movto.getDescricao());
				r.setFormaBaixa(FormaBaixa.BI);
				r.setValor(c.getValor());
				r.setValorAPagar(c.getValorApagar());
				r.setValorDesc(c.getDescTB());
				r.setValorMultaJuros(c.getMultaTB());
				r.setVinculo(idVinculo);
				r.setValorPago(c.getPagoTB());
				r.setUsuario(movto.getUsuario());
				r.setConta(movto.getPlanoConta());

				r.setListaContaARecebers(listaContaARecebers);
				r.setListaMovimentacoes(listaDeMovtos);

				this.listaDeRecebimentos.add(r);

				calcularValorApagar();
			}
		} else {

			calcularValorApagar();

			Movimentacao movto = new Movimentacao();

			if (listaDeMovtos.isEmpty()) {
				idVinculo = gerarVinculo.gerar(ContaAReceber.class);
			}

			this.recebimento.setUsuario(obterUsuario());
			this.recebimento.setVinculo(idVinculo);

			PlanoConta pl1 = new PlanoConta();
			pl1 = contaService.porId(this.movto.getPlanoConta().getId());

			PlanoConta pl2 = new PlanoConta();
			pl2 = contaService.porId(pl1.getContaPai().getId());

			movto.setDataDoc(this.recebimento.getDataPago());
			movto.setDataLanc(this.recebimento.getDataPago());
			movto.setUsuario(this.recebimento.getUsuario());
			movto.setVlrSaida(this.recebimento.getValor());
			movto.setDescricao(descricao);
			movto.setVlrEntrada(null);
			movto.setVinculo(idVinculo);
			movto.setVlrSaida(this.recebimento.getValorPago());
			movto.setTipoLanc(TipoLanc.PC);
			movto.setTipoConta(TipoConta.CC);
			movto.setPlanoConta(pl1);
			movto.setPlanoContaPai(pl2);

			listaDeMovtos.add(movto);

			Recebimento p = new Recebimento();

			p.setDataLanc(new Date());
			p.setDataPago(this.recebimento.getDataPago());
			p.setDescricao(movto.getDescricao());
			p.setFormaBaixa(FormaBaixa.BA);
			p.setValor(totalSelecionado);
			p.setValorAPagar(totalApagar);
			p.setValorDesc(totalDesc);
			p.setValorMultaJuros(totalMultaJuros);
			p.setValorPago(this.recebimento.getValorPago());
			p.setUsuario(this.recebimento.getUsuario());
			p.setConta(movto.getPlanoConta());
			p.setVinculo(idVinculo);

			p.setListaContaARecebers(listaContaARecebers);
			p.setListaMovimentacoes(listaDeMovtos);

			this.listaDeRecebimentos.add(p);

		}
	}

	public void excluirSelecionados() {
		try {
			contaAReceberService.excluirContas(this.listaContaARecebers);
			// this.contaApagarSelecionadas = new ArrayList<>();
			pesquisar();
			this.setTotalSelecionado(BigDecimal.ZERO);
			Messages.addGlobalInfo("Parcela(s) excluida(s) com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Esse registro possui vinculo com outras tabelas!");
		}
	}

	public void excluirRecebimentoAbaixar() {
		if (this.recebimento.getFormaBaixa().equals(FormaBaixa.BI)) {
			this.listaDeMovtos.clear();
			this.listaDeRecebimentos.clear();
			this.recebimento.setValorPago(BigDecimal.ZERO);
			calcularValorApagar();
		} else {
			int achou = -1;
			for (int i = 0; i < this.listaDeMovtos.size(); i++) {
				if (this.listaDeMovtos.get(i).getPlanoConta().getNome()
						.equals(recebimento.getListaMovimentacoes().get(i).getPlanoConta().getNome())) {
					achou = i;
					break;
				}
			}
			if (achou > -1) {
				this.listaDeMovtos.remove(achou);
				this.listaDeRecebimentos.remove(achou);
			}
		}
	}

	public List<PlanoConta> getCarregarContasACreditar() {
		PlanoContaFilter cl = new PlanoContaFilter();
		cl.setTipo(TipoConta.CC);
		cl.setCategoria(TipoRelatorio.A);
		cl.setStatus(true);
		return contaService.filtrados(cl);
	}

	public void calcularTotais() {
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal d = BigDecimal.ZERO;
		BigDecimal t3 = BigDecimal.ZERO;
		BigDecimal t4 = BigDecimal.ZERO;

		for (ContaAReceber c : this.listaContasAReceber) {
			m = m.add(c.getMultaTB());
			d = d.add(c.getDescTB());
			t3 = t3.add(c.getValorApagar());
			t4 = t4.add(c.getPagoTB());
		}

		this.setTotalMultaJuros(m);
		this.setTotalDesc(d);
		this.setTotalApagar(t3);
		this.setTotalPago(t4);

		this.recebimento.setValorPago(t4);
	}

	public void calcularValores() {
		for (ContaAReceber c : this.listaContasAReceber) {
			c.setValorApagar(c.getSaldoDevedor().add(c.getMultaTB().subtract(c.getDescTB())));
			c.setPagoTB(c.getSaldoDevedor().add(c.getMultaTB().subtract(c.getDescTB())));
		}
	}

	public void informativo() {
		labelInfo = "sim";
		if (contaAReceber.getValorApagar().compareTo(contaAReceber.getValorPago()) <= 0) {
			labelInfo = "nao";
		}
	}

	public void calcularValorApagar() {
		BigDecimal c = BigDecimal.ZERO;
		BigDecimal p = BigDecimal.ZERO;

		for (ContaAReceber cr : this.listaContasAReceber) {
			c = c.add(cr.getPagoTB());
		}

		for (Recebimento r : this.listaDeRecebimentos) {
			p = p.add(r.getValorPago());
		}

		if (this.recebimento.getFormaBaixa().equals(FormaBaixa.BA)) {
			p = p.add(this.recebimento.getValorPago());
			if (p.compareTo(c) > 0) {
				FacesContext.getCurrentInstance().validationFailed();
				throw new NegocioException("O valor do recebimento não dever ser maior que o valor à pagar!");
			}
		}

		 saldo = p.subtract(c);
	}
	
	
	public void salvarBaixaMultiplas() {

		BigDecimal c = BigDecimal.ZERO;
		BigDecimal p = BigDecimal.ZERO;

		for (ContaAReceber cp : this.listaContasAReceber) {
			if (cp.getDataDoc().compareTo(this.recebimento.getDataPago()) > 0) {
				throw new NegocioException("A data de recebimento dever ser maior ou igual a data de lançamento ("
						+ formato.format(cp.getDataDoc()) + ") !");
			}
			c = c.add(cp.getPagoTB());
		}

		for (Recebimento pg : this.listaDeRecebimentos) {
			p = p.add(pg.getValorPago());
		}

		if (p.compareTo(c) < 0) {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("Valor de recebimento a menor!");
		}

		contaAReceberService.baixaMultiplas(this.listaContasAReceber, this.listaDeMovtos, this.listaDeRecebimentos,
				this.recebimento);
	}

	public void closeBaixaTitulo() {
		this.listaContasSelecionadas.clear();
		pesquisar();
		/* rowToggleSelect(); */
	}

	public void onCellEdit(CellEditEvent event) {
		String coluna = event.getColumn().getClientId();

		BigDecimal oldValue = (BigDecimal) event.getOldValue();
		BigDecimal newValue = (BigDecimal) event.getNewValue();

		DataTable dataModel = (DataTable) event.getSource();
		ContaAReceber parcela = (ContaAReceber) dataModel.getRowData();

		if (coluna.indexOf("pago") > 0) {
			if (newValue.compareTo(parcela.getValorApagar()) > 0) {
				parcela.setPagoTB(oldValue);

				Integer row = event.getRowIndex();
				dataModel.setRowIndex(row);

				throw new NegocioException("Valor informando a pagar maior que o titulo!");
			}
		}

		if (newValue != null && !newValue.equals(oldValue)) {
			for (ContaAReceber c : this.listaContasAReceber) {
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

	/* Evento ao selecionar uma linha pelo checkbox */
	public void rowSelectCheckBox(SelectEvent event) {
		editar();
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAReceber) event.getObject()).getValorApagar()));
	}

	/* Evento ao deselecionar uma linha no datatable */
	public void rowUnSelect(UnselectEvent event) {
		editar();
		this.setTotalSelecionado(
				this.getTotalSelecionado().subtract(((ContaAReceber) event.getObject()).getValorApagar()));
	}

	/* Evento ao selecionar uma linha pelo checkbox */
	public void rowSelect(SelectEvent event) {
		editar();
		this.setTotalSelecionado(BigDecimal.ZERO);
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAReceber) event.getObject()).getValorApagar()));
		if (this.listaContasSelecionadas.size() > 1) {
			BigDecimal t = BigDecimal.ZERO;
			for (ContaAReceber cr : listaContasSelecionadas) {
				t = t.add(cr.getValorApagar());
				this.setTotalSelecionado(t);
			}
		}
	}

	public List<FormaBaixa> getListaFormaBaixa() {
		return Arrays.asList(FormaBaixa.values());
	}

	public void formaBaixa() {

		if (this.recebimento.getFormaBaixa().equals(FormaBaixa.BI)) {
			this.recebimento.setDescricao(null);
		} else {
			this.listaDeMovtos.clear();
			this.listaDeRecebimentos.clear();

		}
	}

	@SuppressWarnings("unchecked")
	/* Evento ao selecionar todas as linhas no datatable */
	public void onRowSelectAll(ToggleSelectEvent event) {
		DataTable listTemp = (DataTable) event.getSource();
		List<ContaAReceber> list = (List<ContaAReceber>) listTemp.getValue();
		if (event.isSelected()) {
			BigDecimal t = BigDecimal.ZERO;
			for (ContaAReceber p : list) {
				t = t.add(p.getValorApagar());
				this.setTotalSelecionado(t);
			}
			return;
		}
		this.setTotalSelecionado(BigDecimal.ZERO);
	}

	/*
	 * public String getCalculaDif() { BigDecimal dif = contaARce.getValor();
	 * for (ContaAReceber cr : listaDeParcelas) { dif.subtract(cr.getValor()); }
	 * NumberFormat nf = NumberFormat.getCurrencyInstance(); String formatado =
	 * nf.format(dif); return formatado; }
	 */

	/********* Gett e Sett ************/

	public LazyDataModel<ContaAReceber> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<ContaAReceber> model) {
		this.model = model;
	}

	public ContaAReceberFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(ContaAReceberFilter filtro) {
		this.filtro = filtro;
	}

	public ContaAReceber getContaAReceberSelecionado() {
		return contaAReceberSelecionado;
	}

	public void setContaAReceberSelecionado(ContaAReceber contaAReceberSelecionado) {
		this.contaAReceberSelecionado = contaAReceberSelecionado;
	}

	public List<ContaAReceber> getListaContaARecebers() {
		return listaContaARecebers;
	}

	public void setListaContaARecebers(List<ContaAReceber> listaContaARecebers) {
		this.listaContaARecebers = listaContaARecebers;
	}

	public Pessoa getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Pessoa clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public List<Pessoa> getListaDeClientes() {
		return listaDeClientes;
	}

	public void setListaDeClientes(List<Pessoa> listaDeClientes) {
		this.listaDeClientes = listaDeClientes;
	}

	public CabContaAReceber getCabContaAReceber() {
		return cabContaAReceber;
	}

	public void setCabContaAReceber(CabContaAReceber cabContaAReceber) {
		this.cabContaAReceber = cabContaAReceber;
	}

	public Movimentacao getMovto() {
		return movto;
	}

	public void setMovto(Movimentacao movto) {
		this.movto = movto;
	}

	public List<Movimentacao> getListaDeMovtos() {
		return listaDeMovtos;
	}

	public void setListaDeMovtos(List<Movimentacao> listaDeMovtos) {
		this.listaDeMovtos = listaDeMovtos;
	}

	public List<PlanoConta> getListaDePlanoContas() {
		return listaDePlanoContas;
	}

	public void setListaDePlanoContas(List<PlanoConta> listaDePlanoContas) {
		this.listaDePlanoContas = listaDePlanoContas;
	}

	public ContaAReceber getParcela() {
		return parcela;
	}

	public void setParcela(ContaAReceber parcela) {
		this.parcela = parcela;
	}

	public List<ContaAReceber> getListaDeParcelas() {
		return listaDeParcelas;
	}

	public void setListaDeParcelas(List<ContaAReceber> listaDeParcelas) {
		this.listaDeParcelas = listaDeParcelas;
	}

	public ContaAReceber getParcelaEdicao() {
		return parcelaEdicao;
	}

	public void setParcelaEdicao(ContaAReceber parcelaEdicao) {
		this.parcelaEdicao = parcelaEdicao;
	}

	public ContaAReceber getContaAReceber() {
		return contaAReceber;
	}

	public void setContaAReceber(ContaAReceber contaAReceber) {
		this.contaAReceber = contaAReceber;
	}

	public GeradorVinculo getGerarVinculo() {
		return gerarVinculo;
	}

	public void setGerarVinculo(GeradorVinculo gerarVinculo) {
		this.gerarVinculo = gerarVinculo;
	}

	public void setIdVinculo(Long idVinculo) {
		this.idVinculo = idVinculo;
	}

	public List<ContaAReceber> getListaContasSelecionadas() {
		return listaContasSelecionadas;
	}

	public void setListaContasSelecionadas(List<ContaAReceber> listaContasSelecionadas) {
		this.listaContasSelecionadas = listaContasSelecionadas;
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

	public String getPermitirEditar() {
		return permitirEditar;
	}

	public void setPermitirEditar(String permitirEditar) {
		this.permitirEditar = permitirEditar;
	}

	public Long getIdVinculo() {
		return idVinculo;
	}

	public List<Recebimento> getListaDeRecebimentos() {
		return listaDeRecebimentos;
	}

	public void setListaDeRecebimentos(List<Recebimento> listaDeRecebimentos) {
		this.listaDeRecebimentos = listaDeRecebimentos;
	}

	public Recebimento getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}

	public List<ContaAReceber> getListaContasAReceber() {
		return listaContasAReceber;
	}

	public void setListaContasAReceber(List<ContaAReceber> listaContasAReceber) {
		this.listaContasAReceber = listaContasAReceber;
	}

	public String getLabelInfo() {
		return labelInfo;
	}

	public void setLabelInfo(String labelInfo) {
		this.labelInfo = labelInfo;
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

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

}

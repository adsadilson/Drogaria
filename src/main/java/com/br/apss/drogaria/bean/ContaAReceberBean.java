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
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
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

	private List<PlanoConta> listaDePlanoContas;

	private CabContaAReceber cabContaAReceber;

	private Movimentacao movto = new Movimentacao();

	private List<Movimentacao> listaDeMovtos;

	private ContaAReceber parcela;

	private ContaAReceber parcelaEdicao;

	private List<ContaAReceber> listaDeParcelas;

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

	private String permitirEditar;

	@PostConstruct
	public void Inicializar() {
		filtro = new ContaAReceberFilter();
		this.listaContaARecebers = new ArrayList<ContaAReceber>();
		carregarListaDeClientes();
	}

	public void novo() {
		this.contaAReceber = new ContaAReceber();
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

					if (this.contaAReceber.getId() == null) {
						this.contaAReceber.setAgrupadorMovimentacao(gerarVinculo.gerar(Movimentacao.class));
					} else {
						// contaAReceberService.excluirContas(this.listaContasSelecionadas);
					}

					for (int i = 0; i < this.listaDeMovtos.size(); i++) {
						if (this.contaAReceber.getId() != null) {
							this.listaDeMovtos.get(i).setId(null);
						}
						this.listaDeMovtos.get(i)
								.setPlanoContaPai(this.listaDeMovtos.get(i).getPlanoConta().getContaPai());
						this.listaDeMovtos.get(i).setDataDoc(this.cabContaAReceber.getDataDoc());
						this.listaDeMovtos.get(i).setDataLanc(new Date());
						this.listaDeMovtos.get(i).setDocumento(this.cabContaAReceber.getDocumento());
						this.listaDeMovtos.get(i).setVlrEntrada(null);
						this.listaDeMovtos.get(i).setTipoLanc(TipoLanc.CA);
						this.listaDeMovtos.get(i).setTipoConta(TipoConta.D);
						this.listaDeMovtos.get(i).setVinculo(contaAReceber.getAgrupadorMovimentacao());
						this.listaDeMovtos.get(i).setPessoa(this.cabContaAReceber.getCliente());
						this.listaDeMovtos.get(i).setUsuario(obterUsuario());
					}

					this.listaDeMovtos = this.movtoService.salvar(this.listaDeMovtos);

					for (int i = 0; i < this.listaDeParcelas.size(); i++) {
						this.listaDeParcelas.get(i).setDataDoc(this.cabContaAReceber.getDataDoc());
						this.listaDeParcelas.get(i).setStatus("ABERTO");
						this.listaDeParcelas.get(i).setUsuario(obterUsuario());
						this.listaDeParcelas.get(i).setCliente(this.cabContaAReceber.getCliente());
						this.listaDeParcelas.get(i)
								.setAgrupadorMovimentacao(this.contaAReceber.getAgrupadorMovimentacao());
						this.listaDeParcelas.get(i).setValorApagar(this.listaDeParcelas.get(i).getValor());
					}

					for (int i = 0; i < this.listaDeParcelas.size(); i++) {
						this.listaDeParcelas.get(i).setMovimentacoes(this.listaDeMovtos);
					}

					this.cabContaAReceber.setListaContaARecebers(this.listaDeParcelas);
					this.cabContaAReceber.setVinculo(this.contaAReceber.getAgrupadorMovimentacao());
					cabContaAReceberService.salvar(this.cabContaAReceber);

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

			/*
			 * this.parcela.setValor(t); this.setTotalDaNotaMovimentacao(t);
			 */
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

}

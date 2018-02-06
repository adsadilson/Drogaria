package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

import com.br.apss.drogaria.enums.FormaBaixa;
import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.service.ContaAPagarService;
import com.br.apss.drogaria.service.MovimentacaoService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.PlanoContaService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;

@Named
@ViewScoped
public class ContaAPagarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAPagar contaAPagar;

	private ContaAPagarFilter filtro;

	private BigDecimal saldo;

	private BigDecimal totalSelecionado = BigDecimal.ZERO;

	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	private List<ContaAPagar> contaApagarSelecionadas = new ArrayList<ContaAPagar>();

	private List<PlanoConta> listaContas = new ArrayList<PlanoConta>();

	private ContaAPagar parcela;

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
	GeradorVinculo gerarVinculo;

	private BigDecimal totalAVencer = BigDecimal.ZERO;

	private BigDecimal totalAVencido = BigDecimal.ZERO;

	private BigDecimal totalGeral = BigDecimal.ZERO;

	private boolean isToggle = false;

	/******************** Metodos ***********************/

	public void inicializar() {
		contaAPagar = new ContaAPagar();
		filtro = new ContaAPagarFilter();
		movimentacao = new Movimentacao();
		pesquisar();
	}

	public void rowSelect(SelectEvent event) {
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
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAPagar) event.getObject()).getValor()));
	}

	public void rowUnSelect(UnselectEvent event) {
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

	public void excluirSelecionados() {
		try {
			contaAPagarService.excluirContas(contaApagarSelecionadas);
			pesquisar();
			contaApagarSelecionadas = new ArrayList<>();
			Messages.addGlobalInfo("Parcela(s) excluida(s) com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Não é possivel excluir uma conta a pagar se existe outras listaParcelas, "
					+ "selecionas todas para excluir!");
		}
	}

	public void editar() {
		this.parcela = new ContaAPagar();
		this.movimentacao = new Movimentacao();
		BigDecimal t = BigDecimal.ZERO, t2 = BigDecimal.ZERO;
		for (ContaAPagar cp : contaApagarSelecionadas) {
			this.listaParcelas = contaAPagarService.porVinculo(cp.getVinculo());
			this.contaApagarSelecionadas = this.listaParcelas;
			this.listaMovimentacoes = cp.getMovimentacoes();
			for (int i = 0; i < this.listaParcelas.size();) {
				this.contaAPagar = this.listaParcelas.get(0);
				break;
			}
		}
		for (ContaAPagar c : this.listaParcelas) {
			t = t.add(c.getValor());
		}

		for (Movimentacao m : this.listaMovimentacoes) {
			t2 = t2.add(m.getVlrSaida());
		}
		this.parcela.setTotalFormaPg(t);
		this.parcela.setTotalRateio(t2);

	}

	public void editarParcela() {

		BigDecimal recalculo = BigDecimal.ZERO;

		for (ContaAPagar pp : this.listaParcelas) {
			if (this.parcela.getParcela().equals(pp.getParcela())) {

			}
			recalculo = recalculo.add(pp.getValor());

		}

		this.parcela.setTotalFormaPg(recalculo);

		Messages.addGlobalError("Registro alterado com sucesso!");
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam("sucesso", true);
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
	 * if (null != cp.getParcela()) { // pegar só numero converter em int e soma
	 * com i depois // converter em string int p =
	 * Integer.parseInt(cp.getParcela().replaceAll("\\D", "")); p = p + (i + 1);
	 * c.setParcela("D/" + String.valueOf(p)); } else { c.setParcela("D/" + (i +
	 * 1)); }
	 * 
	 * c.setDataVencto(somaDias(cp.getDataVencto(), 30 * (i + 1)));
	 * contaAPagarService.salvar(c); } pesquisar(); } }
	 */
	public void novo() {
		this.contaAPagar = new ContaAPagar();
		this.contaAPagar.setDataDoc(new Date());
		this.parcela = new ContaAPagar();
		this.listaMovimentacoes = new ArrayList<Movimentacao>();
		this.listaParcelas = new ArrayList<ContaAPagar>();
	}

	public void carregarContasPorTipoCategorias() {
		if (null != this.contaAPagar.getTipoConta()) {
			this.listaContas = contaService.listarContasPorTipoCategorias(this.contaAPagar.getTipoConta(),
					TipoRelatorio.A);
		}
	}

	public void carregarContasADebitar() {
		PlanoContaFilter cl = new PlanoContaFilter();
		cl.setTipo(TipoConta.CC);
		cl.setStatus(true);
		this.listaContas = contaService.filtrados(cl);
	}

	public void iniciarBaixaTitulo() {
		contaAPagar = new ContaAPagar();
		contaAPagar.setDataPagto(new Date());

		BigDecimal vlr = BigDecimal.ZERO;
		for (ContaAPagar cp : contaApagarSelecionadas) {
			vlr = vlr.add(cp.getValor());
		}
		contaAPagar.setValor(vlr);

		carregarContasADebitar();
		calcJurDesMul();

	}

	public void baixarTitulos() {

	}

	public void calcJurDesMul() {
		BigDecimal t = BigDecimal.ZERO;
		BigDecimal multa = contaAPagar.getValorMulta();
		BigDecimal juro = contaAPagar.getValorJuro();
		BigDecimal desc = contaAPagar.getValorDesc();
		t = t.add(contaAPagar.getValor().add(multa).add(juro).subtract(desc));
		contaAPagar.setValorPago(t);

	}

	public List<FormaBaixa> getListaFormaBaixa() {
		return Arrays.asList(FormaBaixa.values());
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

	public Boolean validarDatas(Date ini, Date fim) {
		if (ini != null && fim != null) {
			if (fim.before(ini)) {
				return true;
			}
		}
		return false;
	}

	public void pesquisar() {
		listaContaAPagars = contaAPagarService.filtrados(filtro);

		for (ContaAPagar c : listaContaAPagars) {
			c.setDias(intervaloDias(c.getDataVencto(), new Date()));

			if (c.getDataVencto().before(new Date())) {
				this.totalAVencido = this.totalAVencido.add(c.getValor());
			} else {
				this.totalAVencer = this.totalAVencer.add(c.getValor());
			}
			this.totalGeral = this.totalGeral.add(c.getValor());
		}
	}

	public List<Status> getStatus() {
		return Arrays.asList(Status.values());
	}

	public void gerarParcelas() {

		BigDecimal qtde_parcela = new BigDecimal(this.parcela.getNumVezes());
		BigDecimal valorParcela = this.parcela.getValor().divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = this.parcela.getValor().subtract(valorParcial);
		// this.contaAPagar.setVinculo(gerarVinculo.gerar(ContaAPagar.class));

		this.listaParcelas = new ArrayList<ContaAPagar>();
		for (int i = 0; i < this.parcela.getNumVezes(); i++) {
			ContaAPagar ap = new ContaAPagar();
			ap.setTipoCobranca(this.parcela.getTipoCobranca());
			ap.setParcela((i + 1) + "/" + this.parcela.getNumVezes());
			ap.setNumDoc(this.contaAPagar.getNumDoc());
			ap.setDataVencto(i == 0 ? this.contaAPagar.getDataVencto()
					: somaDias(this.contaAPagar.getDataVencto(), this.parcela.getPeriodo() * i));
			ap.setValor(i == 0 ? primeiraParcela : valorParcela);
			// ap.setVlrApagar(i == 0 ? primeiraParcela : valorParcela);
			// ap.setMovimentacoes(contaAPagar.getMovimentacoes());
			this.listaParcelas.add(ap);
		}
		this.parcela.setTotalFormaPg(this.parcela.getValor());
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
			this.parcela.setTotalRateio(t);
			this.parcela.setValor(t);
			if (this.listaMovimentacoes.size() == 00) {
				this.listaParcelas.clear();
				this.parcela.setTotalFormaPg(BigDecimal.ZERO);
			}
		}
	}

	public void addConta() {
		if (!validarDatas(this.contaAPagar.getDataDoc(), this.contaAPagar.getDataVencto())) {
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
				this.parcela.setTotalRateio(t);
				this.parcela.setValor(t);
			} else {
				Messages.addGlobalError("Conta j� cadastrada!");
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.addCallbackParam("sucesso", true);
			}
		} else {
			Messages.addGlobalError("A data de entrada esta maior que a data de vencimento.");
		}
	}

	public void salvar() throws Exception {

		if (!validarDatas(this.contaAPagar.getDataDoc(), this.contaAPagar.getDataVencto())) {

			if (!this.parcela.getTotalFormaPg().equals(BigDecimal.ZERO)) {

				if (this.parcela.getTotalRateio().equals(this.parcela.getTotalFormaPg())) {

					if (this.contaAPagar.getId() == null) {
						this.contaAPagar.setVinculo(gerarVinculo.gerar(ContaAPagar.class));
					} else {
						contaAPagarService.excluirContas(contaApagarSelecionadas);
					}

					for (int i = 0; i < this.listaMovimentacoes.size(); i++) {
						if (this.contaAPagar.getId() != null) {
							this.listaMovimentacoes.get(i).setId(null);
						}
						this.listaMovimentacoes.get(i)
								.setPlanoContaPai(this.listaMovimentacoes.get(i).getPlanoConta().getContaPai());
						this.listaMovimentacoes.get(i).setDataDoc(this.contaAPagar.getDataDoc());
						this.listaMovimentacoes.get(i).setDataLanc(new Date());
						this.listaMovimentacoes.get(i).setDocumento(this.contaAPagar.getNumDoc());
						this.listaMovimentacoes.get(i).setVlrEntrada(null);
						this.listaMovimentacoes.get(i).setPessoa(this.contaAPagar.getFornecedor());
						this.listaMovimentacoes.get(i).setUsuario(obterUsuario());
						this.listaMovimentacoes.get(i).setDocumento(this.contaAPagar.getNumDoc());
					}

					for (int i = 0; i < this.listaParcelas.size(); i++) {
						this.listaParcelas.get(i).setDataDoc(this.contaAPagar.getDataDoc());
						this.listaParcelas.get(i).setDataLanc(new Date());
						this.listaParcelas.get(i).setNumDoc(this.contaAPagar.getNumDoc());
						this.listaParcelas.get(i).setStatus("ABERTO");
						this.listaParcelas.get(i).setFornecedor(this.contaAPagar.getFornecedor());
						this.listaParcelas.get(i).setUsuario(obterUsuario());
						this.listaParcelas.get(i).setVinculo(this.contaAPagar.getVinculo());
					}

					this.listaMovimentacoes = this.movtoServico.salvar(this.listaMovimentacoes);

					for (int i = 0; i < this.listaParcelas.size(); i++) {
						this.listaParcelas.get(i).setMovimentacoes(this.listaMovimentacoes);
						contaAPagarService.salvar(this.listaParcelas.get(i));
					}

					RequestContext request = RequestContext.getCurrentInstance();
					request.addCallbackParam("sucesso", true);
					Messages.addGlobalInfo("Registro atualizado com sucesso.");

					pesquisar();

				} else {
					Messages.addGlobalError("Total do rateio diferente do total de pagamento");
				}
			} else {
				Messages.addGlobalError("Não foi informado nenhum valor para forma de pagamento");
			}
		} else {
			Messages.addGlobalInfo("Data da entrada est� maior que a data de vencimento.");
		}
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

	public BigDecimal getTotalAVencido() {
		return totalAVencido;
	}

	public BigDecimal getTotalGeral() {
		return totalGeral;
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

}

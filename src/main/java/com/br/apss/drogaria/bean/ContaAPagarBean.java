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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.UnselectEvent;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.enums.TipoConta;
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

@Named
@ViewScoped
public class ContaAPagarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAPagar contaAPagar;

	private ContaAPagarFilter filtro;

	private BigDecimal saldo;

	private BigDecimal totalSelecionado = BigDecimal.ZERO;

	private int numVezes = 1;

	private int periodo;

	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	private List<ContaAPagar> contaApagarSelecionadas = new ArrayList<ContaAPagar>();

	private List<PlanoConta> listaContas = new ArrayList<PlanoConta>();

	private List<ContaAPagar> parcelas = new ArrayList<ContaAPagar>();

	private Movimentacao movto;

	private BigDecimal totalRateio = BigDecimal.ZERO;

	private BigDecimal valor = BigDecimal.ZERO;

	private BigDecimal totalPagto = BigDecimal.ZERO;

	@Inject
	private ContaAPagarService contaAPagarService;

	@Inject
	private MovimentacaoService movtoService;

	@Inject
	private PlanoContaService contaService;

	@Inject
	private PessoaService pessoaService;

	private BigDecimal totalAVencer = BigDecimal.ZERO;

	private BigDecimal totalAVencido = BigDecimal.ZERO;

	private BigDecimal totalGeral = BigDecimal.ZERO;

	private boolean isToggle = false;

	private boolean vermelho;

	/******************** Metodos ***********************/

	public void inicializar() {
		contaAPagar = new ContaAPagar();
		filtro = new ContaAPagarFilter();
		movto = new Movimentacao();
		periodo = 30;
		pesquisar();
	}

	public void salvar() {
		if (!this.valor.equals(BigDecimal.ZERO)) {
			if (totalRateio.equals(totalPagto)) {

				List<Movimentacao> movimentos = movtoService.salvar(contaAPagar.getMovimentacoes());

				for (ContaAPagar ca : parcelas) {
					ca.setMovimentacoes(movimentos);
					contaAPagarService.salvar(ca);
				}

				RequestContext request = RequestContext.getCurrentInstance();
				request.addCallbackParam("sucesso", true);
				Messages.addGlobalInfo("Registro salvor com sucesso.");
				pesquisar();
			} else {
				Messages.addGlobalError("Total do rateio diferente do total de pagamento");
			}
		} else {
			Messages.addGlobalError("Não foi informado nenhum valor");
		}
	}

	public void rowSelect(SelectEvent event) {
		this.setTotalSelecionado(this.getTotalSelecionado().add(((ContaAPagar) event.getObject()).getValor()));
	}

	public void rowUnSelect(UnselectEvent event) {
		this.setTotalSelecionado(this.getTotalSelecionado().subtract(((ContaAPagar) event.getObject()).getValor()));
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
			Messages.addGlobalError("Não é possivel excluir uma conta a pagar se existe outras parcelas, "
					+ "selecionas todas para excluir!");
		}
	}

	public void editar() {
		// this.contaAPagar =
		// contaAPagarService.porId(this.cPSelecionado.getId());
	}

	public void editarParcela() {
		BigDecimal recalculo = BigDecimal.ZERO;
		for (ContaAPagar pp : parcelas) {
			recalculo = recalculo.add(pp.getValor());
		}
		totalPagto = recalculo;
		Messages.addGlobalError("Registro alterado com sucesso!");
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.addCallbackParam("sucesso", true);
	}

	public void duplicarLancamento() {
		for (ContaAPagar cp : contaApagarSelecionadas) {
			for (int i = 0; i < numVezes; i++) {
				ContaAPagar c = new ContaAPagar();
				c.setDataDoc(somaDias(cp.getDataDoc(), 30 * (i + 1)));
				c.setDataLanc(cp.getDataLanc());
				c.setValor(cp.getValor());
				c.setValorPago(cp.getValorPago());
				c.setVlrApagar(cp.getVlrApagar());
				c.setFornecedor(cp.getFornecedor());
				c.setUsuario(cp.getUsuario());
				c.setTipoCobranca(cp.getTipoCobranca());
				c.setStatus(cp.getStatus());
				c.setNumDoc(cp.getNumDoc());

				if (null != cp.getParcela()) {
					// pegar só numero converter em int e soma com i depois
					// converter em string
					int p = Integer.parseInt(cp.getParcela().replaceAll("\\D", ""));
					p = p + (i + 1);
					c.setParcela("D/" + String.valueOf(p));
				} else {
					c.setParcela("D/" + (i + 1));
				}

				c.setDataVencto(somaDias(cp.getDataVencto(), 30 * (i + 1)));
				contaAPagarService.salvar(c);
			}
			pesquisar();
		}
	}

	public void novo() {
		contaAPagar = new ContaAPagar();
		contaAPagar.setDataDoc(new Date());
		parcelas = new ArrayList<ContaAPagar>();
		numVezes = 1;
		totalPagto = BigDecimal.ZERO;
		totalRateio = BigDecimal.ZERO;
	}

	public void carregarContasLanctos() {
		PlanoContaFilter cl = new PlanoContaFilter();
		if (null != this.contaAPagar.getTipoConta()) {
			cl.setTipo(this.contaAPagar.getTipoConta());
			cl.setStatus(true);
			this.listaContas = contaService.filtrados(cl);
		}
	}

	public List<Pessoa> getCarregarFornecedores() {
		return pessoaService.listarTodos();
	}

	public void iniciarLancRateio() {
		movto = new Movimentacao();
	}

	public void removerConta() {
		int achou = -1;
		for (int i = 0; i < this.contaAPagar.getMovimentacoes().size(); i++) {
			if (this.contaAPagar.getMovimentacoes().get(i).getPlanoConta().getNome()
					.equals(movto.getPlanoConta().getNome())) {
				achou = i;
				break;
			}
		}
		if (achou > -1) {
			contaAPagar.getMovimentacoes().remove(achou);
			totalRateio = totalRateio.subtract(movto.getVlrSaida());
			contaAPagar.setValor(totalRateio);
		}
	}

	public void addConta() {
		if (!validarDatas(this.contaAPagar.getDataDoc(), this.contaAPagar.getDataVencto())) {
			int achou = -1;
			for (int i = 0; i < this.contaAPagar.getMovimentacoes().size(); i++) {
				if (this.contaAPagar.getMovimentacoes().get(i).getPlanoConta().getNome()
						.equals(movto.getPlanoConta().getNome())) {
					achou = i;
				}
			}
			if (achou < 0) {
				movto.setDataDoc(contaAPagar.getDataDoc());
				movto.setDataLanc(new Date());
				movto.setUsuario(obterUsuario());
				movto.setVlrEntrada(null);
				movto.setDocumento(contaAPagar.getNumDoc());
				movto.setPessoa(contaAPagar.getFornecedor());
				contaAPagar.getMovimentacoes().add(movto);
				totalRateio = totalRateio.add(movto.getVlrSaida());
				contaAPagar.setValor(totalRateio);
				this.setValor(totalRateio);
				movto = new Movimentacao();
			} else {
				Messages.addGlobalError("Conta j� cadastrada!");
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.addCallbackParam("sucesso", true);
			}
		} else {
			Messages.addGlobalError("A data de entrada esta maior que a data de vencimento.");
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
				vermelho = true;
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

		BigDecimal qtde_parcela = new BigDecimal(numVezes);
		totalPagto = valor;
		BigDecimal valorParcela = valor.divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = valor.subtract(valorParcial);

		parcelas = new ArrayList<ContaAPagar>();
		for (int i = 0; i < numVezes; i++) {
			ContaAPagar ap = new ContaAPagar();
			ap.setDataDoc(contaAPagar.getDataDoc());
			ap.setDataLanc(new Date());
			ap.setFornecedor(contaAPagar.getFornecedor());
			ap.setNumDoc(contaAPagar.getNumDoc().isEmpty() ? null : contaAPagar.getNumDoc() + "-" + (i + 1));
			ap.setTipoCobranca(contaAPagar.getTipoCobranca());
			ap.setUsuario(obterUsuario());
			ap.setStatus("ABERTO");
			ap.setParcela((i + 1) + "/" + numVezes);
			ap.setDataVencto(i == 0 ? contaAPagar.getDataVencto() : somaDias(contaAPagar.getDataVencto(), periodo * i));
			ap.setValor(i == 0 ? primeiraParcela : valorParcela);
			ap.setVlrApagar(i == 0 ? primeiraParcela : valorParcela);
			ap.setMovimentacoes(contaAPagar.getMovimentacoes());
			parcelas.add(ap);
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
		for (ContaAPagar cp : parcelas) {
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

	public int getNumVezes() {
		return numVezes;
	}

	public void setNumVezes(int numVezes) {
		this.numVezes = numVezes;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public List<PlanoConta> getListaContas() {
		return listaContas;
	}

	public void setListaContas(List<PlanoConta> listaContas) {
		this.listaContas = listaContas;
	}

	public Movimentacao getMovto() {
		return movto;
	}

	public void setMovto(Movimentacao movto) {
		this.movto = movto;
	}

	public BigDecimal getTotalRateio() {
		return totalRateio;
	}

	public void setTotalRateio(BigDecimal totalRateio) {
		this.totalRateio = totalRateio;
	}

	public List<ContaAPagar> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ContaAPagar> parcelas) {
		this.parcelas = parcelas;
	}

	public BigDecimal getTotalPagto() {
		return totalPagto;
	}

	public void setTotalPagto(BigDecimal totalPagto) {
		this.totalPagto = totalPagto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public boolean isVermelho() {
		return vermelho;
	}

	public void setVermelho(boolean vermelho) {
		this.vermelho = vermelho;
	}

	public BigDecimal getTotalSelecionado() {
		return totalSelecionado;
	}

	public void setTotalSelecionado(BigDecimal totalSelecionado) {
		this.totalSelecionado = totalSelecionado;
	}

}

package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.service.ContaAPagarService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.PlanoContaService;

@Named
@ViewScoped
public class ContaAPagarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAPagar contaAPagar;

	private ContaAPagar cPSelecionado;

	private ContaAPagarFilter filtro;

	private LazyDataModel<ContaAPagar> model;

	private BigDecimal saldo;

	private int numVezes = 1;

	private int periodo;

	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	private List<PlanoConta> listaContas = new ArrayList<PlanoConta>();

	private Movimentacao movto;

	private BigDecimal totalRateio = BigDecimal.ZERO;

	@Inject
	private ContaAPagarService contaAPagarService;

	@Inject
	private PlanoContaService contaService;

	@Inject
	private PessoaService pessoaService;

	/******************** Metodos ***********************/

	public void inicializar() {
		contaAPagar = new ContaAPagar();
		cPSelecionado = new ContaAPagar();
		filtro = new ContaAPagarFilter();
		movto = new Movimentacao();
		periodo = 30;
		pesquisar();
	}

	public void salvar() {

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		contaAPagarService.salvar(contaAPagar);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.contaAPagar = contaAPagarService.porId(this.cPSelecionado.getId());
	}

	public void novo() {
		contaAPagar = new ContaAPagar();
		cPSelecionado = new ContaAPagar();
	}

	public void carregarContasLanctos() {
		PlanoContaFilter cl = new PlanoContaFilter();
		if (null != this.cPSelecionado.getTipoConta()) {
			cl.setTipo(this.cPSelecionado.getTipoConta());
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

	public void addConta() {
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
			movto.setDescricao(cPSelecionado.getDescricao());
			movto.setVlrSaida(cPSelecionado.getValor());
			movto.setDocumento(cPSelecionado.getNumDoc());
			contaAPagar.getMovimentacoes().add(movto);
			totalRateio = totalRateio.add(cPSelecionado.getValor());
			contaAPagar.setValor(totalRateio);
			movto = new Movimentacao();
			cPSelecionado = new ContaAPagar();
		} else {
			Messages.addGlobalError("Conta já cadastrada!");
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.addCallbackParam("sucesso", true);
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

	public void removerConta() {
		System.out.println(contaAPagar.getDescricao());
		/*int achou = -1;
		for (int i = 0; i < this.contaAPagar.getMovimentacoes().size(); i++) {
			if (this.contaAPagar.getMovimentacoes().get(i).getPlanoConta().getNome()
					.equals(movto.getPlanoConta().getNome())) {
				achou = i;
			}
		}
		if (achou > -1) {
			contaAPagar.getMovimentacoes().remove(achou);
			totalRateio = totalRateio.subtract(movto.getVlrSaida());
			contaAPagar.setValor(totalRateio);
		}*/
	}

	public void novoFiltro() {
		this.filtro = new ContaAPagarFilter();
	}

	public List<TipoConta> getListaTipoContas() {
		return Arrays.asList(TipoConta.D, TipoConta.CC);
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

			model = new LazyDataModel<ContaAPagar>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<ContaAPagar> load(int first, int pageSize, String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {

					setRowCount(contaAPagarService.qtdeFiltrados(filtro));

					filtro.setPrimerioRegistro(first);
					filtro.setQuantidadeRegistros(pageSize);
					filtro.setCampoOrdernacao(sortField);
					filtro.setAsc(SortOrder.ASCENDING.equals(sortOrder));

					return listaContaAPagars = contaAPagarService.filtrados(filtro);

				}

				@Override
				public ContaAPagar getRowData(String rowKey) {
					cPSelecionado = contaAPagarService.porId(Long.valueOf(rowKey));
					return cPSelecionado;
				}

				@Override
				public String getRowKey(ContaAPagar objeto) {
					return cPSelecionado.getId().toString();
				}

			};

			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
		} else {
			Messages.addGlobalError("Data inicio maior do que data final!");
		}

	}

	public void preparEdicao() {
		this.contaAPagar = contaAPagarService.porId(this.cPSelecionado.getId());
	}

	public void excluir() {
		contaAPagarService.excluir(cPSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public List<Status> getStatus() {
		return Arrays.asList(Status.values());
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

	public ContaAPagar getcPSelecionado() {
		return cPSelecionado;
	}

	public void setcPSelecionado(ContaAPagar cPSelecionado) {
		this.cPSelecionado = cPSelecionado;
	}

	public LazyDataModel<ContaAPagar> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<ContaAPagar> model) {
		this.model = model;
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

}

package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.CabContaAReceber;
import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.Movimentacao;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
import com.br.apss.drogaria.service.ContaAReceberService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.PlanoContaService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;

@Named
@ViewScoped
public class ContaAReceberBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAReceberFilter filtro;

	private ContaAReceber contaAReceberSelecionado;

	private List<ContaAReceber> listaContaARecebers;

	private LazyDataModel<ContaAReceber> model;

	private Pessoa clienteSelecionado;

	private List<Pessoa> listaDeClientes = new ArrayList<Pessoa>();

	private List<PlanoConta> listaDePlanoContas;

	private CabContaAReceber cabContaAReceber;

	private Movimentacao movto;

	private List<Movimentacao> listaDeMovtos;

	@Inject
	private ContaAReceberService contaAReceberService;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private PlanoContaService contaService;

	@Inject
	private GeradorVinculo idVinculo;

	@PostConstruct
	public void Inicializar() {
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
	}

	public void salvar() {
		System.out.println("teste");
	}

	public void editar() {
	}

	public void excluir() {
		Messages.addGlobalInfo("Registro excluido com sucesso");
	}

	public void pesquisar() {
		model = new LazyDataModel<ContaAReceber>() {

			private static final long serialVersionUID = 1L;

			@Override
			public List<ContaAReceber> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				setRowCount(contaAReceberService.quantidadeFiltrados(filtro));

				filtro.setPrimeiroRegistro(first);
				filtro.setQtdeRegistro(pageSize);
				filtro.setOrdenacao(sortField);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));

				listaContaARecebers = contaAReceberService.filtrados(filtro);

				return listaContaARecebers;
			}

			@Override
			public ContaAReceber getRowData(String rowKey) {
				contaAReceberSelecionado = contaAReceberService.porId(Long.valueOf(rowKey));
				return contaAReceberSelecionado;
			}

			@Override
			public String getRowKey(ContaAReceber objeto) {
				return contaAReceberSelecionado.getId().toString();
			}
		};
	}

	public void carregarListaDeClientes() {
		this.listaDeClientes = pessoaService.listarClientes();
	}

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
	
	public void listaContaLancamento() {
		if (null != this.movto.getTipoConta()) {
			this.listaDePlanoContas = contaService.listarContasPorTipoCategorias(this.movto.getTipoConta(),
					TipoRelatorio.A);
		}
	}

	@SuppressWarnings("unused")
	private Usuario obterUsuario() {
		HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false));
		Usuario usuario = null;
		if (session != null) {
			usuario = (Usuario) session.getAttribute("usuarioAutenticado");
		}
		return usuario;
	}

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

	public GeradorVinculo getIdVinculo() {
		return idVinculo;
	}

	public void setIdVinculo(GeradorVinculo idVinculo) {
		this.idVinculo = idVinculo;
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

}

package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.filter.ContaAPagarFilter;
import com.br.apss.drogaria.service.ContaAPagarService;

@Named
@ViewScoped
public class ContaAPagarBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAPagar contaAPagar = new ContaAPagar();

	private ContaAPagar contaAPagarSelecionado;

	private ContaAPagarFilter filtro = new ContaAPagarFilter();

	private LazyDataModel<ContaAPagar> model;

	private BigDecimal saldo;

	private int numVezes = 1;

	private int periodo;

	private List<ContaAPagar> listaContaAPagars = new ArrayList<ContaAPagar>();

	@Inject
	private ContaAPagarService contaAPagarService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.contaAPagar == null) {
			novo();
		}
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
		this.contaAPagar = contaAPagarService.porId(this.contaAPagarSelecionado.getId());
	}

	public void novo() {
		contaAPagar = new ContaAPagar();
	}

	public void novoFiltro() {
		this.filtro = new ContaAPagarFilter();
	}

	public List<TipoCobranca> getListaTipoCobranças() {
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
					contaAPagarSelecionado = contaAPagarService.porId(Long.valueOf(rowKey));
					return contaAPagarSelecionado;
				}

				@Override
				public String getRowKey(ContaAPagar objeto) {
					return contaAPagarSelecionado.getId().toString();
				}

			};

			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
		} else {
			Messages.addGlobalError("Data inicio maior do que data final!");
		}

	}

	public void preparEdicao() {
		this.contaAPagar = contaAPagarService.porId(this.contaAPagarSelecionado.getId());
	}

	public void excluir() {
		contaAPagarService.excluir(contaAPagarSelecionado);
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

	public ContaAPagar getContaAPagarSelecionado() {
		return contaAPagarSelecionado;
	}

	public void setContaAPagarSelecionado(ContaAPagar contaAPagarSelecionado) {
		this.contaAPagarSelecionado = contaAPagarSelecionado;
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

}

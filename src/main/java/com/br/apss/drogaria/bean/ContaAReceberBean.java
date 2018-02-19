package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.br.apss.drogaria.model.ContaAReceber;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.ContaAReceberFilter;
import com.br.apss.drogaria.service.ContaAReceberService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;

@Named
@ViewScoped
public class ContaAReceberBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaAReceberFilter filtro;

	private ContaAReceber contaAReceberSelecionado;

	private List<ContaAReceber> listaContaARecebers;

	private LazyDataModel<ContaAReceber> model;

	@Inject
	ContaAReceberService contaAReceberService;

	@Inject
	GeradorVinculo idVinculo;

	@PostConstruct
	public void Inicializar() {
		filtro = new ContaAReceberFilter();
		this.listaContaARecebers = new ArrayList<ContaAReceber>();
	}

	public void novo() {

	}

	public void salvar() {

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

	public Boolean validarDatas(Date ini, Date fim) {
		if (ini != null && fim != null) {
			if (fim.before(ini)) {
				return true;
			}
		}
		return false;
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

}

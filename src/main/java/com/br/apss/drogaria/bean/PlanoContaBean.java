package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.service.PlanoContaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class PlanoContaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PlanoConta planoConta = new PlanoConta();

	private PlanoConta planoContaSelecionado;

	private PlanoContaFilter filtro = new PlanoContaFilter();

	private List<PlanoConta> listaPlanoContas = new ArrayList<PlanoConta>();

	private List<PlanoConta> listaContaPais;

	private boolean clonarSimNao = false;

	@Inject
	private PlanoContaService planoContaService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.planoConta == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		PlanoConta planoContaExistente = planoContaService.porMascara(planoConta.getMascara());
		if (planoContaExistente != null && !planoContaExistente.equals(planoConta)) {
			throw new NegocioException("Já existe um registro com essa mascara informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		planoContaService.salvar(planoConta);
		filtro = new PlanoContaFilter();
		clonarSimNao = false;
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.planoConta = planoContaService.porId(this.planoContaSelecionado.getId());
		carregarListaContaPai();
	}

	public void clonar() {
		this.planoConta = planoContaService.porId(this.planoContaSelecionado.getId());
		this.planoConta.setId(null);
		clonarSimNao = true;
		carregarListaContaPai();
	}

	public void novo() {
		planoConta = new PlanoConta();
		listaContaPais = new ArrayList<PlanoConta>();
		carregarListaContaPai();
	}

	public void novoFiltro() {
		this.filtro = new PlanoContaFilter();
	}

	public void pesquisar() {
		this.listaPlanoContas = planoContaService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.planoConta = planoContaService.porId(this.planoContaSelecionado.getId());
	}

	public void excluir() {
		planoContaService.excluir(planoContaSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public void carregarListaContaPai() {
		filtro.setCategoria(TipoRelatorio.S);
		listaContaPais = planoContaService.filtrados(filtro);
	}

	public List<TipoConta> getTipoContas() {
		return Arrays.asList(TipoConta.values());
	}

	public List<TipoRelatorio> getTipoRelat() {
		return Arrays.asList(TipoRelatorio.values());
	}

	/******************** Getters e Setters ***************************/

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public List<PlanoConta> getListaPlanoContas() {
		return listaPlanoContas;
	}

	public void setListaPlanoContas(List<PlanoConta> listaPlanoContas) {
		this.listaPlanoContas = listaPlanoContas;
	}

	public PlanoConta getPlanoContaSelecionado() {
		return planoContaSelecionado;
	}

	public void setPlanoContaSelecionado(PlanoConta planoContaSelecionado) {
		this.planoContaSelecionado = planoContaSelecionado;
	}

	public List<PlanoConta> getListaContaPais() {
		return listaContaPais;
	}

	public void setListaContaPais(List<PlanoConta> listaContaPais) {
		this.listaContaPais = listaContaPais;
	}

	public boolean isClonarSimNao() {
		return clonarSimNao;
	}

	public void setClonarSimNao(boolean clonarSimNao) {
		this.clonarSimNao = clonarSimNao;
	}

	public PlanoContaFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(PlanoContaFilter filtro) {
		this.filtro = filtro;
	}

}

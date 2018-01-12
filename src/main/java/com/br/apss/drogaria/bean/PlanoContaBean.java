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

		PlanoConta planoContaExistente = planoContaService.porNomeTipo(planoConta.getNome(),planoConta.getTipo());
		if (planoContaExistente != null && !planoContaExistente.equals(planoConta)) {
			throw new NegocioException("Já existe um registro com essa nome informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		planoContaService.salvar(planoConta);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.planoConta = planoContaService.porId(this.planoContaSelecionado.getId());
	}

	public void novo() {
		planoConta = new PlanoConta();
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
	
	public List<TipoConta> getTipoContas(){
		return Arrays.asList(TipoConta.values());
	}
	

	/******************** Getters e Setters ***************************/

	public PlanoContaFilter getFiltro() {
		return filtro;
	}

	public PlanoConta getPlanoConta() {
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public void setFiltro(PlanoContaFilter filtro) {
		this.filtro = filtro;
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

}

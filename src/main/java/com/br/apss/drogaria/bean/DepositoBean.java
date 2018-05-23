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

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.model.Deposito;
import com.br.apss.drogaria.model.filter.DepositoFilter;
import com.br.apss.drogaria.service.DepositoService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class DepositoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Deposito deposito = new Deposito();

	private Deposito depositoSelecionado;

	private DepositoFilter filtro = new DepositoFilter();

	private List<Deposito> listaDepositos = new ArrayList<Deposito>();

	@Inject
	private DepositoService depositoService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.deposito == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Deposito depositoExistente = depositoService.porNome(deposito.getNome());
		if (depositoExistente != null && !depositoExistente.equals(deposito)) {
			throw new NegocioException("J� existe um registro com essa nome informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		depositoService.salvar(deposito);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.deposito = depositoService.porId(this.depositoSelecionado.getId());
	}

	public void novo() {
		deposito = new Deposito();
	}

	public void novoFiltro() {
		this.filtro = new DepositoFilter();
	}

	public void pesquisar() {
		this.listaDepositos = depositoService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.deposito = depositoService.porId(this.depositoSelecionado.getId());
	}

	public void excluir() {
		depositoService.excluir(depositoSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}
	
	public List<Status> getStatus(){
		return Arrays.asList(Status.values());
	}

	/******************** Getters e Setters ***************************/

	public DepositoFilter getFiltro() {
		return filtro;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public void setFiltro(DepositoFilter filtro) {
		this.filtro = filtro;
	}

	public List<Deposito> getListaDepositos() {
		return listaDepositos;
	}

	public void setListaDepositos(List<Deposito> listaDepositos) {
		this.listaDepositos = listaDepositos;
	}

	public Deposito getDepositoSelecionado() {
		return depositoSelecionado;
	}

	public void setDepositoSelecionado(Deposito depositoSelecionado) {
		this.depositoSelecionado = depositoSelecionado;
	}

}

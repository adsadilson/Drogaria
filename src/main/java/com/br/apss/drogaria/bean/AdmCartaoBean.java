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
import com.br.apss.drogaria.enums.TipoCartao;
import com.br.apss.drogaria.model.AdmCartao;
import com.br.apss.drogaria.model.filter.AdmCartaoFilter;
import com.br.apss.drogaria.service.AdmCartaoService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class AdmCartaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AdmCartao admCartao = new AdmCartao();

	private AdmCartao admCartaoSelecionado;

	private AdmCartaoFilter filtro = new AdmCartaoFilter();

	private List<AdmCartao> listaAdmCartaos = new ArrayList<AdmCartao>();

	@Inject
	private AdmCartaoService admCartaoService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.admCartao == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		AdmCartao admCartaoExistente = admCartaoService.porNome(admCartao.getNome());
		if (admCartaoExistente != null && !admCartaoExistente.equals(admCartao)) {
			throw new NegocioException("J� existe um registro com essa nome informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		admCartaoService.salvar(admCartao);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.admCartao = admCartaoService.porId(this.admCartaoSelecionado.getId());
	}

	public void novo() {
		admCartao = new AdmCartao();
	}

	public void novoFiltro() {
		this.filtro = new AdmCartaoFilter();
	}

	public void pesquisar() {
		this.listaAdmCartaos = admCartaoService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.admCartao = admCartaoService.porId(this.admCartaoSelecionado.getId());
	}

	public void excluir() {
		admCartaoService.excluir(admCartaoSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public List<Status> getStatus() {
		return Arrays.asList(Status.values());
	}

	public List<TipoCartao> getTipoCartao() {
		return Arrays.asList(TipoCartao.values());
	}

	/******************** Getters e Setters ***************************/

	public AdmCartaoFilter getFiltro() {
		return filtro;
	}

	public AdmCartao getAdmCartao() {
		return admCartao;
	}

	public void setAdmCartao(AdmCartao admCartao) {
		this.admCartao = admCartao;
	}

	public void setFiltro(AdmCartaoFilter filtro) {
		this.filtro = filtro;
	}

	public List<AdmCartao> getListaAdmCartaos() {
		return listaAdmCartaos;
	}

	public void setListaAdmCartaos(List<AdmCartao> listaAdmCartaos) {
		this.listaAdmCartaos = listaAdmCartaos;
	}

	public AdmCartao getAdmCartaoSelecionado() {
		return admCartaoSelecionado;
	}

	public void setAdmCartaoSelecionado(AdmCartao admCartaoSelecionado) {
		this.admCartaoSelecionado = admCartaoSelecionado;
	}

}

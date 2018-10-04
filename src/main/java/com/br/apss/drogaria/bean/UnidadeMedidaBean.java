package com.br.apss.drogaria.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.model.UnidadeMedida;
import com.br.apss.drogaria.model.filter.UnidadeMedidaFilter;
import com.br.apss.drogaria.relatorio.Relatorio;
import com.br.apss.drogaria.service.UnidadeMedidaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class UnidadeMedidaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private UnidadeMedida unidadeMedida = new UnidadeMedida();

	private UnidadeMedida unidadeMedidaSelecionado;

	private UnidadeMedidaFilter filtro = new UnidadeMedidaFilter();

	private List<UnidadeMedida> listaUnidadeMedidas = new ArrayList<UnidadeMedida>();

	@Inject
	private Relatorio relat;

	private List<UnidadeMedida> list = null;

	@Inject
	private UnidadeMedidaService unidadeMedidaService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.unidadeMedida == null) {
			novo();
		}
		pesquisar();
	}

	public void gerarRelatUnidadeMedida() throws IOException {
		list = unidadeMedidaService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Unidade de Medida");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_tipo", false);
		par.put("par_ordenacao", filtro.getCampoOrdenacao());
		String caminho = "/relatorios/reportUnidadeProduto.jrxml";
		relat.gerarRelatorio(caminho, "Lista de Unidade de Medida", par, list);
	}

	public void salvar() {

		UnidadeMedida unidadeMedidaExistente = unidadeMedidaService.porNome(unidadeMedida.getNome());
		if (unidadeMedidaExistente != null && !unidadeMedidaExistente.equals(unidadeMedida)) {
			throw new NegocioException("Já existe um registro com essa descrição informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		unidadeMedidaService.salvar(unidadeMedida);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.unidadeMedida = unidadeMedidaService.porId(this.unidadeMedidaSelecionado.getId());
	}

	public void novo() {
		unidadeMedida = new UnidadeMedida();
	}

	public void novoFiltro() {
		this.filtro = new UnidadeMedidaFilter();
	}

	public void pesquisar() {
		this.listaUnidadeMedidas = unidadeMedidaService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.unidadeMedida = unidadeMedidaService.porId(this.unidadeMedidaSelecionado.getId());
	}

	public void excluir() {
		unidadeMedidaService.excluir(unidadeMedidaSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public List<Status> getStatus() {
		return Arrays.asList(Status.values());
	}

	/******************** Getters e Setters ***************************/

	public UnidadeMedidaFilter getFiltro() {
		return filtro;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public void setFiltro(UnidadeMedidaFilter filtro) {
		this.filtro = filtro;
	}

	public List<UnidadeMedida> getListaUnidadeMedidas() {
		return listaUnidadeMedidas;
	}

	public void setListaUnidadeMedidas(List<UnidadeMedida> listaUnidadeMedidas) {
		this.listaUnidadeMedidas = listaUnidadeMedidas;
	}

	public UnidadeMedida getUnidadeMedidaSelecionado() {
		return unidadeMedidaSelecionado;
	}

	public void setUnidadeMedidaSelecionado(UnidadeMedida unidadeMedidaSelecionado) {
		this.unidadeMedidaSelecionado = unidadeMedidaSelecionado;
	}

	public List<UnidadeMedida> getList() {
		return list;
	}

	public void setList(List<UnidadeMedida> list) {
		this.list = list;
	}

}

package com.br.apss.drogaria.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.enums.EstadoCivil;
import com.br.apss.drogaria.enums.Sexo;
import com.br.apss.drogaria.enums.TipoPessoa;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.relatorio.Relatorio;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class FornecedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa fornecedor = new Pessoa();

	private Pessoa fornecedorSelecionado;

	private PessoaFilter filtro = new PessoaFilter();

	private LazyDataModel<Pessoa> listaFornecedors;

	private List<Pessoa> list = null;

	@Inject
	private Relatorio relat;

	@Inject
	private PessoaService fornecedorService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.fornecedor == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Pessoa fornecedorExistente = fornecedorService.porCpf(fornecedor.getCpfCnpj());
		if (fornecedorExistente != null && !fornecedorExistente.equals(fornecedor)) {
			throw new NegocioException("Já existe um Fornecedor com esse cpf ou cnpj informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		fornecedorService.salvar(fornecedor);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.fornecedor = fornecedorService.porId(this.fornecedorSelecionado.getId());
	}

	public void novo() {
		fornecedor = new Pessoa();
		fornecedor.setFornecedor(true);
	}

	public void novoFiltro() {
		this.filtro = new PessoaFilter();
	}

	public void tipoRelat() {
		this.filtro.setTipoAnalitico(true);
	}

	public void gerarRelatFor() throws IOException {
		filtro.setFornecedor(true);
		list = fornecedorService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Fornecedores");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_tipo", filtro.getTipoAnalitico());
		par.put("par_numDe", filtro.getNumeroDe() + " ate " + filtro.getNumeroAte());
		par.put("par_ordenacao", filtro.getCampoOrdenacao());

		String caminho = "/relatorios/reportCliente.jrxml";

		if (filtro.getTipoAnalitico()) {
			caminho = "/relatorios/reportFichaFornecedor.jrxml";
			par.put("par_nome_relat", "Ficha de Fornecedor(es)");
		}

		relat.gerarRelatorio(caminho, "Lista De Fornecedor", par, list);
	}

	public void gerarRelatForBloqueado() throws IOException {
		filtro.setBloqueado(true);
		filtro.setFornecedor(true);
		list = fornecedorService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Fornecedores Bloqueados");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_tipo", filtro.getTipoAnalitico());
		par.put("par_numDe", filtro.getNumeroDe() + " ate " + filtro.getNumeroAte());
		par.put("par_ordenacao", filtro.getCampoOrdenacao());
		String caminho = "/relatorios/reportCliBloqueado.jrxml";
		if (filtro.getTipoAnalitico()) {
			caminho = "/relatorios/reportCliAnalitico.jrxml";
		}
		relat.gerarRelatorio(caminho, "Lista De Fornecedores Bloqueados", par, list);
	}

	public void pesquisar() {
		listaFornecedors = new LazyDataModel<Pessoa>() {

			private static final long serialVersionUID = 1L;

			public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setCampoOrdenacao(sortField);
				filtro.setFornecedor(true);

				setRowCount(fornecedorService.quantidadeFiltrados(filtro));

				return fornecedorService.filtrados(filtro);
			}

			@Override
			public Pessoa getRowData(String rowKey) {
				Pessoa objeto = (Pessoa) fornecedorService.porId(Long.parseLong(rowKey));
				return objeto;
			}

			@Override
			public String getRowKey(Pessoa objeto) {
				return objeto.getId().toString();
			}

		};
	}

	public void preparEdicao() {
		this.fornecedor = fornecedorService.porId(this.fornecedorSelecionado.getId());
	}

	public void excluir() {
		fornecedorService.excluir(fornecedorSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public List<TipoPessoa> getlistaTipoPessoas() {
		return Arrays.asList(TipoPessoa.values());
	}

	public List<Sexo> getlistaSexos() {
		return Arrays.asList(Sexo.values());
	}

	public List<EstadoCivil> getlistaEstadoCivis() {
		return Arrays.asList(EstadoCivil.values());
	}

	public List<Estado> getlistaEstados() {
		return Arrays.asList(Estado.values());
	}

	/******************** Getters e Setters ***************************/

	public PessoaFilter getFiltro() {
		return filtro;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
	}

	public void setFiltro(PessoaFilter filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Pessoa> getListaFornecedors() {
		return listaFornecedors;
	}

	public void setListaFornecedors(LazyDataModel<Pessoa> listaFornecedors) {
		this.listaFornecedors = listaFornecedors;
	}

	public Pessoa getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Pessoa fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	public List<Pessoa> getList() {
		return list;
	}

	public void setList(List<Pessoa> list) {
		this.list = list;
	}

}

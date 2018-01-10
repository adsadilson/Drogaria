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

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.enums.EstadoCivil;
import com.br.apss.drogaria.enums.Sexo;
import com.br.apss.drogaria.enums.TipoPessoa;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class FornecedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa fornecedor = new Pessoa();

	private Pessoa fornecedorSelecionado;

	private PessoaFilter filtro = new PessoaFilter();

	private List<Pessoa> listaFornecedors = new ArrayList<Pessoa>();

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

	public void pesquisar() {
		this.filtro.setFonecedor(true);
		this.listaFornecedors = fornecedorService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.fornecedor = fornecedorService.porId(this.fornecedorSelecionado.getId());
	}

	public void excluir() {
		fornecedorService.excluir(fornecedorSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}
	
	public List<TipoPessoa> getlistaTipoPessoas(){
		return Arrays.asList(TipoPessoa.values());
	}
	
	public List<Sexo> getlistaSexos(){
		return Arrays.asList(Sexo.values());
	}
	
	public List<EstadoCivil> getlistaEstadoCivis(){
		return Arrays.asList(EstadoCivil.values());
	}
	
	public List<Estado> getlistaEstados(){
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

	public List<Pessoa> getListaFornecedors() {
		return listaFornecedors;
	}

	public void setListaFornecedors(List<Pessoa> listaFornecedors) {
		this.listaFornecedors = listaFornecedors;
	}

	public Pessoa getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Pessoa fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

}

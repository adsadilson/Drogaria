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
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa cliente = new Pessoa();

	private Pessoa clienteSelecionado;

	private PessoaFilter filtro = new PessoaFilter();

	private List<Pessoa> listaClientes = new ArrayList<Pessoa>();

	@Inject
	private PessoaService clienteService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.cliente == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Pessoa clienteExistente = clienteService.porCpf(cliente.getCpfCnpj());
		if (clienteExistente != null && !clienteExistente.equals(cliente)) {
			throw new NegocioException("Já existe um Cliente com esse cpf ou cnpj informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		clienteService.salvar(cliente);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.cliente = clienteService.porId(this.clienteSelecionado.getId());
	}

	public void novo() {
		cliente = new Pessoa();
	}

	public void novoFiltro() {
		this.filtro = new PessoaFilter();
	}

	public void pesquisar() {
		this.listaClientes = clienteService.filtrados(filtro);
	}

	public void preparEdicao() {
		this.cliente = clienteService.porId(this.clienteSelecionado.getId());
	}

	public void excluir() {
		clienteService.excluir(clienteSelecionado);
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

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public void setFiltro(PessoaFilter filtro) {
		this.filtro = filtro;
	}

	public List<Pessoa> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Pessoa> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public Pessoa getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Pessoa clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

}

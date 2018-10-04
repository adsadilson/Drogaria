package com.br.apss.drogaria.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
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
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa cliente = new Pessoa();

	private Pessoa clienteSelecionado;

	private PessoaFilter filtro = new PessoaFilter();

	private LazyDataModel<Pessoa> listaClientes;

	private int qtdRegistro = 0;

	@Inject
	private PessoaService clienteService;

	@Inject
	private Relatorio relat;

	private List<Pessoa> list = null;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.cliente == null) {
			novo();
		}
		pesquisar();
	}

	public void validarRelatorio() {
		list = clienteService.filtrados(filtro);
		if (list.size() <= 0) {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("Nenhum registro encontrado para geração do relatório.");
		}
	}

	public void gerarRelatCli() throws IOException {
		filtro.setCliente(true);
		list = clienteService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Clientes");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_tipo", filtro.getTipoAnalitico());
		par.put("par_numDe", filtro.getNumeroDe() + " ate " + filtro.getNumeroAte());
		par.put("par_ordenacao", filtro.getCampoOrdenacao());

		String caminho = "/relatorios/reportCliente.jrxml";

		if (filtro.getTipoAnalitico()) {
			caminho = "/relatorios/iReportPessoaAnalitico.jrxml";
			par.put("par_nome_relat", "Ficha de Cliente(s)");
		}

		relat.gerarRelatorio(caminho, "Lista De Clientes", par, list);
	}

	public void gerarRelatCliBloqueado() throws IOException {
		filtro.setBloqueado(true);
		filtro.setCliente(true);
		list = clienteService.filtrados(filtro);
		Map<String, Object> par = new HashMap<>();
		par.put("par_nome_relat", "Lista de Clientes Bloqueados");
		par.put("par_situacao", filtro.getStatus());
		par.put("par_tipo", filtro.getTipoAnalitico());
		par.put("par_numDe", filtro.getNumeroDe() + " ate " + filtro.getNumeroAte());
		par.put("par_ordenacao", filtro.getCampoOrdenacao());
		String caminho = "/relatorios/reportCliBloqueado.jrxml";
		if (filtro.getTipoAnalitico()) {
			caminho = "/relatorios/reportCliAnalitico.jrxml";
		}
		relat.gerarRelatorio(caminho, "Lista De Clientes Bloqueados", par, list);
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

	public void consultarClientePorCpf() {
		Pessoa clienteExistente = clienteService.porCpf(cliente.getCpfCnpj());
		if (clienteExistente != null) {
			cliente = clienteExistente;
		}
	}

	public void consultarClientePorCpfMsm() {
		Pessoa clienteExistente = clienteService.porCpf(cliente.getCpfCnpj());
		if (clienteExistente != null) {
			cliente = clienteExistente;
			throw new NegocioException("Já existe um Cliente com esse cpf ou cnpj informado.");
		}
	}

	public void editar() {
		this.cliente = clienteService.porId(this.clienteSelecionado.getId());
	}

	public void novo() {
		cliente = new Pessoa();
		cliente.setCliente(true);
	}

	public void novoFiltro() {
		this.filtro = new PessoaFilter();
	}

	public void tipoRelat() {
		this.filtro.setTipoAnalitico(true);
	}

	public void pesquisar() {

		listaClientes = new LazyDataModel<Pessoa>() {

			private static final long serialVersionUID = 1L;

			public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setCampoOrdenacao(sortField);
				filtro.setCliente(true);

				setRowCount(clienteService.quantidadeFiltrados(filtro));
				return clienteService.filtrados(filtro);
			}

			@Override
			public Pessoa getRowData(String rowKey) {
				Pessoa objeto = (Pessoa) clienteService.porId(Long.parseLong(rowKey));
				return objeto;
			}

			@Override
			public String getRowKey(Pessoa objeto) {
				return objeto.getId().toString();
			}

		};
	}

	public void preparEdicao() {
		this.cliente = clienteService.porId(this.clienteSelecionado.getId());
	}

	public void excluir() {
		clienteService.excluir(clienteSelecionado);
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

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public void setFiltro(PessoaFilter filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Pessoa> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(LazyDataModel<Pessoa> listaClientes) {
		this.listaClientes = listaClientes;
	}

	public Pessoa getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Pessoa clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public int getQtdRegistro() {
		return qtdRegistro;
	}

	public void setQtdRegistro(int qtdRegistro) {
		this.qtdRegistro = qtdRegistro;
	}

}

package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.Arrays;
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
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class FuncionarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa funcionario = new Pessoa();

	private Pessoa funcionarioSelecionado;

	private PessoaFilter filtro = new PessoaFilter();

	private LazyDataModel<Pessoa> listaFuncionarios;

	@Inject
	private PessoaService funcionarioService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.funcionario == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Pessoa funcionarioExistente = funcionarioService.porCpf(funcionario.getCpfCnpj());
		if (funcionarioExistente != null && !funcionarioExistente.equals(funcionario)) {
			throw new NegocioException("J� existe um Funcion�rio com esse cpf ou cnpj informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		funcionarioService.salvar(funcionario);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.funcionario = funcionarioService.porId(this.funcionarioSelecionado.getId());
	}

	public void novo() {
		funcionario = new Pessoa();
		funcionario.setFuncionario(true);
	}

	public void novoFiltro() {
		this.filtro = new PessoaFilter();
	}

	public void pesquisar() {

		listaFuncionarios = new LazyDataModel<Pessoa>() {

			private static final long serialVersionUID = 1L;

			public List<Pessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				filtro.setPrimeiroRegistro(first);
				filtro.setQuantidadeRegistros(pageSize);
				filtro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				filtro.setCampoOrdenacao(sortField);
				filtro.setFuncionario(true);

				setRowCount(funcionarioService.quantidadeFiltrados(filtro));

				return funcionarioService.filtrados(filtro);
			}

			@Override
			public Pessoa getRowData(String rowKey) {
				Pessoa objeto = (Pessoa) funcionarioService.porId(Long.parseLong(rowKey));
				return objeto;
			}

			@Override
			public String getRowKey(Pessoa objeto) {
				return objeto.getId().toString();
			}

		};
	}

	public void preparEdicao() {
		this.funcionario = funcionarioService.porId(this.funcionarioSelecionado.getId());
	}

	public void excluir() {
		funcionarioService.excluir(funcionarioSelecionado);
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

	public Pessoa getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Pessoa funcionario) {
		this.funcionario = funcionario;
	}

	public void setFiltro(PessoaFilter filtro) {
		this.filtro = filtro;
	}

	public LazyDataModel<Pessoa> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(LazyDataModel<Pessoa> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public Pessoa getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(Pessoa funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

}

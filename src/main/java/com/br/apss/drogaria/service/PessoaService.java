package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.repository.PessoaRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PessoaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository dao;

	@Transacional
	public Pessoa salvar(Pessoa obj) {
		if (obj.getId() == null) {
			obj.setDataCadastro(new Date());
		}
		return dao.salvar(obj);
	}

	@Transacional
	public void excluir(Pessoa obj) {
		dao.excluir(obj);
	}

	public List<Pessoa> filtrados(PessoaFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Pessoa> listarTodos() {
		return dao.listarTodos();
	}

	public List<Pessoa> listarFornecedore() {
		return dao.listarFornecedores();
	}

	public List<Pessoa> listarEmpresa() {
		return dao.listarEmpresas();
	}

	public List<Pessoa> listarClientes() {
		return dao.listarClientes();
	}

	public Pessoa porId(Long id) {
		return dao.porId(id);
	}

	public Pessoa porNome(String nome) {
		return dao.porNome(nome);
	}

	public Pessoa porCpf(String cpf) {
		return dao.porCpf(cpf);
	}

	public int quantidadeFiltrados(PessoaFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

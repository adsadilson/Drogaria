package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Permissao;
import com.br.apss.drogaria.model.filter.PermissaoFilter;
import com.br.apss.drogaria.repository.PermissaoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PermissaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PermissaoRepository dao;

	@Transacional
	public void salvar(Permissao obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Permissao obj) {
		dao.excluir(obj);
	}

	public List<Permissao> filtrados(PermissaoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Permissao> listarTodos() {
		return dao.listarTodos();
	}

	public Permissao porId(Long id) {
		return dao.porId(id);
	}

	public Permissao porNome(String nome) {
		return dao.porNome(nome);
	}
	
	public List<Permissao> buscarPermissaoPorGrupo() {
		return dao.buscarPermissaoPorGrupo();
	}

	public int quantidadeFiltrados(PermissaoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

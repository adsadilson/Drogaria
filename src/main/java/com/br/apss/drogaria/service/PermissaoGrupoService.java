package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.PermissaoGrupo;
import com.br.apss.drogaria.model.filter.PermissaoGrupoUserFilter;
import com.br.apss.drogaria.repository.PermissaoGrupoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PermissaoGrupoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PermissaoGrupoRepository dao;

	@Transacional
	public void salvar(PermissaoGrupo tarefa) {
		dao.salvar(tarefa);
	}

	@Transacional
	public void excluir(PermissaoGrupo tarefa) {
		dao.excluir(tarefa);
	}

	public List<PermissaoGrupo> filtrados(PermissaoGrupoUserFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<PermissaoGrupo> listarTodos() {
		return dao.listarTodos();
	}

	public PermissaoGrupo porId(Long id) {
		return dao.porId(id);
	}

	public PermissaoGrupo porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(PermissaoGrupoUserFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

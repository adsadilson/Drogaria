package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.UsuarioFilter;
import com.br.apss.drogaria.repository.UsuarioRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository dao;

	@Transacional
	public void salvar(Usuario obj) {
		if (null == obj.getId()) {
			obj.setCadastro(new Date());
		}
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(Usuario obj) {
		dao.excluir(obj);
	}

	public List<Usuario> filtrados(UsuarioFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Usuario> listarTodos() {
		return dao.listarTodos();
	}

	public Usuario porId(Long id) {
		return dao.porId(id);
	}

	public Usuario porNome(String nome) {
		return dao.porNome(nome);
	}
	
	public List<Usuario> completarVendedor(String nome) {
		return dao.completarVendedor(nome);
	}

	public Usuario porEmail(String email) {
		return dao.porEmail(email);
	}

	public int quantidadeFiltrados(UsuarioFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

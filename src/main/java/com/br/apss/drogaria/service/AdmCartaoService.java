package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.AdmCartao;
import com.br.apss.drogaria.model.filter.AdmCartaoFilter;
import com.br.apss.drogaria.repository.AdmCartaoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class AdmCartaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AdmCartaoRepository dao;

	@Transacional
	public void salvar(AdmCartao obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(AdmCartao obj) {
		dao.excluir(obj);
	}

	public List<AdmCartao> filtrados(AdmCartaoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<AdmCartao> listarContasPais(AdmCartao contaPaiId, TipoConta tipo, TipoRelatorio categoria) {
		return dao.listarContasPais(contaPaiId, tipo, categoria);
	}

	public List<AdmCartao> listarContasPorTipoCategorias(TipoConta tipo, TipoRelatorio categoria) {
		return dao.listarContasPorTipoCategorias(tipo, categoria);
	}

	public List<AdmCartao> listarTodos() {
		return dao.listarTodos();
	}

	public AdmCartao porId(Long id) {
		return dao.porId(id);
	}

	public AdmCartao porNome(String nome) {
		return dao.porNome(nome);
	}

	public AdmCartao porNomeTipo(String nome, TipoConta tipo) {
		return dao.porNomeTipo(nome, tipo);
	}

	public AdmCartao porMascara(String mascara) {
		return dao.porMascara(mascara);
	}

	public int quantidadeFiltrados(AdmCartaoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

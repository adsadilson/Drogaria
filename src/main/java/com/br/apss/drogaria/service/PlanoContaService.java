package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.TipoConta;
import com.br.apss.drogaria.enums.TipoRelatorio;
import com.br.apss.drogaria.model.PlanoConta;
import com.br.apss.drogaria.model.filter.PlanoContaFilter;
import com.br.apss.drogaria.repository.PlanoContaRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PlanoContaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PlanoContaRepository dao;

	@Transacional
	public void salvar(PlanoConta obj) {
		dao.salvar(obj);
	}

	@Transacional
	public void excluir(PlanoConta obj) {
		dao.excluir(obj);
	}

	public List<PlanoConta> filtrados(PlanoContaFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<PlanoConta> listarContasPais(PlanoConta contaPaiId, TipoConta tipo, TipoRelatorio categoria) {
		return dao.listarContasPais(contaPaiId, tipo, categoria);
	}

	public List<PlanoConta> listarTodos() {
		return dao.listarTodos();
	}

	public PlanoConta porId(Long id) {
		return dao.porId(id);
	}

	public PlanoConta porNome(String nome) {
		return dao.porNome(nome);
	}

	public PlanoConta porNomeTipo(String nome, TipoConta tipo) {
		return dao.porNomeTipo(nome, tipo);
	}

	public PlanoConta porMascara(String mascara) {
		return dao.porMascara(mascara);
	}

	public int quantidadeFiltrados(PlanoContaFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

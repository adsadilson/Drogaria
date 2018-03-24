package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Pagamento;
import com.br.apss.drogaria.model.filter.PagamentoFilter;
import com.br.apss.drogaria.repository.ContaAPagarRepository;
import com.br.apss.drogaria.repository.PagamentoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;

public class PagamentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoRepository dao;
	
	@Inject 
	private ContaAPagarRepository contaAPagarRepository;

	@Transacional
	public void salvar(Pagamento obj) {
		dao.salvar(obj);
	}
	
	@Transacional
	public void salvar(List<Pagamento> list) {
		dao.save(list);
	}

	@Transacional
	public void excluir(Pagamento obj) {
		dao.excluir(obj);
	}
	
	@Transacional
	public void excluirPagtoEstornaCP(Pagamento obj) {
		dao.excluir(obj);
		for (ContaAPagar c : obj.getListaContaAPagars()) {
			ContaAPagar cp = new ContaAPagar();
			cp.setId(c.getId());
			cp.setValorPago(cp.getValorApagar());
			contaAPagarRepository.baixaSimples(cp);
		}
	}
	
	public List<Pagamento> porVinculo(Long vinculo) {
		return dao.porVinculo(vinculo);
	}

	public List<Pagamento> filtrados(PagamentoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public List<Pagamento> listarTodos() {
		return dao.listarTodos();
	}

	public Pagamento porId(Long id) {
		return dao.porId(id);
	}

	public Pagamento porNome(String nome) {
		return dao.porNome(nome);
	}

	public int quantidadeFiltrados(PagamentoFilter filtro) {
		return dao.quantidadeFiltrados(filtro);
	}
}

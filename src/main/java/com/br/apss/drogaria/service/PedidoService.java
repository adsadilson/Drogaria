package com.br.apss.drogaria.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.model.Pedido;
import com.br.apss.drogaria.model.filter.PedidoFilter;
import com.br.apss.drogaria.repository.PedidoRepository;
import com.br.apss.drogaria.util.jpa.Transacional;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class PedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository dao;

	@Transacional
	public Pedido salvar(Pedido obj) {
		if (obj.getId() == null) {
			obj.setDataCriacao(new Date());
		}
		
		obj.recalcularValorTotal();
		
		if (obj.isNaoAlteravel()) {
			throw new NegocioException("Pedido n�o pode ser alterado no status "
					+ obj.getStatus().getDescricao() + ".");
		}

		if (obj.getItens().isEmpty()) {
			throw new NegocioException("O pedido deve possuir pelo menos um item.");
		}

		if (obj.isValorTotalNegativo()) {
			throw new NegocioException("Valor total do pedido não pode ser negativo.");
		}
		return dao.salvar(obj);
	}

	@Transacional
	public void excluir(Pedido obj) {
		dao.excluir(obj);
	}

	public List<Pedido> listAll() {
		return dao.listAll();
	}

	public Pedido porId(Long id) {
		return dao.porId(id);
	}

	public Pedido porNome(String nome) {
		return dao.porNome(nome);
	}

	public Pedido porNomeEstado(String nome, Estado uf) {
		return dao.porNomeEstado(nome, uf);
	}

	public List<Pedido> buscarPorEstado(Estado estado) {
		return dao.buscarPorEstado(estado);
	}

	public List<Pedido> filtrados(PedidoFilter filtro) {
		return dao.filtrados(filtro);
	}

	public int qtdeFiltrados(PedidoFilter filtro) {
		return dao.qtdeFiltrados(filtro);
	}
	
	public Pedido buscarPeidoComItens(Long id) {
		return dao.buscarPeidoComItens(id);
	}

}

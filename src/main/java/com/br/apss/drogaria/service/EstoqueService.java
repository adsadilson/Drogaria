package com.br.apss.drogaria.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.br.apss.drogaria.model.ItemPedido;
import com.br.apss.drogaria.model.Pedido;
import com.br.apss.drogaria.util.jpa.Transacional;

public class EstoqueService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService pedidoService;
	
	@Transacional
	public void baixarItensEstoque(Pedido pedido) {
		pedido = this.pedidoService.porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()) {
			item.getProduto().baixarEstoque(item.getQuantidade());
		}
	}

	@Transacional
	public void retornarItensEstoque(Pedido pedido) {
		pedido = this.pedidoService.porId(pedido.getId());
		
		for (ItemPedido item : pedido.getItens()) {
			item.getProduto().adicionarEstoque(item.getQuantidade());
		}
	}
}

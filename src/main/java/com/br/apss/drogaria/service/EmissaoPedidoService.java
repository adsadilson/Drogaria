package com.br.apss.drogaria.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.StatusPedido;
import com.br.apss.drogaria.model.Pedido;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class EmissaoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstoqueService estoqueService;
	
	@Inject
	private PedidoService pedidoService;

	public Pedido emitir(Pedido pedido) {
		pedido = this.pedidoService.salvar(pedido);
		if (pedido.isNaoEmissivel()) {
			throw new NegocioException(
					"Pedido n�o pode ser emitido com status " + pedido.getStatus().getDescricao() + ".");
		}
		this.estoqueService.baixarItensEstoque(pedido);
		pedido.setStatus(StatusPedido.EMITIDO);
		pedido = this.pedidoService.salvar(pedido);
		return pedido;
	}

}

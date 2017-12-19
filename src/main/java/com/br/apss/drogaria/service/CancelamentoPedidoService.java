package com.br.apss.drogaria.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.br.apss.drogaria.enums.StatusPedido;
import com.br.apss.drogaria.model.Pedido;
import com.br.apss.drogaria.util.jsf.NegocioException;

public class CancelamentoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService pedidoService;
	
	@Inject
	private EstoqueService estoqueService;
	
	public Pedido cancelar(Pedido pedido) {
		
		if (pedido.isNaoCancelavel()) {
			throw new NegocioException("Pedido n�o pode ser cancelado no status "
					+ pedido.getStatus().getDescricao() + ".");
		}
		
		if (pedido.isEmitido()) {
			this.estoqueService.retornarItensEstoque(pedido);
		}
		
		pedido.setStatus(StatusPedido.CANCELADO);
		
		pedido = this.pedidoService.salvar(pedido);
		
		return pedido;
	}


}

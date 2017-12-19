package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.br.apss.drogaria.enums.FormaPagamento;
import com.br.apss.drogaria.enums.StatusPedido;

@Entity
@Table(name = "pedido")
@SequenceGenerator(name = "PEDIDO_ID", sequenceName = "PEDIDO_SEQ", allocationSize = 1, initialValue = 1)
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PEDIDO_ID")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao")
	private Date dataCriacao;

	@Column(columnDefinition = "text")
	private String observacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_entrega")
	private Date dataEntrega;

	@Column(name = "valor_frete", precision = 10, scale = 2)
	private BigDecimal valorFrete = BigDecimal.ZERO;

	@Column(name = "valor_desconto", precision = 10, scale = 2)
	private BigDecimal valorDesconto = BigDecimal.ZERO;

	@Column(name = "valor_total", precision = 10, scale = 2)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StatusPedido status = StatusPedido.ORCAMENTO;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", length = 20)
	private FormaPagamento formaPagamento;

	@ManyToOne
	@JoinColumn(name = "vendedor_id")
	private Usuario vendedor;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Pessoa cliente;

	@Embedded
	private EnderecoEntrega enderecoEntrega = new EnderecoEntrega();

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemPedido> itens = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao.toUpperCase();
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	@Embedded
	public EnderecoEntrega getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	@Transient
	public boolean isNovo() {
		return getId() == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNovo();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

	@Transient
	public BigDecimal getValorSubtotal() {
		return this.getValorTotal().subtract(this.getValorFrete()).add(this.getValorDesconto());
	}

	public String getVlrSubtotal() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		String formatado = nf.format(this.getValorTotal().subtract(this.getValorFrete()).add(this.getValorDesconto()));
		return formatado;
	}

	public void recalcularValorTotal() {
		BigDecimal total = BigDecimal.ZERO;


			total = total.add(this.getValorFrete()).subtract(this.getValorDesconto());

			for (ItemPedido item : this.getItens()) {
				if (item.getProduto() != null && item.getProduto().getId() != null) {
					total = total.add(item.getValorTotal());
				}
			}

		this.setValorTotal(total);
	}

	public void adicionarItemVazio() {
		if (this.isOrcamento()) {
			Produto produto = new Produto();

			ItemPedido item = new ItemPedido();
			item.setProduto(produto);
			item.setPedido(this);

			this.getItens().add(0, item);
		}
	}

	@Transient
	public boolean isOrcamento() {
		return StatusPedido.ORCAMENTO.equals(this.getStatus());
	}

	public void removerItemVazio() {
		ItemPedido primeiroItem = this.getItens().get(0);

		if (primeiroItem != null && primeiroItem.getProduto().getId() == null) {
			this.getItens().remove(0);
		}
	}

	@Transient
	public boolean isValorTotalNegativo() {
		return this.getValorTotal().compareTo(BigDecimal.ZERO) < 0;
	}

	@Transient
	public boolean isEmitido() {
		return StatusPedido.EMITIDO.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoEmissivel() {
		return !this.isEmissivel();
	}

	@Transient
	public boolean isEmissivel() {
		return this.isExistente() && this.isOrcamento();
	}

	@Transient
	public boolean isCancelavel() {
		return this.isExistente() && !this.isCancelado();
	}

	@Transient
	private boolean isCancelado() {
		return StatusPedido.CANCELADO.equals(this.getStatus());
	}

	@Transient
	public boolean isNaoCancelavel() {
		return !this.isCancelavel();
	}

	@Transient
	public boolean isAlteravel() {
		return this.isOrcamento();
	}

	@Transient
	public boolean isNaoAlteravel() {
		return !this.isAlteravel();
	}

	@Transient
	public boolean isNaoEnviavelPorEmail() {
		return this.isNovo() || this.isCancelado();
	}

}

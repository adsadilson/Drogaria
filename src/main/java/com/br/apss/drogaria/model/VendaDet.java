package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "venda_det")
@SequenceGenerator(name = "VENDA_DET_ID", sequenceName = "VENDA_DET_SEQ", allocationSize = 1, initialValue = 1)
public class VendaDet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VENDA_DET_ID")
	private Long id;

	@Column(nullable = false, length = 3)
	private BigDecimal quantidade = new BigDecimal("1");

	@Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorUnitario = BigDecimal.ZERO;

	@Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@Column(name = "acr_desc", nullable = false, precision = 10, scale = 2)
	private BigDecimal acrDesc = BigDecimal.ZERO;

	@Column(name = "valor_total_liquido", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorTotalLiquido = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@ManyToOne
	@JoinColumn(name = "venda_cab_id", nullable = false)
	private VendaCab vendaCab;

	@Transient
	private BigDecimal totalDeItensGeral = BigDecimal.ZERO;

	private Long vinculo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public VendaCab getVendaCab() {
		return vendaCab;
	}

	public void setVendaCab(VendaCab vendaCab) {
		this.vendaCab = vendaCab;
	}

	public boolean isInclusao() {
		return getId() == null ? true : false;
	}

	public boolean isEditando() {
		return !isInclusao();
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getAcrDesc() {
		return acrDesc;
	}

	public void setAcrDesc(BigDecimal acrDesc) {
		this.acrDesc = acrDesc;
	}

	public BigDecimal getValorTotalLiquido() {
		return valorTotalLiquido;
	}

	public void setValorTotalLiquido(BigDecimal valorTotalLiquido) {
		this.valorTotalLiquido = valorTotalLiquido;
	}

	public BigDecimal getTotalDeItensGeral() {
		return totalDeItensGeral;
	}

	public void setTotalDeItensGeral(BigDecimal totalDeItensGeral) {
		this.totalDeItensGeral = totalDeItensGeral;
	}

	public Long getVinculo() {
		return vinculo;
	}

	public void setVinculo(Long vinculo) {
		this.vinculo = vinculo;
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
		VendaDet other = (VendaDet) obj;
		if (id == null && other.id == null) {
			if (other.produto.equals(this.produto) && other.getQuantidade().equals(this.getQuantidade())
					&& other.getValorTotal().equals(this.getValorTotal()))
				return true;
			else
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}

}

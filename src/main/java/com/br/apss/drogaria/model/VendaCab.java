package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Table(name = "venda_cab")
@SequenceGenerator(name = "VENDA_CAB_ID", sequenceName = "VENDA_CAB_SEQ", allocationSize = 1, initialValue = 1)
public class VendaCab implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VENDA_CAB_ID")
	private Long id;

	@Column(name = "documento")
	private String documento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data")
	private Date data;

	@Column(name = "valor_bruto", precision = 10, scale = 2)
	private BigDecimal valorBruto = BigDecimal.ZERO;

	@Column(name = "acresimo", precision = 10, scale = 2)
	private BigDecimal acr = BigDecimal.ZERO;

	@Column(name = "desconto", precision = 10, scale = 2)
	private BigDecimal desc = BigDecimal.ZERO;

	@Column(name = "valor_liquido", precision = 10, scale = 2)
	private BigDecimal valorLiquido = BigDecimal.ZERO;

	@Column(name = "valor_pago", precision = 10, scale = 2)
	private BigDecimal valorPago = BigDecimal.ZERO;

	@Transient
	private BigDecimal valorEmPerc = BigDecimal.ZERO;

	@Transient
	private BigDecimal valorTroco = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Pessoa cliente;

	@OneToMany(mappedBy = "vendaCab", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VendaDet> itens = new ArrayList<>();

	private Long vinculo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getAcr() {
		return acr;
	}

	public void setAcr(BigDecimal acr) {
		this.acr = acr;
	}

	public BigDecimal getDesc() {
		return desc;
	}

	public void setDesc(BigDecimal desc) {
		this.desc = desc;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	public BigDecimal getValorEmPerc() {
		return valorEmPerc;
	}

	public void setValorEmPerc(BigDecimal valorEmPerc) {
		this.valorEmPerc = valorEmPerc;
	}

	public BigDecimal getValorTroco() {
		return valorTroco;
	}

	public void setValorTroco(BigDecimal valorTroco) {
		this.valorTroco = valorTroco;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public List<VendaDet> getItens() {
		return itens;
	}

	public void setItens(List<VendaDet> itens) {
		this.itens = itens;
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
		VendaCab other = (VendaCab) obj;
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

}

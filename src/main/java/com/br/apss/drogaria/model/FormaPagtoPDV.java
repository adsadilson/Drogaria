package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.br.apss.drogaria.enums.TipoCartao;

@Entity
@Table(name = "forma_pagto_pdv")
@SequenceGenerator(name = "FORMA_PAGTO_PDV_ID", sequenceName = "FORMA_PAGTO_PDV_SEQ", allocationSize = 1, initialValue = 1)
public class FormaPagtoPDV implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "FORMA_PAGTO_PDV_ID")
	private Long id;

	@Column(name = "flag")
	private String flag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "vencimento")
	private Date vencimento;

	@Column(name = "valor", precision = 10, scale = 2)
	private BigDecimal valor = BigDecimal.ZERO;

	@Column(name = "nome_titular")
	private String nomeTitular;

	@Column(name = "cpf_cnpj")
	private String cpfCnpj;

	@Column(name = "doc")
	private String doc;

	@Column(name = "tipo_cartao")
	private TipoCartao tipoCartao;

	@Column(name = "parcela")
	private Integer parcela;

	@ManyToOne
	@JoinColumn(name = "cartao_id")
	private AdmCartao cartao;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	private Pessoa cliente;

	@ManyToOne
	@JoinColumn(name = "venda_cab_id", nullable = false)
	private VendaCab vendaCab;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getNomeTitular() {
		return nomeTitular;
	}

	public void setNomeTitular(String nomeTitular) {
		this.nomeTitular = nomeTitular;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public TipoCartao getTipoCartao() {
		return tipoCartao;
	}

	public void setTipoCartao(TipoCartao tipoCartao) {
		this.tipoCartao = tipoCartao;
	}

	public AdmCartao getCartao() {
		return cartao;
	}

	public void setCartao(AdmCartao cartao) {
		this.cartao = cartao;
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

	public VendaCab getVendaCab() {
		return vendaCab;
	}

	public void setVendaCab(VendaCab vendaCab) {
		this.vendaCab = vendaCab;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
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
		FormaPagtoPDV other = (FormaPagtoPDV) obj;
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

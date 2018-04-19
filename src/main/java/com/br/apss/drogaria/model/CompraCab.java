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
@Table(name = "compra_cab")
@SequenceGenerator(name = "COMPRA_CAB_ID", sequenceName = "COMPRA_CAB_SEQ", allocationSize = 1, initialValue = 1)
public class CompraCab implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "COMPRA_CAB_ID")
	private Long id;

	@Column(name = "documento")
	private String documento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao")
	private Date dataEmissao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entrada")
	private Date dataEntrada;

	@Column(columnDefinition = "text")
	private String observacao;

	@Column(name = "valor_total", precision = 10, scale = 2)
	private BigDecimal valorTotal = BigDecimal.ZERO;

	@ManyToOne
	@JoinColumn(name = "funcionario_id")
	private Usuario funcionario;

	@ManyToOne
	@JoinColumn(name = "fornecedor_id", nullable = false)
	private Pessoa fornecedor;

	@OneToMany(mappedBy = "compraCab", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CompraDet> itens = new ArrayList<>();

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

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Usuario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Usuario funcionario) {
		this.funcionario = funcionario;
	}

	public Pessoa getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Pessoa fornecedor) {
		this.fornecedor = fornecedor;
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
		CompraCab other = (CompraCab) obj;
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

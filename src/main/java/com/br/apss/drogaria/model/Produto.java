package com.br.apss.drogaria.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;

import com.br.apss.drogaria.enums.TipoProduto;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Entity
@Table(name = "produto")
@SequenceGenerator(name = "PRODUTO_ID", sequenceName = "PRODUTO_SEQ", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PRODUTO_ID")
	private Long id;

	@Column(nullable = false, length = 80)
	private String nome;

	@Column(nullable = false, length = 20, unique = true)
	private String sku;

	@Column(name = "nome_reduzido", length = 80)
	private String nomeReduzido;

	@Column(name = "codigo_barra", length = 15)
	private String codigoBarra;

	@Column(name = "tipo_produto", length = 10)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;

	@Column(name = "quantidade", precision = 12, scale = 2)
	@DecimalMin(value = "0.01", message = "O campo 'ESTOQUE' tem quer ser maior que 0")
	private BigDecimal quantidade = BigDecimal.ZERO;

	@Column(name = "qtd_minima", precision = 12, scale = 2)
	private BigDecimal qtdMinma = BigDecimal.ZERO;

	@Column(name = "qtd_maxima", precision = 12, scale = 2)
	private BigDecimal qtdMaxima = BigDecimal.ZERO;

	@Column(name = "vlr_custo", precision = 12, scale = 2)
	@DecimalMin(value = "0.01", message = "O campo 'CUSTO' tem quer ser maior que 0,00")
	private BigDecimal vlrCusto = BigDecimal.ZERO;

	@Column(name = "vlr_venda", precision = 12, scale = 2)
	@DecimalMin(value = "0.01", message = "O campo 'VENDA' tem quer ser maior que 0,00")
	private BigDecimal vlrVenda = BigDecimal.ZERO;;

	@DecimalMin(value = "0.01", message = "O campo 'MARG LUCRO' não pode ser negativo e nem menor que 0,00")
	@Column(name = "marg_lucro", precision = 12, scale = 2)
	private BigDecimal margLucro = BigDecimal.ZERO;

	@Column(name = "lucro", precision = 12, scale = 2)
	private BigDecimal lucro = BigDecimal.ZERO;

	@Column(name = "data_ultima_venda")
	private Date dtUltimaVenda;

	@ManyToOne
	@JoinColumn(name = "unidade_medida_id")
	private UnidadeMedida unidade;

	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@ManyToOne
	@JoinColumn(name = "subcategoria_id", nullable = false)
	private SubCategoria subCategoria;

	private Boolean status = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku.toUpperCase();
	}

	public String getNomeReduzido() {
		return nomeReduzido;
	}

	public void setNomeReduzido(String nomeReduzido) {
		this.nomeReduzido = nomeReduzido;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getQtdMinma() {
		return qtdMinma;
	}

	public void setQtdMinma(BigDecimal qtdMinma) {
		this.qtdMinma = qtdMinma;
	}

	public BigDecimal getQtdMaxima() {
		return qtdMaxima;
	}

	public void setQtdMaxima(BigDecimal qtdMaxima) {
		this.qtdMaxima = qtdMaxima;
	}

	public BigDecimal getVlrCusto() {
		return vlrCusto;
	}

	public void setVlrCusto(BigDecimal vlrCusto) {
		this.vlrCusto = vlrCusto;
	}

	public BigDecimal getVlrVenda() {
		return vlrVenda;
	}

	public void setVlrVenda(BigDecimal vlrVenda) {
		this.vlrVenda = vlrVenda;
	}

	public BigDecimal getMargLucro() {
		return margLucro;
	}

	public void setMargLucro(BigDecimal margLucro) {
		this.margLucro = margLucro;
	}

	public BigDecimal getLucro() {
		return lucro;
	}

	public void setLucro(BigDecimal lucro) {
		this.lucro = lucro;
	}

	public UnidadeMedida getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeMedida unidade) {
		this.unidade = unidade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getDtUltimaVenda() {
		return dtUltimaVenda;
	}

	public void setDtUltimaVenda(Date dtUltimaVenda) {
		this.dtUltimaVenda = dtUltimaVenda;
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
		Produto other = (Produto) obj;
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

	public void baixarEstoque(BigDecimal quantidade) {
		BigDecimal novaQuantidade = this.getQuantidade().subtract(quantidade);

		if (novaQuantidade.compareTo(BigDecimal.ZERO) < 0) {
			throw new NegocioException("N�o h� disponibilidade no estoque de " + quantidade + " itens do produto "
					+ this.getNome() + ".");
		}
		
		this.setDtUltimaVenda(new Date());
		this.setQuantidade(novaQuantidade);

	}

	public void adicionarEstoque(BigDecimal quantidade) {
		this.setQuantidade(this.getQuantidade().add(quantidade));
	}

}

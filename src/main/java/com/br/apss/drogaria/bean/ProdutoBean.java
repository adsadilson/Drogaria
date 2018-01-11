package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import com.br.apss.drogaria.enums.TipoProduto;
import com.br.apss.drogaria.model.Categoria;
import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.SubCategoria;
import com.br.apss.drogaria.model.UnidadeMedida;
import com.br.apss.drogaria.model.filter.ProdutoFilter;
import com.br.apss.drogaria.service.CategoriaService;
import com.br.apss.drogaria.service.ProdutoService;
import com.br.apss.drogaria.service.SubCategoriaService;
import com.br.apss.drogaria.service.UnidadeMedidaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class ProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Produto produto = new Produto();

	private Produto produtoSelecionado;

	private ProdutoFilter filtro = new ProdutoFilter();

	private List<Produto> listaProdutos = new ArrayList<Produto>();

	private List<SubCategoria> listaSubCategorias = new ArrayList<SubCategoria>();

	private List<UnidadeMedida> listaUnidadeMedidas = new ArrayList<UnidadeMedida>();

	private List<Categoria> listaCategorias = new ArrayList<Categoria>();

	@Inject
	private ProdutoService produtoService;

	@Inject
	private CategoriaService categoriaService;

	@Inject
	private SubCategoriaService subCategoriaService;

	@Inject
	private UnidadeMedidaService uMedidaService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.produto == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Produto produtoExistente = produtoService.porNome(produto.getNome());
		if (produtoExistente != null && !produtoExistente.equals(produto)) {
			throw new NegocioException("Já existe um registro com essa codigo de barra informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		produtoService.salvar(produto);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.produto = produtoService.porId(this.produtoSelecionado.getId());
		carregarListaCategorias();
		carregarListaUnidadeMedidas();
		listarSubCategoriasFiltrada();
	}

	public void novo() {
		produto = new Produto();
		carregarListaCategorias();
		carregarListaUnidadeMedidas();
	}

	public void novoFiltro() {
		this.filtro = new ProdutoFilter();
	}

	public void pesquisar() {
		this.listaProdutos = produtoService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.produto = produtoService.porId(this.produtoSelecionado.getId());
	}

	public void excluir() {
		produtoService.excluir(produtoSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}

	public void carregarListaCategorias() {
		this.listaCategorias = categoriaService.filtrados(null);
	}

	public void carregarListaUnidadeMedidas() {
		this.listaUnidadeMedidas = uMedidaService.listarTodos();
	}

	public List<TipoProduto> getTipos() {
		return Arrays.asList(TipoProduto.values());
	}

	public void listarSubCategoriasFiltrada() {
		if (null != produto.getCategoria()) {
			this.listaSubCategorias = subCategoriaService.porCategoria(produto.getCategoria());
		} else {
			this.listaSubCategorias = new ArrayList<SubCategoria>();
		}
	}

	public void calcMargem() {
		BigDecimal n = produto.getVlrCusto();
		BigDecimal n3 = BigDecimal.ZERO;
		BigDecimal n2 = produto.getMargLucro();
		if (produto.getVlrCusto().signum() > 0 && produto.getMargLucro().signum() > 0) {
			n3 = (n.multiply(n2)).divide(new BigDecimal(100)).add(n);
			produto.setVlrVenda(n3);
			calcLucro();
		} else {
			produto.setVlrVenda(BigDecimal.ZERO);
		}
	}

	public void calcVenda() {
		BigDecimal n = produto.getVlrCusto();
		BigDecimal n3 = BigDecimal.ZERO;
		BigDecimal n2 = produto.getVlrVenda();
		if (n.signum() > 0 && n2.signum() > 0) {
			n3 = (n2.subtract(n)).divide(n, MathContext.DECIMAL128).multiply(new BigDecimal(100));
			produto.setMargLucro(n3);
			calcLucro();
		} else {
			produto.setMargLucro(BigDecimal.ZERO);
		}
	}

	public void calcLucro() {
		BigDecimal n = produto.getVlrCusto();
		BigDecimal n3 = BigDecimal.ZERO;
		BigDecimal n2 = produto.getVlrVenda();
		if (n.signum() > 0 && n2.signum() > 0) {
			n3 = n2.subtract(n);
			produto.setLucro(n3);
		} else {
			produto.setLucro(BigDecimal.ZERO);
		}
	}

	/******************** Getters e Setters ***************************/

	public ProdutoFilter getFiltro() {
		return filtro;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setFiltro(ProdutoFilter filtro) {
		this.filtro = filtro;
	}

	public List<Produto> getListaProdutos() {
		return listaProdutos;
	}

	public void setListaProdutos(List<Produto> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public List<SubCategoria> getListaSubCategorias() {
		return listaSubCategorias;
	}

	public void setListaSubCategorias(List<SubCategoria> listaSubCategorias) {
		this.listaSubCategorias = listaSubCategorias;
	}

	public List<UnidadeMedida> getListaUnidadeMedidas() {
		return listaUnidadeMedidas;
	}

	public void setListaUnidadeMedidas(List<UnidadeMedida> listaUnidadeMedidas) {
		this.listaUnidadeMedidas = listaUnidadeMedidas;
	}

	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}

	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

}

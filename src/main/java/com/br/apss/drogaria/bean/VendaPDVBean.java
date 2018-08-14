package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.br.apss.drogaria.enums.TipoCartao;
import com.br.apss.drogaria.model.AdmCartao;
import com.br.apss.drogaria.model.FormaPagtoPDV;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.VendaCab;
import com.br.apss.drogaria.model.VendaDet;
import com.br.apss.drogaria.model.filter.AdmCartaoFilter;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.service.AdmCartaoService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.ProdutoService;
import com.br.apss.drogaria.util.jsf.NegocioException;
import com.br.apss.drogaria.validadors.ValidarCPFCNPJ;

@Named
@ViewScoped
public class VendaPDVBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/********** Atributos **********/
	private VendaCab vendaCab;

	private VendaDet vendaDet;

	private List<VendaDet> listaDeItensVendidos = new ArrayList<VendaDet>();

	private Produto produto;

	private FormaPagtoPDV formaPagto = new FormaPagtoPDV();;

	private List<FormaPagtoPDV> listaDePagamentos = new ArrayList<FormaPagtoPDV>();

	private List<AdmCartao> listaDeCartoes = new ArrayList<AdmCartao>();

	private Map<String, Integer> parcs = new HashMap<String, Integer>();

	private boolean flagPesquisar = true;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private AdmCartaoService admCartaoService;

	@Inject
	private PessoaService clienteService;

	/********** Metodos **********/
	// Adicionar item vendido
	public void addItem() {
		vendaDet.setVendaCab(vendaCab);
		vendaDet.setProduto(produto);
		listaDeItensVendidos.add(0, vendaDet);
		vendaDet = new VendaDet();
		produto = new Produto();
		flagPesquisar = true;
		calculoFechamento();
	}

	// Remover item vendido
	public void removerItem() {
		listaDeItensVendidos.remove(vendaDet);
		vendaDet = new VendaDet();
		// Verificar se a lista esta vazia se tive limpar as parcelas a pagar
		if (this.listaDeItensVendidos.size() == 0) {
			vendaCab.setValorBruto(BigDecimal.ZERO);
			vendaCab.setValorEmPerc(BigDecimal.ZERO);
			vendaCab.setDesc(BigDecimal.ZERO);
			vendaCab.setValorLiquido(BigDecimal.ZERO);
			vendaCab.setValorPago(BigDecimal.ZERO);
			vendaCab.setValorTroco(BigDecimal.ZERO);
			/*
			 * this.listaParcelas.clear();
			 * this.parcela.setTotalGeralDeParcelas(recalcularParcela());
			 */
		}
		calculoFechamento();
	}

	public void hotkeyView() {
		flagPesquisar = false;
	}

	// Iniciar uma nova venda
	public void iniciarNovaVenda() {
		vendaCab = new VendaCab();
		vendaDet = new VendaDet();
		produto = new Produto();
	}

	// Busca produto por codigo ou nome
	public List<Produto> completarProduto(String nome) {
		List<Produto> p = this.produtoService.buscarPorCodigoNome(nome);
		if (p != null) {
			return p;
		}
		return null;
	}

	// Busca cliente por nome
	public List<Pessoa> completarCliente(String nome) {
		PessoaFilter filtroCliente = new PessoaFilter();
		filtroCliente.setCliente(true);
		filtroCliente.setStatus(true);
		filtroCliente.setNome(nome);
		List<Pessoa> c = this.clienteService.filtrados(filtroCliente);
		if (c != null) {
			return c;
		}
		return null;
	}

	// Seta os dados do produto pesquisado
	public void setaProdutoPesquisado() {
		if (null != produto) {
			vendaDet.setValorUnitario(produto.getVlrVenda());
			calcSubTotal();
		}
	}

	// Buscar Produto pelo codigo de barra
	public void buscarCodigoBarra() {
		produto = produtoService.porCodigoBarra(produto.getCodigoBarra());
		vendaDet.setValorUnitario(produto.getVlrVenda());
		calcSubTotal();
		addItem();
	}

	// Calcular o sub-total com desconto ou acrescimo
	public void calcSubTotalComDescAcre() {
		if (!vendaDet.getQuantidade().equals(null) && vendaDet.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal vlrUnit = vendaDet.getValorTotal().divide(vendaDet.getQuantidade());
			vendaDet.setAcrDesc(vlrUnit.subtract(vendaDet.getValorUnitario()));
			vendaDet.setValorUnitario(vlrUnit);
		}
	}

	// Calcular subTotal do item
	public void calcSubTotal() {
		BigDecimal result = BigDecimal.ZERO;
		if (!vendaDet.getQuantidade().equals(null) && vendaDet.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
			result = (vendaDet.getQuantidade().multiply(vendaDet.getValorUnitario()));
			vendaDet.setValorTotal(result);
			vendaDet.setValorUnitario(vendaDet.getValorTotal().divide(vendaDet.getQuantidade()));
		}
	}

	// Calcular total dos itens vendidos
	public BigDecimal calcularTotalItens() {
		BigDecimal t = BigDecimal.ZERO;
		for (VendaDet item : listaDeItensVendidos) {
			t = t.add(item.getValorTotal());
		}
		return t;
	}

	// Calcular desconto
	public void calcDesc() {
		BigDecimal n = produto.getVlrCusto();
		BigDecimal n3 = BigDecimal.ZERO;
		BigDecimal n2 = produto.getMargLucro();
		if (produto.getVlrCusto().signum() > 0 && produto.getMargLucro().signum() > 0) {
			n3 = (n.multiply(n2)).divide(new BigDecimal(100)).add(n);
			produto.setVlrVenda(n3);
		} else {
			produto.setVlrVenda(BigDecimal.ZERO);
		}
	}

	// Calcular desconto em real
	public void calculoDescReal() {
		if (calcularTotalItens().compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal vlrBruto = vendaCab.getValorBruto();
			BigDecimal vlrPerc = BigDecimal.ZERO;
			vlrPerc = (vendaCab.getDesc().divide(vlrBruto, MathContext.DECIMAL128)).multiply(new BigDecimal(100))
					.setScale(3, RoundingMode.HALF_EVEN);
			vendaCab.setValorEmPerc(vlrPerc);
			calculoFechamento();
		}
	}

	// Calcular desconto em percentual
	public void calculoDescPerc() {
		BigDecimal vlrBruto = vendaCab.getValorBruto();
		vendaCab.setDesc((vlrBruto.multiply(vendaCab.getValorEmPerc()).divide(new BigDecimal(100))));
		calculoFechamento();
	}

	// Calcular do fechamento
	public void calculoFechamento() {
		vendaCab.setValorBruto(calcularTotalItens());
		vendaCab.setValorLiquido(vendaCab.getValorBruto().subtract(vendaCab.getDesc()));
		vendaCab.setValorTroco(vendaCab.getValorPago().subtract(vendaCab.getValorLiquido()));
	}

	// Listar de tipo cartao
	public List<TipoCartao> getTipoCartao() {
		return Arrays.asList(TipoCartao.values());
	}

	// Listar cartoes pelo tipo do cartao CR/CD
	public void listarCartoes() {
		AdmCartaoFilter filtroCartao = new AdmCartaoFilter();
		listaDeCartoes.clear();
		if (null != formaPagto.getTipoCartao()) {
			filtroCartao.setStatus(true);
			filtroCartao.setTipoCartao(formaPagto.getTipoCartao());
			listaDeCartoes = this.admCartaoService.filtrados(filtroCartao);
		}
	}

	// Iniciar o novo pagamento
	public void novoPagamento() {
		formaPagto = new FormaPagtoPDV();
		parcs.clear();
	}

	// Adicionar forma de pagamento cartao
	public void addCartao() {
		System.out.println(formaPagto.getCartao());
		System.out.println(formaPagto.getParcela() + 1);
	}

	// Consultar cpf ou cnpj do cheque
	public void consultarCpfCnpj() {

		if (!ValidarCPFCNPJ.validar(formaPagto.getCpfCnpj())) {
			formaPagto.setNomeTitular("");
			throw new NegocioException("CPF/CNPJ invalido!");
		}

		Pessoa clienteExistente = clienteService.porCpf(formaPagto.getCpfCnpj().trim());
		if (clienteExistente != null) {
			formaPagto.setNomeTitular(clienteExistente.getNome());
		}
	}

	// Listar parcela(s) referente ao cartao selecionado
	public void listParcelas() {
		if (null != formaPagto.getCartao()) {
			Map<String, Integer> unsortMap = new HashMap<String, Integer>();
			unsortMap.clear();
			for (int i = 0; i < formaPagto.getCartao().getParcela(); i++) {
				unsortMap.put((i + 1) + " PARCELA", i);
			}
			parcs = sortByValue(unsortMap);
			// printMap(parcs);
		}
	}

	private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

		// 1. Converter Map em List of Map
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static <K, V> void printMap(Map<K, V> map) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}
	}

	/********** Getters e Setters **********/

	public VendaCab getVendaCab() {
		return vendaCab;
	}

	public void setVendaCab(VendaCab vendaCab) {
		this.vendaCab = vendaCab;
	}

	public VendaDet getVendaDet() {
		return vendaDet;
	}

	public void setVendaDet(VendaDet vendaDet) {
		this.vendaDet = vendaDet;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<VendaDet> getListaDeItensVendidos() {
		return listaDeItensVendidos;
	}

	public void setListaDeItensVendidos(List<VendaDet> listaDeItensVendidos) {
		this.listaDeItensVendidos = listaDeItensVendidos;
	}

	public FormaPagtoPDV getFormaPagto() {
		return formaPagto;
	}

	public void setFormaPagto(FormaPagtoPDV formaPagto) {
		this.formaPagto = formaPagto;
	}

	public List<FormaPagtoPDV> getListaDePagamentos() {
		return listaDePagamentos;
	}

	public void setListaDePagamentos(List<FormaPagtoPDV> listaDePagamentos) {
		this.listaDePagamentos = listaDePagamentos;
	}

	public List<AdmCartao> getListaDeCartoes() {
		return listaDeCartoes;
	}

	public void setListaDeCartoes(List<AdmCartao> listaDeCartoes) {
		this.listaDeCartoes = listaDeCartoes;
	}

	public Map<String, Integer> getParcs() {
		return parcs;
	}

	public void setParcs(Map<String, Integer> parcs) {
		this.parcs = parcs;
	}

	public boolean isFlagPesquisar() {
		return flagPesquisar;
	}

	public void setFlagPesquisar(boolean flagPesquisar) {
		this.flagPesquisar = flagPesquisar;
	}

}

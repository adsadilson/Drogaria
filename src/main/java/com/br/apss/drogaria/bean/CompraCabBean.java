package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.CompraCab;
import com.br.apss.drogaria.model.CompraDet;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.CompraCabFilter;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.ProdutoService;

@Named
@ViewScoped
public class CompraCabBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private CompraCab compraCab;

	private CompraDet compraDet;

	private List<Pessoa> listaDeFornecedores = new ArrayList<Pessoa>();

	private List<CompraDet> listaDeItens = new ArrayList<CompraDet>();

	private CompraCabFilter filtro;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private ProdutoService produtoService;

	@PostConstruct
	public void inicializar() {
		filtro = new CompraCabFilter();
		carregarListaDeFornecedores();
	}

	private void carregarListaDeFornecedores() {
		this.listaDeFornecedores = pessoaService.listarFornecedore();
	}

	public void enderecoFornecedor() {
		String end = null;
		Pessoa p = new Pessoa();
		p = pessoaService.porId(this.compraCab.getFornecedor().getId());
		end = "Cpf/Cnpj: " + p.getCpfCnpj() + "\n" + p.getEndereco() + ", " + p.getNum() + "\n" + p.getBairro() + ", "
				+ p.getCep() + "\n" + p.getCidade() + " - " + p.getEstado();
		this.compraCab.getFornecedor().setEndereco(end);
	}

	public void novo() {
		this.compraCab = new CompraCab();
		this.compraDet = new CompraDet();
		this.compraCab.setDataEmissao(new Date());
		this.compraCab.setDataEntrada(new Date());
		this.compraCab.setUsuario(obterUsuario());
	}

	private Usuario obterUsuario() {
		HttpSession session = ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false));
		Usuario usuario = null;
		if (session != null) {
			usuario = (Usuario) session.getAttribute("usuarioAutenticado");
		}
		return usuario;
	}

	public void calcDiferenca() {
		BigDecimal n = this.compraCab.getValorNota();
		BigDecimal n3 = BigDecimal.ZERO;
		BigDecimal n4 = BigDecimal.ZERO;
		BigDecimal n2 = this.compraCab.getValorItens();

		if (n.compareTo(BigDecimal.ZERO) > 0 && n2.compareTo(BigDecimal.ZERO) > 0) {
			n3 = n2.subtract(n);
			this.compraCab.setValorDif(n3);
			n4 = (n3.divide(n, MathContext.DECIMAL128)).multiply(new BigDecimal(100)).setScale(3,
					RoundingMode.HALF_EVEN);
			this.compraCab.setVlrEmPerc(n4);
		}

	}

	public void calcValorCusto() {
		BigDecimal perc = this.compraCab.getVlrEmPerc();
		BigDecimal soma = BigDecimal.ZERO;
		BigDecimal dif = BigDecimal.ZERO;
		BigDecimal resultado = BigDecimal.ZERO;

		if (!compraDet.getQuantidade().equals(null) && compraDet.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
			if (!perc.equals(BigDecimal.ZERO)) {
				dif = (compraDet.getValorTotal().multiply(perc)).divide(new BigDecimal(100));
				soma = dif.add(compraDet.getValorTotal());
				compraDet.setValorTotalLiquido(soma);
				resultado = soma.divide(compraDet.getQuantidade(), 2, RoundingMode.HALF_UP);
				compraDet.setValorUnitario(resultado);
				compraDet.setValorDif(dif);
			}
		}
	}

	public List<Produto> completarProduto(String nome) {
		return this.produtoService.buscarPorCodigoNome(nome);
	}

	public void addItem() {
			this.listaDeItens.add(0, this.compraDet);
			this.compraDet = new CompraDet();
			this.compraDet.setTotalDeItensGeral(calcularTotalItens());
	}

	public void removerItem() {
		this.listaDeItens.remove(this.compraDet);
		this.compraDet = new CompraDet();
	}

	public BigDecimal calcularTotalItens() {
		BigDecimal t = BigDecimal.ZERO;
		for (CompraDet item : listaDeItens) {
			t = t.add(item.getValorTotalLiquido());
		}
		return t;
	}

	/********* Gett e Sett ************/

	public CompraCab getCompraCab() {
		return compraCab;
	}

	public void setCompraCab(CompraCab compraCab) {
		this.compraCab = compraCab;
	}

	public CompraCabFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(CompraCabFilter filtro) {
		this.filtro = filtro;
	}

	public List<Pessoa> getListaDeFornecedores() {
		return listaDeFornecedores;
	}

	public void setListaDeFornecedores(List<Pessoa> listaDeFornecedores) {
		this.listaDeFornecedores = listaDeFornecedores;
	}

	public CompraDet getCompraDet() {
		return compraDet;
	}

	public void setCompraDet(CompraDet compraDet) {
		this.compraDet = compraDet;
	}

	public List<CompraDet> getListaDeItens() {
		return listaDeItens;
	}

	public void setListaDeItens(List<CompraDet> listaDeItens) {
		this.listaDeItens = listaDeItens;
	}

}

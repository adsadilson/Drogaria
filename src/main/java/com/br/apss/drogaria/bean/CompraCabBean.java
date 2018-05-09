package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Messages;

import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.model.CabContaApagar;
import com.br.apss.drogaria.model.CompraCab;
import com.br.apss.drogaria.model.CompraDet;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.CompraCabFilter;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.ProdutoService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class CompraCabBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private CompraCab compraCab;

	private CompraDet compraDet;

	private CompraDet compraDetSelecionado;

	private List<Pessoa> listaDeFornecedores = new ArrayList<Pessoa>();

	private List<CompraDet> listaDeItens = new ArrayList<CompraDet>();

	private CompraCabFilter filtro;

	private ContaAPagar parcela;

	private ContaAPagar parcelaEditar;

	private List<ContaAPagar> listaParcelas;

	private CabContaApagar cabContaApagar;

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
		this.parcela = new ContaAPagar();
		this.listaParcelas = new ArrayList<ContaAPagar>();
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
			n3 = n.subtract(n2);
			this.compraCab.setValorDif(n3);
			n4 = (n3.divide(n2, MathContext.DECIMAL128)).multiply(new BigDecimal(100)).setScale(3,
					RoundingMode.HALF_EVEN);
			this.compraCab.setVlrEmPerc(n4);
		}

		this.parcela.setValor(n);

	}

	public void calcValorCusto() {
		BigDecimal perc = this.compraCab.getVlrEmPerc();
		BigDecimal soma = BigDecimal.ZERO;
		BigDecimal dif = BigDecimal.ZERO;
		BigDecimal resultado = BigDecimal.ZERO;

		if (!compraDet.getQuantidade().equals(null) && compraDet.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
			if (!perc.equals(BigDecimal.ZERO)) {
				dif = (compraDet.getValorTotal().multiply(perc)).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
				soma = dif.add(compraDet.getValorTotal()).setScale(2, RoundingMode.HALF_UP);
				compraDet.setValorTotalLiquido(soma);
				resultado = soma.divide(compraDet.getQuantidade(), 2, RoundingMode.HALF_UP);
				compraDet.setValorUnitario(resultado);
				compraDet.setValorDif(dif);
			}
		}
	}

	public List<Produto> completarProduto(String nome) {
		List<Produto> p = this.produtoService.buscarPorCodigoNome(nome);
		if (p != null) {
			return p;
		}
		return null;
	}

	public void limparCampo() {
		if (null == this.compraDet.getProduto()) {
			this.compraDet = new CompraDet();
		}
	}

	public void addItem() {
		if (calcularTotalItens().add(this.compraDet.getValorTotalLiquido())
				.compareTo(this.compraCab.getValorNota()) > 0) {
			Messages.addGlobalInfo("Valor do item maior que o da nota");
		} else {
			this.listaDeItens.add(0, this.compraDet);
			this.compraDet = new CompraDet();
			this.compraDet.setTotalDeItensGeral(calcularTotalItens());
		}
	}

	public void atualizarItem() {
		if (calcularTotalItens().add(this.compraDetSelecionado.getValorTotal())
				.compareTo(this.compraCab.getValorNota()) > 0) {
			FacesContext.getCurrentInstance().validationFailed();
			Messages.addGlobalInfo("Valor do item maior que o da nota");
		} else {
			this.compraDet = new CompraDet();
			this.compraDet.setTotalDeItensGeral(calcularTotalItens());
		}
	}

	public void removerItem() {
		this.listaDeItens.remove(this.compraDet);
		this.compraDet = new CompraDet();
		this.compraDet.setTotalDeItensGeral(calcularTotalItens());
	}

	public void abrirEdicao() {
		this.compraDetSelecionado = this.compraDet;
	}

	public void abrirEdicaoFatura() {
		this.parcelaEditar = new ContaAPagar();
		this.parcelaEditar.setParcela(this.parcela.getParcela());
		this.parcelaEditar.setDataVencto(this.parcela.getDataVencto());
		this.parcelaEditar.setNumDoc(this.parcela.getNumDoc());
		this.parcelaEditar.setValor(this.parcela.getValor());
	}

	public void salvarEdicaoParcela() {

		BigDecimal recalculo = BigDecimal.ZERO;

		if (!validarDatas(this.compraCab.getDataEntrada(), this.parcelaEditar.getDataVencto())) {

			for (ContaAPagar pp : this.listaParcelas) {
				if (pp.getParcela().equals(this.parcelaEditar.getParcela())) {
					pp.setDataVencto(this.parcelaEditar.getDataVencto());
					pp.setNumDoc(this.parcelaEditar.getNumDoc());
					pp.setValor(this.parcelaEditar.getValor());
				}
				recalculo = recalculo.add(pp.getValor());
			}
			this.parcela.setTotalGeralDeParcelas(recalculo);
		} else {
			FacesContext.getCurrentInstance().validationFailed();
			throw new NegocioException("A data de vencimento dever ser maior que a data de entrada!");
		}
	}

	public void recalcularParcela() {
		BigDecimal r = BigDecimal.ZERO;
		for (ContaAPagar par : this.listaParcelas) {
			r = r.add(par.getValor());
		}
		this.parcela.setTotalGeralDeParcelas(r);
	}

	public Boolean validarDatas(Date ini, Date fim) {
		if (ini != null && fim != null) {
			if (fim.before(ini)) {
				return true;
			}
		}
		return false;
	}

	public BigDecimal calcularTotalItens() {
		BigDecimal t = BigDecimal.ZERO;
		for (CompraDet item : listaDeItens) {
			t = t.add(item.getValorTotalLiquido());
		}
		return t;
	}

	public List<TipoCobranca> getListaTipoCobrancas() {
		return Arrays.asList(TipoCobranca.values());
	}

	public void gerarParcelas() {

		BigDecimal qtde_parcela = new BigDecimal(this.parcela.getNumVezes());
		BigDecimal valorParcela = this.parcela.getValor().divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = this.parcela.getValor().subtract(valorParcial);

		this.listaParcelas.clear();
		for (int i = 0; i < this.parcela.getNumVezes(); i++) {
			ContaAPagar ap = new ContaAPagar();
			ap.setTipoCobranca(this.parcela.getTipoCobranca());
			ap.setParcela((i + 1) + "/" + this.parcela.getNumVezes());
			ap.setNumDoc(this.compraCab.getDocumento());
			ap.setDataVencto(i == 0 ? somaDias(this.compraCab.getDataEntrada(), 30)
					: somaDias(this.compraCab.getDataEntrada(), this.parcela.getPeriodo() * (i + 1)));
			ap.setValor(i == 0 ? primeiraParcela : valorParcela);
			this.listaParcelas.add(ap);
		}
		this.parcela.setTotalGeralDeParcelas(this.parcela.getValor());
	}

	public Date somaDias(Date data, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
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

	public CompraDet getCompraDetSelecionado() {
		return compraDetSelecionado;
	}

	public void setCompraDetSelecionado(CompraDet compraDetSelecionado) {
		this.compraDetSelecionado = compraDetSelecionado;
	}

	public ContaAPagar getParcela() {
		return parcela;
	}

	public void setParcela(ContaAPagar parcela) {
		this.parcela = parcela;
	}

	public List<ContaAPagar> getListaParcelas() {
		return listaParcelas;
	}

	public void setListaParcelas(List<ContaAPagar> listaParcelas) {
		this.listaParcelas = listaParcelas;
	}

	public CabContaApagar getCabContaApagar() {
		return cabContaApagar;
	}

	public void setCabContaApagar(CabContaApagar cabContaApagar) {
		this.cabContaApagar = cabContaApagar;
	}

	public ContaAPagar getParcelaEditar() {
		return parcelaEditar;
	}

	public void setParcelaEditar(ContaAPagar parcelaEditar) {
		this.parcelaEditar = parcelaEditar;
	}

}

package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;

import com.br.apss.drogaria.enums.TipoCobranca;
import com.br.apss.drogaria.model.CabContaApagar;
import com.br.apss.drogaria.model.CompraCab;
import com.br.apss.drogaria.model.CompraDet;
import com.br.apss.drogaria.model.ContaAPagar;
import com.br.apss.drogaria.model.Deposito;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.Produto;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.CompraCabFilter;
import com.br.apss.drogaria.service.CabContaApagarService;
import com.br.apss.drogaria.service.CompraCabService;
import com.br.apss.drogaria.service.ContaAPagarService;
import com.br.apss.drogaria.service.DepositoService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.service.ProdutoService;
import com.br.apss.drogaria.util.jpa.GeradorVinculo;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class CompraCabBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private CompraCab compraCab;

	private CompraDet compraDet;

	private CompraDet compraDetSelecionado;

	private CompraCab compraCabSelecionado;

	private List<CompraCab> listaDeCompraCab = new ArrayList<CompraCab>();

	private List<CompraCab> listaDeCompraCabSelecionadas = new ArrayList<CompraCab>();

	private List<Pessoa> listaDeFornecedores = new ArrayList<Pessoa>();

	private List<Deposito> listaDeDepositos = new ArrayList<Deposito>();

	private List<CompraDet> listaDeItens = new ArrayList<CompraDet>();

	private CompraCabFilter filtro;

	private ContaAPagar parcela;

	private ContaAPagar parcelaEditar;

	private List<ContaAPagar> listaParcelas;

	private CabContaApagar cabContaApagar;

	@Inject
	private GeradorVinculo gerarVinculo;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private CompraCabService compraCabService;

	@Inject
	private CabContaApagarService cabContaApagarService;

	@Inject
	private DepositoService depositoService;

	@Inject
	private ContaAPagarService contaAPagarService;

	private BigDecimal totalAParcelar = BigDecimal.ZERO;

	private String edicao = "S";

	private boolean isToggle = false;

	@PostConstruct
	public void inicializar() {
		filtro = new CompraCabFilter();
		carregarListaDeFornecedores();
		carregarListaDeDepositos();
	}

	public void pesquisar() {
		this.listaDeCompraCab.clear();
		this.listaDeCompraCab = compraCabService.filtrados(this.filtro);
		this.filtro = new CompraCabFilter();
	}

	private void carregarListaDeFornecedores() {
		this.listaDeFornecedores = pessoaService.listarFornecedore();
	}
	
	private void carregarListaDeDepositos() {
		this.listaDeDepositos = depositoService.listarTodos();
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
		this.cabContaApagar = new CabContaApagar();
		this.compraCab.setDataEmissao(new Date());
		this.compraCab.setDataEntrada(new Date());
		this.compraCab.setUsuario(obterUsuario());
		this.compraCab.setVinculo(gerarVinculo.gerar(CompraCab.class));
		this.parcela = new ContaAPagar();
		this.listaDeItens = new ArrayList<CompraDet>();
		this.listaParcelas = new ArrayList<ContaAPagar>();
	}

	public void novoFiltro() {
		this.filtro = new CompraCabFilter();
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
			this.compraCab.setAcrDesc(n3);
			n4 = (n3.divide(n2, MathContext.DECIMAL128)).multiply(new BigDecimal(100)).setScale(3,
					RoundingMode.HALF_EVEN);
			this.compraCab.setVlrEmPerc(n4);
		}

		this.setTotalAParcelar(n);

	}

	public void editarCompra() {
		RequestContext req = RequestContext.getCurrentInstance();
		List<ContaAPagar> cps = contaAPagarService.porVinculo(this.compraCabSelecionado.getVinculo());
		for (ContaAPagar c : cps) {
			if (!c.getStatus().contains("ABERTO")) {
				throw new NegocioException("Não é permitido a editar este documento: "
						+ this.compraCabSelecionado.getDocumento() + ", pois o mesmo possui baixa.");
			}
		}

		this.compraCab = this.compraCabSelecionado;
		enderecoFornecedor();
		this.listaDeItens = this.compraCabSelecionado.getItens();
		this.cabContaApagar = this.cabContaApagarService.porVinculo(this.compraCabSelecionado.getVinculo());
		this.listaParcelas = this.cabContaApagar.getListaContaAPagars();
		this.totalAParcelar = this.compraCabSelecionado.getValorNota();
		this.compraDet = new CompraDet();
		this.parcela = new ContaAPagar();
		this.compraDet.setTotalDeItensGeral(calcularTotalItens());
		this.parcela.setTotalGeralDeParcelas(recalcularParcela());

		req.execute("PF('dialogCompra').show();");

	}

	public void excluirCompra() {
		List<ContaAPagar> cps = contaAPagarService.porVinculo(this.compraCabSelecionado.getVinculo());
		for (ContaAPagar c : cps) {
			if (!c.getStatus().contains("ABERTO")) {
				throw new NegocioException("Não é permitido excluir o documento: "
						+ this.compraCabSelecionado.getDocumento() + ", pois o mesmo possui baixar.");
			}
		}

		this.compraCabService.excluir(this.compraCabSelecionado);
		pesquisar();
		Messages.addGlobalInfo("Registro excluido com sucesso.");
	}

	public void calcValorCusto() {
		BigDecimal perc = this.compraCab.getVlrEmPerc();
		BigDecimal soma = BigDecimal.ZERO;
		BigDecimal dif = BigDecimal.ZERO;
		BigDecimal resultado = BigDecimal.ZERO;

		if (!compraDet.getQuantidade().equals(null) && compraDet.getQuantidade().compareTo(BigDecimal.ZERO) > 0) {
			dif = (compraDet.getValorTotal().multiply(perc)).divide(new BigDecimal(100)).setScale(2,
					RoundingMode.HALF_UP);
			soma = dif.add(compraDet.getValorTotal()).setScale(2, RoundingMode.HALF_UP);
			compraDet.setValorTotalLiquido(soma);
			resultado = soma.divide(compraDet.getQuantidade(), 2, RoundingMode.HALF_UP);
			compraDet.setValorUnitario(resultado);
			compraDet.setAcrDesc(dif);
		}
	}

	public void consultarNota() {
		if (StringUtils.isNotBlank(this.compraCab.getDocumento())) {
			CompraCab cab = compraCabService.porDocumento(this.compraCab.getDocumento());
			if (null != cab) {
				if (cab.getFornecedor().equals(this.compraCab.getFornecedor())
						&& cab.getDocumento().equals(this.compraCab.getDocumento())
						&& extrairData(cab.getDataEmissao()).equals(extrairData(this.compraCab.getDataEmissao()))) {
					Messages.addGlobalInfo("Nota já lançanda para esse fornecedor por favor verifique!");
				}
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
		this.compraDet.setCompraCab(this.compraCab);
		this.compraDet.setVinculo(this.compraCab.getVinculo());
		this.listaDeItens.add(0, this.compraDet);
		this.compraDet = new CompraDet();
		this.compraDet.setTotalDeItensGeral(calcularTotalItens());
	}

	public void salvarEdicaoItem() {
		calcValorCusto();
		this.compraDet = new CompraDet();
		this.compraDet.setTotalDeItensGeral(calcularTotalItens());
	}

	public void removerItem() {
		this.listaDeItens.remove(this.compraDet);
		this.compraDet = new CompraDet();
		this.compraDet.setTotalDeItensGeral(calcularTotalItens());

		if (this.listaDeItens.size() == 0) {
			this.listaParcelas.clear();
			this.parcela.setTotalGeralDeParcelas(recalcularParcela());
		}
	}

	public void abrirEdicaoItem() {
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

	public void salvar() {
		if (recalcularParcela().compareTo(BigDecimal.ZERO) == 0) {
			throw new NegocioException("Lançamento incompleto por favor verifique!");
		}

		if (this.compraCab.getValorNota().compareTo(calcularTotalItens()) != 0) {
			throw new NegocioException("Valor dos produtos diferente do valor da nota por favor verifique!");
		}

		if (this.compraCab.getValorNota().compareTo(recalcularParcela()) != 0) {
			throw new NegocioException("Valor das faturas diferente do valor da nota por favor verifique!");
		}

		this.compraCab.setItens(this.listaDeItens);
		compraCabService.salvar(this.compraCab);
		this.cabContaApagar.setListaContaAPagars(this.listaParcelas);
		cabContaApagarService.salvar(this.cabContaApagar);

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		Messages.addGlobalInfo("Registro salvor com sucesso!");
	}

	public BigDecimal recalcularParcela() {
		BigDecimal r = BigDecimal.ZERO;
		for (ContaAPagar par : this.listaParcelas) {
			r = r.add(par.getValor());
		}
		return r;
	}

	public BigDecimal calcularTotalItens() {
		BigDecimal t = BigDecimal.ZERO;
		for (CompraDet item : listaDeItens) {
			t = t.add(item.getValorTotalLiquido());
		}
		return t;
	}

	public Boolean validarDatas(Date ini, Date fim) {
		if (ini != null && fim != null) {
			if (fim.before(ini)) {
				return true;
			}
		}
		return false;
	}

	public List<TipoCobranca> getListaTipoCobrancas() {
		return Arrays.asList(TipoCobranca.values());
	}

	public void gerarParcelas() {

		BigDecimal qtde_parcela = new BigDecimal(this.parcela.getNumVezes());
		BigDecimal valorParcela = this.getTotalAParcelar().divide(qtde_parcela, 1, RoundingMode.CEILING);
		BigDecimal valorParcial = valorParcela.multiply(qtde_parcela.subtract(new BigDecimal(1)));
		BigDecimal primeiraParcela = this.getTotalAParcelar().subtract(valorParcial);
		this.cabContaApagar.setDataDoc(this.compraCab.getDataEntrada());
		this.cabContaApagar.setDataLanc(this.compraCab.getDataEntrada());
		this.cabContaApagar.setDocumento(this.compraCab.getDocumento());
		this.cabContaApagar.setValor(this.compraCab.getValorNota());
		this.cabContaApagar.setFornecedor(this.compraCab.getFornecedor());
		this.cabContaApagar.setUsuario(this.compraCab.getUsuario());
		this.cabContaApagar.setVinculo(this.compraCab.getVinculo());

		this.listaParcelas.clear();
		for (int i = 0; i < this.parcela.getNumVezes(); i++) {
			ContaAPagar ap = new ContaAPagar();
			ap.setTipoCobranca(this.parcela.getTipoCobranca());
			ap.setParcela((i + 1) + "/" + this.parcela.getNumVezes());
			ap.setNumDoc(this.compraCab.getDocumento());
			ap.setDataDoc(this.compraCab.getDataEntrada());
			ap.setStatus("ABERTO");
			ap.setDataVencto(i == 0 ? somaDias(this.compraCab.getDataEntrada(), 30)
					: somaDias(this.compraCab.getDataEntrada(), this.parcela.getPeriodo() * (i + 1)));
			ap.setValor(i == 0 ? primeiraParcela : valorParcela);
			ap.setValorApagar(i == 0 ? primeiraParcela : valorParcela);
			ap.setUsuario(this.compraCab.getUsuario());
			ap.setFornecedor(this.compraCab.getFornecedor());
			ap.setAgrupadorMovimentacao(this.compraCab.getVinculo());
			this.listaParcelas.add(ap);
		}

		this.cabContaApagar.setDataVencto(this.listaParcelas.get(0).getDataVencto());
		this.parcela.setTotalGeralDeParcelas(this.totalAParcelar);
	}

	public Date somaDias(Date data, int dias) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, dias);
		return cal.getTime();
	}

	private Date extrairData(Date data) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return format.parse(format.format(data));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void rowSelect(SelectEvent event) {
		editar();

	}

	public void rowSelectCheckBox(SelectEvent event) {
		editar();
	}

	private void editar() {
		// TODO Auto-generated method stub

	}

	public void rowUnSelect(UnselectEvent event) {
		editar();
	}

	public void onRowSelectAll(ToggleSelectEvent event) {
	}

	public void rowToggleSelect() {
		if (listaDeCompraCabSelecionadas.size() > 0) {
			isToggle = true;
		} else {
			isToggle = false;
		}
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

	public BigDecimal getTotalAParcelar() {
		return totalAParcelar;
	}

	public void setTotalAParcelar(BigDecimal totalAParcelar) {
		this.totalAParcelar = totalAParcelar;
	}

	public List<CompraCab> getListaDeCompraCab() {
		return listaDeCompraCab;
	}

	public void setListaDeCompraCab(List<CompraCab> listaDeCompraCab) {
		this.listaDeCompraCab = listaDeCompraCab;
	}

	public List<CompraCab> getListaDeCompraCabSelecionadas() {
		return listaDeCompraCabSelecionadas;
	}

	public void setListaDeCompraCabSelecionadas(List<CompraCab> listaDeCompraCabSelecionadas) {
		this.listaDeCompraCabSelecionadas = listaDeCompraCabSelecionadas;
	}

	public boolean isToggle() {
		return isToggle;
	}

	public void setToggle(boolean isToggle) {
		this.isToggle = isToggle;
	}

	public CompraCab getCompraCabSelecionado() {
		return compraCabSelecionado;
	}

	public void setCompraCabSelecionado(CompraCab compraCabSelecionado) {
		this.compraCabSelecionado = compraCabSelecionado;
	}

	public String getEdicao() {
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public List<Deposito> getListaDeDepositos() {
		return listaDeDepositos;
	}

	public void setListaDeDepositos(List<Deposito> listaDeDepositos) {
		this.listaDeDepositos = listaDeDepositos;
	}

}

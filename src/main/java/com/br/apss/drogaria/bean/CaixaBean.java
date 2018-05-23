package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.model.Caixa;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.filter.CaixaFilter;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.service.CaixaService;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.validadors.ValidarDataInicialFinal;


@Named
@ViewScoped
public class CaixaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/********** Atributos **********/

	private CaixaFilter filtro;
	private Caixa caixa;
	private Caixa caixaSelecionado;
	private Long id;

	private List<Caixa> caixas;

	@Inject
	CaixaService caixaService;

	@Inject
	PessoaService pessoaService;

	/********** Metodos **********/

	public CaixaBean() {
		filtro = new CaixaFilter();
		caixa = new Caixa();
		caixas = new ArrayList<Caixa>();
	}

	public void excluir() {
		caixaService.excluir(caixaSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso!");
		caixaSelecionado = null;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public List<Caixa> getCaixas() {
		return caixas;
	}

	public Caixa getCaixaSelecionado() {
		return caixaSelecionado;
	}

	public CaixaFilter getFiltro() {
		return filtro;
	}

	public Long getId() {
		return id;
	}

	public List<Pessoa> getResponsaveis() {
		PessoaFilter pfiltro = new PessoaFilter();
		pfiltro.setCliente(false);
		pfiltro.setFuncionario(true);
		pfiltro.setFornecedor(false);
		return pessoaService.filtrados(pfiltro);
	}

	public Status[] getStatus() {
		return Status.values();
	}

	public String handleClose(CloseEvent event) {
		return "";
	}

	public void iniciar() {
		if (null != id) {
			this.caixa = caixaService.porId(id);
		} else {
			this.caixa = new Caixa();
		}
	}

	public void novoCaixa() {
		caixa = new Caixa();
	}

	public void novoFiltro() {
		filtro = new CaixaFilter();
	}

	public void pesquisar() {
		if (null != filtro.getData() && null != filtro.getInicial() && null != filtro.getFim()) {
			Messages.addGlobalError("Informe apenas um período ou uma data unica.");
		} else {
			if (null != filtro.getInicial() && null != filtro.getFim()) {
				if (ValidarDataInicialFinal.validateBeginEndDate(filtro.getInicial(), filtro.getFim())) {
					Messages.addGlobalError("Data final maior que a data inicial.");
				} else {
					caixas = caixaService.filtrados(filtro);
				}
			} else {
				caixas = caixaService.filtrados(filtro);
			}
		}
		caixaSelecionado = null;
	}

	public void salvar() {
		Caixa caixaExistente = caixaService.consultarCaixa(caixa);
		if (caixaExistente != null && !caixaExistente.equals(caixa)) {
			Messages.addGlobalError("Já existe caixa aberto com essas informações.");
		} else {
			this.caixa.setAbertura(caixa.getData());
			this.caixa.setStatus("ABERTO");
			caixaService.salvar(this.caixa);
			pesquisar();
			caixa = new Caixa();
			Messages.addGlobalInfo("Registro salvor com sucesso!");
			RequestContext request = RequestContext.getCurrentInstance();
			request.addCallbackParam("sucesso", true);
		}
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	public void setCaixaSelecionado(Caixa caixaSelecionado) {
		this.caixaSelecionado = caixaSelecionado;
	}

	public void setFiltro(CaixaFilter filtro) {
		this.filtro = filtro;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

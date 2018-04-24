package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import com.br.apss.drogaria.model.CompraCab;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.model.filter.CompraCabFilter;
import com.br.apss.drogaria.service.PessoaService;

@Named
@ViewScoped
public class CompraCabBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private CompraCab compraCab;

	private List<Pessoa> listaDeFornecedores = new ArrayList<Pessoa>();

	private CompraCabFilter filtro;

	@Inject
	private PessoaService pessoaService;

	@PostConstruct
	public void inicializar() {
		filtro = new CompraCabFilter();
		carregarListaDeFornecedores();
	}

	private void carregarListaDeFornecedores() {
		this.listaDeFornecedores = pessoaService.listarFornecedore();
	}

	public void enderecoFornecedor() {
		this.compraCab.getFornecedor().setEndereco(null);
		this.compraCab.getFornecedor().setEndereco("Cpf/Cnpj: " + this.compraCab.getFornecedor().getCpfCnpj() + "\n"
				+ this.compraCab.getFornecedor().getEndereco() + ", " + this.compraCab.getFornecedor().getNum());
	}

	public void novo() {
		this.compraCab = new CompraCab();
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

}

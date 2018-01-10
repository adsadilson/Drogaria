package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import com.br.apss.drogaria.model.Categoria;
import com.br.apss.drogaria.model.SubCategoria;
import com.br.apss.drogaria.model.filter.SubCategoriaFilter;
import com.br.apss.drogaria.service.CategoriaService;
import com.br.apss.drogaria.service.SubCategoriaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class SubCategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private SubCategoria subCategoria = new SubCategoria();

	private SubCategoria subCategoriaSelecionado;

	private SubCategoriaFilter filtro = new SubCategoriaFilter();

	private List<SubCategoria> listaSubCategorias = new ArrayList<SubCategoria>();

	@Inject
	private SubCategoriaService subCategoriaService;
	
	@Inject
	private CategoriaService categoriaService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.subCategoria == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		SubCategoria subCategoriaExistente = subCategoriaService.porNome(subCategoria.getNome());
		if (subCategoriaExistente != null && !subCategoriaExistente.equals(subCategoria)) {
			throw new NegocioException("Já existe um registro com essa nome informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		subCategoriaService.salvar(subCategoria);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.subCategoria = subCategoriaService.porId(this.subCategoriaSelecionado.getId());
	}

	public void novo() {
		subCategoria = new SubCategoria();
	}

	public void novoFiltro() {
		this.filtro = new SubCategoriaFilter();
	}

	public void pesquisar() {
		this.listaSubCategorias = subCategoriaService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.subCategoria = subCategoriaService.porId(this.subCategoriaSelecionado.getId());
	}

	public void excluir() {
		subCategoriaService.excluir(subCategoriaSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}
	
	public List<Categoria> getCategorias(){
		return categoriaService.filtrados(null);
	}
	

	/******************** Getters e Setters ***************************/

	public SubCategoriaFilter getFiltro() {
		return filtro;
	}

	public SubCategoria getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(SubCategoria subCategoria) {
		this.subCategoria = subCategoria;
	}

	public void setFiltro(SubCategoriaFilter filtro) {
		this.filtro = filtro;
	}

	public List<SubCategoria> getListaSubCategorias() {
		return listaSubCategorias;
	}

	public void setListaSubCategorias(List<SubCategoria> listaSubCategorias) {
		this.listaSubCategorias = listaSubCategorias;
	}

	public SubCategoria getSubCategoriaSelecionado() {
		return subCategoriaSelecionado;
	}

	public void setSubCategoriaSelecionado(SubCategoria subCategoriaSelecionado) {
		this.subCategoriaSelecionado = subCategoriaSelecionado;
	}

}

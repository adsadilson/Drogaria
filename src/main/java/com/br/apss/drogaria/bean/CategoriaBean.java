package com.br.apss.drogaria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import com.br.apss.drogaria.enums.Status;
import com.br.apss.drogaria.model.Categoria;
import com.br.apss.drogaria.model.filter.CategoriaFilter;
import com.br.apss.drogaria.service.CategoriaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class CategoriaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Categoria categoria = new Categoria();

	private Categoria categoriaSelecionado;

	private CategoriaFilter filtro = new CategoriaFilter();

	private List<Categoria> listaCategorias = new ArrayList<Categoria>();

	@Inject
	private CategoriaService categoriaService;

	/******************** Metodos ***********************/

	public void inicializar() {
		if (this.categoria == null) {
			novo();
		}
		pesquisar();
	}

	public void salvar() {

		Categoria categoriaExistente = categoriaService.porNome(categoria.getNome());
		if (categoriaExistente != null && !categoriaExistente.equals(categoria)) {
			throw new NegocioException("Já existe um registro com essa nome informado.");
		}

		RequestContext request = RequestContext.getCurrentInstance();
		request.addCallbackParam("sucesso", true);
		categoriaService.salvar(categoria);
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		pesquisar();
	}

	public void editar() {
		this.categoria = categoriaService.porId(this.categoriaSelecionado.getId());
	}

	public void novo() {
		categoria = new Categoria();
	}

	public void novoFiltro() {
		this.filtro = new CategoriaFilter();
	}

	public void pesquisar() {
		this.listaCategorias = categoriaService.filtrados(this.filtro);
	}

	public void preparEdicao() {
		this.categoria = categoriaService.porId(this.categoriaSelecionado.getId());
	}

	public void excluir() {
		categoriaService.excluir(categoriaSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
		pesquisar();
	}
	
	public List<Status> getStatus(){
		return Arrays.asList(Status.values());
	}

	/******************** Getters e Setters ***************************/

	public CategoriaFilter getFiltro() {
		return filtro;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void setFiltro(CategoriaFilter filtro) {
		this.filtro = filtro;
	}

	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}

	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	public Categoria getCategoriaSelecionado() {
		return categoriaSelecionado;
	}

	public void setCategoriaSelecionado(Categoria categoriaSelecionado) {
		this.categoriaSelecionado = categoriaSelecionado;
	}

}

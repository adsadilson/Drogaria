package com.br.apss.drogaria.bean;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.br.apss.drogaria.enums.Estado;
import com.br.apss.drogaria.enums.EstadoCivil;
import com.br.apss.drogaria.enums.Sexo;
import com.br.apss.drogaria.enums.TipoPessoa;
import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.model.filter.PessoaFilter;
import com.br.apss.drogaria.service.PessoaService;
import com.br.apss.drogaria.util.jsf.NegocioException;

@Named
@ViewScoped
public class EmpresaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa empresa = new Pessoa();

	private Pessoa empresaSelecionado;

	private PessoaFilter filtro = new PessoaFilter();

	private List<Pessoa> listaEmpresas;

	private int qtdRegistro = 0;

	private StreamedContent foto;

	@Inject
	private PessoaService empresaService;

	/******************** Metodos ***********************/

	public void inicializar() {

		listaEmpresas = empresaService.listarEmpresa();

		if (listaEmpresas.size() > 0) {

			empresa.setId(listaEmpresas.get(0).getId());
			empresa.setTipoPessoa(listaEmpresas.get(0).getTipoPessoa());
			empresa.setCpfCnpj(listaEmpresas.get(0).getCpfCnpj());
			empresa.setNome(listaEmpresas.get(0).getNome());
			empresa.setRgInsc(listaEmpresas.get(0).getRgInsc());
			empresa.setNascimento(listaEmpresas.get(0).getNascimento());
			empresa.setMae(listaEmpresas.get(0).getMae());
			empresa.setPai(listaEmpresas.get(0).getPai());
			empresa.setConjuge(listaEmpresas.get(0).getConjuge());
			empresa.setCnae(listaEmpresas.get(0).getCnae());
			empresa.setInscMunicipal(listaEmpresas.get(0).getInscMunicipal());
			empresa.setFornecedor(listaEmpresas.get(0).getFornecedor());
			empresa.setCliente(listaEmpresas.get(0).getCliente());

			empresa.setCep(listaEmpresas.get(0).getCep());
			empresa.setEndereco(listaEmpresas.get(0).getEndereco());
			empresa.setNum(listaEmpresas.get(0).getNum());
			empresa.setBairro(listaEmpresas.get(0).getBairro());
			empresa.setComplemento(listaEmpresas.get(0).getComplemento());
			empresa.setCidade(listaEmpresas.get(0).getCidade());
			empresa.setEstado(listaEmpresas.get(0).getEstado());

			empresa.setTelefone(listaEmpresas.get(0).getTelefone());
			empresa.setTelefone2(listaEmpresas.get(0).getTelefone2());
			empresa.setCelular(listaEmpresas.get(0).getCelular());
			empresa.setContato1(listaEmpresas.get(0).getContato1());
			empresa.setContato2(listaEmpresas.get(0).getContato2());

			empresa.setEmail(listaEmpresas.get(0).getEmail());
			empresa.setSite(listaEmpresas.get(0).getSite());
			empresa.setCaminhoLogo("C:/APSSystem/uploads/logo.png");
			ImagemBean.caminho = empresa.getCaminhoLogo();

			empresa.setObs(listaEmpresas.get(0).getObs());
		}
	}

	public void fileUploadListener(FileUploadEvent e) {
		try {
			// Pegar o arquivo temporario da memoria
			UploadedFile arquivoUpload = e.getFile();
			// Criar uma arquivo temporario no sistema operacional
			Path arquivoTemp = Files.createTempFile(null, null);
			// Copia o arquivo da memoria para o arquivo temporario
			Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
			empresa.setCaminhoLogo(arquivoTemp.toString());
			ImagemBean.caminho = empresa.getCaminhoLogo();
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new NegocioException("Ocorreu um erro ao tentar realizar  o upload do logotipo.");
		}
	}

	public void salvar() {
		empresa.setEmpresa(true);
		empresaService.salvar(empresa);
		copiarImagem(empresa.getCaminhoLogo(), "C:/APSSystem/uploads/", "logo");
		Messages.addGlobalInfo("Registro salvor com sucesso.");
		inicializar();
	}

	public void copiarImagem(String origem, String destino, String nome) {
		try {
			Path caminhoOrigem = Paths.get(origem);
			Path caminhoDestino = Paths.get(destino + "" + nome + ".png");
			Files.copy(caminhoOrigem, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void consultarEmpresaPorCpf() {
		Pessoa empresaExistente = empresaService.porCpf(empresa.getCpfCnpj());
		if (empresaExistente != null) {
			empresa = empresaExistente;
		}
	}

	public void editar() {
		this.empresa = empresaService.porId(this.empresaSelecionado.getId());
	}

	public void novo() {
		empresa = new Pessoa();
	}

	public void novoFiltro() {
		this.filtro = new PessoaFilter();
	}

	public void preparEdicao() {
		this.empresa = empresaService.porId(this.empresaSelecionado.getId());
	}

	public void excluir() {
		empresaService.excluir(empresaSelecionado);
		Messages.addGlobalInfo("Registro excluido com sucesso.");
	}

	public List<TipoPessoa> getlistaTipoPessoas() {
		return Arrays.asList(TipoPessoa.values());
	}

	public List<Sexo> getlistaSexos() {
		return Arrays.asList(Sexo.values());
	}

	public List<EstadoCivil> getlistaEstadoCivis() {
		return Arrays.asList(EstadoCivil.values());
	}

	public List<Estado> getlistaEstados() {
		return Arrays.asList(Estado.values());
	}

	/******************** Getters e Setters ***************************/

	public PessoaFilter getFiltro() {
		return filtro;
	}

	public Pessoa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Pessoa empresa) {
		this.empresa = empresa;
	}

	public void setFiltro(PessoaFilter filtro) {
		this.filtro = filtro;
	}

	public List<Pessoa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Pessoa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public Pessoa getEmpresaSelecionado() {
		return empresaSelecionado;
	}

	public void setEmpresaSelecionado(Pessoa empresaSelecionado) {
		this.empresaSelecionado = empresaSelecionado;
	}

	public int getQtdRegistro() {
		return qtdRegistro;
	}

	public void setQtdRegistro(int qtdRegistro) {
		this.qtdRegistro = qtdRegistro;
	}

	public StreamedContent getFoto() {
		return foto;
	}

	public void setFoto(StreamedContent foto) {
		this.foto = foto;
	}

}

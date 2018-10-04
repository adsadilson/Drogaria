package com.br.apss.drogaria.relatorio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.br.apss.drogaria.model.Pessoa;
import com.br.apss.drogaria.service.PessoaService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Relatorio implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Pessoa> listaEmpresas;

	private Pessoa empresa = new Pessoa();

	@Inject
	private HttpServletResponse response;

	@Inject
	private FacesContext context;

	@Inject
	private PessoaService empresaService;

	private ByteArrayOutputStream baos;

	private InputStream stream;

	public Relatorio() {
		baos = new ByteArrayOutputStream();
	}

	public void gerarRelatorio(String caminho, String nomeArquivoSaida, Map<String, Object> parms,
			Collection<?> clazz) {

		try {

			listaEmpresas = empresaService.listarEmpresa();

			if (listaEmpresas.size() > 0) {

				empresa.setId(listaEmpresas.get(0).getId());
				empresa.setCpfCnpj(listaEmpresas.get(0).getCpfCnpj());
				empresa.setNome(listaEmpresas.get(0).getNome());

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
				empresa.setCaminhoLogo("C:/APSSystem/uploads/logo.png");

			}

			// seta a configuraçao padrao no formato brasil
			parms.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

			parms.put("emp_nome", empresa.getNome());
			parms.put("emp_logo", empresa.getCaminhoLogo());
			parms.put("emp_end1", empresa.getEndereco() + ", " + empresa.getNum());
			parms.put("emp_end2", empresa.getBairro() + " - " + empresa.getCidade() + " - " + empresa.getEstado()
					+ "    CEP: " + empresa.getCep());
			parms.put("emp_end3", "CNPJ: " + empresa.getCpfCnpj() + "    " + empresa.getEmail());

			// pegar o caminho na memoria
			stream = this.getClass().getResourceAsStream(caminho);

			// copilar o arquivo recebido
			JasperReport report = JasperCompileManager.compileReport(stream);

			// preencher o relatorio com os dados
			JasperPrint print = JasperFillManager.fillReport(report, parms, new JRBeanCollectionDataSource(clazz));

			// exporta o relatorio em pdf para ByteArrayOutputStream()
			JasperExportManager.exportReportToPdfStream(print, baos);

			response.reset();
			// seta o conteudo que vai no response
			response.setContentType("application/pdf");
			// seta o tamanho do arquivo
			response.setContentLength(baos.size());
			// Código abaixo gerar o relatório e disponibiliza diretamente na página ou
			// "attachment para fazer o download
			response.setHeader("Content-Disposition", "inline; filename=" + nomeArquivoSaida + ".pdf");
			// escrever os dados do baos
			//response.sendRedirect("_blank");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();

			context.responseComplete();

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Pessoa> getListaEmpresas() {
		return listaEmpresas;
	}

	public void setListaEmpresas(List<Pessoa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	public Pessoa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Pessoa empresa) {
		this.empresa = empresa;
	}

}

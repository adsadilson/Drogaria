package com.br.apss.drogaria.relatorio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Relatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletResponse response;

	@Inject
	private FacesContext context;

	private ByteArrayOutputStream baos;

	private InputStream stream;

	public Relatorio() {
		baos = new ByteArrayOutputStream();
	}

	public void gerarRelatorio(String caminho, String nomeArquivoSaida, Map<String, Object> parms,
			Collection<?> clazz) {

		try {

			// seta a configuraçao padrao no formato brasil
			parms.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));

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
}

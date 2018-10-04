package com.br.apss.drogaria.bean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("imagem")
@SessionScoped
public class ImagemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static StreamedContent foto;

	public static String caminho;

	public StreamedContent getFoto() throws FileNotFoundException {
		if (caminho == null || caminho.isEmpty()) {
			InputStream stream = this.getClass().getResourceAsStream("/images/branco.png");
			foto = new DefaultStreamedContent(stream);
		} else {
			InputStream stream = new FileInputStream(caminho);
			foto = new DefaultStreamedContent(stream);
		}
		return foto;
	}

	public void setFoto(StreamedContent foto) {
		ImagemBean.foto = foto;
	}

}

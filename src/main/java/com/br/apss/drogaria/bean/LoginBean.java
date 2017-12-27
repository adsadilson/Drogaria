package com.br.apss.drogaria.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.service.UsuarioService;

@Named
@ApplicationScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();

	private Usuario usuarioLogado;

	@Inject
	private UsuarioService usuarioService;

	/******************** Metodos ***********************/

	public void autenticar() {

		SimpleHash hash = new SimpleHash("md5", this.usuario.getSenha());
		usuarioLogado = usuarioService.autenticacao(this.usuario.getEmail(), hash.toHex());

		if (null != usuarioLogado) {
			try {
				Faces.redirect("./");
			} catch (IOException e) {
				e.printStackTrace();
				Messages.addGlobalError(e.getMessage());
			}
		} else {
			Messages.addGlobalInfo("Login ou Senha inválida.");
		}

	}

	/******************** Getters e Setters ***************************/

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

}

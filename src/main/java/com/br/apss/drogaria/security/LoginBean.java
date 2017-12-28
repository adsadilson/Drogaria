package com.br.apss.drogaria.security;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.br.apss.drogaria.model.GrupoUsuario;
import com.br.apss.drogaria.model.Permissao;
import com.br.apss.drogaria.model.Usuario;
import com.br.apss.drogaria.service.UsuarioService;

@Named
@SessionScoped
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
		
		for (GrupoUsuario g : usuarioLogado.getGrupos()) {
			for (Permissao p : g.getPermissoes()) {
				System.out.println(p.getControleMenu().getFuncao());
			}
		}

		if (null != usuarioLogado) {
			try {
				
				HttpSession session;
				FacesContext ctx = FacesContext.getCurrentInstance();
				session = (HttpSession) ctx.getExternalContext().getSession(false);
				
				session.setAttribute("usuarioAutenticado", usuarioLogado);
				Faces.redirect("./");
			} catch (IOException e) {
				e.printStackTrace();
				Messages.addGlobalError(e.getMessage());
			}
		} else {
			Messages.addGlobalInfo("Login ou Senha inváida.");
		}

	}
	
	public String logout() {
		HttpSession session;
		FacesContext ctx = FacesContext.getCurrentInstance();
		session = (HttpSession) ctx.getExternalContext().getSession(false);
		session.setAttribute("usuarioAutenticado", null);
		Enumeration<String> vals = session.getAttributeNames();
		while (vals.hasMoreElements()) {
			session.removeAttribute(vals.nextElement());
		}
		return "/login?faces-redirect=true";

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
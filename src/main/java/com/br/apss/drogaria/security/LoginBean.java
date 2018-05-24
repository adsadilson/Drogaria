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
import com.br.apss.drogaria.service.CaixaService;
import com.br.apss.drogaria.service.UsuarioService;
import com.br.apss.drogaria.util.jpa.FlyWay;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario = new Usuario();

	private Usuario usuarioLogado;

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private CaixaService caixaService;

	@SuppressWarnings("unused")
	private FlyWay flyWay = new FlyWay();

	/******************** Metodos ***********************/

	public LoginBean() {
		// flyWay.uploadBaseDado();
	}

	@SuppressWarnings("unused")
	public void autenticar() {

		SimpleHash hash = new SimpleHash("md5", this.usuario.getSenha());
		usuarioLogado = usuarioService.autenticacao(this.usuario.getEmail(), hash.toHex());

		if (null != usuarioLogado) {
			try {

				HttpSession session;
				FacesContext ctx = FacesContext.getCurrentInstance();
				session = (HttpSession) ctx.getExternalContext().getSession(false);
				session.setAttribute("usuarioAutenticado", usuarioLogado);

				Boolean v = false;
				for (GrupoUsuario g : usuarioLogado.getGrupos()) {
					for (Permissao p : g.getPermissoes()) {
						/*
						 * if(p.getControleMenu().getFuncao().equals("OPERADOR DE CAIXA")) {
						 * System.out.println(p.getControleMenu().getFuncao()); break; }
						 */
						if (g.getNome().contains("OPERADOR DE CAIXA")) {
							v = true;
							break;
						}
					}
				}

				if (v == true) {
					Faces.redirect("/drogaria/homeCX.xhtml");
				} else {
					Faces.redirect("./");
				}

			} catch (IOException e) {
				e.printStackTrace();
				Messages.addGlobalError(e.getMessage());
			}
		} else {
			Messages.addGlobalInfo("Login ou Senha inválida.");
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
